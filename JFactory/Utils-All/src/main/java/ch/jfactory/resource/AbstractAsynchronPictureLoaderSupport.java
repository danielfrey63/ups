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
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class is an abstract implementation of the AsynchronPictureLoaderSupport.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class AbstractAsynchronPictureLoaderSupport implements AsynchronPictureLoaderSupport {
    private Vector listeners = new Vector();

    public int size() {
        return listeners.size();
    }

    public void attach(final AsynchronPictureLoaderListener listener) {
        if (listeners.indexOf(listener) < 0) {
            listeners.addElement(listener);
        }
    }

    public void detach(final AsynchronPictureLoaderListener listener) {
        listeners.removeElement(listener);
    }

    /**
     * Inform all listeners that the image will be loaded.
     *
     * @param name name of the image
     */
    public void informStarted(final String name) {
        final Enumeration e = listeners.elements();
        while (e.hasMoreElements()) {
            final AsynchronPictureLoaderListener listener = (AsynchronPictureLoaderListener) e.nextElement();
            listener.loadStarted(name);
        }
    }

    /**
     * Inform all listeners that the image loading is finished.
     *
     * @param name  name of the image
     * @param image reference to the image
     * @param thumb Description of the Parameter
     */
    public void informFinished(final String name, final Image image, final boolean thumb) {
        final Enumeration e = listeners.elements();
        while (e.hasMoreElements()) {
            final AsynchronPictureLoaderListener listener = (AsynchronPictureLoaderListener) e.nextElement();
            listener.loadFinished(name, image, thumb);
        }
    }

    /**
     * Inform all listeners that the image will be .
     *
     * @param name name of the image
     */
    public void informAborted(final String name) {
        final Enumeration e = listeners.elements();
        while (e.hasMoreElements()) {
            final AsynchronPictureLoaderListener listener = (AsynchronPictureLoaderListener) e.nextElement();
            listener.loadAborted(name);
        }
    }
}
