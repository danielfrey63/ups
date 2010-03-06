package ch.jfactory.component;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.resource.Strings;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A panel containing a label in boldface on the first line and an edit field displaying the current status on the
 * second.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class TextEditItem extends JPanel
{
    private JTextField field;

    /**
     * Build a panel containing a title in boldface on the first line and a edit field displaying the current status as
     * well as a setup button on the second.<p> The key to retrieve the strings are composed as follows, where
     * <code>PREFIX</code> indicates the prefix given as an argument: <ul> <li>Label:
     * <code>PREFIX.TITLE.TEXT</code></li> <li>Button: <code>PREFIX.BUTTON</code></li> </ul> See documentation of {@link
     * ComponentFactory#createButton(String, ActionListener)} to investigate what
     * extenstions are used to complete the keys of the button.
     *
     * @param prefix   the prefix string to use to access the different strings used to setup
     * @param listener the ActionListener to use for the button
     */
    public TextEditItem( final String prefix, final ActionListener listener, final FocusListener focus )
    {
        this( prefix );
        field.addFocusListener( focus );
        field.addActionListener( listener );
    }

    /**
     * Build a panel containing a title in boldface on the first line and a edit field displaying the current status on
     * the second.<p> The key to retrieve the strings are composed as follows, where <code>PREFIX</code> indicates the
     * prefix given as an argument: <ul> <li>Label: <code>PREFIX.TITLE.TEXT</code></li> </ul> See documentation of
     * {@link ComponentFactory#createButton(String, ActionListener)} to investigate
     * what extenstions are used to complete the keys of the button.
     *
     * @param prefix the prefix string to use to access the different strings used to setup
     */
    public TextEditItem( final String prefix )
    {
        final JLabel title = new JLabel( Strings.getString( prefix + ".TITLE.TEXT" ) );
        title.setFont( title.getFont().deriveFont( Font.BOLD ) );

        field = new DefaultTextField();

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
    public void setText( final Object userObject )
    {
        field.setText( userObject.toString() );
    }

    public String getText()
    {
        return field.getText();
    }

    public JTextField getTextField()
    {
        return field;
    }
}
