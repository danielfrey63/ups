/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package simple;

import ch.jfactory.searchtree.JSearchTree;
import java.beans.XMLDecoder;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Simple Example using the Search Tree
 *
 * @author <a href="mail@wegmueller.com">Thomas Wegmueller</a>
 * @version $Revision: 1.1 $ $Date: 2006/08/29 13:10:43 $
 */
public class JSearchTreeExample extends JFrame {

    public JSearchTreeExample() {
        DefaultMutableTreeNode rootNode = null;
        try {
            final XMLDecoder e = new XMLDecoder(this.getClass().getResourceAsStream("model.xml"));
            rootNode = (DefaultMutableTreeNode) e.readObject();
            e.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        final JTree tree = new JTree(treeModel);
        final JSearchTree st = JSearchTree.decorate(tree, true, true, "Searching for:");
        getContentPane().add(new JScrollPane(tree));
        setSize(500, 500);

    }

    public static void main(final String[] args) {
        new JSearchTreeExample().setVisible(true);
    }
}
