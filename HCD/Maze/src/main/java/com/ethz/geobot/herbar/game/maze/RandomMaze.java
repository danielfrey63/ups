/*
 * Herbar CD-ROM version 2
 *
 * RandomMaze.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import com.ethz.geobot.herbar.game.util.CountScore;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * labyrinth generation.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class RandomMaze extends Canvas implements KeyListener
{
    private int mazeElement;

    private final int numSqr = 28;

    private final int dimSqr = 16;

    private final Vector trail = new Vector();

    private final int[] maze = new int[numSqr * numSqr];

    private boolean pfadGenerated = false;

    private int countFull = 0;

    private Color solve;

    private final Color bande = new Color( 180, 220, 100 );

    private final Color neutral;

    private final Color prinzColor = Color.yellow;

    private final Prinz prinz;

    private final Prinzessin prinzessin = new Prinzessin();

    private final Flower empty = new Flower( numSqr, dimSqr );

    private final Flower path = new Flower( numSqr, dimSqr );

    private final QuestionPanel questionPanel;

    private final CountScore countScore;

    private final int numOfQuest = 10;

    private final MazePanel mPanel;

    /**
     * Constructor
     *
     * @param questionPane instance of the floating questionpanel
     * @param wiese        color of the background
     * @param countScore   instance of CountScore
     */
    public RandomMaze( final MazePanel maPanel, final QuestionPanel questionPane, final Color wiese,
                       final CountScore countScore )
    {
        this.mPanel = maPanel;
        this.neutral = wiese;
        this.countScore = countScore;
        this.questionPanel = questionPane;
        this.setVisible( true );
        this.addKeyListener( this );
        this.setFocusable( true );
        prinz = new Prinz( numSqr, dimSqr );
        this.setSize( 470, 470 );
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        questionPanel.setLocation( screenSize.width / 2 - questionPanel.
                getSize().width / 2, screenSize.height / 2 - questionPanel.
                getSize().height / 2 );
    }

    /**
     * @see KeyListener#keyPressed(KeyEvent)
     */
    public void keyPressed( final KeyEvent e )
    {
        try
        {
            if ( e.getKeyCode() == 37 )
            {
                // left
                if ( ( maze[prinz.getIndexArray()] & 1 ) == 0 &&
                        prinz.getIndexArray() % numSqr != 0 )
                {
                    placeAndPaintPrince( -1 );
                }
            }
            if ( e.getKeyCode() == 39 )
            {
                // right
                if ( ( maze[prinz.getIndexArray()] & 2 ) == 0 &&
                        prinz.getIndexArray() % numSqr == ( numSqr - 1 ) )
                {
                    if ( countScore.getTotalScore() < numOfQuest )
                    {
                        prinzessin.paintResult( this.getGraphics(), false, (
                                prinz.getIndexArray() ) % numSqr * dimSqr, (
                                (int) Math.floor( prinz.getIndexArray() / numSqr ) )
                                * dimSqr );
                    }
                    else
                    {
                        prinzessin.paintResult( this.getGraphics(), true, (
                                prinz.getIndexArray() ) % numSqr * dimSqr, (
                                (int) Math.floor( prinz.getIndexArray() / numSqr ) )
                                * dimSqr );
                    }
                    prinz.paint( this.getGraphics(), neutral );
                    mPanel.finished();
                }
                else if ( ( maze[prinz.getIndexArray()] & 2 ) == 0 )
                {
                    placeAndPaintPrince( 1 );
                }
            }
            if ( e.getKeyCode() == 38 )
            {
                // up
                if ( ( maze[prinz.getIndexArray()] & 4 ) == 0 )
                {
                    placeAndPaintPrince( -numSqr );
                }
            }
            if ( e.getKeyCode() == 40 )
            {
                // down
                if ( ( maze[prinz.getIndexArray()] & 8 ) == 0 )
                {
                    placeAndPaintPrince( numSqr );
                }
            }
        }
        catch ( Exception x )
        {
        }
    }

    /**
     * @see KeyListener#keyReleased(KeyEvent)
     */
    public void keyReleased( final KeyEvent e )
    {
    }

    /**
     * @see KeyListener#keyTyped(KeyEvent)
     */
    public void keyTyped( final KeyEvent e )
    {
    }

    /**
     * @see Component#paint(Graphics)
     */
    public void paint( final Graphics g )
    {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(
                new GradientPaint(
                        0.0f * 72, 0.0f * 72, neutral,
                        0.0f * 72, 6.5f * 72, new Color( 120, 240, 70 ), false ) );
        g2.fillRect( 0, 0, this.getWidth(), this.getHeight() );
        g.setColor( neutral );
        g.fillRect( 0, 0, numSqr * dimSqr, numSqr * dimSqr );

        for ( int i = 0; i < maze.length; i++ )
        {
            if ( ( maze[i] & 128 ) != 0 )
            {
                if ( ( maze[i] & 16 ) == 0 )
                {
                    g.setColor( solve );
                    g.fillRect( ( i % numSqr ) * dimSqr, ( (int) Math.floor(
                            i / numSqr ) ) * dimSqr, dimSqr, dimSqr );
                }
                g.setColor( bande );
                g.drawRect( ( i % numSqr ) * dimSqr, ( (int) Math.floor(
                        i / numSqr ) ) * dimSqr, dimSqr, dimSqr );

                if ( ( maze[i] & 16 ) == 0 )
                {
                    if ( ( maze[i] & 1 ) == 0 )
                    {
                        drawLineVertRight( g, i, solve );
                    }
                    if ( ( maze[i] & 2 ) == 0 )
                    {
                        drawLineVertLeft( g, i, solve );
                    }
                    if ( ( maze[i] & 4 ) == 0 )
                    {
                        drawLineHoriDown( g, i, solve );
                    }
                    if ( ( maze[i] & 8 ) == 0 )
                    {
                        drawLineHoriUp( g, i, solve );
                    }
                }
                if ( ( maze[i] & 32 ) == 0 && ( maze[i] & 16 ) != 0 )
                {
                    if ( ( maze[i] & 1 ) == 0 )
                    {
                        drawLineVertRight( g, i, neutral );
                    }
                    if ( ( maze[i] & 2 ) == 0 )
                    {
                        drawLineVertLeft( g, i, neutral );
                    }
                    if ( ( maze[i] & 4 ) == 0 )
                    {
                        drawLineHoriDown( g, i, neutral );
                    }
                    if ( ( maze[i] & 8 ) == 0 )
                    {
                        drawLineHoriUp( g, i, neutral );
                    }
                }
            }
        }
        if ( ( maze[0] & 128 ) != 0 )
        {
            path.paint( this.getGraphics() );
            empty.paint( this.getGraphics() );
            prinz.paint( g, prinzColor );
            prinzessin.paint( g );
        }
    }

    /**
     * paints the solution of the path.
     */
    public void solvePath()
    {
        solve = new Color( 50, 85, 25 );
        repaint();
    }

    /**
     * generates the trails beneath the maintrail
     */
    public void generateEmptyTrail()
    {
        trail.removeAllElements();
        mazeElement = generateEmptyStarts();
        trail.add( new Integer( mazeElement ) );
        maze[mazeElement] = maze[mazeElement] & 223;
        countFull += 1;
        while ( randomNextStep( mazeElement ) == true )
        {
            maze[mazeElement] = maze[mazeElement] & 223;
            trail.add( new Integer( mazeElement ) );
            countFull += 1;
        }
        if ( countFull <= maze.length )
        {
            generateEmptyTrail();
        }
    }

    /**
     * generates the main trail
     */
    public void generateTrail()
    {
        prinz.init();
        pfadGenerated = false;
        countFull = 0;
        initArray();
        initColor();
        trail.removeAllElements();
        mazeElement = numSqr / 2 * numSqr;
        prinz.setIndexArray( mazeElement );
        maze[mazeElement] = maze[mazeElement] & 206;
        trail.add( new Integer( mazeElement ) );
        countFull += 1;
        while ( randomNextStep( mazeElement ) == true )
        {
            maze[mazeElement] = maze[mazeElement] & 207;
            trail.add( new Integer( mazeElement ) );
            countFull += 1;
        }
        if ( pfadGenerated == false )
        {
            generateTrail();
        }
        else
        {
            defineFlowerIndices( trail );
            generateEmptyTrail();
        }
        empty.init( buildArray( maze.length / 8 ) );
        this.repaint();
        this.requestFocus();
    }

    // overpaint the old prince, set the new index and paint the new prince
    private void placeAndPaintPrince( final int diffIndex )
    {
        prinz.paint( this.getGraphics(), neutral );
        path.paint( this.getGraphics() );
        empty.paint( this.getGraphics() );
        prinz.setIndexArray( diffIndex );
        prinz.paint( this.getGraphics(), prinzColor );
        if ( ( maze[prinz.getIndexArray()] & 64 ) == 0 )
        {
            maze[prinz.getIndexArray()] += 64;
            questionPanel.nextQuestion();
            questionPanel.setVisible( true );
            questionPanel.tree.requestFocus();
        }
    }

    // set all maze indices to 1111'1111
    private void initArray()
    {
        for ( int i = 0; i < maze.length; i++ )
        {
            maze[i] = 255;
        }
    }

    // set the path color to the surround color
    private void initColor()
    {
        solve = neutral;
    }

    private int[] buildArray( final int numberOfFlowers )
    {
        final int[] arr = new int[numberOfFlowers];
        int count = 0;
        while ( count < numberOfFlowers )
        {
            final int rand = (int) ( Math.random() * maze.length );
            if ( ( maze[rand] & 64 ) != 0 )
            {
                arr[count] = rand;
                count += 1;
            }
        }
        return arr;
    }

    private void drawLineVertRight( final Graphics g, final int i, final Color color )
    {
        g.setColor( color );
        g.drawLine( ( i % numSqr ) * dimSqr, ( (int) Math.floor( i / numSqr ) )
                * dimSqr + 1, ( i % numSqr ) * dimSqr, (
                (int) Math.floor( i / numSqr ) ) * dimSqr + dimSqr - 1 );
    }

    private void drawLineVertLeft( final Graphics g, final int i, final Color color )
    {
        g.setColor( color );
        g.drawLine( ( i % numSqr ) * dimSqr + dimSqr, (
                (int) Math.floor( i / numSqr ) ) * dimSqr + 1, ( i % numSqr ) *
                dimSqr + dimSqr, ( (int) Math.floor( i / numSqr ) ) * dimSqr +
                dimSqr - 1 );
    }

    private void drawLineHoriUp( final Graphics g, final int i, final Color color )
    {
        g.setColor( color );
        g.drawLine( ( i % numSqr ) * dimSqr + 1, ( (int) Math.floor( i / numSqr
        ) ) * dimSqr + dimSqr, ( i % numSqr ) * dimSqr + dimSqr - 1, (
                (int) Math.floor( i / numSqr ) ) * dimSqr + dimSqr );
    }

    private void drawLineHoriDown( final Graphics g, final int i, final Color color )
    {
        g.setColor( color );
        g.drawLine( ( i % numSqr ) * dimSqr + 1, ( (int) Math.floor( i / numSqr )
        ) * dimSqr, ( i % numSqr ) * dimSqr + dimSqr - 1, ( (int) Math.floor(
                i / numSqr ) ) * dimSqr );
    }

    // generate a startpoint for the empty paths
    private int generateEmptyStarts()
    {
        int randomSeed = 0;
        if ( maze.length - countFull == 0 )
        {
            for ( int i = 0; i < maze.length; i++ )
            {
                if ( maze[i] == 255 )
                {
                    randomSeed = i;
                }
            }
            return randomSeed;
        }
        else
        {
            int count = 0;
            final int[] tempFree = new int[( maze.length - countFull )];
            for ( int i = 0; i < maze.length; i++ )
            {
                if ( maze[i] == 255 )
                {
                    tempFree[count] = i;
                    count += 1;
                }
            }
            final int random = ( (int) ( Math.random() * tempFree.length ) );
            randomSeed = tempFree[random];
            return randomSeed;
        }
    }

    private void defineFlowerIndices( final Vector trail )
    {
        final int[] tempArr = new int[numOfQuest];
        int countDown = 0;
        while ( countDown < numOfQuest )
        {
            final int rand = (int) ( Math.random() * ( trail.size() - 1 ) ) + 1;
            tempArr[countDown] = ( (Integer) trail.elementAt( rand ) ).intValue();
            maze[( (Integer) trail.elementAt( rand ) ).intValue()] &= 191;
            countDown += 1;
            trail.remove( rand );
        }
        path.init( tempArr );
    }

    /**
     * generates randomly the next square of the trail, fill-trail resp.
     *
     * @param i array-index of the last square
     * @return boolean
     */
    private boolean randomNextStep( final int i )
    {
        final int[] d = new int[]{i + 1, i - 1, i + numSqr, i - numSqr};

        // if the right side is reached
        if ( i % numSqr == ( numSqr - 1 ) )
        {
            if ( ( maze[i] & 16 ) == 0 )
            {
                maze[mazeElement] = maze[mazeElement] & 253;
                pfadGenerated = true;
                prinzessin.setPrincessXY( mazeElement % numSqr * dimSqr +
                        ( 4 * dimSqr ), ( (int) Math.floor( mazeElement / numSqr ) )
                        * dimSqr );
                return false;
            }
            else
            {
                d[0] = 0;
            }
        }

        for ( int iNeu = 0; iNeu < d.length; iNeu++ )
        {
            // if old element is a path element
            if ( ( maze[i] & 16 ) == 0 )
            {
                if ( d[iNeu] > 0 && d[iNeu] < maze.length )
                {
                    if ( ( maze[d[iNeu]] & 16 ) == 0 )
                    {
                        d[iNeu] = 0;
                    }
                }
            }
            for ( int io = 0; io < trail.size(); io++ )
            {
                if ( d[iNeu] == ( (Integer) trail.elementAt( io ) ).intValue() )
                {
                    d[iNeu] = 0;
                }
            }

        }
        // if left side is touched under the start element. only path
        if ( ( maze[i] & 16 ) == 0 )
        {
            if ( i % numSqr == 0 && i > prinz.getIndexArray() )
            {
                d[3] = 0;
                d[1] = 0;
            }
        }
        // if left side is touched over the start element. only path
        if ( ( maze[i] & 16 ) == 0 )
        {
            if ( i % numSqr == 0 && i < prinz.getIndexArray() )
            {
                d[1] = 0;
                d[2] = 0;
            }
        }
        // if left side is touched
        if ( i % numSqr == 0 )
        {
            d[1] = 0;
        }
        // if upper side is touched
        if ( i < numSqr )
        {
            d[3] = 0;
            if ( ( maze[i] & 16 ) == 0 )
            {
                d[1] = 0;
            }
        }
        // if lower side is touched
        if ( i >= numSqr * ( numSqr - 1 ) )
        {
            d[2] = 0;
            if ( ( maze[i] & 16 ) == 0 )
            {
                d[1] = 0;
            }
        }
        // check if there is a potential element left which is not null
        int leer = 0;
        for ( final int aD : d )
        {
            if ( aD == 0 )
            {
                leer += 1;
            }
        }
        if ( leer < 4 )
        {
            int count = (int) ( Math.random() * 4 );
            while ( d[count] == 0 )
            {
                count = (int) ( Math.random() * 4 );
            }

            mazeElement = d[count];
            if ( count == 0 )
            {
                maze[mazeElement] = maze[mazeElement] & 254;
                maze[i] = maze[i] & 253;
                if ( ( maze[mazeElement] & 32 ) == 0 )
                {
                    return false;
                }
            }
            if ( count == 1 )
            {
                maze[mazeElement] = maze[mazeElement] & 253;
                maze[i] = maze[i] & 254;
                if ( ( maze[mazeElement] & 32 ) == 0 )
                {
                    return false;
                }
            }
            if ( count == 2 )
            {
                maze[mazeElement] = maze[mazeElement] & 251;
                maze[i] = maze[i] & 247;
                if ( ( maze[mazeElement] & 32 ) == 0 )
                {
                    return false;
                }
            }
            if ( count == 3 )
            {
                maze[mazeElement] = maze[mazeElement] & 247;
                maze[i] = maze[i] & 251;
                if ( ( maze[mazeElement] & 32 ) == 0 )
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            if ( ( maze[i] & 16 ) == 0 )
            {
                pfadGenerated = false;
            }
            return false;
        }
    }
}
