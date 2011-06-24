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
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AboutBox extends JWindow
{
    private final ImageIcon about = ImageLocator.getIcon( "splash.jpg" );

    private final JButton buttonSplash = new JButton();

    public AboutBox( final JFrame parent )
    {
        super( parent );
        build();
        addWindowListener();
        addMouseListener();
    }

    private void addMouseListener()
    {
        buttonSplash.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent me )
            {
                closeWindow();
            }
        } );
    }

    private void addWindowListener()
    {
        addWindowListener( new WindowAdapter()
        {
            public void windowClosing()
            {
                closeWindow();
            }

        } );
    }

    public void closeWindow()
    {
        setVisible( false );
        dispose();
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
        buttonSplash.setBorder( null );
        buttonSplash.setIcon( about );
        buttonSplash.setBounds( 0, 0, 480, 272 );

        final Container contentPane = getContentPane();
        contentPane.setLayout( null );
        contentPane.add( buttonSplash );
        setSize( 480, 272 );
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