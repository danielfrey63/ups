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
        children.setReferer( this );
        children.setListType( GraphEdgeList.LIST_CHILD );
        parents = new GraphEdgeList();
        parents.setReferer( this );
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

    /**
     * Sets the children {@link GraphEdgeList} directly. Used by the model to construct the objects.
     *
     * @param children the new children to set
     */
    public void setChildrenEdges( final GraphEdgeList children )
    {
        this.children = children;
    }

    /** @see GraphNode#isType(Class) */
    public boolean isType( final Class type )
    {
        return type.isAssignableFrom( this.getClass() );
    }

    /** @see GraphNode#getId() */
    public int getId()
    {
        LOGGER.trace( "getId(): " + id );
        return id;
    }

    /** @see GraphNode#getName() */
    public String getName()
    {
        LOGGER.trace( "getName(): " + name );
        return name;
    }

    /** @see GraphNode#getRank() */
    public int getRank()
    {
        LOGGER.trace( "getRank(): " + rank );
        return rank;
    }

    /** @see GraphNode#setId(int) */
    public void setId( final int id )
    {
        LOGGER.trace( "setId(" + id + ")" );
        this.id = id;
    }

    /** @see GraphNode#setName(String) */
    public void setName( final String name )
    {
        LOGGER.trace( "setName(" + name + ")" );
        this.name = name;
    }

    /** @see GraphNode#setRank(int) */
    public void setRank( final int rank )
    {
        LOGGER.trace( "setRank(" + rank + ")" );
        this.rank = rank;
    }

    /** @see GraphNode#getChildren(Class) */
    public GraphNodeList getChildren( final Class type )
    {
        return getChildren( type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#getChildren(Class, Class) */
    public GraphNodeList getChildren( final Class type, final Class role )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList list = AbsGraphModel.getFiltered( getChildren(), type );
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            final Role roleToTest = getChildrenEdges().getRole( node );
            if ( role.isAssignableFrom( roleToTest.getClass() ) )
            {
                result.add( node );
            }
        }
        return result;
    }

    /** @see GraphNode#getAllChildren(Class) */
    public GraphNodeList getAllChildren( final Class type )
    {
        return getAllChildren( type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#getAllChildren(Class) */
    public GraphNodeList getAllChildren( final Class type, final Class role )
    {
        final GraphNodeList result = new GraphNodeList();
        result.addAll( getChildren( type, role ) );
        final GraphNodeList children = getChildren();
        for ( int i = 0; i < children.size(); i++ )
        {
            result.addAll( children.get( i ).getAllChildren( type, role ) );
        }
        return result;
    }

    /** @see GraphNode#getChildRole(GraphNode) */
    public Role getChildRole( final GraphNode node )
    {
        return getChildrenEdges().getRole( node );
    }

    /** @see GraphEdgeList#setRole(GraphNode, Role) */
    public void setChildRole( final GraphNode node, final Role role )
    {
        getChildrenEdges().setRole( node, role );
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

    /** @see GraphNode#setChildren(GraphNodeList, Class) */
    public void setChildren( final GraphNodeList children, final Class type )
    {
        setChildren( children, type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#deleteChildren(Class) */
    public void deleteChildren( final Class type )
    {
        deleteChildren( type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#getParents(Class) */
    public GraphNodeList getParents( final Class type )
    {
        return getParents( type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#getChildren(Class, Class) */
    public GraphNodeList getParents( final Class type, final Class role )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList list = AbsGraphModel.getFiltered( getParents(), type );
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            final Role roleToTest = getParentEdges().getRole( node );
            if ( role.isAssignableFrom( roleToTest.getClass() ) )
            {
                result.add( node );
            }
        }
        return result;
    }

    /** @see GraphNode#getAllParents(Class) */
    public GraphNodeList getAllParents( final Class type )
    {
        return getAllParents( type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#getAllParents(Class, Class) */
    public GraphNodeList getAllParents( final Class type, final Class role )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList parents = getParents( type, role );
        for ( int i = 0; i < parents.size(); i++ )
        {
            result.addAll( parents.get( i ).getAllParents( type, role ) );
        }
        return result;
    }

    /** @see GraphNode#getParentRole(GraphNode) */
    public Role getParentRole( final GraphNode node )
    {
        return getParentEdges().getRole( node );
    }

    /** @see GraphNode#setParentRole(GraphNode, Role) */
    public void setParentRole( final GraphNode node, final Role role )
    {
        getParentEdges().setRole( node, role );
    }

    /** @see GraphNode#setParents(GraphNodeList) */
    public void setParents( final GraphNodeList parents )
    {
        setParents( parents, TYPES_ALL );
    }

    /** @see GraphNode#setParents(GraphNodeList, Class) */
    public void setParents( final GraphNodeList parents, final Class type )
    {
        setParents( parents, type, Role.CLASSES_ALL );
    }

    /** @see GraphNode#deleteParents(Class) */
    public void deleteParents( final Class type )
    {
        deleteParents( type, Role.CLASSES_ALL );
    }

    /** @see Object#toString() */
    public String toString()
    {
        if ( DEBUG_NODE_NAMES )
        {
            final String clazz = this.getClass().getName();
            final String type = clazz.substring( clazz.lastIndexOf( '.', clazz.length() ) + 1,
                    clazz.length() );
            return name + " [type=" + type + ",id=" + id + "]";
        }
        else
        {
            return name;
        }
    }
}
