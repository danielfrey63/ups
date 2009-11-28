/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */

package ch.jfactory.component.tree.filtered;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.jfactory.component.tree.NotifiableTreeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A TreeModel implementation that has the ability to filter nodes. This model works on DefaultMutableTreeNode objects.
 *
 * <p/> The Method isShown is used to determine whether a specific node is shown or not. It works by ANDing the
 * different filters together. So to make a node disappear, it is enough to remove it in one filter. To make a specific
 * node appear, make sure to not filter it out in any set filter.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2006/08/29 13:10:43 $
 */
public class FilteredTreeModel extends AbstractTreeModel
{
    /**
     * The list of filters.
     */
    private final ArrayList<ViewFilter> filters = new ArrayList<ViewFilter>();

    /**
     * The model to which the implementation is delegated to.
     */
    private final DelegatingTreeModel delegate;

    /**
     * Constructs an object by using the given root node.
     *
     * @param root the root node
     */
    public FilteredTreeModel( final MutableTreeNode root )
    {
        super( root );
        this.delegate = new NodeBasedFilteredTreeModel( root );
    }

    public FilteredTreeModel( final NotifiableTreeModel model )
    {
        super( model.getRoot() );
        this.delegate = new ModelBasedFilteredTreeModel( model );
    }

    /**
     * Determines whether the node is shown or not.
     *
     * @param node the node to check whether it is shown or not
     * @return whether the node is showen or not
     */
    public boolean isShown( final Object node )
    {
        final int size = filters.size();
        boolean show = true;

        for ( int i = 0; i < size; i++ )
        {
            final ViewFilter filter = filters.get( i );
            show &= filter.isShown( node );
        }

        return show;
    }

    /**
     * Adds a view filter to the collection.
     *
     * @param filter the filter to add
     */
    public void addViewFilter( final ViewFilter filter )
    {
        filters.add( filter );
        delegate.reload( new TreePath( delegate.getRoot() ) );
    }

    /**
     * Removes a view filter from the collection.
     *
     * @param filter the filter to remove
     */
    public void removeViewFilter( final ViewFilter filter )
    {
        filters.remove( filter );
        delegate.reload( new TreePath( delegate.getRoot() ) );
    }

    public void reload( final Object node )
    {
        delegate.reload( new TreePath( delegate.getPath( node ) ) );
    }

    /**
     * Sets the flag to show parent nodes whose children are not shown.
     *
     * @param showEmptyParents whether to show empty parents
     */
    public void setShowEmptyParents( final boolean showEmptyParents )
    {
    }

    //--- TreeModel implementations

    public int getChildCount( final Object parent )
    {
        return delegate.getChildCount( parent );
    }

    public Object getChild( final Object parent, final int index )
    {
        return delegate.getChild( parent, index );
    }

    public void valueForPathChanged( final TreePath path, final Object newValue )
    {
        delegate.valueForPathChanged( path, newValue );
    }

    //--- AbstractTreeModel overrides

    public Object getRoot()
    {
        return delegate.getRoot();
    }

    public boolean isLeaf( final Object node )
    {
        return delegate.isLeaf( node );
    }

    public void addTreeModelListener( final TreeModelListener l )
    {
        delegate.addTreeModelListener( l );
    }

    public void removeTreeModelListener( final TreeModelListener l )
    {
        delegate.removeTreeModelListener( l );
    }

    public int getIndexOfChild( final Object parent, final Object child )
    {
        return delegate.getIndexOfChild( parent, child );
    }

    protected void remove( final Object child, final TreePath parentPath )
    {
        delegate.removeFromParent( parentPath.pathByAddingChild( child ) );
    }

    protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
    {
        delegate.insertInto( childPath, parentPath, pos );
    }

    //--- Utilities

    private class NodeBasedFilteredTreeModel extends DelegatingTreeModel
    {
        /**
         * The root node.
         */
        private final MutableTreeNode root;

        public NodeBasedFilteredTreeModel( final MutableTreeNode root )
        {
            super( root );
            this.root = root;
        }

        //--- TreeModel interface

        public Object getRoot()
        {
            return root;
        }

        public Object getChild( final Object parent, final int index )
        {
            int count = 0;
            final TreeNode node = (TreeNode) parent;
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                final TreeNode child = node.getChildAt( i );
                if ( isShown( child ) )
                {
                    if ( count++ == index )
                    {
                        return child;
                    }
                }
                else
                {
                    final Object child2 = getChild( child, index - count );
                    if ( child2 != null )
                    {
                        return child2;
                    }
                    count += getChildCount( child );
                }
            }
            return null;
        }

