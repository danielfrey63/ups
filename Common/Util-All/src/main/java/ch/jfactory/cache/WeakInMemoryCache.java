/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for locating images and support for image caching. Weak references are used to hold the image, which is
 * reloaded if the image has been purged.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class WeakInMemoryCache implements ImageCache
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( WeakInMemoryCache.class );

    /** Map containing a cache for images especially icons. */
    private static final Map<String, WeakReference<BufferedImage>> imageCache = new WeakHashMap<String, WeakReference<BufferedImage>>();

    public boolean isCached( final String name )
    {
        final WeakReference<BufferedImage> ref = imageCache.get( name );
        final boolean isCached = ref != null && ref.get() != null;
        LOG.debug( "image " + name + " is " + ( isCached ? "" : "not " ) + "cached in memory" );
        return isCached;
    }

    public BufferedImage getImage( final String name )
    {
        final WeakReference<BufferedImage> ref = imageCache.get( name );
        final BufferedImage image = ref == null ? null : ref.get();
        LOG.debug( ( image == null ? "could not retrieve" : "successfully retrieved" ) + " image " + name + " from memory" );
        return image;
    }

    public void setImage( final String name, final BufferedImage image )
    {
        LOG.debug( "successfully cached image " + name + " to memory" );
        final WeakReference<BufferedImage> ref = new WeakReference<BufferedImage>( image );
        imageCache.put( name, ref );
    }

    public void invalidateCache()
    {
        LOG.debug( "emptying in memory cache" );
        imageCache.clear();
    }

    @Override
    public String toString()
    {
        return "WeakInMemoryCache[]";
    }
}
