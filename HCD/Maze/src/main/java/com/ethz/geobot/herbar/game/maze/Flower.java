/*
 * Herbar CD-ROM version 2
 *
 * Flower.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.resource.ImageLocator;
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * flowers distributed in the maze.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class Flower extends Canvas {

    private int[] iFlowers = new int[]{0};
    private int n;
    private int d;

    /**
     * Constructor
     *
     * @param n number of flowers which fall down
     * @param d position in x-direction wher the flower starts falling
     */
    public Flower(int n, int d) {
        this.n = n;
        this.d = d;
    }

    /**
     * initializes the array of flowers
     *
     * @param iFlowers sets the array
     */
    public void init(int[] iFlowers) {
        this.iFlowers = iFlowers;
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        ImageIcon flower = null;
        for (int i = 0; i < iFlowers.length; i++) {
            for (int ii = 0; ii < 6; ii++) {
                if (i % 6 == ii) {
                    flower = getImageIcon(ii);
                }
            }
            g.drawImage(flower.getImage(), (iFlowers[ i ] % n) * d + ((
                    d - flower.getIconHeight()) / 2) + 1, ((int) Math.floor(iFlowers[ i ] / n)) * d + ((d - flower.getIconHeight()) / 2)
                    + 1, this);
        }

    }

    private ImageIcon getImageIcon(int i) {
        ImageIcon flower = ImageLocator.getIcon("labyFlower_" + i % 6 + ".gif");
        return flower;
    }
}
