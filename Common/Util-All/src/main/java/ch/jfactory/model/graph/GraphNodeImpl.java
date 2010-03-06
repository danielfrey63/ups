package ch.jfactory.model.graph;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 * @castor.class
 */
public class GraphNodeImpl extends AbsSimplePersistentGraphNode implements Comparable
{
    public void printEdges( final Class type )
    {
        getChildrenEdges().printEdges( type );
    }

    /**
     * @see GraphNode#getChildren()
     */
    public GraphNodeList getChildren()
    {
        return getChildrenEdges().getOthers();
    }

    /**
     * @see GraphNode#getParents()
     */
    public GraphNodeList getParents()
    {
        return getParentEdges().getOthers();
    }

    /**
     * Use this method if you want to overwrite the replacement of children.
     *
     * @see GraphNode#setChildren(GraphNodeList, Class, Class)
     */
    public void setChildren( final GraphNodeList children, final Class type, final Class role )
    {
        getChildrenEdges().setOthers( children, type, role );
    }

    /**
     * This method redirects to {@link #addChild(int, GraphNode)}.
     *
     * @see GraphNode#addChild(GraphNode)
     */
    public void addChild( final GraphNode child )
    {
        addChild( getChildrenEdges().size(), child );
    }

    /**
     * This method redirects to {@link #addChild(int, GraphNode, Role)}.
     *
     * @see GraphNode#addChild(int, GraphNode)
     */
    public void addChild( final int index, final GraphNode child )
    {
        addChild( index, child, Role.ROLE_NULL );
    }

    /**
     * This method redirects to {@link #addChild(int, GraphNode, Role)}.
     *
     * @see GraphNode#addChild(GraphNode, Role)
     */
    public void addChild( final GraphNode child, final Role role )
    {
        addChild( getChildrenEdges().size(), child, role );
    }

    /**
     * Use this method if you want to overwrite the addition of a child.
     *
     * @see GraphNode#addChild(int, GraphNode, Role)
     */
    public void addChild( final int index, final GraphNode child, final Role role )
    {
        getChildrenEdges().add( index, child, role );
    }

    /**
     * This method redirects to {@link #addChild(int, GraphNode, Role)}.
     *
     * @see GraphNode#addNewChild(int, String, Class)
     */
    public GraphNode addNewChild( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addChild( index, instance );
        return instance;
    }

    /**
     * Use this method if you want to overwrite the deletion of a child.
     *
     * @see GraphNode#deleteChild(GraphNode)
     */
    public boolean deleteChild( final GraphNode child )
    {
        return getChildrenEdges().delete( child );
    }

    /**
     * This method redirects to {@link #deleteChild(GraphNode)}.
     *
     * @see GraphNode#deleteChildren(Class, Class)
     */
    public void deleteChildren( final Class type, final Class role )
    {
        final GraphNodeList list = getChildren( type, role );
        for ( int i = 0; i < list.size(); i++ )
        {
            deleteChild( list.get( i ) );
        }
    }

    /**
     * Use this method if you want to overwrite the removal from a child.
     *
     * @see GraphNode#removeFromChild(GraphNode)
     */
    public boolean removeFromChild( final GraphNode child )
    {
        return getChildrenEdges().removeLinkTo( child );
    }

    /**
     * Use this method if you want to overwrite the replacement of parents.
     *
     * @see GraphNode#setParents(GraphNodeList, Class, Class)
     */
    public void setParents( final GraphNodeList parents, final Class type, final Class role )
    {
        getParentEdges().setOthers( parents, type, role );
    }

    /**
     * This method redirects to {@link #addParent(int, GraphNode)}.
     *
     * @see GraphNode#addParent(GraphNode)
     */
    public void addParent( final GraphNode parent )
    {
        addParent( getParentEdges().size(), parent );
    }

    /**
     * This method redirects to {@link #addParent(int, GraphNode, Role)}.
     *
     * @see GraphNode#addParent(int, GraphNode)
     */
    public void addParent( final int index, final GraphNode parent )
    {
        addParent( index, parent, Role.ROLE_NULL );
    }

    /**
     * This method redirects to {@link #addParent(int, GraphNode, Role)}.
     *
     * @see GraphNode#addParent(GraphNode, Role)
     */
    public void addParent( final GraphNode parent, final Role role )
    {
        addParent( getParentEdges().size(), parent, role );
    }

    /**
     * Use this method if you want to overwrite the addition of a parent.
     *
     * @see GraphNode#addParent(int, GraphNode, Role)
     */
    public void addParent( final int index, final GraphNode parent, final Role role )
    {
        getParentEdges().add( index, parent, role );
    }

    /**
     * This method redirects to {@link #addParent(int, GraphNode)}.
     *
     * @see GraphNode#addNewParent(int, String, Class)
     */
    public GraphNode addNewParent( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addParent( index, instance );
        return instance;
    }

    /**
     * Use this method if you want to overwrite the deletion of a parent.
     *
     * @see GraphNode#deleteParent(GraphNode)
     */
    public boolean deleteParent( final GraphNode parent )
    {
        return getParentEdges().delete( parent );
    }

    /**
     * This method redirects to {@link #deleteParent(GraphNode)}.
     *
     * @see GraphNode#deleteParents(Class, Class)
     */
    public void deleteParents( final Class type, final Class role )
    {
        final GraphNodeList list = getParents( type, role );
        for ( int i = 0; i < list.size(); i++ )
        {
            deleteParent( list.get( i ) );
        }
    }

    /**
     * Use this method if you want to overwrite the removal from a parent.
     *
     * @see GraphNode#removeFromParent(GraphNode)
     */
    public boolean removeFromParent( final GraphNode parent )
    {
        return getParentEdges().removeLinkTo( parent );
    }

    /**
     * @see Comparable
     */
    public int compareTo( final Object o )
    {
        final GraphNode node = (GraphNode) o;
        return this.getRank() - node.getRank();
    }

    private void writeObject( final ObjectOutputStream s ) throws IOException
    {
        s.defaultWriteObject();
    }

    private void readObject( final ObjectInputStream s ) throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
    }

    public GraphNode getAsGraphNode()
    {
        return this;
    }
}
