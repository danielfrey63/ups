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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public abstract class AbsSimpleGraphNode implements GraphNode, Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AbsSimpleGraphNode.class );

    private static final boolean DEBUG_NODE_NAMES = false;

    public static final Class TYPES_ALL = Object.class;

    private GraphEdgeList children;

    private final GraphEdgeList parents;

    private String name;

    private int id;

    private int rank;

    public AbsSimpleGraphNode()
    {
        children = new GraphEdgeList();
        children.setReferrer( this );
        children.setListType( GraphEdgeList.LIST_CHILD );
        parents = new GraphEdgeList();
        parents.setReferrer( this );
        parents.setListType( GraphEdgeList.LIST_PARENT );
    }

    /**
     * Returns the children.
     *
     * @return GraphEdgeList
     */
    public GraphEdgeList getChildrenEdges()
    {
        return children;
    }

    /**
     * Returns the parents.
     *
     * @return GraphEdgeList
     */
    public GraphEdgeList getParentEdges()
    {
        return parents;
    }

    public boolean isType( final Class type )
    {
        return type.isAssignableFrom( this.getClass() );
    }

    public int getId()
    {
        LOGGER.trace( "getId(): " + id );
        return id;
    }

    public String getName()
    {
        LOGGER.trace( "getName(): " + name );
        return name;
    }

    public int getRank()
    {
        LOGGER.trace( "getRank(): " + rank );
        return rank;
    }

    public void setId( final int id )
    {
        LOGGER.trace( "setId(" + id + ")" );
        this.id = id;
    }

    public void setName( final String name )
    {
        LOGGER.trace( "setName(" + name + ")" );
        this.name = name;
    }

    public void setRank( final int rank )
    {
        LOGGER.trace( "setRank(" + rank + ")" );
        this.rank = rank;
    }

    public GraphNodeList getChildren( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList list = AbsGraphModel.getFiltered( getChildren(), type );
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            result.add( node );
        }
        return result;
    }

    public GraphNodeList getAllChildren( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        result.addAll( getChildren( type ) );
        final GraphNodeList children = getChildren();
        for ( int i = 0; i < children.size(); i++ )
        {
            result.addAll( children.get( i ).getAllChildren( type ) );
        }
        return result;
    }

    /**
     * This method redirects to {@link #setChildren(GraphNodeList, Class)}.
     *
     * @see GraphNode#setChildren(GraphNodeList, Class)
     */
    public void setChildren( final GraphNodeList children )
    {
        setChildren( children, TYPES_ALL );
    }

    public GraphNodeList getParents( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList list = AbsGraphModel.getFiltered( getParents(), type );
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            result.add( node );
        }
        return result;
    }

    public GraphNodeList getAllParents( final Class type )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList parents = getParents( type );
        for ( int i = 0; i < parents.size(); i++ )
        {
            result.addAll( parents.get( i ).getAllParents( type ) );
        }
        return result;
    }

    public void setParents( final GraphNodeList parents )
    {
        setParents( parents, TYPES_ALL );
    }

    public String toString()
    {
        if ( DEBUG_NODE_NAMES )
        {
            final String clazz = this.getClass().getName();
            final String type = clazz.substring( clazz.lastIndexOf( '.', clazz.length() ) + 1,
                    clazz.length() );
            return name + " [type=" + type + ",id=" + id + ",rank=" + rank + "]";
        }
        else
        {
            return name;
        }
    }
}
