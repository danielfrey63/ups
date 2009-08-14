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
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class NewConstraints extends ActionCommand
{

    private SelectionInList models;

    public NewConstraints(final CommandManager manager, final SelectionInList models)
    {
        super(manager, Commands.COMMANDID_NEWCONSTRAINT);
        this.models = models;
    }

    protected void handleExecute()
    {
        final Constraints constraints = (Constraints) models.getSelection();
        final Constraint constraint = new Constraint();
        constraint.setConstraints(constraints);
        constraints.addConstraint(constraint);
        constraints.setCurrent(constraint);
    }
}
