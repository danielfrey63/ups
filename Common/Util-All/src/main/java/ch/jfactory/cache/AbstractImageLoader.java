package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;

public abstract class AbstractImageLoader implements ImageLoader
{
    private static final Logger LOG = Logger.getLogger( AbstractImageLoader.class );

    protected final ImageLoader delegateImageLoader;

    protected AbstractImageLoader( final ImageLoader delegateImageLoader )
    {
        this.delegateImageLoader = delegateImageLoader;
    }

    public final boolean isCached( final String imageName )
    {
        return internalIsCached( imageName ) || delegateImageLoader != null && delegateImageLoader.isCached( imageName );
    }

    public final BufferedImage getImage( final String imageName )
    {
        BufferedImage image = getImageAndHandleMemory( imageName );

        if ( image == null && delegateImageLoader != null )
        {
            image = delegateImageLoader.getImage( imageName );
            if ( image != null )
            {
                internalFetchIntoCache( imageName, image );
            }
            else
            {
                LOG.warn( "Could not get image for \"" + imageName + "\"" );
            }
        }
        return image;
    }

    public final void invalidateCache()
    {
        if ( delegateImageLoader != null )
        {
            delegateImageLoader.invalidateCache();
        }
        internalInvalidateCache();
    }

    public final void close()
    {
        if ( delegateImageLoader != null )
        {
            delegateImageLoader.close();
        }
        internalClose();
    }

    private BufferedImage getImageAndHandleMemory( final String imageName )
    {
        int retryCounter = 2;
        while ( retryCounter > 0 )
        {
            try
            {
                return internalGetImage( imageName );
            }
            catch ( OutOfMemoryError e )
            {
                LOG.warn( "out of memory error detected! Retrying!", e );
                freeMemory( retryCounter < 1 );
                retryCounter--;
            }
            catch ( Throwable t )
            {
                LOG.error( "could not load the image", t );
                return null;
            }
        }
        LOG.error( "could not load the image because of memory constraints!" );
        return null;
    }

    private void freeMemory( final boolean hard )
    {
        LOG.debug( "freeMemory(" + hard + ")" );
        if ( hard )
        {
            invalidateCache();
        }
        System.runFinalization();
        System.gc();
    }

    protected abstract boolean internalIsCached( String imageName );

    protected abstract BufferedImage internalGetImage( String imageName );

    protected abstract void internalFetchIntoCache( String imageName, BufferedImage image );

    protected abstract void internalInvalidateCache();

    protected abstract void internalClose();
}
