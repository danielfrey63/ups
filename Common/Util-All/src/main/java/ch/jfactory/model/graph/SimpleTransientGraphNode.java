package ch.jfactory.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This node is used as a temporary GraphNode which is not saved. All its links (GraphEdges) wont be saved neither.
 * Assosiated GraphNodes wont have any role or rank. Trying to filter associated GraphNodes will return the full set of
 * associated GraphNodes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class SimpleTransientGraphNode extends AbsSimpleGraphNode
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SimpleTransientGraphNode.class );

    /**
     * Mapping between nodes that are used in other <code>SimpleTransientGraphNode</code>s as children and these other
     * instances.
     */
    private static final HashMap childrenReferences = new HashMap();

    /**
     * Mapping between nodes that are used in other <code>SimpleTransientGraphNode</code>s as parents and these other
     * instances.
     */
    private static final HashMap parentsReferences = new HashMap();

    /**
     * References to children <code>SimpleTransientGraphNode</code>s.
     */
    private GraphNodeList children = new GraphNodeList();

    /**
     * References to parent <code>SimpleTransientGraphNode</code>s.
     */
    private final GraphNodeList parents = new GraphNodeList();

    private ArrayList getReferencesFor( final GraphNode node, final HashMap references )
    {
        ArrayList list = (ArrayList) references.get( node );
        if ( list == null )
        {
            list = new ArrayList();
            references.put( node, list );
        }
        return list;
    }

    /**
     * @see GraphNode#getChildren()
     */
    public GraphNodeList getChildren()
    {
        return children;
    }

    /**
     * @see GraphNode#getChildren(Class, Class)
     */
    public GraphNodeList getChildren( final Class type, final Class role )
    {
        return getChildren();
    }

    /**
     * @see GraphNode#setChildren(GraphNodeList)
     */
    public void setChildren( final GraphNodeList children )
    {
        this.children = children;
    }

    /**
     * @see GraphNode#setChildren(GraphNodeList, Class)
     */
    public void setChildren( final GraphNodeList children, final Class type, final Class role )
    {
        throw new NoSuchMethodError( "setChildren(children, type, role) "
                + "not supported. Use setChildren() instead." );
    }

    /**
     * @see GraphNode#addChild(GraphNode)
     */
    public void addChild( final GraphNode child )
    {
        addChild( children.size(), child );
    }

    /**
     * @see GraphNode#addChild(int, GraphNode)
     */
    public void addChild( final int index, final GraphNode child )
    {
        final ArrayList list = getReferencesFor( child, childrenReferences );
        if ( !list.contains( this ) )
        {
            list.add( this );
            children.add( index, child );
            child.addParent( this );
        }
    }

    /**
     * @see GraphNode#addChild(GraphNode, Role)
     */
    public void addChild( final GraphNode child, final Role role )
    {
        LOGGER.info( "SimpleTransientGraphNodes don't support roles." );
        addChild( child );
    }

    /**
     * @see GraphNode#addChild(int, GraphNode, Role)
     */
    public void addChild( final int index, final GraphNode child, final Role role )
    {
        LOGGER.warn( "SimpleTransientGraphNodes don't support roles." );
        addChild( index, child );
    }

    /**
     * @see GraphNode#getChildRole(GraphNode)
     */
    public Role getChildRole( final GraphNode node )
    {
        throw new NoSuchMethodError( "Roles not supported." );
    }

    /**
     * @see GraphNode#addNewChild(int, String, Class)
     */
    public GraphNode addNewChild( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addChild( index, instance );
        return instance;
    }

    /**
     * @see GraphNode#deleteChild(GraphNode)
     */
    public boolean deleteChild( final GraphNode child )
    {
        final int size = children.size();
        children.remove( child );
        ArrayList list = getReferencesFor( child, childrenReferences );
        list = new ArrayList( list );
        for ( final Object aList : list )
        {
            final GraphNode other = (GraphNode) aList;
            other.removeFromParent( child );
        }
        return children.size() != size;
    }

    /**
     * @see GraphNode#deleteChildren(Class)
     */
    public void deleteChildren( final Class type, final Class role )
    {
        for ( int i = 0; i < children.size(); i++ )
        {
            final GraphNode node = children.get( i );
            if ( type.isAssignableFrom( node.getClass() ) )
            {
                deleteChild( node );
            }
        }
    }

    /**
     * @see GraphNode#removeFromChild(GraphNode)
     */
    public boolean removeFromChild( final GraphNode child )
    {
        final int size = children.size();
        if ( children.remove( child ) )
        {
            final ArrayList list = getReferencesFor( child, childrenReferences );
            list.remove( this );
            childrenReferences.put( child, list );
            child.removeFromChild( this );
        }
        return children.size() != size;
    }

    /**
     * @see GraphNode#getParents()
     */
    public GraphNodeList getParents()
    {
        return parents;
    }

    /**
     * @see GraphNode#addParent(GraphNode)
     */
    public void addParent( final GraphNode parent )
    {
        addParent( parents.size(), parent );
    }

    /**
     * @see GraphNode#addParent(int, GraphNode)
     */
    public void addParent( final int index, final GraphNode parent )
    {
        final ArrayList list = getReferencesFor( parent, parentsReferences );
        if ( !list.contains( this ) )
        {
            list.add( this );
            parents.add( index, parent );
            parent.addChild( this );
        }

    }

    /**
     * @see GraphNode#addParent(GraphNode, Role)
     */
    public void addParent( final GraphNode parent, final Role role )
    {
        addParent( parent );
    }

    /**
     * @see GraphNode#addParent(int, GraphNode, Role)
     */
    public void addParent( final int index, final GraphNode parent, final Role role )
    {
        addParent( index, parent );
    }

    /**
     * @see GraphNode#addNewChild(int, String, Class)
     */
    public GraphNode addNewParent( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addParent( index, instance );
        return instance;
    }

    /**
     * @see GraphNode#deleteParent(GraphNode)
     */
    public boolean deleteParent( final GraphNode parent )
    {
        final int size = parents.size();
        parents.remove( parent );
        ArrayList list = getReferencesFor( parent, parentsReferences );
        list = new ArrayList( list );
        for ( final Object aList : list )
        {
            final GraphNode other = (GraphNode) aList;
            other.removeFromParent( parent );
        }
        return parents.size() != size;
    }

    /**
     * @see GraphNode#deleteParents(Class)
     */
    public void deleteParents( final Class type, final Class role )
    {
        for ( int i = 0; i < parents.size(); i++ )
        {
            final GraphNode node = parents.get( i );
            if ( type.isAssignableFrom( node.getClass() ) )
            {
                deleteParent( node );
            }
        }
    }

    /**
     * @see GraphNode#removeFromParent(GraphNode)
     */
    public boolean removeFromParent( final GraphNode parent )
    {
        final int size = parents.size();
        if ( parents.remove( parent ) )
        {
            final ArrayList list = getReferencesFor( parent, parentsReferences );
            list.remove( this );
            parentsReferences.put( parent, list );
            parent.removeFromChild( this );
        }
        return parents.size() != size;
    }

    /**
     * @see GraphNode#setParents(GraphNodeList)
     */
    public void setParents( final GraphNodeList parents )
    {
        throw new NoSuchMethodError( "setParents(GraphNodeList) not implemented yet." );
    }

    /**
     * @see GraphNode#setParents(GraphNodeList, Class)
     */
    public void setParents( final GraphNodeList parents, final Class type, final Class role )
    {
        throw new NoSuchMethodError( "setParents(GraphNodeList, Class) not implemented yet." );
    }

}
