package ch.jfactory.model.graph;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface GraphNode
{
    public int getId();

    public String getName();

    public int getRank();

    public void setId( int id );

    public void setName( String name );

    public void setRank( int rank );

    public boolean isType( Class type );

    public GraphNodeList getChildren();

    public GraphNodeList getChildren( Class type );

    /**
     * Collects all children of the given type in level-order.
     *
     * @param type the type to use as a filter
     * @return the class of the children
     */
    public GraphNodeList getAllChildren( Class type );

    public void setChildren( GraphNodeList children );

    public void setChildren( GraphNodeList children, Class type );

    /**
     * Adds the given child to this <code>GraphNode</code> and assigns to this relation the given role.
     *
     * @param child the child node
     */
    public void addChild( GraphNode child );

    public void addChild( int index, GraphNode child );

    public GraphNode addNewChild( int index, String name, Class type );

    public void deleteChildren( Class type );

    /**
     * Remove the given <code>GraphNode<(code> from list of children of this <code>GraphNode</code>.
     *
     * @param child the child to remove from this
     * @return whether the child was removed
     */
    public boolean deleteChild( GraphNode child );

    public boolean removeFromChild( GraphNode child );

    public GraphNodeList getParents();

    public GraphNodeList getParents( Class type );

    /**
     * Collects all parents of the given type in level-order.
     *
     * @param type the type to use as a filter
     * @return the list of graph nodes
     */
    public GraphNodeList getAllParents( Class type );

    public void setParents( GraphNodeList parents );

    public void setParents( GraphNodeList parents, Class type );

    /**
     * Adds the given parent to this <code>GraphNode</code> and assigns to this relation the given role.
     *
     * @param parent the graph node
     */
    public void addParent( GraphNode parent );

    public void addParent( int index, GraphNode parent );

    public GraphNode addNewParent( int index, String name, Class type );

    public void deleteParents( Class type );

    /**
     * Remove the given <code>GraphNode<(code> from the list of parents of this <code>GraphNode</code>.
     *
     * @param parent the parent to remove from this
     * @return whether the parent was removed
     */
    public boolean deleteParent( GraphNode parent );

    public boolean removeFromParent( GraphNode parent );
}
