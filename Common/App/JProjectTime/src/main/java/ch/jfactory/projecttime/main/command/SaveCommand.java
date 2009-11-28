/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main.command;

import ch.jfactory.projecttime.main.MainModel;
import com.thoughtworks.xstream.XStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class SaveCommand extends ActionCommand
{
    private final MainModel model;

    public SaveCommand( final MainModel model, final CommandManager manager )
    {
        super( manager, Commands.FILE_SAVEAS );
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
                xstream.toXML( model.getProjectModel().getRoot(), writer );
                writer.close();
            }
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
    }
}
