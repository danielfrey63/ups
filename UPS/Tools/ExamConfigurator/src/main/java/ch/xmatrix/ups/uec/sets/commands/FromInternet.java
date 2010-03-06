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

import ch.jfactory.component.WaitOverlay;
import ch.jfactory.convert.Converter;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.CourseModel;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.ldap.LDAPAuthException;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.webservice.UPSServerClient2;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.rpc.ServiceException;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the registrations and exam-lists from the UPS server.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/23 22:19:10 $
 */
public class FromInternet extends ActionCommand
{
    private static final Logger LOG = LoggerFactory.getLogger( FromInternet.class );

    private static final String RESOURCE_FORM = "ch/xmatrix/ups/uec/sets/commands/FromInternet.jfd";

    private final SetBuilder.SubmitTableModel submitModel;

    private transient SessionModel session;

    private transient String pass;

    private transient String user;

    private transient CourseModel course;

    private transient boolean goon = true;

    public FromInternet( final CommandManager commandManager, final SetBuilder.SubmitTableModel submitModel )
    {
        super( commandManager, Commands.COMMANDID_OPENFROMINTERNET );
        this.submitModel = submitModel;
    }

    protected void handleExecute()
    {
        try
        {
            final FormCreator creator = new FormCreator( FormLoader.load( RESOURCE_FORM ) );
            creator.createAll();
            final JDialog dialog = creator.createDialog( null );
            final JList courseList = creator.getList( "courseList" );
            final JList sessionList = creator.getList( "sessionList" );
            final JButton okButton = creator.getButton( "okButton" );
            final JButton cancelButton = creator.getButton( "cancelButton" );
            dialog.getRootPane().setDefaultButton( okButton );
            courseList.setModel( MainModel.findModelById( MainModel.MODELID_COURSEINFO ) );
            sessionList.setModel( MainModel.findModelById( MainModel.MODELID_SESSION ) );
            okButton.addActionListener( new ActionListener()
            {
                public void actionPerformed( final ActionEvent event )
                {
                    try
                    {
                        goon = true;
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
                        course = (CourseModel) courseList.getSelectedValue();
                        session = (SessionModel) sessionList.getSelectedValue();
                        user = creator.getTextField( "userField" ).getText();
                        pass = creator.getTextField( "passField" ).getText();
                        dialog.addWindowListener( new WindowAdapter()
                        {
                            public void windowClosing( final WindowEvent e )
                            {
                                goon = false;
                            }
                        } );
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
        String sessionName = null;
        String courseName = null;
        try
        {
            final UPSServerClient2 ws = new UPSServerClient2();

            sessionName = session.getSeskz();
            courseName = course.getUid();

            final IAnmeldedaten[] anmeldedaten = (IAnmeldedaten[]) ws.invoke( "getAnmeldungen", user, pass, sessionName, courseName, Calendar.class );
            if ( anmeldedaten != null )
            {
                final int length = anmeldedaten.length;
                for ( int i = 0; i < length && goon; i++ )
                {
                    final IAnmeldedaten anmeldedatum = anmeldedaten[i];
                    final String lk = anmeldedatum.getLkNummer();
                    final String id1 = anmeldedatum.getStudentennummer();
                    final byte[] bytes = ws.getPruefungsListe( user, pass, session.getSeskz(), lk, id1 );
                    final PlantList list;
                    if ( bytes != null )
                    {
                        final Converter<ArrayList<String>> converter = Commands.getConverter1();
                        final ArrayList<String> taxa = converter.from( new StringReader( new String( bytes ) ) );
                        list = new PlantList();
                        list.setTaxa( taxa );
                    }
                    else
                    {
                        list = null;
                    }
                    submitModel.add( new Registration( anmeldedatum, list ) );
                }
            }
            else
            {
                LOG.warn( "no anmeldedaten in server database" );
                JOptionPane.showMessageDialog( null, "Keine Anmeldedaten vorhanden. Versuchen Sie die Serverdaten mit dem OIS abzugleichen", "Fehler", JOptionPane.WARNING_MESSAGE );
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

