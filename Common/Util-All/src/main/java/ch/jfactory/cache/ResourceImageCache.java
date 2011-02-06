package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads files from the file system.
 *
 * @author Daniel Frey 21.11.2010 14:12:19
 */
public class ResourceImageCache implements ImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ResourceImageCache.class );

    /** The classpath prefix to search for images. */
    private final String path;

    public ResourceImageCache( final String path )
    {
        this.path = ( path.endsWith( "/" ) || path.endsWith( "\\" ) ? path.substring( 0, path.length() - 1 ) : path ) + "/";
    }

    public BufferedImage getImage( final String name ) throws ImageCacheException
    {
        final String path = locate( name );
        try
        {
            final InputStream stream = getClass().getResourceAsStream( path );
            if ( stream == null )
            {
                throw new ImageCacheException( "could not retrieve image " + name + " from " + path, this, new NullPointerException( "resource at path " + path + " not found" ) );
            }
            final BufferedImage image = ImageIO.read( stream );
            stream.close();
            return image;
        }
        catch ( Throwable e )
        {
            throw new ImageCacheException( "could not load resource " + path, this, e );
        }
    }

    public void setImage( final String name, final BufferedImage image )
    {
        throw new UnsupportedOperationException( "cannot save to classpath" );
    }

    public boolean isCached( final String name )
    {
        return getClass().getResource( name ) != null;
    }

    public void invalidateCache()
    {
        throw new UnsupportedOperationException( "invalidate in class path not supported" );
    }

    @Override
    public String toString()
    {
        return "ResourceImageCache[" + path + "]";
    }

    private String locate( final String name )
    {
        return path + name;
    }
}
