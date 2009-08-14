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
package ch.xmatrix.ups.view.renderer;

import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * Groups tree node renderer. The usual taxons node is postfixed by the group in parantesis if the taxon is assigned to
 * one.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/04/06 11:01:11 $
 */
public class ConstraintsTreeRenderer implements TreeCellRenderer
{

    private static final ArrayList<String> DUMMY_LIST = new ArrayList<String>();

    private final RendererPanel panel = new RendererPanel();

    private final JCheckBox checkbox = new JCheckBox();

    private Constraints models;

    private ArrayList<String> taxa = DUMMY_LIST;

    private boolean enabled;

    public ConstraintsTreeRenderer()
    {
        checkbox.setOpaque(false);
        panel.setPrefixComponent(checkbox);
    }

    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected,
                                                  final boolean expanded, final boolean leaf, final int row,
                                                  final boolean hasFocus)
    {
        final SimpleTaxon taxon = (SimpleTaxon) value;
        final String name = taxon.getName();
        final Constraint constraints = models == null ? null : models.findConstraint(name);
        final List<String> taxaInConstraint = constraints == null ? DUMMY_LIST : constraints.getTaxa();
        final boolean singleTaxon = taxaInConstraint != null && taxaInConstraint.size() == 1;
        final boolean isSpecies = SimpleTaxon.isSpecies(taxon);
        final boolean isInDefaultTaxa = taxa.contains(name);
        final boolean isObligate = singleTaxon && name.equals(taxaInConstraint.get(0));
        ConstraintsRendererUtils.configureForConstraint(panel, models, constraints, taxa, singleTaxon);
        checkbox.setSelected(isInDefaultTaxa);
        panel.setSelected(selected);
        panel.setEnabled(enabled);
        panel.setIcon(ImageLocator.getIcon(TaxonRendererUtils.getIconForTaxon(taxon, false)));
        panel.setText(taxon.getName());
        panel.setSelected(selected);
        panel.setPrefixEnabled(enabled && !isObligate);
        panel.setShowPrefixComponent(isSpecies);
        panel.update();
        return panel;
    }

    public void setConstraints(final Constraints models)
    {
        this.models = models;
    }

    public void setTaxa(final ArrayList<String> taxa)
    {
        this.taxa = taxa == null ? DUMMY_LIST : taxa;
    }

    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }
}
