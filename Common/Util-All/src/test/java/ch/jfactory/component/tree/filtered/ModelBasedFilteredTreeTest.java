/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component.tree.filtered;

import ch.jfactory.component.tree.MapBasedTreeModel;
import javax.swing.JTree;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.uispec4j.Tree;
import org.uispec4j.UISpec4J;

/**
 * Tests for the FilteredTreeModel based on NotifiableTreeModel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/05/02 15:27:11 $
 */
public class ModelBasedFilteredTreeTest extends TestCase
{

    private Tree tree;

    private FilteredTreeModel model;

    private String[] nodes = {
            "colors", "colors/blue", "colors/violet", "colors/red", "colors/yellow",
            "sports", "sports/basketball", "sports/football", "sports/hockey", "sports/soccer",
            "food", "food/hot dogs", "food/bananas", "food/pizza", "food/ravioli",
            "shouldNotExist"
    };

    static
    {
        UISpec4J.init();
    }

    /**
     * Constructs a named test.
     *
     * @param base the name
     */
    public ModelBasedFilteredTreeTest(final String base)
    {
        super(base);
    }

    protected void setUp() throws Exception
    {
        final JTree t = new JTree();
        model = getFilteredTreeModel();
        t.setModel(model);
        tree = new Tree(t);
        tree.expandAll();
    }

    protected FilteredTreeModel getFilteredTreeModel()
    {
        final MapBasedTreeModel tm = new MapBasedTreeModel("JTree");
        tm.insertInto("colors", "JTree", 0);
        tm.insertInto("blue", "colors", 0);
        tm.insertInto("violet", "colors", 1);
        tm.insertInto("red", "colors", 2);
        tm.insertInto("yellow", "colors", 3);
        tm.insertInto("sports", "JTree", 1);
        tm.insertInto("basketball", "sports", 0);
        tm.insertInto("football", "sports", 1);
        tm.insertInto("hockey", "sports", 2);
        tm.insertInto("soccer", "sports", 3);
        tm.insertInto("food", "JTree", 2);
        tm.insertInto("hot dogs", "food", 0);
        tm.insertInto("bananas", "food", 1);
        tm.insertInto("pizza", "food", 2);
        tm.insertInto("ravioli", "food", 3);
        return new FilteredTreeModel(tm);
    }

    public void test000Init()
    {

    }

    /** Test the basic structure of the tree. */
    public void test001SimpleTree()
    {
        doTest(nodes, "1111111111111110");
    }

    /** Tests a simple filter removing one node. */
    public void test002SimpleFilter()
    {
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                return !node.toString().startsWith("r");
            }
        });
        doTest(nodes, "1110111111111100");
    }

    /** Tests the application of two filters removing two separate nodes. */
    public void test003DoubleFilter()
    {
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                return !node.toString().startsWith("r");
            }
        });
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                return !node.toString().startsWith("b");
            }
        });
        doTest(nodes, "1010110111110100");
    }

    public void test004DoubleFilter()
    {
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                return !node.toString().startsWith("r");
            }
        });
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                return !node.toString().startsWith("c");
            }
        });
        doTest(nodes, "0000011111111100");
    }

    public void test005EmptyParent()
    {
        model.addViewFilter(new ViewFilter()
        {
            public boolean isShown(final Object node)
            {
                final String name = node.toString();
                return !name.equals("blue") && !name.equals("red") && !name.equals("violet") && !name.equals("yellow");
            }
        });
        doTest(nodes, "1000011111111110");
    }

    private void doTest(final String[] expectedNodeNames, final String bits)
    {
        assert expectedNodeNames.length == bits.length();
        for (int i = 0; i < bits.length(); i++)
        {
            final String expectedNodeName = expectedNodeNames[i];
            final char bitString = bits.charAt(i);
            final boolean available = bitString == '1';
            boolean failed = false;
            try
            {
                tree.select(expectedNodeName);
                failed = !available;
                tree.selectionEquals(expectedNodeName);
            }
            catch (AssertionFailedError e)
            {
                tree.selectionIsEmpty();
                failed = available;
            }
            if (failed)
            {
                final String negate = (available ? "not " : "");
                assertTrue("\"" + expectedNodeName + "\" should " + negate + "be available", false);
            }
        }
    }
}
