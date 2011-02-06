package ch.jfactory.resource;

import ch.jfactory.cache.ImageCacheException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.java.jveez.vfs.SimplePicture;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 06.02.11 16:30
 */
public class CachedImagePicture implements SimplePicture
{
    private final CachedImage image;

    public CachedImagePicture( final CachedImage image, final String name )
    {
        this.image = image;
    }

    public Image loadImage()
            throws ImageCacheException
    {
        return image.loadImage();
    }

    public String getName()
    {
        return image.getName();
    }

    public Dimension getSize()
    {
        return image.getSize();
    }

    public boolean isLoaded( final boolean thumb )
    {
        return image.isLoaded( thumb );
    }

    public void attach( final AsyncPictureLoaderListener listener )
    {
        image.attach( listener );
    }

    public void detach( final AsyncPictureLoaderListener listener )
    {
        image.detach( listener );
    }

    public void detachAll()
    {
        image.detachAll();
    }

    public BufferedImage getImage( final boolean thumb )
    {
        return image.getImage( thumb );
    }
}
