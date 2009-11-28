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
package ch.jfactory.component.tree.dnd;

import ch.jfactory.component.tree.NotifiableTreeModel;
import javax.swing.tree.TreePath;

/**
 * Simple implementation of an <code>AbstractDnDValidatorUpdater</code> that provides support for moving nodes around.
 * Shifting nodes left and right is not supported. Moving nodes into children of themselves is suppressed.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class MovingDnDValidatorUpdater extends AbstractDnDValidatorUpdater
{
    public MovingDnDValidatorUpdater( final NotifiableTreeModel model )
    {
        super( model );
    }

    public void updateNodesChanged()
    {
    }

    public boolean isMoveAllowed( final TreePath from, final TreePath to, final int index )
    {
        final boolean isMoveAllowed;
        if ( from == null || to == null || from.equals( to ) || from.isDescendant( to ) )
        {
            isMoveAllowed = false;
        }
        else
        {
            final TreePath fromParent = from.getParentPath();
            if ( fromParent != null && fromParent.equals( to ) )
            {
                final int fromIndex = getModel().getIndexOfChild( fromParent.getLastPathComponent(), from.getLastPathComponent() );
                final int delta = index - fromIndex;
                isMoveAllowed = ( delta != 1 ) && ( delta != 0 );
            }
            else
            {
                isMoveAllowed = true;
            }
        }
        return isMoveAllowed;
    }

    public boolean isRightShiftAllowed( final TreePath path )
    {
        return false;
    }

    public boolean isLeftShiftAllowed( final TreePath path )
    {
        return false;
    }

    public boolean isAnyActionAllowed( final TreePath from, final TreePath to )
    {
        return true;
    }

    public void doRightShift( final TreePath path )
    {
    }

    public void doLeftShift( final TreePath path )
    {
    }
}
