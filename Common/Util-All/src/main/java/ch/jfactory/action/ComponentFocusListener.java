/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.action;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

/**
 * Transfers the default button to the given one uppon gain of focus in the component where this is registered, and reasignes the default button to the former one uppon lose of focus.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ComponentFocusListener implements FocusListener
{
    private JButton former;

    private final JButton buttonToFocus;

    /**
     * Register a handler to change the default button if the component has the focus. The return key is used to activate the button within the given component.
     *
     * @param component JComponent which should trigger the default button change
     * @param button    default button of the component
     */
    public static void registerComponentFocusListener( final JComponent component, final JButton button )
    {
        final KeyStroke keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0, false );
        final Action action = new ResetDefaultButtonAction( button, component );
        final String actionKey = keyStroke.toString() + button.toString();
        component.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( keyStroke, actionKey );
        component.getActionMap().put( actionKey, action );
    }

    /**
     * Retisters any key stroke to be usable within the given compnent to activate the given button.
     *
     * @param button    to activate with the given key stroke
     * @param component from within the key stroke should activte the button
     * @param keyStroke the key stroke to activate
     */
    public static void registerComponentFocusListener( final JComponent component, final JButton button, final KeyStroke keyStroke )
    {
        final Action action = new ButtonRedirector( button );
        final String actionKey = keyStroke.toString() + button.toString();
        if ( JTextComponent.class.isAssignableFrom( component.getClass() ) )
        {
            final JTextComponent text = (JTextComponent) component;
            text.getKeymap().removeKeyStrokeBinding( keyStroke );
        }
        component.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( keyStroke, actionKey );
        component.getActionMap().put( actionKey, action );
    }

    public ComponentFocusListener( final JButton button )
    {
        this.buttonToFocus = button;
    }

    public void focusGained( final FocusEvent e )
    {
        final JRootPane rootPane = buttonToFocus.getRootPane();
        if ( rootPane != null )
        {
            former = rootPane.getDefaultButton();
            rootPane.setDefaultButton( buttonToFocus );
        }
    }

    public void focusLost( final FocusEvent e )
    {
        final JRootPane rootPane = buttonToFocus.getRootPane();
        if ( rootPane != null && former != null )
        {
            rootPane.setDefaultButton( former );
        }
    }

    private static class ResetDefaultButtonAction extends ButtonRedirector
    {
        private final JComponent component;

        public ResetDefaultButtonAction( final JButton button, final JComponent component )
        {
            super( button );
            this.component = component;
        }

        public void actionPerformed( final ActionEvent e )
        {
            super.actionPerformed( e );
            // reset focus if component is the same
            final Component compFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if ( compFocusOwner == component )
            {
                button.getRootPane().setDefaultButton( button );
            }
        }
    }
}
