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

import java.util.Collection;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.collections.IteratorUtils;

/**
 * Listens for selection events in the given tree.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class SimpleTreeSelectionListener implements TreeSelectionListener {

    /**
     * The nodes selected.
     */
    private TreePath[] selectionPaths = new TreePath[0];

    /**
     * The tree to restore the selections on.
     */
    private JTree tree;

    /**
     * Constructs a tree selection listener for the given tree
     *
     * @param tree the tree to make this selection listener for
     */
    public SimpleTreeSelectionListener(final JTree tree) {
        this.tree = tree;
    }

    /**
     * {@inheritDoc}
     */
    public void valueChanged(final TreeSelectionEvent treeSelectionEvent) {
        final TreePath[] selectionPaths = tree.getSelectionPaths();
        if ((selectionPaths != null) && (selectionPaths.length > 0)) {
            final TreePath[] selectedTreePaths = tree.getSelectionModel().getSelectionPaths();
            this.selectionPaths = selectedTreePaths;
        }
    }

    /**
     * Restore the selection.
     */
    public void restore() {
        tree.getSelectionModel().setSelectionPaths(selectionPaths);
    }

    /**
     * Call this method if the tree model has been exchanged and you want to keep the selected nodes.
     */
    public void translate() {
        final Collection matches = TreeUtils.matchPaths(IteratorUtils.arrayIterator(selectionPaths), tree.getModel());
        selectionPaths = (TreePath[]) matches.toArray(new TreePath[0]);
    }
}
