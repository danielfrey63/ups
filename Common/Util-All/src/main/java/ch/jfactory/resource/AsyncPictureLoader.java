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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to load Image asynchronously. Use the AsyncPictureLoaderListener to get notification of the
 * loading process.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
class AsyncPictureLoader extends Thread implements IIOReadUpdateListener, IIOReadProgressListener, AsyncPictureLoaderSupport
{
    /** Category information for logging. */
    private static final Logger LOGGER = LoggerFactory.getLogger( AsyncPictureLoader.class.getName() );

    /** If running is set to false, the thread goes down. */
    private boolean running = true;

    /** URL to the image which should be loaded. */
    private String pictureURL = "";

    /** Image reader. */
    private final ImageReader reader = null;

    private final AbstractAsyncPictureLoaderSupport asyncPictureLoaderSupport = new AbstractAsyncPictureLoaderSupport();

    /**
     * Loads a image from the given URL.
     *
     * @param name URL to the picture
     */
    public synchronized void loadImage( final String name )
    {
        this.pictureURL = name;
        notify();
        Thread.yield();
    }

    /** Abort async image loading. */
    public void abort()
    {
        if ( reader != null )
        {
            LOGGER.info( "abort() image loading aborted" );
            reader.abort();
        }
    }

    /** Terminate image loader thread. */
    public synchronized void terminate()
    {
        abort();
        running = false;
        notify();
    }

    /** Async loader... */
    public void run()
    {
        LOGGER.info( "AsyncPictureLoader thread started" );
        synchronized ( this )
        {
            while ( running )
            {
                try
                {
                    if ( "".equals( pictureURL ) )
                    {
                        LOGGER.info( "run() wait for image" );
                        wait();
                    }

                    LOGGER.info( "loading of picture " + pictureURL + " initiate." );
                    PictureLoader.load( pictureURL, false );
                    pictureURL = "";
                }
                catch ( Exception ex )
                {
                    LOGGER.error( "Loading of image " + pictureURL + " failed.", ex );
                }
            }
        }
        LOGGER.info( "AsyncPictureLoader thread stopped" );
    }

    public void imageUpdate( final ImageReader source, final BufferedImage theImage, final int minX, final int minY,
                             final int width, final int height, final int periodX, final int periodY, final int[] bands )
    {
    }

    public void passComplete( final ImageReader source, final BufferedImage bufferedImage )
    {
        LOGGER.info( "loading of picture " + pictureURL + " finished." );
        informFinished( pictureURL, bufferedImage, false );
    }

    public void passStarted( final ImageReader source, final BufferedImage theImage, final int pass, final int minPass,
                             final int maxPass, final int minX, final int minY, final int periodX, final int periodY,
                             final int[] bands )
    {
    }

    public void thumbnailPassComplete( final ImageReader imageReader,
                                       final BufferedImage bufferedImage )
    {
    }

    public void thumbnailPassStarted( final ImageReader source, final BufferedImage theThumbnail, final int pass,
                                      final int minPass, final int maxPass, final int minX, final int minY,
                                      final int periodX, final int periodY, final int[] bands )
    {
    }

    public void thumbnailUpdate( final ImageReader source, final BufferedImage theThumbnail, final int minX,
                                 final int minY, final int width, final int height, final int periodX,
                                 final int periodY, final int[] bands )
    {
    }

    public void imageComplete( final ImageReader source )
    {
    }

    public void imageProgress( final ImageReader source, final float percentageDone )
    {
    }

    public void imageStarted( final ImageReader source, final int imageIndex )
    {
        LOGGER.info( "loading of picture " + pictureURL + " started." );
        informStarted( pictureURL );
    }

    public void readAborted( final ImageReader source )
    {
        LOGGER.info( "loading of picture " + pictureURL + " aborted." );
        informAborted( pictureURL );
    }

    public void sequenceComplete( final ImageReader source )
    {
    }

    public void sequenceStarted( final ImageReader source, final int minIndex )
    {
    }

    public void thumbnailComplete( final ImageReader source )
    {
    }

    public void thumbnailProgress( final ImageReader source, final float percentageDone )
    {
    }

    public void thumbnailStarted( final ImageReader source, final int imageIndex, final int thumbnailIndex )
    {
    }

    public void informFinished( final String name, final Image image, final boolean thumb )
    {
        asyncPictureLoaderSupport.informFinished( name, image, thumb );
    }

    public void informAborted( final String name )
    {
        asyncPictureLoaderSupport.informAborted( name );
    }

    public void informStarted( final String name )
    {
        asyncPictureLoaderSupport.informStarted( name );
    }

    public void detach( final AsyncPictureLoaderListener listener )
    {
        asyncPictureLoaderSupport.detach( listener );
    }

    public void attach( final AsyncPictureLoaderListener listener )
    {
        asyncPictureLoaderSupport.attach( listener );
    }
}
