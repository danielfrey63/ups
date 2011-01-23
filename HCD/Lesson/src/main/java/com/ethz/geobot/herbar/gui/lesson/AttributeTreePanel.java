/*
 * Herbar CD-ROM version 2
 *
 * AbstractTreePanel.java
 *
 * Created on Feb 13, 2003 2:37:09 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.ObjectMenuItem;
import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.SimpleTransientGraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import com.ethz.geobot.herbar.gui.VirtualGraphTreeFactory;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MutableTaxon;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public abstract class AttributeTreePanel extends JPanel
{
    private static final String SIMILAR_TAXA = "Ähnliche Taxa";

    private static final int THRESHOLD = 8;

    private final VirtualGraphTreeNodeFilter filter;

    private final JTree tree;

    private final Level stopperLevel;

    private final String rootNodeName;

    private final HerbarContext herbarContext;

    private final TaxStateModel taxStateModel;

    AttributeTreePanel( final HerbarContext herbarContext, final Level stopper, final TaxStateModel taxStateModel, final String rootNodeName )
    {
        this.herbarContext = herbarContext;
        this.stopperLevel = stopper;
        this.rootNodeName = rootNodeName;
        this.filter = registerFilter();
        this.taxStateModel = taxStateModel;

        setBorder( new EmptyBorder( 2, 2, 2, 2 ) );

        tree = VirtualGraphTreeFactory.getVirtualTree( filter );
        tree.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mousePressed( final MouseEvent e )
            {
                showPopUp( e );
            }

            @Override
            public void mouseReleased( final MouseEvent e )
            {
                showPopUp( e );
            }
        } );
        new TreeExpander( tree, 2 );
        this.setLayout( new BorderLayout() );
        this.add( new JScrollPane( tree ), BorderLayout.CENTER );
    }

    private void showPopUp( final MouseEvent e )
    {
        final TreePath[] paths = tree.getSelectionPaths();
        if ( e.isPopupTrigger() && paths != null && paths.length > 0 )
        {
            final GraphNodeList intersection = getSimilarTaxa( paths );
            final Object[] list = intersection.getAll();
            final int length = list.length;
            final Taxon[] copy = new Taxon[length];
            System.arraycopy( list, 0, copy, 0, length );
            Arrays.sort( copy, new ToStringComparator<Object>() );
            final JPopupMenu menu = new JPopupMenu();
            if ( intersection.size() > THRESHOLD )
            {
                final JMenuItem item = new JMenuItem( SIMILAR_TAXA + "..." );
                item.addActionListener( new ActionListener()
                {
                    public void actionPerformed( final ActionEvent e )
                    {
                        final ListDialog<Taxon> dialog = new ListDialog<Taxon>( herbarContext.getHerbarGUIManager().getParentFrame(), "DIALOG.SIMILAR", copy );
                        dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                        dialog.setSize( 300, 300 );
                        dialog.setLocationRelativeTo( getTopLevelAncestor() );
                        dialog.setVisible( true );
                        if ( dialog.isAccepted() )
                        {
                            final Taxon taxon = dialog.getSelectedData( new Taxon[0] )[0];
                            taxStateModel.setLevel( taxon.getLevel() );
                            taxStateModel.setFocus( taxon );
                        }
                    }
                } );
                menu.add( item );
            }
            else
            {
                final JMenu subMenu = new JMenu( SIMILAR_TAXA );
                for ( final Taxon taxon : copy )
                {
                    final ObjectMenuItem<Taxon> item = new ObjectMenuItem<Taxon>( taxon );
                    item.setEnabled( taxon != null && !taxon.equals( taxStateModel.getFocus() ) );
                    item.addActionListener( new ActionListener()
                    {
                        public void actionPerformed( final ActionEvent e )
                        {
                            if ( taxon != null )
                            {
                                taxStateModel.setLevel( taxon.getLevel() );
                                taxStateModel.setFocus( taxon );
                            }
                        }
                    } );
                    subMenu.add( item );
                }
                menu.add( subMenu );
            }
            menu.show( AttributeTreePanel.this, e.getX(), e.getY() );
        }
    }

    private GraphNodeList getSimilarTaxa( final TreePath[] paths )
    {
        GraphNodeList intersection = null;
        for ( final TreePath path : paths )
        {
            final GraphTreeNode graphTreeNode = (GraphTreeNode) path.getLastPathComponent();
            final GraphNodeList parents = graphTreeNode.getDependent().getParents( MutableTaxon.class );
            if ( intersection == null )
            {
                intersection = parents;
            }
            else
            {
                intersection = intersection.intersect( parents );
            }
        }
        return intersection;
    }

    /**
     * Use this method to instantiate the correct filter set. This method is called from within the super constructor.
     *
     * @return the filter used for displaying the tree.
     */
    public abstract VirtualGraphTreeNodeFilter registerFilter();

    public void setTaxonFocus( Taxon taxon )
    {
        final GraphNode vRoot = new SimpleTransientGraphNode();
        vRoot.setName( rootNodeName );
        Level level = taxon.getLevel();
        if ( stopperLevel == null || stopperLevel.isHigher( level ) )
        {
            while ( level != null && stopperLevel != null && stopperLevel.isHigher( level ) )
            {
                vRoot.addChild( 0, taxon.getAsGraphNode() );
                taxon = taxon.getParentTaxon();
                level = taxon.getLevel();
            }
        }
        else
        {
            vRoot.addChild( taxon.getAsGraphNode() );
        }
        VirtualGraphTreeFactory.updateModel( tree, filter, vRoot );
    }
}

