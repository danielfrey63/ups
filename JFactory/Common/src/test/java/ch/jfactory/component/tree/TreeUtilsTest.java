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

import abbot.tester.ComponentTester;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import junit.extensions.abbot.ComponentTestFixture;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;

/**
 * Tests the TreeUtils class.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/05/02 15:27:24 $
 */
public class TreeUtilsTest extends ComponentTestFixture {

    /**
     * Constructs a named test.
     *
     * @param base the name
     */
    public TreeUtilsTest(final String base) {
        super(base);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() {
        final ComponentTester tester = new ComponentTester();
    }

    public void testTreeExpansion() {
        final JTree tree = new JTree();
        final List startList = new ArrayList();
        CollectionUtils.addAll(startList, IteratorUtils.asIterator(tree.getExpandedDescendants(tree.getPathForRow(0))));
    }
}
