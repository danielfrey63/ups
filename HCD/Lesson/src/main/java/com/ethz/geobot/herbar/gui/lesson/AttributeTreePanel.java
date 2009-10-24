/*
 * Herbar CD-ROM version 2
 *
 * AbstractTreePanel.java
 *
 * Created on Feb 13, 2003 2:37:09 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.SimpleTransientGraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import com.ethz.geobot.herbar.gui.VirtualGraphTreeFactory;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Category;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public abstract class AttributeTreePanel extends JPanel {

    private final static Category CAT = Category.getInstance(AttributeTreePanel.class);

    private VirtualGraphTreeNodeFilter filter;
    private JTree jt;
    private Level stopperLevel;
    private String rootNodeName;
    private Level rootLevel;
    private TreeExpander expander;

    AttributeTreePanel(HerbarModel hModel, Level stopper, String rootNodeName) {
        this.stopperLevel = stopper;
        this.rootNodeName = rootNodeName;
        this.rootLevel = hModel.getRootLevel();
        this.filter = registerFilter();

        setBorder(new EmptyBorder(2, 2, 2, 2));

        jt = VirtualGraphTreeFactory.getVirtualTree(filter);
        expander = new TreeExpander(jt, 2);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(jt), BorderLayout.CENTER);
    }

    /**
     * Use this method to instantiate the correct filter set. This method is called from within the super constructor.
     *
     * @return the filter used for displaying the tree.
     */
    public abstract VirtualGraphTreeNodeFilter registerFilter();

    public void setTaxonFocus(Taxon taxon) {
        GraphNode vRoot = new SimpleTransientGraphNode();
        vRoot.setName(rootNodeName);
        Level level = taxon.getLevel();
        if (stopperLevel == null || (stopperLevel != null && stopperLevel.isHigher(level))) {
            while (level != null && stopperLevel.isHigher(level)) {
                vRoot.addChild(0, taxon.getAsGraphNode());
                taxon = taxon.getParentTaxon();
                level = taxon.getLevel();
            }
        }
        else {
            vRoot.addChild(taxon.getAsGraphNode());
        }
        VirtualGraphTreeFactory.updateModel(jt, filter, vRoot);
    }
}

