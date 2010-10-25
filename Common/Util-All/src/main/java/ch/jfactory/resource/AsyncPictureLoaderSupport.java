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
 * AsyncPictureLoaderSupport should be implemented by all classes which delegate listeners to AsyncPictureLoader
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ created: 23. Mai 2002
 */
interface AsyncPictureLoaderSupport
{
    /**
     * Inform all listeners that the image loading is finished.
     *
     * @param name  name of the image
     * @param image reference to the image
     */
    void informFinished( String name, Image image );

    /**
     * Inform all listeners that the image will be.
     *
     * @param name name of the image
     */
    void informAborted( String name );

    /**
     * Inform all listeners that the image will be loaded.
     *
     * @param name name of the image
     */
    void informStarted( String name );

    /**
     * Register a AsyncPictureLoaderListener.
     *
     * @param listener reference to listener
     */
    void detach( AsyncPictureLoaderListener listener );

    /**
     * Un-register a AsyncPictureLoaderListener.
     *
     * @param listener reference to listener
     */
    void attach( AsyncPictureLoaderListener listener );
}
