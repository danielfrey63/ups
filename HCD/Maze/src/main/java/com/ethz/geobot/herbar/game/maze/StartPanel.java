/*
 * Herbar CD-ROM version 2
 *
 * StartPanel
 *
 * Created on 24. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

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
 * Panel which is shown at the start of the first game. contains the roles
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class StartPanel extends Canvas {

    private ImageIcon princess = ImageLocator.getIcon("maze_intro.jpg");
    private Color wiese;

    /**
     * Constructor
     *
     * @param wiese color for the background
     */
    public StartPanel(Color wiese) {
        this.setSize(470, 470);
        this.wiese = wiese;
        this.setVisible(true);
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0.0f * 72, 0.0f * 72, wiese,
                0.0f * 72, 6.5f * 72, new Color(120, 240, 70), false));
        g2.fillRect(0, 0, 750, 470);

        g.drawImage(princess.getImage(), 0, 0, this);
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT1"), 65, 60);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT2"), 65, 80);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT3"), 65, 100);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT4"), 65, 120);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT5"), 65, 140);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT6"), 65, 160);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT7"), 65, 180);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT8"), 65, 200);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT9"), 65, 220);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT10"), 65, 240);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT11"), 65, 260);
    }

    public void paintOld(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0.0f * 72, 0.0f * 72, wiese,
                0.0f * 72, 6.5f * 72, new Color(120, 240, 70), false));
        g2.fillRect(0, 0, 750, 470);

        g.drawImage(princess.getImage(), this.getWidth() / 2 -
                princess.getIconWidth() / 2, this.getHeight() / 2 + 10, this);

        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT1"), 100, 200);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT2"), 10, 30);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT3"), 10, 40);
        g.drawString(Strings.getString(MazePanel.class, "MAZE.TITLE.TEXT4"), 10, 50);
    }
}
