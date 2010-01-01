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

import ch.jfactory.convert.Converter;
import ch.xmatrix.ups.ust.main.MainModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Saves to the already opened document or redirects to the "save as" command.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/07/27 16:38:58 $
 */
public class SaveCommand extends ActionCommand
{
    private final MainModel model;

    public SaveCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_SAVE );
        this.model = model;
    }

    protected void handleExecute()
    {
        if ( model.isDefaultFile() )
        {
            getCommandManager().getCommand( Commands.COMMANDID_SAVEAS ).execute();
        }
        else
        {
            try
            {
                final ArrayList<String> taxa = model.getUserModel().getTaxa();
                final Commands.Encoded encoded = new Commands.Encoded();
                encoded.list = taxa;
                encoded.uid = model.getUserModel().getUid();
                encoded.exam = model.getUserModel().getExamInfoUid();
                final File file = model.getCurrentFile();
                final FileWriter out = new FileWriter( file );
                final Converter encoder = Commands.getConverter();
                final String xml = encoder.from( encoded );
                out.write( xml );
                out.close();
                model.setDirty( false );
            }
            catch ( IOException e1 )
            {
                e1.printStackTrace();
            }
        }
    }
}
