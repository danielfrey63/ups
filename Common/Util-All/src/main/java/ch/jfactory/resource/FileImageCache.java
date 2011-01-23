package ch.jfactory.resource;

import ch.jfactory.cache.AbstractImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caches file system images.
 *
 * @author Daniel Frey 21.11.2010 18:17:53
 */
public class FileImageCache extends AbstractImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( FileImageCache.class );

    /** The absolute path to search for images. */
    private final String path;

    private final String formatName;

    public FileImageCache( final AbstractImageLoader delegateImageLoader, final String path, final String formatName )
    {
        super( delegateImageLoader );
        this.path = ( path.endsWith( "/" ) || path.endsWith( "\\" ) ? path.substring( 0, path.length() - 1 ) : path ) + "/";
        this.formatName = formatName;
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
                LOG.warn( "could not create directory at " + path );
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
        return path + image;
    }

    protected BufferedImage loadImage( final String name )
    {
        final String path = locate( name );
        try
        {
            final InputStream stream = new FileInputStream( path );
            final BufferedImage image = ImageIO.read( stream );
            LOG.info( "successfully loaded image " + path + " from file system" );
            return image;
        }
        catch ( IOException e )
        {
            LOG.info( "could not load image " + path + " from file system" );
        }
        return null;
    }

    protected void writeImage( final String name, final BufferedImage image )
    {
        try
        {
            final File file = new File( locate( name ) );
            ImageIO.write( image, formatName, file );
        }
        catch ( IOException e )
        {
            LOG.error( "could not write " + new File( locate( name ) ).getAbsolutePath() + " to file system" );
            handleLoaderError( e );
        }
    }

    @Override
    public boolean internalIsCached( final String imageName )
    {
        return new File( imageName ).exists();
    }

    @Override
    public String toString()
    {
        return "FileImageCache[" + path + "," + delegateImageLoader + "]";
    }
}
