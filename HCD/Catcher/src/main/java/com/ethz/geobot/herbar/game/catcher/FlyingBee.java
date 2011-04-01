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
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.SoundList;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

/**
 * Flying Bee, the manipulable Player of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingBee extends FlyingFlower
{
    private int x, y;

    private final int h = 470;

    private final int w = 750;

    private ImageIcon bee = new ImageIcon();

    private double winkel;

    private double radius;

    private double scaleX;

    private double scaleY;

    private float scaleBravo;

    private boolean soundPlayed;

    private AudioClip firstClip, secondClip;

    private SoundList soundList;

    private final String win = "BRAVO";

    private final String loose = "GAME OVER";

    private final Color greenUni;

    private final Font normalFont = new Font( "Arial", Font.PLAIN, 14 );

    private Font myFont = null;

    /**
     * Method FlyingBee. Constructor
     *
     * @param green Color-object for the background
     */
    public FlyingBee( final Color green )
    {
        this.greenUni = green;
        bee = ImageLocator.getIcon( "biene_1.gif" );
        init();
        startLoadingSounds();
        myFont = normalFont.deriveFont( Font.BOLD, 5.0f );
    }

    /**
     * returns the x-position of the bee (center of the image)
     *
     * @return the new x-position
     */
    public int getXPosBee()
    {
        return this.x + bee.getIconWidth() / 2;
    }

    /**
     * returns the y-position of the bee (center of the image)
     *
     * @return the new y-position
     */
    public int getYPosBee()
    {
        return this.y + bee.getIconHeight() / 2;
    }

    /** resets all the values concerning the bee-position and its environment */
    public void init()
    {
        this.x = w / 2;
        this.y = h / 2 - bee.getIconHeight() / 2;
        winkel = 0.0;
        radius = 80.0;
        scaleX = 1.0;
        scaleY = 1.0;
        scaleBravo = 5.0f;
        soundPlayed = false;
    }

    /** stops all sounds when changing the modus */
    public void stopAllSounds()
    {
        if ( firstClip != null )
        {
            firstClip.stop();
        }
        if ( secondClip != null )
        {
            secondClip.stop();
        }
    }

    /**
     * sets the new x-position of the bee by adding diff to the old x-position
     *
     * @param diff velocity-differency
     */
    public void moveRight( final int diff )
    {
        if ( !( x >= w - bee.getIconWidth() / 2 ) )
        {
            x += diff;
        }
    }

    /**
     * sets the new x-position of the bee by subtracting diff to the old x-position
     *
     * @param diff velocity-differency
     */
    public void moveLeft( final int diff )
    {
        if ( !( x <= 0 - bee.getIconWidth() / 2 ) )
        {
            x -= diff;
        }
    }

    /**
     * sets the new y-position of the bee by subtracting diff to the old y-position
     *
     * @param diff velocity-differency
     */
    public void moveUp( final int diff )
    {
        if ( !( y <= 0 - bee.getIconHeight() / 2 ) )
        {
            y -= diff;
        }
    }

    /**
     * sets the new y-position of the bee by adding diff to the old y-position
     *
     * @param diff velocity-differency
     */
    public void moveDown( final int diff )
    {
        if ( !( y >= h - bee.getIconHeight() / 2 ) )
        {
            y += diff;
        }
    }

    /** starts playing the background-sound when start-button is pressed */
    public void startSoundLoop()
    {
        playSound( 2 );
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        g.drawImage( bee.getImage(), x, y, this );
    }

    /**
     * special paint-method which paints the rotating crash of the bee
     *
     * @param g graphicobject
     */
    public void painter( final Graphics g )
    {
        g.setColor( greenUni );
        g.fillRect( 0, 0, 750, 470 );
        g.setColor( Color.yellow );
        g.setFont( normalFont );
        g.drawString( Strings.getString( Catcher.class,
                "CATCHER.AUSGABE.LOOSE" ), 10, 20 );
        myFont = myFont.deriveFont( scaleBravo );
        g.setFont( myFont );
        final Rectangle2D bravoSize = this.getFontMetrics( myFont ).
                getStringBounds( loose, g );
        g.drawString( loose, (int) ( w / 2 - bravoSize.getWidth() / 2 ),
                (int) ( h / 2 + bravoSize.getHeight() / 2 ) );
        final Graphics2D g2d = (Graphics2D) g;
        final AffineTransform tx = new AffineTransform();
        tx.translate( (double) x, (double) y );
        tx.rotate( winkel );
        tx.scale( scaleX, scaleY );
        tx.translate( radius, radius );
        g2d.drawImage( bee.getImage(), tx, this );
    }

    /**
     * special paint-method which paints the "BRAVO"- string when all questions are answered correctly
     *
     * @param g graphicobject
     */
    public void paintWin( final Graphics g )
    {
        g.setColor( greenUni );
        g.fillRect( 0, 0, 750, 470 );
        g.setColor( Color.yellow );
        g.setFont( normalFont );
        g.drawString( Strings.getString( Catcher.class,
                "CATCHER.AUSGABE.WIN" ), 10, 20 );
        myFont = myFont.deriveFont( scaleBravo );
        g.setFont( myFont );
        final Rectangle2D bravoSize = this.getFontMetrics( myFont ).
                getStringBounds( win, g );
        g.drawString( win, (int) ( w / 2 - bravoSize.getWidth() / 2 ),
                (int) ( h / 2 + bravoSize.getHeight() / 2 ) );
    }

    /**
     * plays the specific sound of crashing, winning or flying
     *
     * @param nr number which represents win-, rotate- or fly-sound
     */
    public void playSound( final int nr )
    {
        if ( nr == 2 )
        {
            secondClip = soundList.getClip( "flying_0.au" );
            secondClip.loop();
        }
        else
        {
            soundPlayed = true;
            if ( nr == 0 )
            {
                firstClip = soundList.getClip( "beeCrash.au" );
            }
            else
            {
                firstClip = soundList.getClip( "beeWin.au" );
                secondClip.loop();
            }
            secondClip.stop();
            firstClip.play();
        }
    }

    /**
     * special update-method which realizes the scaling of the "BRAVO"
     *
     * @param g graphicobject
     */
    public void updateWin( final Graphics g )
    {
        if ( !soundPlayed )
        {
            playSound( 1 );
        }
        if ( scaleBravo <= 120.0 )
        {
            scaleBravo += 5.0;
        }
    }

    /**
     * special update-method which realizes the updating of the crashing bee
     *
     * @param g graphicobject
     */
    public void updater( final Graphics g )
    {
        if ( !soundPlayed )
        {
            playSound( 0 );
        }
        if ( scaleBravo <= 80.0 )
        {
            scaleBravo += 5.0f;
        }
        if ( scaleX >= 0 )
        {
            scaleX -= 0.01;
            scaleY -= 0.01;
            winkel += 0.3;
            radius -= 1;
        }
    }

    /** preload sound */
    void startLoadingSounds()
    {
        final String codeBase = System.getProperty( "xmatrix.sound.path" ) + "/";
        soundList = new SoundList( codeBase );
        soundList.startLoading( "flying_0.au" );
        soundList.startLoading( "beeCrash.au" );
        soundList.startLoading( "beeWin.au" );
    }
}
