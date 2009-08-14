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

import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import com.jgoodies.binding.list.SelectionInList;
import javax.swing.JList;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class RemoveTaxa extends ActionCommand
{

    private SelectionInList constraintsModels;

    private JList selection;

    public RemoveTaxa(final CommandManager manager, final SelectionInList constraintsModels)
    {
        super(manager, Commands.COMMANDID_REMOVETAXA);
        this.constraintsModels = constraintsModels;
    }

    protected void handleExecute()
    {
        final Constraints constraints = (Constraints) constraintsModels.getSelection();
        final Constraint constraint = constraints.getCurrent();
        final int[] indices = selection.getSelectedIndices();
        for (int i = 0; i < indices.length; i++)
        {
            final int index = indices[i];
            final String taxon = constraint.getTaxa().get(index);
            constraints.removeTaxon(taxon);
        }
    }

    public void setList(final JList list)
    {
        selection = list;
    }
}
