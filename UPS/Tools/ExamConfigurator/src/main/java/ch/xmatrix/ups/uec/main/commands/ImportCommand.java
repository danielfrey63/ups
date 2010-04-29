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
package ch.xmatrix.ups.uec.main.commands;

import ch.jfactory.file.OpenChooser;
import ch.jfactory.file.ZipUtils;
import ch.xmatrix.ups.uec.session.commands.UploadDialog;
import com.jgoodies.uif.application.Application;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.xmatrix.ups.uec.main.commands.Commands.COMMAND_ID_EXPORT;
import static ch.xmatrix.ups.uec.main.commands.Commands.COMMAND_ID_QUIT;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Imports the data from a ZIP file and reloads all models.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class ImportCommand extends ActionCommand
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ImportCommand.class );

    /** The directory to keep between subsequent open file attempts. */
    private String initialDirectory = System.getProperty( "user.home" );

    public ImportCommand( final CommandManager commandManager )
    {
        super( commandManager, Commands.COMMAND_ID_IMPORT );
    }

    protected void handleExecute()
    {
        try
        {
            final File settingsDirectory = getSettingsDirectory();
            final OpenChooser chooser = new OpenChooser( new ZipFileFilter(), "import", initialDirectory );
            chooser.setModal( true );
            chooser.getChooser().setMultiSelectionEnabled( false );
            chooser.open();
            final File[] files = chooser.getSelectedFiles();
            if ( files.length == 1 )
            {
                final File file = files[0];
                if ( file != null )
                {
                    initialDirectory = file.getParent();
                    backupSettings( settingsDirectory );
                    importSettings( settingsDirectory, file );
                    getCommandManager().getCommand( COMMAND_ID_QUIT ).execute();
                }
                else
                {
                    LOG.info( "user canceled export or no files chosen" );
                }
            }
            else
            {
                LOG.info( "user canceled export or no files chosen" );
            }
        }
        catch ( IOException e )
        {
            LOG.error( e.getMessage(), e );
        }
    }

    private void backupSettings( final File settingsDirectory )
    {
        final ExportCommand exportCommand = (ExportCommand) getCommandManager().getCommand( COMMAND_ID_EXPORT );
        exportCommand.setExportDirectory( settingsDirectory.getParentFile() );
        exportCommand.execute();
    }

    private void importSettings( final File settingsDirectory, final File file ) throws IOException
    {
        ZipUtils.unZip( file.getAbsolutePath(), settingsDirectory.getAbsolutePath() );
        LOG.info( "imported settings from " + file + " to " + settingsDirectory + ". application will be close now." );
        showMessageDialog( null, "Imported settings from " + file + " to " + settingsDirectory + ".\nApplication will be close now." );
    }

    private File getSettingsDirectory()
    {
        final String preferencesNode = Application.getConfiguration().getPreferencesRootName();
        final String preferencesPath = ( System.getProperty( "user.home" ) + "/." + preferencesNode ).replace( "\\", "/" );
        return new File( preferencesPath + "/" + UploadDialog.FILE_DATA );
    }

    private static class ZipFileFilter extends FileFilter
    {
        @Override
        public boolean accept( final File f )
        {
            return f.getName().endsWith( ".zip" ) || f.isDirectory();
        }

        @Override
        public String getDescription()
        {
            return "ZIP Files";
        }
    }
}