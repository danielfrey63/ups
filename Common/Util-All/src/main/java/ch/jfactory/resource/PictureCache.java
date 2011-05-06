/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.resource;

import ch.jfactory.cache.ImageCache;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a cache for images. It maintains a queue for pictures to cache and stores them in a HashMap with soft references.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PictureCache
{
    /** This class' logger. */
    private final static Logger LOG = LoggerFactory.getLogger( PictureCache.class.getName() );

    /** Locator for images. */
    private final ImageCache locator;

    /** Caching thread. */
    private final CacheImageThread cachingThread;

    /** Hash map storing cached picture. */
    private final Map<String, CachedImage> cache = new HashMap<String, CachedImage>();

    /** Task queue, contains images to be cached. */
    private final LinkedList<String> queue = new LinkedList<String>();

    /** PictureCache taking images from directory */
    public PictureCache( final CachingExceptionHandler handler )
    {
        this.locator = ImageLocator.PICT_LOCATOR;
        cachingThread = new CacheImageThread( handler );
        cachingThread.setPriority( Thread.MIN_PRIORITY );
        cachingThread.start();
    }

    /** Clears all entries from caching queue. */
    synchronized public void clearCachingList()
    {
        synchronized ( queue )
        {
            queue.clear();
        }
        LOG.debug( "caching list cleared" );
    }

    /**
     * Puts an image into the cache.
     *
     * @param name name of the image
     * @return a CachedImage-Object representing the image
     * @see CachedImage
     */
    public CachedImage getCachedImage( final String name )
    {
        CachedImage image = cache.get( name );
        if ( image == null )
        {
            LOG.info( "adding cached image \"" + name + "\" to cache" );
            image = new CachedImage( locator, name );
            cache.put( name, image );
        }
        return image;
    }

    /**
     * Registers the image in the queue if not loaded already. Allows for small prioritization by indicating that the image has to be put at the first position.
     *
     * @param name  name of the image
     * @param thumb whether it is a thumbnail
     * @param first insert at the start of the list
     */
    public void cacheImage( final String name, final boolean thumb, final boolean first )
    {
        if ( !getCachedImage( name ).isLoaded( thumb ) )
        {
            LOG.trace( "cached image " + name + " not loaded yet" );
            final boolean isNew;
            synchronized ( queue )
            {
                isNew = !queue.contains( name );
                if ( isNew )
                {
                    if ( first )
                    {
                        queue.addFirst( name );
                        LOG.debug( "added cached image " + name + " to first position in the queue " + queue );
                    }
                    else
                    {
                        queue.addLast( name );
                        LOG.debug( "added cached image " + name + " to last position in the queue " + queue );
                    }
                }
                else if ( first && !queue.getFirst().equals( name ) )
                {
                    queue.remove( name );
                    queue.addFirst( name );
                    LOG.debug( "moved cached image " + name + " to first position in the queue " + queue );
                }
            }
            if ( isNew )
            {
                synchronized ( cachingThread )
                {
                    cachingThread.notify();
                }
            }
        }
        else
        {
            LOG.trace( "image " + name + " loaded already" );
        }
    }

    public interface CachingExceptionHandler
    {
        void handleCachingException( final Throwable e );
    }

    /** Internal class used to processing the caching image list. */
    private class CacheImageThread extends Thread
    {
        private final CachingExceptionHandler handler;

        public CacheImageThread( final CachingExceptionHandler handler )
        {
            this.handler = handler;
        }

        public void run()
        {
            boolean loop = true;
            while ( loop )
            {
                String name = null;
                try
                {
                    // Peek does not throw NSEE on itself
                    name = queue.peek();
                    if ( name == null )
                    {
                        throw new NoSuchElementException( "peeking on null" );
                    }
//                    LOG.debug( "peeking on \"" + name + "\" in queue " + new LinkedList<String>( queue ) );
                    final CachedImage img = getCachedImage( name );
                    if ( !img.isLoaded( false ) )
                    {
                        LOG.debug( "loading cached image \"" + name + "\"" );
                        img.loadImage();
                        LOG.debug( "loaded cached image \"" + name + "\"" );
                    }
                    else
                    {
                        LOG.debug( "cached image \"" + name + "\" loading or loaded already" );
                    }
                    // Important to remove the image from the queue also in loaded and exception case
                    removeFromQueue( name );
                }
                catch ( NoSuchElementException ex )
                {
                    synchronized ( this )
                    {
                        LOG.debug( "caching thread waiting" );
                        try
                        {
                            this.wait();
                        }
                        catch ( InterruptedException iex )
                        {
                            loop = false;
                            LOG.error( "caching thread interrupted", iex );
                        }
                    }
                }
                catch ( Throwable e )
                {
                    // Important to remove the image from the queue also in loaded and exception case
                    removeFromQueue( name );
                    if ( handler != null )
                    {
                        handler.handleCachingException( e );
                    }
                }
            }
        }

        private void removeFromQueue( final String name )
        {
            synchronized ( queue )
            {
                try
                {
                    queue.remove( name );
                    LOG.debug( "popped image " + name + " after successful loading from queue " + queue );
                }
                catch ( Exception e )
                {
                    // Ignore
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
