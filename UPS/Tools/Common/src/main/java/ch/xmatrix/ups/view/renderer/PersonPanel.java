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
package ch.xmatrix.ups.view.renderer;

import ch.xmatrix.ups.domain.PersonData;
import com.jgoodies.binding.beans.BeanUtils;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class PersonPanel extends JPanel
{
    private ch.xmatrix.ups.domain.PersonData personData;

    private final Object[][] fieldData = {
            {"Vorname:", new JLabel()},
            {"Nachname:", new JLabel()},
            {"Immatrikulationsnummer:", new JLabel()},
            {"Studiengang:", new JLabel()}};

    private final Object[][] listenerData = {
            {PersonData.FIRST_NAME, new LabelPropertyChangeListener( (JLabel) fieldData[0][1] )},
            {PersonData.LAST_NAME, new LabelPropertyChangeListener( (JLabel) fieldData[1][1] )},
            {PersonData.ID, new LabelPropertyChangeListener( (JLabel) fieldData[2][1] )},
            {PersonData.COURSE, new LabelPropertyChangeListener( (JLabel) fieldData[3][1] )}};

    public PersonPanel( final PersonData person )
    {
        this.personData = person;
        initListeners();
        build();
    }

    public void setPersonData( final PersonData person )
    {
        removeListeners();
        this.personData = person;
        initListeners();
    }

    private void build()
    {
        final FormLayout layout = new FormLayout( "left:pref, 3dlu, right:pref, pref:grow" );

        final DefaultFormBuilder builder = new DefaultFormBuilder( layout, this );
        builder.setDefaultDialogBorder();

        builder.appendSeparator( "Persönliche Daten" );
        for ( final Object[] fieldDatum : fieldData )
        {
            builder.append( (String) fieldDatum[0], (JLabel) fieldDatum[1] );
        }
    }

    private void removeListeners()
    {
        for ( final Object[] listenerDatum : listenerData )
        {
            personData.removePropertyChangeListener( (PropertyChangeListener) listenerDatum[1] );
        }
    }

    private void initListeners()
    {
        for ( int i = 0; i < listenerData.length; i++ )
        {
            final Object[] listenerDatum = listenerData[i];
            final String propertyName = (String) listenerDatum[0];
            final PropertyChangeListener listener = (PropertyChangeListener) listenerDatum[1];
            personData.addPropertyChangeListener( propertyName, listener );
            try
            {
                final JLabel field = (JLabel) fieldData[i][1];
                final PropertyDescriptor descriptor = new PropertyDescriptor( propertyName, PersonData.class );
                field.setText( (String) BeanUtils.getValue( personData, descriptor ) );
            }
            catch ( IntrospectionException e )
            {
                e.printStackTrace();
            }
        }
    }

    private static class LabelPropertyChangeListener implements PropertyChangeListener
    {
        private final JLabel label;

        private LabelPropertyChangeListener( final JLabel label )
        {
            this.label = label;
        }

        public void propertyChange( final PropertyChangeEvent evt )
        {
            label.setText( (String) evt.getNewValue() );
        }
    }
}
