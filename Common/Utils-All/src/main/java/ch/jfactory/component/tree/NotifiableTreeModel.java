package ch.jfactory.component.tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public interface NotifiableTreeModel extends TreeModel, MutableTreeModel {

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends.  The model will notify all of
     * its listeners that the model has changed.
     */
    void reload();

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends.  The model will notify all of
     * its listeners that the model has changed below the path <code>path</code> (PENDING).
     */
    void reload(TreePath path);

    /**
     * Invoke this method after you've changed how path is to be represented in the tree.
     */
    void nodeChanged(TreePath path);

    /**
     * Invoke this method after you've inserted some TreeNodes into path.  childIndices should be the index of the new
     * elements and must be sorted in ascending order.
     */
    void nodesWereInserted(TreePath parentPath, int[] childIndices);

    /**
     * Invoke this method after you've removed some TreeNodes from path.  childIndices should be the index of the
     * removed elements and must be sorted in ascending order. And removedChildren should be the array of the children
     * objects that were removed.
     */
    void nodesWereRemoved(TreePath parentPath, int[] childIndices, Object[] removedChildren);

    /**
     * Invoke this method after you've changed how the children identified by childIndicies are to be represented in the
     * tree.
     */
    void nodesChanged(TreePath parentPath, int[] childIndices);

    /**
     * Invoke this method if you've totally changed the children of path and its childrens children.
     */
    void nodesChanged(TreePath path);
}
