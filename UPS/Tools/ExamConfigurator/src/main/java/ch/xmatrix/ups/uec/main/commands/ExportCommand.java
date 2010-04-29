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

import ch.jfactory.file.ZipUtils;
import ch.xmatrix.ups.uec.session.commands.UploadDialog;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uifextras.fileaccess.DirectoryChooser;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exports the settings to a file of name <code>uec-settings-<yyyyMMddHHmmss></code>.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExportCommand extends ActionCommand
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ExportCommand.class );

    /** The format to use for the export file name. */
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat( "yyyyMMddHHmmss" );

    private File exportDirectory;

    public ExportCommand( final CommandManager commandManager )
    {
        super( commandManager, Commands.COMMAND_ID_EXPORT );
    }

    protected void handleExecute()
    {
        try
        {
            final File settingsDirectory = getSettingsDirectory();
            final String feedback;
            if ( settingsDirectory.exists() )
            {
                if ( exportDirectory == null )
                {
                    exportDirectory = DirectoryChooser.chooseDirectory( null );
                }
                if ( exportDirectory != null )
                {
                    final File backupFile = new File( exportDirectory + "/uec-settings-" + FORMAT.format( new Date() ) + ".zip" );
                    ZipUtils.zipDirectory( settingsDirectory, backupFile );
                    feedback = "exported settings to " + backupFile;
                }
                else
                {
                    feedback = "User canceled export";
                }
                LOG.info( feedback );
            }
            else
            {
                feedback = settingsDirectory + " does not exist";
                LOG.warn( feedback );
            }
            JOptionPane.showMessageDialog( null, feedback );
        }
        catch ( IOException e )
        {
            LOG.error( e.getMessage(), e );
        }
    }

    private File getSettingsDirectory()
    {
        final String preferencesNode = Application.getConfiguration().getPreferencesRootName();
        final String preferencesPath = ( System.getProperty( "user.home" ) + "/." + preferencesNode ).replace( "\\", "/" );
        return new File( preferencesPath + "/" + UploadDialog.FILE_DATA );
    }

    public void setExportDirectory( final File directory )
    {
        this.exportDirectory = directory;
    }
}
