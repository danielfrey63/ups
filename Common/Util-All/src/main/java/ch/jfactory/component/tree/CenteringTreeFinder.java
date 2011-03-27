/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Scrolls to the found tree node and centers it in the visible part of the tree.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CenteringTreeFinder implements TreeFinder
{
    private final JTree tree;

    public CenteringTreeFinder( final JTree tree )
    {
        this.tree = tree;
    }

    public void setSelection( final TreePath tp )
    {
        tree.setSelectionPath( tp );
        final int iVis = tree.getVisibleRowCount() / 2;
        final int iRow = tree.getRowForPath( tp );
        final int iRowMax = Math.min( iRow + iVis, tree.getRowCount() - 1 );
        final int iRowMin = Math.max( 0, iRow - iVis );
        tree.scrollRowToVisible( iRowMin );
        tree.scrollRowToVisible( iRowMax );
    }
}
