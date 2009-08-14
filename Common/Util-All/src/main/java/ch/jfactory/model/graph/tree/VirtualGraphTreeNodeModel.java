package ch.jfactory.model.graph.tree;

import ch.jfactory.model.graph.DirtyListener;
import ch.jfactory.model.graph.GraphEdge;
import ch.jfactory.model.graph.GraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.tree.TreePath;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class VirtualGraphTreeNodeModel implements GraphModel
{

    private VirtualGraphTreeNodeFilter rootFilter;

    private GraphModel model;

    private VirtualGraphTreeNode root = null;

    private HashMap fullPathCache = new HashMap();

    private boolean readOnly;

    public VirtualGraphTreeNodeModel(final VirtualGraphTreeNodeFilter rootFilter, final GraphModel model)
    {
        this.rootFilter = rootFilter;
        this.model = model;
    }

    public VirtualGraphTreeNodeModel(final VirtualGraphTreeNodeFilter rootFilter, final GraphModel model, final GraphNode root)
    {
        this(rootFilter, model);
        this.root = VirtualGraphTreeNode.getGraphNode(root, this, rootFilter);
    }

    public VirtualGraphTreeNodeList getChildren(final VirtualGraphTreeNode parent)
    {

        // collect all children for each non distinct parent
        final VirtualGraphTreeNodeList result = new VirtualGraphTreeNodeList();
        final VirtualGraphTreeNodeList nonDistincts = parent.getNonDistincts();
        for (int i = 0; i < nonDistincts.size(); i++)
        {
            result.addAll(getChildrenFromDistinct(nonDistincts.getTreeNode(i)));
        }

        // unite children and keep parents
        final VirtualGraphTreeNodeList distincts = new VirtualGraphTreeNodeList();
        for (int i = 0; i < result.size(); i++)
        {
            final VirtualGraphTreeNode newChild = result.getTreeNode(i);
            boolean is = false;
            int j = 0;
            for (; j < distincts.size(); j++)
            {
                final VirtualGraphTreeNode node = distincts.getTreeNode(j);
                if (node.getDependent() == newChild.getDependent())
                {
                    is = true;
                    break;
                }
            }
            if (!is)
            {
                distincts.add(newChild);
            }
            else
            {
                final VirtualGraphTreeNode collector = (VirtualGraphTreeNode) distincts.get(j);
                collector.addNonDistinct(newChild);
            }
        }
        final Comparator comparator = parent.getFilter().getComparator();
        if (comparator != null)
        {
            distincts.sort(comparator);
        }
        return distincts;
    }

    private VirtualGraphTreeNodeList getChildrenFromDistinct(final VirtualGraphTreeNode parent)
    {

        final VirtualGraphTreeNodeList result = new VirtualGraphTreeNodeList();
        VirtualGraphTreeNodeFilter filter = parent.getFilter();
        VirtualGraphTreeNodeFilter[] childrenFilters = filter.getChildrenFilters();

        // if recursive, add this filter to array of filters to search for
        if (filter.isRecursive() | filter.getType() == VirtualGraphTreeNodeFilter.CLASSES_ALL)
        {
            final int len = childrenFilters.length + 1;
            final VirtualGraphTreeNodeFilter[] tempFilters = new VirtualGraphTreeNodeFilter[len];
            System.arraycopy(childrenFilters, 0, tempFilters, 1, len - 1);
            tempFilters[0] = filter;
            childrenFilters = tempFilters;
        }

        // add each filter with it's visible GraphNode2TreeNode objects
        // subsequently
        final GraphNode dependent = parent.getDependent();
        GraphNodeList graphNodeChildren;
        for (VirtualGraphTreeNodeFilter childrenFilter : childrenFilters)
        {
            filter = childrenFilter;
            final Class type = filter.getType();
            final Class role = filter.getRole();

            // descendant flag and filtering
            if (filter.isBothDirections())
            {
                graphNodeChildren = dependent.getChildren(type, role);
                graphNodeChildren.addAll(dependent.getParents(type, role));
            }
            else if (filter.isDescendant())
            {
                graphNodeChildren = dependent.getChildren(type, role);
            }
            else
            {
                graphNodeChildren = dependent.getParents(type, role);
            }

            // bound flag
            graphNodeChildren = getBoundChildren(graphNodeChildren, filter, parent);

            // visibility flag
            for (int j = 0; j < graphNodeChildren.size(); j++)
            {
                final GraphNode child = (GraphNode) graphNodeChildren.get(j);
                final VirtualGraphTreeNode node = VirtualGraphTreeNode.getGraphNode(child, parent);
                if (filter.isVisible())
                {
                    final AbsGraphTreeNodeFilter nodeFilter = filter.getNodeFilter();
                    if (nodeFilter == null || (nodeFilter != null && nodeFilter.isVisible(node)))
                    {
                        result.add(node);
                    }
                    else
                    {
                        result.addAll(getChildren(node));
                    }
                }
                else
                {
                    result.addAll(getChildren(node));
                }
            }
        }
        return result;
    }

    /**
     * I first try to find the next equal filter type, starting at the current filter and stepping upwards to the root
     * filter. After the filter has been found, the corresponding GraphNode is searched.
     */
    private GraphNodeList getBoundChildren(final GraphNodeList graphNodes,
                                           final VirtualGraphTreeNodeFilter filter, VirtualGraphTreeNode parent)
    {

        if (!filter.isBound())
        {
            return graphNodes;
        }
        VirtualGraphTreeNodeFilter parentFilter = filter;
        final Class boundType = filter.getType();
        while ((parentFilter = parentFilter.getParent()) != null)
        {
            if (parentFilter.getType().isAssignableFrom(boundType))
            {
                // find the matching GraphNode
                while (parent != null && !boundType.isAssignableFrom(parent.getDependent().getClass()))
                {
                    parent = parent.getParent();
                }
                if (parent != null)
                {
                    graphNodes.clear();
                    graphNodes.add(parent.getDependent());
                }
            }
        }
        return graphNodes;
    }

    public void put(final TreePath key, final GraphNode value)
    {
        fullPathCache.put(key, value);
    }

    public GraphNode get(final TreePath key)
    {
        return (GraphNode) fullPathCache.get(key);
    }

    public void remove(final TreePath key)
    {
        fullPathCache.remove(key);
    }

//    private TreePath[] getTreePathsFromVisible(TreePath visible) {
//        return null;
//    }
//
//    private TreePath getVisibleTreePath(TreePath key) {
//        TreePath newPath = new TreePath(VirtualGraphTreeNode.NULL);
//        VirtualGraphTreeNodeFilter filter = rootFilter;
//
//        Object[] obj = key.getPath();
//        // first object is ROLE_NULL object
//        for (int i = 1; i < obj.length; i++) {
//            GraphNode node = (GraphNode) obj[i];
//            Class type = node.getClass();
//            if (!filter.getType().isAssignableFrom(type)) {
//                filter = getNextMatchingChildrenFilter(filter, type);
//            }
//            if (filter.isVisible()) {
//                newPath = newPath.pathByAddingChild(obj[i]);
//            }
//        }
//        if (filter.isVisible()) {
//            return newPath;
//        }
//        else {
//            return key;
//        }
//    }
//
//    private VirtualGraphTreeNodeFilter getNextMatchingChildrenFilter(VirtualGraphTreeNodeFilter filter, Class type) {
//
//        VirtualGraphTreeNodeFilter[] children = filter.getChildrenFilters();
//        for (int j = 0; j < children.length; j++) {
//            if (children[j].getType().isAssignableFrom(type)) {
//                filter = children[j];
//            }
//        }
//        return filter;
//    }

    /** @see ch.jfactory.model.graph.GraphModel#getRoot() */
    public GraphNode getRoot()
    {
        if (root == null)
        {
            root = VirtualGraphTreeNode.getGraphNode(model.getRoot(), this, rootFilter);
        }
        if (!root.isType(rootFilter.getType()))
        {
            throw new IllegalStateException("Root filter\'s type must match root node\'s type. filter: " + rootFilter + ", node: " + root);
        }
        return root;
    }

    /** @see ch.jfactory.model.graph.GraphModel#createEdge(GraphNode, GraphNode) */
    public GraphEdge createEdge(final GraphNode parent, final GraphNode child)
    {
        throw new NoSuchMethodError("getEdge not implemented yet");
    }

    /** @see ch.jfactory.model.graph.GraphModel#addChanged(GraphNode) */
    public void addChanged(final GraphNode node)
    {
        model.addChanged(((VirtualGraphTreeNode) node).getDependent());
    }

    /** @see ch.jfactory.model.graph.GraphModel#addChanged(GraphEdge) */
    public void addChanged(final GraphEdge node)
    {
        model.addChanged(node);
    }

    /** @see ch.jfactory.model.graph.GraphModel#createNode(GraphNode, Class) */
    public GraphNode createNode(final GraphNode parent, final Class type)
    {
        return model.createNode(parent, type);
    }

    /** @see ch.jfactory.model.graph.GraphModel#save() */
    public void save()
    {
        throw new NoSuchMethodError("save not implemented yet");
    }

    /** @see ch.jfactory.model.graph.GraphModel#addRemoved(GraphNode) */
    public void addRemoved(final GraphNode node)
    {
        throw new NoSuchMethodError("addRemoved(node) not implemented yet");
    }

    /** @see ch.jfactory.model.graph.GraphModel#addRemoved(GraphEdge) */
    public void addRemoved(final GraphEdge edge)
    {
        throw new NoSuchMethodError("addRemoved(edge) not implemented yet");
    }

    /** @see ch.jfactory.model.graph.GraphModel#doQuit() */
    public void doQuit()
    {
        model.doQuit();
    }

    /** @see ch.jfactory.model.graph.GraphModel#addDirtyListener(DirtyListener) */
    public void addDirtyListener(final DirtyListener listener)
    {
        model.addDirtyListener(listener);
    }

    /** @see ch.jfactory.model.graph.GraphModel#removeDirtyListener(DirtyListener) */
    public void removeDirtyListener(final DirtyListener listener)
    {
        model.removeDirtyListener(listener);
    }

    /** @see ch.jfactory.model.graph.GraphModel#setDirty(boolean) */
    public void setDirty(final boolean dirty)
    {
        model.setDirty(dirty);
    }

    /** @see ch.jfactory.model.graph.GraphModel#getDirty() */
    public boolean getDirty()
    {
        return model.getDirty();
    }

    public void setReadOnly()
    {
        this.readOnly = true;
    }

    public boolean isReadonly()
    {
        return readOnly;
    }
}
