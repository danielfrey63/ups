/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.resource.Strings;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A panel containing a label in boldface on the first line, an edit field displaying the current status and a button to invoke an edit action on the second. The <code>userObject</code> is represented as string in the text field by invoking its <code>toString()</code> method. The <code>ActionListener</code> passed during construction is responsible to
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class EditItem extends JPanel
{
    private JTextField field;

    private Object userObject;

    private JButton button;

    /**
     * Build a panel containing a title in boldface on the first line and a edit field displaying the current status as well as a setup button on the second.<p> The key to retrieve the strings are composed as follows, where <code>PREFIX</code> indicates the prefix given as an argument: <ul> <li>Label: <code>PREFIX.TITLE.TEXT</code></li> <li>Button: <code>PREFIX.BUTTON</code></li> </ul> See documentation of {@link ComponentFactory#createButton(String, ActionListener)} to investigate what extenstions are used to complete the keys of the button.
     *
     * @param prefix   the prefix string to use to access the different strings used to setup
     * @param listener the ActionListener to use for the button
     */
    public EditItem( final String prefix, final ActionListener listener )
    {
        this( prefix );

        button = ComponentFactory.createButton( prefix + ".BUTTON", listener );
        final Icon icon = button.getIcon();
        button.setPreferredSize( new Dimension( icon.getIconWidth(), icon.getIconHeight() ) );

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets( 0, Constants.GAP_WITHIN_TOGGLES, 0, 0 );
        add( button, gbc );
    }

    /**
     * Build a panel containing a title in boldface on the first line and a edit field displaying the current status on the second.<p> The key to retrieve the strings are composed as follows, where <code>PREFIX</code> indicates the prefix given as an argument: <ul> <li>Label: <code>PREFIX.TITLE.TEXT</code></li> </ul> See documentation of {@link ComponentFactory#createButton(String, ActionListener)} to investigate what extenstions are used to complete the keys of the button.
     *
     * @param prefix the prefix string to use to access the different strings used to setup
     */
    public EditItem( final String prefix )
    {
        final JLabel title = new JLabel( Strings.getString( prefix + ".TITLE.TEXT" ) );
        title.setFont( title.getFont().deriveFont( Font.BOLD ) );

        field = new DefaultTextField();
        field.setEditable( false );
        field.setFocusable( false );

        setLayout( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 2;
        gbc.insets = new Insets( Constants.GAP_BETWEEN_GROUP, 0, Constants.GAP_BETWEEN_GROUP, 0 );
        add( title, gbc );
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets( 0, 0, 0, 0 );
        add( field, gbc );
    }

    /**
     * Sets the object behind this item and displays its toString value.
     *
     * @param userObject
     */
    public void setUserObject( final Object userObject )
    {
        this.userObject = userObject;
        field.setText( userObject.toString() );
    }

    public Object getUserObject()
    {
        return userObject;
    }

    /**
     * Dis-/enabled the button in this EditItem.
     *
     * @param enabled whether to enable
     */
    public void setEnabled( final boolean enabled )
    {
        if ( button != null )
        {
            button.setEnabled( enabled );
        }
    }

    /**
     * Makes the field (not) editable.
     *
     * @param editable whether editing should be allowed
     */
    public void setEditable( final boolean editable )
    {
        field.setEditable( editable );
        field.setFocusable( true );
    }

    public JTextField getTextField()
    {
        return field;
    }
}
