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

import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to store and load FilterModel from the persistent storage.
 * <p/>
 * Todo: Refactoring: Load built-in filter models directly into memory instead of persisting on HD.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class FilterFactory
{
    private static final String FILTER_LOCATION = System.getProperty( "herbar.filter.location" );

    private static final Logger LOG = LoggerFactory.getLogger( FilterFactory.class );

    private static FilterFactory instance = null;

    private final HerbarModel model = Application.getInstance().getModel();

    private final Map<String, FilterModel> cachedFilterModels = new HashMap<String, FilterModel>();

    /** Creates a new instance of FilterFactory. */
    protected FilterFactory()
    {
        try
        {
            LOG.info( "reading mapping file for filter definition." );

            final String[] lists;
            if ( AppHerbar.ENV_SCIENTIFIC.equals( System.getProperty( "xmatrix.subject" ) ) )
            {
                lists = new String[]{ "Liste 60", "Liste 200", "Liste 400", "Liste 600", "All" };
            }
            else
            {
                lists = new String[]{ };
            }

            if ( new File( System.getProperty( "herbar.filter.location" ) ).mkdirs() )
            {
                LOG.info( "created directory for filters" );
            }

            for ( final String list : lists )
            {
                final FileWriter writer = new FileWriter( generateFilterFileName( list ) );
                IOUtils.copy( getClass().getResourceAsStream( list + ".xml" ), writer );
                writer.close();
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

    private Level[] collectLevels( final String[] levelNames )
    {
        Level[] levels = new Level[0];
        if ( levelNames != null )
        {
            levels = new Level[levelNames.length];
            for ( int i = 0; i < levelNames.length; i++ )
            {
                levels[i] = model.getLevel( levelNames[i] );
            }
        }
        else
        {
            LOG.warn( "filter has no levels" );
        }
        return levels;
    }

    private FilterModel generateFilterModel( final Filter filter ) throws FilterPersistentException
    {
        final String baseModelName = filter.getBaseFilterName();
        final HerbarModel baseModel = getBaseFilterModel( filter, baseModelName );
        final boolean fixed = filter.getFixed();
        final FilterModel model = new FilterModel( baseModel, filter.getName(), fixed );
        model.clearFilterDetails();

        final Detail[] details = filter.getDetails();
        for ( int i = 0; details != null && i < details.length; i++ )
        {
            final Detail detail = details[i];
            final Taxon scope = baseModel.getTaxon( detail.getScope() );
            final Level[] levels = collectLevels( detail.getLevels() );
            if ( scope != null )
            {
                model.addFilterDetail( scope, levels );
            }
            else
            {
                LOG.warn( "cannot find scope: " + detail.getScope() + " in dependent model: " + baseModelName +
                        " for model: " + filter.getName() + " skip detail" );
            }
        }
        return model;
    }

    private HerbarModel getBaseFilterModel( final Filter filter, final String baseModelName ) throws FilterPersistentException
    {
        final HerbarModel baseModel;
        if ( !"".equals( filter.getBaseFilterName() ) )
        {
            baseModel = getFilterModel( baseModelName );
        }
        else
        {
            baseModel = Application.getInstance().getModel();
        }
        return baseModel;
    }

    /**
     * Return a filter definition from the persistent storage. It also load its dependent filters.
     * <p/>
     * This method makes sure the base model exists, otherwise it is reset to the default. It guarantees also that the
     * name of the model is equal to the name of the file.
     *
     * @param name the name of the filer
     * @return the loaded FilterModel
     * @throws FilterPersistentException is thrown if the load of the filter failed
     */
    public FilterModel getFilterModel( final String name ) throws FilterPersistentException
    {
        FilterModel model = cachedFilterModels.get( name );
        if ( model == null )
        {
            final Filter filter = loadFilter( name );
            // make sure renamed filter files are still consistent
            filter.setName( name );
            // make sure the base name exists
            final String base = filter.getBaseFilterName();
            boolean baseFilterFound = false;
            for ( Iterator<String> it = getFilterNames().iterator(); it.hasNext() && !baseFilterFound; )
            {
                final String filterNameToCompare = it.next();
                baseFilterFound = base.equals( filterNameToCompare );
            }
            if ( !baseFilterFound || base.equals( name ) )
            {
                filter.setBaseFilterName( "" );
            }
            // finally load model
            model = generateFilterModel( filter );
            cachedFilterModels.put( name, model );
        }
        return model;
    }

    /**
     * This method stores the FilterModel to the persistent storage.
     *
     * @param filterModel the FilerDefinition which should be stored
     * @throws FilterPersistentException is thrown if filter couldn't be stored
     */
    public void saveFilterModel( final FilterModel filterModel ) throws FilterPersistentException
    {
        // saving to a new file includes removing the old
        final Collection<FilterModel> values = cachedFilterModels.values();
        final Collection<String> keys = cachedFilterModels.keySet();
        final Iterator<String> keyIterator = keys.iterator();
        String modelName = null;
        for ( final FilterModel model : values )
        {
            final String name = keyIterator.next();
            if ( model == filterModel )
            {
                modelName = name;
                break;
            }
        }
        modelName = (modelName == null ? filterModel.getName() : modelName);
        cachedFilterModels.remove( modelName );
        final String filename = generateFilterFileName( modelName );
        final File file = new File( filename );
        final boolean success = file.delete();
        if ( !success )
        {
            LOG.error( "could not delete \"" + file + "\"" );
        }
        // save
        final Filter filter = new Filter( filterModel );
        saveFilter( filter );
        // save FilterModel to cached list, could be new one or is overwritten
        cachedFilterModels.put( filterModel.getName(), filterModel );
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
        final Collection<FilterModel> values = cachedFilterModels.values();
        final Collection<String> keys = cachedFilterModels.keySet();
        final Iterator<String> keyIterator = keys.iterator();
        String modelName = null;
        for ( final FilterModel model : values )
        {
            final String name = keyIterator.next();
            if ( model == filterModel )
            {
                modelName = name;
                break;
            }
        }
        modelName = (modelName == null ? filterModel.getName() : modelName);
        final String filename = generateFilterFileName( modelName );
        final File file = new File( filename );
        if ( !file.delete() )
        {
            throw new FilterPersistentException( "Failed to remove filter " + filename + " (" + file + ")" );
        }
        else
        {
            cachedFilterModels.remove( modelName );
        }
    }

    private String generateFilterFileName( final String name )
    {
        return FILTER_LOCATION + "/" + name + ".xml";
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
        final XStream x = getSerializer();
        final Filter filter = (Filter) x.fromXML( input );
        if ( filter.getBaseFilterName() == null )
        {
            filter.setBaseFilterName( "" );
        }
        return filter;
    }

    private void saveFilter( final Filter filter ) throws FilterPersistentException
    {
        final String filename = generateFilterFileName( filter.getName() );
        try
        {
            final Writer out = new BufferedWriter( new FileWriter( filename ) );
            getSerializer().toXML( filter, out );
            out.close();
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
        x.addImplicitCollection( Filter.class, "details" );
        x.addImplicitArray( Detail.class, "levels" );
        x.alias( "level", String.class );
        x.useAttributeFor( Filter.class, "fixed" );
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
        final Set<String> list = new HashSet<String>();
        if ( FILTER_LOCATION != null )
        {
            final File file = new File( FILTER_LOCATION );
            if ( file.exists() )
            {
                final File[] files = file.listFiles();
                for ( final File file1 : files != null ? files : new File[0] )
                {
                    LOG.trace( "filter with name " + file1.getName() + " found." );
                    list.add( generateFilterName( file1.getName() ) );
                }
            }
            else
            {
                LOG.info( "create filter path (" + FILTER_LOCATION + ")" );
                final boolean success = file.mkdirs();
                if ( !success )
                {
                    LOG.error( "could not create directory \"" + file + "\"" );
                }
            }
        }
        else
        {
            LOG.warn( "location of filter is not set (please check configuration file)" );
        }
        return list;
    }

    public Set<? extends HerbarModel> getFilters()
    {
        final Set<FilterModel> result = new HashSet<FilterModel>();
        final Set<String> names = getFilterNames();
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
        return result;
    }
}
