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

import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.ImageRetrieveException;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a SoftReference to an Image loaded from.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CachedImage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CachedImage.class );

    private final String name;

    private final AbstractAsyncPictureLoaderSupport listeners = new AbstractAsyncPictureLoaderSupport();

    private final ImageCache locator;

    private Dimension size;

    private boolean imageLoaded = false;

    private SoftReference<BufferedImage> softImageReference;

    public CachedImage( final ImageCache locator, final String name )
    {
        this.name = name;
        this.locator = locator;
    }

    public synchronized BufferedImage loadImage() throws ImageRetrieveException, ImageCacheException
    {
        final BufferedImage i;
        try
        {
            i = locator.getImage( name );
            setImage( i );
            return i;
        }
        catch ( ImageRetrieveException e )
        {
            LOGGER.error( "cannot retrieve image " + name, e );
            throw e;
        }
        catch ( ImageCacheException e )
        {
            LOGGER.error( "cannot retrieve image " + name, e );
            throw e;
        }
    }

    public String getName()
    {
        return name;
    }

    public Dimension getSize()
    {
        Dimension d = size;
        if ( d == null && softImageReference != null && softImageReference.get() != null )
        {
            final BufferedImage image = this.softImageReference.get();
            if ( image != null )
            {
                d = new Dimension( image.getWidth(), image.getHeight() );
                LOGGER.debug( "reading size for image \"" + name + "\" with width " + d.width + ", height " + d.height );
            }
            else
            {
                d = new Dimension( -1, -1 );
            }
            setSize( d );
        }
        return d;
    }

    /**
     * Looking for a thumbnail, returns whether the thumb or image has been loaded. Looking for an image, returns whether the image has been loaded.
     *
     * @param thumb whether to look for a thumbnail
     * @return whether the thumb or image is loaded
     */
    public boolean isLoaded( final boolean thumb )
    {
        return getImage( thumb ) != null || thumb && (getImage( false ) != null);
    }

    public void attach( final AsyncPictureLoaderListener listener )
    {
        listeners.attach( listener );
    }

    public void detach( final AsyncPictureLoaderListener listener )
    {
        listeners.detach( listener );
    }

    public void detachAll()
    {
        listeners.detachAll();
    }

    public BufferedImage getImage( final boolean thumb )
    {
        final BufferedImage image = softImageReference == null ? null : softImageReference.get();
        if ( imageLoaded && image == null )
        {
            LOGGER.debug( "soft reference lost" );
            imageLoaded = false;
        }
        return image;
    }

    private void setImage( final BufferedImage image )
    {
        LOGGER.trace( "setting image for \"" + name + "\"" );
        if ( image == getImage( false ) )
        {
            return;
        }
        synchronized ( this )
        {
            imageLoaded = true;
            softImageReference = new SoftReference<BufferedImage>( image );
        }
        if ( image == null )
        {
            listeners.informAborted( name );
        }
        else
        {
            listeners.informFinished( name, image );
        }
    }

    private void setSize( final Dimension d )
    {
        size = d;
    }
}
