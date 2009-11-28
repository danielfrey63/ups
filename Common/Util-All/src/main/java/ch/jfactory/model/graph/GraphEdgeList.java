package ch.jfactory.model.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class GraphEdgeList implements Serializable
{
    public static final int LIST_CHILD = 0;

    public static final int LIST_PARENT = 1;

    private static final GraphModel MODEL = AbsGraphModel.getModel();

    private final ArrayList list = new ArrayList();

    private GraphNode referer;

    private int listType;

    public void printEdges( final Class type )
    {
        for ( final Object aList : list )
        {
            final GraphEdge edge = (GraphEdge) aList;
            if ( getOther( edge ).isType( type ) )
            {
                System.out.println( edge );
            }
        }
    }

    public void setReferer( final GraphNode referer )
    {
        this.referer = referer;
    }

    public void setListType( final int listType )
    {
        this.listType = listType;
    }

    private GraphNode getOther( final GraphEdge edge )
    {
        if ( listType == LIST_PARENT )
        {
            return edge.getParent();
        }
        else
        {
            return edge.getChild();
        }
    }

    /**
     * Returns the node for which children/parents are stored in this list.
     */
    private GraphNode getReferer( final GraphEdge edge )
    {
        if ( listType == LIST_PARENT )
        {
            return edge.getChild();
        }
        else
        {
            return edge.getParent();
        }
    }

    private void ajustRanks()
    {
        GraphEdge edge;
        for ( int i = 0; i < list.size(); i++ )
        {
            edge = (GraphEdge) list.get( i );
            final int old = edge.getRank();
            if ( old != i )
            {
                edge.setRank( i );
                MODEL.addChanged( edge );
            }
        }
    }

    /**
     * Returns a collection of all parent {@link GraphNode}s in this edge list.
     *
     * @return GraphNodeList of all parent {@link GraphNode}s
     */
    public GraphNodeList getOthers()
    {
        final GraphNodeList result = new GraphNodeList();
        for ( final Object aList : list )
        {
            final GraphEdge edge = (GraphEdge) aList;
            result.add( getOther( edge ) );
        }
        return result;
    }

    /**
     * Returns a collection of all parent {@link GraphNode}s in this edge list.
     *
     * @return GraphNodeList of all parent {@link GraphNode}s
     */
    public GraphNodeList getOthers( final Class type, final Class role )
    {
        final GraphNodeList result = new GraphNodeList();
        for ( final Object aList : list )
        {
            final GraphEdge edge = (GraphEdge) aList;
            final GraphNode node = getOther( edge );
            if ( node.isType( type ) && edge.isRole( role ) )
            {
                result.add( node );
            }
        }
        return result;
    }

    public Role getRole( final GraphNode node )
    {
        final GraphEdge edge = getEdge( node );
        if ( edge == null )
        {
            return Role.ROLE_NULL;
        }
        else
        {
            return edge.getRole();
        }
    }

    public void setRole( final GraphNode node, final Role role )
    {
        final GraphEdge edge = getEdge( node );
        if ( edge != null )
        {
            edge.setRole( role );
        }
    }

    private GraphEdge getEdge( final GraphNode node )
    {
        for ( final Object aList : list )
        {
            final GraphEdge edge = (GraphEdge) aList;
            final GraphNode other = getOther( edge );
            if ( other == node )
            {
                return edge;
            }
        }
        return null;
    }

    public void setOthers( final GraphNodeList others )
    {
        list.clear();
        addAll( others );
    }

    public void setOthers( final GraphNodeList others, final Class type, final Class role )
    {
        if ( getOthers( type, role ).size() == others.size() )
        {
            // Update existing edges instead of removing/creating
            int i = 0;
            for ( final Object aList : list )
            {
                final GraphEdge edge = (GraphEdge) aList;
                final GraphNode node = getOther( edge );
                if ( node.isType( type ) && edge.isRole( role ) )
                {
                    if ( listType == LIST_CHILD )
                    {
                        edge.setChild( others.get( i++ ) );
                    }
                    else
                    {
                        edge.setParent( others.get( i++ ) );
                    }
                    MODEL.addChanged( edge );
                }
            }
        }
        else
        {
            for ( Iterator iter = list.iterator(); iter.hasNext(); )
            {
                final GraphEdge edge = (GraphEdge) iter.next();
                final GraphNode node = getOther( edge );
                if ( node.isType( type ) && edge.isRole( role ) )
                {
                    iter.remove();
                    MODEL.addRemoved( edge );
                }
            }
            addAll( others );
        }
    }

    public boolean add( final GraphEdge edge )
    {
        return add( list.size(), edge );
    }

    public boolean add( final int index, final GraphEdge edge )
    {
        if ( referer != getReferer( edge ) )
        {
            getReferer( edge );
            throw new IllegalStateException( "Referer is not in edges member: " + referer + "(" +
                    referer.hashCode() + ")" + " <-> " + edge );
        }

        if ( list.contains( edge ) )
        {
            return true;
        }

        boolean wasAdded = false;
        if ( !list.contains( edge ) )
        {
            final int oldSize = list.size();
            list.add( index, edge );
            wasAdded = ( oldSize != list.size() );
        }

        ajustRanks();

        // Try counterpart
        if ( wasAdded )
        {
            final GraphNode other = getOther( edge );
            if ( listType == LIST_CHILD )
            {
                other.addParent( referer, edge.getRole() );
            }
            else
            {
                other.addChild( referer, edge.getRole() );
            }
        }
        return wasAdded;
    }

    public boolean add( final GraphNode node )
    {
        return add( list.size(), node );
    }

    public boolean add( final int index, final GraphNode node )
    {
        final GraphEdge edge;
        if ( listType == LIST_CHILD )
        {
            edge = MODEL.createEdge( referer, node );
        }
        else
        {
            edge = MODEL.createEdge( node, referer );
        }
        return add( index, edge );
    }

    public boolean add( final int index, final GraphNode node, final Role role )
    {
        // Note: Role not used any more
        // Todo: Remove role definively
        final GraphEdge edge;
        if ( listType == LIST_CHILD )
        {
            edge = MODEL.createEdge( referer, node );
        }
        else
        {
            edge = MODEL.createEdge( node, referer );
        }
        ajustRanks();
        return add( index, edge );
    }

    public void addAll( final GraphNodeList list )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            add( list.get( i ) );
        }
    }

    public boolean removeLinkTo( final GraphNode node )
    {
        for ( Iterator iter = list.iterator(); iter.hasNext(); )
        {
            final GraphEdge edge = (GraphEdge) iter.next();
            final GraphNode other = getOther( edge );
            if ( other == node )
            {
                iter.remove();
                MODEL.addRemoved( edge );
                if ( listType == LIST_CHILD )
                {
                    other.removeFromParent( referer );
                }
                else
                {
                    other.removeFromChild( referer );
                }
                return true;
            }
        }
        return false;
    }

    public boolean delete( final GraphNode node )
    {
        for ( Iterator iter = list.iterator(); iter.hasNext(); )
        {
            final GraphEdge edge = (GraphEdge) iter.next();
            final GraphNode other = getOther( edge );
            if ( other == node )
            {
                iter.remove();
                MODEL.addRemoved( edge );
                boolean wasRemoved = false;
                if ( listType == LIST_CHILD )
                {
                    wasRemoved = other.deleteParent( referer );
                }
                else
                {
                    wasRemoved = other.deleteChild( referer );
                }
                if ( wasRemoved )
                {
                    MODEL.addRemoved( node );
                }
                return true;
            }
        }
        return false;
    }

    public int size()
    {
        return list.size();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return referer + ", " + ( listType == 0 ? "CHILD" : "PARENT" ) +
                " " + list.toString();
    }
}

// $Log: GraphEdgeList.java,v $
// Revision 1.2  2006/03/14 21:27:55  daniel_frey
// *** empty log message ***
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
