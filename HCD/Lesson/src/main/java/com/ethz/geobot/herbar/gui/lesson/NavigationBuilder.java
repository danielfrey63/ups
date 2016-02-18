package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.component.RendererPanel;
import ch.jfactory.component.tree.TreeUtils;
import ch.jfactory.resource.ImageLocator;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.EditState.MODIFY;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode.ABFRAGEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.COLLAPSE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.EDIT;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.FOCUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.LIST;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.ORDER;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SUB_MODUS;
import com.ethz.geobot.herbar.gui.tax.TaxTree;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import com.ethz.geobot.herbar.model.filter.FilterTaxon;
import com.ethz.geobot.herbar.util.DefaultTaxonTreeNode;
import com.ethz.geobot.herbar.util.TaxonTreeNode;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TreeSearchable;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import static org.apache.commons.lang.ArrayUtils.contains;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationBuilder implements Builder
{
    private static final Logger LOG = LoggerFactory.getLogger( NavigationBuilder.class );

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
        new NavigationTreeSearchable( taxTree ).setRecursive( true );
        setRenderer( taxTree );
        setController( taxTree );
        setListeners( taxTree );

        initRendererPanel();

        setRootTaxon( taxTree );
        setFocus( taxTree, taxStateModel.getFocus() );

        navigation.add( new JScrollPane( taxTree ), BorderLayout.CENTER );
        navigation.add( createSearchPanel( taxTree ), BorderLayout.NORTH );

        return navigation;
    }

    private Component createSearchPanel( final TaxTree taxTree )
    {
        final JPanel panel = new JPanel( new BorderLayout() );
        final JTextField field = new JTextField();
        panel.add( field, BorderLayout.CENTER );
        final JButton button = new JButton( "Selektieren" );
        button.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final String text = field.getText();
                if ( !"".equals( text ) )
                {
                    final int index = Integer.parseInt( text );
                    LOG.info( "seleting at " + index + " path " + taxTree.getPathForRow( index ) );
                    taxTree.setSelectionRow( index );
                    taxTree.scrollRowToVisible( index );
                }
                LOG.info( "" + taxTree.getSelectionPath() );
            }
        } );
        panel.add( button, BorderLayout.WEST );
        return panel;
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
        taxStateModel.addPropertyChangeListener( FOCUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                setFocus( taxTree, (Taxon) e.getNewValue() );
            }
        } );
        taxStateModel.addPropertyChangeListener( LIST.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                SearchableUtils.uninstallSearchable( taxTree );
                setRootTaxon( taxTree );
                new NavigationTreeSearchable( taxTree ).setRecursive( true );
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( ORDER.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                ensureVisibility( taxTree );
            }
        } );
        taxStateModel.addPropertyChangeListener( EDIT.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final boolean isEdit = e.getNewValue() == MODIFY;
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
        taxStateModel.addPropertyChangeListener( COLLAPSE.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                final ArrayList<TreePath> breadcrumb = new ArrayList<TreePath>();
                TreePath currentPath = taxTree.getSelectionPath();
                while ( currentPath != null )
                {
                    breadcrumb.add( currentPath );
                    currentPath = currentPath.getParentPath();
                }
                final ArrayList<TreePath> expandedPaths = TreeUtils.getExpandedPaths( taxTree );
                Collections.sort( expandedPaths, new Comparator<TreePath>()
                {
                    @Override
                    public int compare( TreePath o1, TreePath o2 )
                    {
                        return o2.getPathCount() - o1.getPathCount();
                    }
                } );
                for ( final TreePath path : expandedPaths )
                {
                    if ( !breadcrumb.contains( path ) )
                    {
                        taxTree.collapsePath( path );
                    }
                }
                TreeUtils.ensureVisibility( taxTree, breadcrumb.get( 0 ) );
            }
        } );
        taxTree.addAncestorListener( new AncestorListener()
        {
            @Override
            public void ancestorAdded( final AncestorEvent e )
            {
                final boolean visible = taxTree.isRootVisible();
                taxTree.setRootVisible( !visible );
                taxTree.setRootVisible( visible );
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
        taxTree.addTreeSelectionListener( new TreeSelectionListener()
        {
            @Override
            public void valueChanged( TreeSelectionEvent e )
            {
                final TreePath path = e.getNewLeadSelectionPath();
                if ( path != null )
                {
                    final Object object = path.getLastPathComponent();
                    if ( object instanceof TaxonTreeNode )
                    {
                        final Taxon taxon = ((TaxonTreeNode) object).getTaxon();
                        taxStateModel.setFocus( taxon );
                        LOG.info( "search result set " + taxon );
                    }
                }
            }
        } );
    }

    private void setFocus( final TaxTree taxTree, final Taxon taxon )
    {
        taxTree.setSelectedTaxon( taxon );
        ensureVisibility( taxTree );
    }

    private void setRootTaxon( TaxTree taxTree )
    {
        taxTree.setRootTaxon( taxStateModel.getModel().getRootTaxon() );
        ensureVisibility( taxTree );
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
        TreeUtils.ensureVisibility( taxTree, taxTree.getSelectionPath() );
    }

    public abstract class AbstractController extends MouseAdapter implements KeyListener, ActionListener
    {
        protected final TaxTree taxTree;
        private final Timer timer;
        private MouseEvent lastEvent = null;

        public AbstractController( final TaxTree taxTree )
        {
            timer = new Timer( 250, this );
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

        public void mousePressed( final MouseEvent e )
        {
            handleInternalPopupEvent( e );
        }

        public void mouseReleased( final MouseEvent e )
        {
            handleInternalPopupEvent( e );
        }

        private void handleInternalPopupEvent( MouseEvent e )
        {
            if ( e.isPopupTrigger() )
            {
                final TreePath path = taxTree.getPathForLocation( e.getX(), e.getY() );
                if ( path != null )
                {
                    final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                    final Taxon taxon = node.getTaxon();
                    if ( taxon != null )
                    {
                        handlePopup( taxon, e.getPoint().x, e.getPoint().y );
                    }
                }
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
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                if ( e.getKeyChar() == ' ' )
                {
                    handleComponentClick( taxon );
                    e.consume();
                }
            }
        }

        protected abstract void handleDoubleClick( Taxon taxon );

        protected abstract void handleComponentClick( Taxon taxon );

        protected abstract void handleIconClick( Taxon taxon );

        protected abstract void handleLabelClick( Taxon taxon );

        protected abstract void handlePopup( Taxon taxon, int x, int y );
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
            if ( taxStateModel.getLevel() == null || !taxStateModel.getLevel().equals( taxon.getLevel() ) )
            {
                taxStateModel.setLevel( taxon );
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

        @Override
        protected void handlePopup( Taxon taxon, int x, int y )
        {

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
            // Root taxon has no level and should not be removed
            if ( model instanceof FilterModel && taxon.getLevel() != null )
            {
                final FilterModel filterModel = (FilterModel) model;
                final FilterTaxon filterTaxon = filterModel.getTaxon( taxon.getName() );
                if ( filterTaxon == null )
                {
                    taxStateModel.addTaxonToFilterModel( taxon );
                }
                else
                {
                    taxStateModel.removeFilterTaxonFromFilterModel( filterTaxon );
                }
                context.saveModel( filterModel );
            }
            taxTree.repaint();
        }

        @Override
        protected void handlePopup( final Taxon taxon, final int x, final int y )
        {
            final JPopupMenu menu = new JPopupMenu();
            menu.add( new AbstractAction( "Dieses Taxon mit allen Kindern anwählen" )
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    final HerbarModel model = taxStateModel.getModel();
                    if ( model instanceof FilterModel )
                    {
                        LOG.info( "editing \"" + model + "\" adding all children to \"" + taxon + "\"" );
                        final FilterModel filterModel = (FilterModel) model;
                        filterModel.addFilterTaxon( taxon );
                        final Level[] levels = taxon.getSubLevels();
                        for ( final Level level : levels )
                        {
                            final Taxon[] children = taxon.getAllChildTaxa( level );
                            for ( final Taxon child : children )
                            {
                                if ( child.getLevel() != null )
                                {
                                    taxStateModel.addTaxonToFilterModel( child );
                                }
                            }
                        }
                        taxTree.repaint();
                        context.saveModel( model );
                    }
                }
            } );
            menu.add( new AbstractAction( "Dieses Taxon mit allen Kinder abwählen" )
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    final HerbarModel model = taxStateModel.getModel();
                    if ( model instanceof FilterModel )
                    {
                        LOG.info( "editing \"" + model + "\" removing all children from \"" + taxon + "\"" );
                        final FilterModel filterModel = (FilterModel) model;
                        final FilterTaxon filterTaxon = filterModel.getTaxon( taxon.getName() );
                        if ( filterTaxon != null )
                        {
                            filterModel.removeFilterTaxon( filterTaxon );
                            final Level[] levels = filterTaxon.getSubLevels();
                            for ( final Level level : levels )
                            {
                                final FilterTaxon[] children = filterTaxon.getAllChildTaxa( level );
                                for ( final FilterTaxon child : children )
                                {
                                    if ( child.getLevel() != null )
                                    {
                                        taxStateModel.removeFilterTaxonFromFilterModel( child );
                                    }
                                }
                            }
                            taxTree.repaint();
                            context.saveModel( model );
                        }
                    }
                }
            } );
            menu.addSeparator();
            menu.add( new AbstractAction( "Eltern aller gewählten Taxa anwählen" )
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    final HerbarModel model = taxStateModel.getModel();
                    if ( model instanceof FilterModel )
                    {
                        final FilterTaxon[] list = (FilterTaxon[]) taxStateModel.getTaxList();
                        LOG.info( "editing \"" + model + "\" adding all parents to " + list.length + " taxa in list" );
                        for ( FilterTaxon taxon : list )
                        {
                            Taxon dependentTaxon = taxon.getDependentTaxon();
                            while ( dependentTaxon.getParentTaxon() != null )
                            {
                                dependentTaxon = dependentTaxon.getParentTaxon();
                                taxStateModel.addTaxonToFilterModel( dependentTaxon );
                            }
                        }
                        taxTree.repaint();
                        context.saveModel( model );
                    }
                }
            } );
            menu.add( new AbstractAction( "Eltern dieses Taxons anwählen" )
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    final HerbarModel model = taxStateModel.getModel();
                    if ( model instanceof FilterModel )
                    {
                        LOG.info( "editing \"" + model + "\" adding all parents to \"" + taxon + "\"" );
                        final FilterModel filterModel = (FilterModel) model;
                        filterModel.addFilterTaxon( taxon );
                        final FilterTaxon filterTaxon = filterModel.getTaxon( taxon.getName() );
                        Taxon dependentTaxon = filterTaxon.getDependentTaxon();
                        while ( dependentTaxon.getParentTaxon() != null )
                        {
                            dependentTaxon = dependentTaxon.getParentTaxon();
                            taxStateModel.addTaxonToFilterModel( dependentTaxon );
                        }
                        taxTree.repaint();
                        context.saveModel( model );
                    }
                }
            } );
            menu.show( taxTree, x, y );
        }
    }

    private class LearnAndQueryTreeCellRenderer implements TreeCellRenderer
    {
        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            final Taxon taxon = ((TaxonTreeNode) value).getTaxon();
            final boolean query = taxStateModel.getSubMode( taxon ) == ABFRAGEN;
            final Taxon filterTaxon = taxStateModel.getModel().getTaxon( taxon.getName() );
            final Taxon selectedTaxon = ((TaxTree) tree).getSelectedTaxon();
            boolean isParent = false;
            Taxon current = selectedTaxon;
            while ( !isParent && current != null )
            {
                current = current.getParentTaxon();
                isParent = current != null && current == taxon && !(taxon == taxStateModel.getModel().getRootTaxon());
            }
            final Level level = taxon.getLevel();
            final Taxon[] taxList = taxStateModel.getTaxList();
            panel.setIcon( ImageLocator.getIcon( "icon" + (level == null ? "" : level.getName()) + ".gif" ) );
            panel.setText( query ? "Welches Taxon ist das?" : taxon.toString() );
            final boolean containsFilterTaxon = contains( taxList, filterTaxon );
            final boolean containsTaxon = contains( taxList, taxon );
            final boolean listHasTaxon = filterTaxon != null;
            final boolean sameLevels = taxStateModel.getLevel() == taxon.getLevel();
            panel.setEnabled( isParent || listHasTaxon && sameLevels && (containsFilterTaxon || containsTaxon) );
            panel.setSelected( taxStateModel.getFocus().equals( taxon ) );
            radio.setEnabled( true );
            radio.setSelected( taxStateModel.getScope().equals( taxon ) );
            panel.update();
            if ( isParent ) panel.setTextColor( new Color( 0, 200, 0 ) );
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
            check.setEnabled( taxon.getLevel() != null );
            check.setSelected( taxStateModel.getModel().getTaxon( taxon.getName() ) != null );
            panel.update();
            return panel;
        }
    }

    class NavigationTreeSearchable extends TreeSearchable
    {
        public NavigationTreeSearchable( final JTree tree )
        {
            super( tree );
        }

        @Override
        protected String convertElementToString( Object obj )
        {
            if ( obj instanceof TreePath )
            {
                final TreePath path = (TreePath) obj;
                if ( path.getLastPathComponent() instanceof TaxonTreeNode )
                {
                    final Taxon taxon = ((TaxonTreeNode) path.getLastPathComponent()).getTaxon();
                    if ( taxon.getLevel() == taxStateModel.getLevel() )
                    {
                        return super.convertElementToString( obj );
                    }
                }
            }
            return null;
        }
    }
}
