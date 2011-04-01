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
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.domain.TreeObject;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Adds icons according to the taxon displayed to the panel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2006/04/25 11:08:11 $
 */
public class TaxonRendererUtils
{
    /** The cell constraints for the form layout manager. */
    public static final CellConstraints CC = new CellConstraints();

    /** The component to use for a node. */
    public static final RendererPanel PANEL = new RendererPanel();

    /** Disable construction of utility class. */
    private TaxonRendererUtils()
    {
        super();
    }

    /**
     * Returns the icons for a object.
     *
     * @param object   the object to return the icon for
     * @param selected whether the object is selected
     * @return the icon
     */
    public static ImageIcon getIcon( final Object object, final boolean selected )
    {
        final String icon;
        if ( object instanceof Constraint )
        {
            final Constraint constraint = (Constraint) object;
            if ( constraint.getTaxa() != null && constraint.getTaxa().size() > 1 )
            {
                icon = "group.gif";
            }
            else if ( constraint.getTaxa() != null && constraint.getTaxa().size() == 1 )
            {
                final String taxon = constraint.getTaxa().get( 0 );
                final Constraints constraints = constraint.getConstraints();
                final TaxonTree tree = TaxonModels.find( constraints.getTaxaUid() );
                icon = getIconForTaxon( tree.findTaxonByName( taxon ), selected );
            }
            else
            {
                icon = null;
            }
        }
        else if ( object instanceof String )
        {
            icon = "iconRoot.png";
        }
        else if ( object instanceof SimpleTaxon )
        {
            final SimpleTaxon taxon = (SimpleTaxon) object;
            icon = getIconForTaxon( taxon, selected );
        }
        else if ( object != null )
        {
            final TreeObject treeObject = (TreeObject) object;
            icon = getIconForTaxon( treeObject.getTaxon(), selected );
        }
        else
        {
            icon = null;
        }
        return ImageLocator.getIcon( icon );
    }

    /**
     * Returns an icon for the given taxon object.
     *
     * @param taxon    the taxon to return the icon for
     * @param selected whether the object should be displayed selected
     * @return the icon name
     */
    public static String getIconForTaxon( final SimpleTaxon taxon, final boolean selected )
    {
        String icon;
        final SimpleLevel level = taxon.getLevel();
        if ( level == null )
        {
            icon = "iconRoot";
        }
        else
        {
            icon = "icon" + ( ( level == null ) ? "" : level.getName() );
        }
        if ( selected )
        {
            icon += "Selected";
        }
        icon += ".png";
        return icon;
    }

    /**
     * Returns the text for the given object. Handles String, SimpleTaxon, TreeObject and Constraint objects.
     *
     * @param object the object to display
     * @return the text
     */
    public static String getText( final Object object )
    {
        final String text;
        if ( object instanceof Constraint )
        {
            text = ( (Constraint) object ).getName();
        }
        else if ( object instanceof String )
        {
            text = (String) object;
        }
        else if ( object instanceof SimpleTaxon )
        {
            final SimpleTaxon taxon = (SimpleTaxon) object;
            text = taxon.getName();
        }
        else if ( object != null )
        {
            final TreeObject taxon = (TreeObject) object;
            text = taxon.getTaxon().getName();
        }
        else
        {
            text = null;
        }
        return text;
    }

    /**
     * Sets the icon, text, selected and enabled state of the renderer panel. The icon is set to selected only if the states for selected and enabled are both set to true.
     *
     * @param object   the user object to display
     * @param selected whether to display it selected
     * @param enabled  whether to display it enabled
     * @return the renderer panel
     */
    public static RendererPanel getRendererPanel( final Object object, final boolean selected,
                                                  final boolean enabled )
    {
        PANEL.setIcon( getIcon( object, selected && enabled ) );
        PANEL.setText( getText( object ) );
        PANEL.setSelected( selected );
        PANEL.setEnabled( enabled );
        return PANEL;
    }

    /** Tree cell renderer implementation delegating to enclosing class. */
    public static class DefaultConstraintsTreeCellRenderer extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel,
                                                       final boolean expanded, final boolean leaf, final int row,
                                                       final boolean hasFocus )
        {
            return getRendererPanel( value, sel, tree.isEnabled() );
        }
    }

    /** List cell renderer implementation delegating to enclosing class. */
    public static class DefaultConstraintsListCellRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent( final JList list, final Object value, final int index,
                                                       final boolean isSelected, final boolean cellHasFocus )
        {
            return getRendererPanel( value, isSelected, list.isEnabled() );
        }
    }
}
