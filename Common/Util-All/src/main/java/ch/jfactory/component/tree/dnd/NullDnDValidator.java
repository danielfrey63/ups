package ch.jfactory.component.tree.dnd;

import javax.swing.tree.TreePath;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 */
public class NullDnDValidator extends AbstractDnDValidatorUpdater
{
    /**
     * @see DnDValidatorUpdater#isMoveAllowed(javax.swing.tree.TreePath,javax.swing.tree.TreePath,int)
     */
    public boolean isMoveAllowed( final TreePath missile, final TreePath parent, final int index )
    {
        return false;
    }

    /**
     * @see AbstractDnDValidatorUpdater#updateNodesChanged()
     */
    public void updateNodesChanged()
    {
    }

    /**
     * @see ch.jfactory.component.tree.dnd.DnDValidatorUpdater#isRightShiftAllowed(TreePath)
     */
    public boolean isRightShiftAllowed( final TreePath path )
    {
        return false;
    }

    /**
     * @see ch.jfactory.component.tree.dnd.DnDValidatorUpdater#isLeftShiftAllowed(TreePath)
     */
    public boolean isLeftShiftAllowed( final TreePath path )
    {
        return false;
    }

    /**
     * @see ch.jfactory.component.tree.dnd.DnDValidatorUpdater#isAnyActionAllowed(TreePath, TreePath)
     */
    public boolean isAnyActionAllowed( final TreePath from, final TreePath to )
    {
        return false;
    }

    /**
     * @see ch.jfactory.component.tree.dnd.DnDValidatorUpdater#doRightShift(TreePath)
     */
    public void doRightShift( final TreePath path )
    {
    }

    /**
     * @see ch.jfactory.component.tree.dnd.DnDValidatorUpdater#doLeftShift(TreePath)
     */
    public void doLeftShift( final TreePath path )
    {
    }

}
