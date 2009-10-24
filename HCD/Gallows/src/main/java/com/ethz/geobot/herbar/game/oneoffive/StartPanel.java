/*
 * Herbar CD-ROM version 2
 *
 * StartPanel.java
 *
 * Created on 26. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * StartPanel with Help-textes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class StartPanel extends Canvas {

    private ImageIcon bird = ImageLocator.getIcon("hangman_intro.jpg");

    /**
     * Constructor
     */
    public StartPanel() {
        this.setSize(750, 470);
        this.setVisible(true);
    }

    /**
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        /**
         Graphics2D g2 = (Graphics2D) g;
         g2.setPaint(
         new GradientPaint(
         0.5f * 72, -1.0f * 72, new Color( 10, 10, 35 ),
         10.4f * 72, -1.0f * 72, new Color( 100, 215, 230 ), false ) );
         g2.fillRect( 0, 0, 750, 470 );
         */
        g.drawImage(bird.getImage(), 0, 0, this);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE1"), 280, 170);
        g.drawString(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE2"), 280, 190);
        g.drawString(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE3"), 280, 210);
        g.drawString(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE4"), 280, 230);
        g.drawString(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE5"), 280, 250);
    }
}
