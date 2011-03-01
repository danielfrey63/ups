package ch.jfactory.model.graph.tree;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import javax.swing.tree.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNode implements GraphNode
{
    private static final Logger LOGGER = LoggerFactory.getLogger( VirtualGraphTreeNode.class );

    /**
     * The ROLE_NULL <code>GraphNode</code> is used because instances of <code> TreePath</code> may not be constructed
     * without an object. I use <code>TreePath</code>s in the {@link VirtualGraphTreeNodeModel}'s cache as a key to the
     * <code>GraphNode2TreeNode</code> objects. The ROLE_NULL object is also used when composing the key to interrogate
     * the cache.
     */
    public static final GraphNode NULL = new GraphNodeImpl();

    /** In a tree model, the parent is unique. */
    private final VirtualGraphTreeNode parent;

    /** The wrapped object. */
    private final GraphNode dependent;

    /**
     * The model associated with this node. Primary purpose of the model is a separate cache, so more than one tree may
     * be displayed independently.
     */
    private final VirtualGraphTreeNodeModel model;

    /** The filter associated with this <code>GraphNode2TreeNode</code> */
    private VirtualGraphTreeNodeFilter filter;

    /**
     * Internal member to hold the unique key to this <code>GraphNode2TreeNode </code> object. It is used as key to
     * identify this object in the cache. The <code>ancestors</code> member is composed of the the (partly hidden)
     * dependent nodes.
     */
    private TreePath ancestors = new TreePath( NULL );

    /**
     * If several children nodes are wrappers for the same {@link #dependent}, and they are merged, so that only
     * distinct <code>dependent<code>s are displayed, this member variable is used to keep the cross reference of the
     * merged <code>GraphNode2TreeNode</code> objects.
     */
    private final VirtualGraphTreeNodeList nonDistincts = new VirtualGraphTreeNodeList( this );

    /**
     * Constructor is held private for factory method to have better control over construction of objects.
     *
     * @param dependent the dependent graph node
     * @param model     the model
     * @param filter    the filter
     * @param parent    the parent
     */
    private VirtualGraphTreeNode( final GraphNode dependent,
                                  final VirtualGraphTreeNodeModel model, final VirtualGraphTreeNodeFilter filter,
                                  final VirtualGraphTreeNode parent )
    {
        this.dependent = dependent;
        this.model = model;
        this.filter = filter;
        this.parent = parent;
        checkTypeMatching( dependent, filter );
    }

    /**
     * Factory method to create a {@link VirtualGraphTreeNode} root object. No parent is needed.
     *
     * @param dependent the {@link GraphNode} object that is wrapped
     * @param model     the {@link VirtualGraphTreeNodeModel} used
     * @param filter    the {@link VirtualGraphTreeNodeFilter} associated with this node
     * @return either a new <code>GraphNode2TreeNode</code> or an existing one.
     */
    public static VirtualGraphTreeNode getGraphNode( final GraphNode dependent, final VirtualGraphTreeNodeModel model,
                                                     final VirtualGraphTreeNodeFilter filter )
    {
        checkTypeMatching( dependent, filter );
        return createNode( dependent, null, model, filter );
    }

    /**
     * Factory method to add a {@link VirtualGraphTreeNode} object to a given parent.
     *
     * @param dependent the dependent node
     * @param parent    the parent <code>VirtualGraphTreeNode</code>
     * @return either a new <code>VirtualGraphTreeNode</code> or an existing one.
     */
    public static VirtualGraphTreeNode getGraphNode( final GraphNode dependent, final VirtualGraphTreeNode parent )
    {
        final VirtualGraphTreeNodeModel model = parent.model;
        final Class type = dependent.getClass();
        final VirtualGraphTreeNodeFilter parentFilter = parent.getFilter();
        final VirtualGraphTreeNodeFilter filter = parentFilter.getChildrenFilter( type );
        checkTypeMatching( dependent, filter );
        return createNode( dependent, parent, model, filter );
    }

    /**
     * Each new object has its unique tree path (that is, the path formed by visible and invisible nodes as declared by
     * the root {@link VirtualGraphTreeNodeFilter}).
     *
     * @param dependent the dependent node
     * @param parent    the parent <code>VirtualGraphTreeNode</code>
     * @param model     the model
     * @param filter    the filter
     * @return either a new <code>VirtualGraphTreeNode</code> or an existing one.
     */
    private static VirtualGraphTreeNode createNode( final GraphNode dependent, final VirtualGraphTreeNode parent,
                                                    final VirtualGraphTreeNodeModel model, final VirtualGraphTreeNodeFilter filter )
    {
        final TreePath key;
        if ( parent != null )
        {
            key = parent.ancestors.pathByAddingChild( dependent );
        }
        else
        {
            key = new TreePath( NULL );
        }
        VirtualGraphTreeNode filteredNode = (VirtualGraphTreeNode) model.get( key );

        if ( filteredNode == null )
        {
            filteredNode = new VirtualGraphTreeNode( dependent, model, filter, parent );
            model.put( key, filteredNode );
            filteredNode.ancestors = key;
        }

        return filteredNode;
    }

    private static void checkTypeMatching( final GraphNode node, final VirtualGraphTreeNodeFilter filter )
    {
        if ( !node.isType( filter.getType() ) )
        {
            throw new IllegalStateException( "Filters type (" + filter.getType() + ") must match nodes type ("
                    + node.getClass() + ")" );
        }
    }

    private GraphNodeList getDependents( final GraphNodeList list )
    {
        final GraphNodeList result = new GraphNodeList();
        for ( int i = 0; i < list.size(); i++ )
        {
            // Bad workaround: VirtualGraphTreeNode should only receive lists
            // of it self's type, not GraphNodeImpl or so.
            GraphNode node = list.get( i );
            if ( node instanceof VirtualGraphTreeNode )
            {
                node = ( (VirtualGraphTreeNode) node ).getDependent();
                LOGGER.warn( "false type translated: " + node );
            }
            result.add( node );
        }
        return result;
    }

    private VirtualGraphTreeNodeList getVirtual( final GraphNodeList list )
    {
        final VirtualGraphTreeNodeList result = new VirtualGraphTreeNodeList();
        for ( int i = 0; i < list.size(); i++ )
        {
            final VirtualGraphTreeNode node = (VirtualGraphTreeNode) list.get( i );
            result.add( get( node ) );
        }
        return result;
    }

    private VirtualGraphTreeNode get( final VirtualGraphTreeNode node )
    {
        final TreePath full = node.ancestors.pathByAddingChild( node );
        return (VirtualGraphTreeNode) model.get( full );
    }

    /**
     * Method typical for the tree behaviour of a <code>TreeNode</code>
     *
     * @return VirtualGraphTreeNode
     */
    public VirtualGraphTreeNode getParent()
    {
        return parent;
    }

    /**
     * Returns the filter.
     *
     * @return TreeFilterDetail
     */
    public VirtualGraphTreeNodeFilter getFilter()
    {
        return filter;
    }

    /**
     * Returns the dependent.
     *
     * @return GraphNode
     */
    public GraphNode getDependent()
    {
        return dependent;
    }

    public void addNonDistinct( final VirtualGraphTreeNode sibling )
    {
        if ( !nonDistincts.contains( sibling ) )
        {
            nonDistincts.add( sibling );
        }
    }

    /**
     * Returns the nonDistincts.
     *
     * @return VirtualGraphTreeNode[]
     */
    public VirtualGraphTreeNodeList getNonDistincts()
    {
        return nonDistincts;
    }

    /**
     * Inserts a child from the trees point of view. If -- from the graphs point of view -- the given node is a
     * child/parent to this node, then the appropriate addChild/addParent method is invoked.
     *
     * @param index the index to add it to
     * @param node  the node to add
     */
    public void addTreeChild( final int index, VirtualGraphTreeNode node )
    {
        final GraphNode dep = node.getDependent();
        final Class type = dep.getClass();
        final VirtualGraphTreeNodeFilter childFilter = filter.getChildrenFilter( type );
        if ( node.model != model )
        {
            // Insertion from another tree
            node = createNode( dep, this, model, childFilter );
        }
        if ( childFilter.isDescendant() )
        {
            addChild( index, node );
        }
        else
        {
            addParent( index, node );
        }
    }

    public void removeTreeChild( final VirtualGraphTreeNode node )
    {
        final GraphNode dep = node.getDependent();
        final Class type = dep.getClass();
        final VirtualGraphTreeNodeFilter childFilter = filter.getChildrenFilter( type );
        if ( childFilter.isDescendant() )
        {
            node.removeFromParent( this );
        }
        else
        {
            node.removeFromChild( this );
        }
    }

    /** @see GraphNode#getId() */
    public int getId()
    {
        return dependent.getId();
    }

    /** @see GraphNode#getName() */
    public String getName()
    {
        return dependent.getName();
    }

    /** @see GraphNode#getRank() */
    public int getRank()
    {
        return dependent.getRank();
    }

    /** @see GraphNode#isType(Class) */
    public boolean isType( final Class type )
    {
        return dependent.isType( type );
    }

    /**
     * Returns the only tree view parent.
     *
     * @see GraphNode#getParents()
     */
    public GraphNodeList getParents()
    {
        final GraphNodeList result = new GraphNodeList();
        result.add( getParent() );
        return result;
    }

    public GraphNodeList getParents( final Class type )
    {
        return getVirtual( dependent.getParents( type ) );
    }

    public GraphNodeList getAllParents( final Class type )
    {
        return getVirtual( dependent.getAllParents( type ) );
    }

    public GraphNodeList getChildren()
    {
        return model.getChildren( this );
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
        final GraphNodeList children = getChildren( type );
        for ( int i = 0; i < children.size(); i++ )
        {
            result.addAll( children );
            result.addAll( children.get( i ).getAllChildren( type ) );
        }
        return result;
    }

    /**
     * Simple wrapper that delegates to the {@link #dependent}.
     *
     * @see GraphNode#setId(int)
     */
    public void setId( final int id )
    {
        dependent.setId( id );
    }

    public void setName( final String name )
    {
        dependent.setName( name );
    }

    public void setRank( final int rank )
    {
        dependent.setRank( rank );
    }

    public void setChildren( final GraphNodeList children )
    {
        dependent.setChildren( getDependents( children ) );
    }

    public void setChildren( final GraphNodeList children, final Class type )
    {
        dependent.setChildren( getDependents( children ), type );
    }

    public void addChild( final GraphNode child )
    {
        addChild( dependent.getChildren().size(), child );
    }

    public void addChild( final int index, final GraphNode child )
    {
        // Find index in context of all children
        final GraphNodeList children = getChildren();
        final VirtualGraphTreeNode visibleInsert = (VirtualGraphTreeNode) children.get( index );
        final GraphNodeList hidden = dependent.getChildren();
        final int hiddenIndex;
        if ( visibleInsert == null )
        {
            // Is at the end of the list
            hiddenIndex = hidden.size();
        }
        else
        {
            // Is before the specified index
            final GraphNode dependentOfVisible = visibleInsert.getDependent();
            hiddenIndex = hidden.get( dependentOfVisible );
        }
        final VirtualGraphTreeNode missile = (VirtualGraphTreeNode) child;
        dependent.addChild( hiddenIndex, missile.getDependent() );
    }

    public GraphNode addNewChild( final int index, final String name, final Class type )
    {
        return getGraphNode( dependent.addNewChild( index, name, type ), this );
    }

    public boolean deleteChild( final GraphNode child )
    {
        final VirtualGraphTreeNode vChild = (VirtualGraphTreeNode) child;
        return dependent.deleteChild( vChild.getDependent() );
    }

    public void deleteChildren( final Class type )
    {
        getDependent().deleteChildren( type );
    }

    public boolean removeFromChild( final GraphNode child )
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) child;
        return getDependent().removeFromChild( vNode.getDependent() );
    }

    public void setParents( final GraphNodeList parents )
    {
        dependent.setParents( getDependents( parents ) );
    }

    public void setParents( final GraphNodeList parents, final Class type )
    {
        dependent.setParents( getDependents( parents ), type );
    }

    public void addParent( final GraphNode parent )
    {
        addParent( dependent.getParents().size(), parent );
    }

    public void addParent( final int index, final GraphNode parent )
    {
        // Find index in context of all children. It might be that the parent
        // inserted is not a visible parent of this node, but an invisible one.
        // We have to search for it.
        final GraphNodeList visible = getChildren();
        final VirtualGraphTreeNode pNode = (VirtualGraphTreeNode) parent;
        final VirtualGraphTreeNodeFilter pFilter = pNode.getFilter();
        VirtualGraphTreeNode visibleInsert = (VirtualGraphTreeNode) visible.get( index );
        while ( visibleInsert != null && visibleInsert.getFilter().getType() != pFilter.getType() )
        {
            visibleInsert = visibleInsert.parent;
        }
        final int hiddenIndex;
        if ( visibleInsert == null )
        {
            // Is at the end of the list
            hiddenIndex = visible.size();
        }
        else
        {
            final GraphNode dependentOfVisible = visibleInsert.getDependent();
            hiddenIndex = dependent.getParents().get( dependentOfVisible );
        }
        dependent.addParent( hiddenIndex, pNode.getDependent() );
    }

    public GraphNode addNewParent( final int index, final String name, final Class type )
    {
        return getGraphNode( dependent.addNewParent( index, name, type ), this );
    }

    public boolean deleteParent( final GraphNode parent )
    {
        final VirtualGraphTreeNode vParent = (VirtualGraphTreeNode) parent;
        return dependent.deleteParent( vParent.getDependent() );
    }

    public void deleteParents( final Class type )
    {
        getDependent().deleteParents( type );
    }

    public boolean removeFromParent( final GraphNode parent )
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) parent;
        return getDependent().removeFromParent( vNode.getDependent() );
    }

    public String toString()
    {
        return dependent.toString();
    }
}
