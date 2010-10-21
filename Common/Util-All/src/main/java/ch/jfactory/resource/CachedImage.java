/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.awt.Dimension;
import java.awt.Image;
import java.lang.ref.SoftReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a SoftReference to an Image loaded from
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CachedImage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CachedImage.class );

    private final String pictureURL;

    private final AbstractAsyncPictureLoaderSupport listeners = new AbstractAsyncPictureLoaderSupport();

    private final CachedImageLocator locator;

    private Dimension size;

    private boolean imageLoaded = false;

    private boolean thumbNailLoaded = false;

    private SoftReference<Image> image;

    private SoftReference<Image> thumbNail;

    public CachedImage( final CachedImageLocator locator, final String pictureURL )
    {
        this.pictureURL = pictureURL;
        this.locator = locator;
    }

    public synchronized Image loadImage( final boolean thumb )
    {
        final Image i = PictureLoader.load( locator.getPath() + pictureURL, thumb );
        setImage( i, thumb );
        return i;
    }

    public String getName()
    {
        return pictureURL;
    }

    public Dimension getSize()
    {
        Dimension d = size;
        if ( d == null )
        {
            d = PictureLoader.getSize( locator.getPath() + pictureURL );
            setSize( d );
        }
        return d;
    }

    /**
     * Looking for a thumbnail, returns whether the thumb or image has been loaded. Looking for an image, returns
     * whether the image has been loaded.
     *
     * @param thumb whether to look for a thumbnail
     * @return whether the thumb or image is loaded
     */
    public boolean loaded( final boolean thumb )
    {
        return getImage( thumb ) != null || thumb && ( getImage( false ) != null );
    }

    public void attach( final AsyncPictureLoaderListener list )
    {
        listeners.attach( list );
    }

    public void detach( final AsyncPictureLoaderListener list )
    {
        listeners.detach( list );
    }

    public Image getImage( final boolean thumb )
    {
        Image image;
        if ( thumb )
        {
            image = getThumbnail();
            if ( image == null )
            {
                image = getFullImage();
            }
        }
        else
        {
            image = getFullImage();
        }
        return image;
    }

    private void setImage( final Image image, final boolean thumb )
    {
        LOGGER.debug( "Setting image for " + pictureURL + ", " + thumb );
        if ( image == getImage( thumb ) )
        {
            return;
        }
        synchronized ( this )
        {
            if ( thumb )
            {
                final Image img = ( this.image == null ) ? null : this.image.get();
                if ( img == null )
                {
                    thumbNailLoaded = true;
                    thumbNail = new SoftReference<Image>( image );
                }
            }
            else
            {
                imageLoaded = true;
                thumbNailLoaded = false;
                thumbNail = null;
                this.image = new SoftReference<Image>( image );
            }
        }
        if ( image == null )
        {
            listeners.informAborted( pictureURL );
        }
        else
        {
            listeners.informFinished( pictureURL, image, thumb );
        }
    }

    private void setSize( final Dimension d )
    {
        size = d;
    }

    private Image getFullImage()
    {
        final Image image = this.image == null ? null : this.image.get();
        if ( imageLoaded && image == null )
        {
            LOGGER.debug( "soft reference lost" );
            imageLoaded = false;
        }
        return image;
    }

    private Image getThumbnail()
    {
        final Image image = thumbNail == null ? null : thumbNail.get();
        if ( thumbNailLoaded && image == null )
        {
            LOGGER.debug( "soft reference lost" );
            thumbNailLoaded = false;
        }
        return image;
    }
}
