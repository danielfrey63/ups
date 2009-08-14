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

import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.uec.main.MainModel;
import com.jgoodies.uifextras.fileaccess.DirectoryChooser;
import java.io.File;
import java.util.List;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Closes the current card, opens the welcome card, resets the dirty flag and the current file.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExportCommand extends ActionCommand
{

    private MainModel model;

    public ExportCommand(final CommandManager commandManager, final MainModel model)
    {
        super(commandManager, Commands.COMMANDID_EXPORT);
        this.model = model;
    }

    protected void handleExecute()
    {
        final File dir = DirectoryChooser.chooseDirectory(null);
        final List<SimpleModelList> models = MainModel.getModels();
        for (final SimpleModelList list : models)
        {

        }
    }
}
