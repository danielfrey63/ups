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

import ch.jfactory.color.ColorUtils;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;

/**
 * Utility methods for image processing.
 *
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2008/01/06 10:16:23 $
 */
public class ImageUtils {

    private static final boolean DEBUG = false;

    public static BufferedImage createBufferedImage(final Image image) {
        final BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        result.getGraphics().drawImage(image, 0, 0, null);
        return result;
    }

    public static BufferedImage createNormalizedImage(final BufferedImage image) throws ImageException {
        final int w = image.getWidth(null);
        final int[] pixels = getPixels(image);
        final double d = (double) getDarkestGray(pixels);
        final double f = 255d;
        final double factor = (f - d) / f;
        final int[] normalizedPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            final int pixel = pixels[i];
            final Color color = new Color(pixel, true);
            final double g = (double) color.getRed();
            if (color.getAlpha() > 0) {
                final int newGray = (int) Math.round((g - d) / factor);
                normalizedPixels[i] = new Color(newGray, newGray, newGray, color.getAlpha()).getRGB();
            } else {
                normalizedPixels[i] = color.getRGB();
            }
        }
        final BufferedImage newImage = createImageFromBuffer(normalizedPixels, w);
        if (DEBUG) {
            System.out.println("Normalized:");
        }
        if (DEBUG) {
            printImagePixels(newImage);
        }
        if (DEBUG) {
            debugIcon(newImage, "Normalized:");
        }
        return newImage;
    }

    public static BufferedImage createImageFromBuffer(final int[] buffer, final int w) {
        final int h = buffer.length / w;
        final Image intermediate = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, buffer, 0, w));
        final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        result.getGraphics().drawImage(intermediate, 0, 0, w, h, null);
        return result;
    }

    public static BufferedImage createGrayscaleImage(final Image image) throws ImageException {
        final int[] pixels = getPixels(image);
        System.out.println(pixels[0]);
        for (int i = 0; i < pixels.length; i++) {
            final int pixel = pixels[i];
            pixels[i] = ColorUtils.getGray(pixel);
        }
        final BufferedImage newImage = createImageFromBuffer(pixels, image.getWidth(null));
        if (DEBUG) {
            System.out.println("Grayscale:");
        }
        if (DEBUG) {
            printImagePixels(newImage);
        }
        if (DEBUG) {
            debugIcon(newImage, "Grayscale:");
        }
        return newImage;
    }

    public static BufferedImage createColorizedImage(final Color color, final int width, final int height) {
        final int[] buffer = new int[width * height];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0xFFFFFF;
        }
        final BufferedImage image = createImageFromBuffer(buffer, width);
        final BufferedImage result = createColorizedImage(image, color);
        if (DEBUG) {
            System.out.println("Original:");
        }
        if (DEBUG) {
            printImagePixels(image);
        }
        if (DEBUG) {
            debugIcon(image, "Original:");
        }
        if (DEBUG) {
            System.out.println("Colorized:");
        }
        if (DEBUG) {
            printImagePixels(result);
        }
        if (DEBUG) {
            debugIcon(result, "Colorized:");
        }
        return result;
    }

    /**
     * Creates an image by resizing the given image with the given factor. By default, a smooth rescaling algorithm is
     * used.
     *
     * @param image  the image to resize
     * @param color  the color to apply
     * @param factor the scaling factor
     * @return a colorized resized BufferedImage
     */
    public static BufferedImage createColorizedImage(final Image image, final Color color, final float factor) {
        final int width = image.getWidth(null);
        final int height = image.getHeight(null);
        final Image resized = image.getScaledInstance((int) (width * factor), (int) (height * factor), Image.SCALE_SMOOTH);
        try {
            waitImage(resized);
        }
        catch (ImageException e) {
            e.printStackTrace();
        }
        return createColorizedImage(resized, color);
    }

    public static BufferedImage createColorizedImage(final Image image, final Color color) {
        try {
            final int[] buffer = getPixels(image);
            final int[] colorized = createColorizedBuffer(buffer, color);
            final BufferedImage result = createImageFromBuffer(colorized, image.getWidth(null));
            if (DEBUG) {
                System.out.println("Colorized:");
            }
            if (DEBUG) {
                printImagePixels(result);
            }
            if (DEBUG) {
                debugIcon(result, "Colorized:");
            }
            return result;
        }
        catch (ImageException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printImagePixels(final Image image) {
        try {
            final StringBuffer b = new StringBuffer();
            final int[] pixels = getPixels(image);
            final int w = image.getWidth(null);
            for (int i = 0; i < w; i++) {
                final int h = image.getHeight(null);
                for (int j = 0; j < h; j++) {
                    final int pixel = pixels[w * i + j];
                    b.append(StringUtils.leftPad("" + net.infonode.util.ImageUtils.getRed(pixel), 3));
                    b.append(" ");
                    b.append(StringUtils.leftPad("" + net.infonode.util.ImageUtils.getGreen(pixel), 3));
                    b.append(" ");
                    b.append(StringUtils.leftPad("" + net.infonode.util.ImageUtils.getBlue(pixel), 3));
                    b.append(" ");
                    b.append(StringUtils.leftPad("" + net.infonode.util.ImageUtils.getAlpha(pixel), 3));
                    b.append(" ");
                    if (j < h - 1) {
                        b.append("| ");
                    }
                }
                b.append("\n");
            }
            System.out.println(b.toString());
        }
        catch (ImageException e) {
            e.printStackTrace();
        }
    }

    public static int getDarkestGray(final int[] pixels) {
        int darkest = 256;
        for (int i = 0; i < pixels.length; i++) {
            final int pixel = pixels[i];
            final Color color = new Color(pixel, true);
            final int red = color.getRed();
            if (color.getAlpha() > 0 && red < darkest) {
                darkest = red;
            }
        }
        return darkest;
    }

    /**
     * Creates a new pixel buffer with all grays replaced by the given color. The brightness of the original gray is
     * copied to the new color pixel. Alpha channel is preserved.
     *
     * @param pixels the pixels to translate
     * @param color  the replacment color
     * @return a new buffer
     */
    public static int[] createColorizedBuffer(final int[] pixels, final Color color) {
        final int[] result = new int[pixels.length];
        if (DEBUG) {
            printImagePixels(createImageFromBuffer(pixels, 16));
        }
        for (int i = 0; i < pixels.length; i++) {
            final Color pColor = new Color(pixels[i], true);
            final float[] pHSB = Color.RGBtoHSB(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), null);
            final float[] nHSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            final Color npColor = new Color(Color.HSBtoRGB(nHSB[0], pHSB[1], pHSB[2]));
            final Color nPixelColor = new Color(npColor.getRed(), npColor.getGreen(), npColor.getBlue(), pColor.getAlpha());
            result[i] = nPixelColor.getRGB();
        }
        return result;
    }

    private static void debugIcon(final BufferedImage image, final String message) {
        try {
            final File file = File.createTempFile("image", ".png");
            ImageIO.write(image, "png", file);
            System.out.println(message + file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the image from the given path.
     *
     * @param path either an absoute path in the classpath or a local file path
     * @return the image laoded
     * @throws java.io.IOException if a reading error occures
     */
    public static Image createImage(final String path) throws IOException {
        final URL url = ImageUtils.class.getResource(path);
        Image image = null;
        try {
            if (url != null) {
                image = create(url);
            } else if (new File(path).exists()) {
                image = create(path);
            } else {
                final URL url2 = new URL(path);
                image = create(url2);
            }
        }
        catch (ImageException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage createThumbnail(final String path, final int w, final int h) throws IOException {
        final URL url1 = ImageUtils.class.getResource(path);
        BufferedImage image = null;
        if (url1 != null) {
            image = createThumbnail(url1, w, h);
        } else if (new File(path).exists()) {
            image = createThumbnail(new File(path), w, h);
        } else {
            final URL url2 = new URL(path);
            image = createThumbnail(url2, w, h);
        }
        return image;
    }

    public static BufferedImage createThumbnail(final URL url, final int w, final int h) throws IOException {
        return createThumbnail(url.openStream(), w, h);
    }

    public static BufferedImage createThumbnail(final File file, final int w, final int h) throws IOException {
        return createThumbnail(new FileImageInputStream(file), w, h);
    }

    public static BufferedImage createThumbnail(final InputStream is, final int w, final int h) throws IOException {
        final File tempDir = new File(System.getProperty("java.io.tmpdir"));
        return createThumbnail(new FileCacheImageInputStream(is, tempDir), w, h);
    }

    /**
     * Creates a BufferedImage by loading the given stream. Closes the input stream.
     *
     * @param is the input stream
     * @param w  the width
     * @param h  the height
     * @return a buffered image
     * @throws IOException if an error occured during reading
     */
    public static BufferedImage createThumbnail(final ImageInputStream is, final int w, final int h) throws IOException {
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(is);
        final ImageReader reader = readers.next();
        reader.setInput(is, true, true);
        final BufferedImage image;
        if (reader.hasThumbnails(0)) {
            image = reader.readThumbnail(0, 0);
        } else {
            final float sumpling;
            final float THUMBNAIL_SIZE = 64;
            if (w > h) {
                sumpling = Math.max(1, w / THUMBNAIL_SIZE);
            } else {
                sumpling = Math.max(1, h / THUMBNAIL_SIZE);
            }

            final ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceSubsampling((int) sumpling, (int) sumpling, 0, 0);
            image = reader.read(0, param);
        }
        reader.dispose();
        is.close();
        return image;
    }

    /**
     * Builds one buffered image from an array of images by aligning them vertically.
     *
     * @param images the images to paint together
     * @return one new image including all images
     */
    public static BufferedImage getGhostImage(final BufferedImage[] images) {

        // Find broades component and total height
        double height = 0;
        double width = 0;
        for (int i = 0; i < images.length; i++) {
            final BufferedImage image = images[i];
            height += image.getHeight();
            width = Math.max(width, image.getWidth());
        }

        // Build the buffered image vertically and left aligned.
        final BufferedImage ghost = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics2D g2 = ghost.createGraphics();

        int y = 0;
        for (int i = 0; i < images.length; i++) {
            final BufferedImage image = images[i];
            g2.drawImage(image, null, 0, y);
            y += image.getHeight();
        }
        g2.dispose();

        return ghost;
    }

    /**
     * Calculates a buffered ghost like image from the component given. Make sure the component has already the correct
     * bounding size.
     *
     * @param comp the component to derive the image from
     * @return a buffered ghost like image of the component
     */
    public static BufferedImage getGhostImage(final JComponent comp) {
        // The layout manager would normally do this
        comp.setOpaque(false);
        final Color transparent = new Color(0, 0, 0, 255);
        comp.setBackground(transparent);
        final Rectangle r = comp.getBounds();

        // Get a buffered image of the selection for dragging a ghost image
        final BufferedImage ghost = new BufferedImage((int) r.getWidth(), (int) r.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics2D g2 = ghost.createGraphics();
        g2.setPaint(Color.white);
        g2.fillRect(r.x, r.y, r.width, r.height);

        // Make the image ghostlike
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        // Ask the cell renderer to paint itself into the BufferedImage
        comp.paint(g2);

        // Now paint a gradient UNDER the ghosted JLabel text (but not under the icon if any). Note: this will need
        // tweaking if your icon is not positioned to the left of the text
        final int nStartOfText;
        if (comp instanceof JLabel) {
            final JLabel label = (JLabel) comp;
            final Icon icon = label.getIcon();
            nStartOfText = (icon == null) ? 0 : icon.getIconWidth() + label.getIconTextGap();
        } else {
            nStartOfText = 0;
        }
        // Make the gradient ghostlike
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));
        final Color white = new Color(255, 255, 255, 0);
        g2.setPaint(new GradientPaint(nStartOfText, 0, SystemColor.controlShadow, ghost.getWidth(), 0, white));
        g2.fillRect(nStartOfText, 0, ghost.getWidth(), ghost.getHeight());
        g2.dispose();

        return ghost;
    }

    public static BufferedImage getBufferedImage(final Image image) {

        final BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        final Graphics g = bi.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bi;
    }

    public static BufferedImage createHeadlessSmoothBufferedImage(final BufferedImage source, final int width, final int height) {

        final int type = BufferedImage.TYPE_INT_RGB;
        final BufferedImage dest = new BufferedImage(width, height, type);

        int sourcex;
        int sourcey;

        final double scalex = (double) width / source.getWidth();
        final double scaley = (double) height / source.getHeight();

        int x1;
        int y1;

        double xdiff;
        double ydiff;

        int rgb;
        int rgb1;
        int rgb2;

        for (int y = 0; y < height; y++) {
            sourcey = y * source.getHeight() / dest.getHeight();
            ydiff = (y / scaley) - sourcey;

            for (int x = 0; x < width; x++) {
                sourcex = x * source.getWidth() / dest.getWidth();
                xdiff = (x / scalex) - sourcex;

                x1 = Math.min(source.getWidth() - 1, sourcex + 1);
                y1 = Math.min(source.getHeight() - 1, sourcey + 1);

                rgb1 = getRGBInterpolation(source.getRGB(sourcex, sourcey), source.getRGB(x1, sourcey), xdiff);
                rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source.getRGB(x1, y1), xdiff);

                rgb = getRGBInterpolation(rgb1, rgb2, ydiff);

                dest.setRGB(x, y, rgb);
            }
        }

        return dest;
    }

    private static int getRGBInterpolation(final int value1, final int value2, final double distance) {
        final int alpha1 = (value1 & 0xFF000000) >>> 24;
        final int red1 = (value1 & 0x00FF0000) >> 16;
        final int green1 = (value1 & 0x0000FF00) >> 8;
        final int blue1 = (value1 & 0x000000FF);

        final int alpha2 = (value2 & 0xFF000000) >>> 24;
        final int red2 = (value2 & 0x00FF0000) >> 16;
        final int green2 = (value2 & 0x0000FF00) >> 8;
        final int blue2 = (value2 & 0x000000FF);

        return ((int) (alpha1 * (1.0 - distance) + alpha2 * distance) << 24)
                | ((int) (red1 * (1.0 - distance) + red2 * distance) << 16)
                | ((int) (green1 * (1.0 - distance) + green2 * distance) << 8)
                | (int) (blue1 * (1.0 - distance) + blue2 * distance);
    }

    // From InfoNode ImageUtils
    public static Image create(final URL url) throws ImageException {
        final Image image = Toolkit.getDefaultToolkit().createImage(url);
        waitImage(image);
        return image;
    }

    public static Image create(final String filename) throws ImageException {
        final Image image = Toolkit.getDefaultToolkit().createImage(filename);
        waitImage(image);
        return image;
    }

    public static void waitImage(final Image image) throws ImageException {
        final MediaTracker tracker = new MediaTracker(new Canvas());
        tracker.addImage(image, 1);

        try {
            tracker.waitForID(1);
        }
        catch (InterruptedException e) {
        }
    }

    static class ImageException extends Exception {

        public ImageException() {
        }

        public ImageException(final String message) {
            super(message);
        }

        public ImageException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public ImageException(final Throwable cause) {
            super(cause);
        }
    }

    public static int[] getPixels(final Image image) throws ImageException {
        return getPixels(image, 0, 0, image.getWidth(null), image.getHeight(null));
    }

    public static int[] getPixels(final Image image, final int x, final int y, final int width, final int height) throws ImageException {
        final int[] pixels = new int[width * height];
        final PixelGrabber pg = new PixelGrabber(image, x, y, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            throw new ImageException("Image fetch aborted or errored");
        }

        return pixels;
    }

    /**
     * Convenience method that returns a scaled instance of the provided {@code BufferedImage}.
     *
     * @param img           the original image to be scaled
     * @param targetWidth   the desired width of the scaled instance, in pixels
     * @param targetHeight  the desired height of the scaled instance, in pixels
     * @param hint          one of the rendering hints that corresponds to {@code RenderingHints.KEY_INTERPOLATION}
     *                      (e.g. {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR}, {@code
     *                      RenderingHints.VALUE_INTERPOLATION_BILINEAR}, {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step scaling technique that provides higher quality
     *                      than the usual one-step technique (only useful in downscaling cases, where {@code
     *                      targetWidth} or {@code targetHeight} is smaller than the original dimensions, and generally
     *                      only when the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     * <p/><p/>
     * Origin: Chris Campbell http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
     */
    public BufferedImage getScaledInstance(final BufferedImage img, final int targetWidth, final int targetHeight, final Object hint,
                                           final boolean higherQuality) {
        final int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB :
                BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            final BufferedImage tmp = new BufferedImage(w, h, type);
            final Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
