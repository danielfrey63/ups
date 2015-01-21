package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.application.view.search.SearchableUtils;
import ch.jfactory.component.RendererPanel;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.gui.tax.TaxTree;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import com.ethz.geobot.herbar.model.filter.FilterTaxon;
import com.ethz.geobot.herbar.util.DefaultTaxonTreeNode;
import com.ethz.geobot.herbar.util.TaxonTreeNode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Abfragen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Edit;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Focus;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Model;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Ordered;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SubModus;
import static java.awt.Toolkit.getDefaultToolkit;
import static org.apache.commons.lang.ArrayUtils.contains;

public class NavigationBuilder implements Builder
{
    private final HerbarContext context;
    private final TaxStateModel taxStateModel;
    private final RendererPanel panel = new RendererPanel();
    private final JRadioButton radio = new JRadioButton();
    private final JCheckBox check = new JCheckBox();
    private final JPanel navigation = new JPanel( new BorderLayout() );
    private final TreeCellRenderer useRenderer = new LearnAndQueryTreeCellRenderer();
    private final TreeCellRenderer editRenderer = new EditTreeCellRenderer();

    private UseController useController;
    private EditController editController;

    public NavigationBuilder( final HerbarContext context, final TaxStateModel taxStateModel )
    {
        this.context = context;
        this.taxStateModel = taxStateModel;
    }

    @Override
    public JComponent getPanel()
    {
        final TaxTree taxTree = new TaxTree();
        setSearchable( taxTree );
        setRenderer( taxTree );
        setController( taxTree );
        setListeners( taxTree );

        initRendererPanel();

        taxTree.setRootTaxon( taxStateModel.getModel().getRootTaxon() );

        navigation.add( new JScrollPane( taxTree ), BorderLayout.CENTER );

        return navigation;
    }

    private void setSearchable( final TaxTree taxTree )
    {
        SearchableUtils.installSearchable( taxTree ).setRecursive( true );
    }

    private void initRendererPanel()
    {
        panel.setPrefixComponent( radio );
        panel.setShowPrefixComponent( true );
    }

    public void setRenderer( final TaxTree taxTree )
    {
        taxTree.setCellRenderer( useRenderer );
    }

    private void setController( final TaxTree taxTree )
    {
        useController = new UseController( taxTree );
        taxTree.addKeyListener( useController );
        taxTree.addMouseListener( useController );

        editController = new EditController( taxTree );
    }

