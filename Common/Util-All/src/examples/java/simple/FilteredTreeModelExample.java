/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package simple;

import ch.jfactory.component.tree.MapBasedTreeModel;
import ch.jfactory.component.tree.filtered.FilteredTreeModel;
import ch.jfactory.component.tree.filtered.ViewFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTree;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/08/29 13:10:43 $
 */
public class FilteredTreeModelExample {
    public static void main(final String[] args) {
        final String root = "Root";
        final MapBasedTreeModel ntm = new MapBasedTreeModel(root);
        ntm.insertInto("Parent01", root, 0);
        ntm.insertInto("Parent02", root, 1);
        ntm.insertInto("Anotherparent02", root, 2);
        ntm.insertInto("Child01", "Parent01", 0);
        final FilteredTreeModel ftm = new FilteredTreeModel(ntm);
        final JTree tree = new JTree(ftm);
        final JButton b = new JButton("Filter");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ftm.addViewFilter(new ViewFilter() {
                    public boolean isShown(final Object node) {
                        final String string = (String) node;
                        return !(string.startsWith("Child"));
                    }
                });
            }
        });
        final JFrame f = new JFrame();
        f.add(tree);
        f.add(b, BorderLayout.SOUTH);
        f.setSize(299, 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
