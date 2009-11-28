/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.xmatrix.ups.view.editor;

import ch.jfactory.component.AbstractPlainDocument;
import ch.jfactory.component.FixedFormatTextField;
import ch.xmatrix.ups.domain.PersonData;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.DocumentAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class PersonEditPanel extends JPanel
{
    private final PersonData person;

    private final Trigger trigger = new Trigger();

    private JTextField idField;

    private JTextField firstNameField;

    private JTextField lastNameField;

    private JComboBox courseField;

    private final List listeners = new ArrayList();

    private boolean complete = false;

    private static final String PATTERN = "NN-NNN-NNN";

    private final Notifier notifier = new Notifier();

    public PersonEditPanel( final PersonData person )
    {
        this.person = person;
        build();
    }

    public void save()
    {
        trigger.triggerCommit();
    }

    private void build()
    {
        final FormLayout layout = new FormLayout( "left:pref, 3dlu, pref:grow" );

        final DefaultFormBuilder builder = new DefaultFormBuilder( layout, this );
        builder.setDefaultDialogBorder();

        builder.append( "Vorname:", createFirstNameField() );
        builder.append( "Nachname:", createLastNameField() );
        builder.append( "Immatrikulationsnummer:", createIdField() );
        builder.append( "Studiengang:", createCourseField() );
    }

    private JTextField createFirstNameField()
    {
        firstNameField = new JTextField();
        final PropertyAdapter adapter = new PropertyAdapter( person, PersonData.FIRST_NAME );
        firstNameField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter, trigger ), new FirstNameDocument() ) );
        firstNameField.getDocument().addDocumentListener( notifier );
        return firstNameField;
    }

    private JTextField createLastNameField()
    {
        lastNameField = new JTextField();
        final PropertyAdapter adapter = new PropertyAdapter( person, PersonData.LAST_NAME );
        lastNameField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter, trigger ), new LastNameDocument() ) );
        lastNameField.getDocument().addDocumentListener( notifier );
        return lastNameField;
    }

    private JTextField createIdField()
    {
        idField = new FixedFormatTextField( PATTERN, "00-000-000" );
        final PropertyAdapter adapter = new PropertyAdapter( person, PersonData.ID );
        idField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter, trigger ), idField.getDocument() ) );
        idField.getDocument().addDocumentListener( notifier );
        return idField;
    }

    private JComboBox createCourseField()
    {
        final PropertyAdapter adapter = new PropertyAdapter( person, PersonData.COURSE );
        courseField = new JComboBox();
        final List courseList = new ArrayList( 4 );
        courseList.add( "" );
        courseList.add( "Biologie" );
        courseList.add( "Pharmazie" );
        courseList.add( "Andere" );
        courseField.setModel( new ComboBoxAdapter( (ListModel) new SelectionInList( courseList ), new BufferedValueModel( adapter, trigger ) ) );
        courseField.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                notifyComplete();
            }
        } );
        return courseField;
    }

    public boolean isComplete()
    {
        return !firstNameField.getText().equals( "" ) &&
                !lastNameField.getText().equals( "" ) &&
                !courseField.getSelectedItem().equals( "" ) &&
                !idField.getText().equals( PATTERN );
    }

    private class FirstNameDocument extends AbstractPlainDocument
    {
        protected boolean validate( final String newValue )
        {
            boolean ret = true;
            for ( int i = 0; i < newValue.length(); i++ )
            {
                final char c = newValue.charAt( i );
                ret &= Character.isLetter( c ) || c == ' ' || c == '-';
            }
            notifyComplete();
            return ret;
        }
    }

    private class LastNameDocument extends AbstractPlainDocument
    {
        protected boolean validate( final String newValue )
        {
            boolean ret = true;
            for ( int i = 0; i < newValue.length(); i++ )
            {
                final char c = newValue.charAt( i );
                ret &= Character.isLetter( c ) || c == ' ' || c == '-' || c == '(' || c == ')';
            }
            return ret;
        }
    }

    public interface CompletionListener
    {
        void updateComplete( boolean complete );
    }

    public void addCompletionListener( final CompletionListener listener )
    {
        listeners.add( listener );
    }

    private void notifyComplete()
    {
        final boolean oldComplete = complete;
        complete = isComplete();
        if ( oldComplete != complete )
        {
            for ( final Object listener1 : listeners )
            {
                final CompletionListener listener = (CompletionListener) listener1;
                listener.updateComplete( complete );
            }
        }
    }

    private class Notifier implements DocumentListener
    {
        public void changedUpdate( final DocumentEvent e )
        {
            notifyComplete();
        }

        public void insertUpdate( final DocumentEvent e )
        {
            notifyComplete();
        }

        public void removeUpdate( final DocumentEvent e )
        {
            notifyComplete();
        }
    }
}
