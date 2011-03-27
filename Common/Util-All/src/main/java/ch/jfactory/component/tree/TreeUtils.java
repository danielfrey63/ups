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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Utility methods for trees.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2007/09/27 10:41:47 $
 */
public final class TreeUtils
{
    /** Makes sure this class is not instantiated. */
    private TreeUtils()
    {
        // Just to make sure this class is not instantiated.
    }

    /**
     * Returns the array of nodes converted to tree paths.
     *
     * @param nodes the nodes array to convert
     * @return the tree paths
     */
    public static TreePath[] getPathsForNodes( final TreeNode[] nodes )
    {
        final int length = nodes.length;
        final ArrayList paths = new ArrayList();
        for ( int i = 0; i < length; i++ )
        {
            final TreeNode node = nodes[i];
            paths.add( getPathForNode( node ) );
        }
        return (TreePath[]) paths.toArray( new TreePath[paths.size()] );
    }

    /**
     * Returns the path for the given TreeNode.
     *
     * @param node the node to translate to a path
     * @return the path constructed
     */
    public static TreePath getPathForNode( TreeNode node )
    {
        final ArrayList path = new ArrayList();
        path.add( node );
        while ( ( node = node.getParent() ) != null )
        {
            path.add( 0, node );
        }
        return new TreePath( path.toArray() );
    }

    /**
     * Returns an array of tree nodes for the given tree paths.
     *
     * @param paths the tree paths to extract the tree nodes from
     * @return an array of tree nodes or null if paths is null
     */
    public static Object[] getNodesForPaths( final TreePath[] paths )
    {
        return getNodesForPaths( paths, new Object[0] );
    }

    /**
     * Returns an array of tree nodes for the given tree paths.
     *
     * @param paths the tree paths to extract the tree nodes from
     * @param type  the type of the new array
     * @return an array of tree nodes or null if paths is null
     */
    public static Object[] getNodesForPaths( final TreePath[] paths, final Object[] type )
    {
        if ( paths == null )
        {
            return null;
        }
        final int length = paths.length;
        final ArrayList nodes = new ArrayList();

        for ( int i = 0; i < length; i++ )
        {
            final TreePath path = paths[i];
            nodes.add( path.getLastPathComponent() );
        }

        return nodes.toArray( type );
    }

    /**
     * Expands all nodes in the tree;
     *
     * @param tree the tree
     */
    public static void expandAll( final JTree tree )
    {
        for ( int i = 0; i < tree.getRowCount(); i++ )
        {
            tree.expandRow( i );
        }
    }

    public static void collapseAll( final JTree tree )
    {
        for ( int i = tree.getRowCount() - 1; i >= 0; i-- )
        {
            tree.collapseRow( i );
        }
        if ( tree.isRootVisible() )
        {
            tree.expandPath( tree.getPathForRow( 0 ) );
        }
    }

    /**
     * Matches the paths given in the iterator to the models nodes and returns them in a collection. Unmached paths are not returned. Warning: Make sure the children of one parent contain only distinct names, as maching occures based on the toString() result of each tree paths and models object.
     *
     * @param keys  the itereator containing tree path objects
     * @param model the tree model
     * @return a collection of tree paths matched
     */
    public static Collection matchPaths( final Iterator keys, final TreeModel model )
    {
        final HashSet matches = new HashSet();
        while ( keys.hasNext() )
        {
            boolean isAdoptable = true;
            final TreePath treePath = (TreePath) keys.next();
            final Object[] pathObjects = treePath.getPath();
            for ( int i = 0; i < pathObjects.length && isAdoptable; i++ )
            {
                final Object pathObject = pathObjects[i];
                if ( i == 0 )
                {
                    final Object root = model.getRoot();
                    if ( pathObject.toString().equals( root.toString() ) )
                    {
                        pathObjects[0] = root;
                    }
                    else
                    {
                        isAdoptable = false;
                    }
                }
                else
                {
                    final Object parent = pathObjects[i - 1];
                    isAdoptable = false;
                    for ( int j = 0; j < model.getChildCount( parent ); j++ )
                    {
                        final Object child = model.getChild( parent, j );
                        if ( pathObject.toString().equals( child.toString() ) )
                        {
                            pathObjects[i] = child;
                            isAdoptable = true;
                        }
                    }
                }
            }
            if ( isAdoptable )
            {
                matches.add( new TreePath( pathObjects ) );
            }
        }
        return matches;
    }

    /**
     * Builds the path to entry in the tree model.
     *
     * @param model
     * @param entry
     * @return the tree path found or null if none
     */
    public static TreePath findPathInTreeModel( final TreeModel model, final Object entry )
    {
        final Object root = model.getRoot();
        final TreePath path = new TreePath( root );
        return findPathInTreeModel( model, path, entry );
    }

    private static TreePath findPathInTreeModel( final TreeModel model, final TreePath path, final Object entry )
    {
        final Object node = path.getLastPathComponent();
        if ( node == entry )
        {
            return path;
        }
        else
        {
            for ( int i = 0; i < model.getChildCount( node ); i++ )
            {
                final Object child = model.getChild( node, i );
                final TreePath result = findPathInTreeModel( model, path.pathByAddingChild( child ), entry );
                if ( result != null )
                {
                    return result;
                }
            }
        }
        return null;
    }

    public static TreeNode findNodeInDefaultMutableTreeModel( final DefaultMutableTreeModel model, final Object entry )
    {
        final MutableTreeNode node = (MutableTreeNode) model.getRoot();
        final TreeNode foundNode;
        if ( node instanceof DefaultMutableTreeNode )
        {
            foundNode = findNodeByUserObject( (DefaultMutableTreeNode) node, entry );
        }
        else
        {
            throw new IllegalArgumentException( "you want to search nodes of type " + node.getClass().getName()
                    + " for an object object of type " + entry.getClass().getName() );
        }
        return foundNode;
    }

    public static TreeNode findNodeByUserObject( final DefaultMutableTreeNode node, final Object object )
    {
        if ( node.getUserObject() == object )
        {
            return node;
        }
        else
        {
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                final TreeNode result = findNodeByUserObject( (DefaultMutableTreeNode) node.getChildAt( i ), object );
                if ( result != null )
                {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Centers the tree path given in the view port of the scroll pane.
     *
     * @param tree the tree to center the path
     * @param tp   the tree path to center
     */
    public static void ensureVisibility( final JTree tree, final TreePath tp )
    {
        // Make sure to expand path before making visible, otherwise getRowForPath returns -1
        tree.makeVisible( tp );
        // Move path towards center
        final int iVis = tree.getVisibleRowCount() / 2;
        final int iRow = tree.getRowForPath( tp );
        final int iRowMax = Math.min( iRow + iVis, tree.getRowCount() - 1 );
        final int iRowMin = Math.max( 0, iRow - iVis );
        tree.scrollRowToVisible( iRowMin );
        tree.scrollRowToVisible( iRowMax );
    }

    public static ArrayList<TreePath> getExpandedPaths( final JTree tree, final TreePath path )
    {
        final ArrayList<TreePath> paths = new ArrayList<TreePath>();
        final Enumeration<TreePath> enumerator = tree.getExpandedDescendants( path );
        while ( enumerator != null && enumerator.hasMoreElements() )
        {
            final TreePath p = enumerator.nextElement();
            paths.add( p );
        }
        return paths;
    }

    public static ArrayList<TreePath> getExpandedPaths( final JTree tree )
    {
        return getExpandedPaths( tree, tree.getPathForRow( 0 ) );
    }
}
