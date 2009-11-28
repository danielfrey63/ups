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

import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.uec.sets.SetBuilder;
import ch.xmatrix.ups.view.editor.PersonEditBuilder;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.uif.AbstractDialog;
import com.jgoodies.uifextras.panel.HeaderPanel;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.awt.BorderLayout;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Asks for new person data.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:19:10 $
 */
public class AddPerson extends ActionCommand
{
    private static final Logger LOG = Logger.getLogger( AddPerson.class );

    private final SetBuilder.SubmitTableModel model;

    private final AbstractDialog dialog;

    private final PersonData person;

    public AddPerson( final CommandManager commandManager, final SetBuilder.SubmitTableModel model )
    {
        super( commandManager, Commands.COMMANDID_ADDPERSION );
        this.model = model;
        person = new PersonData();
        dialog = new Dialog();
    }

    protected void handleExecute()
    {
        try
        {
            dialog.open();
            final Anmeldedaten anmeldedaten = new Anmeldedaten();
            anmeldedaten.setVorname( person.getFirstName() );
            anmeldedaten.setNachname( person.getLastName() );
            anmeldedaten.setStudentennummer( person.getId() );
            anmeldedaten.setStudiengang( person.getCourse() );
            final Registration registration = new Registration( anmeldedaten, null );
            model.add( registration );
            person.setFirstName( "" );
            person.setLastName( "" );
            person.setId( "" );
            person.setCourse( "" );
        }
        catch ( Exception e )
        {
            LOG.error( "error during action", e );
        }
    }

    private class Dialog extends AbstractDialog
    {
        private PersonEditBuilder builder;

        public Dialog()
        {
            super( (JFrame) null, "Neue Person", true );
        }

        protected JComponent buildHeader()
        {
            return new HeaderPanel( "Angaben zum Prüfling", "" +
                    "Geben Sie hier die Daten eines Prüflings\nohne Pflanzelisten ein." );
        }

        protected JComponent buildContent()
        {
            final JPanel panel = new JPanel( new BorderLayout() );
            builder = new PersonEditBuilder( person );
            panel.add( builder.getPanel(), BorderLayout.CENTER );
            panel.add( buildButtonBarWithOKCancel(), BorderLayout.SOUTH );
            builder.addCompletionListener( new PersonEditBuilder.CompletionListener()
            {
                public void updateComplete( final boolean complete )
                {
                    getOKAction().setEnabled( complete );
                }
            } );
            getOKAction().setEnabled( false );
            return panel;
        }

        public void doAccept()
        {
            super.doAccept();
            builder.save();
        }
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel( new PlasticXPLookAndFeel() );
        final AddPerson action = new AddPerson( new CommandManager(), null );
        action.dialog.setResourceBundle( ResourceBundle.getBundle( "ch.xmatrix.ups.uec.Strings" ) );
        action.handleExecute();
    }
}
