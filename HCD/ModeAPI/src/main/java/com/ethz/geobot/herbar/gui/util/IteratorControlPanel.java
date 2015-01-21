/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.collection.cursor.CursorChangeEvent;
import ch.jfactory.collection.cursor.CursorChangeListener;
import ch.jfactory.collection.cursor.DefaultNotifiableCursor;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.Strings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * User interactive control to set position inside a list of object (Taxon etc.). First, you have to register the IteratorPositionChangeListener to the control. Then set the list using the setList method.
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

    private boolean isEnabled = true;

    /**
     * Creates new form IteratorControlPanel
     *
     * @param label a label to place before the numbers null, skips the display of
     */
    public IteratorControlPanel( final String label )
    {
        this.labelString = label;
        initComponents();
    }

    @Override
    public void setEnabled( boolean enabled )
    {
        isEnabled = enabled;
        super.setEnabled( isEnabled );
        previous.setEnabled( isEnabled && cursor.hasPrevious() );
        next.setEnabled( isEnabled && cursor.hasNext() );
        positionInfoText.setEnabled( isEnabled );
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
    }

    public void cursorChange( final CursorChangeEvent event )
    {
        // update control state
        next.setEnabled( isEnabled && cursor.hasNext() );
        previous.setEnabled( isEnabled && cursor.hasPrevious() );

        final String prefix = (labelString == null ? "" : labelString + " ");
        final String from = (cursor.isEmpty() ? "0" : "" + (cursor.getCurrentIndex() + 1));
        final String to = (cursor.isEmpty() ? "0" : "" + cursor.getSize());
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
            ((IteratorControlListener) aList).itemChange( event );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents()
    {
        previous = createPrevButton();
        next = createNextButton();
        positionInfoText = createPositionLabel();
        cursor.addCursorChangeListener( this );
    }

    private JLabel createPositionLabel()
    {
        final JLabel label = new JLabel();
        final String prefix = (labelString == null ? "" : labelString + " ");
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
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.NEXT", action );
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
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.PREV", action );
    }

    protected void previousActionPerformed()
    {
        cursor.previous();
    }

    protected void nextActionPerformed()
    {
        cursor.next();
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
