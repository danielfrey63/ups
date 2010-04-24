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
 * Closes the current card, opens the welcome card, resets the dirty flag and the current file.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExportCommand extends ActionCommand
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ExportCommand.class );

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat( "yyyyMMddHHmmss" );

    public ExportCommand( final CommandManager commandManager )
    {
        super( commandManager, Commands.COMMAND_ID_EXPORT );
    }

    protected void handleExecute()
    {
        try
        {
            final String prefsNode = Application.getConfiguration().getPreferencesRootName();
            final String prefsPath = ( System.getProperty( "user.home" ) + "/." + prefsNode ).replace( "\\", "/" );
            final File prefsFile = new File( prefsPath + "/" + UploadDialog.FILE_DATA );
            final String feedback;
            if ( prefsFile.exists() )
            {
                final File dir = DirectoryChooser.chooseDirectory( null );
                if ( dir != null )
                {
                    final File backupFile = new File( dir + "/uec-settings-" + FORMATTER.format( new Date() ) + ".zip" );

                    ZipUtils.zipDirectory( prefsFile, backupFile );
                    LOG.info( "exported existing settings to " + backupFile );
                    feedback = "Exported to " + backupFile;
                }
                else
                {
                    feedback = "User canceled export";
                }
            }
            else
            {
                feedback = prefsFile + " does not exist";
                LOG.warn( feedback );
            }
            JOptionPane.showMessageDialog( null, feedback );
        }
        catch ( IOException e )
        {
            LOG.error( e.getMessage(), e );
        }
    }
}
