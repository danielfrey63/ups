/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main;

import com.thoughtworks.xstream.XStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import ch.jfactory.projecttime.domain.impl.Invoice;
import ch.jfactory.projecttime.domain.api.IFEntry;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class OpenCommand extends ActionCommand {

    private MainModel model;

    public OpenCommand(final CommandManager manager, final MainModel model) {
        super(manager, Commands.COMMANDID_OPEN);
        this.model = model;
    }

    protected void handleExecute() {
        try {
            final JFileChooser chooser = new JFileChooser();
            final int ret = chooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                final XStream xstream = Commands.getSerializer();
                final FileReader reader = new FileReader(chooser.getSelectedFile());
                final Object object = xstream.fromXML(reader);
                final Commands.Data data = (Commands.Data) object;
                fixData(data.root, null);
                model.getProjectModel().setRoot(data.root);
                if (data.entry2InvoiceMap != null) model.getProjectModel().setEntry2InvoiceMap(data.entry2InvoiceMap);
                final List invoices = model.getInvoiceModel().getInvoices();
                for (int i = 0; i < invoices.size(); i++) {
                    model.getInvoiceModel().deleteInvoice(i);
                }
                if (data.invoices != null) {
                    for (int i = 0; i < data.invoices.size(); i++) {
                        final Invoice invoice = (Invoice) data.invoices.get(i);
                        model.getInvoiceModel().addInvoice(invoice);
                    }
                }
                reader.close();
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void fixData(IFEntry entry, IFEntry parent) {
        entry.setParent(parent);
        final IFEntry[] children = entry.getChildren();
        for (int i = 0; children != null && i < children.length; i++) {
            final IFEntry child = children[i];
            fixData(child, entry);
        }
    }
}
