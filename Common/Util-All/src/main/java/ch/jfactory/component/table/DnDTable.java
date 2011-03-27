/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.table;

import javax.swing.TransferHandler;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class DnDTable extends SortedTable
{
    public DnDTable( final SortableTableModel model )
    {
        super( model );
        setDragEnabled( true );
        setTransferHandler( new ResortingTransferHandler() );
    }

    private class ResortingTransferHandler extends TransferHandler
    {
    }
}
