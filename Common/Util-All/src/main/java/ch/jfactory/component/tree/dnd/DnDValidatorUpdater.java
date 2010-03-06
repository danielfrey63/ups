/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component.tree.dnd;

import ch.jfactory.component.tree.NotifiableTreeModel;
import javax.swing.tree.TreePath;

/**
 * This interface provides an abstraction for the <code>DnDTree</code>. It delibarates the <code>DnDTree</code> from
 * being concerned about the following aspects of the concrete tree implmenetation:
 *
 * <ul>
 *
 * <li> The exact type/nature of the model used in the tree</li>
 *
 * <li> The kind of nodes used in the tree</li>
 *
 * <li> The logic to allow/avoid for drag and drop actions</li>
 *
 * </ul>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 * @see DnDTree
 */
public interface DnDValidatorUpdater
{
    /**
     * Returns whether a drag to the right is appropriate. A drag to the right is detected by the <code>DnDTree</code>
     * and may be validated by this interfaces implementation.
     *
     * @param path the path on which nodes the right shift wants to be checked
     * @return whether the right shift is allowed
     */
    public boolean isRightShiftAllowed( TreePath path );

    /**
     * Returns whether a drag to the left is appropriate. A drag to the left is detected by the <code>DnDTree</code> and
     * may be validated by this interfaces implementation.
     *
     * @param path the path on which nodes the left shift wants to be checked
     * @return whether the left shift is allowed
     */
    public boolean isLeftShiftAllowed( TreePath path );

    /**
     * Returns whether a move from one not to another is appropriate. A move action is detected by the
     * <code>DnDTree</code> and may be validated by this interfaces implementation.
     *
     * @param from  the TreePath being moved, including the node moved
     * @param to    the TreePath <code>from</code> is moved to
     * @param index
     * @return whether the move shift is allowed
     */
    public boolean isMoveAllowed( TreePath from, TreePath to, int index );

    /**
     * Returns whether any action (<href="#isRightShiftAllowed(TreePath)"> right shift</href> ,
     * <href="#isLeftShiftAllowed(TreePath)">left shift </href>, <href="#isMoveAllowed(TreePath,
     * TreePath)">moving</href> ) is allowed.
     *
     * @param from the TreePath being moved, including the node moved
     * @param to   the TreePath <code>from</code> is moved to
     * @return whether any action is allowed Todo remove this interface method as it is not correctly supported by the
     *         DndTree.
     */
    public boolean isAnyActionAllowed( TreePath from, TreePath to );

    /**
     * Performs a right shift on the given <code>TreePath</code>
     *
     * @param path the <code>TreePath</code> to be right shifted
     */
    public void doRightShift( TreePath path );

    /**
     * Performs a left shift on the given <code>TreePath</code>
     *
     * @param path the <code>TreePath</code> to be left shifted
     */
    public void doLeftShift( TreePath path );

    /**
     * Sets the tree to update and validate.
     *
     * @param model the new model to set
     */
    public void setModel( NotifiableTreeModel model );

    public static class NopValidatorUpdater implements DnDValidatorUpdater
    {
        public boolean isRightShiftAllowed( final TreePath path )
        {
            return false;
        }

        public boolean isLeftShiftAllowed( final TreePath path )
        {
            return false;
        }

        public boolean isMoveAllowed( final TreePath from, final TreePath to, final int index )
        {
            return false;
        }

        public boolean isAnyActionAllowed( final TreePath from, final TreePath to )
        {
            return false;
        }

        public void doRightShift( final TreePath path )
        {
        }

        public void doLeftShift( final TreePath path )
        {
        }

        public void setModel( final NotifiableTreeModel model )
        {
        }
    }
}
