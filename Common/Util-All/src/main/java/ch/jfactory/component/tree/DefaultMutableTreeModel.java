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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * An implementation DefaultTreeModel and NotifiableTreeModel interface.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/08/29 13:10:43 $
 */
public class DefaultMutableTreeModel extends DefaultTreeModel implements NotifiableTreeModel
{
    public DefaultMutableTreeModel( final MutableTreeNode root )
    {
        super( root );
    }

    public DefaultMutableTreeModel( final MutableTreeNode root, final boolean asksAllowsChildren )
    {
        super( root, asksAllowsChildren );
    }

    // Todo: resolve this fake.
    public DefaultMutableTreeModel( final TreeModel model )
    {
        super( null );
    }

    // MutableTreeModel

    public void removeFromParent( final TreePath path )
    {
        super.removeNodeFromParent( (MutableTreeNode) path.getLastPathComponent() );
    }

    public void insertInto( final TreePath path, final TreePath parent, final int pos )
    {
        super.insertNodeInto( (MutableTreeNode) path.getLastPathComponent(), (MutableTreeNode) parent.getLastPathComponent(), pos );
    }

    // NotifiableTreeModel

    public void reload( final TreePath path )
    {
        super.reload( (TreeNode) path.getLastPathComponent() );
    }

    public void nodeChanged( final TreePath path )
    {
        super.nodeChanged( (TreeNode) path.getLastPathComponent() );
    }

    public void nodesWereInserted( final TreePath path, final int[] childIndices )
    {
        super.nodesWereInserted( (TreeNode) path.getLastPathComponent(), childIndices );
    }

    public void nodesWereRemoved( final TreePath path, final int[] childIndices, final Object[] removedChildren )
    {
        super.nodesWereRemoved( (TreeNode) path.getLastPathComponent(), childIndices, removedChildren );
    }

    public void nodesChanged( final TreePath path, final int[] childIndices )
    {
        super.nodesChanged( (TreeNode) path.getLastPathComponent(), childIndices );
    }

    public void nodesChanged( final TreePath path )
    {
        super.nodesChanged( (TreeNode) path.getLastPathComponent(), null );
    }
}
