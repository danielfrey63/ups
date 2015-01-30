/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.cache;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Loads files from the file system.
 *
 * @author Daniel Frey 21.11.2010 14:12:19
 */
public class ResourceImageCache implements ImageCache
{
    /**
     * The classpath prefix to search for images.
     */
    private final String path;

    public ResourceImageCache( final String path )
    {
        this.path = (path.endsWith( "/" ) || path.endsWith( "\\" ) ? path.substring( 0, path.length() - 1 ) : path) + "/";
    }

    public BufferedImage getImage( final String name ) throws ImageRetrieveException
    {
        final String path = locate( name );
        try
        {
            final InputStream stream = getClass().getResourceAsStream( path );
            if ( stream == null )
            {
                throw new ImageRetrieveException( "could not retrieve image " + name + " from " + path, new NullPointerException( "resource at path " + path + " not found" ) );
            }
            final BufferedImage image = ImageIO.read( stream );
            stream.close();
            return image;
        }
        catch ( Throwable e )
        {
            throw new ImageRetrieveException( "could not load resource " + path, e );
        }
    }

    public void setImage( final String name, final BufferedImage image )
    {
        throw new UnsupportedOperationException( "cannot save to classpath" );
    }

    public boolean isCached( final String name )
    {
        return getClass().getResource( name ) != null;
    }

    public void invalidateCache()
    {
        throw new UnsupportedOperationException( "invalidate in class path not supported" );
    }

    @Override
    public String toString()
    {
        return "ResourceImageCache[" + path + "]";
    }

    private String locate( final String name )
    {
        return path + name;
    }
}
