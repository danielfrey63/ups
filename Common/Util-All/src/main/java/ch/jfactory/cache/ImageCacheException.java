package ch.jfactory.cache;

/**
 * Exception during image caching.
 *
 * @author Daniel Frey 05.02.11 14:31
 */
public class ImageCacheException extends Exception
{
    private final ImageCache cache;

    public ImageCacheException( final String message, final ImageCache cache, final Throwable cause )
    {
        super( message, cause );
        this.cache = cache;
    }

    public ImageCache getCache()
    {
        return cache;
    }
}
