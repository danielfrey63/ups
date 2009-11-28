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
package ch.xmatrix.ups.uec.sets.commands;

import ch.jfactory.component.table.SortableTableModel;
import ch.jfactory.component.table.TableUtils;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import javax.swing.ListSelectionModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:19:10 $
 */
public class RemoveFromTable extends ActionCommand
{
    private final SetBuilder.SubmitTableModel tableModel;

    private final ListSelectionModel selectionModel;

    private final SortableTableModel sortableModel;

    public RemoveFromTable( final CommandManager commandManager, final SetBuilder.SubmitTableModel tableModel,
                            final ListSelectionModel selectionModel, final SortableTableModel sortableModel )
    {
        super( commandManager, Commands.COMMANDID_REMOVE );
        this.tableModel = tableModel;
        this.selectionModel = selectionModel;
        this.sortableModel = sortableModel;
    }

    protected void handleExecute()
    {
        final int[] indices = TableUtils.getSelection( selectionModel );
        for ( int i = indices.length - 1; i >= 0; i-- )
        {
            final int index = indices[i];
            final int indexToRemove = sortableModel.getIndexes()[index];
            final Registration registration = tableModel.getRegistrations()[indexToRemove];
            tableModel.remove( registration );
        }
    }
}
