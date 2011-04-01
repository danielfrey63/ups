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
 * Flying grass images.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingGras extends FlyingFlower
{
    private double x, y, yinc;

    private Graphics g;

    private ImageIcon gras = new ImageIcon();

    /** Constructor for the FlyingGras object */
    public FlyingGras()
    {
        final int randomGif = (int) ( Math.random() * 3 );
        gras = ImageLocator.getIcon( "grasUnten_" + randomGif + ".gif" );

        //random initial coordinates within the panel bounderies
        x = ( Math.random() * getW() );
        y = ( Math.random() * getH() );

        yinc = ( Math.random() * 0.5 ) + 3.0;
    }

    /**
     * increase the velocity of the grass-images
     *
     * @param vel difference to old velocity
     */
    public void incVelocity( final int vel )
    {
        yinc += vel;
    }

    /**
     * decrease the velocity of the grass-images
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
     * Description of the Method
     *
     * @param g Description of the Parameter
     */
    public void update( final Graphics g )
    {
        if ( y < getH() + gras.getIconHeight() )
        {
            y += yinc;
        }
        else
        {
            x = ( Math.random() * getW() );
            y = -gras.getIconHeight();
        }
    }

    /**
     * Description of the Method
     *
     * @param g Description of the Parameter
     */
    public void paint( final Graphics g )
    {
        g.drawImage( gras.getImage(), (int) x, (int) y, this );
    }
}
