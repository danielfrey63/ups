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
 * Flying Background.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingEarth extends FlyingFlower
{
    private final int x;

    private int y;

    private final int yinc;

    private final int h = 470;

    private final int w = 750;

    private Graphics g;

    private final ImageIcon earth = ImageLocator.getIcon( "bgCatch.jpg" );

    /** Constructor for the FlyingEarth object */
    public FlyingEarth()
    {
        x = 0;
        y = 0;
        yinc = 3;
    }

    /**
     * updates the position of the image of the grass
     *
     * @param g graphicobject
     */
    public void update( final Graphics g )
    {
        if ( y < h )
        {
            y += yinc;
        }
        else
        {
            y = 0;
        }
    }

    /**
     * paints the images of the grass
     *
     * @param g graphicobject
     */
    public void paint( final Graphics g )
    {
        g.drawImage( earth.getImage(), x, y, this );
        g.drawImage( earth.getImage(), x, y - earth.getIconHeight(), this );
    }
}
