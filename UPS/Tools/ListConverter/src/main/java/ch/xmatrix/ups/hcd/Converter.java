/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.hcd;

import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class Converter
{
    private static final Map<String, ArrayList<String>> MAP = new HashMap<String, ArrayList<String>>();

    public static void main( final String[] args )
    {
        try
        {
            // Introduction
            final int ret = JOptionPane.showConfirmDialog( null, "Im Folgenden wird Ihnen eine Möglichkeit geboten, eine mit dem UPS Studenten-Tool (Version 2.0-20060724)\n" +
                    "erstellten Pflanzenliste in Stofflisten für die Herbar CD-ROM Version 2 zu konvertieren. Bitte geben Sie im folgenden \n" +
                    "Dialog die Pflanzenliste an, die Sie konvertieren möchten. Wichtig ist, dass Sie die Liste mit dem UPS Studenten-Tool \n" +
                    "Version 2.0-20060724 gesichert haben, damit es einwandfrei funktioniert. Falls dies nicht der Fall ist, öffnen \n" +
                    "Sie ihre Pflanzenliste zuerst im UPS Studenten-Tool Version 2.0-20060724 und speichern Sie sie in der neuen Version ab.",
                    "Hinweis", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE );
            if ( ret == JOptionPane.CANCEL_OPTION )
            {
                System.exit( 0 );
            }

            // Opendialog
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter( new FileFilter()
            {
                public boolean accept( final File f )
                {
                    return f.isDirectory() || f.getName().endsWith( ".xust" );
                }

                public String getDescription()
                {
                    return "UPS Studenten-Tool Version 2.0-20060724 Dateien (*.xust)";
                }
            } );
            chooser.setMultiSelectionEnabled( false );
            if ( JFileChooser.CANCEL_OPTION == chooser.showOpenDialog( null ) )
            {
                System.exit( 0 );
            }
            final File file = chooser.getSelectedFile();
            final FileReader reader = new FileReader( file );

            final XStream x = new XStream();
            x.alias( "root", Root.class );
            final Object o = x.fromXML( reader );
            try
            {
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
                    throw new Exception( "list of type " + o.getClass().getName() + " not valid" );
                }

                // Get taxa in levels
                final TaxonTree[] trees = TaxonModels.getTaxonTreesArray();
                final TaxonTree taxa;
                if ( trees.length > 1 )
                {
                    taxa = (TaxonTree) JOptionPane.showInputDialog( null,
                            "Bitte wählen Sie einen Taxonomischen Baum, auf dessen Basis die Liste konvertiert werden soll",
                            "Auswahl des Taxonbaums", JOptionPane.QUESTION_MESSAGE, null, trees, trees[0] );
                }
                else if ( trees.length == 1 )
                {
                    taxa = trees[0];
                }
                else
                {
                    throw new Exception( "no taxon trees present" );
                }

                for ( final Object l : list )
                {
                    final String taxon = (String) l;
                    final SimpleTaxon simpleTaxon = taxa.findTaxonByName( taxon );
                    mapTaxon( simpleTaxon );
                }

                // Print lists
                final String r = System.getProperty( "line.separator" );
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
                JOptionPane.showMessageDialog( null, "Konvertierung erfolgreich. Die Stoffliste mit dem Namen \"" +
                        name + "\" \n" +
                        "steht beim nächsten Start der Herbar CD-ROM Version 2 zur Verfügung.",
                        "Konvertierung erfolgreich", JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Throwable e )
            {
                JOptionPane.showMessageDialog( null, "Während der Konvertierung der Pflanzenliste ist ein Fehler" +
                        "aufgetreten.\n" + e.getMessage(),
                        "Hinweis", JOptionPane.WARNING_MESSAGE );
                e.printStackTrace();
            }
            finally
            {
                if ( reader != null )
                {
                    reader.close();
                }
            }
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
        System.exit( 0 );
    }

    private static void mapTaxon( final SimpleTaxon taxon )
    {
        final SimpleLevel level = taxon.getLevel();
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
            final SimpleTaxon parent = taxon.getParentTaxon();
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
}
