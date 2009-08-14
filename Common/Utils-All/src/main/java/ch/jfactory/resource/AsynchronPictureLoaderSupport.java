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

/**
 * AsynchronPictureLoaderSupport should be implemented by all classes which delegate listeners to
 * AsynchronPictureLoader
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created 23. Mai 2002
 */
interface AsynchronPictureLoaderSupport {
    /**
     * Inform all listeners that the image loading is finished.
     *
     * @param name  name of the image
     * @param image reference to the image
     */
    void informFinished(String name, Image image, boolean thumb);

    /**
     * Inform all listeners that the image will be .
     *
     * @param name name of the image
     */
    void informAborted(String name);

    /**
     * Inform all listeners that the image will be loaded.
     *
     * @param name name of the image
     */
    void informStarted(String name);

    /**
     * register a AsynchronPictureLoaderListener
     *
     * @param listener reference to listener
     */
    void detach(AsynchronPictureLoaderListener listener);

    /**
     * unregister a AsynchronPictureLoaderListener
     *
     * @param listener reference to listener
     */
    void attach(AsynchronPictureLoaderListener listener);
}
