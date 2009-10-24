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
import org.apache.log4j.Category;

/**
 * Counts the score, the correct the false answers respectively.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:06 $
 */
public class CountScore implements Score {

    private final static Category cat = Category.getInstance(CountScore.class);
    private int rightScore;
    private int wrongScore;
    private int maxScore;
    private EventListenerList eventListeners = new EventListenerList();

    public CountScore() {
    }

    public int getWrongScore() {
        return wrongScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public int getTotalScore() {
        int total = rightScore - wrongScore;
        return total;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void addWrongScore(int diff) {
        this.wrongScore += diff;
        fireScoreChanged();
    }

    public void addRightScore(int diff) {
        this.rightScore += diff;
        fireScoreChanged();
    }

    public void init(int initRightScoreValue, int initWrongScoreValue, int initMaxScore) {
        this.rightScore = initRightScoreValue;
        this.wrongScore = initWrongScoreValue;
        this.maxScore = initMaxScore;
        fireScoreChanged();
    }

    public void addScoreListener(ScoreListener l) {
        this.eventListeners.add(ScoreListener.class, l);
    }

    public void removeScoreListener(ScoreListener l) {
        this.eventListeners.remove(ScoreListener.class, l);
    }

    protected void fireScoreChanged() {
        Object[] listeners = eventListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[ i ] == ScoreListener.class) {
                ((ScoreListener) listeners[ i + 1 ]).scoreChanged();
            }
        }
    }
}
