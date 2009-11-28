/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.io.File;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 * Helper class for locating images and support for image caching.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/04/25 11:09:31 $
 */
public class ImageLocator
{
    public static final String PROPERTY_IMAGE_LOCATION = "xmatrix.picture.path";

    public static final CachedImageLocator pictLocator;

    private static final Logger LOGGER = Logger.getLogger( ImageLocator.class );

    private static final CachedImageLocator iconLocator;

    private static final String ICON_PATH;

    /**
     * get an ImageIcon from Cache or reloads it into the cache.
     *
     * @param name name of the image
     * @return reference to the ImageIcon
     */
    public static ImageIcon getIcon( final String name )
    {
        if ( name == null )
        {
            return null;
        }
        LOGGER.debug( "icon " + name );
        return iconLocator.getImageIcon( name );
    }

    public static ImageIcon getPicture( final String name )
    {
        return pictLocator.getImageIcon( name );
    }

    //public static void clearCache() {
    //    pictLocator.clearCacheList();
    //}

    static
    {
        String iconPath = System.getProperty( "jfactory.resource.path" );
        if ( iconPath == null )
        {
            iconPath = System.getProperty( "user.dir" );
            LOGGER.warn( "system property \"jfactory.resource.path\" not found, defaulting to \"" + iconPath + "\"" );
        }
        ICON_PATH = ( iconPath.endsWith( "/" ) || iconPath.endsWith( "\\" ) ? iconPath : iconPath + "/" );
        iconLocator = new CachedImageLocator()
        {
            public String getPath()
            {
                return ICON_PATH;
            }
        };
        pictLocator = new CachedImageLocator()
        {
            public String getPath()
            {
                final String root = System.getProperty( "xmatrix.cd.path", "" );
                final String defaultPicturePath = System.getProperty( "xmatrix.picture.path.small", "/" );
                final String picturePath = System.getProperty( PROPERTY_IMAGE_LOCATION, defaultPicturePath );
                if ( root.length() > 0 )
                {
                    return new File( root, picturePath ).getPath() + "/";
                }
                else
                {
                    return new File( picturePath ).getPath() + "/";
                }
            }
        };

        LOGGER.info( "Icon resources at " + iconLocator.getPath() );
        LOGGER.info( "Pictures resources at " + pictLocator.getPath() );
    }
}
