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

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
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
     * An implementation of this method removes the last object of the given path from the last object of the parent path. It must notify any {@link TreeModelListener TreeModelListener}s thereafter with a {@link TreeModelEvent TreeModelEvent} by calling the listeners {@link TreeModelListener#treeNodesRemoved(TreeModelEvent) treeNodesRemoved(TreeModelEvent)} method.
     *
     * @param path the path to remove
     */
    void removeFromParent( TreePath path );

    /**
     * An implementation of this method inserts the last object of the given child path from the last object of the given parent path. It must notify any {@link TreeModelListener TreeModelListener}s thereafter with a {@link TreeModelEvent TreeModelEvent} by calling the listeners {@link TreeModelListener#treeNodesInserted(TreeModelEvent) treeNodesInserted(TreeModelEvent)} method.
     *
     * @param child  the path to insert
     * @param parent the path to add the child to
     * @param pos    the position of the new child
     */
    void insertInto( TreePath child, TreePath parent, int pos );
}
