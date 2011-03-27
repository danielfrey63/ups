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

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Holds some of the commen implementations of a <code>MutableTreeNode</code>. The following methods are implemented: <ul> <li>{@link #remove(int)}</li> <li>{@link #isLeaf()}</li> <li>{@link #removeFromParent()}</li> <li>{@link #getIndex(TreeNode)}</li> </ul>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class AbstractMutableTreeNode implements MutableTreeNode
{
    /** @see MutableTreeNode#remove(int) */
    public void remove( final int index )
    {
        final MutableTreeNode child = (MutableTreeNode) getChildAt( index );
        remove( child );
    }

    /** @see TreeNode#isLeaf() */
    public boolean isLeaf()
    {
        return getChildCount() == 0;
    }

    /** @see MutableTreeNode#removeFromParent() */
    public void removeFromParent()
    {
        final MutableTreeNode parent = (MutableTreeNode) getParent();
        if ( parent != null )
        {
            parent.remove( this );
        }
    }

    /** @see TreeNode#getIndex(TreeNode) */
    public int getIndex( final TreeNode node )
    {
        for ( int i = 0; i < getChildCount(); i++ )
        {
            if ( getChildAt( i ) == node )
            {
                return i;
            }
        }
        return -1;
    }

    /** @see TreeNode#children() */
    public Enumeration children()
    {
        final int count = getChildCount();
        if ( count == 0 )
        {
            return DefaultMutableTreeNode.EMPTY_ENUMERATION;
        }
        else
        {
            final Vector list = new Vector();
            for ( int i = 0; i < count; i++ )
            {
                list.add( getChildAt( i ) );
            }
            return list.elements();
        }
    }

}
