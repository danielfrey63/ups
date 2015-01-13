package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.builder.Builder;
import ch.jfactory.application.view.search.SearchableUtils;
import ch.jfactory.component.ObjectPopup;
import ch.jfactory.component.RendererPanel;
import ch.jfactory.resource.ImageLocator;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Abfragen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.*;
import com.ethz.geobot.herbar.gui.tax.TaxTree;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.util.DefaultTaxonTreeNode;
import com.ethz.geobot.herbar.util.TaxonTreeNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import static org.apache.commons.lang.ArrayUtils.contains;

public class NavigationBuilder implements Builder
{
    private final HerbarContext context;
    private final TaxStateModel taxStateModel;
    private final RendererPanel panel = new RendererPanel();
    private final JRadioButton radio = new JRadioButton();
    private final JPanel navigation = new JPanel( new BorderLayout() );

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
        taxTree.setCellRenderer( new TreeCellRenderer()
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
        } );
    }

    private void setController( final TaxTree taxTree )
    {
        final Controller controller = new Controller( taxTree );
        taxTree.addKeyListener( controller );
        taxTree.addMouseListener( controller );
    }

    private void setListeners( final TaxTree taxTree )
    {
        taxStateModel.addPropertyChangeListener( Focus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                taxTree.setSelectedTaxon( (Taxon) e.getNewValue() );
            }
        } );
        taxStateModel.addPropertyChangeListener( Model.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                taxTree.setRootTaxon( taxStateModel.getModel().getRootTaxon() );
            }
        } );
        taxStateModel.addPropertyChangeListener( SubModus.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final DefaultTreeModel model = (DefaultTreeModel) taxTree.getModel();
                TreePath path = taxTree.getSelectionPath();
                Object node;
                do
                {
                    node = path.getLastPathComponent();
                    model.nodeChanged( (TreeNode) node );
                    path = path.getParentPath();
                }
                while ( path != null && path.getPathCount() > 0 );
            }
        } );
        taxTree.addAncestorListener( new AncestorListener()
        {
            @Override
            public void ancestorAdded( AncestorEvent event )
            {
                final DefaultTreeModel model = (DefaultTreeModel) taxTree.getModel();
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) model.getRoot();
                final TreePath path = new TreePath( node );
                ensureVisibleNodes( taxTree, model, path );
            }

            @Override
            public void ancestorRemoved( AncestorEvent event )
            {
            }

            @Override
            public void ancestorMoved( AncestorEvent e )
            {
            }
        } );
        ;
    }

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

    private class Controller extends MouseAdapter implements KeyListener, ActionListener
    {

        private final TaxTree taxTree;
        private final Timer timer = new Timer( (Integer) getDefaultToolkit().getDesktopProperty( "awt.multiClickInterval" ), this );

        private final RendererPanel panel = new RendererPanel();
        private MouseEvent lastEvent = null;

        public Controller( final TaxTree taxTree )
        {
            this.taxTree = taxTree;
            panel.setIcon( ImageLocator.getIcon( "iconArt.gif" ) );
            panel.setText( "Placeholder" );
            panel.setPrefixComponent( new JRadioButton() );
            panel.setShowPrefixComponent( true );
            panel.update();
            panel.doLayout();
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            // handle single click
            timer.stop();
            handleFocus( taxTree.getPathForLocation( lastEvent.getX(), lastEvent.getY() ) );
        }

        public void mouseClicked( final MouseEvent e )
        {
            // region and double click
            if ( e.getClickCount() > 2 )
            {
                return;
            }

            final TreePath path = taxTree.getPathForLocation( e.getX(), e.getY() );
            final Rectangle bounds = taxTree.getPathBounds( path );
            lastEvent = e;
            if ( bounds != null && path != null )
            {
                final Rectangle scopeRegion = panel.getComponent( 2 ).getBounds();
                final Rectangle levelRegion = panel.getComponent( 1 ).getBounds();
                final int x = e.getX() - bounds.x;
                final int y = e.getY() - bounds.y;
                if ( scopeRegion.contains( x, y ) )
                {
                    handleScope( path );
                }
                else if ( levelRegion.contains( x, y ) )
                {
                    handleLevel( path );
                }
                else if ( timer.isRunning() )
                {
                    timer.stop();
                    handleDoubleClick( path );
                }
                else
                {
                    timer.restart();
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
            if ( e.getKeyChar() == ' ' )
            {
                handleScope( taxTree.getSelectionPath() );
                e.consume();
            }
        }

        private void handleDoubleClick( final TreePath path )
        {
            System.out.println( "handling double click at " + path );
        }

        private void handleScope( final TreePath path )
        {
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                if ( !taxStateModel.getScope().equals( taxon ) )
                {
                    taxStateModel.setScope( taxon );
                    taxTree.repaint();
                }
            }
        }

        private void handleLevel( final TreePath path )
        {
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                if ( !taxStateModel.getLevel().equals( taxon.getLevel() ) )
                {
                    taxStateModel.setLevel( taxon.getLevel() );
                    taxTree.repaint();
                }
            }
        }

        private void handleFocus( final TreePath path )
        {
            if ( path != null )
            {
                final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
                final Taxon taxon = node.getTaxon();
                if ( !taxStateModel.getFocus().equals( taxon ) && contains( taxStateModel.getTaxList(), taxon ) )
                {
                    taxStateModel.setFocus( taxon );
                    taxTree.repaint();
                }
            }
        }
    }

    public class ListPopUp extends ObjectPopup<HerbarModel>
    {
        public ListPopUp()
        {
            super( context.getModels().toArray( new HerbarModel[context.getModels().size()] ) );
        }

        public void showPopUp( final Component component )
        {
            showPopUp( component, taxStateModel.getModel() );
        }

        public void itemSelected( final HerbarModel obj )
        {
            context.setCurrentModel( obj );
            taxStateModel.setModel( obj );
        }
    }
}
