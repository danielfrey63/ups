/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.resource;

import ch.jfactory.cache.FileImageCache;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.NestedImageCache;
import ch.jfactory.cache.ResourceImageCache;
import ch.jfactory.cache.UrlImageCache;
import ch.jfactory.cache.WeakInMemoryCache;
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

    public static final NestedImageCache PICT_LOCATOR;

    private static final Logger LOGGER = LoggerFactory.getLogger( ImageLocator.class );

    private static final ImageCache ICON_LOCATOR;

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
        try
        {
            return new ImageIcon( ICON_LOCATOR.getImage( name ) );
        }
        catch ( ImageCacheException e )
        {
            LOGGER.error( "cannot retrieve icon for " + name, e );
            return new ImageIcon( name );
        }
    }

    public static ImageIcon getPicture( final String name )
    {
        try
        {
            return new ImageIcon( PICT_LOCATOR.getImage( name ) );
        }
        catch ( ImageCacheException e )
        {
            LOGGER.error( "cannot retrieve picture for " + name, e );
            return new ImageIcon( name );
        }
    }

    static
    {
        ICON_LOCATOR = new ResourceImageCache( getIconPath() );
        final ImageCache fileSystemCache = new FileImageCache( getPicturePath(), "jpg" );
        final ImageCache weakInMemoryCache = new WeakInMemoryCache();
        final ImageCache urlImageCache = new UrlImageCache( getImageURL(), "jpg" );
        PICT_LOCATOR = new NestedImageCache( new ImageCache[]{weakInMemoryCache, fileSystemCache}, weakInMemoryCache, fileSystemCache, urlImageCache );

        LOGGER.info( "icon resources at " + ICON_LOCATOR );
        LOGGER.info( "pictures resources at " + PICT_LOCATOR );
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
        final String url = System.getProperty( "xmatrix.cache.local.path", System.getProperty( "user.home" ).replace( '\\', '/' ) + "/.hcd2/" );
        return new File( url ).getAbsolutePath();
    }

    public static String getImageURL()
    {
        final String url = System.getProperty( "xmatrix.cache.net.path", "<no image URL defined>" );
        LOGGER.info( "image URL is " + url );
        return url;
    }
}
