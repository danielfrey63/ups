package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * At the begin of the first game this panel is showed. it contains the roles.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class StartPanel extends Canvas {

    private ImageIcon blume = ImageLocator.getIcon("catcher_intro.jpg");
    private Color green;

    /**
     * Constructor
     *
     * @param green Color for the background
     */
    public StartPanel(Color green) {
        this.setSize(750, 470);
        this.green = green;
        this.setVisible(true);
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        //*
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0.5f * 72, -1.0f * 72, green,
                10.4f * 72, -1.0f * 72, new Color(220, 225, 80), false));
        g2.fillRect(0, 0, 750, 470);
        //
        g.drawImage(blume.getImage(), 0, 0, this);
        g.setColor(Color.yellow);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT1"), 40, 40);
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT2"), 40, 60);
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT3"), 40, 80);
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT4"), 40, 100);
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT5"), 40, 120);
        g.drawString(Strings.getString(Catcher.class, "CATCHER.TITLE.TEXT6"), 40, 140);
    }
}
