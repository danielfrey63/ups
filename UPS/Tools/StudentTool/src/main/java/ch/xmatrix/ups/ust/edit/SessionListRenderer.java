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

import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.MainModel;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:19 $
 */
public class SessionListRenderer implements ListCellRenderer
{
    private static final Logger LOG = Logger.getLogger( SessionListRenderer.class );

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat( "dd.MM.yyyy" );

    private JPanel panel;

    private JLabel exam;

    private JLabel session;

    private JLabel due;

    private JLabel dueInfo;

    private JLabel constraints;

    private JLabel constraintsInfo;

    private JLabel taxa;

    private JLabel taxaInfo;

    public SessionListRenderer()
    {
        try
        {
            final FormCreator creator = new FormCreator( FormLoader.load( "ch/xmatrix/ups/ust/edit/ExamInfoPanel.jfd" ) );
            creator.createAll();
            panel = creator.getPanel( "mainPanel" );
            exam = creator.getLabel( "labelExam" );
            session = creator.getLabel( "labelExamInfo" );
            due = creator.getLabel( "labelDue" );
            dueInfo = creator.getLabel( "labelDueInfo" );
            constraints = creator.getLabel( "labelConstraints" );
            constraintsInfo = creator.getLabel( "labelConstraintsInfo" );
            taxa = creator.getLabel( "labelTaxa" );
            taxaInfo = creator.getLabel( "labelTaxaInfo" );
        }
        catch ( Exception e )
        {
            LOG.error( "error while building form", e );
        }

    }

    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final SessionModel sessionModel = (SessionModel) value;
        session.setText( sessionModel.getName() );
        session.setToolTipText( sessionModel.getDescription() );
        dueInfo.setText( FORMAT.format( sessionModel.getDue() ) );
        dueInfo.setToolTipText( sessionModel.getDescription() );
        final Constraints constraintsModel = (Constraints) MainModel.findModel( sessionModel.getConstraintsUid() );
        constraintsInfo.setText( constraintsModel.getName() );
        constraintsInfo.setToolTipText( constraintsModel.getDescription() );
        final TaxonTree taxaModel = TaxonModels.find( constraintsModel.getTaxaUid() );
        taxaInfo.setText( taxaModel.getName() );

        exam.setForeground( isSelected ? Color.white : Color.black );
        session.setForeground( isSelected ? Color.white : Color.black );
        due.setForeground( isSelected ? Color.white : Color.black );
        dueInfo.setForeground( isSelected ? Color.white : Color.black );
        constraints.setForeground( isSelected ? Color.white : Color.black );
        constraintsInfo.setForeground( isSelected ? Color.white : Color.black );
        taxa.setForeground( isSelected ? Color.white : Color.black );
        taxaInfo.setForeground( isSelected ? Color.white : Color.black );

        final Color bg1 = list.getSelectionBackground();
        final Color bg2 = new Color( bg1.getRed(), bg1.getGreen(), bg1.getBlue(), 50 );
        panel.setBackground( isSelected ? bg1 : ( index % 2 == 0 ? bg2 : list.getBackground() ) );
        panel.setForeground( isSelected ? list.getSelectionForeground() : list.getForeground() );

        return panel;
    }
}
