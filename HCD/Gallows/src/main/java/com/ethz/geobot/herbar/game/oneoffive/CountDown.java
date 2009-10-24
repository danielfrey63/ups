/*
 * Herbar CD-ROM version 2
 *
 * CountDown.java
 *
 * Created on 28. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import org.apache.log4j.Category;

/**
 * ScoreBar which represents right and wrong answers of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class CountDown extends Canvas {

    private static final Category CAT = Category.getInstance(CountDown.class);
    private int answerRight;
    private int totalQuestions;
    private boolean showScoreBar = false;

    /**
     * Cnstructor
     */
    public CountDown() {
        this.setSize(102, 15);
        this.setVisible(true);
    }

    /**
     * as soon as the game is started the scorebar is showed
     *
     * @param show true, if the scorebar is to show
     */
    public void setShowScoreBar(boolean show) {
        this.showScoreBar = show;
    }


    /**
     * adjust the answer-values of the bar according score changes
     *
     * @param right total right answers
     * @param max   ??? Description of the Parameter ???
     */
    public void actualizeScoreBar(int right, int max) {
        this.answerRight = right;
        this.totalQuestions = max;
        repaint();
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        if (showScoreBar) {
            int xLeft = 0;
            int yLeft = 0;
            g.setColor(new Color(144, 144, 144));
            g.fillRect(xLeft, yLeft, 100, 10);
            g.setColor(new Color(250, 220, 70));
            if (answerRight != totalQuestions) {
                g.fillRect(xLeft, yLeft, 100 - (answerRight * (100 / totalQuestions)), 10);
            }
            g.setColor(new Color(29, 64, 13));
            g.drawRect(xLeft, yLeft, 100, 10);
        }
    }
}
