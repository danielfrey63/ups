/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.thoughtworks.xstream.XStream;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionConvertFilterFromUst extends AbstractParametrizedAction
{
    private static final Map<String, ArrayList<String>> MAP = new HashMap<String, ArrayList<String>>();

    private static final ArrayList<String> ERR = new ArrayList<String>();

    public ActionConvertFilterFromUst( final JFrame parent )
    {
        super( "MENU.ITEM.IMPORT", parent );
    }

    public void actionPerformed( final ActionEvent e )
    {
        try
        {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter( new UstFileFilter() );
            chooser.setMultiSelectionEnabled( false );
            if ( JFileChooser.CANCEL_OPTION == chooser.showOpenDialog( null ) )
            {
                return;
            }
            final File file = chooser.getSelectedFile();
            final Reader reader = new InputStreamReader( new FileInputStream( file ), "UTF-8" );

            final XStream x = new XStream();
            x.alias( "root", Root.class );
            final Object o = x.fromXML( reader );

            final List list;
            final Root root;
            if ( o instanceof Root )
            {
                root = (Root) o;
                list = root.list;
            }
            else if ( o instanceof List )
            {
                list = (List) o;
            }
            else
            {
                JOptionPane.showMessageDialog( parent, "Unbekannter Listentyp. Liste kann nicht importiert werden.", "Fehler", JOptionPane.ERROR_MESSAGE );
                return;
            }
            final HerbarModel model = Application.getInstance().getModel();
            for ( final Object l : list )
            {
                final String taxon = (String) l;
                final Taxon foundTaxon = model.getTaxon( taxon );
                if ( foundTaxon == null )
                {
                    ERR.add( taxon );
                }
                else
                {
                    mapTaxon( foundTaxon );
                }
            }

            final String r = System.getProperty( "line.separator" );
            boolean accept = false;
            while ( accept )
            {
            }
            final String name = JOptionPane.showInputDialog( "Wie soll die Prüfungsliste heissen?" );
            final String dirName = System.getProperty( "user.home" ) + "/.hcd2/filter/sc/";
            final File dir = new File( dirName );
            dir.mkdirs();
            final String f = new File( dir, name + ".xml" ).getAbsolutePath();
            final Writer writer = new FileWriter( f );
            writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + r );
            writer.write( "<filter baseFilterName=\"\" name=\"Pruefungsliste\">" + r );
            final Set<String> levels = MAP.keySet();
            for ( final String level : levels )
            {
                final ArrayList<String> taxaOnLevel = MAP.get( level );
                for ( final String taxon : taxaOnLevel )
                {
                    writer.write( "    <detail scope=\"" + taxon + "\">" + r );
                    writer.write( "        <level>" + level + "</level>" + r );
                    writer.write( "    </detail>" + r );
                }
            }
            writer.write( "</filter>" );
            writer.close();
            if ( ERR.size() == 0 )
            {
                JOptionPane.showMessageDialog( null, "Konvertierung erfolgreich. Die Stoffliste mit dem Namen \"" +
                        name + "\" \n" +
                        "steht beim nächsten Start der Herbar CD-ROM Version 2 zur Verfügung.",
                        "Konvertierung erfolgreich", JOptionPane.INFORMATION_MESSAGE );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Konvertierung erfolgreich aber nicht vollständig. " +
                        "Folgende Arten wurden nicht konvertiert: " + ERR + ". Die " +
                        "Stoffliste mit dem Namen \"" + name + "\" \n" +
                        "steht beim nächsten Start der Herbar CD-ROM Version 2 zur Verfügung. Bitte fügen Sie die " +
                        "fehlende(n) Art(en) manuell in der Herbar CD-ROM hinzu.",
                        "Konvertierung mit Warnungen.", JOptionPane.INFORMATION_MESSAGE );
            }

//            final HerbarModel base = Application.getInstance().getModel();
//            final FilterModel model = new FilterModel( base, "Pruefungsliste" );
//            FilterFactory.getInstance().saveFilterModel( model );
//        }
//        catch ( FilterPersistentException e1 )
//        {
//            e1.printStackTrace();
        }
        catch ( FileNotFoundException e1 )
        {
            e1.printStackTrace();
        }
        catch ( UnsupportedEncodingException e1 )
        {
            e1.printStackTrace();
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
        catch ( Exception e1 )
        {
            e1.printStackTrace();
        }
    }

    private static void mapTaxon( final Taxon taxon )
    {
        final Level level = taxon.getLevel();
        if ( level != null )
        {
            final String levelString = level.getName();
            ArrayList<String> taxaInLevel = MAP.get( levelString );
            if ( taxaInLevel == null )
            {
                taxaInLevel = new ArrayList<String>();
                MAP.put( levelString, taxaInLevel );
            }
            taxaInLevel.add( taxon.getName() );
            final Taxon parent = taxon.getParentTaxon();
            if ( parent != null )
            {
                mapTaxon( parent );
            }
        }
    }

    public class Root
    {
        public ArrayList<String> list;

        String uid;

        String exam;
    }

    private static class UstFileFilter extends FileFilter
    {
        public boolean accept( final File f )
        {
            return f.isDirectory() || f.getName().endsWith( ".xust" );
        }

        public String getDescription()
        {
            return "UPS Studenten-Tool Version 3.0 Dateien (*.xust)";
        }
    }
}
