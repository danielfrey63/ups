/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree.filtered;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tests for the FilteredTreeModel based on MutableTreeNode.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/05/02 15:27:11 $
 */
public class NodeBasedFilteredTreeTest extends ModelBasedFilteredTreeTest
{
    public NodeBasedFilteredTreeTest( final String base )
    {
        super( base );
    }

    protected FilteredTreeModel getFilteredTreeModel()
    {
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) new JTree().getModel().getRoot();
        return new FilteredTreeModel( root );
    }
}
