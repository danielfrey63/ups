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
package ch.xmatrix.ups.uec.master.commands;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import com.jgoodies.binding.list.SelectionInList;
import ch.xmatrix.ups.uec.master.MasterDetailsFactory;
import ch.xmatrix.ups.uec.master.commands.Commands;
import ch.xmatrix.ups.domain.TaxonBased;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class DeleteCommand extends ActionCommand {

    private SelectionInList models;
    private MasterDetailsFactory factory;

    public DeleteCommand(final CommandManager manager, final SelectionInList models,
                         final MasterDetailsFactory factory) {
        super(manager, Commands.COMMANDID_DELETE);
        this.models = models;
        this.factory = factory;
    }

    protected void handleExecute() {
        final TaxonBased model = (TaxonBased) models.getSelection();
        final int index = models.getSelectionIndex();
        factory.delete(model);
        final int newSelection;
        if (index == 0) {
            if (models.getSize() > 0) {
                newSelection = 0;
            }
            else {
                newSelection = -1;
            }
        }
        else {
            newSelection = index - 1;
        }
        models.setSelectionIndex(newSelection);
    }
}
