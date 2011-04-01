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
 * TaxonTreeNode.java
 *
 * Created on 31. Mai 2002, 15:49
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.util;

import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;

/**
 * Wraps a <code>Taxon</code> into a non-mutable <code>TreeNode</code>. The <code>DefaultTaxonTreeNode</code> may be used in a tree to display the <code>Taxon</code> objects..
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 * @see TaxonTreeNode
 */
public class DefaultTaxonTreeNode implements TaxonTreeNode
{
    /** The <code>Taxon</code> object to be wrapped. */
    private final Taxon tax;

    /**
     * Wrappes a Taxon into a TreeNode.
     *
     * @param tax The Taxon to be wrapped
     */
    public DefaultTaxonTreeNode( final Taxon tax )
    {
        this.tax = tax;
    }

    /** @see TaxonTreeNode#getTaxon() */
    public Taxon getTaxon()
    {
        return tax;
    }

    /** @see TreeNode#getAllowsChildren() */
    public boolean getAllowsChildren()
    {
        return tax.getLevel().getChildLevel() == null;
    }

    /** @see TreeNode#getChildAt(int) */
    public TreeNode getChildAt( final int param )
    {
        return new DefaultTaxonTreeNode( tax.getChildTaxon( param ) );
    }

    /** @see TreeNode#getChildCount() */
    public int getChildCount()
    {
        return tax.getChildTaxa().length;
    }

    /** @see TreeNode#getIndex(TreeNode) */
    public int getIndex( final TreeNode treeNode )
    {
        return tax.getChildTaxon( ( (DefaultTaxonTreeNode) treeNode ).getTaxon() );
    }

    /** @see TreeNode#getParent() */
    public TreeNode getParent()
    {
        return new DefaultTaxonTreeNode( tax.getParentTaxon() );
    }

    /** @see TreeNode#isLeaf() */
    public boolean isLeaf()
    {
        return getChildCount() == 0;
    }

    /** @see TreeNode#children() */
    public Enumeration children()
    {
        final Taxon[] taxa = tax.getChildTaxa();
        final Vector v = new Vector( taxa.length );
        for ( final Taxon aTaxa : taxa )
        {
            v.addElement( new DefaultTaxonTreeNode( aTaxa ) );
        }
        return v.elements();
    }

    /** @see Object#toString() */
    public String toString()
    {
        return tax.toString();
    }
}
