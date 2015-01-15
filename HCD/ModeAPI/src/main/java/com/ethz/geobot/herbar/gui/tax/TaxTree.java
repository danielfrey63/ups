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

/*
 * TaxTreePanel.java
 *
 * Created on 4. April 2002, 16:15
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.component.tree.SearchableTree;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.event.ModelChangeEvent;
import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import com.ethz.geobot.herbar.util.DefaultTaxonTreeNode;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple JTree that takes a TreeNode and uses a DefaultTreeModel with a Taxon specific CellRenderer. This tree allows for searches by implementing the #TreeFinder interface.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxTree extends SearchableTree implements ModelChangeListener
{
    private final static Logger LOG = LoggerFactory.getLogger( TaxTree.class );

    private final static TreeNode EMPTY_TREE = new DefaultMutableTreeNode();

    /** Creates a new instance of TaxTreePanel */
    public TaxTree()
    {
        super( EMPTY_TREE );
        this.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setCellRenderer( new TaxTreeCellRenderer() );
    }

    /**
     * Creates a new instance of TaxTreePanel
     *
     * @param root the root node to display
     */
    public TaxTree( final Taxon root )
    {
        super( createTaxonTreeNode( root ) );
        this.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setCellRenderer( new TaxTreeCellRenderer() );
    }

    /**
     * initialize a tax tree which displays all taxa in a {@link HerbarModel} Add itself as a {@link ModelChangeListener} to the given {@link HerbarModel}
     *
     * @param model the model to be shown
     */
    public TaxTree( final HerbarModel model )
    {
        this( model.getRootTaxon() );
        this.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        model.addModelChangeListener( this );
    }

    /**
     * initialize a tax tree with a new {@link HerbarModel} Add itself as a {@link ModelChangeListener} to the given {@link HerbarModel}
     *
     * @param model the model to be shown or null for an empty model
     */
    public void setHerbarModel( final HerbarModel model )
    {
        if ( model != null )
        {
            setRootTaxon( model.getRootTaxon() );
            model.addModelChangeListener( this );
        }
        else
        {
            setRootTaxon( null );
        }
    }

    /**
     * change the root taxon of the tree. This method support a null argument. It displays on null a empty tree
     *
     * @param tax Taxon object or null
     */
    public void setRootTaxon( final Taxon tax )
    {
        if ( tax != null )
        {
            DefaultTaxonTreeNode.clearCache();
            setRootTreeNode( createTaxonTreeNode( tax ) );
        }
        else
        {
            setRootTreeNode( EMPTY_TREE );
        }
        revalidate();
        repaint();
    }

    /**
     * return the root object as TreeNode.
     *
     * @return the root TreeNode object
     */
    public TreeNode getRootAsTreeNode()
    {
        return (TreeNode) this.getModel().getRoot();
    }

    /**
     * generate a tree node for a specific taxon.
     *
     * @param tax taxon which should be encapsulate in a TreeNode
     */
    private static TreeNode createTaxonTreeNode( final Taxon tax )
    {
        return DefaultTaxonTreeNode.createOrGetTreeNode( tax );
    }

    /**
     * get the selected taxon.
     *
     * @return selected taxon or null if no taxon is selected.
     */
    public Taxon getSelectedTaxon()
    {
        final TreePath path = getSelectionPath();
        if ( path != null )
        {
            final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();
            return node.getTaxon();
        }
        else
        {
            return null;
        }
    }

    private TreePath getTreePathForTaxon( final TreePath path, final Taxon taxon )
    {
        final DefaultTaxonTreeNode node = (DefaultTaxonTreeNode) path.getLastPathComponent();

        if ( node.getTaxon().equals( taxon ) )
        {
            return path;
        }

        for ( Enumeration e = node.children(); e.hasMoreElements(); )
        {
            final DefaultTaxonTreeNode subNode = (DefaultTaxonTreeNode) e.nextElement();
            TreePath subPath = path.pathByAddingChild( subNode );
            subPath = getTreePathForTaxon( subPath, taxon );
            if ( subPath != null )
            {
                return subPath;
            }
        }

        return null;
    }

    /**
     * set the selected taxon
     *
     * @param taxon selected taxon
     */
    public void setSelectedTaxon( final Taxon taxon )
    {
        if ( taxon != null )
        {
            TreePath tp = new TreePath( getModel().getRoot() );
            tp = getTreePathForTaxon( tp, taxon );
            setSelection( tp );
        }
        else
        {
            clearSelection();
        }
    }

    public void modelChanged( final ModelChangeEvent event )
    {
        LOG.info( "HerbarModel change receive" );
        final HerbarModel model = (HerbarModel) event.getSource();
        setRootTaxon( model.getRootTaxon() );
    }
}

// $Log: TaxTree.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.20  2005/06/17 06:39:58  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.19  2004/08/31 22:10:16  daniel_frey
// Examlist loading working
//
// Revision 1.18  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.17  2003/04/02 14:49:03  daniel_frey
// - Revised wizards
//
// Revision 1.16  2003/02/18 17:04:02  dirk_hoffmann
// correct FilterModel issues
//
// Revision 1.15  2003/02/18 02:30:51  dirk_hoffmann
// correct wizard problems
//
// Revision 1.14  2003/02/12 19:13:26  dirk_hoffmann
// delete FilterDefinition
//
// Revision 1.13  2003/01/23 10:54:27  daniel_frey
// - Optimized imports
//
// Revision 1.12  2003/01/22 12:26:54  daniel_frey
// - Removed bug where null Taxon was mapped to root tree node in setSelectedTaxon.
//
// Revision 1.11  2003/01/22 11:57:29  daniel_frey
// - Moved setSelection definitively to super class
//
// Revision 1.10  2002/09/13 11:40:25  dirk
// fix null selection problem
//
// Revision 1.9  2002/09/12 13:41:45  dirk
// suppport null in setRootTaxon
//
// Revision 1.8  2002/09/12 12:17:12  dirk
// add support for empty TaxTree
//
// Revision 1.7  2002/08/05 11:27:12  Dani
// - Moved to ch.xmatrix
//
// Revision 1.6  2002/08/02 00:42:18  Dani
// Optimized import statements
//
// Revision 1.5  2002/07/11 13:56:29  dirk
// add setSelectedTaxon method
//
