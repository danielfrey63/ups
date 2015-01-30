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

import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.cache.ImageRetrieveException;
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

    public Image loadImage() throws ImageCacheException, ImageRetrieveException
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
