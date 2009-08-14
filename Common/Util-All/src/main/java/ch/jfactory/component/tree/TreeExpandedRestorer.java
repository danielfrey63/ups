/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component.tree;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * This class proviedes methodes to save and restore expansion and selection states of a tree. Make sure to save the
 * state on the <b>same nodes</b> as restore takes place on.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.9 $ $Date: 2007/09/27 10:41:47 $
 */
public class TreeExpandedRestorer {

    /**
     * Selections are stored with path information. If you plan to insert different nodes, you should use this option.
     */
    public static final SelectionType SELECTION_BY_PATH = new SelectionType("SelectionByPath");

    /**
     * Selections are stored with row information. If you decide to delete nodes, you should select this option.
     */
    public static final SelectionType SELECTION_BY_ROW = new SelectionType("SelectionByRow");

    private List<TreePath> expandedPaths;
    private JTree tree;
    private TreePath[] selectionPaths;
    private TreePath leadSelectionPath;
    private Rectangle nodeBounds;

    /**
     * Saves the expanded state and selection of the tree.
     *
     * @param tree the tree for which selection and expanstion state are saved
     */
    public TreeExpandedRestorer(final JTree tree) {
        this(tree, false);
    }

    /**
     * Saves the expanded state and optionally discards the selection of the tree.
     *
     * @param tree           the tree for which selection and expanstion state are saved
     * @param clearSelection whether to discard selection
     */
    public TreeExpandedRestorer(final JTree tree, final boolean clearSelection) {
        this.tree = tree;
        if (clearSelection) clearSelection();
    }

    /**
     * Saves the subtree of the give path
     *
     * @param path the path to save the subtree of
     */
    public void save(final TreePath path) {
        saveSelection();
        saveExpandedState(path);
    }

    /**
     * Saves selected node and all expanded paths.
     */
    public void save() {
        saveSelection();
        final Object root = tree.getModel().getRoot();
        if (root != null) {
            saveExpandedState(new TreePath(root));
        }
    }

    /**
     * Use this method if you want to transfer a node with a tree, and you want to have the selection transfered too.
     *
     * @param target the node being transfered
     * @param old    the source tree path where the node was or is attached to
     * @param nw     the destination tree path where the node will be or is attached to
     */
    public void update(final Object target, final TreePath old, final TreePath nw) {
        final TreePath[] tp = (TreePath[]) expandedPaths.toArray(new TreePath[0]);
        for (int i = 0; i < tp.length; i++) {
            final Object[] objs = tp[i].getPath();
            for (int j = 0; j < objs.length; j++) {
                if (objs[j] == target) {
                    final Object[] newPath = new Object[objs.length - old.getPathCount() + nw.getPathCount()];
                    System.arraycopy(nw.getPath(), 0, newPath, 0, nw.getPathCount());
                    System.arraycopy(objs, j, newPath, nw.getPathCount(), objs.length - j);
                    tp[i] = new TreePath(newPath);
                }
            }
        }
    }

    public void remove(final TreePath tp) {
        for (Iterator<TreePath> iterator = expandedPaths.iterator(); iterator.hasNext();) {
            final TreePath path = iterator.next();
            if (tp.isDescendant(path)) iterator.remove();
        }
    }

    /**
     * Expands all paths last saved and restores selected node.
     */
    public void restore() {
        restoreExpandedState();
        restoreSelections();
    }

    public void clearSelection() {
        selectionPaths = null;
    }

    public void setSelection(final TreePath path) {
        selectionPaths = new TreePath[]{path};
    }

    public void addSelection(final TreePath path) {
        final int length = selectionPaths.length;
        final TreePath[] newSelectionPaths = new TreePath[length + 1];
        newSelectionPaths[length] = path;
        selectionPaths = newSelectionPaths;
    }

    public void restoreSelections() {
        final List<TreePath> correctedPaths = new ArrayList<TreePath>();
        if (selectionPaths != null) {

            // Translate selection paths if necessary
            for (int i = 0; i < selectionPaths.length; i++) {
                final TreePath path = selectionPaths[i];
                if (pathExists(path)) {
                    correctedPaths.add(path);
                }
                else if (matchPath(path) != null) {
                    final TreePath matchedPath = matchPath(path);
                    correctedPaths.add(matchedPath);
                }
            }
            final TreePath[] paths = (TreePath[]) correctedPaths.toArray(new TreePath[0]);
            tree.setSelectionPaths(paths);

            // Mark lead selection path
            if (pathExists(leadSelectionPath)) {
                tree.setLeadSelectionPath(leadSelectionPath);
            }
            else {
                tree.setLeadSelectionPath(matchPath(leadSelectionPath));
            }

            // Scroll visible rect so that the selected node is in the same line as before
            final Rectangle visibleRect = tree.getVisibleRect();
            final Rectangle rect = tree.getPathBounds(tree.getLeadSelectionPath());
            if (nodeBounds != null && rect != null) {
                visibleRect.translate(0, rect.y - nodeBounds.y);
                tree.scrollRectToVisible(visibleRect);
            }
        }
    }

    public void restoreExpandedState() {
        for (int i = 0; expandedPaths != null && i < expandedPaths.size(); i++) {
            final TreePath path = expandedPaths.get(i);
            final TreePath pathToExpand;
            if (pathExists(path)) {
                pathToExpand = path;
            }
            else {
                pathToExpand = matchPath(path);
            }
            if (pathToExpand != null) {
                tree.expandPath(pathToExpand);
                TreePath parent = pathToExpand.getParentPath();
                while (parent != null && !tree.isExpanded(parent)) {
                    tree.expandPath(parent);
                    parent = parent.getParentPath();
                }
            }
        }
    }

    private TreePath matchPath(final TreePath oldPath) {
        final Map<Object, TreePath> paths = new HashMap<Object, TreePath>();
        final Object root = tree.getModel().getRoot();
        if (root != null) {
            paths.put(root, new TreePath(root));
            cachePaths(root, paths);
        }
        return paths.get(oldPath.getLastPathComponent());
    }

    private void cachePaths(final Object parent, final Map<Object, TreePath> paths) {
        final TreePath path = paths.get(parent);
        final TreeModel model = tree.getModel();
        for (int i = 0; i < model.getChildCount(parent); i++) {
            final Object child = model.getChild(parent, i);
            final TreePath childPath = path.pathByAddingChild(child);
            paths.put(child, childPath);
            cachePaths(child, paths);
        }
    }

    private void saveExpandedState(final TreePath path) {
        expandedPaths = TreeUtils.getExpandedPaths(tree, path);
    }

    private void saveSelection() {
        selectionPaths = tree.getSelectionPaths();
        leadSelectionPath = tree.getLeadSelectionPath();
        if (leadSelectionPath == null && selectionPaths != null) {
            if (selectionPaths.length > 0) {
                leadSelectionPath = selectionPaths[0];
            }
        }
        nodeBounds = tree.getPathBounds(leadSelectionPath);
    }

    private boolean pathExists(final TreePath path) {
        if (path == null) {
            return false;
        }
        final Object[] objects = path.getPath();
        Object node = tree.getModel().getRoot();
        if (node != objects[ 0 ]) {
            return false;
        }
        for (int i = 1; i < objects.length; i++) {
            final Object object = objects[ i ];
            if (tree.getModel().getIndexOfChild(node, object) == -1) {
                return false;
            }
            node = object;
        }
        return true;
    }

    private static class SelectionType {

        private String type;

        private SelectionType(final String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }
}
