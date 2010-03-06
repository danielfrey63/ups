package ch.jfactory.model.graph;

import ch.jfactory.lang.ToStringComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class GraphNodeList
{
    private static final Comparator COMPARATOR = new ToStringComparator();

    private final ArrayList list = new ArrayList();

    public GraphNodeList()
    {
    }

    public GraphNodeList( final GraphNode element )
    {
        list.add( element );
    }

    public GraphNode get( final int index )
    {
        if ( list.size() > index && index >= 0 )
        {
            return (GraphNode) list.get( index );
        }
        else
        {
            return null;
        }
    }

    public int get( final GraphNode node )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode nd = (GraphNode) list.get( i );
            if ( nd == node )
            {
                return i;
            }
        }
        return -1;
    }

    public GraphNode[] getAll()
    {
        return (GraphNode[]) list.toArray( new GraphNode[list.size()] );
    }

    public GraphNode[] getAll( final Object[] arraytype )
    {
        return (GraphNode[]) list.toArray( arraytype );
    }

    public void add( final GraphNode node )
    {
        list.add( node );
    }

    public void add( final int index, final GraphNode node )
    {
        list.add( index, node );
    }

    public void addAll( final GraphNodeList list )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            this.list.add( list.get( i ) );
        }
    }

    public boolean remove( final GraphNode toRemove )
    {
        return list.remove( toRemove );
    }

    public void removeAllNodes( final GraphNodeList toRemove )
    {
        list.removeAll( Arrays.asList( toRemove.getAll() ) );
    }

    public int size()
    {
        return list.size();
    }

    public void clear()
    {
        list.clear();
    }

    public GraphNodeList getChildren( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        for ( final Object aList : list )
        {
            final GraphNode node = (GraphNode) aList;
            result.addAll( node.getChildren( type ) );
        }
        return result;
    }

    public boolean contains( final GraphNode node )
    {
        return list.contains( node );
    }

    public void sort()
    {
        sort( COMPARATOR );
    }

    public void sort( final Comparator comparator )
    {
        Collections.sort( list, comparator );
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return list.toString();
    }
}
