/*
 * Herbar CD-ROM version 2
 *
 * Prinzessin.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.game.util.SoundList;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;

/**
 * the princess in form of a head with a crone.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class Prinzessin extends Canvas
{
    private SoundList soundList;

    private int princessX;

    private int princessY;

    /**
     * Constructor
     */
    public Prinzessin()
    {
        startLoadingSounds();
    }

    /**
     * sets the princess's position
     *
     * @param x x-position
     * @param y y-position
     */
    public void setPrincessXY( final int x, final int y )
    {
        this.princessX = x;
        this.princessY = y;
    }

    /**
     * paints the diplom when win, the frog when loose at the end of the game
     *
     * @param g      Graphics object
     * @param result true, if all questions correctly answered
     * @param x      x-position of the game-over-picture
     * @param y      y-position of the game-over-picture
     */
    public void paintResult( final Graphics g, final boolean result, final int x, final int y )
    {
        if ( result )
        {
            g.drawImage( ImageLocator.getIcon( "labyDiplom.gif" ).getImage(),
                    x + 20, y, this );
            soundList.getClip( "morning.au" ).play();
        }
        else
        {
            g.drawImage( ImageLocator.getIcon( "labyFrog.gif" ).getImage(),
                    x + 20, y, this );
            soundList.getClip( "frog.au" ).play();
        }
        paint( g );
    }

    /**
     * @see Component#paint(Graphics)
     */
    public void paint( final Graphics g )
    {
        g.drawImage( ImageLocator.getIcon( "labyPrincess.gif" ).getImage(),
                princessX, princessY, this );
    }

    /**
     * Method update.
     *
     * @param g      Graphics object
     * @param result result
     */
    public void update( final Graphics g, final int result )
    {
        paint( g );
    }

    private void startLoadingSounds()
    {
        final String codeBase = System.getProperty( "xmatrix.sound.path" ) + "/";
        soundList = new SoundList( codeBase );
        soundList.startLoading( "frog.au" );
        soundList.startLoading( "morning.au" );
    }

}
