package ch.jfactory.component.tree;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author $Author $
 * @version $Revision $ $Date $
 */
public class GraphTreeNode extends AbstractMutableTreeNode
{
    /**
     * Mapping between {@link VirtualGraphTreeNode} objects and corresponding <code>GraphTreeNode</code> objects.
     */
    private static final HashMap cache = new HashMap();

    /**
     * The <code>GraphNode2TreeNode</code> being wrapped.
     */
    private final VirtualGraphTreeNode node;

    private GraphTreeNode( final VirtualGraphTreeNode node )
    {
        this.node = node;
    }

    private List getChildren()
    {
        List children = new ArrayList();
        final GraphNodeList childs = node.getChildren();
        final int len = childs.size();
        children = new ArrayList( len );
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
        GraphTreeNode result = (GraphTreeNode) cache.get( node );
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

    public static GraphTreeNode getNode( final JTree tree )
    {
        return getNode( tree.getSelectionPath() );
    }

    public VirtualGraphTreeNode getNode()
    {
        return node;
    }

    public GraphNode getDependent()
    {
        return node.getDependent();
    }

    public GraphTreeNode insertNew( final int index, final String name, final Class type )
    {
        final VirtualGraphTreeNode newChild = node.addNewTreeChild( index, name, type );
        return getTreeNode( newChild );
    }

    /**
     * Returns whether the given class is a supertype or interface, or the same type of this node.
     */
    public boolean isType( final Class clazz )
    {
        return node.isType( clazz );
    }

    public void deleteChild( final GraphTreeNode node )
    {
        getNode().deleteTreeChild( node.getNode() );
    }

    /**
     * @see TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt( final int index )
    {
        return (TreeNode) getChildren().get( index );
    }

    /**
     * @see TreeNode#getChildCount()
     */
    public int getChildCount()
    {
        return getChildren().size();
    }

    /**
     * @see TreeNode#getParent()
     */
    public TreeNode getParent()
    {
        final VirtualGraphTreeNode parentNode = node.getParent();
        if ( parentNode == null )
        {
            return null;
        }
        return getTreeNode( parentNode );
    }

    /**
     * @see TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren()
    {
        return true;
    }

    /**
     * @see MutableTreeNode#insert(MutableTreeNode, int)
     */
    public void insert( final MutableTreeNode child, final int index )
    {
        final VirtualGraphTreeNode childNode = ( (GraphTreeNode) child ).getNode();
        node.addTreeChild( index, childNode );
    }

    /**
     * @see MutableTreeNode#remove(MutableTreeNode)
     */
    public void remove( final MutableTreeNode treeNode )
    {
        final GraphTreeNode child = (GraphTreeNode) treeNode;
        node.removeTreeChild( child.getNode() );
    }

    /**
     * @see MutableTreeNode#setParent(MutableTreeNode)
     */
    public void setParent( final MutableTreeNode newParent )
    {
        throw new NoSuchMethodError( "setParent not implemented yet" );
    }

    /**
     * @see MutableTreeNode#setUserObject(Object)
     */
    public void setUserObject( final Object object )
    {
        if ( object instanceof String )
        {
            node.setName( (String) object );
            AbsGraphModel.getModel().setDirty( true );
        }
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return node.toString();
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals( final Object obj )
    {
        final GraphTreeNode node = (GraphTreeNode) obj;
        return this.node == node.node;
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode()
    {
        return node.hashCode();
    }
}
