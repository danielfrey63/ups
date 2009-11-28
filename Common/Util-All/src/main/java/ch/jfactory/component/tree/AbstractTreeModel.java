/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.component.tree;

import com.jgoodies.binding.beans.Model;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A tree model that is independent of DefaultTreeModel or MutableTreeNode. It handles notification for node changes.
 *
 * <ul>
 *
 * <li>It provides a default implemetation of the {@link TreeModel TreeModels} methods:
 *
 * <ul>
 *
 * <li>{@link TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener) addTreeModelListener(TreeModelListener)}</li>
 *
 * <li>{@link TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
 * removeTreeModelListener(TreeModelListener)}.</li>
 *
 * <li>{@link TreeModel#isLeaf(Object) isLeaf(Object)}</li>
 *
 * <li>{@link TreeModel#getIndexOfChild(Object,Object) getIndexOfChild(Object, Object)}</li>
 *
 * </li></ul>
 *
 * <li>It provides a default implementation for {@link NotifiableTreeModel NotifiableTreeModel}:
 *
 * <ul>
 *
 * <li>{@link NotifiableTreeModel#reload() reload()}</li>
 *
 * <li>{@link NotifiableTreeModel#reload(javax.swing.tree.TreePath) reload(TreePath)}</li>
 *
 * <li>{@link NotifiableTreeModel#nodeChanged(javax.swing.tree.TreePath) nodeChanged(TreePath)}</li>
 *
 * <li>{@link NotifiableTreeModel#nodesChanged(javax.swing.tree.TreePath,int[]) nodesChanged(TreePath[], int[])}</li>
 *
 * <li>{@link NotifiableTreeModel#nodesChanged(javax.swing.tree.TreePath) nodesChanged(TreePath)}</li>
 *
 * <li>{@link NotifiableTreeModel#nodesWereInserted(javax.swing.tree.TreePath,int[]) nodesWereInserted(TreePath),
 * int[])}</li>
 *
 * <li>{@link NotifiableTreeModel#nodesWereRemoved(javax.swing.tree.TreePath,int[],Object[]) nodesWereRemoved(TreePath,
 * int[], Object[])}</li>
 *
 * </li></ul>
 *
 * <ul>
 *
 * <li>It provides a <em>basic</em> implementation for all {@link MutableTreeModel MutableTreeModels} methods.
 * <em>Basic</em> means that it handles all the notification stuff, but leafes the actual insetion/removal to the
 * implementeation by providing an abstract method to implement:
 *
 * <ul>
 *
 * <li>{@link MutableTreeModel#removeFromParent(javax.swing.tree.TreePath) removeFromParent(TreePath)} calls abstract
 * {@link #remove remove(Object, TreePath)} to do the actual removal.</li>
 *
 * <li>{@link MutableTreeModel#insertInto(javax.swing.tree.TreePath,javax.swing.tree.TreePath,int) insertInto(TreePath,
 * TreePath, int)} calls abstract {@link #insert insert(TreePath, TreePath, int)} to do the actual insertion.</li>
 *
 * </ul>
 *
 * </ul>
 *
 * <li>It provides some convenience methods for clients that want to manipulate the tree. They do the automatic
 * notification of listeners:
 *
 * <ul>
 *
 * <li>{@link #removeFromParent(javax.swing.tree.TreePath[]) remove(TreePath[])}</li>
 *
 * <li>{@link #insertInto(Object,javax.swing.tree.TreePath,int) insert(Object, TreePath)}</li>
 *
 * </li></ul>
 *
 * </ul>
 *
 * It remains to the implementation of this class to provide the remaining methods of {@link TreeModel TreeModel} and
 * all methods of {@link MutableTreeModel MutableTreeModel}.
 *
 * <p/>To use the model to remove nodes, a client may use the convenience method {@link
 * #removeFromParent(javax.swing.tree.TreePath)}  remove}.
 *
 * <pre>
 * TreePath path = getPathToNodeWhereNewChildIsToBeInserted();
 * model.remove(path);
 * </pre>
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2008/01/06 10:16:23 $
 */
public abstract class AbstractTreeModel extends Model implements NotifiableTreeModel
{
    private final ArrayList<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();

    protected Object root;

    public AbstractTreeModel( final Object root )
    {
        super();
        setRoot( root );
    }

    /**
     * Experimental.
     *
     * @param root
     */
    public void setRoot( final Object root )
    {
        this.root = root;
        fireTreeStructureChanged( new TreeModelEvent( this, root == null ? null : new TreePath( root ) ) );
    }

    // TreeModel

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @param l the listener to add
     */
    public void addTreeModelListener( final TreeModelListener l )
    {
        listenerList.add( l );
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     */
    public void removeTreeModelListener( final TreeModelListener l )
    {
        listenerList.remove( l );
    }

    public boolean isLeaf( final Object node )
    {
        return getChildCount( node ) == 0;
    }

    public int getIndexOfChild( final Object parent, final Object child )
    {
        for ( int i = 0; i < getChildCount( parent ); i++ )
        {
            if ( getChild( parent, i ).equals( child ) )
            {
                return i;
            }
        }
        return -1;
    }

    public Object getRoot()
    {
        return root;
    }

    // NotifiableTreeModel

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends.  The model will notify all of
     * its listeners that the model has changed.
     */
    public void reload()
    {
        final Object root = getRoot();
        if ( root != null )
        {
            reload( new TreePath( root ) );
        }
        else
        {
            reload( new TreePath( new Object() ) );
        }
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends.  The model will notify all of
     * its listeners that the model has changed below the path <code>path</code> (PENDING).
     */
    public void reload( final TreePath path )
    {
        if ( path != null )
        {
            fireTreeStructureChanged( new TreeModelEvent( this, path ) );
        }
    }

    /**
     * Invoke this method after you've changed how path is to be represented in the tree.
     */
    public void nodeChanged( final TreePath path )
    {
        if ( listenerList != null && path != null )
        {
            final TreePath parentPath = path.getParentPath();
            if ( parentPath != null )
            {
                final Object parent = parentPath.getLastPathComponent();
                final int index = getIndexOfChild( parent, path.getLastPathComponent() );
                if ( index != -1 )
                {
                    nodesChanged( parentPath, new int[]{index} );
                }
            }
            else
            {
                nodesChanged( path );
            }
        }
    }

    /**
     * Invoke this method after you've changed how path is to be represented in the tree.
     */
    public void nodeChanged( final Object node )
    {
        nodeChanged( TreeUtils.findPathInTreeModel( this, node ) );
    }

    /**
     * Invoke this method after you've changed how the children identified by childIndicies are to be represented in the
     * tree.
     */
    public void nodesChanged( final TreePath parentPath, final int[] childIndices )
    {
        if ( childIndices == null )
        {
            throw new IllegalArgumentException( "childIndeces must not be null" );
        }
        if ( parentPath != null )
        {
            final int len = childIndices.length;
            if ( len > 0 )
            {
                final Object parent = parentPath.getLastPathComponent();
                final Object[] children = new Object[len];
                for ( int i = 0; i < len; i++ )
                {
                    children[i] = getChild( parent, childIndices[i] );
                }
                fireTreeNodesChanged( new TreeModelEvent( this, parentPath, childIndices, children ) );
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of path and its childrens children.
     */
    public void nodesChanged( final TreePath path )
    {
        if ( path != null )
        {
            fireTreeNodesChanged( new TreeModelEvent( this, path ) );
        }
    }

    /**
     * Invoke this method after you've inserted some TreeNodes into path.  childIndices should be the index of the new
     * elements and must be sorted in ascending order.
     */
    public void nodesWereInserted( final TreePath parentPath, final int[] childIndices )
    {
        if ( listenerList != null && parentPath != null && childIndices != null && childIndices.length > 0 )
        {
            final int cCount = childIndices.length;
            final Object parent = parentPath.getLastPathComponent();
            final Object[] newChildren = new Object[cCount];
            for ( int i = 0; i < cCount; i++ )
            {
                newChildren[i] = getChild( parent, childIndices[i] );
            }
            fireTreeNodesInserted( new TreeModelEvent( this, parentPath, childIndices, newChildren ) );
        }
    }

    /**
     * Invoke this method after you've removed some TreeNodes from path.  childIndices should be the index of the
     * removed elements and must be sorted in ascending order. And removedChildren should be the array of the children
     * objects that were removed.
     */
    public void nodesWereRemoved( final TreePath parentPath, final int[] childIndices, final Object[] removedChildren )
    {
        if ( parentPath != null && childIndices != null )
        {
            fireTreeNodesRemoved( new TreeModelEvent( this, parentPath, childIndices, removedChildren ) );
        }
    }

    /**
     * Call this method to remove nodes. Handles proparation of a TreeModelEvent and calls {@link #remove remove(Object,
     * TreePath)}. Notifies listeners.
     *
     * @param path the child to remove
     */
    public void removeFromParent( final TreePath path )
    {
        final TreePath parentPath = path.getParentPath();
        final Object child = path.getLastPathComponent();
        final int index = getIndexOfChild( parentPath.getLastPathComponent(), child );
        remove( child, parentPath );
        nodesWereRemoved( parentPath, new int[]{index}, new Object[]{child} );
    }

    /**
     * Call this method to insert nodes. Does call the abstract {@link #insert insert} method and notifies listeners.
     *
     * @param child  the child to insert
     * @param parent the parent to insert it to
     * @param pos    the position to insert the child
     */
    public void insertInto( final TreePath child, final TreePath parent, final int pos )
    {
        insert( child, parent, pos );
        nodesWereInserted( parent, new int[]{pos} );
    }

    //--- Utilities

    /**
     * Convenience method to remove several nodes from this model. Calls {@link #removeFromParent(javax.swing.tree.TreePath)
     * removeFromParent(TreePath)} for each array element, that in turn notifies listeners.
     *
     * @param childPaths an array of paths to remove
     */
    public void removeFromParent( final TreePath[] childPaths )
    {
        for ( final TreePath path : childPaths )
        {
            removeFromParent( path );
        }
    }

    /**
     * Convenience method to insert a new object into this model. Calls {@link #insertInto(javax.swing.tree.TreePath,
     * javax.swing.tree.TreePath,int) insertInto(TreePath, TreePath, int)} that in turn notifies listeners.
     *
     * @param child  the object to insert
     * @param parent the path to insert it to
     */
    public void insertInto( final Object child, final TreePath parent, final int position )
    {
        insertInto( parent.pathByAddingChild( child ), parent, position );
    }

    //--- This interface

    /**
     * Implement this method to do the actual removal of the child from the parent. Do not call any listener
     * notifications. This is handled for you from the calling method {@link #removeFromParent(javax.swing.tree.TreePath)
     * removeFromParent(TreePath)}.
     *
     * @param child      the child to remove
     * @param parentPath the parent to remove it from
     */
    protected abstract void remove( Object child, TreePath parentPath );

    /**
     * Implement this method to insert a childPath into the parentPath at the given position. Do not notify listeners as
     * that is done by the calling method {@link #insertInto(javax.swing.tree.TreePath,javax.swing.tree.TreePath,int)
     * insertInto(TreePath, TreePath, int)}.
     *
     * @param childPath  the child to insert
     * @param parentPath the parent to insert it to
     * @param pos        the position to insert it at
     */
    protected abstract void insert( TreePath childPath, TreePath parentPath, int pos );

    //--- Helper

    protected void fireTreeNodesChanged( final TreeModelEvent event )
    {
        final Iterator<TreeModelListener> i = getCopyOfListIterator();
        while ( i.hasNext() )
        {
            i.next().treeNodesChanged( event );
        }
    }

    protected void fireTreeNodesInserted( final TreeModelEvent event )
    {
        final Iterator<TreeModelListener> i = getCopyOfListIterator();
        while ( i.hasNext() )
        {
            i.next().treeNodesInserted( event );
        }
    }

    protected void fireTreeNodesRemoved( final TreeModelEvent event )
    {
        final Iterator<TreeModelListener> i = getCopyOfListIterator();
        while ( i.hasNext() )
        {
            i.next().treeNodesRemoved( event );
        }
    }

    protected void fireTreeStructureChanged( final TreeModelEvent event )
    {
        final Iterator<TreeModelListener> i = getCopyOfListIterator();
        while ( i.hasNext() )
        {
            i.next().treeStructureChanged( event );
        }
    }

    private Iterator<TreeModelListener> getCopyOfListIterator()
    {
        final ArrayList<TreeModelListener> list;
        synchronized ( this )
        {
            list = (ArrayList<TreeModelListener>) listenerList.clone();
        }
        return list.iterator();
    }
}
