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
import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.command.AddFilterToAndedFilterable;
import ch.jfactory.command.CollapseAllTreeNodes;
import ch.jfactory.command.ExpandAllTreeNodes;
import ch.jfactory.command.RemoveFilterFromAndedFilterable;
import ch.jfactory.component.RendererPanel;
import ch.jfactory.component.tree.AbstractTreeModel;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.component.tree.filtered.ModelBasedFilteredTreeModel;
import ch.jfactory.filter.Filter;
import ch.jfactory.filter.MultiAndFilter;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.ust.main.UserModel;
import ch.xmatrix.ups.view.renderer.ConstraintsRendererUtils;
import ch.xmatrix.ups.view.renderer.TaxonRendererUtils;
import com.jidesoft.swing.SearchableUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.pietschy.command.CommandManager;

/**
 * Builds a panel like a list but with a simple flat tree consisting of wrapped {@link Constraint}
 * objects.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2007/05/16 17:00:15 $
 */
public class ConstraintsSelectionBuilder extends ActionCommandPanelBuilder
{
    private JTree tree;

    private Constraints constraints;

    private ModelBasedFilteredTreeModel treeModel;

    private UserModel userModel;

    private ConstraintsTreeModel constraintsTreeModel;

    public ConstraintsSelectionBuilder()
    {
        tree = createConstraintsTree();
    }

    public JComponent getRepaintComponent()
    {
        return tree;
    }

    public void setModel( final UserModel userModel )
    {
        this.userModel = userModel;
        this.constraints = (Constraints) AbstractMainModel.findModel( userModel.getConstraintsUid() );
        final ArrayList<Constraint> allConstraints = constraints.getConstraints();
        Collections.sort( allConstraints, new ToStringComparator() );
        constraintsTreeModel.setConstraints( allConstraints );
        treeModel.reload();
    }

    public void addTreeSelectionListener( final TreeSelectionListener listener )
    {
        tree.addTreeSelectionListener( listener );
    }

    public void initCommands()
    {
        final CommandManager manager = getCommandManager();
        initCommand( new ExpandAllTreeNodes( manager, tree ), true );
        initCommand( new CollapseAllTreeNodes( manager, tree ), true );
        final TreeExpandedRestorer saver = new TreeExpandedRestorer( tree );
        final Filter hide = new HideCompletedConstraintsViewFilter();
        initToggleCommand( new AddFilterToAndedFilterable( manager, Commands.COMMANDID_CONSTRAINTS_OPEN, treeModel, hide, saver ), false );
        initToggleCommand( new RemoveFilterFromAndedFilterable( manager, Commands.COMMANDID_CONSTRAINTS_ALL, treeModel, hide, saver ), true );
    }

    public JComponent createMainPanel()
    {
        final JComponent panel = super.createMainPanel();
        panel.setLayout( new BorderLayout() );
        panel.add( getCommandManager().getGroup( Commands.GROUPID_CONSTRAINTS_TOOLBAR ).createToolBar(), BorderLayout.NORTH );
        panel.add( new JScrollPane( tree ), BorderLayout.CENTER );
        return panel;
    }

    private JTree createConstraintsTree()
    {
        constraintsTreeModel = new ConstraintsTreeModel();
        treeModel = new ModelBasedFilteredTreeModel( constraintsTreeModel );
        treeModel.setFilter( new MultiAndFilter( new Filter[0] ) );
        tree = new JTree( treeModel );
        tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        tree.setBorder( new EmptyBorder( 3, 3, 3, 3 ) );
        tree.setRootVisible( false );
        tree.setCellRenderer( new ConstraintsRenderer() );
        SearchableUtils.installSearchable( tree );
        return tree;
    }

    private class HideCompletedConstraintsViewFilter implements Filter
    {
        public boolean matches( final Object node )
        {
            if ( node instanceof Constraint )
            {
                final Constraint constraint = (Constraint) node;
                return isUncomplete( constraint );
            }
            else if ( node instanceof SimpleTaxon )
            {
                final SimpleTaxon taxon = (SimpleTaxon) node;
                final Constraint constraint = constraints.findConstraint( taxon.getName() );
                return isUncomplete( constraint );
            }
            else
            {
                throw new IllegalArgumentException( node.getClass().getName() + " not handable" );
            }
        }

