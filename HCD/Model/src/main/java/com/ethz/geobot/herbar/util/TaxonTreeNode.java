/*
 * TaxonTreeNode.java
 *
 * Created on 5. August 2002, 1:12
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.util;

import com.ethz.geobot.herbar.model.Taxon;
import javax.swing.tree.TreeNode;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface TaxonTreeNode extends TreeNode {

    /**
     * Returns the wrapped Taxon object.
     *
     * @return the Taxon object
     */
    public Taxon getTaxon();
}
