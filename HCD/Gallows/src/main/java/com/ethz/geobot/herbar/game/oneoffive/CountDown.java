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
package com.ethz.geobot.herbar.game.oneoffive;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * ScoreBar which represents right and wrong answers of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class CountDown extends Canvas
{
    private int answerRight;

    private int totalQuestions;

    private boolean showScoreBar = false;

    /** Cnstructor */
    public CountDown()
    {
        this.setSize( 102, 15 );
        this.setVisible( true );
    }

    /**
     * as soon as the game is started the scorebar is showed
     *
     * @param show true, if the scorebar is to show
     */
    public void setShowScoreBar( final boolean show )
    {
        this.showScoreBar = show;
    }

    /**
     * adjust the answer-values of the bar according score changes
     *
     * @param right total right answers
     * @param max   ??? Description of the Parameter ???
     */
    public void actualizeScoreBar( final int right, final int max )
    {
        this.answerRight = right;
        this.totalQuestions = max;
        repaint();
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        if ( showScoreBar )
        {
            final int xLeft = 0;
            final int yLeft = 0;
            g.setColor( new Color( 144, 144, 144 ) );
            g.fillRect( xLeft, yLeft, 100, 10 );
            g.setColor( new Color( 250, 220, 70 ) );
            if ( answerRight != totalQuestions )
            {
                g.fillRect( xLeft, yLeft, 100 - ( answerRight * ( 100 / totalQuestions ) ), 10 );
            }
            g.setColor( new Color( 29, 64, 13 ) );
            g.drawRect( xLeft, yLeft, 100, 10 );
        }
    }
}
