package ch.jfactory.model.graph.tree;

/**
 * Class for a tree like structure to support filtering nodes of a graph for a tree.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNodeFilter
{
    /** Makes a node visible. */
    public static final boolean VISIBILITY_VISIBLE = true;

    /** Hides a node. */
    public static final boolean VISIBILITY_HIDDEN = false;

    /** The next node is a parent of this node. */
    public static final int LINE_ANCESTOR = 1;

    /** The next node is a child of this node. */
    public static final int LINE_DESCENDANT = 2;

    /** The next node is either a parent or a child of this node. */
    public static final int LINE_RELATED = 3;

    /** Types of this node are displayed recursively. Sub nodes of the same type will pass also. */
    public static final boolean SELF_RECURSIVE = true;

    /**
     * Types of this node pass, but children/parents - depending on LINE_ANCESTOR, LINE_DESCENDANT and LINE_RELATED - of
     * the same type don't.
     */
    public static final boolean SELF_FLAT = false;

    /** Makes node types taking into account prior filters. See {@link #bound} for further explanation. */
    public static final boolean CONSTRAINT_BOUND = true;

    /** Makes node types being added without taking other filters into account. */
    public static final boolean CONSTRAINT_FREE = false;

    /** Matching all classes. */
    public static final Class CLASSES_ALL = Object.class;

    /** The vertices type to filter out */
    private Class type = CLASSES_ALL;

    /**
     * Is this vertex a child of its parent? A parent of its child would be LINE_DESCENDANT. Note: Taking the default,
     * the filter has not to be indicated on the parent side, but on the children side, as potentially more than one
     * child may occur, but have not to be children.
     */
    private int direction = LINE_ANCESTOR;

    /** Should the vertex be displayed */
    private boolean visible;

    /** Should recursive links in vertices to the same type be resolved? */
    private boolean recursive;

    /**
     * This attribute specifies the behaviour of this filter when used in a filter array. It allows to filter objects
     * not only by type, but -- if set to <code>true</code> (the default) -- considers the toString() of a previously
     * found object and only delivers children matching this string.<p>
     */
    private boolean bound = true;

    /** Parent filter. May be null if root filter */
    private VirtualGraphTreeNodeFilter parent;

    /** Subsequent filters that are used as children for this filter */
    private VirtualGraphTreeNodeFilter[] childrenFilters;

    public VirtualGraphTreeNodeFilter( final Class type, final boolean visible,
                                       final boolean recursive, final boolean bound,
                                       final VirtualGraphTreeNodeFilter[] childrenFilters, final int direction )
    {
        this.type = type;
        this.visible = visible;
        this.recursive = recursive;
        this.bound = bound;
        this.childrenFilters = ( childrenFilters == null ? new VirtualGraphTreeNodeFilter[0] : childrenFilters );
        this.direction = direction;
        for ( final VirtualGraphTreeNodeFilter childFilter : this.childrenFilters )
        {
            childFilter.parent = this;
        }
    }

    /**
     * Returns the displayed.
     *
     * @return boolean
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * Returns the down.
     *
     * @return boolean
     */
    public boolean isDescendant()
    {
        return ( direction & 2 ) == 2;
    }

    public boolean isBothDirections()
    {
        return ( direction & 3 ) == 3;
    }

    /**
     * Returns the type.
     *
     * @return String
     */
    public Class getType()
    {
        return type;
    }

    /**
     * Returns the recursive.
     *
     * @return boolean
     */
    public boolean isRecursive()
    {
        return recursive;
    }

    /**
     * Returns the bound.
     *
     * @return boolean
     */
    public boolean isBound()
    {
        return bound;
    }

    /**
     * Returns all the children filters.
     *
     * @return GraphNodeFilter[]
     */
    public VirtualGraphTreeNodeFilter[] getChildrenFilters()
    {
        return childrenFilters;
    }

    /**
     * Returns the first children filters of the given type and if not present, returns this instance if the types are
     * matching.
     *
     * @param type the type to retrieve
     * @return GraphNodeFilter[]
     */
    public VirtualGraphTreeNodeFilter getChildrenFilter( final Class type )
    {
        // Check for more specific real children first
        for ( final VirtualGraphTreeNodeFilter childrenFilter : childrenFilters )
        {
            if ( childrenFilter.getType().isAssignableFrom( type ) )
            {
                return childrenFilter;
            }
        }
        // Check for less specific recursion now
        if ( this.type.isAssignableFrom( type ) )
        {
            return this;
        }
        return null;
    }

    /**
     * Returns the parent.
     *
     * @return GraphNode2TreeFilter
     */
    public VirtualGraphTreeNodeFilter getParent()
    {
        return parent;
    }

    /** @see Object#toString() */
    public String toString()
    {
        return type + " [down=" + direction + ",displayed=" + visible + ",recursive=" + recursive + ",bound=" + bound + "]";
    }

    /**
     * Factory method to generate a simple nested linear filter. I.e. to have the following types displayed (top-down):
     * <pre>
     * -+ File
     *  +--+ URL
     *     +--+ String
     * </pre>
     *
     * for which you need to specify the classes array as <code>new Class[] {File.class, URL.class,
     * String.class}</code>.<p>
     *
     * The attributes details for displaying the filter are ordered and valued as follows:
     *
     * <ul> <li>Visibility may be 1 ({@link #VISIBILITY_VISIBLE}) or 0 ({@link #VISIBILITY_HIDDEN})</li> <li>Containing
     * instances of it self may be 1 ({@link #SELF_RECURSIVE}) or 0 ({@link #SELF_FLAT})</li> <li>Constraint may be 1
     * ({@link #CONSTRAINT_BOUND}) or 0 ({@link #CONSTRAINT_FREE})</li> <li>Line may be 1 ({@link #LINE_ANCESTOR}), 2
     * ({@link #LINE_DESCENDANT}) or 3 ({@link #LINE_RELATED})</li> </ul>
     *
     * While supported by the VirtualGraphTreeNodeFilter, with this factory method it is not possible to have several
     * children filters nested into one parent filter. Only one child is allowed for a parent. I.e. the following is not
     * achievable:
     *
     * <pre>
     * -+ File
     *  +--+ URL
     *     +--+ String
     *     +--+ Integer
     *  +--+ StringBuffer
     *     +--+ Long
     * </pre>
     *
     * <code>File</code> and <code>URL</code> each contain two children filter and therefore are not suitable here.<p>
     *
     * @param classes      the types to filter. Length must be equal to first order length of filterMatrix.
     * @param filterMatrix the attributes for each type to filter. First order length must be equal to classes' length.
     *                     Second order length must be 4.
     * @return the top filter defined by the first row, subsequently containing the remaining rows.
     */
    public static VirtualGraphTreeNodeFilter getFilter( final Class[] classes, final int[][] filterMatrix )
    {
        if ( filterMatrix.length != classes.length )
        {
            throw new IllegalArgumentException( "First order length of classes and matrix must be equal" );
        }
        if ( filterMatrix[0].length != 4 )
        {
            throw new IllegalArgumentException( "Second order length filterMatrix must be 4" );
        }
        VirtualGraphTreeNodeFilter filter = null;
        for ( int r = filterMatrix.length - 1; r >= 0; r-- )
        {
            final int[] filterData = filterMatrix[r];
            filter = getFilter( classes[r], filterData, filter );
        }
        return filter;
    }

    private static VirtualGraphTreeNodeFilter getFilter( final Class type, final int[] data, final VirtualGraphTreeNodeFilter filter )
    {
        return new VirtualGraphTreeNodeFilter( type,
                data[0] == 1 ? VISIBILITY_VISIBLE : VISIBILITY_HIDDEN,
                data[1] == 1 ? SELF_RECURSIVE : SELF_FLAT,
                data[2] == 1 ? CONSTRAINT_BOUND : CONSTRAINT_FREE,
                filter == null ? null : new VirtualGraphTreeNodeFilter[]{filter},
                data[3] == 1 ? LINE_ANCESTOR : data[3] == 2 ? LINE_DESCENDANT : LINE_RELATED );
    }

}










