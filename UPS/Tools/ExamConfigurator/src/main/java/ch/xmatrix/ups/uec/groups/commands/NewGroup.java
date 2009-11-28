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
package ch.xmatrix.ups.uec.groups.commands;

import ch.xmatrix.ups.uec.groups.GroupModel;
import ch.xmatrix.ups.uec.groups.GroupsModel;
import com.jgoodies.binding.list.SelectionInList;
import javax.swing.DefaultListModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Adds a group to the list.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/17 23:29:42 $
 */
public class NewGroup extends ActionCommand
{
    private final SelectionInList model;

    private final DefaultListModel listModel;

    public NewGroup( final CommandManager commandManager, final SelectionInList model, final DefaultListModel listModel )
    {
        super( commandManager, Commands.COMMANDID_NEWGROUP );
        assert model != null : "model must not be null";
        assert listModel != null : "list model must not be null";
        this.model = model;
        this.listModel = listModel;
    }

    protected void handleExecute()
    {
        final GroupsModel groups = (GroupsModel) model.getSelection();
        if ( groups != null )
        {
            final GroupModel group = new GroupModel( "<Neue Gruppe>" );
            groups.addGroup( group );
            listModel.addElement( group );
        }
    }
}
