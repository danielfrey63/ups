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
package ch.jfactory.projecttime.main;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import ch.jfactory.projecttime.invoice.InvoiceModel;
import ch.jfactory.projecttime.project.ProjectModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class MapInvoiceCommand extends ActionCommand {

    private MainModel model;

    public MapInvoiceCommand(final String id, final CommandManager commandManager, final MainModel model) {
        super(commandManager, id);
        this.model = model;
    }

    protected void handleExecute() {
        final InvoiceModel invoiceModel = model.getInvoiceModel();
        final ProjectModel projectModel = model.getProjectModel();
        if (Commands.COMMANDID_ADDINVOICE == getId()) {
            final TreeSelectionModel selectionModel = (TreeSelectionModel) projectModel.getSelectionModel().getValue();
            final TreePath[] paths = selectionModel.getSelectionPaths();
            for (int i = 0; paths != null && i < paths.length; i++) {
                final TreePath path = paths[i];
                final IFEntry entry = (IFEntry) path.getLastPathComponent();
                final Invoice invoice = invoiceModel.getCurrentInvoice();
                projectModel.addInvoice(entry, invoice);
            }
        }
        else if (Commands.COMMANDID_REMOVEINVOICE == getId()) {
            final TreeSelectionModel selectionModel = invoiceModel.getTreeSelectionModel();
            final TreePath[] paths = selectionModel.getSelectionPaths();
            for (int j = 0; j < paths.length; j++) {
                final TreePath path = paths[j];
                final Object node = path.getLastPathComponent();
                if (node instanceof IFEntry) {
                    final IFEntry entry = (IFEntry) node;
                    if (entry.getEnd() != null) {
                        projectModel.removeInvoice(entry);
                    }
                }
            }
        }
    }
}
