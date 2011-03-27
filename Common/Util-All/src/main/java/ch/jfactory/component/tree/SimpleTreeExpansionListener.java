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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

/**
 * Listens for expand and collapse events of the given tree.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class SimpleTreeExpansionListener implements TreeExpansionListener
{
    /** The expanded tree paths. */
    private final HashSet expandedTreePaths = new HashSet();

    /** The tree to expand. */
    private JTree tree;

    /**
     * Constructs a tree expansion listener for the given tree.
     *
     * @param tree the tree to construct the expansion listener to
     */
    public SimpleTreeExpansionListener( final JTree tree )
    {
        setTree( tree );
    }

    /**
     * Sets the tree.
     *
     * @param tree the tree to set
     */
    private void setTree( final JTree tree )
    {
        this.tree = tree;
    }

    /** {@inheritDoc} */
    public void treeExpanded( final TreeExpansionEvent treeExpansionEvent )
    {
        expandedTreePaths.add( treeExpansionEvent.getPath() );
    }

    /** {@inheritDoc} */
    public void treeCollapsed( final TreeExpansionEvent treeExpansionEvent )
    {
        // Remove the path from the cache
        final TreePath treePath = treeExpansionEvent.getPath();
        expandedTreePaths.remove( treePath );

        // Make sure that child paths are not expanded in the future
        final Iterator iter = expandedTreePaths.iterator();

        while ( iter.hasNext() )
        {
            final TreePath path = (TreePath) iter.next();

            if ( treePath.isDescendant( path ) )
            {
                iter.remove();
            }
        }
    }

    /** Restores the tree expansion state. */
    public void restore()
    {
        for ( final Object o : ( (HashSet) expandedTreePaths.clone() ) )
        {
            final TreePath path = (TreePath) o;
            tree.expandPath( path );
        }
    }

    /**
     * Returns the number of expanded paths.
     *
     * @return the number of expanded paths
     */
    public int getNumberOfExpandedPaths()
    {
        return expandedTreePaths.size();
    }

    /** To call when the model has changed and the tree paths have to be restored to the new model. Translates successfully by matching the names of the nodes only if the names are unique within a parent node. */
    public void translate()
    {
        final Collection matches = TreeUtils.matchPaths( expandedTreePaths.iterator(), tree.getModel() );
        expandedTreePaths.clear();
        expandedTreePaths.addAll( matches );
    }
}
