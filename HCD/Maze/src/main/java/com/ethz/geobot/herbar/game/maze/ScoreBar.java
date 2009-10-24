/*
 * Herbar CD-ROM version 2
 *
 * ScoreBar.java
 *
 * Created on 24. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.apache.log4j.Category;

/**
 * ScoreBar which represents right and wrong answers of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class ScoreBar extends Canvas {

    private static final Category CAT = Category.getInstance(ScoreBar.class);
    private int answerRight;
    private int answerWrong;
    private boolean showScoreBar = false;
    private Color wiese;

    /**
     * Constructor
     *
     * @param wiese color of the background
     */
    public ScoreBar(Color wiese) {
        this.setSize(140, 300);
        this.setVisible(true);
        this.wiese = wiese;
        init();
    }

    /**
     * sets the scorebar to visible
     *
     * @param show true, if scorebar is to show
     */
    public void setShowScoreBar(boolean show) {
        this.showScoreBar = show;
    }

    /**
     * initialization of right and wrong answers, repaints
     */
    public void init() {
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
    public void actualizeScoreBar(int right, int wrong) {
        this.answerRight = right;
        this.answerWrong = wrong;
        repaint();
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(
                new GradientPaint(
                        0.0f * 72, 0.0f * 72, wiese,
                        0.0f * 72, 6.5f * 72, new Color(120, 240, 70), false));
        g2.fillRect(0, 0, 750, 470);

        if (showScoreBar) {
            int xLeft = 15;
            int yLeft = 50;
            g.setColor(new Color(144, 144, 144));
            g.fillRect(xLeft, 0, 10, 100);
            g.setColor(new Color(250, 75, 75));
            g.fillRect(xLeft, yLeft, 10, answerWrong * 5);
            g.setColor(new Color(50, 200, 50));
            g.fillRect(xLeft, yLeft - (answerRight * 5), 10, answerRight * 5);
            g.setColor(new Color(29, 64, 13));
            g.drawRect(xLeft, 0, 10, 100);
        }
    }
}
