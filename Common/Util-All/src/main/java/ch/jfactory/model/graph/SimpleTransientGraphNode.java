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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This node is used as a temporary GraphNode which is not saved. All its links (GraphEdges) wont be saved neither. Associated GraphNodes wont have any role or rank. Trying to filter associated GraphNodes will return the full set of associated GraphNodes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class SimpleTransientGraphNode extends AbsSimpleGraphNode
{
    /** Mapping between nodes that are used in other <code>SimpleTransientGraphNode</code>s as children and these other instances. */
    private static final HashMap<GraphNode, ArrayList<GraphNode>> childrenReferences = new HashMap<GraphNode, ArrayList<GraphNode>>();

    /** Mapping between nodes that are used in other <code>SimpleTransientGraphNode</code>s as parents and these other instances. */
    private static final HashMap<GraphNode, ArrayList<GraphNode>> parentsReferences = new HashMap<GraphNode, ArrayList<GraphNode>>();

    /** References to children <code>SimpleTransientGraphNode</code>s. */
    private GraphNodeList children = new GraphNodeList();

    /** References to parent <code>SimpleTransientGraphNode</code>s. */
    private final GraphNodeList parents = new GraphNodeList();

    private ArrayList<GraphNode> getReferencesFor( final GraphNode node, final HashMap<GraphNode, ArrayList<GraphNode>> references )
    {
        ArrayList<GraphNode> list = references.get( node );
        if ( list == null )
        {
            list = new ArrayList<GraphNode>();
            references.put( node, list );
        }
        return list;
    }

    /** @see GraphNode#getChildren() */
    public GraphNodeList getChildren()
    {
        return children;
    }

    /** @see GraphNode#getChildren(Class) */
    public GraphNodeList getChildren( final Class type )
    {
        return getChildren();
    }

    /** @see GraphNode#setChildren(GraphNodeList) */
    public void setChildren( final GraphNodeList children )
    {
        this.children = children;
    }

    /** @see GraphNode#setChildren(GraphNodeList, Class) */
    public void setChildren( final GraphNodeList children, final Class type )
    {
        throw new NoSuchMethodError( "setChildren(children, type, role) "
                + "not supported. Use setChildren() instead." );
    }

    /** @see GraphNode#addChild(GraphNode) */
    public void addChild( final GraphNode child )
    {
        addChild( children.size(), child );
    }

    /** @see GraphNode#addChild(int, GraphNode) */
    public void addChild( final int index, final GraphNode child )
    {
        final ArrayList<GraphNode> list = getReferencesFor( child, childrenReferences );
        if ( !list.contains( this ) )
        {
            list.add( this );
            children.add( index, child );
            child.addParent( this );
        }
    }

    /** @see GraphNode#addNewChild(int, String, Class) */
    public GraphNode addNewChild( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addChild( index, instance );
        return instance;
    }

    /** @see GraphNode#deleteChild(GraphNode) */
    public boolean deleteChild( final GraphNode child )
    {
        final int size = children.size();
        children.remove( child );
        ArrayList<GraphNode> list = getReferencesFor( child, childrenReferences );
        list = new ArrayList<GraphNode>( list );
        for ( final Object aList : list )
        {
            final GraphNode other = (GraphNode) aList;
            other.removeFromParent( child );
        }
        return children.size() != size;
    }

    /** @see GraphNode#deleteChildren(Class) */
    public void deleteChildren( final Class type )
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

    /** @see GraphNode#removeFromChild(GraphNode) */
    public boolean removeFromChild( final GraphNode child )
    {
        final int size = children.size();
        if ( children.remove( child ) )
        {
            final ArrayList<GraphNode> list = getReferencesFor( child, childrenReferences );
            list.remove( this );
            childrenReferences.put( child, list );
            child.removeFromChild( this );
        }
        return children.size() != size;
    }

    /** @see GraphNode#getParents() */
    public GraphNodeList getParents()
    {
        return parents;
    }

    /** @see GraphNode#addParent(GraphNode) */
    public void addParent( final GraphNode parent )
    {
        addParent( parents.size(), parent );
    }

    /** @see GraphNode#addParent(int, GraphNode) */
    public void addParent( final int index, final GraphNode parent )
    {
        final ArrayList<GraphNode> list = getReferencesFor( parent, parentsReferences );
        if ( !list.contains( this ) )
        {
            list.add( this );
            parents.add( index, parent );
            parent.addChild( this );
        }

    }

    /** @see GraphNode#addNewChild(int, String, Class) */
    public GraphNode addNewParent( final int index, final String name, final Class type )
    {
        final GraphNode instance = AbsGraphModel.getTypeFactory().getInstance( type );
        instance.setName( name );
        addParent( index, instance );
        return instance;
    }

    /** @see GraphNode#deleteParent(GraphNode) */
    public boolean deleteParent( final GraphNode parent )
    {
        final int size = parents.size();
        parents.remove( parent );
        ArrayList<GraphNode> list = getReferencesFor( parent, parentsReferences );
        list = new ArrayList<GraphNode>( list );
        for ( final Object aList : list )
        {
            final GraphNode other = (GraphNode) aList;
            other.removeFromParent( parent );
        }
        return parents.size() != size;
    }

    /** @see GraphNode#deleteParents(Class) */
    public void deleteParents( final Class type )
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

    /** @see GraphNode#removeFromParent(GraphNode) */
    public boolean removeFromParent( final GraphNode parent )
    {
        final int size = parents.size();
        if ( parents.remove( parent ) )
        {
            final ArrayList<GraphNode> list = getReferencesFor( parent, parentsReferences );
            list.remove( this );
            parentsReferences.put( parent, list );
            parent.removeFromChild( this );
        }
        return parents.size() != size;
    }

    /** @see GraphNode#setParents(GraphNodeList) */
    public void setParents( final GraphNodeList parents )
    {
        throw new NoSuchMethodError( "setParents(GraphNodeList) not implemented yet." );
    }

    /** @see GraphNode#setParents(GraphNodeList, Class) */
    public void setParents( final GraphNodeList parents, final Class type )
    {
        throw new NoSuchMethodError( "setParents(GraphNodeList, Class) not implemented yet." );
    }

}
