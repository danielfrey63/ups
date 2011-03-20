package ch.jfactory.model.graph.tree;

/**
 * Class for a tree like structure to support filtering nodes of a graph for a tree.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNodeFilter
{
    /** The visibility of a node. */
    public enum Visibility
    {
        /** Hides a node. */
        VISIBLE,
        /** Makes a node visible. */
        HIDDEN
    }

    /** The relation between this filters and the next node. */
    public enum Lineage
    {
        /** The next node is a parent of this node. */
        ANCESTOR,
        /** The next node is a child of this node. */
        DESCENDANT,
        /** The next node is either a parent or a child of this node. */
        RELATED
    }

    /** Applies to nodes of same type and filters whether to show them. */
    public enum Self
    {
        /** Types of this node are displayed recursively. Sub nodes of the same type will pass also. */
        RECURSIVE,
        /** Types of this node pass, but children/parents - depending on LINE_ANCESTOR, LINE_DESCENDANT and RELATED - of the same type don't. */
        FLAT
    }

    /** Taking prior filters into account. */
    public enum Constraint
    {
        /** Makes node types taking into account prior filters. See {@link #constraint} for further explanation. */
        BOUND,
        /** Makes node types being added without taking other filters into account. */
        FREE
    }

    /** Matching all classes. */
    public static final Class CLASSES_ALL = Object.class;

    /** The vertices type to filter out */
    private Class type = CLASSES_ALL;

    /** Is this vertex a child of its parent? A parent of its child would be LINE_DESCENDANT. Note: Taking the default, the filter has not to be indicated on the parent side, but on the children side, as potentially more than one child may occur, but have not to be children. */
    private Lineage direction = Lineage.ANCESTOR;

    /** Should the vertex be displayed */
    private Visibility visible;

    /** Should recursive links in vertices to the same type be resolved? */
    private Self self;

    /** This attribute specifies the behaviour of this filter when used in a filter array. It allows to filter objects not only by type, but -- if set to <code>true</code> (the default) -- considers the toString() of a previously found object and only delivers children matching this string.<p> */
    private Constraint constraint = Constraint.BOUND;

    /** Parent filter. May be null if root filter */
    private VirtualGraphTreeNodeFilter parent;

    /** Subsequent filters that are used as children for this filter */
    private VirtualGraphTreeNodeFilter[] childrenFilters;

    public VirtualGraphTreeNodeFilter( final Class type, final Visibility visible, final Self self, final Constraint constraint, final Lineage direction, final VirtualGraphTreeNodeFilter... childrenFilters )
    {
        this.type = type;
        this.visible = visible;
        this.self = self;
        this.constraint = constraint;
        this.childrenFilters = ( childrenFilters == null ? new VirtualGraphTreeNodeFilter[0] : childrenFilters );
        this.direction = direction;
        for ( final VirtualGraphTreeNodeFilter childFilter : this.childrenFilters )
        {
            childFilter.parent = this;
        }
    }

    public VirtualGraphTreeNodeFilter( final Class type, final Visibility visible, final Self self, final Constraint constraint, final Lineage direction )
    {
        this( type, visible, self, constraint, direction, new VirtualGraphTreeNodeFilter[0] );
    }

    /**
     * Returns the displayed.
     *
     * @return boolean
     */
    public boolean isVisible()
    {
        return visible == Visibility.VISIBLE;
    }

    /**
     * Returns the down.
     *
     * @return boolean
     */
    public boolean isDescendant()
    {
        return direction == Lineage.DESCENDANT;
    }

    public boolean isBothDirections()
    {
        return direction == Lineage.RELATED;
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
     * Returns whether recursive.
     *
     * @return boolean
     */
    public boolean isRecursive()
    {
        return self == Self.RECURSIVE;
    }

    /**
     * Returns whether bound.
     *
     * @return boolean
     */
    public boolean getConstraint()
    {
        return constraint == Constraint.BOUND;
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
     * Returns the first children filters of the given type and if not present, returns this instance if the types are matching.
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
        return type + " [down=" + direction + ",displayed=" + visible + ",self=" + self + ",constraint=" + constraint + "]";
    }

    /**
     * Factory method to generate a simple nested linear filter. I.e. to have the following types displayed (top-down):
     * <pre>
     * -+ File
     *  +--+ URL
     *     +--+ String
     * </pre>
     *
     * for which you need to specify the classes array as <code>new Class[] {File.class, URL.class, String.class}</code>.<p>
     *
     * The attributes details for displaying the filter are ordered and valued as follows:
     *
     * <ul> <li>Visibility may be 1 ({@link Visibility#VISIBLE Visibility.VISIBLE}) or 0 ({@link Visibility#HIDDEN Visibility.HIDDEN})</li> <li>Containing instances of it self may be 1 ({@link Self#RECURSIVE Self.RECURSIVE}) or 0 ({@link Self#FLAT Self.FLAT})</li> <li>Constraint may be 1 ({@link Constraint#BOUND Constraint.BOUND}) or 0 ({@link Constraint#FREE Constraint.FREE})</li> <li>Lineage may be 1 ({@link Lineage#ANCESTOR Lineage.ANCESTOR}), 2 ({@link Lineage#DESCENDANT Lineage.DESCENDANT}) or 3 ({@link Lineage#RELATED Lineage.RELATED})</li> </ul>
     *
     * While supported by the VirtualGraphTreeNodeFilter, with this factory method it is not possible to have several children filters nested into one parent filter. Only one child is allowed for a parent. I.e. the following is not achievable:
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
     * @param filterMatrix the attributes for each type to filter. First order length must be equal to classes' length. Second order length must be 4.
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
                data[0] == 1 ? Visibility.VISIBLE : Visibility.HIDDEN,
                data[1] == 1 ? Self.RECURSIVE : Self.FLAT,
                data[2] == 1 ? Constraint.BOUND : Constraint.FREE,
                data[3] == 1 ? Lineage.ANCESTOR : data[3] == 2 ? Lineage.DESCENDANT : Lineage.RELATED, filter == null ? null : new VirtualGraphTreeNodeFilter[]{filter}
        );
    }

}










