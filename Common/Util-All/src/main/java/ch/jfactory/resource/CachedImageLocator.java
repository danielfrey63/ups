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
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for locating images and support for image caching.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CachedImageLocator extends AbstractAsyncPictureLoaderSupport implements AsyncPictureLoaderListener
{
    /** This class' logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger( CachedImageLocator.class );

    /** Map containing a cache for images especially icons. */
    private static final Map<String, WeakReference<ImageIcon>> imageCache = new WeakHashMap<String, WeakReference<ImageIcon>>();

    /** Cache for pictures. */
    private final PictureCache cache = new PictureCache( this );

    /** Loader for images. */
    private AsyncPictureLoader loader = null;

    private final String path;

    public CachedImageLocator( final String path )
    {
        this.path = path;
    }

    /**
     * Get an ImageIcon from Cache or reloads it into the cache.
     *
     * @param name name of the image
     * @return reference to the ImageIcon
     */
    public ImageIcon getImageIcon( final String name )
    {
        LOGGER.trace( "request for " + name );
        WeakReference<ImageIcon> ref = imageCache.get( name );
        if ( ( ref == null ) || ( ref.get() == null ) )
        {
            final String fullName = locate( name );
            final URL url = getClass().getResource( fullName );
            final ImageIcon icon = url == null ? new ImageIcon( fullName ) : new ImageIcon( url );
            imageCache.put( name, ref = new WeakReference<ImageIcon>( icon ) );
        }

        return ref.get();
    }

    /**
     * First, the method tries to get the image from the cache. If it isn't cached, the image will be loaded.
     *
     * @param name Description of the Parameter
     */
    public void loadImage( final String name )
    {
        final String url = locate( name );

        final Image img = cache.addOrGetCachedImage( url ).getImage( false );
        if ( img == null )
        {
            if ( loader == null )
            {
                initLoader();
            }
            loader.loadImage( url );
        }
        else
        {
            LOGGER.info( "get image " + url + " from cache." );
            informStarted( name );
            informFinished( name, img, false );
        }
    }

    /** abort current image loading */
    public void abort()
    {
        if ( loader != null )
        {
            loader.abort();
        }
    }

    public void loadFinished( final String name, final Image img, final boolean thumbNail )
    {
        informFinished( name, img, thumbNail );
    }

    public void loadAborted( final String name )
    {
        informAborted( name );
    }

    public void loadStarted( final String name )
    {
        informStarted( name );
    }

    protected void finalize() throws Throwable
    {
        LOGGER.info( "CachedImageLocator going to be finalize" );

        loader.terminate();
        loader.join();
        super.finalize();

        LOGGER.info( "CachedImageLocator is finalize & thread stopped" );
    }

    private void initLoader()
    {
        loader = new AsyncPictureLoader();
        loader.setPriority( Thread.MIN_PRIORITY );
        loader.attach( this );
        loader.start();
    }

    /**
     * Append system depending path to the image string.
     *
     * @param image image name
     * @return name full qualified name
     */
    private String locate( final String image )
    {
        return getPath() + image;
    }

    @Override
    public String toString()
    {
        return "CachedImageLocator[" + getPath() + "]";
    }

    public String getPath()
    {
        return path;
    }
}
