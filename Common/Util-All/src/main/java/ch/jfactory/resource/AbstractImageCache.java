package ch.jfactory.resource;

import ch.jfactory.cache.AbstractImageLoader;
import ch.jfactory.cache.ImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Loads files from the file system.
 *
 * @author Daniel Frey 21.11.2010 14:12:19
 */
public abstract class AbstractImageCache extends AbstractImageLoader
{
    public AbstractImageCache( final ImageLoader delegateImageLoader )
    {
        super( delegateImageLoader );
    }

    protected abstract BufferedImage loadImage( String name );

    protected abstract void writeImage( String name, BufferedImage image );

    @Override
    public BufferedImage internalGetImage( final String name )
    {
        return loadImage( name );
    }

    @Override
    public void internalFetchIntoCache( final String name, final BufferedImage image )
    {
        writeImage( name, image );
    }

    @Override
    public void internalInvalidateCache()
    {
        // Do nothing, especially not deleting files.
    }

    @Override
    public void internalClose()
    {
        // Do nothing
    }
}
