/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main;

import com.thoughtworks.xstream.XStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFileChooser;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class SaveCommand extends ActionCommand
{
    private final MainModel model;

    public SaveCommand( final CommandManager manager, final MainModel model )
    {
        super( manager, Commands.COMMANDID_SAVEAS );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            final JFileChooser chooser = new JFileChooser();
            final int ret = chooser.showSaveDialog( null );
            if ( ret == JFileChooser.APPROVE_OPTION )
            {
                final XStream xstream = Commands.getSerializer();
                final FileWriter writer = new FileWriter( chooser.getSelectedFile() );
                final Commands.Data data = new Commands.Data();
                data.root = model.getProjectModel().getRoot();
//                data.types = model.getTypeModel().getDataModel();
                data.invoices = model.getInvoiceModel().getInvoices();
                data.entry2InvoiceMap = (Map) model.getProjectModel().getEntry2InvoiceMap().getValue();
                xstream.toXML( data, writer );
                writer.close();
            }
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
    }
}
