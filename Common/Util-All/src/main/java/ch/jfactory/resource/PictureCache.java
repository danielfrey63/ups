/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a cache for images. It maintains a queue for pictures to cache and stores them in a HashMap
 * with soft references.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PictureCache
{
    /** This class' logger. */
    private final static Logger LOGGER = LoggerFactory.getLogger( PictureCache.class.getName() );

    /** Locator for images. */
    private final CachedImageLocator locator;

    /** Caching thread. */
    private final CacheImageThread cachingThread = new CacheImageThread();

    /** Hash map storing cached picture. */
    private final Map<String, CachedImage> cache = new HashMap<String, CachedImage>();

    /** Task queue, contains images to be cached. */
    private final LinkedList<String> queue = new LinkedList<String>();

    /**
     * PictureCache taking images from directory
     *
     * @param locator the images are out of this directory
     */
    public PictureCache( final CachedImageLocator locator )
    {
        this.locator = locator;
        cachingThread.setPriority( Thread.MIN_PRIORITY );
        cachingThread.start();
    }

    /** Clears all entries from cache. */
    synchronized public void clearCachingList()
    {
        synchronized ( queue )
        {
            queue.clear();
        }
        LOGGER.debug( "caching list cleared" );
    }

    /**
     * Puts an image into the cache.
     *
     * @param name name of the image
     * @return a CachedImage-Object representing the image
     * @see CachedImage
     */
    public CachedImage addOrGetCachedImage( final String name )
    {
        CachedImage image = cache.get( name );
        if ( image == null )
        {
            LOGGER.info( "adding image \"" + name + "\" to cache" );
            image = new CachedImage( locator, name );
            cache.put( name, image );
        }
        return image;
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
            synchronized ( queue )
            {
                isNew = !queue.contains( name );
                if ( isNew )
                {
                    if ( first )
                    {
                        queue.addFirst( name );
                    }
                    else
                    {
                        queue.addLast( name );
                    }
                }
                else if ( first && !queue.getFirst().equals( name ) )
                {
                    queue.remove( name );
                    queue.addFirst( name );
                }
            }
            if ( isNew )
            {
                LOGGER.debug( "caching list changed due to \"" + name + "\" and is now " + queue );
                synchronized ( cachingThread )
                {
                    cachingThread.notify();
                }
            }
        }
        else
        {
            LOGGER.info( "image " + name + " found in cache" );
        }
    }

    /** Internal class used to processing the caching image list. */
    private class CacheImageThread extends Thread
    {
        public void run()
        {
            boolean loop = true;
            while ( loop )
            {
                try
                {
                    final String name;
                    final boolean thumb = false;
                    synchronized ( queue )
                    {
                        name = queue.removeFirst();
                        LOGGER.debug( "popped \"" + name + "\" from queue " + queue.toString() );
                    }
                    LOGGER.info( "caching image \"" + name + "\"" );
                    final CachedImage img = addOrGetCachedImage( name );
                    if ( !img.loaded( thumb ) )
                    {
                        addOrGetCachedImage( name ).loadImage( thumb );
                    }
                    LOGGER.info( "cached image \"" + name + "\"" );
                }
                catch ( NoSuchElementException ex )
                {
                    synchronized ( this )
                    {
                        LOGGER.info( "caching thread waiting" );
                        try
                        {
                            this.wait();
                        }
                        catch ( InterruptedException iex )
                        {
                            loop = false;
                            LOGGER.error( "caching thread interrupted", iex );
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "PictureCache[" + locator + "]";
    }
}
