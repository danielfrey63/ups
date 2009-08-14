/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import org.apache.log4j.Category;

/**
 * This class is used to load Image asynchronously. Use the AsynchronPictureLoaderListener to get notification of the
 * loading process.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
class AsynchronPictureLoader extends Thread implements IIOReadUpdateListener, IIOReadProgressListener, AsynchronPictureLoaderSupport {
    /**
     * category information for logging
     */
    private static final Category CAT = Category.getInstance(AsynchronPictureLoader.class.getName());

    /**
     * if running is set to false, the thread goes down
     */
    private boolean running = true;

    /**
     * url to the image which should be loaded
     */
    private String pictureURL = ""; //$NON-NLS-1$

    /**
     * image reader
     */
    private ImageReader reader = null;

    private AbstractAsynchronPictureLoaderSupport
            asynchronPictureLoaderSupport =
            new AbstractAsynchronPictureLoaderSupport();

    /**
     * loads a image from the given url.
     *
     * @param name url to the picture
     */
    public synchronized void loadImage(final String name) {
        this.pictureURL = name;
        notify();
        Thread.yield();
    }

    /**
     * abort asynchron image loading
     */
    public void abort() {
        if (reader != null) {
            CAT.info("abort() image loading aborted");
            reader.abort();
        }
    }

    /**
     * terminate image loader thread
     */
    public synchronized void terminate() {
        abort();
        running = false;
        notify();
    }

    /**
     * asynchron loader...
     */
    public void run() {
        CAT.info("AsynchronPictureLoader thread started");
        synchronized (this) {
            while (running) {
                try {
                    if ("".equals(pictureURL)) {
                        CAT.info("run() wait for image");
                        wait();
                    }

                    CAT.info("loading of picture " + pictureURL + " initiate.");
                    PictureLoader.load(pictureURL, false);
                    pictureURL = "";
                }
                catch (Exception ex) {
                    CAT.error("Loading of image " + pictureURL + " failed.", ex);
                }
            }
        }
        CAT.info("AsynchronPictureLoader thread stopped");
    }

    public void imageUpdate(final ImageReader imageReader, final BufferedImage
            bufferedImage, final int param, final int param3, final int param4,
                           final int param5, final int param6, final int param7, final int[] values) {
    }

    public void passComplete(final ImageReader imageReader,
                             final BufferedImage bufferedImage) {

        CAT.info("loading of picture " + pictureURL + " finished.");
        informFinished(pictureURL, bufferedImage, false);
    }

    public void passStarted(final ImageReader imageReader,
                            final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                            final int param5, final int param6, final int param7, final int param8, final int[] values) {
    }

    public void thumbnailPassComplete(final ImageReader imageReader,
                                      final BufferedImage bufferedImage) {
    }

    public void thumbnailPassStarted(final ImageReader imageReader,
                                     final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                     final int param5, final int param6, final int param7, final int param8, final int[] values) {
    }

    public void thumbnailUpdate(final ImageReader imageReader,
                                final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                final int param5, final int param6, final int param7, final int[] values) {
    }

    public void imageComplete(final ImageReader imageReader) {
    }

    public void imageProgress(final ImageReader imageReader, final float param) {
    }

    public void imageStarted(final ImageReader imageReader, final int param) {
        CAT.info("loading of picture " + pictureURL + " started.");
        informStarted(pictureURL);
    }

    public void readAborted(final ImageReader imageReader) {
        CAT.info("loading of picture " + pictureURL + " aborted.");
        informAborted(pictureURL);
    }

    public void sequenceComplete(final ImageReader imageReader) {
    }

    public void sequenceStarted(final ImageReader imageReader, final int param) {
    }

    public void thumbnailComplete(final ImageReader imageReader) {
    }

    public void thumbnailProgress(final ImageReader imageReader, final float param) {
    }

    public void thumbnailStarted(final ImageReader imageReader, final int param,
                                 final int param2) {
    }

    public void informFinished(final String name, final Image image, final boolean thumb) {
        asynchronPictureLoaderSupport.informFinished(name, image, thumb);
    }

    public void informAborted(final String name) {
        asynchronPictureLoaderSupport.informAborted(name);
    }

    public void informStarted(final String name) {
        asynchronPictureLoaderSupport.informStarted(name);
    }

    public void detach(final AsynchronPictureLoaderListener listener) {
        asynchronPictureLoaderSupport.detach(listener);
    }

    public void attach(final AsynchronPictureLoaderListener listener) {
        asynchronPictureLoaderSupport.attach(listener);
    }
}

// $Log: AsynchronPictureLoader.java,v $
// Revision 1.2  2006/03/14 21:27:55  daniel_frey
// *** empty log message ***
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/07/22 13:00:09  daniel_frey
// *** empty log message ***
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.3  2002/08/02 00:42:20  Dani
// Optimized import statements
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
