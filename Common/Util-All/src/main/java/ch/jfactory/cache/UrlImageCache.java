package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caches URL images.
 *
 * @author Daniel Frey 21.11.2010 18:17:53
 */
public class UrlImageCache implements ImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( UrlImageCache.class );

    /** The absolute path to search for images. */
    private final String url;

    /** Write format string. */
    private final String format;

    public UrlImageCache( final String url, final String format )
    {
        this.url = url;
        this.format = format;
    }

    /**
     * Append system depending path to the image string.
     *
     * @param image image name
     * @return name full qualified name
     */
    public URL locate( final String image )
    {
        try
        {
            return new URL( url + image );
        }
        catch ( MalformedURLException e )
        {
            LOG.error( "URL seems to be incorrect", e );
        }
        return null;
    }

    public BufferedImage getImage( final String name ) throws ImageCacheException
    {
        final URL url = locate( name );
        try
        {
            final BufferedImage image = ImageIO.read( url );
            LOG.debug( "successfully retrieved image " + name + " from " + url );
            return image;
        }
        catch ( Throwable e )
        {
            throw new ImageCacheException( "could not retrieve image " + name + " from " + url + " due to unknown exception", e, -1, this );
        }
    }

    public void setImage( final String name, final BufferedImage image ) throws ImageCacheException
    {
        final URL url = locate( name );
        try
        {
            ImageIO.write( image, format, ImageIO.createImageOutputStream( url ) );
            LOG.debug( "successfully cached image " + name + " to " + url );
        }
        catch ( Throwable e )
        {
            throw new ImageCacheException( "could not cache image " + name + " to " + url, e, -1, this );
        }
    }

    public boolean isCached( final String name )
    {
        try
        {
            final URL url = locate( name );
            final String protocol = url.getProtocol();
            if ( protocol != null && "http".equals( protocol ) )
            {
                return ( (HttpURLConnection) url.openConnection() ).getResponseCode() == 200;
            }
            else
            {
                LOG.error( "unknown protocol " + protocol );
                return false;
            }
        }
        catch ( MalformedURLException e )
        {
            LOG.error( "URL malformed " + url, e );
        }
        catch ( IOException e )
        {
            LOG.error( "cannot open connection to " + url, e );
        }
        return false;
    }

    public void invalidateCache() throws ImageCacheException
    {
        try
        {
            final URL url = new URL( this.url );
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput( true );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
            connection.setRequestMethod( "DELETE" );
            connection.connect();
            connection.getInputStream().close();
            connection.disconnect();
        }
        catch ( IOException e )
        {
            throw new ImageCacheException( "could not invalidate the cache at " + url, e, -1, this );
        }
    }

    @Override
    public String toString()
    {
        return "UrlImageCache[url=" + url + ",format=" + format + "]";
    }
}
