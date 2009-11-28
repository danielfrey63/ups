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
package ch.xmatrix.ups.ust.main.commands;

import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.SaveChooser;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.ust.main.MainModel;
import java.io.File;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Asks for a file and redirects to the save command.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/07/27 16:38:58 $
 */
public class SaveAsCommand extends ActionCommand
{
    private final MainModel model;

    private final ExtentionFileFilter filter = new ExtentionFileFilter( Strings.getString( "savechooser.filetype.description" ),
            new String[]{Commands.NEW_FILE_EXTENTION}, true );

    public SaveAsCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_SAVEAS );
        this.model = model;
    }

    protected void handleExecute()
    {
        new SaveChooser( filter, "savechooser", model.getLastOpenSaveDirectory() )
        {
            protected void save( final File file )
            {
                if ( file != null )
                {
                    model.setLastOpenSaveDirectory( file.getParentFile().getAbsolutePath() );
                    model.setCurrentFile( file );
                    getCommandManager().getCommand( Commands.COMMANDID_SAVE ).execute();
                }
            }
        }.open();
    }
}
