/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
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
        for ( final AsyncPictureLoaderListener listener : listeners )
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
        for ( final AsyncPictureLoaderListener listener : listeners )
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
        for ( final AsyncPictureLoaderListener listener : listeners )
        {
            listener.loadAborted( name );
        }
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + listeners + "]";
    }
}
