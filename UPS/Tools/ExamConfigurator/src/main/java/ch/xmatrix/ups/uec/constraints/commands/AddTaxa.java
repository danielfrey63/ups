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
package ch.xmatrix.ups.uec.constraints.commands;

import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.domain.Constraints;
import com.jgoodies.binding.list.SelectionInList;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.lang.ArrayUtils;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/08/04 15:50:01 $
 */
public class AddTaxa extends ActionCommand {

    private SelectionInList models;
    private TreeSelectionModel selection;

    public AddTaxa(final CommandManager manager, final SelectionInList models, final TreeSelectionModel selection) {
        super(manager, Commands.COMMANDID_ADDTAXA);
        this.models = models;
        this.selection = selection;
    }

    protected void handleExecute() {
        final Constraints constraints = (Constraints) models.getSelection();
        final TreePath[] paths = selection.getSelectionPaths();
        for (int i = 0; i < paths.length; i++) {
            final TreePath path = paths[i];
            final SimpleTaxon taxon = (SimpleTaxon) path.getLastPathComponent();
            if (SimpleTaxon.isSpecies(taxon)) {
                final String[] taxa = constraints.getDefaultTaxa();
                final String[] temp = taxa == null ? new String[0] : taxa;
                if (!ArrayUtils.contains(temp, taxon)) {
                    constraints.setDefaultTaxa((String[]) ArrayUtils.add(temp, taxon.getName()));
                }
            }
            constraints.addTaxon(taxon.getName());
        }
    }
}
