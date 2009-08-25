package net.java.jveez.ui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.java.jveez.utils.BlurUtils;
import org.apache.log4j.Logger;

public class BlurPanel extends JPanel {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(BlurPanel.class);

    private BufferedImage parentArea;
    private BufferedImage blurredArea;

    private Component blurSource;

    public BlurPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public BlurPanel() {
        setOpaque(false);
    }

    public void setBlurSource(Component blurSource) {
        this.blurSource = blurSource;
    }

    public void paint(Graphics g) {
//    System.out.println("PAINT !");

        int width = getWidth();
        int height = getHeight();

        if (parentArea == null || parentArea.getWidth() != width || parentArea.getHeight() != height) {
            LOG.info("Creating images " + width + "x" + height);
            parentArea = getGraphicsConfiguration().createCompatibleImage(width, height);
            blurredArea = getGraphicsConfiguration().createCompatibleImage(width, height);
        }

        if (blurSource != null) {
//      System.out.println("Copying parent area ...");

            // determine region to copy
            Point pSource = new Point(0, 0);
            Point pDest = new Point(0, 0);

            // move to screen coordinates
            SwingUtilities.convertPointToScreen(pSource, blurSource);
            SwingUtilities.convertPointToScreen(pDest, this);

            // calculate intersection
            Rectangle rSource = new Rectangle(pSource.x, pSource.y, blurSource.getWidth(), blurSource.getHeight());
            SwingUtilities.computeIntersection(pDest.x, pDest.y, width, height, rSource);

            // move back to component coordinates
            pSource.setLocation(rSource.x, rSource.y);
            SwingUtilities.convertPointFromScreen(pSource, blurSource);

            rSource.x = pSource.x;
            rSource.y = pSource.y;

            // rSource contains the source rectangle we want to copy

            Graphics2D graphics = parentArea.createGraphics();
            graphics.translate(-rSource.x, -rSource.y);
            blurSource.paint(graphics);
            graphics.dispose();

//      System.out.println("Creating blur ...");
//      BlurUtils.blur2(parentArea, blurredArea);
            BlurUtils.blur2(parentArea, blurredArea, 15);
            BlurUtils.blur2(blurredArea, parentArea, 15, Color.WHITE, 0.5f);

//      try
//      {
//        ImageIO.write(parentArea, "JPEG", new File("image.jpg"));
//      }
//      catch (IOException e)
//      {
//        e.printStackTrace();
//      }

//      System.out.println("Copy blur as background");
            g.drawImage(parentArea, 0, 0, this);
        }

        super.paint(g);
    }

}
