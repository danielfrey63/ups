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
