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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Expands a tree to the given depth. Tree expansion takes place uppon change of the model or during construction time.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class TreeExpander implements PropertyChangeListener
{
    private final int depth;

    private final JTree tree;

    /** Registers members and expands the tree. */
    public TreeExpander( final JTree tree, final int depth )
    {
        this.depth = depth;
        this.tree = tree;
        tree.addPropertyChangeListener( this );
        expand( new TreePath( tree.getModel().getRoot() ), 0 );
    }

    /** @see PropertyChangeListener#propertyChange(PropertyChangeEvent) */
    public void propertyChange( final PropertyChangeEvent evt )
    {
        if ( evt.getPropertyName().equals( "model" ) )
        {
            expand( new TreePath( tree.getModel().getRoot() ), 0 );
        }
    }

    private void expand( final TreePath path, final int currentDepth )
    {
        tree.expandPath( path );
        if ( currentDepth < depth )
        {
            final TreeNode node = (TreeNode) path.getLastPathComponent();
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                final TreePath childPath = path.pathByAddingChild( node.getChildAt( i ) );
                expand( childPath, currentDepth + 1 );
            }
        }
    }
}
