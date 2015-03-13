/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.collection.cursor.ArrayCursor;
import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

/**
 * User interactive control to set position inside a list of object (Taxon etc.). First, you have to register the IteratorPositionChangeListener to the control. Then set the list using the setList method.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class IteratorControlPanel extends JPanel
{
    private final Cursor<Taxon> cursor = new ArrayCursor<Taxon>( new Taxon[0] );

    private JButton first;

    private JButton previous;

    private JButton next;

    private JButton last;

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

    public void setList( final Taxon[] objects )
    {
        cursor.setCollection( objects );
    }

    private void setCurrent( Taxon taxon )
    {
        cursor.setCurrent( taxon );
    }

    public synchronized void addIteratorControlListener( final IteratorControlListener listener )
    {
        if ( iteratorControlListenerList == null )
        {
            iteratorControlListenerList = new Vector<IteratorControlListener>();
        }
        iteratorControlListenerList.add( listener );
    }

    public void updateControlState( final Taxon taxon )
    {
        setCurrent( taxon );

        next.setEnabled( isEnabled && cursor.hasNext() );
        previous.setEnabled( isEnabled && cursor.hasPrevious() );

        final String prefix = (labelString == null ? "" : labelString + " ");
        final String from = (cursor.isEmpty() ? "0" : "" + (cursor.getCurrentIndex() + 1));
        final String to = (cursor.isEmpty() ? "0" : "" + cursor.getSize());
        final String text = Strings.getString( "ITERATOR.TEXT", prefix + from, to );
        positionInfoText.setText( text );
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
        first = createFirstButton();
        previous = createPrevButton();
        next = createNextButton();
        last = createLastButton();
        positionInfoText = createPositionLabel();
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

    private JButton createFirstButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                while ( cursor.hasPrevious() )
                {
                    cursor.previous();
                }
                fireItemChange( new IteratorControlEvent( cursor.getCurrent() ) );
            }
        };
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.FIRST", action );
    }

    private JButton createPrevButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                cursor.previous();
                fireItemChange( new IteratorControlEvent( cursor.getCurrent() ) );
            }
        };
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.PREV", action );
    }

    private JButton createNextButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                cursor.next();
                fireItemChange( new IteratorControlEvent( cursor.getCurrent() ) );
            }
        };
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.NEXT", action );
    }

    private JButton createLastButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                while ( cursor.hasNext() )
                {
                    cursor.next();
                }
                fireItemChange( new IteratorControlEvent( cursor.getCurrent() ) );
            }
        };
        return ComponentFactory.createButton( "BUTTON.NAVIGATION.LAST", action );
    }

    public JButton getFirstButton()
    {
        return first;
    }

    public JButton getPrevButton()
    {
        return previous;
    }

    public JButton getNextButton()
    {
        return next;
    }

    public JButton getLastButton()
    {
        return last;
    }

    public JComponent getDisplay()
    {
        return positionInfoText;
    }
}
