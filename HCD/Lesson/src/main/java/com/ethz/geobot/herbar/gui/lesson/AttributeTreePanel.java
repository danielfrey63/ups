/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class AttributeTreePanel extends JPanel
{
    private static final String SIMILAR_TAXA = "Ähnliche Taxa";

    private static final int THRESHOLD = 8;

    private final VirtualGraphTreeNodeFilter filter;

    private final JTree tree;

    private final Level stopperLevel;

    private final String rootNodeName;

    private final HerbarContext herbarContext;

    private final TaxStateModel taxStateModel;

    AttributeTreePanel( final HerbarContext herbarContext, final Level stopper, final TaxStateModel taxStateModel, final String rootNodeName, final VirtualGraphTreeNodeFilter filter )
    {
        this.herbarContext = herbarContext;
        this.stopperLevel = stopper;
        this.rootNodeName = rootNodeName;
        this.filter = filter;
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
            final Taxon[] taxa = getIntersectionInCurrentList( getSimilarTaxa( paths ) );
            Arrays.sort( taxa, new ToStringComparator<Object>() );
            final JPopupMenu menu = new JPopupMenu();
            if ( taxa.length > THRESHOLD )
            {
                menu.add( getListDialog( taxa ) );
            }
            else
            {
                final JMenu item = getPopUp( taxa );
                menu.add( item );
                item.setEnabled( taxa.length > 0 );
            }
            menu.show( AttributeTreePanel.this, e.getX(), e.getY() );
        }
    }

    private Taxon[] getIntersectionInCurrentList( final GraphNodeList intersection )
    {
        final List<Taxon> result = new ArrayList<Taxon>();
        for ( int i = 0; i < intersection.size(); i++ )
        {
            final GraphNode taxon = intersection.get( i );
            final Taxon currentTaxon = taxStateModel.getModel().getTaxon( taxon.getName() );
            intersection.remove( taxon );
            if ( currentTaxon != null )
            {
                result.add( currentTaxon );
            }
        }
        return result.toArray( new Taxon[result.size()] );
    }

    private JMenu getPopUp( final Taxon[] copy )
    {
        final JMenu subMenu = new JMenu( SIMILAR_TAXA + " (" + copy.length + ")" );
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
                        setNewFocus( taxon );
                    }
                }
            } );
            subMenu.add( item );
        }
        return subMenu;
    }

    private void setNewFocus( final Taxon taxon )
    {
        taxStateModel.setLevel( taxon );
        taxStateModel.setFocus( taxon );
    }

    private JMenuItem getListDialog( final Taxon[] copy )
    {
        final JMenuItem item = new JMenuItem( SIMILAR_TAXA + " (" + copy.length + ")..." );
        item.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final ListDialog<Taxon> dialog = new ListDialog<Taxon>( herbarContext.getHerbarGUIManager().getParentFrame(), "DIALOG.SIMILAR", copy );
                dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                dialog.setSize( 300, 600 );
                dialog.setLocationRelativeTo( getTopLevelAncestor() );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    setNewFocus( dialog.getSelectedData( new Taxon[0] )[0] );
                }
            }
        } );
        return item;
    }

    private GraphNodeList getSimilarTaxa( final TreePath[] paths )
    {
        GraphNodeList intersection = null;
        for ( final TreePath path : paths )
        {
            final GraphTreeNode graphTreeNode = (GraphTreeNode) path.getLastPathComponent();
            final GraphNodeList parents = graphTreeNode.getDependent().getParents( Taxon.class );
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

    public void setTaxonFocus( Taxon taxon )
    {
        final GraphNode vRoot = new SimpleTransientGraphNode();
        vRoot.setName( rootNodeName );
        Level level = taxon.getLevel();
        if ( stopperLevel == null || stopperLevel.isHigher( level ) )
        {
            while ( level != null && (stopperLevel != null && stopperLevel.isHigher( level ) || stopperLevel == null) )
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
