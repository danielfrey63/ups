package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.application.SystemUtil;
import ch.jfactory.collection.IntSet;
import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.AbsSimplePersistentGraphNode;
import ch.jfactory.model.graph.GraphEdge;
import ch.jfactory.model.graph.GraphEdgeImpl;
import ch.jfactory.model.graph.GraphEdgeList;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.SimpleTransientGraphNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class GraphModelImpl extends AbsGraphModel {
    /*
     * Caching:
     * - Make sure that nodes already in the removed cache are eliminated
     * uppon next retrieval from (still valid) global cache.
     */

    private static final Category cat = Category.getInstance(GraphModelImpl.class);
    private static final boolean DEBUG = cat.isDebugEnabled();

    private static final GraphModelImpl INSTANCE = new GraphModelImpl();

    private static Statement stmt;
    private static Connection conn;

    private static HashMap nodeCache = new HashMap();
    private static ArrayList newEdges = new ArrayList();
    private static ArrayList newNodes = new ArrayList();
    private static ArrayList removedEdges = new ArrayList();
    private static ArrayList removedNodes = new ArrayList();
    private static HashSet changedNodes = new HashSet();
    private static HashSet changedEdges = new HashSet();
    private static HashMap edgeIdCache = new HashMap();
    private static HashMap edgeBuildCache = new HashMap();
    private static HashMap edgeCache = new HashMap();

    private static int maxNewEdgeId = -1;
    private static int maxNewNodeId = -1;
    private static PreparedStatement selectVertices;
    private static PreparedStatement selectChildren;
    private static PreparedStatement selectParents;
    private static PreparedStatement selectEdges;

    static {
        try {
            Class.forName(driver);
        }
        catch (Exception e) {
            cat.fatal("Fatal error during initialization of model", e);
        }
    }

    static void getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            mod = System.getProperty(MODEL);
            typeFact = System.getProperty(TYPE_FACTORY);
            roleFact = System.getProperty(ROLE_FACTORY);
            url = System.getProperty(URL).replaceFirst("\\$\\{user\\.home\\}", System.getProperty("user.home").replace('\\', '/') + "/.hcd2");
            user = System.getProperty(USER);
            password = System.getProperty(PASSWORD);
            driver = System.getProperty(DRIVER);
            if (DEBUG) {
                cat.debug("getting model (xmatrix.input.model): " + mod);
                cat.debug("getting type factory (xmatrix.input.model.typefactory): " + typeFact);
                cat.debug("getting role factory (xmatrix.input.model.rolefactory): " + roleFact);
                cat.debug("getting original url (xmatrix.input.db): " + System.getProperty(URL));
                cat.debug("getting url (xmatrix.input.db corrected): " + url);
                cat.debug("getting user (xmatrix.input.user): " + user);
                cat.debug("getting password (xmatrix.input.pw): " + password);
                cat.debug("getting driver (xmatrix.input.driver): " + driver);
            }
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            selectVertices = conn.prepareStatement("SELECT name, rank, type FROM vertices WHERE id = ?");
            selectEdges = conn.prepareStatement("SELECT e.parent_id, e.child_id FROM edges e WHERE e.id = ?");
            selectChildren = conn.prepareStatement("SELECT e.id FROM edges e, vertices c WHERE c.id = e.child_id "
                    + " AND e.parent_id = ? ORDER BY e.rank, c.rank, c.id");
            selectParents = conn.prepareStatement("SELECT e.id, e.parent_id FROM edges e, vertices p "
                    + " WHERE p.id = e.parent_id AND e.child_id = ? ORDER BY e.rank, p.rank, p.id");
        }
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#doQuit()
     */
    public void doQuit() {
//        ResultSet rs = null;
        try {
            //stmt.executeUpdate("SELECT * FROM vertices v LEFT OUTER JOIN "
            //    + "edges e ON e.child_id = v.id WHERE child_id IS ROLE_NULL");

            stmt.executeUpdate("SHUTDOWN COMPACT");
            stmt.close();
            conn.close();
        }
        catch (Exception e) {
            cat.fatal("Fatal error during quit", e);
        }
    }

    public static GraphModelImpl getInstance() {
        return INSTANCE;
    }

    private GraphNode getNode(int id) {
        // Return null for zero id
        if (id == 0) {
            return null;
        }

        // Check for cached node. This must be a AbsSimplePersistentGraphNode
        // as we need access to the edges children.
        Integer bigId = new Integer(id);
        AbsSimplePersistentGraphNode gn = (AbsSimplePersistentGraphNode) nodeCache.get(bigId);
        if (gn != null) {
            removedNodes.remove(gn);
            return gn;
        }

        // Fetch from database
        ResultSet rs = null;
        try {
            selectVertices.setInt(1, id);
            rs = selectVertices.executeQuery();
            rs.next();
            String type = rs.getString("type");
            gn = (AbsSimplePersistentGraphNode) getTypeFactory().getInstance(type);
            gn.setId(id);
            gn.setName(rs.getString("name"));
            gn.setRank(rs.getInt("rank"));
            rs.close();

            // Important to put the new node into the cache before any
            // recursions take place
            nodeCache.put(bigId, gn);

            // Collect child ids
            selectChildren.setInt(1, id);
            rs = selectChildren.executeQuery();
            IntSet childEdgeIds = new IntSet();
            while (rs.next()) {
                childEdgeIds.add(rs.getInt("id"));
            }
            rs.close();

            // Fill children and load parents
            GraphEdgeList children = gn.getChildrenEdges();
            int[] childEdgeIs = childEdgeIds.getArray();
            for (int i = 0; i < childEdgeIs.length; i++) {
                children.add(getEdge(childEdgeIs[ i ]));
            }

            return gn;
        }
        catch (Exception e) {
            e.printStackTrace();
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            SystemUtil.EXIT.exit(1);
        }
        return null;
    }

    private GraphEdge getEdge(int id) {
        Integer bigId = new Integer(id);
        GraphEdge edge = (GraphEdge) edgeIdCache.get(bigId);
        if (edge == null) {
            // Temporary cache while building the objects
            edge = (GraphEdge) edgeBuildCache.get(bigId);
        }
        if (edge != null) {
            return edge;
        }
        ResultSet rs = null;
        try {
            selectEdges.setInt(1, id);
            rs = selectEdges.executeQuery();
            rs.next();
            int parentId = rs.getInt("parent_id");
            int childId = rs.getInt("child_id");
            rs.close();

            edge = new GraphEdgeImpl(id);
            edgeBuildCache.put(bigId, edge);

            GraphNode parent = getNode(parentId);
            GraphNode child = getNode(childId);

            edgeBuildCache.remove(bigId);
            edge.setParent(parent);
            edge.setChild(child);
            putCombinedEdge(edge);

            return edge;
        }
        catch (Exception e) {
            e.printStackTrace();
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * To speed up this bottleneck a combined id is used.
     */
    public void putCombinedEdge(GraphEdge edge) {
        GraphNode parent = edge.getParent();
        GraphNode child = edge.getChild();
        long newId = getCombinedId(parent, child);
        edgeCache.put(new Long(newId), edge);
        edgeIdCache.put(new Integer(edge.getId()), edge);
    }

    public Object removeCombinedEdge(GraphEdge edge) {
        if (edge == null) {
            return null;
        }
        return edgeIdCache.remove(new Integer(edge.getId()));
    }

    private GraphEdge getCombinedEdge(GraphNode parent, GraphNode child) {
        Collection values = edgeIdCache.values();
        if (isReadOnly()) {
            long newId = getCombinedId(parent, child);
            return (GraphEdge) edgeCache.get(new Long(newId));
        }
        else {
            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                GraphEdge e = (GraphEdge) iterator.next();
                if (e.getParent() == parent && e.getChild() == child) {
                    return e;
                }
            }
            return null;
        }
    }

    private long getCombinedId(GraphNode parent, GraphNode child) {
        long pId = 0;
        long cId = 0;
        if (parent != null) {
            pId = (long) parent.getId();
        }
        if (child != null) {
            cId = (long) child.getId();
        }
        return (pId << 32) + cId;
    }

    /**
     * Uppon call of this method, the new, update and delete caches are cleared.
     *
     * @see ch.jfactory.model.graph.GraphModel#getRoot()
     */
    public GraphNode getRoot() {
        GraphNode result = null;
        ResultSet rs = null;
        try {
            getConnection();
            // Load root
            rs = stmt.executeQuery("SELECT id FROM vertices WHERE type = \'ROOT\'");
            rs.next();
            int id = rs.getInt(1);
            rs.close();
            result = getNode(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        changedNodes.clear();
        changedEdges.clear();
        cat.debug("cleard changed/node");
        cat.debug("cleard changed/edge");
        cat.info("Model loaded");
        return result;
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#createEdge(GraphNode,GraphNode)
     */
    public GraphEdge createEdge(GraphNode parent, GraphNode child) {
        GraphEdge edge = getCombinedEdge(parent, child);
        if (edge == null) {
            // Resurect
            for (Iterator iter = removedEdges.iterator(); iter.hasNext();) {
                GraphEdge e = (GraphEdge) iter.next();
                if (getCombinedId(parent, child) ==
                        getCombinedId(e.getParent(), e.getChild())) {
                    edge = e;
                    iter.remove();
                }
            }
        }
        if (edge == null) {
            edge = new GraphEdgeImpl(maxNewEdgeId, parent, child);
            if (!(SimpleTransientGraphNode.class.isAssignableFrom(parent.getClass())
                    || SimpleTransientGraphNode.class.isAssignableFrom(child.getClass()))) {
                newEdges.add(edge);
            }
        }
        // Resurect eventually removed egdes / nodes
        removedEdges.remove(edge);
        removedNodes.remove(parent);
        removedNodes.remove(child);
        // Make sure new nodes that have been removed before from the cache
        // are resurected. I.e. during dnd.
        if (parent.getId() < 0 && !newNodes.contains(parent)) {
            newNodes.add(parent);
        }
        if (child.getId() < 0 && !newNodes.contains(child)) {
            newNodes.add(child);
        }
        if (cat.isDebugEnabled()) {
            cat.debug("added to new/edge cache " + edge);
        }
        putCombinedEdge(edge);
        if (cat.isDebugEnabled()) {
            cat.debug("added to global/edge cache " + edge);
        }
        maxNewEdgeId--;
        return edge;
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#addRemoved(GraphNode)
     */
    public void addRemoved(GraphNode node) {
        if (newNodes.contains(node)) {
            newNodes.remove(node);
            if (cat.isDebugEnabled()) {
                cat.debug("removed from new/node cache " + node);
            }
        }
        else {
            removedNodes.add(node);
            if (cat.isDebugEnabled()) {
                cat.debug("added to remove/node cache " + node);
            }
        }
        setDirty(true);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#addRemoved(GraphEdge)
     */
    public void addRemoved(GraphEdge edge) {
        Object obj;
        // Edge has never been saved
        if (newEdges.contains(edge)) {
            newEdges.remove(edge);
            if (cat.isDebugEnabled()) {
                cat.debug("removed from new/edge cache " + edge);
            }
        }
        // Edge is already persistent
        else {
            if (changedEdges.contains(edge)) {
                changedEdges.remove(edge);
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from changed/edge cache " + edge);
                }
            }
            if (!removedEdges.contains(edge)) {
                removedEdges.add(edge);
                if (cat.isDebugEnabled()) {
                    cat.debug("added to remove/edge cache " + edge);
                }
            }
        }
        obj = removeCombinedEdge(edge);
        if (cat.isDebugEnabled()) {
            cat.debug("removed from global/edge cache " + obj);
        }
        setDirty(true);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#addChanged(GraphNode)
     */
    public void addChanged(GraphNode node) {
        if (!newNodes.contains(node)) {
            changedNodes.add(node);
            setDirty(true);
            if (cat.isDebugEnabled()) {
                cat.debug("added to changed/node cache " + node);
            }
        }
        else {
            if (cat.isDebugEnabled()) {
                cat.debug("node already in new/node cache " + node);
            }
        }
        setDirty(true);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#addChanged(GraphEdge)
     */
    public void addChanged(GraphEdge edge) {
        if (!newEdges.contains(edge)) {
            // New edges that have already been removed before saving are not
            // new edges cache or the removed edges cache.
            if (newNodes.contains(edge.getParent())
                    || newNodes.contains(edge.getChild())) {
                newEdges.add(edge);
            }
            else {
                changedEdges.add(edge);
            }
            setDirty(true);
            if (cat.isDebugEnabled()) {
                cat.debug("added to changed/edge cache " + edge);
            }
        }
        else {
            if (cat.isDebugEnabled()) {
                cat.debug("edge already in new/edge cache " + edge);
            }
        }
        setDirty(true);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#createNode(GraphNode,Class)
     */
    public GraphNode createNode(GraphNode parent, Class type) {
        GraphNode node = getTypeFactory().getInstance(type);
        newNodes.add(node);
        node.setId(maxNewNodeId);
        node.setName("<new node>");
        if (parent != null) {
            node.addParent(parent);
        }
        if (cat.isDebugEnabled()) {
            cat.debug("added to new/node cache " + node);
        }
        nodeCache.put(new Integer(maxNewNodeId), node);
        if (cat.isDebugEnabled()) {
            cat.debug("added to global/node cache " + node);
        }
        maxNewNodeId--;
        return node;
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#save()
     */
    public void save() {
        GraphNode node = null;
        GraphEdge edge = null;
        try {
            getConnection();
            for (Iterator iter = changedNodes.iterator(); iter.hasNext();) {
                node = (GraphNode) iter.next();
                stmt.executeUpdate("UPDATE vertices "
                        + "SET name = \'" + node.getName() + "\'"
                        + ", rank = " + node.getRank()
                        + "WHERE id = " + node.getId());
                iter.remove();
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from changed/node cache " + node);
                }
            }
            // Save new nodes before new or changed edges, as this alters the id
            // of the new nodes, and therefore the references to it. Like this
            // the correct ids are saved to the db.
            // Make sure that the edge cache is updated, as it depends from the
            // node ids.
            edge = null;
            for (Iterator iter = newNodes.iterator(); iter.hasNext();) {
                node = (GraphNode) iter.next();
                // Remove from N cache as this would be with wrong id
                int oldId = node.getId(); // Before calling IDENTITY()
                nodeCache.remove(new Integer(oldId));
                // Update edges cache before saving. Remove all edges
                // corresponding to this node in order to change the ids of the
                // nodes later, and readding the edges then.
                List tEdges = new ArrayList();
                GraphNodeList list = node.getChildren();
                Object rem = null;
                for (int i = 0; i < list.size(); i++) {
                    GraphNode child = list.get(i);
                    rem = removeCombinedEdge(getCombinedEdge(node, child));
                    if (rem == null) {
                        cat.error("Edge not in cache for parent " + node
                                + " and child " + child);
                        printAllCaches(".*" + node.getName() + ".*"
                                + child.getName() + ".*");
                    }
                    else {
                        tEdges.add(rem);
                    }
                }
                list = node.getParents();
                for (int i = 0; i < list.size(); i++) {
                    GraphNode parent = list.get(i);
                    rem = removeCombinedEdge(getCombinedEdge(parent, node));
                    if (rem == null) {
                        cat.error("Edge not in cache for parent " + parent
                                + " and child " + node);
                        printAllCaches(".*" + parent.getName() + ".*"
                                + node.getName() + ".*");
                    }
                    else {
                        tEdges.add(rem);
                    }
                }
                // Update db entries
                String name = "\'" + node.getName() + "\'";
                String type = "\'" + getTypeFactory().getType(node) + "\'";
                int rank = node.getRank();
                stmt.executeUpdate("INSERT INTO vertices "
                        + "( name, rank, type ) { VALUES ( " + name
                        + ", " + rank + ", " + type + " ) }");
                ResultSet rs = stmt.executeQuery("CALL IDENTITY()");
                rs.next();
                int id = rs.getInt(1);
                rs.close();
                node.setId(id);
                // Update permanent caches
                nodeCache.put(new Integer(id), node);
                for (Iterator iterator = tEdges.iterator(); iterator.hasNext();) {
                    GraphEdge tEdge = (GraphEdge) iterator.next();
                    putCombinedEdge(tEdge);
                }
                // Update n cache
                iter.remove();
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from new/node cache " + node);
                }
            }

            node = null;
            for (Iterator iter = changedEdges.iterator(); iter.hasNext();) {
                edge = (GraphEdge) iter.next();
                stmt.executeUpdate("UPDATE edges "
                        + "SET parent_id = " + edge.getParent().getId()
                        + ", child_id = " + edge.getChild().getId()
                        + ", rank = " + edge.getRank()
                        + "WHERE id = " + edge.getId());
                iter.remove();
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from changed/edge cache " + edge);
                }
            }
            edge = null;
            for (Iterator iter = removedEdges.iterator(); iter.hasNext();) {
                edge = (GraphEdge) iter.next();
                stmt.executeUpdate("DELETE FROM edges WHERE id = " + edge.getId());
                stmt.close();
                iter.remove();
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from removed/edge cache " + edge);
                }
            }
            node = null;
            for (Iterator iter = removedNodes.iterator(); iter.hasNext();) {
                node = (GraphNode) iter.next();
                int id = node.getId();
                stmt.executeUpdate("DELETE FROM edges WHERE parent_id = " + id
                        + " OR child_id = " + id);
                stmt.executeUpdate("DELETE FROM vertices WHERE id = " + id);
                stmt.close();
                iter.remove();
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from remvoed/node cache " + node);
                }
            }

            node = null;
            for (Iterator iter = newEdges.iterator(); iter.hasNext();) {
                edge = (GraphEdge) iter.next();
                removeCombinedEdge(edge);
                int parentId = edge.getParent().getId();
                int childId = edge.getChild().getId();
                if (parentId > 0 && childId > 0) {
                    int rank = edge.getRank();
                    stmt.executeUpdate("INSERT INTO edges ( parent_id, child_id, rank ) { VALUES ( " +
                            parentId + ", " + childId + ", " + rank + " ) }");
                    ResultSet rs = stmt.executeQuery("CALL IDENTITY()");
                    rs.next();
                    int id = rs.getInt(1);
                    rs.close();
                    edge.setId(id);
                    iter.remove();
                }
                else {
                    cat.warn("not saved edges as node missing: " + edge);
                }

                putCombinedEdge(edge);
                if (cat.isDebugEnabled()) {
                    cat.debug("removed from new/edge cache " + edge);
                }
            }
            // Clean up zombie records
//            stmt.executeUpdate("DELETE FROM vertices WHERE id NOT IN "
//                + "(SELECT DISTINCT parent_id FROM edges "
//                + "UNION SELECT child_id FROM edges) "
//                + "AND type != 'ROOT'");
            setDirty(false);
            newEdges.clear();
            newNodes.clear();
            changedNodes.clear();
            changedEdges.clear();
            maxNewEdgeId = -1;
            maxNewNodeId = -1;
        }
        catch (SQLException e) {
            cat.fatal("Fatal error during save");
            if (node != null) {
                cat.fatal("Node is " + node);
            }
            if (edge != null) {
                cat.fatal("Edge is " + edge + "(id=" + edge.getId() + ")");
                cat.fatal("Parent is " + edge.getParent()
                        + "(id=" + edge.getParent().getId() + ")");
                cat.fatal("Child is " + edge.getChild()
                        + "(id=" + edge.getChild().getId() + ")");
            }
            cat.fatal("Unable to recover error", e);
            throw new IllegalStateException("Save failed");
        }
    }

    public void printAllCaches(String search) {
        Collection[] lists = new Collection[]{newNodes, changedNodes, removedNodes, newEdges, changedEdges,
                removedEdges, edgeIdCache.values(), nodeCache.values()};
        String[] titles = {"nodes new", "nodes changed", "nodes removed",
                "edges new", "edges changed", "edges removed", "edge cache",
                "edge id cache", "node cache"};
        for (int i = 0; i < titles.length; i++) {
            int count = 0;
            StringBuffer buffer = new StringBuffer();
            for (Iterator iter = lists[ i ].iterator(); iter.hasNext();) {
                Object o = iter.next();
                Pattern p = Pattern.compile(search);
                Matcher m = p.matcher(o.toString());
                boolean b = m.matches();
                if (b) {
                    if (GraphNode.class.isAssignableFrom(o.getClass())) {
                        buffer.append(o + "[" + ((GraphNode) o).getId() + "]"
                                + o.hashCode()
                                + System.getProperty("line.separator"));
                    }
                    else {
                        buffer.append(o + "[" + ((GraphEdge) o).getId() + "]"
                                + o.hashCode()
                                + System.getProperty("line.separator"));
                    }
                    count++;
                }
            }
            System.out.println("+++ " + titles[ i ] + " total " + count);
            System.out.println(buffer);
        }
    }

    public void printAllCacheSizes(String search) {
        Collection[] lists = new Collection[]{newNodes, changedNodes, removedNodes, newEdges, changedEdges,
                removedEdges, edgeIdCache.values(), nodeCache.values()};
        String[] titles = {"nodes new", "nodes changed", "nodes removed",
                "edges new", "edges changed", "edges removed", "edge cache",
                "edge id cache", "node cache"};
        for (int i = 0; i < titles.length; i++) {
            int count = 0;
            for (Iterator iter = lists[ i ].iterator(); iter.hasNext();) {
                Object o = iter.next();
                Pattern p = Pattern.compile(search);
                Matcher m = p.matcher(o.toString());
                boolean b = m.matches();
                if (b) {
                    count++;
                }
            }
            System.out.println("+++ " + titles[ i ] + " total " + count);
        }
    }

    public void printDynamicCaches(String search) {
        Collection[] lists = new Collection[]{newNodes, changedNodes,
                removedNodes, newEdges, changedEdges, removedEdges};
        String[] titles = {"nodes new", "nodes changed", "nodes removed",
                "edges new", "edges changed", "edges removed"};
        for (int i = 0; i < titles.length; i++) {
            int count = 0;
            StringBuffer buffer = new StringBuffer();
            for (Iterator iter = lists[ i ].iterator(); iter.hasNext();) {
                Object o = iter.next();
                Pattern p = Pattern.compile(search);
                Matcher m = p.matcher(o.toString());
                boolean b = m.matches();
                if (b) {
                    buffer.append(o + " " + o.hashCode()
                            + System.getProperty("line.separator"));
                    count++;
                }
            }
            System.out.println("+++ " + titles[ i ] + " total " + count);
            System.out.println(buffer);
        }
    }
}

class IntArray {

    private int[] ints = new int[1];
    private int size = 0;

    public void add(int i) {
        if (size == ints.length) {
            int[] newInts = new int[ints.length * 2];
            System.arraycopy(ints, 0, newInts, 0, ints.length);
            ints = newInts;
        }
        ints[ size++ ] = i;
    }

    public int[] getArray() {
        int[] result = new int[size];
        System.arraycopy(ints, 0, result, 0, size);
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "" + ints;
    }
}
