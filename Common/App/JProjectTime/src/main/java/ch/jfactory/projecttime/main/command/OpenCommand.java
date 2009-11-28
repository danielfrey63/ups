/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main.command;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.main.MainModel;
import com.thoughtworks.xstream.XStream;
import java.io.FileReader;
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
public class OpenCommand extends ActionCommand
{
    private final MainModel model;

    public OpenCommand( final MainModel model, final CommandManager manager )
    {
        super( manager, Commands.FILE_OPEN );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            final JFileChooser chooser = new JFileChooser();
            final int ret = chooser.showOpenDialog( null );
            if ( ret == JFileChooser.APPROVE_OPTION )
            {
                final XStream xstream = Commands.getSerializer();
                final FileReader reader = new FileReader( chooser.getSelectedFile() );
                final IFEntry root = (IFEntry) xstream.fromXML( reader );
                model.getProjectModel().setRoot( root );
                reader.close();
            }
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
    }
}
