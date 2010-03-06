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
import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.OpenChooser;
import ch.jfactory.lang.ToStringComparator;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the registrations and exam-lists from the UPS server.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class FromDirectory extends LoadFilesystem
{
    private static final Logger LOG = LoggerFactory.getLogger( FromDirectory.class );

    public FromDirectory( final CommandManager commandManager, final SetBuilder.SubmitTableModel model )
    {
        super( commandManager, Commands.COMMANDID_LOADDIRECTORY, model );
    }

    protected OpenChooser getChooser()
    {
        return new Chooser();
    }

    private class Chooser extends OpenChooser
    {
        public Chooser()
        {
            super( null, JFileChooser.DIRECTORIES_ONLY, "openchooser", System.getProperty( "user.dir" ) );
        }

        protected void load( final File directory )
        {
            File file = null;
            try
            {
                final File[] files = directory.listFiles( new ExtentionFileFilter( "", new String[]{EXTENTION}, false ) );
                Arrays.sort( files, new ToStringComparator() );
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
                        "Während der Verarbeitung der Dateien ist ein Fehler\n" +
                        "aufgetreten. Allfällige bereits verarbeitete Dateien\n" +
                        "dieses Imports werden deshalb verworfen." );
            }
        }
    }

    public static void main( final String[] args ) throws InvocationTargetException, InterruptedException
    {
        System.setProperty( "jfactory.strings.resource", "ch.xmatrix.ups.uec.Strings" );
        System.setProperty( "log4j.configuration", "log4j.properties" );
        final FromDirectory ff = new FromDirectory( new CommandManager(), null );
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