        private boolean isUncomplete( final Constraint constraint )
        {
            final ArrayList<String> userTaxa = userModel.getTaxa();
            final int totalCount = ConstraintsRendererUtils.getTotalCount( constraint, constraints, userTaxa );
            final int minimalCount = constraint.getMinimalCount();
            return minimalCount > totalCount;
        }
    }

    private class ConstraintsTreeModel extends AbstractTreeModel
    {
        private static final String ROOT = "ConstraintsRoot";

        private ArrayList<Constraint> allConstraints = null;

        public ConstraintsTreeModel()
        {
            super( ROOT );
        }

        public void setConstraints( final ArrayList<Constraint> constraints )
        {
            allConstraints = constraints;
        }

        public int getChildCount( final Object parent )
        {
            int result = -1;
            if ( allConstraints == null )
            {
                result = 0;
            }
            else if ( parent == ROOT )
            {
                result = allConstraints.size();
            }
            else if ( parent instanceof Constraint )
            {
                final Constraint constraint = (Constraint) parent;
                final int size = constraint.getTaxa().size();
                result = size <= 1 ? 0 : size;
            }
            else
            {
                result = 0;
            }
            return result;
        }

        public Object getChild( final Object parent, final int index )
        {
            Object result = null;
            if ( parent == ROOT )
            {
                result = allConstraints.get( index );
            }
            else if ( parent instanceof Constraint )
            {
                final Constraint constraint = (Constraint) parent;
                final List<String> children = constraint.getTaxa();
                final String taxonName = children.get( index );
                final TaxonTree tree = TaxonModels.find( constraints.getTaxaUid() );
                result = tree.findTaxonByName( taxonName );
            }
            else
            {
                throw new IllegalArgumentException( "unknown node type " + parent.getClass().getName() );
            }
            return result;
        }

        protected void remove( final Object child, final TreePath parentPath )
        {
            throw new IllegalArgumentException( "remove not supported" );
        }

        protected void insert( final TreePath child, final TreePath parent, final int pos )
        {
            throw new IllegalArgumentException( "insert not supported" );
        }

        public void valueForPathChanged( final TreePath path, final Object newValue )
        {
            throw new IllegalArgumentException( "value for path change not supported" );
        }
    }

    private class ConstraintsRenderer implements TreeCellRenderer
    {
        private final RendererPanel panel = new RendererPanel( RendererPanel.SelectionType.TEXTONLY );

        public ConstraintsRenderer()
        {
            panel.setEnabled( true );
        }

        public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean selected,
                                                       final boolean expanded, final boolean leaf, final int row,
                                                       final boolean hasFocus )
        {
            final String iconName;
            if ( value instanceof Constraint )
            {
                final Constraint constraint = (Constraint) value;
                final ArrayList<String> userTaxa = userModel.getTaxa();
                final List<String> taxa = constraint.getTaxa();
                if ( taxa != null && taxa.size() == 1 )
                {
                    final TaxonTree treeModel = TaxonModels.find( constraints.getTaxaUid() );
                    final String taxonName = taxa.get( 0 );
                    final SimpleTaxon taxon = treeModel.findTaxonByName( taxonName );
                    iconName = TaxonRendererUtils.getIconForTaxon( taxon, false );
                    ConstraintsRendererUtils.configureForConstraint( panel, constraints, constraint, userTaxa, true );
                }
                else
                {
                    iconName = "group.gif";
                    ConstraintsRendererUtils.configureForConstraint( panel, constraints, constraint, userTaxa, false );
                }
            }
            else if ( value instanceof String )
            {
                iconName = "iconRoot.png";
            }
            else
            {
                final SimpleTaxon taxon = (SimpleTaxon) value;
                iconName = TaxonRendererUtils.getIconForTaxon( taxon, false );
            }
            final ImageIcon icon = ImageLocator.getIcon( iconName );
            panel.setIcon( icon );
            panel.setText( value.toString() );
            panel.setSelected( selected );
            panel.update();
            return panel;
        }
    }
}
