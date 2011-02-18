package ch.jfactory.cache;

/**
 * Exception during image caching. Overwrites equals so it returns true if the messages of the causes are the same.
 *
 * @author Daniel Frey 05.02.11 14:31
 */
public class ImageCacheException extends Exception
{
    private final long freeSpace;

    private final ImageCache imageCache;

    public ImageCacheException( final String message, final Throwable cause, final long freeSpace, final ImageCache imageCache )
    {
        super( message, cause );
        this.freeSpace = freeSpace;
        this.imageCache = imageCache;
    }

    public long getFreeSpace()
    {
        return freeSpace;
    }

    public ImageCache getImageCache()
    {
        return imageCache;
    }

    public boolean equals( final Object other )
    {
        if ( !( other instanceof ImageCacheException ) )
        {
            return false;
        }
        final Throwable otherCause = ( (ImageCacheException) other ).getCause();
        final Throwable cause = getCause();
        return cause.getMessage().equals( otherCause.getMessage() );
    }
}
