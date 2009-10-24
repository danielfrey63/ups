/*
 * Herbar CD-ROM version 2
 *
 * FlyingEarth.java
 *
 * Created on 7. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.resource.ImageLocator;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Flying Background.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class FlyingEarth extends FlyingFlower {

    private int x, y;
    private int yinc;
    private int h = 470;
    private int w = 750;
    private Graphics g;
    private ImageIcon earth = ImageLocator.getIcon("bgCatch.jpg");


    /**
     * Constructor for the FlyingEarth object
     */
    public FlyingEarth() {
        x = 0;
        y = 0;
        yinc = 3;
    }


    /**
     * updates the position of the image of the grass
     *
     * @param g graphicobject
     */
    public void update(Graphics g) {
        if (y < h) {
            y += yinc;
        }
        else {
            y = 0;
        }
    }


    /**
     * paints the images of the grass
     *
     * @param g graphicobject
     */
    public void paint(Graphics g) {
        g.drawImage(earth.getImage(), x, y, this);
        g.drawImage(earth.getImage(), x, y - earth.getIconHeight(), this);
    }
}
