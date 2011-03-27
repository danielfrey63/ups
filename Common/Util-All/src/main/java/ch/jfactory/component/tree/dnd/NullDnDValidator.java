/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree.dnd;

import javax.swing.tree.TreePath;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 */
public class NullDnDValidator extends AbstractDnDValidatorUpdater
{
    /** @see DnDValidatorUpdater#isMoveAllowed(TreePath, TreePath, int) */
    public boolean isMoveAllowed( final TreePath missile, final TreePath parent, final int index )
    {
        return false;
    }

    /** @see AbstractDnDValidatorUpdater#updateNodesChanged() */
    public void updateNodesChanged()
    {
    }

    /** @see DnDValidatorUpdater#isRightShiftAllowed(TreePath) */
    public boolean isRightShiftAllowed( final TreePath path )
    {
        return false;
    }

    /** @see DnDValidatorUpdater#isLeftShiftAllowed(TreePath) */
    public boolean isLeftShiftAllowed( final TreePath path )
    {
        return false;
    }

    /** @see DnDValidatorUpdater#isAnyActionAllowed(TreePath, TreePath) */
    public boolean isAnyActionAllowed( final TreePath from, final TreePath to )
    {
        return false;
    }

    /** @see DnDValidatorUpdater#doRightShift(TreePath) */
    public void doRightShift( final TreePath path )
    {
    }

    /** @see DnDValidatorUpdater#doLeftShift(TreePath) */
    public void doLeftShift( final TreePath path )
    {
    }

}
