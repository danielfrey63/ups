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
import com.jgoodies.binding.list.SelectionInList;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Remove a specimen from the list.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:19:14 $
 */
public class DeleteSpecimen extends ActionCommand
{

    private final SelectionInList model;

    public DeleteSpecimen(final CommandManager commandManager, final SelectionInList model)
    {
        super(commandManager, Commands.COMMANDID_DELETESPECIMEN);
        assert model != null : "SelectionInList must not be null";
        this.model = model;
    }

    protected void handleExecute()
    {
        final SpecimensModel specimens = (SpecimensModel) model.getSelection();
        final SpecimenModel specimen = specimens.getCurrent();
        if (specimens != null)
        {
            specimens.setCurrent(null);
            specimens.remove(specimen);
            specimen.setTaxon(null);
        }
    }
}
