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
package ch.xmatrix.ups.ust.edit;

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.MainModel;
import ch.xmatrix.ups.ust.main.UserModel;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2007/05/16 17:00:15 $
 */
public class ExamInfoBuilder extends JPanel
{
    private final JLabel session = new JLabel();

    private final JLabel due = new JLabel();

    private final JLabel constraints = new JLabel();

    private final JLabel taxa = new JLabel();

    private static final Format FORMAT = new SimpleDateFormat( "dd.MM.yyyy" );

    public void setModel( final UserModel userModel )
    {
        final SessionModel session = (SessionModel) MainModel.findModel( userModel.getExamInfoUid() );
        final Constraints constraintsModel = (Constraints) AbstractMainModel.findModel( userModel.getConstraintsUid() );
        final TaxonTree tree = TaxonModels.find( constraintsModel.getTaxaUid() );
        this.session.setText( session.getName() );
        this.session.setToolTipText( session.getDescription() );
        due.setText( FORMAT.format( session.getDue() ) );
        due.setToolTipText( session.getDescription() );
        constraints.setText( constraintsModel.getName() );
        constraints.setToolTipText( constraintsModel.getDescription() );
        taxa.setText( tree.getName() );
        taxa.setToolTipText( tree.getDescription() );
    }

    public JPanel getPanel()
    {
        final FormLayout layout = new FormLayout(
                "8dlu, r:p, 3dlu, r:p, p:g, 8dlu",
                "8dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 8dlu" );
        setLayout( layout );
        final CellConstraints cc = new CellConstraints();
        final ComponentFactory factory = DefaultComponentFactory.getInstance();
        add( factory.createSeparator( Strings.getString( "info.exam.details" ), SwingConstants.LEFT ), cc.xyw( 2, 2, 4 ) );
        add( new JLabel( Strings.getString( "info.exam.details.session" ) ), cc.xy( 2, 4 ) );
        add( session, cc.xyw( 4, 4, 2 ) );
        add( new JLabel( Strings.getString( "info.exam.details.due" ) ), cc.xy( 2, 6 ) );
        add( due, cc.xyw( 4, 6, 2 ) );
        add( new JLabel( Strings.getString( "info.exam.details.constraints" ) ), cc.xy( 2, 8 ) );
        add( constraints, cc.xyw( 4, 8, 2 ) );
        add( new JLabel( Strings.getString( "info.exam.details.taxa" ) ), cc.xy( 2, 10 ) );
        add( taxa, cc.xyw( 4, 10, 2 ) );

        return this;
    }
}
