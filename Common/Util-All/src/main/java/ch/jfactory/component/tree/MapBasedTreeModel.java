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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.TreePath;

/**
 * Simple implementation of {@link AbstractTreeModel AbstractTreeModel} based on object mappings. The only way for a
 * client to insert/remove children is to call the call {@link #insertInto(javax.swing.tree.TreePath,
 * javax.swing.tree.TreePath, int) insertInto(TreePath, TreePath, int)} or {@link #insertInto(Object,
 * javax.swing.tree.TreePath, int) insertInto(Object, TreePath, int)}.
 *
 * <p/>You can insert objects into objects directly by using {@link #insertInto(Object, Object, int) insertInto(Object,
 * Object, int)}. Typically you would use this model like this:
 *
 * <pre>
 * final MyObject root = new MyObject("Root");
 * final MyObject child1 = new MyObject("Child1");
 * final MapBasedTreeModel model = new MapBasedTreeModel(root);
 * model.insertInto(child1, root, 0);
 * model.insertInto(new MyObject("Child2"), root, 1);
 * model.insertInto(new MyObject("Child3"), root, 2);
 * model.insertInto(new MyObject("Sub1"), child1, 0);
 * model.insertInto(new MyObject("Sub2"), child1, 1);
 * </pre>
 *
 * <p/><strong>Warning:</strong> As this implementation is map-based, make sure the tree objects are different in the
 * sense of {@link #equals}. In particular, <code>String</code> objects are not suitable in this sense.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $
 */
public class MapBasedTreeModel extends AbstractTreeModel
{
    private final Map children = new HashMap();

    private final Map objectToPath = new HashMap();

    public MapBasedTreeModel( final Object root )
    {
        super( root );
        final TreePath path = new TreePath( root );
        children.put( root, new ArrayList() );
        objectToPath.put( root, path );
    }

    public int getChildCount( final Object parent )
    {
        final ArrayList children = (ArrayList) this.children.get( parent );
        return children == null ? 0 : children.size();
    }

    public Object getChild( final Object parent, final int index )
    {
        final ArrayList children = (ArrayList) this.children.get( parent );
        return children.get( index );
    }

    public void valueForPathChanged( final TreePath path, final Object newValue )
    {
        System.out.println( "changed" );
    }

    protected void remove( final Object child, final TreePath parentPath )
    {
        final ArrayList children = (ArrayList) this.children.get( parentPath.getLastPathComponent() );
        children.remove( child );
    }

    protected void insert( final TreePath child, final TreePath parent, final int pos )
    {
        final Object parentNode = parent.getLastPathComponent();
        ArrayList children = (ArrayList) this.children.get( parentNode );
        if ( children == null )
        {
            children = new ArrayList();
            this.children.put( parentNode, children );
            objectToPath.put( parentNode, parent );
        }
        children.add( pos, child.getLastPathComponent() );
        objectToPath.put( child.getLastPathComponent(), child );
    }

    /**
     * Inserts the child object into the parent object by resolving the parent objects path from the cache and calling
     * {@link #insertInto(Object, javax.swing.tree.TreePath, int) insertInto(Object, TreePath, int)}.
     *
     * @param child    the child to insert
     * @param parent   the parent to insert the child to
     * @param position the position to insert the child to
     */
    public void insertInto( final Object child, final Object parent, final int position )
    {
        final TreePath parentPath = (TreePath) objectToPath.get( parent );
        insertInto( parentPath.pathByAddingChild( child ), parentPath, position );
    }
}
