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
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Flying Flowers.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingFlower extends Canvas
{
    private double posX, posY;

    private double yinc;

    private int h = 470;

    private int w = 750;

    private Graphics g;

    private ImageIcon blume = new ImageIcon();

    /** Constructor for the FlyingFlower object */
    public FlyingFlower()
    {
        final int randomGif = (int) ( Math.random() * 11 );
        blume = ImageLocator.getIcon( "blume_" + randomGif + ".gif" );

        //random initial coordinates within the panel bounderies
        posX = ( Math.random() * w );
        posY = ( Math.random() * h );

        yinc = ( Math.random() * 0.5 ) + 3.0;
    }

    /**
     * Gets the xPosFlower attribute of the FlyingFlower object
     *
     * @return The xPosFlower value
     */
    public int getXPosFlower()
    {
        return (int) ( posX + blume.getIconWidth() / 2 );
    }

    /**
     * Gets the yPosFlower attribute of the FlyingFlower object
     *
     * @return The yPosFlower value
     */
    public int getYPosFlower()
    {
        return (int) ( posY + blume.getIconHeight() / 2 );
    }

    /**
     * increase the velocity of the flower-images
     *
     * @param vel difference to old velocity
     */
    public void incVelocity( final int vel )
    {
        yinc += vel;
    }

    /**
     * decrease the velocity of the flower-images
     *
     * @param vel difference to old velocity
     */
    public void decVelocity( final int vel )
    {
        if ( yinc > 1 )
        {
            yinc -= vel;
        }
    }

    /**
     * update the position of the flower-images
     *
     * @param g graphicsobject
     */
    public void update( final Graphics g )
    {
        if ( posY < h + blume.getIconHeight() )
        {
            posY += yinc;
        }
        else
        {
            posX = ( Math.random() * w );
            posY = -blume.getIconHeight();
        }
    }

    /**
     * Dpaint the flower-images
     *
     * @param g graphicsobject
     */
    public void paint( final Graphics g )
    {
        g.drawImage( blume.getImage(), (int) posX, (int) posY, this );
    }

    /**
     * Returns the h.
     *
     * @return int
     */
    public int getH()
    {
        return h;
    }

    /**
     * Returns the w.
     *
     * @return int
     */
    public int getW()
    {
        return w;
    }

    /**
     * Returns the x.
     *
     * @return double
     */
    public double getPosX()
    {
        return posX;
    }

    /**
     * Returns the y.
     *
     * @return double
     */
    public double getPosY()
    {
        return posY;
    }

    /**
     * Sets the h.
     *
     * @param h The h to set
     */
    public void setH( final int h )
    {
        this.h = h;
    }

    /**
     * Sets the w.
     *
     * @param w The w to set
     */
    public void setW( final int w )
    {
        this.w = w;
    }

    /**
     * Sets the x.
     *
     * @param x The x to set
     */
    public void setX( final double x )
    {
        this.posX = x;
    }

    /**
     * Sets the y.
     *
     * @param y The y to set
     */
    public void setY( final double y )
    {
        this.posY = y;
    }

}
