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
 * This is the listener Interface for the Asynchron Picture Loader.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface AsynchronPictureLoaderListener
{
    /**
     * Is called if the loading of the image is finished.
     *
     * @param name name of the image
     * @param img  reference to the image
     */
    void loadFinished( String name, Image img, boolean thumb );

    /**
     * Is called if the loading of a image is aborted.
     *
     * @param name name of the image
     */
    void loadAborted( String name );

    /**
     * Is called if the image is about to be loaded.
     *
     * @param name name of the image
     */
    void loadStarted( String name );
}
