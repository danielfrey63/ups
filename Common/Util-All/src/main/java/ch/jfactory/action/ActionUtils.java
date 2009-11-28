/*
 * Herbar CD-ROM version 2
 *
 * ActionUtils.java
 *
 * Created on Feb 7, 2003 1:38:31 PM
 * Created by Daniel
 */
package ch.jfactory.action;

import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ActionUtils
{
    /**
     * logger instance
     */
    private static final Logger cat = Logger.getLogger( ActionUtils.class );

    /**
     * Rigister a button to a given KeyStroke. The method generates a new <code>Action</code> if it hasn't been defined
     * one, which redirects to all registered <code>ActionListener</code>s. Otherwise it just registers the key for the
     * registered <code>Action</code>.<p> Note: Make sure to have added the button to the root pane when calling this
     * method.
     *
     * @param keyStroke given KeyStroke on which the action should occur
     * @param button    button which action will executed
     */
    public static void registerKeyStrokeAction( final KeyStroke keyStroke, final JButton button )
    {
        Action action = button.getAction();
        if ( action == null )
        {
            action = new ButtonRedirector( button );
        }
        final String actionKey = keyStroke.toString() + button.hashCode();
        if ( cat.isDebugEnabled() )
        {
            cat.debug( "register action: " + actionKey );
        }
        button.getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( keyStroke, actionKey );
        button.getRootPane().getActionMap().put( actionKey, action );
    }

    /**
     * Registers an escape key for the given panel and button. Make sure you place the button and to register actions
     * before using this method.<p> The method generates a new <code>Action</code> if it hasn't been defined one, which
     * redirects to all registered <code>ActionListener</code>s. Otherwise it just registers the key for the registered
     * <code>Action</code>.
     *
     * @param button The button to register the key with
     */
    public static void registerEscapeKey( final JButton button )
    {
        final KeyStroke keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, false );
        registerKeyStrokeAction( keyStroke, button );
    }

}