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
package ch.jfactory.projecttime.invoice;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.image.SimpleIconFactory;
import ch.jfactory.component.table.TableUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import ch.jfactory.projecttime.main.ProjectTreeCellRenderer;
import com.jgoodies.binding.value.ValueModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2006/11/16 13:25:17 $
 */
public class InvoiceBuilder extends ActionCommandPanelBuilder {

    private InvoiceModel model;
    private JXTreeTable table;

    public InvoiceBuilder(final InvoiceModel model) {
        this.model = model;
    }

    protected void initCommands() {
        initCommand(new AddEntry(getCommandManager(), model), true);
        initCommand(new DeleteEntry(getCommandManager(), model), false);
    }

    protected void initModelListeners() {
        model.addPropertyChangeListener(InvoiceModel.PROPERTYNAME_INVOICESMODIFIED, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                table.updateUI();
            }
        });
        model.getTableSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                getCommand(Commands.COMMANDID_DELETE).setEnabled(model.getTableSelectionModel().getMinSelectionIndex() > -1);
            }
        });
    }

    public JComponent createMainPanel() {

        final TreeTableModel treeTableModel = model.getTreeTableModel();
        table = new JXTreeTable(treeTableModel);
        table.setDefaultRenderer(Calendar.class, new TableUtils.CalendarCellRenderer("dd.MM.yyyy HH:mm"));
        table.setDefaultEditor(String.class, new TableUtils.StringEditor());
        table.setDefaultEditor(Calendar.class, new TableUtils.CalendarEditor());
        table.setSelectionModel(model.getTableSelectionModel());

        final JTree tree = (JTree) table.getDefaultRenderer(AbstractTreeTableModel.hierarchicalColumnClass);
        tree.setCellRenderer(new MixedProjectInvoiceRenderer(model.getEntry2InvoiceMap()));
        tree.setSelectionModel(model.getTreeSelectionModel());

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(getCommandManager().getGroup(Commands.GROUPID_TOOLBAR).createToolBar(), BorderLayout.NORTH);

        return panel;
    }

    protected void initComponentListeners() {
        model.getTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getNewLeadSelectionPath();
                Invoice invoice = null;
                while (path != null) {
                    Object last = path.getLastPathComponent();
                    if (last instanceof Invoice) {
                        invoice = (Invoice) last;
                        break;
                    }
                    else {
                        path = path.getParentPath();
                    }
                }
                model.setCurrentInvoice(invoice);
            }
        });
    }

    public void updateUI() {
        model.resetTreeTableModel();
        table.updateUI();
    }

    private class MixedProjectInvoiceRenderer extends ProjectTreeCellRenderer {

        private JLabel label = new JLabel();
        private Icon invoiceIcon;

        public MixedProjectInvoiceRenderer(ValueModel entry2InvoiceMap) {
            super(entry2InvoiceMap);
            invoiceIcon = new SimpleIconFactory("/16x16/fill").createDropShadowIcon("file_line2.png", "#0000FF");
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            final JLabel current;
            if (value instanceof String) {
                label.setText((String) value);
                current = label;
            }
            else if (value instanceof Invoice) {
                final Invoice invoice = (Invoice) value;
                label.setText(invoice.getNumber());
                label.setIcon(invoiceIcon);
                current = label;
            }
            else if (value instanceof IFEntry) {
                current = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }
            else {
                current = label;
            }
            return current;
        }
    }
}
