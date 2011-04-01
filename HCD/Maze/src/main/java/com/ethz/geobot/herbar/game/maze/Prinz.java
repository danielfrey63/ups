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
package com.ethz.geobot.herbar.game.maze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * the prince in form of a crone.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class Prinz extends Canvas
{
    private int i;

    private final int n;

    private final int d;

    private final Polygon polygonPrinz;

    /**
     * Constructor
     *
     * @param numOfSqrs number of squares of the maze
     * @param dimOfSqrs width, heitghs resp. of one square
     */
    public Prinz( final int numOfSqrs, final int dimOfSqrs )
    {
        this.n = numOfSqrs;
        this.d = dimOfSqrs;
        final int[] px = new int[]{d / 10, d / 3, d - d / 3, d - d / 10,
                d - 2 * d / 6, d / 2, d - 4 * d / 6};
        final int[] py = new int[]{d / 10, d - d / 10, d - d / 10, d / 10, 2 * d / 5,
                d / 10, 2 * d / 5};
        this.polygonPrinz = new Polygon( px, py, 7 );
        init();
    }

    /**
     * sets the new actual array-index of the prince's position
     *
     * @param iNeu array-index of the actual ocupied maze-square
     */
    public void setIndexArray( final int iNeu )
    {
        final int xOld = i % n * d;
        final int yOld = (int) Math.floor( i / n ) * d;
        this.i += iNeu;
        final int xNew = i % n * d;
        final int yNew = (int) Math.floor( i / n ) * d;
        translatePrinz( xNew - xOld, yNew - yOld );
    }

    /**
     * returns the actual array-index
     *
     * @return int
     */
    public int getIndexArray()
    {
        return this.i;
    }

    /** initializes the pirnce's position */
    public void init()
    {
        translatePrinz( -( i % n * d ), -( (int) Math.floor( i / n ) * d ) );
        this.i = 0;
    }

    /** @see Component#update(Graphics) */
    public void update( final Graphics g )
    {
        paint( g );
    }

    /**
     * Method paint.
     *
     * @param g     Graphics object
     * @param color color of the prince's crone
     */
    public void paint( final Graphics g, final Color color )
    {
        g.setColor( color );
        g.fillPolygon( polygonPrinz );
    }

    private void translatePrinz( final int dx, final int dy )
    {
        polygonPrinz.translate( dx, dy );
    }
}
