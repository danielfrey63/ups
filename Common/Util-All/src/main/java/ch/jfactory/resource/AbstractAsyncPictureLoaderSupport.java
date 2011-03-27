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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an abstract implementation of the AsyncPictureLoaderSupport.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class AbstractAsyncPictureLoaderSupport implements AsyncPictureLoaderSupport
{
    private final List<AsyncPictureLoaderListener> listeners = new ArrayList<AsyncPictureLoaderListener>();

    public int size()
    {
        return listeners.size();
    }

    public void attach( final AsyncPictureLoaderListener listener )
    {
        if ( listeners.indexOf( listener ) < 0 )
        {
            listeners.add( listener );
        }
    }

    public void detach( final AsyncPictureLoaderListener listener )
    {
        listeners.remove( listener );
    }

    public void detachAll()
    {
        listeners.clear();
    }

    /**
     * Inform all listeners that the image will be loaded.
     *
     * @param name name of the image
     */
    public void informStarted( final String name )
    {
        final ArrayList<AsyncPictureLoaderListener> copy = new ArrayList<AsyncPictureLoaderListener>( listeners );
        for ( final AsyncPictureLoaderListener listener : copy )
        {
            listener.loadStarted( name );
        }
    }

    /**
     * Inform all listeners that the image loading is finished.
     *
     * @param name  name of the image
     * @param image reference to the image
     */
    public void informFinished( final String name, final Image image )
    {
        final ArrayList<AsyncPictureLoaderListener> copy = new ArrayList<AsyncPictureLoaderListener>( listeners );
        for ( final AsyncPictureLoaderListener listener : copy )
        {
            listener.loadFinished( name, image, false );
        }
    }

    /**
     * Inform all listeners that the image will be .
     *
     * @param name name of the image
     */
    public void informAborted( final String name )
    {
        final ArrayList<AsyncPictureLoaderListener> copy = new ArrayList<AsyncPictureLoaderListener>( listeners );
        for ( final AsyncPictureLoaderListener listener : copy )
        {
            listener.loadAborted( name );
        }
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + hashCode() + "," + listeners.size() + "]";
    }
}
