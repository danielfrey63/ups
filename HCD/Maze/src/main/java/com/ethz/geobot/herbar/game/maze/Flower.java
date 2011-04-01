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
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.resource.ImageLocator;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * flowers distributed in the maze.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class Flower extends Canvas
{
    private int[] iFlowers = new int[]{0};

    private final int n;

    private final int d;

    /**
     * Constructor
     *
     * @param n number of flowers which fall down
     * @param d position in x-direction wher the flower starts falling
     */
    public Flower( final int n, final int d )
    {
        this.n = n;
        this.d = d;
    }

    /**
     * initializes the array of flowers
     *
     * @param iFlowers sets the array
     */
    public void init( final int[] iFlowers )
    {
        this.iFlowers = iFlowers;
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        ImageIcon flower = null;
        for ( int i = 0; i < iFlowers.length; i++ )
        {
            for ( int ii = 0; ii < 6; ii++ )
            {
                if ( i % 6 == ii )
                {
                    flower = getImageIcon( ii );
                }
            }
            g.drawImage( flower.getImage(), ( iFlowers[i] % n ) * d + ( (
                    d - flower.getIconHeight() ) / 2 ) + 1, ( (int) Math.floor( iFlowers[i] / n ) ) * d + ( ( d - flower.getIconHeight() ) / 2 )
                    + 1, this );
        }

    }

    private ImageIcon getImageIcon( final int i )
    {
        return ImageLocator.getIcon( "labyFlower_" + i % 6 + ".gif" );
    }
}
