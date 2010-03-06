package com.ethz.geobot.herbar.gui.about;

import ch.jfactory.animation.AnimationQueue;
import ch.jfactory.animation.Paintable;
import ch.jfactory.animation.fading.FadingPaintable;
import ch.jfactory.animation.scrolltext.ScrollingTextPaintable;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.logging.LogUtils;
import java.awt.Color;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        setSize( width, height );

        getContentPane().setLayout( null );
        getContentPane().add( label );

        WindowUtils.centerOnScreen( this );
        setVisible( true );
    }

    public Splash( final ImageIcon imageIcon, final AnimationQueue scroller )
    {
        final int width = imageIcon.getIconWidth();
        final int height = imageIcon.getIconHeight();
        final JLabel label = new JLabel( imageIcon );
        label.setBounds( 0, 0, width, height );
        setSize( width, height );

        getContentPane().setLayout( null );
        getContentPane().add( scroller );
        getContentPane().add( label );

        WindowUtils.centerOnScreen( this );
        setVisible( true );
    }

    public void finish()
    {
        dispose();
    }

    public static void main( final String[] args ) throws FileNotFoundException
    {
        LogUtils.init();
        final ImageIcon imageIcon = new ImageIcon( "resources/splash.jpg" );
        final AnimationQueue scrollingComponent = new AnimationQueue();
        scrollingComponent.setBounds( 100, 68, 200, 167 );
        final Insets insets = new Insets( 0, 10, 0, 10 );
        scrollingComponent.setInsets( insets );

        final Color fadeColor = new Color( 255, 255, 255, 150 );
        final Paintable fader = new FadingPaintable( fadeColor );
        scrollingComponent.addPaintable( fader );

        final int printSpaceWidth = scrollingComponent.getSize().width - insets.left - insets.right;
        final FileInputStream textFile = new FileInputStream( "config/News.txt" );
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFile, printSpaceWidth, true );
        scroller.setBackgroundColor( fadeColor );
        scroller.setScrollDelay( 5 );
        scrollingComponent.addPaintable( scroller );

        new Splash( imageIcon, scrollingComponent );
    }
}

