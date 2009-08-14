package ch.jfactory.model.graph;

import java.io.Serializable;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 * @castor.class
 */
public class GraphEdgeImpl implements GraphEdge, Serializable
{

    private int id;

    private int rank;

    private GraphNode child;

    private GraphNode parent;

    private Role role;

    private GraphEdge recursive;

    public GraphEdgeImpl()
    {
    }

    public GraphEdgeImpl(final int id, final GraphNode parent, final GraphNode child)
    {
        this(id);
        this.parent = parent;
        this.child = child;
    }

    public GraphEdgeImpl(final int id, final GraphNode parent, final GraphNode child, final Role role, final GraphEdge recursive)
    {

        this(id, parent, child);
        setRole(role);
        setRecursive(recursive);
    }

    public GraphEdgeImpl(final int id)
    {
        setId(id);
    }

    /** @see ch.jfactory.model.graph.GraphEdge#isRole(Class) */
    public boolean isRole(final Class role)
    {
        return role.isAssignableFrom(role.getClass());
    }

    /**
     * @castor.field
     * @castor.field-xml
     * @see ch.jfactory.model.graph.GraphEdge#getChild()
     */
    public GraphNode getChild()
    {
        return child;
    }

    /**
     * @castor.field
     * @castor.field-xml
     * @see ch.jfactory.model.graph.GraphEdge#getParent()
     */
    public GraphNode getParent()
    {
        return parent;
    }

    /**
     * @castor.field
     * @castor.field-xml
     * @see ch.jfactory.model.graph.GraphEdge#getRole()
     */
    public Role getRole()
    {
        if (role == null)
        {
            return Role.ROLE_NULL;
        }
        else
        {
            return role;
        }
    }

    /** @see ch.jfactory.model.graph.GraphEdge#getRecursive() */
    public GraphEdge getRecursive()
    {
        return recursive;
    }

    /**
     * @castor.field
     * @castor.field-xml
     * @see ch.jfactory.model.graph.GraphEdge#getRank()
     */
    public int getRank()
    {
        return rank;
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setChild(GraphNode) */
    public void setChild(final GraphNode child)
    {
        final GraphModel model = AbsGraphModel.getModel();
        model.addRemoved(this);
        this.child = child;
        model.addChanged(this);
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setParent(GraphNode) */
    public void setParent(final GraphNode parent)
    {
        final GraphModel model = AbsGraphModel.getModel();
        model.addRemoved(this);
        this.parent = parent;
        model.addChanged(this);
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setRole(Role) */
    public void setRole(final Role role)
    {
        this.role = (role == Role.ROLE_NULL ? null : role);
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setRecursive(GraphEdge) */
    public void setRecursive(final GraphEdge recursive)
    {
        this.recursive = recursive;
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setRank(int) */
    public void setRank(final int rank)
    {
        this.rank = rank;
    }

    /**
     * @castor.field
     * @castor.field-xml
     * @see ch.jfactory.model.graph.GraphEdge#getId()
     */
    public int getId()
    {
        return id;
    }

    /** @see ch.jfactory.model.graph.GraphEdge#setId(int) */
    public void setId(final int id)
    {
        this.id = id;
    }

    /** @see java.lang.Object#toString() */
    public String toString()
    {
        String pId = "null node (NN)";
        String cId = pId;
        if (parent != null)
        {
            pId = parent + "(" + parent.hashCode() + ")";
        }
        if (child != null)
        {
            cId = child + "(" + child.hashCode() + ")";
        }
        return "[p=" + pId + ",c=" + cId
                + ",rank=" + rank + ",role=" + role +
                (role == null ? "" : "(" + role.hashCode() + ")")
                + ",rec=" + recursive + "]";
    }

}

// $Log: GraphEdgeImpl.java,v $
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
