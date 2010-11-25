package ch.jfactory.resource;

import ch.jfactory.cache.ImageLoader;
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
public class UrlImageCache extends AbstractImageCache
{
    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger( UrlImageCache.class );

    /** The absolute path to search for images. */
    private final String url;

    public UrlImageCache( final ImageLoader delegateImageLoader, final String url )
    {
        super( delegateImageLoader );
        this.url = url;
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

    protected BufferedImage loadImage( final String name )
    {
        final URL url = locate( name );
        try
        {
            final BufferedImage image = ImageIO.read( url );
            LOG.info( "successfully locaded image " + url + " from URL" );
            return image;
        }
        catch ( IOException e )
        {
            LOG.info( "could not load image " + url + " from URL" );
        }
        return null;
    }

    protected void writeImage( final String name, final BufferedImage image )
    {
        throw new UnsupportedOperationException( "file upload not supported" );
    }

    @Override
    protected boolean internalIsCached( final String name )
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

    @Override
    public String toString()
    {
        return "UrlImageCache[" + url + "," + delegateImageLoader + "]";
    }
}
