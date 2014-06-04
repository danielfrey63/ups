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
package ch.xmatrix.ups.uec.groups;

import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.jfactory.component.RendererPanel;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * Groups tree node renderer. The usual taxons node is postfixed by the group in parantesis if the taxon is assigned to
 * one.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:07:45 $
 */
public class GroupsTreeRenderer implements TreeCellRenderer
{
    private final JCheckBox checkbox = new JCheckBox();

    private final RendererPanel panel = new RendererPanel();

    private GroupsModel models;

    private boolean enabled = true;

    public GroupsTreeRenderer()
    {
        checkbox.setOpaque( false );
    }

    public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean selected,
                                                   final boolean expanded, final boolean leaf, final int row,
                                                   final boolean hasFocus )
    {
        final SimpleTaxon taxon = (SimpleTaxon) value;
        final GroupModel model1 = models == null ? null : models.find( taxon.getName() );
        final GroupModel current = models == null ? null : models.getCurrentGroup();
        final GroupModel model = models == null ? null : models.find( taxon.getName() );
        checkbox.setSelected( model1 != null );
        panel.setSelected( selected );
        panel.setEnabled( enabled );
        panel.setText( taxon.getName() );
        panel.setIcon( ImageLocator.getIcon( TaxonRendererUtils.getIconForTaxon( taxon, false ) ) );
        panel.setPrefixComponent( checkbox );
        panel.setShowPrefixComponent( taxon instanceof SimpleTaxon && SimpleTaxon.isSpecies( taxon ) );
        panel.setPrefixEnabled( isEnabled() && tree.isEnabled() && ( model1 == current || model1 == null ) );
        panel.setExtensionText( model == null ? null : " (" + model.getName() + ")" );
        panel.setOk( true );
        panel.update();
        return panel;
    }

    public void setGroupsModel( final GroupsModel model )
    {
        this.models = model;
    }

    /**
     * If called with <code>false</code>, simulates the disabled state by calling the renderer behind with selection and
     * enabled attributes set to <code>false</code>. Selecting nodes will not make a visual difference.
     *
     * @param enabled whether to pseudo-disable the tree or not.
     */
    public void setEnabled( final boolean enabled )
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
