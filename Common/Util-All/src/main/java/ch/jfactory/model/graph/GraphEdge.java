package ch.jfactory.model.graph;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface GraphEdge
{

    /**
     * Returns the id.
     *
     * @return int
     */
    public int getId();

    /** Returns whether this is of the given role. */
    public boolean isRole(Class role);

    /**
     * Returns the child.
     *
     * @return GraphNode
     */
    public GraphNode getChild();

    /**
     * Returns the parent.
     *
     * @return GraphNode
     */
    public GraphNode getParent();

    /**
     * Returns the role.
     *
     * @return Role
     */
    public Role getRole();

    /**
     * Returns the text.
     *
     * @return String
     */
    public GraphEdge getRecursive();

    /**
     * Returns the rank.
     *
     * @return int
     */
    public int getRank();

    /**
     * Sets the id.
     *
     * @param int the new id to set
     */
    public void setId(int id);

    /**
     * Sets the child.
     *
     * @param child The child to set
     */
    public void setChild(GraphNode child);

    /**
     * Sets the parent.
     *
     * @param parent The parent to set
     */
    public void setParent(GraphNode parent);

    /**
     * Sets the role.
     *
     * @param role The role to set
     */
    public void setRole(Role role);

    /**
     * Sets the text.
     *
     * @param text The text to set
     */
    public void setRecursive(GraphEdge recursive);

    /**
     * Sets the rank.
     *
     * @param rank
     */
    public void setRank(int rank);
}