        public int getIndexOfChild( final Object parent, final Object child )
        {
            final TreeNode node = (TreeNode) parent;
            int j = 0;
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                if ( isShown( child ) )
                {
                    if ( child.equals( node.getChildAt( i ) ) )
                    {
                        return j;
                    }
                    j++;
                }
            }
            return -1;
        }

        public int getChildCount( final Object parent )
        {
            int count = 0;
            final TreeNode node = (TreeNode) parent;
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                final TreeNode child = node.getChildAt( i );

                if ( isShown( child ) )
                {
                    count++;
                }
                else
                {
                    count += getChildCount( child );
                }
            }
            return count;
        }

        public boolean isLeaf( final Object obj )
        {
            final TreeNode node = (TreeNode) obj;
            return node.isLeaf();
        }

        public void valueForPathChanged( final TreePath path, final Object newValue )
        {
            final MutableTreeNode node = (MutableTreeNode) path.getLastPathComponent();
            node.setUserObject( newValue );
            nodeChanged( node );
        }

        //--- AbstractTreeModel implementation

        protected void remove( final Object child, final TreePath parentPath )
        {
            throw new UnsupportedOperationException();
        }

        protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
        {
            throw new UnsupportedOperationException();
        }

        //--- DelegatingTreeModel interface

        /**
         * Returns the visible path for a given node.
         *
         * @param object the node to return the path for
         * @return the visible path of the node
         */
        public Object[] getPath( final Object object )
        {
            TreeNode node = (TreeNode) object;
            final ArrayList<TreeNode> path = new ArrayList<TreeNode>();
            path.add( node );

            while ( ( node = node.getParent() ) != null )
            {
                if ( isShown( node ) )
                {
                    path.add( 0, node );
                }
            }

            return path.toArray();
        }
    }

    private class ModelBasedFilteredTreeModel extends DelegatingTreeModel
    {
        /**
         * Model behind.
         */
        private final NotifiableTreeModel model;

        private final Map<Object, Object> parents = new HashMap<Object, Object>();

        public ModelBasedFilteredTreeModel( final NotifiableTreeModel model )
        {
            super( model.getRoot() );
            this.model = model;
        }

        //--- TreeModel interface

        public int getChildCount( final Object parent )
        {
            int count = 0;
            for ( int i = 0; i < model.getChildCount( parent ); i++ )
            {
                final Object child = model.getChild( parent, i );
                if ( isShown( child ) )
                {
                    count++;
                }
                else
                {
                    count += getChildCount( child );
                }
            }
            return count;
        }

        public Object getChild( final Object parent, final int index )
        {
            int count = 0;
            for ( int i = 0; i < model.getChildCount( parent ); i++ )
            {
                final Object child = model.getChild( parent, i );
                if ( isShown( child ) )
                {
                    if ( count++ == index )
                    {
                        parents.put( child, parent );
                        return child;
                    }
                }
                else
                {
                    final Object child2 = getChild( child, index - count );
                    if ( child2 != null )
                    {
                        parents.put( child2, parent );
                        return child2;
                    }
                    count += getChildCount( child );
                }
            }
            return null;
        }

        public void valueForPathChanged( final TreePath path, final Object newValue )
        {
            model.valueForPathChanged( path, newValue );
        }

        //--- AbstractTreeModel overrides

        public Object getRoot()
        {
            return model.getRoot();
        }

        public boolean isLeaf( final Object node )
        {
            return model.isLeaf( node );
        }

        public int getIndexOfChild( final Object parent, final Object child )
        {
            for ( int i = 0; i < getChildCount( parent ); i++ )
            {
                if ( child == getChild( parent, i ) )
                {
                    return i;
                }
            }
            return -1;
        }

        protected void remove( final Object child, final TreePath parentPath )
        {
            throw new UnsupportedOperationException();
        }

        protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
        {
            throw new UnsupportedOperationException();
        }

        //--- DelegatingTreeModel interface

        public Object[] getPath( final Object object )
        {
            final List<Object> result = new ArrayList<Object>();
            result.add( object );
            Object parent = parents.get( object );
            while ( parent != null )
            {
                result.add( 0, parent );
                parent = parents.get( parent );
            }
            return result.toArray();
        }
    }

    private abstract class DelegatingTreeModel extends AbstractTreeModel
    {
        public DelegatingTreeModel( final Object object )
        {
            super( object );
        }

        public abstract Object[] getPath( final Object object );
    }
}
