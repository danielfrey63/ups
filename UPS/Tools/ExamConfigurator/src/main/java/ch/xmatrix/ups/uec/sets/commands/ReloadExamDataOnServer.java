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

import ch.jfactory.component.Dialogs;
import ch.jfactory.component.WaitOverlay;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.webservice.UPSServerClient2;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the registrations and exam-lists from the ETH OIS into the UPS Server.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class ReloadExamDataOnServer extends ActionCommand
{
    private static final Logger LOG = LoggerFactory.getLogger( ReloadExamDataOnServer.class );

    private static final String RESOURCE_FORM = "ch/xmatrix/ups/uec/sets/commands/ReloadServerData.jfd";

    private transient String pass;

    private transient String user;

    public ReloadExamDataOnServer( final CommandManager commandManager )
    {
        super( commandManager, Commands.COMMAND_ID_RELOAD_SERVER_DATA );
    }

    protected void handleExecute()
    {
        try
        {
            final FormCreator creator = new FormCreator( FormLoader.load( RESOURCE_FORM ) );
            creator.createAll();
            final JDialog dialog = creator.createDialog( null );
            final JButton okButton = creator.getButton( "okButton" );
            final JButton cancelButton = creator.getButton( "cancelButton" );
            dialog.getRootPane().setDefaultButton( okButton );
            okButton.addActionListener( new ActionListener()
            {
                public void actionPerformed( final ActionEvent event )
                {
                    try
                    {
                        final Thread thread = new Thread()
                        {
                            public void run()
                            {
                                doRequest();
                            }
                        };
                        final WaitOverlay waitOverlay = new WaitOverlay( dialog, Color.white );
                        waitOverlay.setTickers(
                                waitOverlay.new FadeInTicker( 0.2f, 500 ),
                                waitOverlay.new WaitForThreadTicker( dialog.getRootPane(), thread ),
                                waitOverlay.new FadeOutTicker( 300 ),
                                waitOverlay.new DisposeDialogTicker( dialog ) );
                        waitOverlay.start();
                        user = creator.getTextField( "userField" ).getText();
                        pass = creator.getTextField( "passField" ).getText();
                        thread.start();
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            } );
            cancelButton.addActionListener( new ActionListener()
            {
                public void actionPerformed( final ActionEvent e )
                {
                    dialog.dispose();
                }
            } );
            dialog.pack();
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Ein Fehler ist aufgetreten:\n" + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE );
        }
    }

    private void doRequest()
    {
        final String sessionName = null;
        final String courseName = null;
        try
        {
            final UPSServerClient2 ws = new UPSServerClient2();
            final IPruefungsSession reloadedSession = (IPruefungsSession) ws.invoke( "reloadPruefungsDaten", user, pass );
            if ( reloadedSession != null )
            {
                final String name = reloadedSession.getSessionsname();
                LOG.info( "reload of OIS data successfull for session \"" + name + "\"" );
                Dialogs.showInfoMessage( null, "Prüfungssessions-Daten für\n\"" + name +
                        "\"\nerfolgreich vom OIS auf den UPS Server geladen.", "Erfolgreich" );
            }
            else
            {
                LOG.error( "refresh of exam data not successfull" );
                Dialogs.showErrorMessage( null, "Es konnten keine Prüfungssessions-Daten geladen werden. " +
                        "Wurden diese im OIS schon freigegeben?", "Fehler" );
            }
        }
        catch ( UPSServerException e )
        {
            LOG.error( e.getLogMessage(), e.getLogThrowable() );
            JOptionPane.showMessageDialog( null, e.getFeedbackMessage(), "Fehler", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception e )
        {
            LOG.error( "unknown error", e );
            JOptionPane.showMessageDialog( null, "Unbekannter Fehler", "Fehler", JOptionPane.ERROR_MESSAGE );
        }
    }
}
