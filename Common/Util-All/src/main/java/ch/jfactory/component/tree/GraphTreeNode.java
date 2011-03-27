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

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author $Author $
 * @version $Revision $ $Date $
 */
public class GraphTreeNode extends AbstractMutableTreeNode
{
    /** Mapping between {@link VirtualGraphTreeNode} objects and corresponding <code>GraphTreeNode</code> objects. */
    private static final HashMap<VirtualGraphTreeNode, GraphTreeNode> cache = new HashMap<VirtualGraphTreeNode, GraphTreeNode>();

    /** The <code>GraphNode2TreeNode</code> being wrapped. */
    private final VirtualGraphTreeNode node;

    private GraphTreeNode( final VirtualGraphTreeNode node )
    {
        this.node = node;
    }

    private List<GraphTreeNode> getChildren()
    {
        final GraphNodeList childs = node.getChildren();
        final int len = childs.size();
        final List<GraphTreeNode> children = new ArrayList<GraphTreeNode>( len );
        for ( int i = 0; i < len; i++ )
        {
            final VirtualGraphTreeNode element = (VirtualGraphTreeNode) childs.get( i );
            children.add( getTreeNode( element ) );
        }
        return children;
    }

    /**
     * Factory method to retrieve a <code>GraphTreeNode</code> with an <code>GraphNode2TreeNode</code>.
     *
     * @param node the <code>GraphNode2TreeNode</code> to retrieve the <code>GraphTreeNode</code> for
     * @return the <code>GraphTreeNode</code> found
     */
    public static GraphTreeNode getTreeNode( final VirtualGraphTreeNode node )
    {
        GraphTreeNode result = cache.get( node );
        if ( result == null )
        {
            result = new GraphTreeNode( node );
            cache.put( node, result );
        }
        return result;
    }

    public static GraphTreeNode getNode( final TreePath tp )
    {
        return (GraphTreeNode) tp.getLastPathComponent();
    }

    public VirtualGraphTreeNode getNode()
    {
        return node;
    }

    public GraphNode getDependent()
    {
        return node.getDependent();
    }

    /** Returns whether the given class is a super type or interface, or the same type of this node. */
    public boolean isType( final Class clazz )
    {
        return node.isType( clazz );
    }

    public TreeNode getChildAt( final int index )
    {
        return getChildren().get( index );
    }

    public int getChildCount()
    {
        return getChildren().size();
    }

    public TreeNode getParent()
    {
        final VirtualGraphTreeNode parentNode = node.getParent();
        if ( parentNode == null )
        {
            return null;
        }
        return getTreeNode( parentNode );
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public void insert( final MutableTreeNode child, final int index )
    {
        final VirtualGraphTreeNode childNode = ( (GraphTreeNode) child ).getNode();
        node.addTreeChild( index, childNode );
    }

    public void remove( final MutableTreeNode treeNode )
    {
        final GraphTreeNode child = (GraphTreeNode) treeNode;
        node.removeTreeChild( child.getNode() );
    }

    public void setParent( final MutableTreeNode newParent )
    {
        throw new NoSuchMethodError( "setParent not implemented yet" );
    }

    public void setUserObject( final Object object )
    {
        if ( object instanceof String )
        {
            node.setName( (String) object );
            AbsGraphModel.getModel().setDirty( true );
        }
    }

    public String toString()
    {
        return node.toString();
    }

    public boolean equals( final Object obj )
    {
        if ( obj instanceof GraphTreeNode )
        {
            final GraphTreeNode node = (GraphTreeNode) obj;
            return this.node == node.node;
        }
        return false;
    }

    public int hashCode()
    {
        return node.hashCode();
    }
}
