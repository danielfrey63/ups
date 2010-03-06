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
import com.wegmueller.ups.ldap.LDAPAuthException;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.webservice.UPSServerClient2;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.xml.rpc.ServiceException;
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
        super( commandManager, Commands.COMMANDID_RELOADSERVERDATA );
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
            final Throwable cause1 = e.getCause();
            if ( cause1 != null )
            {
                final Throwable cause2 = e.getCause();
                if ( cause2 instanceof InvocationTargetException )
                {
                    final InvocationTargetException inner2 = (InvocationTargetException) cause2;
                    final Throwable cause3 = inner2.getTargetException();
                    if ( cause3 instanceof LDAPAuthException )
                    {
                        LOG.error( "unknown user or password: " + cause2.getMessage(), cause2 );
                        JOptionPane.showMessageDialog( null, "Name oder Passwort sind unbekannt", "Fehler", JOptionPane.ERROR_MESSAGE );
                    }
                    else if ( cause3 instanceof UPSServerException )
                    {
                        final UPSServerException inner3 = (UPSServerException) cause3;
                        if ( UPSServerException.MISSING_DATA.equals( inner3.getName() ) )
                        {
                            LOG.warn( "no data for \"" + sessionName + "\" and \"" + courseName + "\"", cause2 );
                            JOptionPane.showMessageDialog( null, "Keine Daten vorhanden", "Fehler", JOptionPane.ERROR_MESSAGE );
                        }
                        else
                        {
                            throw new IllegalStateException( e );
                        }
                    }
                    else
                    {
                        throw new IllegalStateException( e );
                    }
                }
                else
                {
                    throw new IllegalStateException( e );
                }
            }
            else
            {
                throw new IllegalStateException( e );
            }
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        catch ( RemoteException e )
        {
            final Throwable cause = e.getCause();
            if ( cause instanceof UnknownHostException )
            {
                LOG.error( "server not contactable", cause );
                JOptionPane.showMessageDialog( null, "Der Server konnte nicht erreicht werden", "Fehler", JOptionPane.ERROR_MESSAGE );
            }
            else
            {
                LOG.error( "unknown remote exception", e );
            }
        }
        catch ( ServiceException e )
        {
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
