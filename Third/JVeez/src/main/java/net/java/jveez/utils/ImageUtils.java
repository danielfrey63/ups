/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.utils;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

public class ImageUtils {

    private static final Logger LOG = Logger.getLogger(ImageUtils.class);
    private static final boolean DEBUG = LOG.isDebugEnabled();

    private final static GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    private static final IIOReadProgressListener iioReadProgressListener = new CancellableIIOReadProgressListener();

    public static GraphicsConfiguration getGraphicsconfiguration() {
        return graphicsConfiguration;
    }

    public static BufferedImage loadImage(Picture picture) {
        DebugUtils.ensureIsNotDispatchThread();

        int retryCounter = 2;
        while (retryCounter > 0) {
            try {
                return _loadImage(picture);
            }
            catch (OutOfMemoryError e) {
                LOG.warn("Out of memory error detected ! Retrying !", e);
                Utils.freeMemory(retryCounter < 1);
                retryCounter--;
            }
            catch (OperationCancelledException e) {
                LOG.info("Thread has been interrupted while loading the picture \"" + picture + "\"");
                Thread.currentThread().interrupt();
                return null;
            }
            catch (Throwable t) {
                LOG.error("Could not load the image", t);
                return null;
            }
        }
        LOG.error("Could not load the image because of memory constraints !");
        return null;
    }

    private static BufferedImage _loadImage(Picture picture) throws IOException {
        BufferedImage bufferedImage = null;

        // detect image format ...
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(picture.getFile());
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        ImageReader reader = (iterator.hasNext() ? iterator.next() : null);
        if (reader == null) {
            LOG.warn("No image reader found for " + picture);
            return null;
        }
        reader.addIIOReadProgressListener(iioReadProgressListener);
        reader.setInput(imageInputStream);
        bufferedImage = reader.read(0);

        imageInputStream.close();

        return bufferedImage;
    }

