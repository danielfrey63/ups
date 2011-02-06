package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

public class NestedImageCache implements ImageCache
{
    private static final Logger LOG = Logger.getLogger( NestedImageCache.class );

    private static final int MAX_RETRY_COUNT = 3;

    private final ImageCache[] caches;

    public NestedImageCache( final ImageCache... caches )
    {
        assertNotNull( caches );
        for ( final ImageCache cache : caches )
        {
            assertNotNull( cache );
        }
        this.caches = caches;
    }

    public BufferedImage getImage( final String name ) throws ImageCacheException
    {
        int retryCount = MAX_RETRY_COUNT;
        BufferedImage image = null;
        while ( image == null && retryCount-- > 0 )
        {
            for ( int i = 0; image == null && i < caches.length; i++ )
            {
                final ImageCache currentCache = caches[i];
                try
                {
                    image = currentCache.getImage( name );
                }
                catch ( final ImageCacheException e )
                {
                    if ( e.getCause() instanceof OutOfMemoryError )
                    {
                        LOG.warn( "trying to free memory for the " + ( MAX_RETRY_COUNT - retryCount ) + ". time" );
                        System.runFinalization();
                        System.gc();
                    }
                    else
                    {
                        throw e;
                    }
                }
                if ( image != null )
                {
                    setImage( name, image, i );
                }
            }
        }
        return image;
    }

    public void setImage( final String name, final BufferedImage image ) throws ImageCacheException
    {
        setImage( name, image, caches.length );
    }

    /**
     * Returns true if all the nested caches have the image.
     *
     * @param name image name
     */
    public boolean isCached( final String name )
    {
        boolean allHaveIt = true;
        for ( final ImageCache cache : caches )
        {
            allHaveIt &= cache.isCached( name );
        }
        return allHaveIt;
    }

    public final void invalidateCache() throws ImageCacheException
    {
        for ( final ImageCache cache : caches )
        {
            cache.invalidateCache();
        }
    }

    private void setImage( final String name, final BufferedImage image, final int max ) throws ImageCacheException
    {
        for ( int i = max; i > 0; i-- )
        {
            caches[i - 1].setImage( name, image );
        }
    }

    private void assertNotNull( final Object object )
    {
        if ( object == null )
        {
            throw new IllegalArgumentException( "may not be null" );
        }
    }

    @Override
    public String toString()
    {
        final List<ImageCache> imageCaches = Arrays.asList( caches );
        return NestedImageCache.class.getSimpleName() + "[" + imageCaches + "]";
    }
}
