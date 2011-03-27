/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.update;

import java.io.IOException;

/**
 * Implementation of this interface represent a part of the application (module, database) fot the application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public interface UpdateModule
{
    /**
     * This method should be overwritten to implement the update behavior. Maybe download the jar file and overwrite the local version.
     *
     * @throws IOException an exception which occurs during update
     */
    void update() throws IOException;

    /**
     * this method return the version information of the local installed version of the fragment.
     *
     * @return version information
     */
    VersionInfo getLocalVersionInfo();

    /**
     * this method return the version information about the server update version.
     *
     * @return version information;
     */
    VersionInfo getServerVersionInfo();
}
