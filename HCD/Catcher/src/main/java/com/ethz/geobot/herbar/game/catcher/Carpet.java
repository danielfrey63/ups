/*
 * Herbar CD-ROM version 2
 *
 * Carpet.java
 *
 * Created on 09. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.SoundList;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Animation class with thread of the catcher-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class Carpet extends Canvas implements Runnable
{
    /**
     * number of flying flowers
     */
    public static final int MAX = 10;

    /**
     * number of flying gras
     */
    public static final int MAXGRAS = 20;

    /**
     * number of flying birds
     */
    public static final int MAXBIRD = 5;

    private static final Logger LOG = LoggerFactory.getLogger( Carpet.class );

    private boolean threadRunning = false;

    private Image image;

    private Graphics offscreen;

    private final FlyingFlower[] ff;

    private final FlyingFlower[] fg;

    private final FlyingBird[] fb;

    private final EnergyBar eb;

    private final FlyingBee bee;

    private final FlyingEarth fe;

    private final CountScore countScore;

    private boolean flyingCarpet = false;

    private boolean flyingBird = false;

    private boolean rotatingBee = false;

    private boolean winingBee = false;

    private SoundList soundList;

    /**
     * Method Carpet.        Constructor
     *
     * @param catcher    instance of class Catcher
     * @param bee        instance of class Bee
     * @param ff         instance of class FlyingFlower
     * @param fg         instance of class FlyingGras
     * @param fb         instance of class FlyingBird
     * @param fe         instance of class FlyingEarth
     * @param eb         instance of class EnergyBar
     * @param countScore instance of class countScore
     */
    public Carpet( final Catcher catcher, final FlyingBee bee, final FlyingFlower[] ff,
                   final FlyingGras[] fg, final FlyingBird[] fb, final FlyingEarth fe, final EnergyBar eb,
                   final CountScore countScore )
    {
        this.bee = bee;
        this.ff = ff;
        this.fg = fg;
        this.fb = fb;
        this.fe = fe;
        this.eb = eb;
        this.countScore = countScore;
        startLoadingSounds();
    }

    /**
     * decides if the carpet movement is on or off
     *
     * @param flyC boolean background-movement on or off
     */
    public void setFlyingCarpet( final boolean flyC )
    {
        flyingCarpet = flyC;
    }

    /**
     * decides if the bird movement is on or off
     *
     * @param flyB boolean if the birds are flying or not
     */
    public void setFlyingBird( final boolean flyB )
    {
        flyingBird = flyB;
    }

    /**
     * preload sound of collision between bird and bee
     */
    void startLoadingSounds()
    {
        final String codeBase = System.getProperty( "xmatrix.sound.path" ) + "/";
        soundList = new SoundList( codeBase );
        soundList.startLoading( "kollision.au" );
    }

    /**
     * plays the sound when crash between bird and bee
     */
    public void playSound()
    {
        soundList.getClip( "kollision.au" ).play();
    }

    /**
     * decides if the bee movement is rotating or not
     *
     * @param rotB boolean if the bee is rotating or not
     */
    public void setRotatingBee( final boolean rotB )
    {
        rotatingBee = rotB;
    }

    /**
     * decides if the bee movement is rotating or not
     *
     * @param winB boolean if the bee is winning or not
     */
    public void setWiningBee( final boolean winB )
    {
        winingBee = winB;
    }

    /**
     * THE paint method
     *
     * @param g Description of the Parameter
     */
    public void paint( final Graphics g )
    {
        if ( offscreen == null )
        {
            image = createImage( this.getSize().width, this.getSize().height );
            offscreen = image.getGraphics();
        }

        fe.paint( offscreen );

        for ( int i = 0; i < MAXGRAS; i++ )
        {
            fg[i].paint( offscreen );
        }
        for ( int i = 0; i < MAX; i++ )
        {
            ff[i].paint( offscreen );
        }
        eb.paint( offscreen );
        if ( rotatingBee )
        {
            bee.painter( offscreen );
        }
        else if ( winingBee )
        {
            bee.paintWin( offscreen );
        }
        else
        {
            bee.paint( offscreen );
        }

        for ( int i = 0; i < MAXBIRD; i++ )
        {
            fb[i].paint( offscreen );
        }
        g.drawImage( image, 0, 0, this );
    }

    /**
     * Main processing method for the carpet object
     */
    public void run()
    {
        try
        {
            while ( threadRunning )
            {
                try
                {
                    Thread.sleep( 15 );
                }
                catch ( InterruptedException e )
                {
                }

                if ( !threadRunning )
                {
                    return;
                }
                repaint();
            }
        }
        catch ( Exception x )
        {
            LOG.error( "run()", x );
        }
    }

    /**
     * initializes the Thread
     */
    public void init()
    {
        threadRunning = true;
        new Thread( this ).start();
    }

    /**
     * updates the component's position
     *
     * @param g Graphics object
     */
    public void update( final Graphics g )
    {
        if ( flyingCarpet )
        {
            fe.update( g );
            for ( int i = 0; i < MAXGRAS; i++ )
            {
                fg[i].update( g );
            }
            for ( int i = 0; i < MAX; i++ )
            {
                ff[i].update( g );
            }
        }
        if ( rotatingBee )
        {
            bee.updater( g );
        }
        if ( winingBee )
        {
            bee.updateWin( g );
        }
        if ( flyingBird )
        {
            for ( int i = 0; i < MAXBIRD; i++ )
            {
                fb[i].update( g );
            }
            for ( int i = 0; i < MAXBIRD; i++ )
            {
                if ( fb[i].getYPosBird() < bee.getYPosBee() + 20
                        && fb[i].getYPosBird() > bee.getYPosBee() - 20 )
                {
                    if ( fb[i].getXPosBird() > bee.getXPosBee() - 3
                            && fb[i].getXPosBird() < bee.getXPosBee() + 3 )
                    {
                        countScore.addWrongScore( 5 );
                        playSound();
                    }
                }
            }
        }
        paint( g );
    }

    /**
     * stops the Thread
     */
    public void stop()
    {
        threadRunning = false;
    }
}
