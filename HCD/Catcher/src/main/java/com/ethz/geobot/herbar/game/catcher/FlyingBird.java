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
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Flying Birds.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingBird extends FlyingFlower
{
    private double posX, posY;

    private double xinc;

    private final double yinc;

    private final int h = 470;

    private final int w = 750;

    private Graphics g;

    private ImageIcon bird = new ImageIcon();

    /** Constructor for the FlyingBird object */
    public FlyingBird()
    {
        final int randomGif = (int) ( Math.random() * 1 );
        bird = ImageLocator.getIcon( "vogel_" + randomGif + ".gif" );

        //random initial coordinates within the panel bounderies
        posX = ( Math.random() * w );
        posY = ( Math.random() * h );

        xinc = ( Math.random() * 3.5 ) + 3.0;
        yinc = ( Math.random() * 1.5 ) + 1.0;
    }

    /**
     * Gets the xPosBird attribute of the FlyingBird object
     *
     * @return The xPosBird value
     */
    public int getXPosBird()
    {
        return (int) ( posX + bird.getIconWidth() / 2 );
    }

    /**
     * Gets the yPosBird attribute of the FlyingBird object
     *
     * @return The yPosBird value
     */
    public int getYPosBird()
    {
        return (int) ( posY + bird.getIconHeight() / 2 );
    }

    /**
     * Description of the Method
     *
     * @param vel Description of the Parameter
     */
    public void incVelocity( final int vel )
    {
        xinc += vel;
    }

    /**
     * Description of the Method
     *
     * @param vel Description of the Parameter
     */
    public void decVelocity( final int vel )
    {
        if ( xinc > 1 )
        {
            xinc -= vel;
        }
    }

    /**
     * Description of the Method
     *
     * @param g Description of the Parameter
     */
    public void update( final Graphics g )
    {
        if ( posX < w + bird.getIconHeight() )
        {
            posX += xinc;
            posY += yinc;
        }
        else
        {
            posY = ( Math.random() * h ) - 2 * bird.getIconHeight();
            posX = ( Math.random() * w / 4 ) - 2 * bird.getIconWidth();
        }
    }

    /**
     * Description of the Method
     *
     * @param g Description of the Parameter
     */
    public void paint( final Graphics g )
    {
        g.drawImage( bird.getImage(), (int) posX, (int) posY, this );
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
