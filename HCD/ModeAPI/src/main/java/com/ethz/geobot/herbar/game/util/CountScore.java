/*
 * Herbar CD-ROM version 2
 *
 * CountScore.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
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
