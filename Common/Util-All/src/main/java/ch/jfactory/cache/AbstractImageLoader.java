package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;

public abstract class AbstractImageLoader implements ImageLoader
{
    private static final Logger LOG = Logger.getLogger( AbstractImageLoader.class );

    protected final ImageLoader delegateImageLoader;

    protected ImageLoader referringImageLoader;

    private LoaderErrorHandler handler;

    protected AbstractImageLoader( final AbstractImageLoader delegateImageLoader )
    {
        this.delegateImageLoader = delegateImageLoader;
        if ( delegateImageLoader != null )
        {
            delegateImageLoader.referringImageLoader = this;
        }
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
                try
                {
                    internalFetchIntoCache( imageName, image );
                }
                catch ( Throwable e )
                {
                    LOG.error( "error during caching", e );
//                    handleLoaderError( e );
                }
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

    private BufferedImage getImageAndHandleMemory( final String name )
    {
        int retryCounter = 2;
        while ( retryCounter > 0 )
        {
            try
            {
                return internalGetImage( name );
            }
            catch ( OutOfMemoryError e )
            {
                LOG.warn( "out of memory error detected! Retrying!", e );
                freeMemory( retryCounter < 1 );
                retryCounter--;
            }
            catch ( Throwable t )
            {
                LOG.error( "could not load image " + name, t );
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

    public void registerLoaderErrorHandler( final LoaderErrorHandler handler )
    {
        this.handler = handler;
    }

    public void handleLoaderError( final Throwable e )
    {
        if ( handler != null )
        {
            handler.handleLoaderError( e );
        }
        else if ( referringImageLoader != null )
        {
            referringImageLoader.handleLoaderError( e );
        }
    }

    public interface LoaderErrorHandler
    {
        void handleLoaderError( Throwable e );
    }
}
