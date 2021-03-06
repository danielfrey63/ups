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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * ScoreBar which represents right and wrong answers of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class ScoreBar extends Canvas
{
    private int answerRight;

    private int answerWrong;

    private boolean showScoreBar = false;

    private final Color wiese;

    /**
     * Constructor
     *
     * @param wiese color of the background
     */
    public ScoreBar( final Color wiese )
    {
        this.setSize( 140, 300 );
        this.setVisible( true );
        this.wiese = wiese;
        init();
    }

    /**
     * sets the scorebar to visible
     *
     * @param show true, if scorebar is to show
     */
    public void setShowScoreBar( final boolean show )
    {
        this.showScoreBar = show;
    }

    /** initialization of right and wrong answers, repaints */
    public void init()
    {
        answerRight = 0;
        answerWrong = 0;
        repaint();
    }

    /**
     * adjust the answer-values of the bar according score changes
     *
     * @param right total right answers
     * @param wrong total wrong answers
     */
    public void actualizeScoreBar( final int right, final int wrong )
    {
        this.answerRight = right;
        this.answerWrong = wrong;
        repaint();
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(
                new GradientPaint(
                        0.0f * 72, 0.0f * 72, wiese,
                        0.0f * 72, 6.5f * 72, new Color( 120, 240, 70 ), false ) );
        g2.fillRect( 0, 0, 750, 470 );

        if ( showScoreBar )
        {
            final int xLeft = 15;
            final int yLeft = 50;
            g.setColor( new Color( 144, 144, 144 ) );
            g.fillRect( xLeft, 0, 10, 100 );
            g.setColor( new Color( 250, 75, 75 ) );
            g.fillRect( xLeft, yLeft, 10, answerWrong * 5 );
            g.setColor( new Color( 50, 200, 50 ) );
            g.fillRect( xLeft, yLeft - ( answerRight * 5 ), 10, answerRight * 5 );
            g.setColor( new Color( 29, 64, 13 ) );
            g.drawRect( xLeft, 0, 10, 100 );
        }
    }
}
