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
package ch.jfactory.projecttime.project.builder;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.component.tree.DefaultMutableTreeModel;
import ch.jfactory.component.tree.NotifiableTreeModel;
import ch.jfactory.component.tree.dnd.DnDTree;
import ch.jfactory.component.tree.dnd.DnDValidatorUpdater;
import ch.jfactory.lang.DateUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.main.MainModel;
import ch.jfactory.projecttime.project.ProjectModel;
import ch.jfactory.projecttime.project.command.AddEntry;
import ch.jfactory.projecttime.project.command.Commands;
import ch.jfactory.projecttime.project.command.DeleteEntry;
import ch.jfactory.resource.ImageLocator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * The builder for the project panel which has a tree and some toolbar. The tree displays the current projects
 * structure.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class ProjectBuilder extends ActionCommandPanelBuilder {

    private JTree tree;
    private MainModel model;

    public ProjectBuilder(MainModel model) {
        this.model = model;
    }

    protected JComponent createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(createToolbar(), BorderLayout.NORTH);
        panel.add(createTree(), BorderLayout.CENTER);
        return panel;
    }

    protected void initCommands() {
        initCommand(new AddEntry(model.getProjectModel(), getCommandManager()));
        initCommand(new DeleteEntry(model.getProjectModel(), getCommandManager()));
    }

    protected void initListeners() {
        final ProjectModel projectModel = model.getProjectModel();
        projectModel.addPropertyChangeListener(ProjectModel.PROPERTYNAME_ROOT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry root = (IFEntry) evt.getNewValue();
                final DefaultMutableTreeNode rootNode = wrapNode(root);
                final DefaultMutableTreeModel treeModel = new DefaultMutableTreeModel(rootNode);
                tree.setModel(treeModel);
            }
        });
        projectModel.addPropertyChangeListener(ProjectModel.PROPERTYNAME_NEWCHILD, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry child = (IFEntry) evt.getNewValue();
                final TreePath selectionPath = tree.getSelectionPath();
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
                final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                node.add(childNode);
                final int position = node.getIndex(childNode);
                final DefaultMutableTreeModel treeModel = (DefaultMutableTreeModel) tree.getModel();
                treeModel.nodesWereInserted(selectionPath, new int[]{position});
                tree.setSelectionPath(selectionPath.pathByAddingChild(childNode));
            }
        });
        projectModel.addPropertyChangeListener(ProjectModel.PROPERTYNAME_DELETEDCHILD, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final TreePath selectionPath = tree.getSelectionPath();
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
                final int position = node.getParent().getIndex(node);
                node.removeFromParent();
                final DefaultMutableTreeModel treeModel = (DefaultMutableTreeModel) tree.getModel();
                System.out.println("removing " + node + " at " + position);
                treeModel.nodesWereRemoved(selectionPath.getParentPath(), new int[]{position}, new Object[]{node});
                tree.setSelectionPath(selectionPath.getParentPath());
            }
        });
        projectModel.getCurrentBeanModel().getTriggerChannel().addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (Boolean.TRUE.equals((Boolean) evt.getNewValue())) {
                    final NotifiableTreeModel treeModel = (NotifiableTreeModel) tree.getModel();
                    treeModel.nodeChanged(tree.getSelectionPath());
                }
            }
        });
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                final TreePath path = e.getNewLeadSelectionPath();
                final IFEntry entry;
                final boolean invalid = path != null;
                if (invalid) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    entry = (IFEntry) node.getUserObject();
                }
                else {
                    entry = null;
                }
                projectModel.getCurrentBeanModel().setBean(entry);
                getCommandManager().getCommand(Commands.ADD_COMMAND).setEnabled(invalid);
                getCommandManager().getCommand(Commands.DELETE_COMMAND).setEnabled(invalid && entry.getParent() != null);
            }
        });
    }

    private JComponent createToolbar() {
        return getCommandManager().getGroup(Commands.POPUP_GROUP).createToolBar("toolbar");
    }

    private JComponent createTree() {
        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(model.getProjectModel().getRoot());
        final DnDValidatorUpdater treeValidator = new NopValidatorUpdater();
        tree = new DnDTree(new DefaultMutableTreeModel(rootNode), treeValidator);
        tree.setComponentPopupMenu(getCommandManager().getGroup(Commands.POPUP_GROUP).createPopupMenu());
        tree.setCellRenderer(new ProjectTreeCellRenderer());
        return new JScrollPane(tree);
    }

    private DefaultMutableTreeNode wrapNode(IFEntry entry) {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry);
        final IFEntry[] children = entry.getChildren();
        for (int i = 0; children != null && i < children.length; i++) {
            node.add(wrapNode(children[ i ]));
        }
        return node;
    }

    private static class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            final IFEntry entry = (IFEntry) node.getUserObject();
            final StringBuffer buffer = new StringBuffer();
            final Calendar start = entry.getStart();
            final Calendar end = entry.getEnd();
            if (!"".equals(entry.getName())) {
                buffer.append(entry.getName());
            }
            else if (start != null && end != null) {
                final SimpleDateFormat startFormat;
                if (DateUtils.isSameDay(start, end)) {
                    startFormat = new SimpleDateFormat("HH:mm");
                }
                else if (DateUtils.isSameMonth(start, end)) {
                    startFormat = new SimpleDateFormat("HH:mm dd.");
                }
                else if (DateUtils.isSameYear(start, end)) {
                    startFormat = new SimpleDateFormat("HH:mm dd.MM.");
                }
                else {
                    startFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                }
                buffer.append(startFormat.format(start.getTime()));
                buffer.append(" - ");
                final SimpleDateFormat endFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                buffer.append(endFormat.format(end.getTime()));
                buffer.append(" (");
                buffer.append(DateUtils.dateDifference(start.getTime().getTime(), end.getTime().getTime(), "HH:mm"));
                buffer.append(")");
                label.setIcon(ImageLocator.getIcon("people.gif"));
            }
            label.setText(buffer.toString());
            return label;
        }
    }

    private static class NopValidatorUpdater implements DnDValidatorUpdater {

        public boolean isRightShiftAllowed(TreePath path) {
            return false;
        }

        public boolean isLeftShiftAllowed(TreePath path) {
            return false;
        }

        public boolean isMoveAllowed(TreePath from, TreePath to, int index) {
            return false;
        }

        public boolean isAnyActionAllowed(TreePath from, TreePath to) {
            return false;
        }

        public void doRightShift(TreePath path) {
        }

        public void doLeftShift(TreePath path) {
        }

        public void setModel(NotifiableTreeModel model) {
        }
    }
}
