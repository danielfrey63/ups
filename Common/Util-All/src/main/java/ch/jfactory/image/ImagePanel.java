/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Panel holing an image. The image may be filling the panel or displayed in its original size. If displayed in its
 * original size, no adaptations are made during resize of the component. If the image doesn't fit the panel, it may be
 * draged with the mouse to reveal other parts of it. If filling is set to true, the image is resized to fill the
 * component.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class ImagePanel extends JPanel
{

    private Image image = DEFAULT_IMAGE;

    private Image display = image;

    private boolean fill = false;

    private Dimension lastComponentSize = getSize();

    private Point startPoint = new Point(0, 0);

    private Point imageZero = new Point(0, 0);

    private Point lastImageZero;

    private final MouseMotionAdapter MOTION_ADAPTER = new MouseMotionAdapter()
    {
        @Override
        public void mouseDragged(final MouseEvent e)
        {

            // Current diff
            final int dx = e.getPoint().x - startPoint.x;
            final int dy = e.getPoint().y - startPoint.y;

            // Translate image zero by the diff and consider the insets as bounds
            imageZero.x = lastImageZero.x + dx;
            imageZero.y = lastImageZero.y + dy;
            assureBounds();

            repaint();
        }
    };

    private final MouseAdapter MOUSE_ADAPTER = new MouseAdapter()
    {
        @Override
        public void mousePressed(final MouseEvent e)
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            startPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(final MouseEvent e)
        {
            setCursor(Cursor.getDefaultCursor());
            lastImageZero = new Point(imageZero);
        }
    };

    private static final String NO_IMAGE_STRING = "no image";

    private static final DefaultImage DEFAULT_IMAGE = new DefaultImage(new Dimension(500, 500), NO_IMAGE_STRING);

    private static final Dimension DEFAULT_NON_FILLED_PREFERRED_SIZE = new Dimension(100, 100);

    public ImagePanel()
    {
        this(null, false);
    }

    public ImagePanel(final Image image)
    {
        this(image, false);
    }

    public ImagePanel(final Image image, final boolean fill)
    {
        this.fill = fill;
        installListener();
        setImage(image);
        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(final ComponentEvent e)
            {
                setDisplayImage();
                // Just translate by the new size difference.
                final int nw = getWidth();
                final int nh = getHeight();
                final int ow = lastComponentSize.width;
                final int oh = lastComponentSize.height;
                imageZero.translate((nw - ow) / 2, (nh - oh) / 2);
                lastImageZero = new Point(imageZero);
                lastComponentSize = getSize();
                assureBounds();
                repaint();
            }
        });
    }

    public void setImage(final Image image)
    {
        if (image != null)
        {
            this.image = image;
            this.display = image;
            final int cw = getWidth();
            final int ch = getHeight();
            final int iw = display.getWidth(null);
            final int ih = display.getHeight(null);
            imageZero = new Point((cw - iw) / 2, (ch - ih) / 2);
            lastImageZero = new Point(imageZero);
            assureBounds();
        }
        else
        {
            this.image = DEFAULT_IMAGE;
        }
        setDisplayImage();
    }

    public Image getImage()
    {
        return display;
    }

    public void setFill(final boolean fill)
    {
        this.fill = fill;
        setDisplayImage();
        installListener();
    }

    public boolean getFill()
    {
        return fill;
    }

    public void paint(final Graphics g)
    {
        super.paint(g);
        if (display == null)
        {
            return;
        }
        drawImage(g, display);
    }

    public Dimension getPreferredSize()
    {
        return DEFAULT_NON_FILLED_PREFERRED_SIZE;
    }

    private void drawImage(final Graphics g, final Image image)
    {

        final Insets in = getInsets();
        // Display area width and height
        final int dw = getWidth() - in.left - in.right;
        final int dh = getHeight() - in.top - in.bottom;

        g.setClip(in.left, in.top, dw, dh);
        if (fill || image == DEFAULT_IMAGE)
        {
            // Image width and height
            final int iw = image.getWidth(null);
            final int ih = image.getHeight(null);
            g.drawImage(image, in.left + (dw - iw) / 2, in.top + (dh - ih) / 2, iw, ih, null);
        }
        else
        {
            g.drawImage(image, imageZero.x, imageZero.y, null);
        }
    }

    private void setDisplayImage()
    {
        if (fill)
        {
            final Insets in = getInsets();

            final int iw = image.getWidth(null);
            final int ih = image.getHeight(null);
            // Display area width and height
            final int dw = getWidth() - in.left - in.right;
            final int dh = getHeight() - in.top - in.bottom;
            final float fw = ((float) dw) / ((float) iw);
            final float fh = ((float) dh) / ((float) ih);
            final float f = Math.min(fw, fh);
            final BufferedImage bi = ImageUtils.createBufferedImage(image);
            if (f > 0)
            {
                display = ImageUtils.createHeadlessSmoothBufferedImage(bi, (int) (iw * f), (int) (ih * f));
            }
        }
        else
        {
            display = image;
        }
    }

    private void installListener()
    {
        if (fill)
        {
            removeMouseMotionListener(MOTION_ADAPTER);
            removeMouseListener(MOUSE_ADAPTER);
        }
        else
        {
            addMouseMotionListener(MOTION_ADAPTER);
            addMouseListener(MOUSE_ADAPTER);
        }
    }

    // Makes sure the image stays within the bounds of the insets. There is only one exception: The display area is too
    // small to show the entire image. Here the mouse may move the image beyound the inset bounds.
    private void assureBounds()
    {
        final int cw = getWidth();
        final int ch = getHeight();
        final int iw = display.getWidth(null);
        final int ih = display.getHeight(null);
        if (cw != 0 && ch != 0)
        {
            final Insets in = getInsets();
            if (cw >= iw + in.right + in.left)
            {
                imageZero.x = Math.min(Math.max(imageZero.x, in.left), cw - in.right - iw);
            }
            else
            {
                imageZero.x = Math.min(Math.max(imageZero.x, cw - in.right - iw), in.left);
            }
            if (ch >= ih + in.top + in.bottom)
            {
                imageZero.y = Math.min(Math.max(imageZero.y, in.top), ch - in.bottom - ih);
            }
            else
            {
                imageZero.y = Math.min(Math.max(imageZero.y, ch - in.bottom - ih), in.top);
            }
        }
    }

    public static void main(final String[] args) throws IOException
    {
        final String path = "E:/Beispiel-gross.jpg";
//        final String path = "E:/Beispiel-klein.jpg";
        final JFrame f = new JFrame();
        final ImagePanel panel = new ImagePanel(ImageUtils.createImage(path), false);
        panel.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(1, 1, 1, 1),
                new LineBorder(Color.GRAY, 1)), new EmptyBorder(1, 1, 1, 1)));
        f.add(panel, BorderLayout.CENTER);
        final JCheckBox cb = new JCheckBox("fill");
        cb.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                panel.setFill(cb.isSelected());
                panel.repaint();
            }
        });
        f.add(cb, BorderLayout.NORTH);
        f.setSize(400, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
