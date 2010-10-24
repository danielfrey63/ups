/*
 * Herbar CD-ROM version 2
 *
 * IteratorControlPanel.java
 *
 * Created on ??. ??? 2002, ??:??
 * Created by ???
 */
package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.collection.cursor.CursorChangeEvent;
import ch.jfactory.collection.cursor.CursorChangeListener;
import ch.jfactory.collection.cursor.DefaultNotifiableCursor;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.Strings;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * User interactive control to set position inside a list of object (Taxon etc.). First, you have to register the
 * IteratorPositionChangeListener to the control. Then set the list using the setList method.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class IteratorControlPanel extends JPanel implements CursorChangeListener
{
    private final DefaultNotifiableCursor cursor = new DefaultNotifiableCursor();

    private JButton previous;

    private JButton next;

    private JLabel positionInfoText;

    private Vector<IteratorControlListener> iteratorControlListenerList;

    private String labelString;

    private FlowLayout layout;

    /**
     * Creates new form IteratorControlPanel
     *
     * @param label a label to place before the numbers null, skips the display of
     */
    public IteratorControlPanel( final String label )
    {
        this.labelString = label;
        initGUI();
    }

    public void setCursor( final Cursor cursor )
    {
        this.cursor.setCursor( cursor );
    }

    public void setCursor( final Object[] objects )
    {
        cursor.setCursor( objects );
    }

    public DefaultNotifiableCursor getIteratorCursor()
    {
        return cursor;
    }

    public synchronized void addIteratorControlListener( final IteratorControlListener listener )
    {
        if ( iteratorControlListenerList == null )
        {
            iteratorControlListenerList = new Vector<IteratorControlListener>();
        }
        iteratorControlListenerList.add( listener );
        listener.itemChange( new IteratorControlEvent( cursor.getCurrent() ) );
    }

    public void cursorChange( final CursorChangeEvent event )
    {
        // update control state
        next.setEnabled( cursor.hasNext() );
        previous.setEnabled( cursor.hasPrevious() );

        final String prefix = ( labelString == null ? "" : labelString + " " );
        final String from = ( cursor.isEmpty() ? "0" : "" + ( cursor.getCurrentIndex() + 1 ) );
        final String to = ( cursor.isEmpty() ? "0" : "" + cursor.getSize() );
        final String text = Strings.getString( "ITERATOR.TEXT", prefix + from, to );
        positionInfoText.setText( text );

        fireItemChange( new IteratorControlEvent( event.getCurrentObject() ) );
    }

    protected void fireItemChange( final IteratorControlEvent event )
    {
        final Vector list;
        synchronized ( this )
        {
            if ( iteratorControlListenerList == null )
            {
                return;
            }
            list = (Vector) iteratorControlListenerList.clone();
        }
        for ( final Object aList : list )
        {
            ( (IteratorControlListener) aList ).itemChange( event );
        }
    }

    /** This method is called from within the constructor to initialize the form. */
    private void initGUI()
    {
        previous = createPrevButton();
        next = createNextButton();
        positionInfoText = createPositionLabel();

        layout = new FlowLayout( FlowLayout.LEFT, 5, 0 );
        setLayout( layout );
        add( previous );
        add( positionInfoText );
        add( next );

        cursor.addCursorChangeListener( this );
    }

    private JLabel createPositionLabel()
    {
        final JLabel label = new JLabel();
        final String prefix = ( labelString == null ? "" : labelString + " " );
        final String max = Strings.getString( "ITERATOR.MAX.TEXT" );
        label.setText( Strings.getString( "ITERATOR.TEXT", prefix + max, max ) );
        label.setPreferredSize( label.getPreferredSize() );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        return label;
    }

    private JButton createNextButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                nextActionPerformed();
            }
        };
        final JButton button = ComponentFactory.createButton( "BUTTON.NAVIGATION.NEXT", action );
        button.setBorder( BorderFactory.createEmptyBorder() );
        return button;
    }

    private JButton createPrevButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                previousActionPerformed();
            }
        };
        final JButton button = ComponentFactory.createButton( "BUTTON.NAVIGATION.PREV", action );
        button.setBorder( BorderFactory.createEmptyBorder() );
        return button;
    }

    protected void previousActionPerformed()
    {
        cursor.previous();
    }

    protected void nextActionPerformed()
    {
        cursor.next();
    }

    public void setAlignment( final int alignment )
    {
        layout.setAlignment( alignment );
    }

    public JButton getNextButton()
    {
        return next;
    }

    public JButton getPrevButton()
    {
        return previous;
    }

    public JComponent getDisplay()
    {
        return positionInfoText;
    }

    public void reset()
    {
        cursorChange( new CursorChangeEvent( cursor ) );
    }
}
