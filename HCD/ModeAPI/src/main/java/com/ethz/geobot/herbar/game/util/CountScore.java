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
package com.ethz.geobot.herbar.game.util;

import javax.swing.event.EventListenerList;

/**
 * Counts the score, the correct the false answers respectively.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:06 $
 */
public class CountScore implements Score
{
    private int rightScore;

    private int wrongScore;

    private int maxScore;

    private final EventListenerList eventListeners = new EventListenerList();

    public CountScore()
    {
    }

    public int getWrongScore()
    {
        return wrongScore;
    }

    public int getRightScore()
    {
        return rightScore;
    }

    public int getTotalScore()
    {
        final int total = rightScore - wrongScore;
        return total;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    public void addWrongScore( final int diff )
    {
        this.wrongScore += diff;
        fireScoreChanged();
    }

    public void addRightScore( final int diff )
    {
        this.rightScore += diff;
        fireScoreChanged();
    }

    public void init( final int initRightScoreValue, final int initWrongScoreValue, final int initMaxScore )
    {
        this.rightScore = initRightScoreValue;
        this.wrongScore = initWrongScoreValue;
        this.maxScore = initMaxScore;
        fireScoreChanged();
    }

    public void addScoreListener( final ScoreListener l )
    {
        this.eventListeners.add( ScoreListener.class, l );
    }

    public void removeScoreListener( final ScoreListener l )
    {
        this.eventListeners.remove( ScoreListener.class, l );
    }

    protected void fireScoreChanged()
    {
        final Object[] listeners = eventListeners.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if ( listeners[i] == ScoreListener.class )
            {
                ( (ScoreListener) listeners[i + 1] ).scoreChanged();
            }
        }
    }
}
