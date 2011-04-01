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

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.AbstractPlainDocument;
import ch.jfactory.component.FixedFormatTextField;
import ch.jfactory.component.SimpleDocumentListener;
import ch.xmatrix.ups.domain.PersonData;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.DocumentAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:20 $
 */
public class PersonEditBuilder implements Builder
{
    private static final Logger LOG = LoggerFactory.getLogger( PersonEditBuilder.class );

    private final Trigger trigger = new Trigger();

    private final List<CompletionListener> listeners = new ArrayList<CompletionListener>();

    private final Notifier notifier = new Notifier();

    private boolean complete = false;

    private final PersonData person;

    private JPanel panel;

    private FixedFormatTextField idField;

    private JTextField firstNameField;

    private JTextField lastNameField;

    private JComboBox comboCourse;

    private String pattern;

    public PersonEditBuilder( final PersonData person )
    {
        this.person = person;
        build();
    }

    public JComponent getPanel()
    {
        return panel;
    }

    public void save()
    {
        trigger.triggerCommit();
    }

    private void build()
    {
        try
        {
            final FormCreator creator = new FormCreator( FormLoader.load( "ch/xmatrix/ups/view/editor/PersonEdit.jfd" ) );
            creator.createAll();
            panel = creator.getPanel( "panel" );

            firstNameField = creator.getTextField( "fieldFirstname" );
            final PropertyAdapter adapter0 = new PropertyAdapter( person, PersonData.FIRST_NAME );
            firstNameField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter0, trigger ), new FirstNameDocument() ) );
            firstNameField.getDocument().addDocumentListener( notifier );

            lastNameField = creator.getTextField( "fieldFamilyname" );
            final PropertyAdapter adapter1 = new PropertyAdapter( person, PersonData.LAST_NAME );
            lastNameField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter1, trigger ), new LastNameDocument() ) );
            lastNameField.getDocument().addDocumentListener( notifier );

            idField = (FixedFormatTextField) creator.getTextField( "fieldId" );
            final PropertyAdapter adapter2 = new PropertyAdapter( person, PersonData.ID );
            idField.setDocument( new DocumentAdapter( new BufferedValueModel( adapter2, trigger ), idField.getDocument() ) );
            idField.getDocument().addDocumentListener( notifier );

            comboCourse = creator.getComboBox( "comboCourse" );
            final PropertyAdapter adapter = new PropertyAdapter( person, PersonData.COURSE );
            final ArrayList<String> courseList = new ArrayList<String>();
            final ListModel list = comboCourse.getModel();
            for ( int i = 0; i < list.getSize(); i++ )
            {
                courseList.add( (String) list.getElementAt( i ) );
            }
            comboCourse.setModel( new ComboBoxAdapter( (ListModel) new SelectionInList( courseList ), new BufferedValueModel( adapter, trigger ) ) );
            comboCourse.addActionListener( new ActionListener()
            {
                public void actionPerformed( final ActionEvent e )
                {
                    notifyComplete();
                }
            } );

            pattern = idField.getPattern();
        }
        catch ( Exception e )
        {
            LOG.error( "could not load form", e );
        }
    }

    public boolean isComplete()
    {
        return !firstNameField.getText().equals( "" ) &&
                !lastNameField.getText().equals( "" ) &&
                !comboCourse.getSelectedItem().equals( "" ) &&
                !idField.getText().equals( pattern );
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
            for ( final CompletionListener listener : listeners )
            {
                listener.updateComplete( complete );
            }
        }
    }

    private class Notifier extends SimpleDocumentListener
    {
        public void changedUpdate( final DocumentEvent e )
        {
            notifyComplete();
        }
    }
}
