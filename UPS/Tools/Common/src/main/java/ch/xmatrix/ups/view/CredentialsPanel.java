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

import ch.xmatrix.ups.domain.Credentials;
import ch.xmatrix.ups.model.CredentialsModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * A panel that shows two text fields for username and password. The fields are validated for not being empty.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/21 11:02:52 $
 */
public class CredentialsPanel extends JPanel {

    private CredentialsModel model;

    public CredentialsPanel(final CredentialsModel model) {
        this.model = model;
        initLayout();
    }

    private void initLayout() {
        setLayout(new FormLayout("8dlu, p, 4dlu, max(140dlu;p):g(1.0), 8dlu", "8dlu, p, 4dlu, p, 8dlu"));
        addLine(2, new JTextField(), "Benutzername:", Credentials.PROPERTYNAME_USERNAME, "Username");
        addLine(4, new JPasswordField(), "Passwort:", Credentials.PROPERTYNAME_PASSWORD, "Password");
        model.getValidationResultModel().addPropertyChangeListener(ValidationResultModel.PROPERTYNAME_RESULT, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                final ValidationResult result = (ValidationResult) evt.getNewValue();
                ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(CredentialsPanel.this);
                ValidationComponentUtils.updateComponentTreeMandatoryBorder(CredentialsPanel.this);
                ValidationComponentUtils.updateComponentTreeSeverity(CredentialsPanel.this, result);
                ValidationComponentUtils.updateComponentTreeSeverityBackground(CredentialsPanel.this, result);
            }
        });
    }

    private void addLine(final int line, final JTextField field, final String label, final String property, final String messageKey) {
        final CellConstraints cc = new CellConstraints();
        Bindings.bind(field, model.getModel(property), false);
        ValidationComponentUtils.setMandatory(field, true);
        ValidationComponentUtils.setMessageKey(field, messageKey);
        add(new JLabel(label), cc.xy(2, line));
        add(field, cc.xy(4, line));
    }
}
