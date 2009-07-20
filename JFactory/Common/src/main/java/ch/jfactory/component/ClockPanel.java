/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package ch.jfactory.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.log4j.Logger;

/**
 * Totally self-contained visual time component that displays an analogue time string.
 *
 * TODO: Setting opaque to false does not work really.
 *
 * @author Daniel Frey 31.07.2008 16:58:26
 */
public class ClockPanel extends JPanel implements ActionListener {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(ClockPanel.class);

    /** The offscreen image. */
    private Image offScreenImage;

    /** The offscreen graphics. */
    private Graphics2D offScreenGraphics;

    /** The date string. */
    private String date;

    /** The background timer. */
    private Timer timer;

    /** The date format. Parametrize if necessary... */
    private static final DateFormat formatter = new SimpleDateFormat("EEEE d.M.yyyy HH:mm:ss");

    /** Inits the clock object. */
    public ClockPanel() {
        init();
        start();
    }

    private void init() {
        setLayout(null);
        addNotify();
        initOffscreenImage();
    }

    /** Start the timer. */
    public void start() {
        if (timer == null) {
            timer = new Timer(1000, this);
            LOG.info("starting timer");
            timer.start();
        }
    }

    /** Stop the timer. */
    public void stop() {
        if (timer != null) {
            LOG.info("stopping timer");
            timer.stop();
            timer = null;
        }
    }

    /** {@inheritDoc} */
    public void actionPerformed(final ActionEvent actionEvent) {
        date = formatter.format(new Date());
        final Font font = getFont();
        setPreferredSize(new Dimension(getStringWidth(date), getStringHeigth(font)));
        invalidate();
        repaint();
    }

    /** {@inheritDoc} */
    public void paint(final Graphics graphics) {
        initOffscreenImage();
        if (offScreenGraphics != null && date != null) {
            offScreenGraphics.setColor(isOpaque() ? getBackground() : new Color(0, 0, 0, 0));
            offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());
            offScreenGraphics.setColor(getForeground());
            offScreenGraphics.setFont(getFont());
            offScreenGraphics.drawString(date, getXLoc(date), getYLoc());
            graphics.drawImage(offScreenImage, 0, 0, this);
        }
    }

    private void initOffscreenImage() {
        if (offScreenImage == null && getWidth() > 0 && getHeight() > 0) {
            offScreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
            offScreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            offScreenGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
    }

    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        resetOffScreenImage(size);
    }

    private void resetOffScreenImage(final Dimension size) {
        if (offScreenImage != null) {
            final int imageWidth = offScreenImage.getWidth(null);
            final int imageHeight = offScreenImage.getHeight(null);
            if (offScreenImage != null && (imageWidth != size.getWidth() || imageHeight != size.getHeight())) {
                offScreenImage = null;
            }
        }
    }

    private int getXLoc(final String text) {
        final int textWidth = getStringWidth(text);
        final int extraPixelsOnSides = getWidth() - textWidth;
        return extraPixelsOnSides / 2;
    }

    private int getYLoc() {
        final FontMetrics fontMetrics = getFontMetrics(getFont());
        return getHeight() - fontMetrics.getDescent();
    }

    private int getStringWidth(final String text) {
        final FontMetrics fontMetrics = getFontMetrics(getFont());
        return fontMetrics.stringWidth(text);
    }

    private int getStringHeigth(final Font font) {
        final FontMetrics fontMetrics = getFontMetrics(font);
        return fontMetrics.getHeight();
    }
}