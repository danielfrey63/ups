package com.ethz.geobot.herbar.modeapi.wizard.filter;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.tree.AbstractMutableTreeNode;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterDefinitionDetail;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterDefinitionPane extends WizardPane
{
    private static final Logger LOG = LoggerFactory.getLogger( WizardFilterDefinitionPane.class );

    /** name of the pane */
    public static final String NAME = "filter.define";

    private final String filterPropertyName;

    private DefaultTreeModel model;

    private JTree tree;

    private JScrollPane scrollPane;

    public WizardFilterDefinitionPane( final String filterPropertyName )
    {
        super( NAME, new String[]{filterPropertyName} );
        this.filterPropertyName = filterPropertyName;
    }

    public void activate()
    {
        final FilterModel filterModel = (FilterModel) getProperty( filterPropertyName );
        if ( model == null )
        {
            filterModel.addFilterDetail();
        }
        model = new DefaultTreeModel( new FilterTreeNode( filterModel ) );
        tree.setModel( model );
        new TreeExpander( tree, 3 );
        checkLevels( (FilterTreeNode) model.getRoot() );
    }

    public JPanel createDisplayPanel( final String prefix )
    {
        this.addComponentListener( new ComponentAdapter()
        {
            public void componentResized( final ComponentEvent e )
            {
                final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
                ter.save();
                model.reload();
                ter.restore();
            }
        } );

        final JPanel text = createTextPanel( prefix );
        final JPanel title = createDefaultTitlePanel( prefix );
        final JPanel taxonText = createTextPanel( prefix + ".TAXON" );
        final JPanel levelText = createTextPanel( prefix + ".LEVEL" );

        tree = createTree();

        final JButton addTaxon = createAddTaxonButton( prefix );
        final JButton deleteTaxon = createDeleteTaxaButton( prefix );
        final JButton editTaxon = createEditTaxonButton( prefix );
        final JButton addLevel = createAddLevelsButton( prefix );
        final JButton deleteLevel = createDeleteLevelsButton( prefix );

        final int gap = Constants.GAP_WITHIN_TOGGLES;

        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = 7;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add( text, gbc );
        gbc.gridy += 1;
        panel.add( title, gbc );

        gbc.gridwidth = 4;
        gbc.gridy += 1;
        panel.add( taxonText, gbc );
        gbc.gridwidth = 3;
        gbc.gridx += 4;
        panel.add( levelText, gbc );

        gbc.gridy += 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets( gap, 0, gap, gap );

        gbc.weightx = 0.0;
        gbc.gridx = 0;
        panel.add( addTaxon, gbc );
        gbc.gridx += 1;
        panel.add( deleteTaxon, gbc );
        gbc.gridx += 1;
        panel.add( editTaxon, gbc );
        gbc.weightx = 0.5;
        gbc.gridx += 1;
        panel.add( new JPanel(), gbc );

        gbc.weightx = 0.0;
        gbc.gridx += 1;
        panel.add( addLevel, gbc );
        gbc.gridx += 1;
        panel.add( deleteLevel, gbc );
        gbc.weightx = 0.5;
        gbc.gridx += 1;
        panel.add( new JPanel(), gbc );

        gbc.insets = new Insets( 0, 0, 0, 0 );
        gbc.weightx = 0.0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy += 1;
        panel.add( scrollPane, gbc );

        return panel;
    }

    private JTree createTree()
    {
        final JTree tree = new JTree();
        scrollPane = new JScrollPane( tree );
        scrollPane.setPreferredSize( new Dimension( 400, 10 ) );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        tree.setRootVisible( false );
        tree.setShowsRootHandles( true );
        tree.setCellRenderer( new FilterTreeCellRenderer( this ) );
        tree.setAutoscrolls( true );

        return tree;
    }

    private JButton createDeleteLevelsButton( final String prefix )
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final TreePath[] paths = tree.getSelectionPaths();
                if ( paths == null )
                {
                    return;
                }
                final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
                ter.save();
                for ( final TreePath path : paths )
                {
                    final FilterTreeNode node = (FilterTreeNode) path.getLastPathComponent();
                    node.removeFromParent();
                    ter.remove( path );
                }
                model.reload();
                ter.restore();
            }
        };
        final JButton button = createListEditButton( prefix + ".LEVEL.DELETE", action );
        button.setEnabled( false );
        // make sure not all level would be deleted for a scope, and that all selected nodes are levels.
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                final JTree tree = (JTree) e.getSource();
                final TreePath[] paths = tree.getSelectionPaths();
                if ( paths == null )
                {
                    button.setEnabled( false );
                }
                else
                {
                    boolean allSelectionsAreLevels = true;
                    for ( final TreePath path1 : paths )
                    {
                        allSelectionsAreLevels &= isLeaf( path1 );
                    }
                    boolean wouldRemoveAllLevels = false;
                    final TreeNode root = (TreeNode) tree.getModel().getRoot();
                    for ( int i = 0; i < root.getChildCount(); i++ )
                    {
                        final TreeNode taxon = root.getChildAt( i );
                        int children = taxon.getChildCount();
                        for ( final TreePath path : paths )
                        {
                            final TreeNode node = (TreeNode) path.getParentPath().getLastPathComponent();
                            if ( node == taxon )
                            {
                                children--;
                            }
                        }
                        wouldRemoveAllLevels |= ( children == 0 );
                    }
                    button.setEnabled( allSelectionsAreLevels && !wouldRemoveAllLevels );
                }
            }
        } );
        return button;
    }

    private boolean isLeaf( final TreePath path )
    {
        final TreeNode node = (TreeNode) path.getLastPathComponent();
        return node.isLeaf();
    }

    private JButton createAddLevelsButton( final String prefix )
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final Level[] levels = getValidNewLevels( tree.getSelectionPath() );
                Arrays.sort( levels, new ToStringComparator<Level>() );
                final JDialog parent = (JDialog) WizardFilterDefinitionPane.this.getTopLevelAncestor();
                final ListDialog dialog = new ListDialog( parent, "DIALOG.LEVELS", levels );
                dialog.setSize( 300, 300 );
                dialog.setLocationRelativeTo( WizardFilterDefinitionPane.this );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    final Level[] newLevels = (Level[]) dialog.getSelectedData( new Level[0] );
                    insertNewLevels( newLevels );
                }
            }
        };
        final JButton button = createListEditButton( prefix + ".LEVEL.ADD", action );
        button.setEnabled( false );
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            // button is enabled if there is exactly one non-leaf node selected - a scope - and the assigned levels
            // are not already complete, or any levels of the same scope (and the scope itself) are selected.
            public void valueChanged( final TreeSelectionEvent e )
            {
                final JTree tree = (JTree) e.getSource();
                final TreePath[] paths = tree.getSelectionPaths();
                if ( paths == null || paths.length == 0 )
                {
                    button.setEnabled( false );
                    return;
                }
                // make sure that all selected nodes are from the same parent. the parent may be selected as well.
                Object theParent = null;
                boolean ok = true;
                TreePath parentPath = null;
                for ( final TreePath path : paths )
                {
                    final TreeNode treeNode = (TreeNode) ( path.getLastPathComponent() );
                    if ( treeNode.isLeaf() )
                    {
                        final Object parent = path.getParentPath().getLastPathComponent();
                        if ( theParent == null )
                        {
                            theParent = parent;
                            parentPath = path.getParentPath();
                        }
                        ok &= theParent == parent;
                    }
                    else
                    {
                        if ( theParent == null )
                        {
                            theParent = treeNode;
                            parentPath = path;
                        }
                        ok &= theParent == treeNode;
                    }
                }
                // look for new valid levels
                if ( ok )
                {
                    final Level[] validNewLevels = getValidNewLevels( parentPath );
                    ok = ( validNewLevels.length > 0 );
                }
                button.setEnabled( ok );
            }
        } );
        return button;
    }

    private void insertNewLevels( final Level[] newLevels )
    {
        final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
        ter.save();
        for ( final Level newLevel : newLevels )
        {
            final FilterTreeNode newNode = new FilterTreeNode( newLevel );
            final FilterTreeNode filterTreeNode = getDetailsNode( tree.getSelectionPath() );
            final FilterDefinitionDetail detail = (FilterDefinitionDetail) filterTreeNode.getUserObject();
            filterTreeNode.insert( newNode, getIndex( detail, newLevel ) );
            ter.addSelection( new TreePath( model.getPathToRoot( newNode ) ) );
        }
        model.reload();
        ter.restore();
    }

    private Level[] getValidNewLevels( final TreePath selectionPath )
    {
        final FilterTreeNode parentNode = getFilterModelNode( selectionPath );
        final FilterModel filterModel = (FilterModel) parentNode.getUserObject();
        final FilterTreeNode filterTreeNode = getDetailsNode( selectionPath );
        final FilterDefinitionDetail detail = (FilterDefinitionDetail) filterTreeNode.getUserObject();
        return getValidNewLevels( filterModel, detail );
    }

    private Level[] getValidNewLevels( final FilterModel filterModel, final FilterDefinitionDetail detail )
    {
        // make sure to display only valid levels. valid levels are all levels of the base list which occur
        // also in the scopes sub levels, but are not already chosen.
        final Taxon scope = detail.getScope();
        final Level[] empty = new Level[0];
        Level[] levels = scope.getSubLevels();
        final boolean top1 = scope.equals( filterModel.getRootTaxon() );
        final boolean top2 = filterModel.getRootTaxon().equals( scope );
        if ( top1 || top2 )
        {
            levels = ArrayUtils.unite( levels, filterModel.getDependantModel().getLevels(), empty, new LevelComparator() );
            levels = ArrayUtils.removeAll( levels, detail.getLevels(), empty );
        }
        else
        {
            levels = ArrayUtils.removeAll( levels, detail.getLevels(), empty );
            levels = ArrayUtils.intersect( levels, filterModel.getDependantModel().getLevels(), empty );
        }
        Arrays.sort( levels, new LevelComparator() );

        return levels;
    }

    private FilterTreeNode getFilterModelNode( final TreePath selectionPath )
    {
        final FilterTreeNode filterTreeNode = getDetailsNode( selectionPath );
        return (FilterTreeNode) filterTreeNode.getParent();
    }

    private FilterTreeNode getDetailsNode( final TreePath selectionPath )
    {
        FilterTreeNode filterTreeNode = (FilterTreeNode) selectionPath.getLastPathComponent();
        if ( filterTreeNode.isLeaf() )
        {
            filterTreeNode = (FilterTreeNode) filterTreeNode.getParent();
        }
        return filterTreeNode;
    }

    private JButton createDeleteTaxaButton( final String prefix )
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final TreePath[] paths = tree.getSelectionPaths();
                final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
                ter.save();
                for ( final TreePath path : paths )
                {
                    final FilterTreeNode node = (FilterTreeNode) path.getLastPathComponent();
                    final FilterTreeNode root = (FilterTreeNode) model.getRoot();
                    root.remove( node );
                    ter.remove( path );

                }
                model.reload();
                ter.restore();
            }
        };
        final JButton button = createListEditButton( prefix + ".TAXON.DELETE", action );
        button.setEnabled( false );
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            // button gets activated when only scopes are selected
            public void valueChanged( final TreeSelectionEvent e )
            {
                final JTree tree = (JTree) e.getSource();
                final TreePath[] paths = tree.getSelectionPaths();
                if ( paths == null || paths.length == 0 )
                {
                    button.setEnabled( false );
                }
                else
                {
                    boolean allTaxa = true;
                    for ( final TreePath path : paths )
                    {
                        final TreeNode node = (TreeNode) path.getLastPathComponent();
                        allTaxa &= !node.isLeaf();
                    }
                    final TreeNode root = (TreeNode) tree.getModel().getRoot();
                    button.setEnabled( allTaxa && root.getChildCount() != paths.length );
                }
            }
        } );
        return button;
    }

    private boolean checkLevels( final FilterTreeNode root )
    {
        final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
        ter.save();
        final boolean ok;
        if ( !( ok = cleanupLevels( root ) ) )
        {
            JOptionPane.showMessageDialog( WizardFilterDefinitionPane.this,
                    Strings.getString( prefix + ".MESSAGE.CLEAN.TEXT" ),
                    Strings.getString( prefix + ".MESSAGE.CLEAN.TITLE" ),
                    JOptionPane.INFORMATION_MESSAGE );
        }
        model.reload();
        ter.restore();
        return ok;
    }

    private JButton createEditTaxonButton( final String prefix )
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final FilterModel filterModel = (FilterModel) getProperty( filterPropertyName );
                final Taxon rootTaxon = filterModel.getDependantModel().getRootTaxon();
                final TaxTreeDialog dialog = new TaxTreeDialog( (JDialog) getTopLevelAncestor(), rootTaxon );
                dialog.setSize( 400, 500 );
                dialog.setLocationRelativeTo( WizardFilterDefinitionPane.this );
                final FilterTreeNode node = (FilterTreeNode) tree.getSelectionPath().getLastPathComponent();
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) node.getUserObject();
                Taxon scope = detail.getScope();
                dialog.setSelectedTaxon( scope );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
                    ter.save();
                    scope = dialog.getSelectedTaxon();
                    detail.setScope( scope );
                    checkLevels( node );
                    model.reload();
                    ter.restore();
                }
            }
        };
        final JButton button = createListEditButton( prefix + ".TAXON.EDIT", action );
        button.setEnabled( false );
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                final JTree tree = (JTree) e.getSource();
                final TreePath[] paths = tree.getSelectionPaths();
                if ( paths == null || paths.length != 1 )
                {
                    button.setEnabled( false );
                }
                else
                {
                    final TreePath path = paths[0];
                    final TreeNode node = (TreeNode) path.getLastPathComponent();
                    button.setEnabled( !node.isLeaf() );
                }
            }
        } );
        return button;
    }

    private JButton createAddTaxonButton( final String prefix )
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final FilterModel filterModel = (FilterModel) getProperty( filterPropertyName );
                final Taxon rootTaxon = filterModel.getDependantModel().getRootTaxon();
                final TaxTreeDialog dialog = new TaxTreeDialog( (JDialog) getTopLevelAncestor(), rootTaxon );
                dialog.setSize( 400, 500 );
                dialog.setLocationRelativeTo( WizardFilterDefinitionPane.this );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    final Taxon scope = dialog.getSelectedTaxon();
                    final Level[] levels = scope.getSubLevels();
                    final FilterDefinitionDetail detail = filterModel.createFilterDetail( scope, levels );
                    final FilterTreeNode root = (FilterTreeNode) model.getRoot();
                    root.insert( new FilterTreeNode( detail ), root.getChildCount() );

                    final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
                    ter.save();
                    model.reload();
                    ter.restore();
                }
            }
        };
        return createListEditButton( prefix + ".TAXON.ADD", action );
    }

    private int getIndex( final FilterDefinitionDetail detail, final Level newLevel )
    {
        int index = 0;
        if ( detail.getLevels().length == 0 )
        {
            return 0;
        }
        Level tempLevel;
        Level[] assigned;
        do
        {
            assigned = detail.getLevels();
            tempLevel = assigned[index++];
        }
        while ( tempLevel != null && tempLevel.isHigher( newLevel ) && index < assigned.length );
        return index - ( tempLevel != null && tempLevel.isHigher( newLevel ) ? 0 : 1 );
    }

    /**
     * Checks for levels not consistent with the scope and removes them. Makes sure that at least one level is in the
     * scope.
     *
     * @param node the FilterTreeNode containing the FilterDefinitionDetail to check
     * @return <code>true</code> if no changes were made
     */
    private boolean cleanupLevels( final FilterTreeNode node )
    {
        final Object userObject = node.getUserObject();
        boolean ok = true;
        if ( userObject instanceof FilterDefinitionDetail )
        {
            // collect levels to remove
            final FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
            final List<FilterTreeNode> toRemove = new ArrayList<FilterTreeNode>();
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                final FilterTreeNode child = (FilterTreeNode) node.getChildAt( i );
                final Level level = (Level) child.getUserObject();
                if ( !isConsistent( detail, level ) )
                {
                    toRemove.add( child );
                    ok = false;
                }
            }
            // remove them
            for ( final Object aToRemove : toRemove )
            {
                final FilterTreeNode child = (FilterTreeNode) aToRemove;
                node.remove( child );
            }
            // make sure at least one detail is there
            if ( node.getChildCount() == 0 )
            {
                final Level level = detail.getScope().getLevel();
                node.insert( new FilterTreeNode( level ), getIndex( detail, level ) );
                ok = false;
            }
            return ok;
        }
        else
        { // is a filter model
            for ( int i = 0; i < node.getChildCount(); i++ )
            {
                ok &= cleanupLevels( (FilterTreeNode) node.getChildAt( i ) );
            }
        }
        return ok;
    }

    /**
     * Checks whether the level given is being contained in the sub levels of the scope.
     *
     * @param detail       the FilterDefinitionDetail of the level to evaluate
     * @param levelToCheck the level to validate
     * @return <code>true</code> if the level is being contained in the scopes levels
     */
    protected boolean isConsistent( final FilterDefinitionDetail detail, final Level levelToCheck )
    {
        final Taxon scope = detail.getScope();
        final Level[] scopesLevel = scope.getSubLevels();
        return ArrayUtils.contains( scopesLevel, levelToCheck ) || levelToCheck == scope.getLevel();
    }

    /**
     * Checks whether the given scope is consistent with the other scopes in the model. Especially it is checked whether
     * the given scope is: <ul> <li>not the same any other one in the model</li> <li>is not a child of any scope in the
     * model</li> <li>is not a parent of any scope in the model</li> </ul>
     *
     * @param detailToCheck the FilterDefinitionDetail containing the scope under investigation
     * @return <code>true</code> if all conditions evaluate to <code>true</code>
     */
    protected boolean isConsistent( final FilterDefinitionDetail detailToCheck )
    {
        final FilterModel filterModel = (FilterModel) getProperty( filterPropertyName );
        FilterDefinitionDetail[] details = filterModel.getFilterDetails();
        // make sure two same scopes are properly recognized
        details = ArrayUtils.remove( details, detailToCheck, new FilterDefinitionDetail[0] );
        final Taxon checkScope = detailToCheck.getScope();
        final Level checkLevel = checkScope.getLevel();
        boolean consistent = true;
        for ( final FilterDefinitionDetail detail : details )
        {
            final Taxon scope = detail.getScope();
            final Level level = scope.getLevel();
            consistent &=
                    ( level == checkLevel && scope != checkScope ) ||
                            ( level != null && level.isHigher( checkLevel ) && !containsTaxon( checkScope, scope ) ) ||
                            ( checkLevel != null && checkLevel.isHigher( level ) && !containsTaxon( scope, checkScope ) ) ||
                            ( containsTaxon( checkScope, scope ) && !ArrayUtils.haveCommon( detailToCheck.getLevels(), detail.getLevels() ) ) ||
                            ( containsTaxon( scope, checkScope ) && !ArrayUtils.haveCommon( detailToCheck.getLevels(), detail.getLevels() ) ) ||
                            ( scope == checkScope && !ArrayUtils.haveCommon( detailToCheck.getLevels(), detail.getLevels() ) );
        }
        return consistent;
    }

    public void registerPropertyChangeListener( final WizardModel wizardModel )
    {
        wizardModel.addPropertyChangeListener( filterPropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                final FilterModel fModel = (FilterModel) event.getNewValue();
                if ( fModel != null )
                {
                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "model change to: " + fModel.getName() );
                    }
                    model = new DefaultTreeModel( new FilterTreeNode( fModel ) );
                    tree.setModel( model );
                    model.reload();
                }
                else
                {
                    LOG.warn( "change filter model to null." );
                }
            }
        } );
    }

    private boolean containsTaxon( final Taxon toBeSearchedFor, final Taxon possibleParent )
    {
        return ArrayUtils.contains( possibleParent.getAllChildTaxa( toBeSearchedFor.getLevel() ), toBeSearchedFor );
    }

    public static class FilterTreeNode extends AbstractMutableTreeNode
    {
        private FilterTreeNode[] children;

        private FilterTreeNode parent;

        private Object userObject;

        public FilterTreeNode( final Object userObject )
        {
            setUserObject( userObject );
        }

        public void insert( final MutableTreeNode child, final int index )
        {
            final FilterTreeNode childNode = (FilterTreeNode) child;
            // update model
            if ( userObject instanceof FilterModel )
            {
                if ( !( childNode.getUserObject() instanceof FilterDefinitionDetail ) )
                {
                    throw new IllegalArgumentException( "Child of a FilterModel node must be a FilterDefinitionDetail" );
                }
                final FilterModel filterModel = (FilterModel) userObject;
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) childNode.getUserObject();
                filterModel.addFilterDetail( detail );
            }
            else if ( userObject instanceof FilterDefinitionDetail )
            {
                if ( !( childNode.getUserObject() instanceof Level ) )
                {
                    throw new IllegalArgumentException( "Child of a FilterDefinitionDetail node must be a Level" );
                }
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                final Level level = (Level) childNode.getUserObject();
                final List<Level> levels = new ArrayList<Level>( Arrays.asList( detail.getLevels() ) );
                levels.add( index, level );
                detail.setLevels( levels.toArray( new Level[levels.size()] ) );
            }
            // update cache
            final List<FilterTreeNode> childrenList = new ArrayList<FilterTreeNode>( Arrays.asList( children ) );
            childrenList.add( index, childNode );
            childNode.parent = this;
            childNode.children = new FilterTreeNode[childNode.getChildCount()];
            children = childrenList.toArray( new FilterTreeNode[childrenList.size()] );
        }

        public void remove( final MutableTreeNode child )
        {
            final FilterTreeNode childNode = (FilterTreeNode) child;
            // update model
            if ( userObject instanceof FilterModel )
            {
                final FilterModel filterModel = (FilterModel) userObject;
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) childNode.getUserObject();
                filterModel.removeFilterDetail( detail );
            }
            else if ( userObject instanceof FilterDefinitionDetail )
            {
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                Level[] levels = detail.getLevels();
                levels = (Level[]) ArrayUtils.remove( levels, childNode.getUserObject(), new Level[0] );
                detail.setLevels( levels );
            }
            // update cache
            children = (FilterTreeNode[]) ArrayUtils.remove( children, child, new FilterTreeNode[0] );
        }

        public void setParent( final MutableTreeNode newParent )
        {
            throw new NoSuchMethodError( "setParent not implemented" );
        }

        public void setUserObject( final Object userObject )
        {
            this.userObject = userObject;
        }

        public boolean getAllowsChildren()
        {
            return userObject instanceof FilterModel || userObject instanceof FilterDefinitionDetail;
        }

        public TreeNode getChildAt( final int childIndex )
        {
            if ( children == null )
            {
                children = new FilterTreeNode[getChildCount()];
            }
            if ( children.length < childIndex || children[childIndex] == null )
            {
                FilterTreeNode result = null;
                if ( userObject instanceof FilterModel )
                {
                    final FilterModel filterModel = (FilterModel) userObject;
                    final FilterDefinitionDetail filterDetail = filterModel.getFilterDetails()[childIndex];
                    result = new FilterTreeNode( filterDetail );
                }
                else if ( userObject instanceof FilterDefinitionDetail )
                {
                    final FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                    final Level level = detail.getLevels()[childIndex];
                    result = new FilterTreeNode( level );
                }
                if ( result != null )
                {
                    result.parent = this;
                }
                children[childIndex] = result;
                return result;
            }
            else
            {
                return children[childIndex];
            }
        }

        public int getChildCount()
        {
            if ( userObject instanceof FilterModel )
            {
                return ( (FilterModel) userObject ).getFilterDetails().length;
            }
            else if ( userObject instanceof FilterDefinitionDetail )
            {
                return ( (FilterDefinitionDetail) userObject ).getLevels().length;
            }
            else if ( userObject instanceof Level )
            {
                return 0;
            }
            return 0;
        }

        public TreeNode getParent()
        {
            return parent;
        }

        public Object getUserObject()
        {
            return userObject;
        }

        public String toString()
        {
            if ( userObject instanceof FilterModel )
            {
                return "FilterModel root";
            }
            else if ( userObject instanceof FilterDefinitionDetail )
            {
                final FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                return detail.getScope() + " " + Arrays.asList( detail.getLevels() );
            }
            else if ( userObject instanceof Level )
            {
                return userObject.toString();
            }
            return "";
        }
    }

    public static class FilterTreeCellRenderer extends DefaultTreeCellRenderer
    {
        private final WizardFilterDefinitionPane pane;

        public FilterTreeCellRenderer( final WizardFilterDefinitionPane pane )
        {
            this.pane = pane;
        }

        public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean selected, final boolean expanded,
                                                       final boolean leaf, final int row, final boolean hasFocus )
        {
            super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
            if ( value instanceof FilterTreeNode )
            {
                final FilterTreeNode filterTreeNode = (FilterTreeNode) value;
                final Object newValue = filterTreeNode.getUserObject();
                if ( newValue instanceof FilterModel )
                {
                    setText( ( (FilterModel) newValue ).getName() );
                }
                else if ( newValue instanceof FilterDefinitionDetail )
                {
                    final FilterDefinitionDetail detail = (FilterDefinitionDetail) newValue;
                    final Taxon scope = detail.getScope();
                    setText( scope.getName() );
                    final Level level = scope.getLevel();
                    if ( level != null )
                    {
                        setIcon( ImageLocator.getIcon( "icon" + level.getName() + ".gif" ) );
                    }
                    if ( !pane.isConsistent( detail ) )
                    {
                        setForeground( Color.red );
                    }
                }
                else if ( newValue instanceof Level )
                {
                    final Level level = (Level) newValue;
                    final String name = level.getName();
                    setText( name );
                    final ImageIcon icon = ImageLocator.getIcon( "icon" + name + ".gif" );
                    final int diff = pane.scrollPane.getViewport().getWidth() / 2 - 30;
                    setIcon( new ImageIcon( new PrefixedIcon( icon, diff ) ) );
                    final FilterTreeNode parent = (FilterTreeNode) filterTreeNode.getParent();
                    final FilterDefinitionDetail detail = (FilterDefinitionDetail) parent.getUserObject();
                    if ( !pane.isConsistent( detail, level ) )
                    {
                        setForeground( Color.red );
                    }
                }
                else
                {
                    setText( "unknown value of class " + newValue.getClass() );
                }
                if ( selected )
                {
                    setBackgroundSelectionColor( UIManager.getColor( "Tree.selectionBackground" ) );
                }
                else
                {
                    setBackgroundNonSelectionColor( UIManager.getColor( "Tree.background" ) );
                }
            }

            return this;
        }
    }

    public static class PrefixedIcon extends BufferedImage
    {
        public PrefixedIcon( final ImageIcon icon, final int sizeOfPrefix )
        {
            super( 32 + sizeOfPrefix, 16, TYPE_INT_ARGB_PRE );

            final Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
            map.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            map.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
            final RenderingHints hints = new RenderingHints( map );
            final Graphics2D g2 = this.createGraphics();
            g2.setRenderingHints( hints );

            final int w = 32;
            final int h = 16;
            g2.setColor( UIManager.getColor( "Tree.hash" ) );
            drawDashedLine( g2, 0, h / 2, sizeOfPrefix - 5, h / 2, 8, 4 );
            g2.drawImage( icon.getImage(), sizeOfPrefix, 0, w, h, null );
        }

        private void drawDashedLine( final Graphics2D g, int x1, final int y1, final int x2, final int y2, final int dashLength, final int dashSpace )
        {
            while ( x1 < x2 )
            {
                g.drawLine( x1, y1, Math.min( x1 + dashLength, x2 ), y2 );
                x1 += dashLength + dashSpace;
            }
        }
    }
}
