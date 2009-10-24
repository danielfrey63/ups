/*
 * Herbar CD-ROM version 2
 *
 * TaxTreeCellRenderer.java
 *
 * Created on 14. Mai 2002, 16:53
 * Created by Dani
 */
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.util.TaxonTreeNode;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Category;


/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Category cat = Category.getInstance(TaxTreeCellRenderer.class);

    /**
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(JTree,Object,boolean,boolean,boolean,
     *int,boolean)
     */
    public Component getTreeCellRendererComponent(JTree jt, Object objValue, boolean bSelected, boolean bExpanded,
                                                  boolean bLeaf, int iRow, boolean bHasFocus) {

        super.getTreeCellRendererComponent(jt, objValue, bSelected, bExpanded, bLeaf, iRow, bHasFocus);
        Taxon taxon = null;
        if (objValue instanceof TaxonTreeNode) {
            taxon = ((TaxonTreeNode) objValue).getTaxon();
            setText(taxon.toString());
            Level level = taxon.getLevel();
            setIcon(ImageLocator.getIcon("icon" + (level == null ? "" : level.getName()) + ".gif"));
        }
        else if (objValue instanceof DefaultMutableTreeNode) {
            setText("");
        }
        else {
            cat.fatal("Node " + objValue.getClass().getName() + " not of expected type TaxonTreeNode");
        }
        setBackgroundNonSelectionColor(jt.getBackground());
        revalidate();
        repaint();
        return this;
    }
}
