package ch.jfactory.model.graph.tree;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNodeFilter
{
    /**
     * Makes a node visible.
     */
    public static final boolean VISIBILITY_VISIBLE = true;

    /**
     * Hides a node.
     */
    public static final boolean VISIBILITY_HIDDEN = false;

    /**
     * The next node is a parent of this node.
     */
    public static final int LINE_ANCESTOR = 1;

    /**
     * The next node is a child of this node.
     */
    public static final int LINE_DESCENDANT = 2;

    /**
     * The next node is either a parent or a child of this node.
     */
    public static final int LINE_RELATED = 3;

    /**
     * Types of this node are displayed recursively. Subnodes of the same type will pass also.
     */
    public static final boolean SELF_RECURSIVE = true;

    /**
     * Types of this node pass, but children/parents - depending on LINE_ANCESTOR, LINE_DESCENDANT and LINE_RELATED - of
     * the same type don't.
     */
    public static final boolean SELF_FLAT = false;

    /**
     * Makes node types taking into account prior filters. See {@link #bound} for further explanation.
     */
    public static final boolean CONSTRAINT_BOUND = true;

    /**
     * Makes node types beeing added without taking other filters into account.
     */
    public static final boolean CONSTRAINT_FREE = false;

    public static final Class CLASSES_ALL = Object.class;

    public static final Class ROLES_ALL = Object.class;

    /**
     * The vertices type to filter out
     */
    private Class type = CLASSES_ALL;

    /**
     * The edge role to filter out
     */
    private Class role = ROLES_ALL;

    /**
     * Is this vertice a child of its parent? A parent of its child would be LINE_DESCENDANT. Note: Taking the default,
     * the filter has not to be indicated on the parent side, but on the childs side, as potentially more than one child
     * may occur, but are not obliguet to be childs.
     */
    private int direction = LINE_ANCESTOR;

    /**
     * Should the vertice be displayed
     */
    private boolean visible;

    /**
     * Should recursive links in vertices to the same type be resolved?
     */
    private boolean recursive;

    /**
     * This attribute specifies the behaviour of this filter when used in a filter array. It allows to filter objects
     * not only by type, but -- if set to <code>true</code> (the default) -- consideres the toString() of a previously
     * found object and only delivers children matching this string.<p>
     */
    private boolean bound = true;

    /**
     * Determines whether the tree for this filter is deep or flat. Only applies to recursive filters, and only for the
     * recursive elements filtered by this filter.
     */
    private final boolean flat = false;

    /**
     * Parent filter. May be null if root filter
     */
    private VirtualGraphTreeNodeFilter parent;

    /**
     * Subsequent filters that are used as children for this filter
     */
    private VirtualGraphTreeNodeFilter[] childrenFilters;

    /**
     * This comparator is used to sort the children of the associated GraphNode.
     */
    private Comparator comparator;

    /**
     * Individual filter for nodes which might be customized by delivering a AbsGraphTreeNodeFilter.
     */
    private AbsGraphTreeNodeFilter nodeFilter;

    public VirtualGraphTreeNodeFilter( final Class type, final boolean displayed,
                                       final boolean recursive, final boolean bound,
                                       final VirtualGraphTreeNodeFilter[] filters, final int direction )
    {
        this.type = type;
        this.visible = displayed;
        this.recursive = recursive;
        this.bound = bound;
        this.childrenFilters =
                ( filters == null ? new VirtualGraphTreeNodeFilter[0] : filters );
        this.direction = direction;
        for ( final VirtualGraphTreeNodeFilter childFilter : this.childrenFilters )
        {
            childFilter.parent = this;
        }
    }

    /**
     * Method VirtualGraphTreeNodeFilter.
     *
     * @param type       the Class type to filter
     * @param role       the Class role to filter
     * @param comp       the Comparator to sort the children
     * @param nodeFilter the AbsGraphTreeNodeFilter to filter
     * @param displayed  whether nodes of this filter is displayed
     * @param recursive  whether this filter is applied recursively
     * @param bound      whether this filter is bound to a matching parent filter
     * @param filters    the children filters
     * @param direction  the direction in which the children are found
     */
    public VirtualGraphTreeNodeFilter( final Class type, final Class role, final Comparator comp,
                                       final AbsGraphTreeNodeFilter nodeFilter, final boolean displayed, final boolean recursive,
                                       final boolean bound, final VirtualGraphTreeNodeFilter[] filters, final int direction )
    {
        this( type, displayed, recursive, bound, filters, direction );
        this.role = role;
        this.comparator = comp;
        this.nodeFilter = nodeFilter;
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

    public Class getRole()
    {
        return role;
    }

    /**
     * Returns the recusive.
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
     * Returns the childrenFilters.
     *
     * @return GraphNodeFilter[]
     */
    public VirtualGraphTreeNodeFilter[] getChildrenFilters()
    {
        return childrenFilters;
    }

    /**
     * Returns the childrenFilters of the given type.
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

    /**
     * Returns the flat.
     *
     * @return boolean
     */
    public boolean isFlat()
    {
        return flat;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return type + " [down=" + direction + ",displayed=" + visible +
                ",recursive=" + recursive + ",bound=" + bound + "]";
    }

    /**
     * Returns the comparator.
     *
     * @return Comparator
     */
    public Comparator getComparator()
    {
        return comparator;
    }

    /**
     * Returns the nodeFilter.
     *
     * @return AbsGraphTreeNodeFilter
     */
    public AbsGraphTreeNodeFilter getNodeFilter()
    {
        return nodeFilter;
    }

    /**
     * Fectory method to generate a simple nested linear filter. I.e. to have i.e. the following types displayed
     * (top-down):
     * <pre>
     * -+ File
     *  +--+ URL
     *     +--+ String
     * </pre>
     * you need to specify the classes array as <code>new Class[] {File.class, URL.class, String.class}</code>.<p> The
     * attributes details for displaying the filter are ordered and valued as follows: <ul> <li>Visibility may be 1
     * ({@link #VISIBILITY_VISIBLE}) or 0 ({@link #VISIBILITY_HIDDEN})</li> <li>Containing instances of it self may be 1
     * ({@link #SELF_RECURSIVE}) or 0 ({@link #SELF_FLAT})</li> <li>Constraint may be 1 ({@link #CONSTRAINT_BOUND}) or 0
     * ({@link #CONSTRAINT_FREE})</li> <li>Line may be 1 ({@link #LINE_ANCESTOR}), 2 ({@link #LINE_DESCENDANT}) or 3
     * ({@link #LINE_RELATED})</li> </ul> It is not possible to have several children filters nested into one parent
     * filter. Only one child is allowed for a parent. I.e. the following is not achievable with this method:
     * <pre>
     * -+ File
     *  +--+ URL
     *     +--+ String
     *     +--+ Integer
     *  +--+ StringBuffer
     *     +--+ Long
     * </pre>
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

//    public static void main(String[] args) {
//        ch.jfactory.logging.LogUtils.init();
//
//        javax.swing.JFrame f = new javax.swing.JFrame();
//        java.awt.Container contentPane = f.getContentPane();
//        contentPane.setLayout(new java.awt.BorderLayout());
//        f.setSize(400, 600);
//        f.setVisible(true);
//        System.out.println(System.currentTimeMillis() + " Started ...");
//        com.ethz.geobot.herbar.model.HerbarModel model = com.ethz.geobot.herbar.Application.getInstance().getModel();
//        ch.jfactory.model.graph.GraphNode root = model.getTaxon("Ranunculaceae").getAsGraphNode();
//        System.out.println(System.currentTimeMillis() + " Model loaded ...");
//        VirtualGraphTreeNodeFilter filter = getFilter(
//                new Class[] { com.ethz.geobot.herbar.model.Taxon.class, com.ethz.geobot.herbar.model.MorText.class,
//                    com.ethz.geobot.herbar.model.MorValue.class, com.ethz.geobot.herbar.model.MorAttribute.class,
//                    com.ethz.geobot.herbar.model.MorSubject.class, com.ethz.geobot.herbar.model.MorAttribute.class,
//                    com.ethz.geobot.herbar.model.MorValue.class, com.ethz.geobot.herbar.model.MorText.class },
//                new int[][] { {1, 0, 0, 2}, {1, 0, 0, 2}, {1, 0, 0, 1}, {1, 0, 0, 1},
//                              {1, 0, 1, 1}, {1, 0, 0, 2}, {1, 0, 0, 2}, {1, 0, 0, 2} });
//        System.out.println(System.currentTimeMillis() + " Filter created ...");
//        javax.swing.JTree tree = com.ethz.geobot.herbar.gui.VirtualGraphTreeFactory.getVirtualTree(root, filter);
//        tree.setShowsRootHandles(true);
//        tree.setRootVisible(true);
//        System.out.println(System.currentTimeMillis() + " Tree made ...");
//        contentPane.add(new javax.swing.JScrollPane(tree), java.awt.BorderLayout.CENTER);
//        f.validate();
//        f.repaint();
//    }










