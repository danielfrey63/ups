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
package ch.xmatrix.ups.view;

import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.xmatrix.ups.domain.Credentials;
import ch.xmatrix.ups.model.CredentialsModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * A dialog to show a user and password field and calls doApply upon successful validation.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2006/08/29 13:33:33 $
 */
public abstract class CredentialsDialog extends I15nComponentDialog
{

    protected CredentialsModel model;

    public CredentialsDialog(final JFrame parent)
    {
        super(parent, "credentials");
    }

    public CredentialsModel getModel()
    {
        return model;
    }

    protected JComponent createComponentPanel()
    {
        this.model = new CredentialsModel(new Credentials());
        model.getValidationResultModel().addPropertyChangeListener(ValidationResultModel.PROPERTYNAME_RESULT,
                new PropertyChangeListener()
                {
                    public void propertyChange(final PropertyChangeEvent evt)
                    {
                        final ValidationResult result = (ValidationResult) evt.getNewValue();
                        enableApply(result.getErrors().size() == 0);
                    }
                });
        return new CredentialsPanel(model);
    }

    protected void onCancel()
    {
        model.triggerFlush();
    }

    protected void onApply() throws I15nComponentDialog.ComponentDialogException
    {
        model.triggerCommit();
        doApply();
    }

    protected abstract void doApply() throws ComponentDialogException;
}
