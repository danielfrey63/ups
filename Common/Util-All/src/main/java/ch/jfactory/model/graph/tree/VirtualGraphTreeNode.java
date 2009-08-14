package ch.jfactory.model.graph.tree;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.Role;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNode implements GraphNode
{

    private static final Logger LOGGER = Logger.getLogger(VirtualGraphTreeNode.class);

    /**
     * The ROLE_NULL <code>GraphNode</code> is used because instances of <code> TreePath</code> may not be constructe
     * without an object. I use <code>TreePath</code>s in the {@link VirtualGraphTreeNodeModel}'s cache as a key to the
     * <code>GraphNode2TreeNode</code> objects. The ROLE_NULL object is also used when composing the key to interrogate
     * the cache.
     */
    public static final GraphNode NULL = new GraphNodeImpl();

    /** In a tree model, the parent is unique. */
    private VirtualGraphTreeNode parent;

    /** The wrapped object. */
    private GraphNode dependent;

    /**
     * The model associated with this node. Primary purpose of the model is a separate cache, so more than one tree may
     * be displayed independently.
     */
    private VirtualGraphTreeNodeModel model;

    /** The filter associated with this <code>GraphNode2TreeNode</code> */
    private VirtualGraphTreeNodeFilter filter;

    /**
     * Internal member to hold the unique key to this <code>GraphNode2TreeNode </code> object. It is used as key to
     * identify this object in the cache. The <code>ancestors</code> member is composed of the the (partly hidden)
     * dependent nodes.
     */
    private TreePath ancestors = new TreePath(NULL);

    /**
     * If several children nodes are wrappers for the same {@link #dependent}, and they are merged, so that only
     * distinct <code>dependent<code>s are displayed, this member variable is used to keep the cross reference of the
     * merged <code>GraphNode2TreeNode</code> objects.
     */
    private VirtualGraphTreeNodeList nonDistincts =
            new VirtualGraphTreeNodeList(this);

    /** Constructor is held private for factory method to have better control over construction of objects. */
    private VirtualGraphTreeNode(final GraphNode dependent,
                                 final VirtualGraphTreeNodeModel model, final VirtualGraphTreeNodeFilter filter,
                                 final VirtualGraphTreeNode parent)
    {

        this.dependent = dependent;
        this.model = model;
        this.filter = filter;
        this.parent = parent;
        checkTypeMatching(dependent, filter);
    }

    /**
     * Factory method to create a {@link VirtualGraphTreeNode} root object. No parent is needed.
     *
     * @param dependent the {@link GraphNode} object that is wrapped
     * @param model     the {@link VirtualGraphTreeNodeModel} used
     * @param filter    the {@link VirtualGraphTreeNodeFilter} associated with this node
     * @return either a new <code>GraphNode2TreeNode</code> or an existing one.
     */
    public static VirtualGraphTreeNode getGraphNode(final GraphNode dependent, final VirtualGraphTreeNodeModel model,
                                                    final VirtualGraphTreeNodeFilter filter)
    {

        checkTypeMatching(dependent, filter);
        return createNode(dependent, null, model, filter);
    }

    /**
     * Factory method to create new {@link VirtualGraphTreeNode} objects with the model of a given parent.
     *
     * @param parent the parent <code>VirtualGraphTreeNode</code>
     * @return either a new <code>VirtualGraphTreeNode</code> or an existing one.
     */
    public static VirtualGraphTreeNode getGraphNode(final VirtualGraphTreeNodeFilter filter, final VirtualGraphTreeNode parent)
    {

        final VirtualGraphTreeNodeModel model = parent.model;
        final GraphNode depParent = parent.getDependent();
        final GraphNode dependent = model.createNode(depParent, filter.getType());
        checkTypeMatching(dependent, filter);
        final VirtualGraphTreeNode result = createNode(dependent, parent, model, filter);
        depParent.addChild(dependent);
        return result;
    }

    /**
     * Factory method to add a {@link VirtualGraphTreeNode} object to a given parent.
     *
     * @param parent the parent <code>VirtualGraphTreeNode</code>
     * @return either a new <code>VirtualGraphTreeNode</code> or an existing one.
     */
    public static VirtualGraphTreeNode getGraphNode(final GraphNode dependent, final VirtualGraphTreeNode parent)
    {

        final VirtualGraphTreeNodeModel model = parent.model;
        final Class type = dependent.getClass();
        final VirtualGraphTreeNodeFilter parentFilter = parent.getFilter();
        final VirtualGraphTreeNodeFilter filter = parentFilter.getChildrenFilter(type);
        checkTypeMatching(dependent, filter);
        return createNode(dependent, parent, model, filter);
    }

    /**
     * Each new object has its uniqe tree path (that is, the path formed by visible and invisible nodes as declared by
     * the root {@link VirtualGraphTreeNodeFilter}).
     *
     * @param parent the parent <code>VirtualGraphTreeNode</code>
     * @return either a new <code>VirtualGraphTreeNode</code> or an existing one.
     */
    private static VirtualGraphTreeNode createNode(final GraphNode dependent, final VirtualGraphTreeNode parent,
                                                   final VirtualGraphTreeNodeModel model, final VirtualGraphTreeNodeFilter filter)
    {

        final TreePath key;
        if (parent != null)
        {
            key = parent.ancestors.pathByAddingChild(dependent);
        }
        else
        {
            key = new TreePath(NULL);
        }
        VirtualGraphTreeNode filteredNode = (VirtualGraphTreeNode) model.get(key);

        if (filteredNode == null)
        {
            filteredNode = new VirtualGraphTreeNode(dependent, model, filter, parent);
            model.put(key, filteredNode);
            filteredNode.ancestors = key;
        }

        return filteredNode;
    }

    private static void checkTypeMatching(final GraphNode node, final VirtualGraphTreeNodeFilter filter)
    {
        if (!node.isType(filter.getType()))
        {
            throw new IllegalStateException("Filters type (" + filter.getType() + ") must match nodes type ("
                    + node.getClass() + ")");
        }
    }

    private GraphNodeList getDependents(final GraphNodeList list)
    {
        final GraphNodeList result = new GraphNodeList();
        for (int i = 0; i < list.size(); i++)
        {
            // Bad workaround: VirtualGraphTreeNode should only receive lists
            // of itselfs type, not GraphNodeImpl or so.
            GraphNode node = list.get(i);
            if (node instanceof VirtualGraphTreeNode)
            {
                node = ((VirtualGraphTreeNode) node).getDependent();
                LOGGER.warn("false type translated: " + node);
            }
            result.add(node);
        }
        return result;
    }

    private VirtualGraphTreeNodeList getVirtual(final GraphNodeList list)
    {
        final VirtualGraphTreeNodeList result = new VirtualGraphTreeNodeList();
        for (int i = 0; i < list.size(); i++)
        {
            final VirtualGraphTreeNode node = (VirtualGraphTreeNode) list.get(i);
            result.add(get(node));
        }
        return result;
    }

    private VirtualGraphTreeNode get(final VirtualGraphTreeNode node)
    {
        final TreePath full = node.ancestors.pathByAddingChild(node);
        return (VirtualGraphTreeNode) model.get(full);
    }

    /*
        private VirtualGraphTreeNodeList getFiltered(VirtualGraphTreeNodeList list,
        Class type) {
            if (type.equals("*")) {
                return list;
            }
            VirtualGraphTreeNodeList result = new VirtualGraphTreeNodeList();
            for (int i = 0; i < list.size(); i++) {
                VirtualGraphTreeNode node = (VirtualGraphTreeNode)list.getTreeNode(i);
                if (node.isType(type)) {
                    result.add(node);
                }
            }
            return result;
        }
    */
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
     * Sets the filter.
     *
     * @param filter The filter to set
     */
    public void setFilter(final VirtualGraphTreeNodeFilter filter)
    {
        this.filter = filter;
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

    public void addNonDistinct(final VirtualGraphTreeNode sibling)
    {
        if (!nonDistincts.contains(sibling))
        {
            nonDistincts.add(sibling);
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
     */
    public void addTreeChild(final int index, VirtualGraphTreeNode node)
    {
        final GraphNode dep = node.getDependent();
        final Class type = dep.getClass();
        final VirtualGraphTreeNodeFilter childFilter = filter.getChildrenFilter(type);
        if (node.model != model)
        {
            // Insertion from another tree
            node = createNode(dep, this, model, childFilter);
        }
        if (childFilter.isDescendant())
        {
            addChild(index, node);
        }
        else
        {
            addParent(index, node);
        }
    }

    public VirtualGraphTreeNode addNewTreeChild(final int index, final String name, final Class type)
    {
        final VirtualGraphTreeNodeFilter filter = this.filter.getChildrenFilter(type);
        final VirtualGraphTreeNode node = getGraphNode(filter, this);
        node.setName(name);
        addTreeChild(index, node);
        return node;
    }

    public void removeTreeChild(final VirtualGraphTreeNode node)
    {
        final GraphNode dep = node.getDependent();
        final Class type = dep.getClass();
        final VirtualGraphTreeNodeFilter childFilter = filter.getChildrenFilter(type);
        if (childFilter.isDescendant())
        {
            node.removeFromParent(this);
        }
        else
        {
            node.removeFromChild(this);
        }
    }

    public void deleteTreeChild(final VirtualGraphTreeNode node)
    {
        final GraphNode dep = node.getDependent();
        final Class type = dep.getClass();
        final VirtualGraphTreeNodeFilter childFilter = filter.getChildrenFilter(type);
        if (childFilter.isDescendant())
        {
            deleteChild(node);
        }
        else
        {
            deleteParent(node);
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

    /** @see ch.jfactory.model.graph.GraphNode#isType(Class) */
    public boolean isType(final Class type)
    {
        return dependent.isType(type);
    }

    /**
     * Returns the only tree view parent.
     *
     * @see GraphNode#getParents()
     */
    public GraphNodeList getParents()
    {
        final GraphNodeList result = new GraphNodeList();
        result.add(getParent());
        return result;
    }

    /** @see GraphNode#getParents(Class) */
    public GraphNodeList getParents(final Class type)
    {
        return getParents(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#getParents(Class, Class) */
    public GraphNodeList getParents(final Class type, final Class role)
    {
        return getVirtual(dependent.getParents(type, role));
    }

    /** @see GraphNode#getAllParents(Class) */
    public GraphNodeList getAllParents(final Class type)
    {
        return getAllParents(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#getAllParents(Class, Class) */
    public GraphNodeList getAllParents(final Class type, final Class role)
    {
        return getVirtual(dependent.getAllParents(type, role));
    }

    /** @see GraphNode#getParentRole(GraphNode) */
    public Role getParentRole(final GraphNode node)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) node;
        return getDependent().getParentRole(vNode.getDependent());
    }

    /** @see GraphNode#setParentRole(GraphNode, Role) */
    public void setParentRole(final GraphNode node, final Role role)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) node;
        dependent.setParentRole(vNode.getDependent(), role);
    }

    /** @see GraphNode#getChildren() */
    public GraphNodeList getChildren()
    {
        return model.getChildren(this);
    }

    /** @see GraphNode#getChildren(Class) */
    public GraphNodeList getChildren(final Class type)
    {
        return getChildren(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#getChildren(Class, Class) */
    public GraphNodeList getChildren(final Class type, final Class role)
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList list = AbsGraphModel.getFiltered(getChildren(), type);
        for (int i = 0; i < list.size(); i++)
        {
            final GraphNode node = list.get(i);
            if (role.isAssignableFrom(getDependent().getChildRole(node).getClass()))
            {
                result.add(node);
            }
        }
        return result;
    }

    /** @see GraphNode#getAllChildren(Class) */
    public GraphNodeList getAllChildren(final Class type)
    {
        return getAllChildren(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#getAllChildren(Class, Class) */
    public GraphNodeList getAllChildren(final Class type, final Class role)
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList children = getChildren(type, role);
        for (int i = 0; i < children.size(); i++)
        {
            result.addAll(children);
            result.addAll(children.get(i).getAllChildren(type, role));
        }
        return result;
    }

    /** @see GraphNode#getChildRole(GraphNode) */
    public Role getChildRole(final GraphNode node)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) node;
        return dependent.getChildRole(vNode.getDependent());
    }

    /** @see GraphNode#setChildRole(GraphNode, Role) */
    public void setChildRole(final GraphNode node, final Role role)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) node;
        dependent.setChildRole(vNode.getDependent(), role);
    }

    /**
     * Simple wrapper that delegates to the {@link #dependent}.
     *
     * @see GraphNode#setId(int)
     */
    public void setId(final int id)
    {
        dependent.setId(id);
    }

    /** @see GraphNode#setName(String) */
    public void setName(final String name)
    {
        dependent.setName(name);
    }

    /** @see GraphNode#setRank(int) */
    public void setRank(final int rank)
    {
        dependent.setRank(rank);
    }

    /** @see GraphNode#setChildren(GraphNodeList) */
    public void setChildren(final GraphNodeList children)
    {
        dependent.setChildren(getDependents(children));
    }

    /** @see GraphNode#setChildren(GraphNodeList, Class) */
    public void setChildren(final GraphNodeList children, final Class type)
    {
        setChildren(children, type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#setChildren(GraphNodeList, Class, Class) */
    public void setChildren(final GraphNodeList children, final Class type, final Class role)
    {
        dependent.setChildren(getDependents(children), type, role);
    }

    /** @see GraphNode#addChild(GraphNode) */
    public void addChild(final GraphNode child)
    {
        addChild(dependent.getChildren().size(), child);
    }

    /**
     * Translates the visible index to the index compared to all hidden and visible <code>GraphNode</code>s.
     *
     * @see GraphNode#addChild(int, GraphNode)
     */
    public void addChild(final int index, final GraphNode child)
    {
        addChild(index, child, Role.ROLE_NULL);
    }

    /** @see GraphNode#addChild(GraphNode, Role) */
    public void addChild(final GraphNode child, final Role role)
    {
        addChild(dependent.getChildren().size(), child, role);
    }

    /** @see GraphNode#addChild(int, GraphNode, Role) */
    public void addChild(final int index, final GraphNode child, final Role role)
    {
        // Find index in context of all children
        final GraphNodeList childs = getChildren();
        final VirtualGraphTreeNode visibleInsert = (VirtualGraphTreeNode) childs.get(index);
        final GraphNodeList hidden = dependent.getChildren();
        final int hiddenIndex;
        if (visibleInsert == null)
        {
            // Is at the end of the list
            hiddenIndex = hidden.size();
        }
        else
        {
            // Is before the specified index
            final GraphNode dependentOfVisible = visibleInsert.getDependent();
            hiddenIndex = hidden.get(dependentOfVisible);
        }
        final VirtualGraphTreeNode missile = (VirtualGraphTreeNode) child;
        dependent.addChild(hiddenIndex, missile.getDependent(), role);
    }

    /** @see GraphNode#addNewChild(int, String, Class) */
    public GraphNode addNewChild(final int index, final String name, final Class type)
    {
        return getGraphNode(dependent.addNewChild(index, name, type), this);
    }

    /** @see GraphNode#deleteChild(GraphNode) */
    public boolean deleteChild(final GraphNode child)
    {
        final VirtualGraphTreeNode vChild = (VirtualGraphTreeNode) child;
        return dependent.deleteChild(vChild.getDependent());
    }

    /** @see GraphNode#deleteChildren(Class) */
    public void deleteChildren(final Class type)
    {
        deleteChildren(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#deleteChildren(Class, Class) */
    public void deleteChildren(final Class type, final Class role)
    {
        getDependent().deleteChildren(type, role);
    }

    /** @see GraphNode#removeFromChild(GraphNode) */
    public boolean removeFromChild(final GraphNode child)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) child;
        return getDependent().removeFromChild(vNode.getDependent());
    }

    /** @see GraphNode#setParents(GraphNodeList) */
    public void setParents(final GraphNodeList parents)
    {
        dependent.setParents(getDependents(parents));
    }

    /** @see GraphNode#setParents(GraphNodeList, Class) */
    public void setParents(final GraphNodeList parents, final Class type)
    {
        setParents(getDependents(parents), type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#setParents(GraphNodeList, Class, Class) */
    public void setParents(final GraphNodeList parents, final Class type, final Class role)
    {
        dependent.setParents(getDependents(parents), type, role);
    }

    /** @see GraphNode#addParent(GraphNode) */
    public void addParent(final GraphNode parent)
    {
        addParent(dependent.getParents().size(), parent);
    }

    /** @see GraphNode#addParent(int, GraphNode) */
    public void addParent(final int index, final GraphNode parent)
    {
        addParent(index, parent, Role.ROLE_NULL);
    }

    /** @see GraphNode#addParent(GraphNode, Role) */
    public void addParent(final GraphNode parent, final Role role)
    {
        addParent(dependent.getParents().size(), parent, role);
    }

    /** @see GraphNode#addParent(int, GraphNode, Role) */
    public void addParent(final int index, final GraphNode parent, final Role role)
    {
        // Find index in context of all children. It might be that the parent
        // inserted is not a visible parent of this node, but an invisible one.
        // We have to search for it.
        final GraphNodeList visibles = getChildren();
        final VirtualGraphTreeNode vParent = (VirtualGraphTreeNode) parent;
        final VirtualGraphTreeNodeFilter pFilter = vParent.getFilter();
        VirtualGraphTreeNode visibleInsert = (VirtualGraphTreeNode) visibles.get(index);
        while (visibleInsert != null
                && visibleInsert.getFilter().getType() != pFilter.getType())
        {
            visibleInsert = visibleInsert.parent;
        }
        final int hiddenIndex;
        if (visibleInsert == null)
        {
            // Is at the end of the list
            hiddenIndex = visibles.size();
        }
        else
        {
            final GraphNode dependentOfVisible = visibleInsert.getDependent();
            hiddenIndex = dependent.getParents().get(dependentOfVisible);
        }
        final VirtualGraphTreeNode missile = (VirtualGraphTreeNode) parent;
        dependent.addParent(hiddenIndex, missile.getDependent(), role);
    }

    /** @see GraphNode#addNewParent(int, String, Class) */
    public GraphNode addNewParent(final int index, final String name, final Class type)
    {
        return getGraphNode(dependent.addNewParent(index, name, type), this);
    }

    /** @see GraphNode#deleteParent(GraphNode) */
    public boolean deleteParent(final GraphNode parent)
    {
        final VirtualGraphTreeNode vParent = (VirtualGraphTreeNode) parent;
        return dependent.deleteParent(vParent.getDependent());
    }

    /** @see GraphNode#deleteParents(Class) */
    public void deleteParents(final Class type)
    {
        deleteParents(type, Role.CLASSES_ALL);
    }

    /** @see GraphNode#deleteParents(Class, Class) */
    public void deleteParents(final Class type, final Class role)
    {
        getDependent().deleteParents(type, role);
    }

    /** @see GraphNode#removeFromParent(GraphNode) */
    public boolean removeFromParent(final GraphNode parent)
    {
        final VirtualGraphTreeNode vNode = (VirtualGraphTreeNode) parent;
        return getDependent().removeFromParent(vNode.getDependent());
    }

    /** @see GraphNode#toString() */
    public String toString()
    {
        return dependent.toString();
    }
}

// $Log: VirtualGraphTreeNode.java,v $
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
