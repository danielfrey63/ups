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
package ch.jfactory.projecttime.project;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.component.tree.TreeUtils;
import ch.jfactory.component.tree.dnd.DnDTree;
import ch.jfactory.component.tree.dnd.DnDValidatorUpdater;
import ch.jfactory.component.tree.dnd.MovingDnDValidatorUpdater;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.main.ProjectTreeCellRenderer;
import com.jgoodies.binding.PresentationModel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * The builder for the project panel which has a tree and some toolbar. The tree displays the current projects
 * structure.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class ProjectBuilder extends ActionCommandPanelBuilder {

    private JTree tree;
    private ProjectModel model;

    public ProjectBuilder(final ProjectModel model) {
        this.model = model;
    }

    protected JComponent createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(createToolbar(), BorderLayout.NORTH);
        panel.add(createTree(), BorderLayout.CENTER);
        return panel;
    }

    protected void initCommands() {
        initCommand(new AddEntry(getCommandManager(), model));
        initCommand(new DeleteEntry(getCommandManager(), model));
        initCommand(new StartNewEntry(getCommandManager(), model));
        initCommand(new StopNewEntry(getCommandManager(), model));
    }

    protected void initModelListeners() {
        // Change the tree model in the project model
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_ROOT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry root = (IFEntry) evt.getNewValue();
                final ProjectTreeModel treeModel = new ProjectTreeModel(root);
                model.setTreeModel(treeModel);
            }
        });
        // React upon a change of the tree model in the project model and asign it to the tree.
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_TREEMODEL, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                tree.setModel((TreeModel) evt.getNewValue());
            }
        });
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_NEWCHILD, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final TreeSelectionModel selectionModel = (TreeSelectionModel) model.getSelectionModel().getValue();
                final TreePath selection = TreeUtils.findPathInTreeModel(model.getTreeModel(), evt.getNewValue());
                selectionModel.setSelectionPath(selection);
                TreeUtils.ensureVisibility(tree, selection);
            }
        });
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_DELETEDCHILD, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry child = (IFEntry) evt.getNewValue();
                final IFEntry parent = child.getParent();
                final TreePath path = TreeUtils.findPathInTreeModel(model.getTreeModel(), parent);
                final TreeSelectionModel selectionModel = (TreeSelectionModel) model.getSelectionModel().getValue();
                selectionModel.setSelectionPath(path);
            }
        });
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_RUNNING, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final boolean runningEntry = (evt.getNewValue() != null);
                getCommandManager().getCommand(Commands.COMMANDIT_STOP).setEnabled(runningEntry);
                final boolean selected = model.getCurrentBeanModel().getBean() != null;
                getCommandManager().getCommand(Commands.COMMANDID_START).setEnabled(!runningEntry && selected);
                if (!runningEntry) {
                    final IFEntry entry = (IFEntry) evt.getOldValue();
                    entry.setEnd(Calendar.getInstance());
                    model.getCurrentBeanModel().setBean(entry);
                }
            }
        });
        // Make sure that nodes sizes are adapted when a new timeentry has been made
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_RUNNING, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry entry = (IFEntry) (evt.getNewValue() == null ? evt.getOldValue() : evt.getNewValue());
                final ProjectTreeModel treeModel = (ProjectTreeModel) tree.getModel();
                treeModel.nodeChanged(entry);
            }
        });
        // Make sure that nodes sizes are adapted in size when the editor has been commited.
        model.getCurrentBeanModel().getTriggerChannel().addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (Boolean.TRUE.equals((Boolean) evt.getNewValue())) {
                    final ProjectTreeModel treeModel = (ProjectTreeModel) tree.getModel();
                    treeModel.nodeChanged(tree.getSelectionPath());
                }
            }
        });
        model.getCurrentBeanModel().addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final IFEntry entry = (IFEntry) (evt.getNewValue() == null ? evt.getOldValue() : evt.getNewValue());
                final ProjectTreeModel treeModel = (ProjectTreeModel) tree.getModel();
                treeModel.nodeChanged(entry);
            }
        });
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_INVOICEADDED, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                tree.repaint();
            }
        });
        model.addPropertyChangeListener(ProjectModel.PROPERTYNAME_INVOICEREMOVED, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                tree.repaint();
            }
        });
    }

    protected void initComponentListeners() {
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                final TreePath path = e.getNewLeadSelectionPath();
                final IFEntry entry;
                final boolean selected = path != null;
                if (selected) {
                    entry = (IFEntry) path.getLastPathComponent();
                }
                else {
                    entry = null;
                }
                model.getCurrentBeanModel().setBean(entry);
                getCommandManager().getCommand(Commands.COMMANDID_ADD).setEnabled(selected);

                final boolean notroot = selected && entry.getParent() != null;
                getCommandManager().getCommand(Commands.COMMANDID_DELETE).setEnabled(notroot);

                final boolean running = (model.getRunning() != null);
                getCommandManager().getCommand(Commands.COMMANDID_START).setEnabled(selected && !running);
            }
        });
    }

    private JComponent createToolbar() {
        return getCommandManager().getGroup(Commands.GROUPID_POPUP).createToolBar("toolbar");
    }

    private JComponent createTree() {
        final ProjectTreeModel treeModel = model.getTreeModel();
        final DnDValidatorUpdater treeValidator = new MovingDnDValidatorUpdater(treeModel);
        tree = new DnDTree(treeModel, treeValidator);
        tree.setComponentPopupMenu(getCommandManager().getGroup(Commands.GROUPID_POPUP).createPopupMenu());
        tree.setCellRenderer(new ProjectTreeCellRenderer(model.getEntry2InvoiceMap()));
        tree.setSelectionModel((TreeSelectionModel) model.getSelectionModel().getValue());
        return new JScrollPane(tree);
    }
}