    private void setListeners( final TaxTree taxTree )
    {
        taxStateModel.addPropertyChangeListener( Focus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                taxTree.setSelectedTaxon( (Taxon) e.getNewValue() );
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( Model.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                taxTree.setRootTaxon( taxStateModel.getModel().getRootTaxon() );
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( SubModus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( Ordered.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( Edit.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final boolean isEdit = e.getNewValue() == EDIT;
                panel.setPrefixComponent( isEdit ? check : radio );
                panel.setShowPrefixComponent( true );
                taxTree.setRootTaxon( isEdit ? context.getDataModel().getRootTaxon() : taxStateModel.getModel().getRootTaxon() );
                taxTree.setCellRenderer( isEdit ? editRenderer : useRenderer );
                taxTree.removeKeyListener( isEdit ? useController : editController );
                taxTree.removeMouseListener( isEdit ? useController : editController );
                taxTree.addKeyListener( isEdit ? editController : useController );
                taxTree.addMouseListener( isEdit ? editController : useController );
            }
        } );
        taxTree.addAncestorListener( new AncestorListener()
        {
            @Override
            public void ancestorAdded( final AncestorEvent e )
            {
                final DefaultTreeModel model = (DefaultTreeModel) taxTree.getModel();
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) model.getRoot();
                final TreePath path = new TreePath( node );
                ensureVisibleNodes( taxTree, model, path );
            }

            @Override
            public void ancestorRemoved( final AncestorEvent e )
            {
            }

            @Override
            public void ancestorMoved( final AncestorEvent e )
            {
            }
        } );
    }

    /**
     * Ensures the visibility of the selected node and its parents.
     *
     * @param taxTree the tree with the selected node
     */
    private void ensureVisibility( final TaxTree taxTree )
    {
        final DefaultTreeModel model = (DefaultTreeModel) taxTree.getModel();
        TreePath path = taxTree.getSelectionPath();
        if ( path != null )
        {
            while ( path != null && path.getPathCount() > 0 )
            {
                model.nodeChanged( (TreeNode) path.getLastPathComponent() );
                path = path.getParentPath();
            }
        }
    }

    /**
     * Ensures the visibility of all visible of the node passed and all of its children.
     *
     * @param tree  the tree with the visible nodes
     * @param model the tree model
     * @param path  the path of the node to ensure visibility
     */
    private void ensureVisibleNodes( final JTree tree, final DefaultTreeModel model, final TreePath path )
    {
        final Enumeration<TreePath> descendants = tree.getExpandedDescendants( path );
        final TreeNode node = (TreeNode) path.getLastPathComponent();
        model.nodeChanged( node );
        while ( descendants != null && descendants.hasMoreElements() )
        {
            final TreePath childPath = descendants.nextElement();
            ensureVisibleNodes( tree, model, childPath );
        }
        for ( int i = 0; i < node.getChildCount(); i++ )
        {
            model.nodeChanged( node.getChildAt( i ) );
        }
    }

    public abstract class AbstractController extends MouseAdapter implements KeyListener, ActionListener
    {
        protected final TaxTree taxTree;
        private final Timer timer = new Timer( (Integer) getDefaultToolkit().getDesktopProperty( "awt.multiClickInterval" ), this );
        private MouseEvent lastEvent = null;

        public AbstractController( final TaxTree taxTree )
        {
            this.taxTree = taxTree;
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            // handle single click
            timer.stop();
            final TreePath path = taxTree.getPathForLocation( lastEvent.getX(), lastEvent.getY() );
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                handleLabelClick( taxon );
            }
        }

        public void mouseClicked( final MouseEvent e )
        {
            // region and double click
            if ( e.getClickCount() > 2 )
            {
                return;
            }

            final TreePath path = taxTree.getPathForLocation( e.getX(), e.getY() );
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                final Rectangle bounds = taxTree.getPathBounds( path );
                lastEvent = e;
                if ( bounds != null && taxon != null )
                {
                    final Rectangle boxRegion = panel.getComponent( 2 ).getBounds();
                    final Rectangle iconRegion = panel.getComponent( 1 ).getBounds();
                    final int x = e.getX() - bounds.x;
                    final int y = e.getY() - bounds.y;
                    if ( boxRegion.contains( x, y ) )
                    {
                        handleComponentClick( taxon );
                    }
                    else if ( iconRegion.contains( x, y ) )
                    {
                        handleIconClick( taxon );
                    }
                    else if ( timer.isRunning() )
                    {
                        timer.stop();
                        handleDoubleClick( taxon );
                    }
                    else
                    {
                        timer.restart();
                    }
                }
            }
        }

        @Override
        public void keyTyped( final KeyEvent e )
        {
            handleKey( e );
        }

        @Override
        public void keyPressed( final KeyEvent e )
        {
            handleKey( e );
        }

        @Override
        public void keyReleased( final KeyEvent e )
        {
            handleKey( e );
        }

        private void handleKey( final KeyEvent e )
        {
            final TreePath path = taxTree.getSelectionPath();
            final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
            final Taxon taxon = node.getTaxon();
            if ( e.getKeyChar() == ' ' )
            {
                handleComponentClick( taxon );
                e.consume();
            }
        }

        protected abstract void handleDoubleClick( Taxon taxon );

        protected abstract void handleComponentClick( Taxon taxon );

        protected abstract void handleIconClick( Taxon taxon );