    public static BufferedImage loadImage(byte[] imageData) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
            // here, we will copy the (unmanaged) image from ImageIO into a compatible (managed) image to ensure best performance
            return copyIntoCompatibleImage(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
        }
        catch (Throwable t) {
            LOG.error("Could not load the image", t);
            return null;
        }
    }

    private static void close(ImageOutputStream output) {
        if (output != null) {
            try {
                output.close();
            }
            catch (IOException e) {
                LOG.warn("Could not close the output stream", e);
            }
        }
    }

    public static BufferedImage copyIntoCompatibleImage(Image image, int width, int height) {
        if (image == null) {
            return null;
        }

        BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage(width, height);
        Graphics2D g2d = compatibleImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return compatibleImage;
    }

    public static BufferedImage createScaledImage(BufferedImage image, int maximumWidth, int maximumHeight) {
        final double imageWidth = image.getWidth();
        final double imageHeight = image.getHeight();
        return createScaledImage(image, getScaleFactor(maximumWidth, imageWidth, maximumHeight, imageHeight));
    }

    private static double getScaleFactor(int maximumWidth, double imageWidth, int maximumHeight, double imageHeight) {
        return Math.min((double) maximumWidth / imageWidth, (double) maximumHeight / imageHeight);
    }

    private static BufferedImage createScaledImage(BufferedImage image, double scale) {
        if (scale == 1.0) {
            return image;
        }

        final double imageWidth = image.getWidth();
        final double imageHeight = image.getHeight();
        final int reducedWidth = (int) (imageWidth * scale);
        final int reducedHeight = (int) (imageHeight * scale);

        BufferedImage reducedImage = graphicsConfiguration.createCompatibleImage(reducedWidth, reducedHeight);
        Graphics2D g2d = reducedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, AffineTransform.getScaleInstance(scale, scale), null);
        g2d.dispose();

        return reducedImage;
    }

    public static BufferedImage transformImage(BufferedImage image, boolean rotate, boolean clockWise, boolean flipAroundX, boolean flipAroundY) {
        if (!rotate && !flipAroundX && !flipAroundY) {
            return image;
        }

        int targetWith = (rotate ? image.getHeight() : image.getWidth());
        int targetHeight = (rotate ? image.getWidth() : image.getHeight());

        AffineTransform transform = new AffineTransform();

        if (flipAroundX) {
            transform.translate(0, targetHeight);
            transform.scale(1, -1);
        }
        if (flipAroundY) {
            transform.translate(targetWith, 0);
            transform.scale(-1, 1);
        }
        if (rotate) {
            if (clockWise) {
                transform.translate(image.getHeight(), 0);
                transform.rotate(Math.PI / 2);
            }
            else {
                transform.translate(0, image.getWidth());
                transform.rotate(-Math.PI / 2);
            }
        }

        BufferedImage copy = graphicsConfiguration.createCompatibleImage(targetWith, targetHeight);
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(image, transform, null);
        g2d.dispose();

        return copy;
    }

    public static byte[] getImageBytes(BufferedImage bufferedImage, String mimeType) {
        long start = System.currentTimeMillis();
        ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType).next();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageOutputStream output = new MemoryCacheImageOutputStream(bos);

        try {
            writer.setOutput(output);
            writer.write(bufferedImage);
            LOG.debug("image stored in " + (System.currentTimeMillis() - start) + "ms");
        }
        catch (Exception e) {
            LOG.error("an exception has been caught while saving image as a byte array", e);
            return null;
        }
        finally {
            close(output);
        }
        return bos.toByteArray();
    }

    /**
     * @param picture the image picture to extract Exif metadata.
     * @return an <code>javax.swing.ImageIcon</code> that's represent a thumbnail from Exif metadata, and rotate it if
     *         necessary. null if the image don't support full Exif.
     */
    public static BufferedImage loadThumbnailFromEXIF(Picture picture) {
        if (picture == null) {
            if (DEBUG) {
                LOG.debug("No EXIF thumbnail for null picture");
            }
            return null;
        }

        final File file = picture.getFile();
        if (!Utils.isSupportedExifImage(file)) {
            if (DEBUG) {
                LOG.debug("EXIF thumbnail not supported by \"" + file + "\"");
            }
            return null;
        }

        try {
            final Metadata metadata = JpegMetadataReader.readMetadata(file);
            final ExifDirectory exifDirectory = (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
            final byte[] thumbnailBytes = exifDirectory.getThumbnailData();

            // some broken pictures have the thumbnail data EXIF tag but no data
            if (thumbnailBytes == null || thumbnailBytes.length == 0) {
                if (DEBUG) {
                    LOG.debug("EXIF tag for thumbnail but no data for \"" + file + "\"");
                }
                return null;
            }

            BufferedImage image = loadImage(thumbnailBytes);

            if (image == null) {
                LOG.info("Could not read the EXIF thumbnail data for \"" + file + "\"");
                return null;
            }
            else if (DEBUG) {
                LOG.debug("Thumbnail of size " + image.getWidth() + "x" + image.getHeight() +
                        " loaded successfully from EXIF data for \"" + file + "\"");
            }

            final int exifOrientation = (exifDirectory.containsTag(ExifDirectory.TAG_ORIENTATION) ? exifDirectory.getInt(ExifDirectory.TAG_ORIENTATION) : 0);

            if (exifOrientation > 1) {
                LOG.info("Correcting thumbnail rotation (" + exifOrientation + ")");

                //     1        2       3      4         5            6           7          8
                //
                //    888888  888888      88  88      8888888888  88                  88  8888888888
                //    88          88      88  88      88  88      88  88          88  88      88  88
                //    8888      8888    8888  8888    88          8888888888  8888888888          88
                //    88          88      88  88
                //    88          88  888888  888888

                switch (exifOrientation) {
                    case 2:
                        image = transformImage(image, false, false, false, true);
                        break;

                    case 3:
                        image = transformImage(image, false, false, true, true);
                        break;

                    case 4:
                        image = transformImage(image, false, false, true, false);
                        break;

                    case 5:
                        image = transformImage(image, true, true, false, true);
                        break;

                    case 6:
                        image = transformImage(image, true, true, false, false);
                        break;

                    case 7:
                        image = transformImage(image, true, false, false, true);
                        break;

                    case 8:
                        image = transformImage(image, true, false, false, false);
                        break;
                }
            }

            return image;
        }
        catch (Exception e) {
            LOG.warn("Thumbnail loading via EXIF data failed for \"" + file + "\"", e);
            return null;
        }
    }

    /**
     * Convenience method that returns a scaled instance of the provided {@code BufferedImage}. This method will use a
     * multi-step scaling technique that provides higher quality than the usual one-step technique (only useful in
     * down-scaling cases, where {@code targetWidth} or {@code targetHeight} is smaller than the original dimensions,
     * and generally only when the {@code BILINEAR} hint is specified)
     *
     * @param img          the original image to be scaled
     * @param targetWidth  the desired width of the scaled instance, in pixels
     * @param targetHeight the desired height of the scaled instance, in pixels
     * @param hint         one of the rendering hints that corresponds to {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *                     {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR}, {@code
     *                     RenderingHints.VALUE_INTERPOLATION_BILINEAR}, {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @return a scaled version of the original {@codey BufferedImage}
     */
    public static BufferedImage createScaledImage(BufferedImage img, int targetWidth, int targetHeight, Object hint) {
        final int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        // Use multi-step technique: start with original size, then
        // scale down in multiple passes with drawImage()
        // until the target size is reached
        final int imageWidth = img.getWidth();
        final int imageHeight = img.getHeight();
        final double scale = getScaleFactor(targetWidth, imageWidth, targetHeight, imageHeight);
        int width, height;
        if (hint == RenderingHints.VALUE_INTERPOLATION_BILINEAR && (imageHeight > targetHeight || imageWidth > targetWidth)) {
            final double itW = ((double) imageWidth) / ((double) targetWidth);
            final double itH = ((double) imageHeight) / ((double) targetHeight);
            if (itW > itH) {
                final int log = (int) (Math.log(itW) / Math.log(2) + 1);
                final int factor = (int) Math.pow(2, log);
                width = targetWidth * factor;
                height = ((int) ((double) targetWidth * imageHeight / imageWidth) * factor);
            }
            else {
                final int log = (int) (Math.log(itH) / Math.log(2) + 1);
                final int factor = (int) Math.pow(2, log);
                width = ((int) ((double) targetWidth * imageWidth / imageHeight) * factor);
                height = targetHeight * factor;
            }
        }
        else {
            if (imageWidth > imageHeight) {
                width = targetWidth;
                height = (int) (imageHeight * scale);
            }
            else {
                width = (int) (imageWidth * scale);
                height = targetHeight;
            }
        }

        BufferedImage ret = (BufferedImage) img;
        do {
            if (width > targetWidth) {
                width /= 2;
            }

            if (height > targetHeight) {
                height /= 2;
            }

            BufferedImage tmp = new BufferedImage(width, height, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, width, height, null);
            g2.dispose();

            ret = tmp;
        }
        while (width != targetWidth && height != targetHeight);

        return ret;
    }

    public static VolatileImage createVolatileImage(int width, int height) {
        return graphicsConfiguration.createCompatibleVolatileImage(width, height);
    }

    public static BufferedImage createScaledImage(BufferedImage image, double sx, double sy) {
        int scaledWidth = (int) Math.ceil(image.getWidth() * sx);
        int scaledHeight = (int) Math.ceil(image.getHeight() * sy);

        BufferedImage scaledImage = graphicsConfiguration.createCompatibleImage(scaledWidth, scaledHeight);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, AffineTransform.getScaleInstance(sx, sy), null);
        graphics2D.dispose();

        return scaledImage;
    }
}
