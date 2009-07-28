/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.constraints;

import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.view.renderer.ConstraintsRendererUtils;
import ch.xmatrix.ups.view.renderer.RendererPanel;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.model.TaxonModels;
import ch.jfactory.resource.ImageLocator;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2007/04/06 11:01:10 $
 */
public class ConstraintsListRenderer implements ListCellRenderer {

    private static final ArrayList<String> DUMMY_LIST = new ArrayList<String>();

    private final RendererPanel panel = new RendererPanel(RendererPanel.SelectionType.ALL);

    private Constraints models;
    private ArrayList<String> defaultTaxa = DUMMY_LIST;
    private TaxonTree tree;
    private boolean enabled;

    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean selected, final boolean cellHasFocus) {
        final Constraint constraint = (Constraint) value;
        final List<String> taxa = constraint == null ? DUMMY_LIST : constraint.getTaxa();
        panel.setEnabled(enabled);
        panel.setSelected(selected);
        panel.setText(constraint.getName());
        final String iconName;
        if (taxa != null && taxa.size() == 1) {
            final String taxonName = taxa.get(0);
            final SimpleTaxon taxon = tree.findTaxonByName(taxonName);
            iconName = TaxonRendererUtils.getIconForTaxon(taxon, selected && enabled);
        }
        else if (taxa != null && taxa.size() > 1) {
            iconName = "group.gif";
        }
        else {
            iconName = null;
        }
        panel.setIcon(ImageLocator.getIcon(iconName));
        ConstraintsRendererUtils.configureForConstraint(panel, models, constraint, defaultTaxa, true);
        panel.update();
        return panel;
    }

    public void setConstraints(final Constraints models) {
        this.models = models;
        final String[] taxa = models == null ? null : models.getDefaultTaxa();
        defaultTaxa = models == null || taxa == null ? DUMMY_LIST : new ArrayList<String>(Arrays.asList(taxa));
        tree = models == null ? null : TaxonModels.find(models.getTaxaUid());
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
