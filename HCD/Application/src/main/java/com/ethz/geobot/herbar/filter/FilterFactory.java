/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */

/*
 * FilterFactory.java
 *
 * Created on 21. August 2002, 15:15
 */
package com.ethz.geobot.herbar.filter;

import ch.jfactory.file.FileUtils;
import ch.jfactory.lang.ToStringComparator;
import com.ethz.geobot.herbar.Application;
import static com.ethz.geobot.herbar.modeapi.HerbarContext.ENV_SCIENTIFIC;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to store and load FilterModel from the persistent storage.
 * <p/>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class FilterFactory
{
    private static final String FILTER_LOCATION = System.getProperty( "herbar.filter.location" );

    private static final Logger LOG = LoggerFactory.getLogger( FilterFactory.class );

    private static FilterFactory instance = null;

    private final Map<String, FilterModel> nameModelCache = new HashMap<String, FilterModel>();

    /**
     * Creates a new instance of FilterFactory.
     */
    protected FilterFactory()
    {
        try
        {
            LOG.info( "reading mapping file for filter definition." );

            final String filePath = FILTER_LOCATION + "/../version.xml";
            final String version = "6.0";
            final String property = "version";
            final File filterDirectory = new File( System.getProperty( "herbar.filter.location" ) );
            if ( version.equals( FileUtils.readPropertyFromXML( filePath, property ) ) )
            {
                // Delete all filters
                org.apache.commons.io.FileUtils.deleteDirectory( filterDirectory );
                filterDirectory.mkdirs();

                // Write the version file
                FileUtils.writePropertyToXML( filePath, property, version );
            }
            final String[] lists = ENV_SCIENTIFIC.equals( System.getProperty( "xmatrix.subject" ) ) ? new String[]{"200 CH", "200 Leuchtmann", "200", "400", "400-200 ZH", "600 Zeiger", "600", "600-200 ZH", "600-400", "Alle Taxa", "PHARMBIO", "Alle Taxa"} : new String[]{"Alle Taxa"};
            filterDirectory.mkdirs();

            for ( final String list : lists )
            {
                final FileWriter writer = new FileWriter( generateFilterFileName( list ) );
                IOUtils.copy( getClass().getResourceAsStream( list + ".xml" ), writer );
                writer.close();
                final FilterModel model = generateFilterModel( loadFilter( list ) );
                nameModelCache.put( list, model );
            }
        }
        catch ( Exception ex )
        {
            LOG.error( "failed to load mapping file for filter definition.", ex );
        }
    }

    /**
     * This method returns the reference to the FilterFactory.
     *
     * @return the FilterFactory singleton
     */
    public static FilterFactory getInstance()
    {
        if ( instance == null )
        {
            instance = new FilterFactory();
        }
        return instance;
    }

    private FilterModel generateFilterModel( final Filter filter ) throws FilterPersistentException
    {
        final HerbarModel baseModel = Application.getInstance().getModel();
        final FilterModel model = new FilterModel( baseModel, filter.getName(), filter.getFixed(), filter.getRank() );
        final Detail[] details = filter.getDetails();
        for ( int i = 0; details != null && i < details.length; i++ )
        {
            final Detail detail = details[i];
            final Taxon scope = baseModel.getTaxon( detail.getScope() );
            if ( scope != null )
            {
                model.addFilterTaxon( scope );
            }
            else
            {
                LOG.warn( "cannot find scope  \"" + detail.getScope() + "\" for model \"" + filter.getName() + "\" - skipping detail" );
            }
        }
        return model;
    }

    /**
     * Return a filter definition from the persistent storage. It also load its dependent filters.
     * <p/>
     * This method makes sure the base model exists, otherwise it is reset to the default. It guarantees also that the
     * name of the model is equal to the name of the file.
     *
     * @param name the name of the filter file
     * @return the loaded FilterModel
     * @throws FilterPersistentException is thrown if the load of the filter failed
     */
    public FilterModel getFilterModel( final String name ) throws FilterPersistentException
    {
        FilterModel model = nameModelCache.get( name );
        if ( model == null )
        {
            model = generateFilterModel( loadFilter( name ) );
            nameModelCache.put( name, model );
        }
        return model;
    }

    /**
     * This method stores the FilterModel to the persistent storage.
     *
     * @param filterModel the FilterDefinition which should be stored
     * @throws FilterPersistentException is thrown if filter couldn't be stored
     */
    public void saveFilterModel( final FilterModel filterModel ) throws FilterPersistentException
    {
        // cleanup old model
        String modelName = null;
        for ( final String oldModelName : nameModelCache.keySet() )
        {
            final FilterModel oldModel = nameModelCache.get( oldModelName );
            if ( oldModel == filterModel )
            {
                modelName = oldModelName;
            }
        }
        nameModelCache.remove( modelName );
        new File( generateFilterFileName( modelName ) ).delete();

        // dump model to data object
        final Filter filter = new Filter();
        filter.setName( filterModel.getName() );
        filter.setFixed( filterModel.isFixed() );
        final Set<Detail> details = new HashSet<Detail>();
        final Level[] levels = filterModel.getRootTaxon().getSubLevels();
        for ( final Level level : levels )
        {
            final Taxon[] taxa = filterModel.getRootTaxon().getAllChildTaxa( level );
            for ( final Taxon taxon : taxa )
            {
                final Detail detail = new Detail();
                detail.setScope( taxon.getName() );
                details.add( detail );
            }
        }
        filter.setDetails( details.toArray( new Detail[details.size()] ) );

        // save new model
        saveFilter( filter );
        nameModelCache.put( filterModel.getName(), filterModel );
    }

    /**
     * This method remove a FilterModel from the persistent storage.
     *
     * @param filterModel the name of the filter
     * @throws FilterPersistentException is thrown if filter couldn't be deleted
     */
    public void removeFilterModel( final FilterModel filterModel ) throws FilterPersistentException
    {
        // make sure the right model is handled even if the name has changed.
        final String modelName = filterModel.getName();
        final String filename = generateFilterFileName( modelName );
        final File file = new File( filename );
        if ( !file.delete() )
        {
            throw new FilterPersistentException( "Failed to remove filter " + filename + " (" + file + ")" );
        }
        else
        {
            nameModelCache.remove( modelName );
        }
    }

    private String generateFilterFileName( final String name )
    {
        return FILTER_LOCATION + name + ".xml";
    }

    private String generateFilterName( final String filename )
    {
        return filename.substring( 0, filename.lastIndexOf( '.' ) );
    }

    private Filter loadFilter( final String name ) throws FilterPersistentException
    {
        final String filter = generateFilterFileName( name );
        try
        {
            return loadFilter( new FileInputStream( filter ) );
        }
        catch ( FileNotFoundException e )
        {
            final String msg = "cannot read \"" + filter + "\"";
            LOG.error( msg );
            throw new FilterPersistentException( msg, e );
        }
    }

    private Filter loadFilter( final InputStream input )
    {
        return (Filter) getSerializer().fromXML( input );
    }

    private void saveFilter( final Filter filter ) throws FilterPersistentException
    {
        final String filename = generateFilterFileName( filter.getName() );
        try
        {
            final Writer writer = new OutputStreamWriter( new FileOutputStream( filename ), Charset.forName( "UTF-8" ) );
            writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + System.getProperty( "line.separator" ) );
            getSerializer().toXML( filter, writer );
            writer.close();
        }
        catch ( Exception ex )
        {
            final String msg = "Failed to save filter " + filter.getName() + " (" + filename + ").";
            LOG.error( msg, ex );
            throw new FilterPersistentException( msg, ex );
        }
    }

    private XStream getSerializer()
    {
        final XStream x = new XStream();
        x.alias( "filter", Filter.class );
        x.alias( "detail", Detail.class );
        x.addImplicitCollection( Filter.class, "details", Detail.class );
        x.useAttributeFor( Filter.class, "fixed" );
        x.useAttributeFor( Filter.class, "rank" );
        x.useAttributeFor( Filter.class, "name" );
        x.useAttributeFor( Detail.class, "scope" );
        return x;
    }

    /**
     * This method is called to get all available filters.
     *
     * @return a set of filter names, represent by String objects
     */
    public Set<String> getFilterNames()
    {
        final Set<String> list = new TreeSet<String>( new ToStringComparator<String>() );
        if ( FILTER_LOCATION != null )
        {
            final File dir = new File( FILTER_LOCATION );
            if ( dir.exists() )
            {
                final File[] files = dir.listFiles();
                for ( final File file : files != null ? files : new File[0] )
                {
                    LOG.trace( "filter with name " + file.getName() + " found." );
                    list.add( generateFilterName( file.getName() ) );
                }
            }
            else
            {
                LOG.info( "create filter path (" + FILTER_LOCATION + ")" );
                final boolean success = dir.mkdirs();
                if ( !success )
                {
                    LOG.error( "could not create directory \"" + dir + "\"" );
                }
            }
        }
        else
        {
            LOG.warn( "location of filter is not set (please check configuration file)" );
        }
        return list;
    }

    public Collection<? extends HerbarModel> getFilters()
    {
        final List<FilterModel> result = new ArrayList<FilterModel>();
        final Collection<String> names = getFilterNames();
        for ( final String name : names )
        {
            try
            {
                result.add( getFilterModel( name ) );
            }
            catch ( FilterPersistentException e )
            {
                LOG.error( "failed to load filter " + name, e );
            }
        }
        Collections.sort( result, new Comparator<FilterModel>()
        {
            @Override
            public int compare( final FilterModel o1, final FilterModel o2 )
            {
                return o1.getRank() - o2.getRank();
            }
        } );
        return result;
    }
}
