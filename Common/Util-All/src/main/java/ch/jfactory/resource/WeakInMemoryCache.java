/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import ch.jfactory.cache.AbstractImageLoader;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Helper class for locating images and support for image caching. Weak references are used to hold the image, which is
 * reloaded if the image has been purged.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class WeakInMemoryCache extends AbstractImageLoader
{
    /** Map containing a cache for images especially icons. */
    private static final Map<String, WeakReference<BufferedImage>> imageCache = new WeakHashMap<String, WeakReference<BufferedImage>>();

    public WeakInMemoryCache( final AbstractImageLoader delegateImageLoader )
    {
        super( delegateImageLoader );
    }

    @Override
    protected boolean internalIsCached( final String name )
    {
        final WeakReference<BufferedImage> ref = imageCache.get( name );
        return ref != null && ref.get() != null;
    }

    @Override
    protected BufferedImage internalGetImage( final String name )
    {
        final WeakReference<BufferedImage> ref = imageCache.get( name );
        return ref == null ? null : ref.get();
    }

    @Override
    protected void internalFetchIntoCache( final String name, final BufferedImage image )
    {
        final WeakReference<BufferedImage> ref = new WeakReference<BufferedImage>( image );
        imageCache.put( name, ref );
    }

    @Override
    protected void internalInvalidateCache()
    {
        imageCache.clear();
    }

    @Override
    protected void internalClose()
    {
        imageCache.clear();
    }

    @Override
    public String toString()
    {
        return "WeakInMemoryCache[" + delegateImageLoader + "]";
    }
}
