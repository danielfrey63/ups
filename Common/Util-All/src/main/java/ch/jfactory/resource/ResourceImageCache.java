package ch.jfactory.resource;

import ch.jfactory.cache.AbstractImageLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads files from the file system.
 *
 * @author Daniel Frey 21.11.2010 14:12:19
 */
public class ResourceImageCache extends AbstractImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ResourceImageCache.class );

    /** The classpath prefix to search for images. */
    private final String path;

    public ResourceImageCache( final AbstractImageLoader delegateImageLoader, final String path )
    {
        super( delegateImageLoader );
        this.path = path;
    }

    @Override
    public BufferedImage loadImage( final String name )
    {
        try
        {
            final InputStream stream = getClass().getResourceAsStream( path + name );
            if ( stream == null )
            {
                LOG.warn( "could not load " + path + name );
                return null;
            }
            final BufferedImage image = ImageIO.read( stream );
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
    protected void writeImage( final String name, final BufferedImage image )
    {
        throw new UnsupportedOperationException( "cannot save to classpath" );
    }

    @Override
    protected boolean internalIsCached( final String imageName )
    {
        return getClass().getResource( imageName ) != null;
    }

    @Override
    public String toString()
    {
        return "ResourceImageCache[" + path + "," + delegateImageLoader + "]";
    }
}
