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
package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.filter.Detail;
import com.ethz.geobot.herbar.filter.Filter;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionConvertSpeciesList extends AbstractParametrizedAction
{
    public ActionConvertSpeciesList( final JFrame parent )
    {
        super( "MENU.ITEM.IMPORT", parent );
    }

    private static Logger LOG = LoggerFactory.getLogger( ActionConvertSpeciesList.class );

    private static String lastDirectory = null;

    /**
     * Transforms the file of taxa by adding all parent taxa.
     *
     * @param e
     */
    public void actionPerformed( final ActionEvent e )
    {
        try
        {
            final JFileChooser chooser = new JFileChooser( lastDirectory );
            chooser.setFileFilter( new UstFileFilter() );
            chooser.setMultiSelectionEnabled( false );
            if ( JFileChooser.CANCEL_OPTION == chooser.showOpenDialog( null ) )
            {
                return;
            }
            final File file = chooser.getSelectedFile();
            lastDirectory = file.getParent();
            // Load old model
            final InputStream reader = new FileInputStream( file );
            final Filter filter = (Filter) getSerializer().fromXML( reader );
            reader.close();
            final Detail[] details = filter.getDetails();
            final Set<Detail> resultSet = new HashSet<Detail>( Arrays.asList( details ) );
            final HerbarModel model = Application.getInstance().getModel();
            for ( Detail detail : details )
            {
                final String scope = detail.getScope();
                final Taxon taxon = model.getTaxon( scope );
                if ( taxon == null )
                {
                    LOG.error( "species \"" + scope + "\" not found in taxonomy, skipping..." );
                }
                else
                {
                    Taxon parent = taxon;
                    while ( parent != null )
                    {
                        final Detail d = new Detail();
                        d.setScope( parent.getName() );
                        resultSet.add( d );
                        parent = parent.getParentTaxon();
                    }
                }
            }
            // Save new model
            final Filter resultFilter = new Filter();
            resultFilter.setFixed( false );
            resultFilter.setName( filter.getName() );
            resultFilter.setRank( 0 );
            resultFilter.setDetails( resultSet.toArray( details ) );
            final FileOutputStream out = new FileOutputStream( file );
            out.write( getSerializer().toXML( resultFilter ).getBytes() );
            out.close();
        }
        catch ( FileNotFoundException e1 )
        {
            e1.printStackTrace();
        }
        catch ( Exception e1 )
        {
            e1.printStackTrace();
        }
    }

    private XStream getSerializer()
    {
        return getxStream();
    }

    public static XStream getxStream()
    {
        final XStream x = new XStream();
        x.alias( "filter", Filter.class );
        x.alias( "detail", Detail.class );
        x.addImplicitCollection( Filter.class, "details", Detail.class );
        x.useAttributeFor( Filter.class, "fixed" );
        x.useAttributeFor( Filter.class, "rank" );
        x.useAttributeFor( Filter.class, "name" );
        x.useAttributeFor( Detail.class, "scope" );
        x.addPermission( NoTypePermission.NONE );
        x.addPermission( NullPermission.NULL );
        x.addPermission( PrimitiveTypePermission.PRIMITIVES );
        x.allowTypes( new Class[] { Filter.class, Detail.class, TreeSet.class, Properties.class, Proxy.class, TextAttribute.class, EnumSet.class, EnumMap.class } );
        x.allowTypeHierarchy( TreeMap.class );

        return x;
    }

    private static class UstFileFilter extends FileFilter
    {
        public boolean accept( final File f )
        {
            return f.isDirectory() || f.getName().endsWith( ".xml" );
        }

        public String getDescription()
        {
            return "eBot XML Datei mit Arten (*.xml)";
        }
    }
}
