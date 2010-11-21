package ch.jfactory.resource;

import ch.jfactory.cache.AbstractImageLoader;
import ch.jfactory.cache.ImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads files from the file system.
 *
 * @author Daniel Frey 21.11.2010 14:12:19
 */
public class ResourceImageCache extends AbstractImageLoader
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ResourceImageCache.class );

    /** The classpath prefix to search for images. */
    private final String path;

    public ResourceImageCache( final ImageLoader delegateImageLoader, final String path )
    {
        super( delegateImageLoader );
        this.path = path;
    }

    @Override
    public boolean internalIsCached( final String imageName )
    {
        return new File( imageName ).exists();
    }

    @Override
    public BufferedImage internalGetImage( final String name )
    {
        try
        {
            final InputStream stream = getClass().getResourceAsStream( path + name );
            if ( stream == null )
            {
                LOG.error( "image not locatable at " + path + name );
                return null;
            }
            final ImageInputStream imageInputStream = ImageIO.createImageInputStream( stream );
            if ( imageInputStream == null )
            {
                LOG.error( "image not found at " + path + name );
                return null;
            }
            final Iterator<ImageReader> iterator = ImageIO.getImageReaders( imageInputStream );
            final ImageReader reader = ( iterator.hasNext() ? iterator.next() : null );
            if ( reader == null )
            {
                LOG.error( "no image reader found for " + path + name );
                return null;
            }
            reader.setInput( imageInputStream );
            final BufferedImage image = reader.read( 0 );
            imageInputStream.close();
            stream.close();
            return image;
        }
        catch ( IOException e )
        {
            LOG.error( "could not load resource " + path + name );
        }
        return null;
    }

    @Override
    public void internalFetchIntoCache( final String name, final BufferedImage image )
    {
        try
        {
            ImageIO.write( image, "jpg", new File( locate( name ) ) );
        }
        catch ( IOException e )
        {
            LOG.error( "could not save image " + locate( name ) );
        }
    }

    /**
     * Append system depending path to the image string.
     *
     * @param image image name
     * @return name full qualified name
     */
    public String locate( final String image )
    {
        return path + image;
    }

    @Override
    public void internalInvalidateCache()
    {
        // Resources cannot and should not be deleted.
    }

    @Override
    public void internalClose()
    {
        // Do nothing
    }

    @Override
    public String toString()
    {
        return "FileImageCache[" + path + "," + delegateImageLoader + "]";
    }
}
