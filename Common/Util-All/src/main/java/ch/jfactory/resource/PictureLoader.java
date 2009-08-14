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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.stream.ImageInputStream;
import org.apache.log4j.Logger;

public class PictureLoader
{
    private static final Dimension NULLDIMENSION = new Dimension(0, 0);

    private static final Logger LOGGER = Logger.getLogger(PictureLoader.class);

    public static Image load(final String pictureURL, final boolean thumb)
    {
        return load(pictureURL, null, null, thumb);
    }

    public static Image load(final String pictureURL, final IIOReadProgressListener read, final IIOReadUpdateListener update,
                             final boolean thumb)
    {
        try
        {
            LOGGER.info("loading of picture " + pictureURL + " initiate.");
            final java.io.File file = new java.io.File(pictureURL);
            if (file.exists())
            {
                final ImageInputStream iis = ImageIO.createImageInputStream(file);
                ImageReader reader = (ImageReader) ImageIO.getImageReaders(iis).next();
                reader.setInput(iis);
                if (read != null)
                {
                    reader.addIIOReadProgressListener(read);
                }
                if (update != null)
                {
                    reader.addIIOReadUpdateListener(update);
                }
                Image image = null;
                if (thumb)
                {
                    final ImageReadParam param = new ImageReadParam();
                    param.setSourceSubsampling(2, 2, 0, 0);
                    image = reader.read(0, param);
                }
                else
                {
                    image = reader.read(0);
                }
                reader.dispose();
                reader = null;
                iis.close();
                return image;
            }
            else
            {
                LOGGER.warn("No Image at " + pictureURL);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            LOGGER.warn("Loading of image " + pictureURL + " failed.", ex);
        }
        return null;
    }

    public static Dimension getSize(final String pictureURL)
    {
        ImageReader reader = null;
        ImageInputStream iis = null;
        try
        {
            final java.io.File file = new java.io.File(pictureURL);
            if (file.exists())
            {
                iis = ImageIO.createImageInputStream(file);
                reader = (ImageReader) ImageIO.getImageReaders(iis).next();
                reader.setInput(iis);
                final Dimension dim = new Dimension();
                dim.width = reader.getWidth(0);
                dim.height = reader.getHeight(0);
                reader.dispose();
                iis.close();
                return dim;
            }
            else
            {
                final String message = "Size of image " + file.getAbsoluteFile() + " is null because, file does not exist";
                final IllegalStateException e = new IllegalStateException(message);
                LOGGER.error(message, e);
                throw e;
            }
        }
        catch (IOException e)
        {
        }
        catch (RuntimeException ex)
        {
            LOGGER.error("Error while processing image " + pictureURL);
            throw ex;
        }
        finally
        {
            reader = null;
            iis = null;
        }
        return NULLDIMENSION;
    }

    static class IOImageListener implements IIOReadUpdateListener, IIOReadProgressListener
    {
        private AsynchronPictureLoaderListener listener;

        private String pictureURL;

        private boolean thumb;

        public IOImageListener(final AsynchronPictureLoaderListener listener, final String pictureURL, final boolean thumb)
        {
            this.listener = listener;
            this.pictureURL = pictureURL;
            this.thumb = thumb;
        }

        public void imageUpdate(final ImageReader imageReader, final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                final int param5, final int param6, final int param7, final int[] values)
        {
        }

        public void passComplete(final ImageReader imageReader, final BufferedImage bufferedImage)
        {
            LOGGER.info("loading of picture " + pictureURL + " finished.");
            if (listener != null)
            {
                listener.loadFinished(pictureURL, bufferedImage, thumb);
            }
        }

        public void passStarted(final ImageReader imageReader, final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                final int param5, final int param6, final int param7, final int param8, final int[] values)
        {
        }

        public void thumbnailPassComplete(final ImageReader imageReader, final BufferedImage bufferedImage)
        {
        }

        public void thumbnailPassStarted(final ImageReader imageReader, final BufferedImage bufferedImage, final int param, final int param3,
                                         final int param4, final int param5, final int param6, final int param7, final int param8, final int[] values)
        {
        }

        public void thumbnailUpdate(final ImageReader imageReader, final BufferedImage bufferedImage, final int param, final int param3,
                                    final int param4, final int param5, final int param6, final int param7, final int[] values)
        {
        }

        public void imageComplete(final ImageReader imageReader)
        {
        }

        public void imageProgress(final ImageReader imageReader, final float param)
        {
        }

        public void imageStarted(final ImageReader imageReader, final int param)
        {
            LOGGER.info("loading of picture " + pictureURL + " started.");
            if (listener != null)
            {
                listener.loadStarted(pictureURL);
            }
        }

        public void readAborted(final ImageReader imageReader)
        {
            LOGGER.info("loading of picture " + pictureURL + " aborted.");
            if (listener != null)
            {
                listener.loadAborted(pictureURL);
            }
        }

        public void sequenceComplete(final ImageReader imageReader)
        {
        }

        public void sequenceStarted(final ImageReader imageReader, final int param)
        {
        }

        public void thumbnailComplete(final ImageReader imageReader)
        {
        }

        public void thumbnailProgress(final ImageReader imageReader, final float param)
        {
        }

        public void thumbnailStarted(final ImageReader imageReader, final int param, final int param2)
        {
        }

    }

}