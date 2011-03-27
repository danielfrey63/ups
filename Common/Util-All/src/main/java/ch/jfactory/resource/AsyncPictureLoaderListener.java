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
 * This is the listener Interface for the AsyncPictureLoader.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface AsyncPictureLoaderListener
{
    /**
     * Is called if the loading of the image is finished.
     *
     * @param name  name of the image
     * @param img   reference to the image
     * @param thumb is it a thumbnail?
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
