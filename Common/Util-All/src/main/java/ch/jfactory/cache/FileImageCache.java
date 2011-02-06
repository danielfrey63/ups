package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caches file system images.
 *
 * @author Daniel Frey 21.11.2010 18:17:53
 */
public class FileImageCache implements ImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( FileImageCache.class );

    /** The absolute path to search for images. */
    private final String path;

    private final String format;

    public FileImageCache( final String path, final String format )
    {
        this.path = ( path.endsWith( "/" ) || path.endsWith( "\\" ) ? path.substring( 0, path.length() - 1 ) : path ) + "/";
        this.format = format;
        ensureDirectoryCreated( path );
    }

    private void ensureDirectoryCreated( final String path )
    {
        final File file = new File( path );
        if ( !file.exists() )
        {
            final boolean success = file.mkdirs();
            if ( !success )
            {
                LOG.error( "could not create directory at " + path );
            }
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
        return new File( path + image ).getAbsolutePath();
    }

    public BufferedImage getImage( final String name ) throws ImageCacheException
    {
        final String path = locate( name );
        try
        {
            final File file = new File( path );
            if ( file.exists() )
            {
                final BufferedImage image = ImageIO.read( file );
                LOG.debug( "successfully retrieved image " + name + " from " + path );
                return image;
            }
            else
            {
                return null;
            }
        }
        catch ( Throwable e )
        {
            throw new ImageCacheException( "could not retrieve image " + name + " from " + path, this, e );
        }
    }

    public void setImage( final String name, final BufferedImage image ) throws ImageCacheException
    {
        ensureDirectoryCreated( path );
        final String path = locate( name );
        try
        {
            final File file = new File( path );
            ImageIO.write( image, format, file );
            LOG.debug( "successfully cached image " + name + " to " + path );
        }
        catch ( Throwable e )
        {
            final File file = new File( path );
            if ( file.exists() )
            {
                file.delete();
            }
            throw new ImageCacheException( "could not cache image " + name + " to " + path, this, e );
        }
    }

    public boolean isCached( final String name )
    {
        final String path = locate( name );
        final boolean exists = new File( path ).exists();
        LOG.debug( "image " + name + " is cached in " + path );
        return exists;
    }

    public void invalidateCache() throws ImageCacheException
    {
        deleteFile( new File( path ) );
    }

    private void deleteFile( final File file ) throws ImageCacheException
    {
        final boolean isDirectory = file.isDirectory();
        final File[] files = file.listFiles();
        if ( isDirectory && files != null && files.length > 0 )
        {
            for ( final File child : files )
            {
                deleteFile( child );
            }
        }
        else
        {
            if ( !file.delete() )
            {
                final String message = "could not remove " + ( isDirectory ? "directory" : "file" ) + " " + file + " from cache";
                throw new ImageCacheException( message, this, new IOException( message ) );
            }
        }
    }

    @Override
    public String toString()
    {
        return "FileImageCache[path=" + path + ",format=" + format + "]";
    }
}
