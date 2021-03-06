/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.command;

import ch.jfactory.component.table.SortableTableModel;
import ch.jfactory.component.table.TableUtils;
import javax.swing.ListSelectionModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Moves rows of a SortableTableModel up or down by a given delta.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class TableMove extends ActionCommand
{
    private final int delta;

    private final SortableTableModel tableModel;

    private final ListSelectionModel selectionModel;

    public TableMove( final CommandManager commandManager, final String commandId, final SortableTableModel tableModel,
                      final ListSelectionModel selectionModel, final int delta )
    {
        super( commandManager, commandId );
        this.delta = delta;
        this.tableModel = tableModel;
        this.selectionModel = selectionModel;
    }

    protected void handleExecute()
    {
        final int[] indices = tableModel.move( TableUtils.getSelection( selectionModel ), delta );
        TableUtils.setSelection( indices, selectionModel );
    }
}
