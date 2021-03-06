/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */

/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.resource.ImageLocator;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Thread class with run-method of the oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class Galgen extends Canvas implements Runnable
{
    public static final int TOTAL_STEPS = 5;

    private static final int STEP_ZERO = 0;

    private static final int STEP_BASE = 1;

    private static final int STEP_SENKRECHT = 2;

    private static final int STEP_WAAGRECHT = 3;

    private static final int STEP_SCHLINGE = 4;

    private static final int STEP_BIRD = TOTAL_STEPS;

    private boolean threadRunning = false;

    private Thread motor;

    private final ImageIcon bgBild = ImageLocator.getIcon( "bg.jpg" );

    private int galgenBauStep = 0;

    private final Color colorPBL = new Color( 110, 102, 87 );

    private final Color colorPBR = new Color( 61, 55, 47 );

    private final Color colorPBO = new Color( 146, 113, 85 );

    private final Color colorPSL = new Color( 127, 36, 0 );

    private final Color colorPSR = new Color( 96, 31, 5 );

    private final Color colorPSO = new Color( 152, 60, 24 );

    private final Color colorPWL = new Color( 152, 60, 24 );

    private final Color colorPWR = new Color( 96, 31, 5 );

    private final Color bgGalgen = new Color( 63, 59, 21 );

    private final Color bgGalgenHell = new Color( 87, 82, 32 );

    private final Color polygonKontur = new Color( 80, 25, 4 );

    private final Color baseKontur = new Color( 53, 49, 11 );

    private Image image;

    private Graphics offscreen;

    // Coordinates from the Base. begins always in the lower right corner.
    private final double[] pBUx = {250, 201, 167, 208};

    private final double[] pBUy = {462, 473, 473, 462};

    private final double[] pBOx = {250, 199, 164, 208};

    private final double[] pBOy = {444, 457, 457, 446};

    // Coordinates from the vertical bar.
    private final double[] pSUx = {234, 215, 180, 196};

    private final double[] pSUy = {448, 453, 453, 448};

    private final double[] pSOx = {217, 187, 122, 154};

    private final double[] pSOy = {110, 124, 124, 110};

    // Coordinates from the horizontal bar.
    private final double[] pWUx = {219, 217, 154, 157};

    private final double[] pWUy = {152, 110, 110, 152};

    private final double[] pWOx = {435, 446, 376, 374};

    private final double[] pWOy = {66, 14, 14, 66};

    // Coordinates from the galgen seil.
    private final double[] pOx = {376, 380, 380, 376};

    private final double[] pOy = {90, 88, 88, 90};

    private final double[] pUx = {376, 380, 380, 376};

    private final double[] pUy = {173, 175, 173, 175};

    private final GalgenPolygon[] spritesPolygon = new GalgenPolygon[5];

    private final GalgenPolygon spriteBase = new GalgenPolygon( pBUx, pBUy, pBOx, pBOy, colorPBR, colorPBL, colorPBO, baseKontur, true );

    private final GalgenPolygon spriteSenkrecht = new GalgenPolygon( pSUx, pSUy,
            pSOx, pSOy, colorPSR, colorPSL, colorPSO, polygonKontur, true );

    private final GalgenPolygon spriteWaagrecht = new GalgenPolygon( pWUx, pWUy,
            pWOx, pWOy, colorPWR, colorPWL, colorPSO, polygonKontur, false );

    private final GalgenPolygon spriteGalgen = new GalgenSchlinge( pOx, pOy, pUx,
            pUy, bgGalgen, bgGalgen, bgGalgen, bgGalgenHell, false );

    private final GalgenPolygon spriteBird = new GalgenVogel( this );

    /** Constructor for the Galgen object */
    public Galgen()
    {
        spritesPolygon[0] = spriteGalgen;
        spritesPolygon[1] = spriteBase;
        spritesPolygon[2] = spriteWaagrecht;
        spritesPolygon[3] = spriteSenkrecht;
        spritesPolygon[4] = spriteBird;
    }

    /**
     * Sets the galgenBauStep attribute of the Galgen object
     *
     * @param count The new galgenBauStep value
     */
    public void setGalgenBauStep( final int count )
    {
        this.galgenBauStep = count;
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
        offscreen.drawImage( bgBild.getImage(), 0, 0, this );

        for ( final GalgenPolygon aSpritesPolygon : spritesPolygon )
        {
            aSpritesPolygon.paint( offscreen );
        }
        g.drawImage( image, 0, 0, this );
    }

    /** Main processing method for the Galgen object */
    public void run()
    {
        while ( threadRunning )
        {
            try
            {
                Thread.sleep( 80 );
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

    /** initializes the Thread */
    public void initThread()
    {
        motor = new Thread( this );
        motor.start();
        threadRunning = true;
    }

    /** @param g Graphics object */
    public void update( final Graphics g )
    {
        if ( galgenBauStep == STEP_ZERO )
        {
            for ( final GalgenPolygon aSpritesPolygon : spritesPolygon )
            {
                aSpritesPolygon.setIsVisible( false );
                aSpritesPolygon.setGrowing( false );
                aSpritesPolygon.init();
            }
        }
        if ( galgenBauStep == STEP_BASE )
        {
            spriteBase.setGrowing( true );
            spriteBase.setIsVisible( true );
        }
        if ( galgenBauStep == STEP_SENKRECHT )
        {
            spriteSenkrecht.setGrowing( true );
            spriteSenkrecht.setIsVisible( true );
            spriteBase.setGrowing( true );
            spriteBase.setIsVisible( true );
        }
        if ( galgenBauStep == STEP_WAAGRECHT )
        {
            spriteWaagrecht.setGrowing( true );
            spriteWaagrecht.setIsVisible( true );
            spriteSenkrecht.setGrowing( true );
            spriteSenkrecht.setIsVisible( true );
            spriteBase.setGrowing( true );
            spriteBase.setIsVisible( true );
        }
        if ( galgenBauStep == STEP_SCHLINGE )
        {
            spriteGalgen.setGrowing( true );
            spriteGalgen.setIsVisible( true );
            spriteWaagrecht.setGrowing( true );
            spriteWaagrecht.setIsVisible( true );
            spriteSenkrecht.setGrowing( true );
            spriteSenkrecht.setIsVisible( true );
            spriteBase.setGrowing( true );
            spriteBase.setIsVisible( true );
        }
        if ( galgenBauStep == STEP_BIRD )
        {
            for ( int i = 0; i < spritesPolygon.length - 1; i++ )
            {
                spritesPolygon[i].setIsVisible( true );
                spritesPolygon[i].setGrowing( true );
            }

            spriteBird.setIsVisible( true );
        }

        for ( final GalgenPolygon aSpritesPolygon : spritesPolygon )
        {
            if ( aSpritesPolygon.isVisible() )
            {
                aSpritesPolygon.update( g );
            }
        }
        paint( g );
    }

    /** stops the Thread */
    public void stopThread()
    {
        threadRunning = false;
    }
}
