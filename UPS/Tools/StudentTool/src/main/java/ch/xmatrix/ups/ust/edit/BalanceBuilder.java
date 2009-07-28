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

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.resource.Strings;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.UserModel;
import ch.xmatrix.ups.view.renderer.ConstraintsRendererUtils;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2007/05/16 17:00:15 $
 */
public class BalanceBuilder extends ActionCommandPanelBuilder {

    private static final ImageIcon OK = new ImageIcon(BalanceBuilder.class.getResource("/16x16/fill/bulb_ok.png"));
    private static final ImageIcon NOK = new ImageIcon(BalanceBuilder.class.getResource("/16x16/fill/bulb_nok.png"));

    private UserModel userModel;
    private ArrayList<StatsLabel> statLabels = new ArrayList<StatsLabel>();
    private JLabel icon = new IconLabel();

    public void setModel(final UserModel userModel) {
        this.userModel = userModel;
        for (int i = 0; i < statLabels.size(); i++) {
            final StatsLabel label = (StatsLabel) statLabels.get(i);
            label.repaint();
        }
        icon.repaint();
    }

    public ArrayList<JComponent> getComponentsToRepaint() {
        final ArrayList<JComponent> result = new ArrayList<JComponent>();
        result.addAll(statLabels);
        result.add(icon);
        return result;
    }

    public JComponent createMainPanel() {
        final JComponent panel = super.createMainPanel();
        final ComponentFactory factory = DefaultComponentFactory.getInstance();
        final CellConstraints cc = new CellConstraints();
        final FormLayout layout = new FormLayout(
                "8dlu, l:p, 8dlu, l:p:g(1.0), 3dlu, r:p, 8dlu",
                "8dlu, p, 3dlu, p, 3dlu, p, 8dlu, p, 3dlu, p, 3dlu, p, 8dlu");
        panel.setLayout(layout);

        panel.add(factory.createSeparator(Strings.getString("info.balance.species"), SwingConstants.LEFT), cc.xyw(2, 2, 5));
        panel.add(new JLabel(Strings.getString("info.balance.species.fixed")), cc.xy(4, 4));
        panel.add(createGivenSpeciesStat(), cc.xy(6, 4));
        panel.add(new JLabel(Strings.getString("info.balance.species.selected")), cc.xy(4, 6));
        panel.add(createSelectedSpeciesStat(), cc.xy(6, 6));

        panel.add(factory.createSeparator(Strings.getString("info.balance.constraints"), SwingConstants.LEFT), cc.xyw(2, 8, 5));
        panel.add(new JLabel(Strings.getString("info.balance.constraints.count")), cc.xy(4, 10));
        panel.add(createGivenConstraintsStat(), cc.xy(6, 10));
        panel.add(new JLabel(Strings.getString("info.balance.constraints.complete")), cc.xy(4, 12));
        panel.add(createCompletedConstraintsStat(), cc.xy(6, 12));
        panel.add(icon, cc.xywh(2, 10, 1, 3, "l, c"));

        return panel;
    }

    private StatsLabel createGivenSpeciesStat() {
        final StatsLabel label = new StatsLabel() {
            public int getStatistic(final Constraints constraints) {
                int count = 0;
                for (int i = 0; i < constraints.getConstraints().size(); i++) {
                    final Constraint constraint = (Constraint) constraints.getConstraints().get(i);
                    final List<String> taxa = constraint.getTaxa();
                    if (taxa.size() == 1) {
                        final TaxonTree tree = TaxonModels.find(constraints.getTaxaUid());
                        final SimpleTaxon taxon = tree.findTaxonByName(taxa.get(0));
                        if (SimpleTaxon.isSpecies(taxon)) {
                            count++;
                        }
                    }
                }
                return count;
            }
        };
        statLabels.add(label);
        return label;
    }

    private StatsLabel createSelectedSpeciesStat() {
        final StatsLabel label = new StatsLabel() {
            public int getStatistic(final Constraints constraints) {
                return userModel.getTaxa().size();
            }
        };
        statLabels.add(label);
        return label;
    }

    private StatsLabel createGivenConstraintsStat() {
        final StatsLabel label = new StatsLabel() {
            public int getStatistic(final Constraints constraints) {
                return constraints.getConstraints().size();
            }
        };
        statLabels.add(label);
        return label;
    }

    private StatsLabel createCompletedConstraintsStat() {
        final StatsLabel label = new StatsLabel() {
            public int getStatistic(final Constraints constraints) {
                final ArrayList<String> taxa = userModel.getTaxa();
                return ConstraintsRendererUtils.getCompleteConstraints(constraints, taxa);
            }
        };
        statLabels.add(label);
        return label;
    }

    private abstract class StatsLabel extends JLabel {

        public StatsLabel() {
            super("-");
            setHorizontalTextPosition(JLabel.LEFT);
        }

        public void repaint() {
            if (userModel != null) {
                final Constraints constraints = (Constraints) AbstractMainModel.findModel(userModel.getConstraintsUid());
                final int statistic = getStatistic(constraints);
                setText("" + statistic);
            }
            super.repaint();
        }

        public abstract int getStatistic(final Constraints constraints);
    }

    private class IconLabel extends JLabel {

        public void repaint() {
            if (statLabels.size() == 4) {
                final int totalCount = Integer.parseInt(statLabels.get(2).getText());
                final int actualCount = Integer.parseInt(statLabels.get(3).getText());
                if (totalCount <= actualCount) {
                    setIcon(OK);
                }
                else {
                    setIcon(NOK);
                }
            }
            super.repaint();
        }
    }
}
