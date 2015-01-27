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

    /**
     * Creates a new instance of TaxTreePanel
     */
    public TaxTree()
    {
        super( EMPTY_TREE );
        this.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setCellRenderer( new TaxTreeCellRenderer() );
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
