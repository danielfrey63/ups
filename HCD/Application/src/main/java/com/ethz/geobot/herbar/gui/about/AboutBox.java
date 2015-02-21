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
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AboutBox extends JWindow
{
    private final ImageIcon about = ImageLocator.getIcon( "splash.gif" );

    public AboutBox( final JFrame parent )
    {
        super( parent );
        build();
        addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent me )
            {
                AboutBox.this.dispose();
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
                this.toFront();
            }
        }
    }

    private void build()
    {
        final int width = about.getIconWidth();
        final int height = about.getIconHeight();
        final JLabel label = new JLabel( about );
        label.setBounds( 0, 0, width, height );
        setContentPane( new TranslucentPane() );
        setSize( width, height );
        setBackground( new Color( 0, 0, 0, 0 ) );
        setLayout( null );
        add( label );
    }

    public static void main( final String[] args )
    {
        LogUtils.init();
        final JFrame parent = new JFrame();
        final AboutBox dlg = new AboutBox( parent );
        dlg.getSize();
        dlg.setVisible( true );
        dlg.toFront();
    }
}