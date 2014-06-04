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
package ch.xmatrix.ups.uec.groups;

import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.jfactory.component.RendererPanel;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import com.jgoodies.binding.list.SelectionInList;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:07:45 $
 */
public class TaxaListRenderer implements ListCellRenderer
{
    private final SelectionInList models;

    private final RendererPanel panel = new RendererPanel( RendererPanel.SelectionType.ALL );

    private boolean enabled;

    public TaxaListRenderer( final SelectionInList models )
    {
        this.models = models;
    }

    public Component getListCellRendererComponent( final JList list, final Object value, final int index,
                                                   final boolean selected, final boolean hasFocus )
    {
        final String name = (String) value;
        final GroupsModel model = (GroupsModel) models.getSelection();
        final TaxonTree tree = TaxonModels.find( model == null ? null : model.getTaxaUid() );
        final SimpleTaxon taxon = tree.findTaxonByName( name );
        final ImageIcon icon = ImageLocator.getIcon( TaxonRendererUtils.getIconForTaxon( taxon, selected && enabled ) );
        panel.setIcon( icon );
        panel.setText( name );
        panel.setSelected( selected );
        panel.setEnabled( enabled );
        panel.update();
        return panel;
    }

    public void setEnabled( final boolean enabled )
    {
        this.enabled = enabled;
    }
}
