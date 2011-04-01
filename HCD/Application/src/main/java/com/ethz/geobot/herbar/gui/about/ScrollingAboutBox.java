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
package com.ethz.geobot.herbar.gui.about;

import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ScrollingAboutBox extends JWindow
{
    private static final int TOPPOSITION = 63;

    public String[] LEUTE = {
            "Baltisberger Matthias",
            "Frey Daniel",
    };

    private final int delay = 20;

    private Thread motor;

    private int shiftMax, shift;

    private Dimension dim;

    private FontMetrics fm;

    private boolean running = false;

    private final ImageIcon about = ImageLocator.getIcon( "splash.jpg" );

    private final ScrollLabel sl = new ScrollLabel();

    private final JButton buttonSplash = new JButton();

    public ScrollingAboutBox( final JFrame parent )
    {
        super( parent );
        enableEvents( AWTEvent.WINDOW_EVENT_MASK );
        try
        {
            init();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void init() throws Exception
    {
        getContentPane().setLayout( null );
        buttonSplash.setBorder( null );
        this.getContentPane().add( sl );
        buttonSplash.setIcon( about );
        this.getContentPane().add( buttonSplash );
        buttonSplash.setBounds( 0, 0, 400, 300 );
        sl.setBounds( 0, TOPPOSITION, 400, 178 );
        this.setSize( 400, 300 );
        buttonSplash.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent p_me )
            {
                sl.stop();
                dispose();
            }
        } );
    }

    public void setVisible( final boolean b )
    {
        if ( b != this.isVisible() )
        {
            super.setVisible( b );
            if ( b )
            {
                sl.init();
            }
            else
            {
                sl.stop();
            }
            this.toFront();
        }
    }

    //Overridden so we can exit when window is closed
    protected void processWindowEvent( final WindowEvent e )
    {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING )
        {
            cancel();
        }
        super.processWindowEvent( e );
    }

    //Close the dialog
    void cancel()
    {
        sl.stop();
        dispose();
    }

    class ScrollLabel extends JComponent implements Runnable
    {
        public ScrollLabel()
        {
            setOpaque( false );
            this.setDoubleBuffered( true );
        }

        public void stop()
        {
            running = false;
        }

        public void run()
        {
            motor.setPriority( Thread.MIN_PRIORITY );
            while ( running )
            {
                shift += 1;
                shift %= dim.height + LEUTE.length * shiftMax;
                try
                {
                    Thread.sleep( 1000 / delay );
                }
                catch ( InterruptedException e )
                {
                }
                if ( !running )
                {
                    return;
                }
                repaint();

            }
        }

        public void init()
        {
            setBackground( Color.white );
            dim = this.getSize();
            final Image image = createImage( dim.width, dim.height );
            final Graphics gi = image.getGraphics();
            fm = gi.getFontMetrics();
            shiftMax = fm.getHeight();
            motor = new Thread( this );
            running = true;
            motor.start();
        }

        public void paint( final Graphics g )
        {
            g.setFont( g.getFont().deriveFont( Font.BOLD, 14f ) );
            g.setColor( Color.white );
            if ( dim != null )
            {
                for ( int i = 0; i < LEUTE.length; i++ )
                {
                    final int x = ( dim.width - fm.stringWidth( LEUTE[i] ) ) / 2;
                    g.drawString( LEUTE[i], x, dim.height - shift + i * shiftMax );
                }
            }
        }

        public void update( final Graphics g )
        {
            paint( g );
        }

    }

    public static void main( final String[] args )
    {
        LogUtils.init();
        final JWindow w = new ScrollingAboutBox( null );
        w.setVisible( true );
    }
}