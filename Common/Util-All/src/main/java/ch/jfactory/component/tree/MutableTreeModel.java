package ch.jfactory.component.tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Interface to remove and add tree paths.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public interface MutableTreeModel extends TreeModel
{
    /**
     * An implementation of this method removes the last object of the given path from the last object of the parent
     * path. It must notify any {@link javax.swing.event.TreeModelListener TreeModelListener}s thereafter with a {@link
     * javax.swing.event.TreeModelEvent TreeModelEvent} by calling the listeners {@link
     * javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
     * treeNodesRemoved(TreeModelEvent)} method.
     *
     * @param path the path to remove
     */
    void removeFromParent( TreePath path );

    /**
     * An implementation of this method inserts the last object of the given child path from the last object of the
     * given parent path. It must notify any {@link javax.swing.event.TreeModelListener TreeModelListener}s thereafter
     * with a {@link javax.swing.event.TreeModelEvent TreeModelEvent} by calling the listeners {@link
     * javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
     * treeNodesInserted(TreeModelEvent)} method.
     *
     * @param child  the path to insert
     * @param parent the path to add the child to
     * @param pos    the position of the new child
     */
    void insertInto( TreePath child, TreePath parent, int pos );
}
