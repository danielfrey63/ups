/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
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

    private final ArrayList<GraphEdge> list = new ArrayList<GraphEdge>();

    private GraphNode referrer;

    private int listType;

    public void setReferrer( final GraphNode referrer )
    {
        this.referrer = referrer;
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
     *
     * @param edge the edge to which the referrer is searched for
     * @return the referring node
     */
    private GraphNode getReferrer( final GraphEdge edge )
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

    private void adjustRanks()
    {
        GraphEdge edge;
        for ( int i = 0; i < list.size(); i++ )
        {
            edge = list.get( i );
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
            result.add( getOther( (GraphEdge) aList ) );
        }
        return result;
    }

    /**
     * Returns a collection of all parent {@link GraphNode}s in this edge list.
     *
     * @param type the class type to filter by
     * @return GraphNodeList of all parent {@link GraphNode}s
     */
    public GraphNodeList getOthers( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        for ( final Object aList : list )
        {
            final GraphEdge edge = (GraphEdge) aList;
            final GraphNode node = getOther( edge );
            if ( node.isType( type ) )
            {
                result.add( node );
            }
        }
        return result;
    }

    public void setOthers( final GraphNodeList others, final Class type )
    {
        if ( getOthers( type ).size() == others.size() )
        {
            // Update existing edges instead of removing/creating
            int i = 0;
            for ( final Object aList : list )
            {
                final GraphEdge edge = (GraphEdge) aList;
                final GraphNode node = getOther( edge );
                if ( node.isType( type ) )
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
            for ( Iterator<GraphEdge> iterator = list.iterator(); iterator.hasNext(); )
            {
                final GraphEdge edge = iterator.next();
                final GraphNode node = getOther( edge );
                if ( node.isType( type ) )
                {
                    iterator.remove();
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
        if ( referrer != getReferrer( edge ) )
        {
            getReferrer( edge );
            throw new IllegalStateException( "Referrer is not in edges member: " + referrer + "(" +
                    referrer.hashCode() + ")" + " <-> " + edge );
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

        adjustRanks();

        // Try counterpart
        if ( wasAdded )
        {
            final GraphNode other = getOther( edge );
            if ( listType == LIST_CHILD )
            {
                other.addParent( referrer );
            }
            else
            {
                other.addChild( referrer );
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
        // Note: Role not used any more
        final GraphEdge edge;
        if ( listType == LIST_CHILD )
        {
            edge = MODEL.createEdge( referrer, node );
        }
        else
        {
            edge = MODEL.createEdge( node, referrer );
        }
//        adjustRanks();
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
        for ( Iterator<GraphEdge> iterator = list.iterator(); iterator.hasNext(); )
        {
            final GraphEdge edge = iterator.next();
            final GraphNode other = getOther( edge );
            if ( other == node )
            {
                iterator.remove();
                MODEL.addRemoved( edge );
                if ( listType == LIST_CHILD )
                {
                    other.removeFromParent( referrer );
                }
                else
                {
                    other.removeFromChild( referrer );
                }
                return true;
            }
        }
        return false;
    }

    public boolean delete( final GraphNode node )
    {
        for ( Iterator<GraphEdge> iterator = list.iterator(); iterator.hasNext(); )
        {
            final GraphEdge edge = iterator.next();
            final GraphNode other = getOther( edge );
            if ( other == node )
            {
                iterator.remove();
                MODEL.addRemoved( edge );
                final boolean wasRemoved;
                if ( listType == LIST_CHILD )
                {
                    wasRemoved = other.deleteParent( referrer );
                }
                else
                {
                    wasRemoved = other.deleteChild( referrer );
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

    /** @see Object#toString() */
    public String toString()
    {
        return referrer + ", " + ( listType == 0 ? "CHILD" : "PARENT" ) +
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
