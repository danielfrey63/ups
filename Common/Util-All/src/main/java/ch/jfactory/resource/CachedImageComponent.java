/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.lang.ref.SoftReference;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.apache.log4j.Category;


public class CachedImageComponent extends JComponent implements AsynchronPictureLoaderListener {
    private static final Category cat = Category.getInstance(CachedImageComponent.class);
    private static Dimension minimumSize = new Dimension(50, 50);
    private PictureCache cache;
    private CachedImage img;
    private boolean thumbNail;
    private SoftReference image;
    private double zoomFaktor = 1.0;
    /**
     * Size which represents a zooming factor of 1.
     */
    private int defaultSize = 0;
    private Dimension size;
    public static Border BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    /*BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(Color.red,1),
           BorderFactory.createEmptyBorder(10, 10, 10, 10)
         );*/

    public CachedImageComponent(final PictureCache c, final int size) {
        this(c);
        this.setBorder(BORDER);
        defaultSize = size;
    }

    public double getZoomFaktor() {
        return zoomFaktor;
    }

    public CachedImageComponent(final PictureCache c) {
        cache = c;
    }

    public synchronized void setImage(final String name, final boolean thumb) {
        CachedImage im = null;
        if (name != null) {
            im = cache.addCachedImage(name);
        }
        boolean revalidate = false;
        if (img != null) {
            img.detach(this);
            revalidate = true;
        }
        img = im;
        image = null;
        size = null;
        if (img != null) {
            img.attach(this);
        }
        this.thumbNail = thumb;
        if (revalidate) {
            this.redoLayout();
        }
        repaint();
    }

    public synchronized void setZoomFaktor(final double d) {
        size = null;
        zoomFaktor = d;
        redoLayout();
    }

    public CachedImage getImage() {
        return img;
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public synchronized Dimension getPreferredSize() {
        super.getPreferredSize();
        if (img == null) {
            return minimumSize;
        }
        if (size != null) {
            return size;
        }
        size = new Dimension();
        final Insets i = getInsets();
        final int w = i.right + i.left;
        final int h = i.top + i.bottom;
        final Dimension s = img.getSize();
        double factor = zoomFaktor;
        if (defaultSize > 0) {
            factor *= defaultSize / Math.max(s.getWidth(), s.getHeight());
        }
        size.setSize((s.width * factor) + w, (s.height * factor) + h);
        return size;
    }

    private void loadImage() {
        if (img == null) {
            return;
        }
        if ((image == null) || (image.get() == null)) {
            if (!img.loaded(thumbNail)) {
                if (img.loaded(true)) {
                    image = new SoftReference(img.getImage(true));
                }
                cache.cacheImage(img.getName(), thumbNail, true);
            }
            else {
                image = new SoftReference(img.getImage(thumbNail));
            }
        }
    }

    public void redoLayout() {
        this.revalidate();
        //this.repaint();
    }


    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        loadImage();
        final Insets i = getInsets();
        final int w = i.right + i.left;
        final int h = i.top + i.bottom;
        Image im = (image == null) ? null : (Image) image.get();
        if (im != null) {
            final Dimension p = getPreferredSize();
            final double wi = p.width - w / 2;
            final double he = p.height - h / 2;
            final int iw = im.getWidth(null);
            final int ih = im.getHeight(null);
            final double f = (double) PictureConverter.getFittingFactor((int) (wi), (int) (he), iw, ih);
            // Use time consuming smooth scaling for small images
            if (f == 1) {
                // Todo: store scaled image back
            }
            else if (f < 0.2) {
                im = im.getScaledInstance((int) (iw * f), (int) (ih * f), Image.SCALE_SMOOTH);
                g.drawImage(im, 0, 0, null);
            }
            else {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                final AffineTransform trans = new AffineTransform();
                trans.scale(f, f);
                trans.translate(w / 2, h / 2);
                ((Graphics2D) g).drawImage(im, trans, null);
            }
        }
    }

    public synchronized void loadFinished(final String name, final Image img, final boolean thumb) {
        cat.debug("Picture " + name + " loadFinished");
        boolean ok = false;
        size = null;
        ok = (image == null);
        ok = ok || (image.get() == null);
        ok = ok || (image.get() != img);
        if (ok) {
            image = null;
            repaint();
        }
    }

    public synchronized void loadAborted(final String name) {
        image = null;
        size = null;
        repaint();
    }

    public void loadStarted(final String name) {
    }

}
