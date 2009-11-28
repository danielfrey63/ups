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
 * Wraps a <code>Taxon</code> into a non-mutable <code>TreeNode</code>. The <code>DefaultTaxonTreeNode</code> may be
 * used in a tree to display the <code>Taxon</code> objects. For a editable <code>DefaultTaxonTreeNode</code> see
 * <code>MutuableTaxonTreeNode</code>.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 * @see TaxonTreeNode
 */
public class DefaultTaxonTreeNode implements TaxonTreeNode
{
    /**
     * The <code>Taxon</code> object to be wrapped.
     */
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

    /**
     * @see com.ethz.geobot.herbar.util.TaxonTreeNode#getTaxon()
     */
    public Taxon getTaxon()
    {
        return tax;
    }

    /**
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren()
    {
        return tax.getLevel().getChildLevel() == null;
    }

    /**
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt( final int param )
    {
        return new DefaultTaxonTreeNode( tax.getChildTaxon( param ) );
    }

    /**
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    public int getChildCount()
    {
        return tax.getChildTaxa().length;
    }

    /**
     * @see javax.swing.tree.TreeNode#getIndex(TreeNode)
     */
    public int getIndex( final TreeNode treeNode )
    {
        return tax.getChildTaxon( ( (DefaultTaxonTreeNode) treeNode ).getTaxon() );
    }

    /**
     * @see javax.swing.tree.TreeNode#getParent()
     */
    public TreeNode getParent()
    {
        return new DefaultTaxonTreeNode( tax.getParentTaxon() );
    }

    /**
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf()
    {
        return getChildCount() == 0;
    }

    /**
     * @see javax.swing.tree.TreeNode#children()
     */
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

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return tax.toString();
    }
}
