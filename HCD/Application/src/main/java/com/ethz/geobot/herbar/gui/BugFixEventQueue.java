package com.ethz.geobot.herbar.gui;

/**
 * <code>BugFixEventQueue</code> is a workaround for a bug in jdk1.4
 * (http://developer.java.sun.com/developer/bugParade/bugs/4737679.html)
 * that causes odd characters to be inserted whenever the user presses
 * any of the cursor-movement keys in conjunction with the ALT key.
 * The bug is characterized by a KEY_TYPED event immediately following
 * the release of the ALT key, so we look for that pattern and kill
 * the event.
 * <p>
 * NOTE that this disables the Windows feature that lets you insert
 * non-ASCII characters by holding the ALT key down while punching in
 * a code on the numeric pad.
 *
 * @author Alan Moore
 */

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class BugFixEventQueue extends EventQueue
{
    private static final BugFixEventQueue INSTANCE = new BugFixEventQueue();

    private boolean altReleased;

    public static void install()
    {
        final EventQueue current = Toolkit.getDefaultToolkit().getSystemEventQueue();
        if ( current == INSTANCE )
        {
            return;
        }
        current.push( INSTANCE );
    }

    protected void dispatchEvent( final AWTEvent event )
    {
        if ( event instanceof KeyEvent )
        {
            final KeyEvent evt = (KeyEvent) event;
            final int keyCode = evt.getKeyCode();
            final int evtType = evt.getID();

            if ( keyCode == KeyEvent.VK_ALT )
            {
                altReleased = ( evtType == KeyEvent.KEY_RELEASED );
                return;
            }
            else if ( altReleased && evtType == KeyEvent.KEY_TYPED )
            {
                altReleased = false;
                evt.consume();
                return;
            }
            else
            {
                altReleased = false;
            }
        }
        super.dispatchEvent( event );
    }
}

