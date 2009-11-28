/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.domain;

import com.jgoodies.binding.beans.Model;

/**
 * Authentication credentials.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public class Credentials extends Model
{
    /**
     * Password property key.
     */
    public static final String PROPERTYNAME_PASSWORD = "password";

    /**
     * Username property key.
     */
    public static final String PROPERTYNAME_USERNAME = "username";

    private String username;

    private String password;

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the name to set
     */
    public void setUsername( final String username )
    {
        final String oldUsername = getUsername();
        this.username = username;
        firePropertyChange( PROPERTYNAME_USERNAME, oldUsername, username );
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword( final String password )
    {
        final String oldPassword = getPassword();
        this.password = password;
        firePropertyChange( PROPERTYNAME_PASSWORD, oldPassword, password );
    }
}
