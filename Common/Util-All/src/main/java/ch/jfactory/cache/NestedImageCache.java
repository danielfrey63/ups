package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class NestedImageCache implements ImageCache
{
    private static final Logger LOG = Logger.getLogger( NestedImageCache.class );

    private static final int MAX_RETRY_COUNT = 3;

    private final ImageCache[] caches;

    private final ImageCache[] cachesToInvalidate;

    private Set<ImageCache> disabled = new HashSet<ImageCache>();

    /**
     * @param cachesToInvalidate the caches to invalidate when {@link #invalidateCache} is called
     * @param caches             the caches to search for images in the order of priority
     */
    public NestedImageCache( final ImageCache[] cachesToInvalidate, final ImageCache... caches )
    {
        assertNotNull( caches );
        for ( final ImageCache cache : caches )
        {
            assertNotNull( cache );
        }
        this.caches = caches;
        this.cachesToInvalidate = cachesToInvalidate == null ? new ImageCache[0] : cachesToInvalidate;
    }

    public void disableCache( final ImageCache cache )
    {
        disabled.add( cache );
    }

    public void invalidateCache( final ImageCache cache ) throws ImageCacheException
    {
        cache.invalidateCache();
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
                if ( !disabled.contains( currentCache ) )
                {
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

    public void invalidateCache() throws ImageCacheException
    {
        for ( final ImageCache cache : cachesToInvalidate )
        {
            if ( !disabled.contains( cache ) )
            {
                cache.invalidateCache();
            }
        }
    }

    private void setImage( final String name, final BufferedImage image, final int max ) throws ImageCacheException
    {
        for ( int i = max; i > 0; i-- )
        {
            final ImageCache cache = caches[i - 1];
            if ( !disabled.contains( cache ) )
            {
                cache.setImage( name, image );
            }
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
