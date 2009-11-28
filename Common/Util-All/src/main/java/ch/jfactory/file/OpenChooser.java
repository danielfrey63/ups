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
package ch.jfactory.file;

import ch.jfactory.component.Dialogs;
import ch.jfactory.resource.Strings;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uif.application.ApplicationDescription;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 * A JFileChooser embeded in a JDialog with a help text in the north. To use this class subclass and overwrite the
 * method {@link #load(File) load(File)}. Optionally you may overwrite {@link #cleanUpFileName(java.io.File)
 * cleanupFileName(File)} for checks on or change of a file name.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.5 $
 */
public class OpenChooser extends AbstractChooser
{
    /**
     * {@inheritDoc}
     */
    public OpenChooser( final FileFilter filter, final String base, final File initialDir )
    {
        super( null, filter, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final FileFilter filter, final String base, final String initialDir )
    {
        super( null, filter, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final int selectionMode, final String base, final File initialDir )
    {
        super( null, selectionMode, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final int selectionMode, final String base, final String initialDir )
    {
        super( null, selectionMode, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final JFrame parent, final FileFilter filter, final String base, final File initialDir )
    {
        super( parent, filter, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final JFrame parent, final FileFilter filter, final String base, final String initialDir )
    {
        super( parent, filter, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final JFrame parent, final int selectionMode, final String base, final File initialDir )
    {
        super( parent, selectionMode, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * {@inheritDoc}
     */
    public OpenChooser( final JFrame parent, final int selectionMode, final String base, final String initialDir )
    {
        super( parent, selectionMode, base, initialDir, JFileChooser.OPEN_DIALOG );
    }

    /**
     * Makes sure the file is only overwritten upon permission of the user.
     *
     * @param files the files to check
     * @return whether saving is ok
     */
    protected boolean checkFiles( final File[] files )
    {
        for ( final File file : files )
        {
            if ( !file.exists() )
            {
                Dialogs.showInfoMessage( getChooser(), getErrorTitle(), getErrorText( file ) );
                return false;
            }
        }
        return true;
    }

    protected void execute( final File[] files )
    {
        if ( files.length > 0 )
        {
            setDirectory( files[0].getParentFile() );
        }
        load( files );
    }

    protected void load( final File[] files )
    {
        for ( final File file : files )
        {
            load( file );
        }
    }

    /**
     * <p>If an ExtentionFileFilter has been passed to the constructor, this method makes sure the given chunk does
     * match exactly to one file. Otherwise an info box is shown.</p>
     *
     * <p>Overwrite this method if you want to doublecheck or change the file name before any checking for overwriting
     * is done.</p>
     *
     * @param file the original file choosen
     * @return the cleaned up file
     */
    protected File cleanUpFileName( final File file )
    {
        final FileFilter filter = getChooser().getFileFilter();
        if ( filter instanceof ExtentionFileFilter && file != null )
        {
            // Find matching extention in which case the file is returned as is.
            final ExtentionFileFilter simpleFilter = (ExtentionFileFilter) filter;
            final String[] extentions = simpleFilter.getExtentions();

            // Find matching file in file system
            if ( extentions.length > 1 )
            {
                final List<File> found = new ArrayList<File>();
                for ( final String extention : extentions )
                {
                    final File temp = new File( file.getAbsolutePath() + extention );
                    if ( temp.exists() && !temp.isDirectory() )
                    {
                        found.add( temp );
                    }
                }
                final int size = found.size();
                if ( size == 1 )
                {
                    return found.get( 0 );
                }
                else if ( size > 0 )
                {
                    final String title = Strings.getString( base + ".double.title" );
                    final String text = Strings.getString( base + ".double.text", size + "" );
                    Dialogs.showInfoMessage( getChooser(), title, text );
                    return null;
                }
            }
        }

        return file;
    }

    /**
     * Overwrite this method to invoke an action to save the file after it has been checked for overwriting and name
     * cleanup.
     *
     * @param file
     */
    protected void load( final File file )
    {
    }

    public static void main( final String[] args )
    {
        Application.setDescription( new ApplicationDescription(
                "", "", "", "", "", "", "", "", ""
        ) );
        final ResourceBundle bundle = new ListResourceBundle()
        {
            protected Object[][] getContents()
            {
                return new String[][]{
                        {"test.icon", "/file.png"},
                        {"test.frame.TITLE", "Öffne UST Datei"},
                        {"test.frame.TEXT1", "Öffne UST Datei"},
                        {"test.frame.TEXT2", "Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt. Das ist ein wirklich langer Text, der testen soll, ob das mit dem Zeilenumbruch klappt."},
                        {"test.frame.SYMBOL", "file.png"},
                        {"test.error.title", "Datei unbekannt"},
                        {"test.error.text", "Die Datei existiert nicht"},
                        {"test.double.title", "Mehrdeutige Datei"},
                        {"test.double.text", "Im Dateisystem befinden sich {0} Dateien, auf die der angegebene Name mit automatisch angehängter erlaubter Erweiterung passen würde. Bitte explizit die Erweiterung angeben."},
                        {"test.filetype.description", "Studenten-Tool Pflanzenlisten Version 2 (*.xust, *.ust)"},
                        {"BUTTON.OK.TEXT", "OK"},
                        {"BUTTON.CANCEL.TEXT", "Abbrechen"}
                };
            }
        };
        Strings.setResourceBundle( bundle );
        final OpenChooser dialog = new OpenChooser( null, "test", System.getProperty( "user.dir" ) );
        dialog.setDefaultCloseOperation( JDialog.EXIT_ON_CLOSE );
        dialog.open();
    }
}
