/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.model;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.xmatrix.ups.domain.TreeObject;
import javax.swing.tree.TreePath;

/**
 * A tree model for {@link TreeObject}s
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/21 11:02:52 $
 */
public class TreeObjectTreeModel extends AbstractTreeModel
{

    public TreeObjectTreeModel(final TreeObject root)
    {
        super(root);
    }

    public void setRoot(final TreeObject root)
    {
        super.root = root;
        reload();
    }

    protected void remove(final Object child, final TreePath parentPath)
    {
        throw new IllegalStateException("removals not supported");
    }

    protected void insert(final TreePath child, final TreePath parent, final int pos)
    {
        throw new IllegalStateException("insertions not supported");
    }

    public int getChildCount(final Object parent)
    {
        final TreeObject treeObject = (TreeObject) parent;
        final TreeObject[] children = treeObject.getChildren();
        return children == null ? 0 : children.length;
    }

    public Object getChild(final Object parent, final int index)
    {
        return ((TreeObject) parent).getChildren()[index];
    }

    public void valueForPathChanged(final TreePath path, final Object newValue)
    {
    }
}
