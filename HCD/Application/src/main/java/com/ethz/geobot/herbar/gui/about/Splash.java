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

import ch.jfactory.animation.AnimationQueue;
import ch.jfactory.animation.Paintable;
import ch.jfactory.animation.fading.FadingPaintable;
import ch.jfactory.animation.scrolltext.ScrollingTextPaintable;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.logging.LogUtils;
import java.awt.Color;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * don't use any vml access here!
 */
public class Splash extends JWindow
{
    public Splash( final ImageIcon imageIcon )
    {
        final int width = imageIcon.getIconWidth();
        final int height = imageIcon.getIconHeight();
        final JLabel label = new JLabel( imageIcon );
        label.setBounds( 0, 0, width, height );
        setContentPane( new TranslucentPane() );
        setSize( width, height );
        setBackground( new Color( 0, 0, 0, 0 ) );
        setLayout( null );
        add( label );

        WindowUtils.centerOnScreen( this );
        setVisible( true );
    }

    public Splash( final ImageIcon imageIcon, final AnimationQueue scroller )
    {
        final int width = imageIcon.getIconWidth();
        final int height = imageIcon.getIconHeight();
        final JLabel label = new JLabel( imageIcon );
        label.setBounds( 0, 0, width, height );
        setContentPane( new TranslucentPane() );
        setSize( width, height );
        setBackground( new Color( 0, 0, 0, 0 ) );
        setLayout( null );
        add( scroller );
        add( label );

        WindowUtils.centerOnScreen( this );
        setAlwaysOnTop( true );
        setVisible( true );
    }

    public void finish()
    {
        dispose();
    }

    public static void main( final String[] args ) throws FileNotFoundException
    {
        LogUtils.init();
        final ImageIcon imageIcon = new ImageIcon( "resources/splash.png" );
        final AnimationQueue scrollingComponent = new AnimationQueue();
        scrollingComponent.setBounds( 100, 68, 200, 167 );
        final Insets insets = new Insets( 0, 10, 0, 10 );
        scrollingComponent.setInsets( insets );

        final Color fadeColor = new Color( 255, 255, 255, 150 );
        final Paintable fader = new FadingPaintable( fadeColor );
        scrollingComponent.addPaintable( fader );

        final int printSpaceWidth = scrollingComponent.getSize().width - insets.left - insets.right;
        final InputStream textFile = Splash.class.getResourceAsStream( "/News.txt" );
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFile, printSpaceWidth, true );
        scroller.setBackgroundColor( fadeColor );
        scroller.setScrollDelay( 5 );
        scrollingComponent.addPaintable( scroller );

        new Splash( imageIcon, scrollingComponent );
    }

}

