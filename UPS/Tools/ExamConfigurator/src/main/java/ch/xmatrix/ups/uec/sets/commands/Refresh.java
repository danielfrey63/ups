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
package ch.xmatrix.ups.uec.sets.commands;

import ch.xmatrix.ups.uec.sets.SetBuilder;
import com.wegmueller.ups.webservice.UPSServerClient2;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Reloads the exam data the UPS server.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:16 $
 */
public class Refresh extends ActionCommand
{

    private SetBuilder.SubmitTableModel submitModel;

    private transient String session;

    private transient String pass;

    private transient String user;

    private transient ProgressMonitor progress;

    private transient String course;

    public Refresh(final CommandManager commandManager, final SetBuilder.SubmitTableModel submitModel)
    {
        super(commandManager, Commands.COMMANDID_REFRESH);
        this.submitModel = submitModel;
    }

    protected void handleExecute()
    {
        try
        {
            final UPSServerClient2 ws = new UPSServerClient2();
            ws.reloadPruefungsDaten(user, pass);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten:\n" + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
