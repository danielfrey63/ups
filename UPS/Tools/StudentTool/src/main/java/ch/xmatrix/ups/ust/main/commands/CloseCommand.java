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

import ch.xmatrix.ups.ust.main.MainModel;
import java.beans.PropertyVetoException;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Closes the current card, opens the welcome card, resets the dirty flag and the current file.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/07/27 16:38:57 $
 */
public class CloseCommand extends ActionCommand
{

    private MainModel model;

    public CloseCommand(final CommandManager commandManager, final MainModel model)
    {
        super(commandManager, Commands.COMMANDID_CLOSE);
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            model.setClosing();
            model.setCurrentFile(null);
            model.setCurrentCard(MainModel.CARDS_WELCOME);
            model.setDirty(false);
        }
        catch (PropertyVetoException e)
        {
        }
    }
}
