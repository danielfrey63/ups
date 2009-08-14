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
package ch.jfactory.typemapper;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:59 $
 */
public class AddType extends ActionCommand
{

    private TypeModel model;

    public AddType(final CommandManager commandManager, final TypeModel model)
    {
        super(commandManager, Commands.COMMANDID_ADD);
        this.model = model;
    }

    protected void handleExecute()
    {
        final TypeMapping mapping = new TypeMapping("Neuer Typ", "/16x16/outline/man.png");
        model.getDataModel().add(mapping);
        model.getSelectionInList().setSelection(mapping);
    }
}
