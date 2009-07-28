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
package ch.xmatrix.ups.uec.specimens;

import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.view.renderer.RendererPanel;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import ch.xmatrix.ups.model.SpecimenModel;
import ch.xmatrix.ups.model.SpecimensModel;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * Displays the specimen model details as an extention to the text.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/23 22:19:08 $
 */
public class SpecimensTreeRenderer implements TreeCellRenderer {

    private final RendererPanel panel = new RendererPanel();

    private SpecimensModel models;
    private boolean enabled;

    public SpecimensTreeRenderer() {
        setEnabled(true);
    }

    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected,
                                                  final boolean isExpanded, final boolean isLeaf, final int row,
                                                  final boolean hasFocus) {

        final SimpleTaxon taxon = (SimpleTaxon) value;
        final SpecimenModel model = models == null ? null : models.find(taxon.getName());
        panel.setEnabled(enabled);
        panel.setSelected(selected);
        panel.setExtentionText(model == null ? null : model.toString());
        panel.setOk(model != null && model.getId() != null && !model.isDeactivatedIfKnown());
        panel.setText(taxon.getName());
        panel.setEnabled(models != null && !models.isFixed());
        panel.setIcon(ImageLocator.getIcon(TaxonRendererUtils.getIconForTaxon(taxon, false)));
        panel.update();
        return panel;
    }

    public void setSpecimensModel(final SpecimensModel models) {
        this.models = models;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
