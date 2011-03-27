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

import ch.jfactory.lang.ToStringComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class GraphNodeList
{
    private static final Comparator<GraphNode> COMPARATOR = new ToStringComparator<GraphNode>();

    private final ArrayList<GraphNode> list = new ArrayList<GraphNode>();

    public GraphNodeList()
    {
    }

    public GraphNodeList( final ArrayList<GraphNode> list )
    {
        this.list.addAll( list );
    }

    public GraphNodeList( final GraphNode element )
    {
        list.add( element );
    }

    public GraphNode get( final int index )
    {
        if ( list.size() > index && index >= 0 )
        {
            return list.get( index );
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
            final GraphNode nd = list.get( i );
            if ( nd == node )
            {
                return i;
            }
        }
        return -1;
    }

    public GraphNode[] getAll()
    {
        return list.toArray( new GraphNode[list.size()] );
    }

    public GraphNode[] getAll( final Object[] arrayType )
    {
        return (GraphNode[]) list.toArray( arrayType );
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

    public GraphNodeList intersect( final GraphNodeList other )
    {
        final ArrayList<GraphNode> copy = new ArrayList<GraphNode>( list );
        copy.retainAll( other.list );
        return new GraphNodeList( copy );
    }

    public void sort()
    {
        sort( COMPARATOR );
    }

    public void sort( final Comparator<GraphNode> comparator )
    {
        Collections.sort( list, comparator );
    }

    /** @see Object#toString() */
    public String toString()
    {
        return list.toString();
    }
}
