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
import java.util.List;
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

    private Vector iteratorControlListenerList;

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

    /**
     * Creates new form IteratorControlPanel
     */
    public IteratorControlPanel()
    {
        this( null );
    }

    public void setCursor( final Cursor cursor )
    {
        this.cursor.setCursor( cursor );
    }

    public void setCursor( final Object[] objects )
    {
        cursor.setCursor( objects );
    }

    public void setCursor( final List list )
    {
        cursor.setCursor( list );
    }

    public DefaultNotifiableCursor getIteratorCursor()
    {
        return cursor;
    }

    public synchronized void addIteratorControlListener( final IteratorControlListener listener )
    {
        if ( iteratorControlListenerList == null )
        {
            iteratorControlListenerList = new Vector();
        }
        iteratorControlListenerList.add( listener );
        listener.itemChange( new IteratorControlEvent( cursor.getCurrent() ) );
    }

    public synchronized void removeIteratorControlListener( final IteratorControlListener listener )
    {
        if ( iteratorControlListenerList != null )
        {
            iteratorControlListenerList.remove( listener );
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     */
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
                nextActionPerformed( e );
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
                previousActionPerformed( e );
            }
        };
        final JButton button = ComponentFactory.createButton( "BUTTON.NAVIGATION.PREV", action );
        button.setBorder( BorderFactory.createEmptyBorder() );
        return button;
    }

    protected void previousActionPerformed( final ActionEvent e )
    {
        cursor.previous();
    }

    protected void nextActionPerformed( final ActionEvent e )
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

// $Log: IteratorControlPanel.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.15  2005/06/17 06:39:58  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.14  2004/08/31 22:10:16  daniel_frey
// Examlist loading working
//
// Revision 1.13  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.12  2004/03/04 23:39:28  daniel_frey
// - Build with News on Splash
//
// Revision 1.11  2003/05/25 21:38:46  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.10  2003/04/14 08:54:08  daniel_frey
// - Statistics of exam mode not (more) correct
//
// Revision 1.9  2003/04/13 21:51:15  daniel_frey
// - Separated status actions and display into toolbar and status bar
//
// Revision 1.8  2003/04/02 14:49:03  daniel_frey
// - Revised wizards
//
// Revision 1.7  2003/03/16 08:53:01  daniel_frey
// - Exam mode with first shot of results
// - UI rolled partly back
//
// Revision 1.6  2003/02/27 12:21:56  daniel_frey
// - Removed bug where activate was called twice during initalization of mode
// - Moved some components common to lesson and exam to modeapi
// - Added additional functions to exam
//
// Revision 1.5  2002/09/11 12:48:01  dirk
// change cursor package to xmatrix
//
// Revision 1.4  2002/08/05 11:27:12  Dani
// - Moved to ch.xmatrix
//
// Revision 1.3  2002/05/28 14:04:21  Thomas
// take "von"-String from res
// buttons have borders and no focusring
// add. label can be added before 1 von 3-String
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
