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
package ch.xmatrix.ups.uec.specimens.commands;

import ch.xmatrix.ups.model.SpecimenModel;
import ch.xmatrix.ups.model.SpecimensModel;
import ch.xmatrix.ups.uec.specimens.commands.Commands;
import ch.xmatrix.ups.domain.SimpleTaxon;
import com.jgoodies.binding.list.SelectionInList;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import javax.swing.tree.TreeSelectionModel;

/**
 * Adds a specimen to the list.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:19:14 $
 */
public class NewSpecimen extends ActionCommand {

    private SelectionInList model;
    private TreeSelectionModel selection;

    public NewSpecimen(final CommandManager commandManager, final SelectionInList model,
                       final TreeSelectionModel selection) {
        super(commandManager, Commands.COMMANDID_NEWSPECIMEN);
        assert model != null : "model must not be null";
        this.model = model;
        this.selection = selection;
    }

    protected void handleExecute() {
        final SpecimensModel specimens = (SpecimensModel) model.getSelection();
        final SimpleTaxon taxon = (SimpleTaxon) (selection.getSelectionCount() == 1 ?
                selection.getSelectionPath().getLastPathComponent() : null);
        if (specimens != null && taxon != null) {
            final SpecimenModel specimen = new SpecimenModel();
            specimen.setTaxon(taxon.getName());
            specimens.addSpecimenModel(specimen);
            specimens.setCurrent(specimen);
        }
    }
}
