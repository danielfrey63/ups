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
package ch.xmatrix.ups.uec.sets.commands;

import ch.jfactory.component.Dialogs;
import ch.jfactory.file.OpenChooser;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the registrations and exam-lists from the UPS server.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class FromFiles extends LoadFilesystem
{
    private static final Logger LOG = LoggerFactory.getLogger( FromFiles.class );

    public FromFiles( final CommandManager commandManager, final SetBuilder.SubmitTableModel model )
    {
        super( commandManager, Commands.COMMAND_ID_LOAD_FILES, model );
        getChooser();
    }

    protected OpenChooser getChooser()
    {
        final Chooser chooser = new Chooser();
        chooser.getChooser().setMultiSelectionEnabled( true );
        return chooser;
    }

    private static class Filter extends FileFilter
    {
        public boolean accept( final File f )
        {
            return f.isDirectory() || f.getName().endsWith( EXTENTION );
        }

        public String getDescription()
        {
            return "UPS Pr�fungsliste (" + EXTENTION + ")";
        }
    }

    private class Chooser extends OpenChooser
    {
        public Chooser()
        {
            super( new Filter(), "openchooser", System.getProperty( "user.dir" ) );
        }

        protected void load( final File[] files )
        {
            File file = null;
            try
            {
                for ( final File file1 : files )
                {
                    file = file1;
                    loadAnonymousFile( file );
                }
                writeBuffer();
            }
            catch ( Exception e )
            {
                LOG.error( "error during processing of file" + ( file != null ? " \"" + file + "\"" : "s" ), e );
                Dialogs.showWarnMessage( null, "Warnung", "" +
                        "W�hrend der Verarbeitung der Dateien ist ein Fehler\n" +
                        "aufgetreten. Allf�llige bereits verarbeitete Dateien\n" +
                        "dieses Imports werden deshalb verworfen." );
            }
        }
    }

    public static void main( final String[] args ) throws InvocationTargetException, InterruptedException
    {
        System.setProperty( "jfactory.strings.resource", "ch.xmatrix.ups.uec.Strings" );
        System.setProperty( "log4j.configuration", "log4j.properties" );
        final FromFiles ff = new FromFiles( new CommandManager(), null );
        SwingUtilities.invokeAndWait( new Runnable()
        {
            public void run()
            {
                ff.handleExecute();
                final File[] files = ff.getChooser().getSelectedFiles();
                for ( final File file : files )
                {
                    System.out.println( file );
                }
            }
        } );
        System.exit( 0 );
    }
}