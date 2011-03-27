/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */

packagesimple;

import ch.jfactory.component.tree.filtered.FilteredTreeModel;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/08/29 13:10:43 $
 */
public class FilteredTreeModelExample2
{
    public static void main( final String[] args )
    {
        final JTree tree = new JTree();
        tree.setRootVisible( true );
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        final TreeModel model = new FilteredTreeModel( root );
        tree.setModel( model );
        tree.expandRow( 3 );
        tree.expandRow( 2 );
        tree.expandRow( 1 );
        final JFrame f = new JFrame();
        f.add( tree );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setVisible( true );
    }
}
