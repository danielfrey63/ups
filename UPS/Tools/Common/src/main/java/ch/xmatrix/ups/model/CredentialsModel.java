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
package ch.xmatrix.ups.model;

import ch.xmatrix.ups.domain.Credentials;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class CredentialsModel extends PresentationModel
{
    private final ValidationResultModel validationResultModel;

    public CredentialsModel( final Credentials credentials )
    {
        super( credentials );
        validationResultModel = new DefaultValidationResultModel();
        final PropertyChangeListener handler = new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final PropertyValidationSupport support = new PropertyValidationSupport( credentials, "Credentials" );
                if ( ValidationUtils.isBlank( credentials.getPassword() ) )
                {
                    support.addError( "Credentials.Password", "Das Passwort darf nicht leer sein" );
                }
                if ( ValidationUtils.isBlank( credentials.getUsername() ) )
                {
                    support.addError( "Credentials.Username", "Der Benutzername darf nicht leer sein" );
                }
                getValidationResultModel().setResult( support.getResult() );
            }
        };
        getBeanChannel().addValueChangeListener( handler );
        addBeanPropertyChangeListener( handler );
    }

    public ValidationResultModel getValidationResultModel()
    {
        return validationResultModel;
    }
}
