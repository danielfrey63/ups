/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import ch.jfactory.cache.ImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for locating images and support for image caching.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/04/25 11:09:31 $
 */
public class ImageLocator
{
    public static final String PROPERTY_IMAGE_LOCATION = "xmatrix.picture.path";

    public static final ImageLoader pictLocator;

    private static final Logger LOGGER = LoggerFactory.getLogger( ImageLocator.class );

    private static final ImageLoader iconLocator;

    /**
     * get an ImageIcon from Cache or reloads it into the cache.
     *
     * @param name name of the image
     * @return reference to the ImageIcon
     */
    public static ImageIcon getIcon( final String name )
    {
        if ( name == null || "".equals( name ) )
        {
            return null;
        }
        LOGGER.trace( "icon " + name );
        final BufferedImage image = iconLocator.getImage( name );
        return image == null ? new ImageIcon( name ) : new ImageIcon( image );
    }

    public static ImageIcon getPicture( final String name )
    {
        final BufferedImage image = pictLocator.getImage( name );
        return image == null ? new ImageIcon( name ) : new ImageIcon( image );
    }

    static
    {
        iconLocator = new WeakInMemoryCache( new ResourceImageCache( null, getIconPath() ) );
        pictLocator = new WeakInMemoryCache( new FileImageCache( new UrlImageCache( null, getImageURL() ), getPicturePath(), "jpg" ) );

        LOGGER.info( "icon resources at " + iconLocator );
        LOGGER.info( "pictures resources at " + pictLocator );
    }

    public static String getIconPath()
    {
        String iconPath = System.getProperty( "xmatrix.resource.path" );
        if ( iconPath == null )
        {
            iconPath = System.getProperty( "user.dir" );
            LOGGER.warn( "system property \"xmatrix.resource.path\" not found, defaulting to \"" + iconPath + "\"" );
        }
        return iconPath.endsWith( "/" ) || iconPath.endsWith( "\\" ) ? iconPath : iconPath + "/";
    }

    public static String getPicturePath()
    {
        return new File( System.getProperty( "xmatrix.cd.path", "" ) ).getPath() + "/";
    }

    public static String getImageURL()
    {
        final String url = System.getProperty( "xmatrix.url.path", "<no image URL defined>" );
        LOGGER.info( "image URL is " + url );
        return url;
    }
}
