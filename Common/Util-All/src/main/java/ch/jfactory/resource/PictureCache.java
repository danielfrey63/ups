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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a cache for images. It stores Images into a HashMap via a soft reference.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PictureCache implements AsyncPictureLoaderListener
{
    class CacheListEntry
    {
        public CacheListEntry( final String image, final boolean t )
        {
            this.image = image;
            this.thumb = t;
        }

        public String image;

        public boolean thumb;
    }

    /** category for logging */
    protected final static Logger LOGGER = LoggerFactory.getLogger( PictureCache.class.getName() );

    protected final static boolean INFO = LOGGER.isInfoEnabled();

    protected final static boolean DEBUG = LOGGER.isDebugEnabled();

    protected HashMap<String, CacheListEntry> cacheListHash = new HashMap<String, CacheListEntry>();

    private final CachedImageLocator locator;

    /**
     * caching thread
     *
     * @see CacheImageThread
     */
    private final CacheImageThread cachingThread = new CacheImageThread();

    /** Hash map storing cached picture. */
    private final Map<String, CachedImage> pictureCache = new HashMap<String, CachedImage>();

    /** Task list, contains images to be cached. */
    protected final LinkedList<String> cachingList = new LinkedList<String>();

    /**
     * PictureCache taking images from directory
     *
     * @param locator the images are out of this directory
     */
    public PictureCache( final CachedImageLocator locator )
    {
        cachingThread.setPriority( Thread.MIN_PRIORITY );
        cachingThread.start();
        this.locator = locator;
    }

    /** Clears all entries from cache. */
    synchronized public void clearCachingList()
    {
        synchronized ( cachingList )
        {
            cachingList.clear();
            cacheListHash.clear();
        }
        if ( DEBUG )
        {
            LOGGER.debug( "caching list cleared" );
        }
    }

    /**
     * Puts an Image into the cache.
     *
     * @param name name of the image
     * @return a CachedImage-Object representing the image
     * @see CachedImage
     */
    public CachedImage addOrGetCachedImage( final String name )
    {
        CachedImage image = pictureCache.get( name );
        if ( image == null )
        {
            if ( INFO )
            {
                LOGGER.info( "adding image " + name + " in " + locator.getPath() + " " + " to cache" );
            }
            image = new CachedImage( locator, name );
            pictureCache.put( name, image );
        }
        return image;
    }

    /**
     * Removes an image from the cache.
     *
     * @param name name of the cache
     */
    public void removeImage( final String name )
    {
        if ( INFO )
        {
            LOGGER.info( "removing image " + name + " from cache" );
        }
        pictureCache.remove( name );
    }

    /**
     * The image define by the name will be inserted into the cache. It is inserted only if it is not there already. If
     * it is there and should be inserted at the first position, it is moved to the first position.
     *
     * @param name  name of the image
     * @param thumb whether it is a thumbnail
     * @param first insert at the start of the list
     */
    public void cacheImage( final String name, final boolean thumb, final boolean first )
    {
        if ( !addOrGetCachedImage( name ).loaded( thumb ) )
        {
            final boolean isNew;
            synchronized ( cachingList )
            {
                isNew = !cachingList.contains( name );
                if ( isNew )
                {
                    if ( first )
                    {
                        cachingList.addFirst( name );
                    }
                    else
                    {
                        cachingList.addLast( name );
                    }
                }
                else if ( first && !cachingList.getFirst().equals( name ) )
                {
                    cachingList.remove( name );
                    cachingList.addFirst( name );
                }
                if ( !thumb )
                {
                    CacheListEntry cle = cacheListHash.get( name );
                    if ( cle == null )
                    {
                        cle = new CacheListEntry( name, thumb );
                        cacheListHash.put( name, cle );
                    }
                    cle.thumb = thumb;
                }
            }
            if ( isNew )
            {
                if ( DEBUG )
                {
                    LOGGER.debug( "caching List changed due to " + name );
                }
                synchronized ( cachingThread )
                {
                    cachingThread.notify();
                }
            }
        }
    }

    public void loadFinished( final String name, final Image img, final boolean thumb )
    {
        addOrGetCachedImage( name ).setImage( img, thumb );
    }

    public void loadAborted( final String name )
    {
        removeImage( name );
    }

    public void loadStarted( final String name )
    {
        // nothing
    }

    /**
     * Internal class used to processing the caching image list.
     *
     * @author Dirk
     */
    class CacheImageThread extends Thread
    {
        public void run()
        {
            boolean loop = true;
            while ( loop )
            {
                try
                {
                    final String name;
                    boolean thumb = false;
                    synchronized ( PictureCache.this.cachingList )
                    {
                        name = cachingList.removeFirst();
                        final CacheListEntry cle = cacheListHash.get( name );
                        if ( cle != null )
                        {
                            thumb = cle.thumb;
                        }
                        cacheListHash.remove( name );
                    }
                    LOGGER.info( "caching image " + name + ", thumb = " + thumb );
                    final CachedImage img = addOrGetCachedImage( name );
                    if ( !img.loaded( thumb ) )
                    {
                        addOrGetCachedImage( name ).loadImage( thumb );
                    }
                }
                catch ( NoSuchElementException ex )
                {
                    synchronized ( this )
                    {
                        if ( INFO )
                        {
                            LOGGER.info( "caching thread waiting" );
                        }
                        try
                        {
                            this.wait();
                        }
                        catch ( InterruptedException iex )
                        {
                            loop = false;
                            LOGGER.error( "Caching thread interrupted.", iex );
                        }
                    }
                }
            }
        }
    }
}
