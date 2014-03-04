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

import ch.jfactory.action.ActionUtils;
import ch.jfactory.action.KeyBindings;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 * Factory class to create different components. All factory methods are based on parametrized properties. The properties are loaded with the {@link Strings} class.<p> $Author: daniel_frey $ $Revision: 1.1 $
 */
public class ComponentFactory
{
    /**
     * Creates a button with name, icon and tooltip by extracting the strings/icon name from a resource. If an object not null is given as a discriminator, the resource associated with that object is used, otherwise the global resource is used. See {@link Strings#getString(Object, String)}.<p> To compose the key for the strings resource, the following conventions are used, where <code>PREFIX</code> indicates the prefix given as an argument:
     * <p/>
     * <ul>
     * <p/>
     * <li>Text: <code>PREFIX.TEXT</code></li>
     * <p/>
     * <li>Icon name: <code>PREFIX.ICON</code></li>
     * <p/>
     * <li>Disabled icon name: <code>PREFIX.ICON2</code></li>
     * <p/>
     * <li>Tooltip: <code>PREFIX.HINT</code></li>
     * <p/>
     * <li>Shortcut: <code>PREFIX.SHORT</code></li>
     * <p/>
     * </ul> This button does -- in addition to the one created by {@link #createSimpleButton(Class, String, ActionListener)} -- a key stroke on the top level component.
     *
     * @param obj            the object to search the associated resource bundle
     * @param prefix         the prefix used to search properties
     * @param actionListener actionListener to invoke
     * @return The button created
     */
    public static JButton createButton( final Class obj, final String prefix, final ActionListener actionListener )
    {
        final JButton button = createSimpleButton( obj, prefix, actionListener );
        final String shortcut = Strings.getString( obj, prefix + ".SHORT" );
        final KeyStroke key = KeyBindings.parseKeyStroke( shortcut );
        if ( key != null )
        {
            final String shortCutHint = " (" + KeyBindings.getSymbolicModifierName( shortcut ) + ")";
            button.setToolTipText( button.getToolTipText() + shortCutHint );
        }
        // registers the key binding the first time the button is showing up, guaranteed to be added to a root pane
        button.addHierarchyListener( new HierarchyListener()
        {
            public void hierarchyChanged( final HierarchyEvent e )
            {
                registerKeyStroke( button, Strings.getString( obj, prefix + ".SHORT" ), this );
            }
        } );

        return button;
    }

    private static void registerKeyStroke( final JButton button, final String resourceKey, final HierarchyListener owner )
    {
        if ( button.getRootPane() != null )
        {
            final KeyStroke key = KeyBindings.parseKeyStroke( resourceKey );
            if ( key != null )
            {
                ActionUtils.registerKeyStrokeAction( key, button );
            }
            if ( button.getTopLevelAncestor() instanceof JFrame && owner != null )
            {
                button.removeHierarchyListener( owner );
            }
        }
    }

    /**
     * Creates a button based on a resource bundle. The resource bundle is used to retrieve different properties of the new button:
     * <p/>
     * <ul>
     * <p/>
     * <li>Text: <code>PREFIX.TEXT</code></li>
     * <p/>
     * <li>Icon name: <code>PREFIX.ICON</code></li>
     * <p/>
     * <li>Disabled icon name: <code>PREFIX.ICON2</code></li>
     * <p/>
     * <li>Tooltip: <code>PREFIX.HINT</code></li>
     * <p/>
     * <li>Shortcut: <code>PREFIX.SHORT</code> (should be an empty string)</li>
     * <p/>
     * </ul>
     * <p/>
     * The tooltip is automatically completed by the shortcut in paranthesis. This button is not automatically associated with the key stroke, so the SHORT property should be left emtpy. If you want to create an associated button use {@link #createButton(Class, String, ActionListener)} or {@link #createButton(String, ActionListener)}.
     *
     * @param obj            The object to search the associated resource bundle
     * @param prefix         The key prefix used to retrieve properties
     * @param actionListener The event handler for this button
     * @return a new button
     */
    private static JButton createSimpleButton( final Class obj, final String prefix, final ActionListener actionListener )
    {
        final JButton button = new JButton();
        button.setText( Strings.getString( obj, prefix + ".TEXT" ) );
        button.setIcon( ImageLocator.getIcon( Strings.getString( obj, prefix + ".ICON" ) ) );
        final String disabledIconKey = Strings.getString( obj, prefix + ".ICON2" );
        if ( !"".equals( disabledIconKey ) )
        {
            button.setDisabledIcon( ImageLocator.getIcon( disabledIconKey ) );
        }
        button.addActionListener( actionListener );
        final String tooltip = Strings.getString( obj, prefix + ".HINT" );
        button.setToolTipText( tooltip );
        button.setFocusPainted( false );
        return button;
    }

    /**
     * Creates a button with name, icon and tooltip by extracting the strings/icon name from the global resource.<p> To compose the key for the strings resource, the following conventions are used, where <code>PREFIX</code> indicates the prefix given as an argument:
     * <p/>
     * <ul>
     * <p/>
     * <li>Text: <code>PREFIX.TEXT</code></li>
     * <p/>
     * <li>Icon: <code>PREFIX.ICON</code></li>
     * <p/>
     * <li>Tooltip: <code>PREFIX.HINT</code></li>
     * <p/>
     * </ul>
     *
     * @param prefix         The prefix used to search properties
     * @param actionListener ActionListener to invoke
     * @return The button created
     */
    public static JButton createButton( final String prefix, final ActionListener actionListener )
    {
        return createButton( null, prefix, actionListener );
    }

    public static JLabel createLabel( final Class obj, final String prefix )
    {
        final String text = Strings.getString( obj, prefix + ".TEXT" );
        final ImageIcon icon = ImageLocator.getIcon( Strings.getString( obj, prefix + ".ICON" ) );
        final JLabel label = new JLabel( text, icon, JLabel.LEFT );
        label.setToolTipText( Strings.getString( obj, prefix + ".HINT" ) );
        return label;
    }

    public static JButton createLabelButton( final Class obj, final String prefix, final ActionListener action )
    {
        final JButton button = createSimpleButton( obj, prefix, action );
        button.setBorder( BorderFactory.createEmptyBorder() );
        button.setHorizontalAlignment( SwingConstants.LEFT );
        button.setFocusable( false );
        return button;
    }

    public static JComponent createBarSeparator( final int top, final int left, final int bottom, final int right )
    {
        return createSeparator( "separator.png", top, left, bottom, right );
    }

    public static JComponent createBarSeparator()
    {
        return createSeparator( "separator.png", 0, 0, 0, 0 );
    }

    public static JComponent createArrowSeparator( final int top, final int left, final int bottom, final int right )
    {
        return createSeparator( "separator_arrow.png", top, left, bottom, right );
    }

    public static JComponent createSeparator( final String imageName, final int top, final int left, final int bottom, final int right )
    {
        final JLabel label = new JLabel( ImageLocator.getIcon( imageName ) );
        label.setBorder( BorderFactory.createEmptyBorder( top, left, bottom, right ) );
        return label;
    }

    public static JComponent createSeparator( final int top, final int left, final int bottom, final int right )
    {
        final JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createEmptyBorder( top, left, bottom, right ) );
        return panel;
    }
}
