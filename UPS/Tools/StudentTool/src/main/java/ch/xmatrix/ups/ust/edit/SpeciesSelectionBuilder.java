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
import ch.jfactory.application.view.search.Searchable;
import ch.jfactory.application.view.search.SearchableUtils;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.UserModel;
import ch.xmatrix.ups.view.renderer.RendererPanel;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

/**
 * Bulids a panel to select species which get selected in the taxon tree.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2007/05/16 17:00:16 $
 */
public class SpeciesSelectionBuilder extends ActionCommandPanelBuilder {

    private JList list = new JList();

    public void setModel(final UserModel userModel) {
        final Constraints constraints = (Constraints) AbstractMainModel.findModel(userModel.getConstraintsUid());
        final TaxonTree tree = TaxonModels.find(constraints.getTaxaUid());
        final SimpleTaxon[] taxa = getSpecies(tree.getRootTaxon()).toArray(new SimpleTaxon[0]);
        Arrays.sort(taxa, new ToStringComparator());
        list.setListData(taxa);
    }

    public void addListSelectionListener(final ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    protected JComponent createMainPanel() {
        final TaxaListRenderer renderer = new TaxaListRenderer();
        list.setBorder(new EmptyBorder(3, 3, 3, 3));
        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(renderer);
        final Searchable searchable = SearchableUtils.installSearchable(list);
        searchable.setBackground(null);
        return new JScrollPane(list);
    }

    private ArrayList<SimpleTaxon> getSpecies(final SimpleTaxon taxon) {
        final ArrayList<SimpleTaxon> result = new ArrayList<SimpleTaxon>();
        if (SimpleTaxon.isSpecies(taxon)) {
            result.add(taxon);
        }
        final ArrayList<SimpleTaxon> children = taxon.getChildTaxa();
        for (int i = 0; children != null && i < children.size(); i++) {
            final SimpleTaxon child = children.get(i);
            result.addAll(getSpecies(child));
        }
        return result;
    }

    private class TaxaListRenderer implements ListCellRenderer {

        private RendererPanel panel = new RendererPanel(RendererPanel.SelectionType.ALL);

        public TaxaListRenderer() {
            panel.setEnabled(true);
        }

        public Component getListCellRendererComponent(final JList list, final Object value, final int index,
                                                      final boolean selected, final boolean hasFocus) {
            final SimpleTaxon taxon = (SimpleTaxon) value;
            final ImageIcon icon = ImageLocator.getIcon(TaxonRendererUtils.getIconForTaxon(taxon, selected));
            panel.setIcon(icon);
            panel.setText(taxon.getName());
            panel.setSelected(selected);
            panel.update();
            return panel;
        }
    }
}
