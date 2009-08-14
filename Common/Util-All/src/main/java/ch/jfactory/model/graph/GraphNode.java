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

    public void setId(int id);

    public void setName(String name);

    public void setRank(int rank);

    public boolean isType(Class type);

    public GraphNodeList getChildren();

    public GraphNodeList getChildren(Class type);

    public GraphNodeList getChildren(Class type, Class role);

    /**
     * Collects all children of the given type in level-order.
     *
     * @param type the type to use as a filter
     */
    public GraphNodeList getAllChildren(Class type);

    public GraphNodeList getAllChildren(Class type, Class role);

    public Role getChildRole(GraphNode node);

    public void setChildRole(GraphNode node, Role role);

    public void setChildren(GraphNodeList children);

    public void setChildren(GraphNodeList children, Class type);

    public void setChildren(GraphNodeList children, Class type, Class role);

    public void addChild(GraphNode child);

    public void addChild(int index, GraphNode child);

    /** Adds the given child to this <code>GraphNode</code> and assigns to this relation the given role. */
    public void addChild(GraphNode child, Role role);

    public void addChild(int index, GraphNode child, Role role);

    public GraphNode addNewChild(int index, String name, Class type);

    public void deleteChildren(Class type);

    public void deleteChildren(Class type, Class role);

    /**
     * Remove the given <code>GraphNode<(code> from list of children of this <code>GraphNode</code>.
     *
     * @param parent the child to remove from this
     * @return whether the child was removed
     */
    public boolean deleteChild(GraphNode child);

    public boolean removeFromChild(GraphNode child);

    public GraphNodeList getParents();

    public GraphNodeList getParents(Class type);

    public GraphNodeList getParents(Class type, Class role);

    /**
     * Collects all parents of the given type in level-order.
     *
     * @param type the type to use as a filter
     */
    public GraphNodeList getAllParents(Class type);

    public GraphNodeList getAllParents(Class type, Class role);

    public Role getParentRole(GraphNode node);

    public void setParentRole(GraphNode node, Role role);

    public void setParents(GraphNodeList parents);

    public void setParents(GraphNodeList parents, Class type);

    public void setParents(GraphNodeList parents, Class type, Class role);

    public void addParent(GraphNode parent);

    public void addParent(int index, GraphNode parent);

    /** Adds the given parent to this <code>GraphNode</code> and assigns to this relation the given role. */
    public void addParent(GraphNode parent, Role role);

    public void addParent(int index, GraphNode parent, Role role);

    public GraphNode addNewParent(int index, String name, Class type);

    public void deleteParents(Class type);

    public void deleteParents(Class type, Class role);

    /**
     * Remove the given <code>GraphNode<(code> from the list of parents of this <code>GraphNode</code>.
     *
     * @param parent the parent to remove from this
     * @return whether the parent was removed
     */
    public boolean deleteParent(GraphNode parent);

    public boolean removeFromParent(GraphNode parent);
}
