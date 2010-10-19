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
 * This class is used to load Image asynchronously. Use the AsynchronPictureLoaderListener to get notification of the
 * loading process.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
class AsynchronPictureLoader extends Thread implements IIOReadUpdateListener, IIOReadProgressListener, AsynchronPictureLoaderSupport
{
    /** Category information for logging. */
    private static final Logger LOGGER = LoggerFactory.getLogger( AsynchronPictureLoader.class.getName() );

    /** If running is set to false, the thread goes down. */
    private boolean running = true;

    /** URL to the image which should be loaded. */
    private String pictureURL = "";

    /** Image reader. */
    private final ImageReader reader = null;

    private final AbstractAsynchronPictureLoaderSupport asynchronPictureLoaderSupport = new AbstractAsynchronPictureLoaderSupport();

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

    /** Abort asynchron image loading. */
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

    /** Asynchron loader... */
    public void run()
    {
        LOGGER.info( "AsynchronPictureLoader thread started" );
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
        LOGGER.info( "AsynchronPictureLoader thread stopped" );
    }

    public void imageUpdate( final ImageReader imageReader, final BufferedImage
            bufferedImage, final int param, final int param3, final int param4,
                             final int param5, final int param6, final int param7, final int[] values )
    {
    }

    public void passComplete( final ImageReader imageReader,
                              final BufferedImage bufferedImage )
    {
        LOGGER.info( "loading of picture " + pictureURL + " finished." );
        informFinished( pictureURL, bufferedImage, false );
    }

    public void passStarted( final ImageReader imageReader,
                             final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                             final int param5, final int param6, final int param7, final int param8, final int[] values )
    {
    }

    public void thumbnailPassComplete( final ImageReader imageReader,
                                       final BufferedImage bufferedImage )
    {
    }

    public void thumbnailPassStarted( final ImageReader imageReader,
                                      final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                      final int param5, final int param6, final int param7, final int param8, final int[] values )
    {
    }

    public void thumbnailUpdate( final ImageReader imageReader,
                                 final BufferedImage bufferedImage, final int param, final int param3, final int param4,
                                 final int param5, final int param6, final int param7, final int[] values )
    {
    }

    public void imageComplete( final ImageReader imageReader )
    {
    }

    public void imageProgress( final ImageReader imageReader, final float param )
    {
    }

    public void imageStarted( final ImageReader imageReader, final int param )
    {
        LOGGER.info( "loading of picture " + pictureURL + " started." );
        informStarted( pictureURL );
    }

    public void readAborted( final ImageReader imageReader )
    {
        LOGGER.info( "loading of picture " + pictureURL + " aborted." );
        informAborted( pictureURL );
    }

    public void sequenceComplete( final ImageReader imageReader )
    {
    }

    public void sequenceStarted( final ImageReader imageReader, final int param )
    {
    }

    public void thumbnailComplete( final ImageReader imageReader )
    {
    }

    public void thumbnailProgress( final ImageReader imageReader, final float param )
    {
    }

    public void thumbnailStarted( final ImageReader imageReader, final int param,
                                  final int param2 )
    {
    }

    public void informFinished( final String name, final Image image, final boolean thumb )
    {
        asynchronPictureLoaderSupport.informFinished( name, image, thumb );
    }

    public void informAborted( final String name )
    {
        asynchronPictureLoaderSupport.informAborted( name );
    }

    public void informStarted( final String name )
    {
        asynchronPictureLoaderSupport.informStarted( name );
    }

    public void detach( final AsynchronPictureLoaderListener listener )
    {
        asynchronPictureLoaderSupport.detach( listener );
    }

    public void attach( final AsynchronPictureLoaderListener listener )
    {
        asynchronPictureLoaderSupport.attach( listener );
    }
}
