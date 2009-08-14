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

import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import junit.framework.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.uispec4j.Tree;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/05/02 15:18:27 $
 */
public class TreeUpdaterTest extends TestCase
{

    /**
     * Constructs a named test.
     *
     * @param base the name
     */
    public TreeUpdaterTest(final String base)
    {
        super(base);
    }

    /** {@inheritDoc} */
    protected void setUp()
    {
    }

    /** Reloads the tree model and tests expansion state. */
    public void testExpansionOnReload()
    {
        final JTree tree = new JTree();
        final Tree tester = new Tree(tree);
        final TreeUpdater updater = new TreeUpdater(tree);
        tester.expandAll();
        final List<TreePath> storedExpandedPaths = TreeUtils.getExpandedPaths(tree);
        ((DefaultTreeModel) tree.getModel()).reload();
        updater.update();
        compareExpandedStates(tree, storedExpandedPaths);
    }

    /**
     * Compares the contents of both lists and fails if they do not contain the same objects.
     *
     * @param tree   the tree to retrieve the current expansion state from
     * @param stored
     */
    private void compareExpandedStates(final JTree tree, final List<TreePath> stored)
    {
        final List<TreePath> current = TreeUtils.getExpandedPaths(tree);
        assertTrue("Expansion state not equal as before.", CollectionUtils.isEqualCollection(current, stored));
    }

    /** Tests that a different selection really does fail in the test. */
    public void testSelectionNotEqual()
    {
        final JTree tree = new JTree();
        final Tree tester = new Tree(tree);
        final TreeUpdater updater = new TreeUpdater(tree);
        tester.expandAll();
        tester.select("colors/red");
        final String storedSelection = getSelection(tree);
        tester.select("colors/blue");
        updater.update();
        final String actualSelection = getSelection(tree);
        assertNotSame("Selection equal, but shouldn't.", storedSelection, actualSelection);
    }

    /** Reloads a default mutable tree model and tests for the selection state. */
    public void testSelectionOnReload()
    {
        final JTree tree = new JTree();
        final Tree tester = new Tree(tree);
        final TreeUpdater updater = new TreeUpdater(tree);
        tester.expandAll();
        tester.select("colors/red");
        final String storedSelection = getSelection(tree);
        ((DefaultTreeModel) tree.getModel()).reload();
        updater.update();
        final String actualSelection = getSelection(tree);
        assertEquals("Selection not equal.", storedSelection, actualSelection);
    }

    /** Reloads a default mutable tree model and tests for the selection state. */
    public void testSelectionOnExchange()
    {
        final JTree tree = new JTree();
        final Tree tester = new Tree(tree);
        final TreeUpdater updater = new TreeUpdater(tree);
        tester.expandAll();
        tester.select("colors/red");
        final String storedSelection = getSelection(tree);
        tree.setModel(new JTree().getModel());
        updater.update();
        final String actualSelection = getSelection(tree);
        assertEquals("Selection not equal.", storedSelection, actualSelection);
    }

    /**
     * Sets a new tree model that does not contain the selected node any more and tests for the selection state which
     * should be non-equal.
     */
    public void testMatchPaths()
    {
        final JTree tree = new JTree();
        final Tree tester = new Tree(tree);
        final TreeUpdater updater = new TreeUpdater(tree);
        tester.expandAll();
        tester.select("colors/red");
        final String storedSelection = getSelection(tree);
        tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Niet")));
        updater.update();
        final String actualSelection = getSelection(tree);
        assertNotSame("Selection equal, but shouldn't.", storedSelection, actualSelection);
    }

    /**
     * Returns the selected node as string.
     *
     * @param tree the tree to look for the single selected node
     * @return the node text as string
     */
    private String getSelection(final JTree tree)
    {
        final String result;
        final TreePath[] selectionPaths = tree.getSelectionPaths();
        if (selectionPaths == null)
        {
            result = null;
        }
        else
        {
            result = selectionPaths[0].getLastPathComponent().toString();
        }
        return result;
    }
}