        protected abstract void handleLabelClick( Taxon taxon );
    }

    private class UseController extends AbstractController
    {

        public UseController( final TaxTree taxTree )
        {
            super( taxTree );
        }

        @Override
        protected void handleDoubleClick( final Taxon taxon )
        {
            System.out.println( "handling double click at " + taxon );
        }

        @Override
        protected void handleComponentClick( final Taxon taxon )
        {
            if ( !taxStateModel.getScope().equals( taxon ) )
            {
                taxStateModel.setScope( taxon );
                taxTree.repaint();
            }
        }

        @Override
        protected void handleIconClick( final Taxon taxon )
        {
            if ( !taxStateModel.getLevel().equals( taxon.getLevel() ) )
            {
                taxStateModel.setLevel( taxon.getLevel() );
                taxTree.repaint();
            }
        }

        @Override
        protected void handleLabelClick( final Taxon taxon )
        {
            if ( !taxStateModel.getFocus().equals( taxon ) && contains( taxStateModel.getTaxList(), taxon ) )
            {
                taxStateModel.setFocus( taxon );
                taxTree.repaint();
            }
        }
    }

    private class EditController extends AbstractController
    {
        public EditController( final TaxTree taxTree )
        {
            super( taxTree );
        }

        @Override
        protected void handleDoubleClick( final Taxon taxon )
        {
            handleClick( taxon );
        }

        @Override
        protected void handleComponentClick( final Taxon taxon )
        {
            handleClick( taxon );
        }

        @Override
        protected void handleIconClick( final Taxon taxon )
        {
            handleClick( taxon );
        }

        @Override
        protected void handleLabelClick( final Taxon taxon )
        {
            handleClick( taxon );
        }

        private void handleClick( final Taxon taxon )
        {
            final HerbarModel model = taxStateModel.getModel();
            if ( model instanceof FilterModel )
            {
                final FilterModel filterModel = (FilterModel) model;
                final FilterTaxon filterTaxon = filterModel.getTaxon( taxon.getName() );
                if ( filterTaxon == null )
                {
                    filterModel.addFilterTaxon( taxon );
                }
                else
                {
                    filterModel.removeFilterTaxon( filterTaxon );
                }
            }
            taxTree.repaint();
        }
    }
//
//    public class ListPopUp extends ObjectPopup<HerbarModel>
//    {
//        public ListPopUp()
//        {
//            super( context.getModels().toArray( new HerbarModel[context.getModels().size()] ) );
//        }
//
//        public void showPopUp( final Component component )
//        {
//            showPopUp( component, taxStateModel.getModel() );
//        }
//
//        public void itemSelected( final HerbarModel obj )
//        {
//            context.setCurrentModel( obj );
//            taxStateModel.setModel( obj );
//        }
//    }

    private class LearnAndQueryTreeCellRenderer implements TreeCellRenderer
    {
        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            final Taxon taxon = ((TaxonTreeNode) value).getTaxon();
            final boolean query = taxStateModel.getSubMode( taxon.getName() ) == Abfragen;
            final Taxon filterTaxon = taxStateModel.getModel().getTaxon( taxon.getName() );
            final Level level = taxon.getLevel();
            final Taxon[] taxList = taxStateModel.getTaxList();
            panel.setIcon( ImageLocator.getIcon( "icon" + (level == null ? "" : level.getName()) + ".gif" ) );
            panel.setText( query ? "Welches Taxon ist das?" : taxon.toString() );
            final boolean containsFilterTaxon = contains( taxList, filterTaxon );
            final boolean containsTaxon = contains( taxList, taxon );
            final boolean listHasTaxon = filterTaxon != null;
            final boolean sameLevels = taxStateModel.getLevel() == taxon.getLevel();
            panel.setEnabled( listHasTaxon && sameLevels && (containsFilterTaxon || containsTaxon) );
            panel.setSelected( taxStateModel.getFocus().equals( taxon ) );
            radio.setEnabled( true );
            radio.setSelected( taxStateModel.getScope().equals( taxon ) );
            panel.update();
            return panel;
        }
    }

    private class EditTreeCellRenderer implements TreeCellRenderer
    {
        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            final Taxon taxon = ((TaxonTreeNode) value).getTaxon();
            final Level level = taxon.getLevel();
            panel.setIcon( ImageLocator.getIcon( "icon" + (level == null ? "" : level.getName()) + ".gif" ) );
            panel.setText( taxon.toString() );
            panel.setEnabled( true );
            panel.setSelected( false );
            check.setEnabled( false );
            check.setSelected( taxStateModel.getModel().getTaxon( taxon.getName() ) != null );
            panel.update();
            return panel;
        }
    }
}
