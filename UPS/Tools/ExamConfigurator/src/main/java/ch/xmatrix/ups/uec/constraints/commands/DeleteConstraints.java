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

import ch.xmatrix.ups.domain.Constraints;
import com.jgoodies.binding.list.SelectionInList;
import javax.swing.JList;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public class DeleteConstraints extends ActionCommand
{

    private SelectionInList constraintsModels;

    private JList selection;

    public DeleteConstraints(final CommandManager manager, final SelectionInList constraintsModels)
    {
        super(manager, Commands.COMMANDID_DELETECONSTRAINT);
        this.constraintsModels = constraintsModels;
    }

    protected void handleExecute()
    {
        final Constraints constraints = (Constraints) constraintsModels.getSelection();
        final int[] indices = selection.getSelectedIndices();
        for (int i = 0; i < indices.length; i++)
        {
            final int index = indices[i];
            constraints.removeConstraint(constraints.getConstraints().get(index));
        }
    }

    public void setList(final JList list)
    {
        selection = list;
    }
}
