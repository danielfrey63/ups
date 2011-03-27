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

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 * Does resolve a problem of not aligning the edit field correctly in a tree.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class FixedDefaultTreeCellEditor extends DefaultTreeCellEditor
{
    public FixedDefaultTreeCellEditor( final JTree tree,
                                       final DefaultTreeCellRenderer renderer, final TreeCellEditor editor )
    {
        super( tree, renderer, editor );
    }

    public FixedDefaultTreeCellEditor( final JTree tree,
                                       final DefaultTreeCellRenderer renderer )
    {
        super( tree, renderer );
    }

    protected void determineOffset( final JTree tree, final Object value, final boolean selected,
                                    final boolean expanded, final boolean leaf, final int row )
    {
        if ( renderer != null )
        {
            final JLabel l = (JLabel) renderer.getTreeCellRendererComponent
                    ( tree, value, selected, expanded, leaf, row, tree.hasFocus()
                            && tree.getLeadSelectionRow() == row );
            editingIcon = l.getIcon();
            if ( editingIcon != null )
            {
                offset = renderer.getIconTextGap() + editingIcon.getIconWidth();
            }
            else
            {
                offset = renderer.getIconTextGap();
            }
        }
        else
        {
            editingIcon = null;
            offset = 0;
        }
    }
}
