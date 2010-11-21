/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for locating images and support for image caching.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CachedImageLocator
{
    /** This class' logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger( CachedImageLocator.class );

    /** Map containing a cache for images especially icons. */
    private static final Map<String, WeakReference<ImageIcon>> imageCache = new WeakHashMap<String, WeakReference<ImageIcon>>();

    private final String path;

    public CachedImageLocator( final String path )
    {
        this.path = path;
    }

    /**
     * Get an ImageIcon from Cache or reloads it into the cache.
     *
     * @param name name of the image
     * @return reference to the ImageIcon
     */
    public ImageIcon getImageIcon( final String name )
    {
        LOGGER.trace( "request for " + name );
        WeakReference<ImageIcon> ref = imageCache.get( name );
        if ( ( ref == null ) || ( ref.get() == null ) )
        {
            final String fullName = locate( name );
            final URL url = getClass().getResource( fullName );
            final ImageIcon icon = url == null ? new ImageIcon( fullName ) : new ImageIcon( url );
            imageCache.put( name, ref = new WeakReference<ImageIcon>( icon ) );
        }

        return ref.get();
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
    public String toString()
    {
        return "CachedImageLocator[" + path + "]";
    }
}
