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
 * Copyright x-matrix Switzerland (c) 2003
 *
 * VirtualGraphTreeFactory.java
 *
 * Created on Jan 27, 2003 11:32:41 AM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.component.tree.SearchableTree;
import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.SimpleTransientGraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeModel;
import com.ethz.geobot.herbar.gui.util.GraphNodeTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 * Constructs new <code>JTree</code>s with a {@link VirtualGraphTreeNodeModel}.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class VirtualGraphTreeFactory
{
    public static JTree getVirtualTree( final GraphNode root, final VirtualGraphTreeNodeFilter filter )
    {
        final GraphModel m = AbsGraphModel.getModel();
        final VirtualGraphTreeNodeModel model = new VirtualGraphTreeNodeModel( filter, m, root );
        final VirtualGraphTreeNode treeRoot = VirtualGraphTreeNode.getGraphNode( root, model, filter );
        final GraphTreeNode rootNode = GraphTreeNode.getTreeNode( treeRoot );
        final JTree jt = new SearchableTree( rootNode );
        jt.setRootVisible( false );
        jt.setCellRenderer( new GraphNodeTreeCellRenderer() );
        return jt;
    }

    public static JTree getVirtualTree( final VirtualGraphTreeNodeFilter filter )
    {
        return getVirtualTree( new SimpleTransientGraphNode(), filter );
    }

    /**
     * Set to the given tree a new model based on the new root and filter.
     *
     * @param jt      the tree the model is changed for
     * @param filter  the new filter used for the new model
     * @param newRoot the new node used as root
     */
    public static void updateModel( final JTree jt, final VirtualGraphTreeNodeFilter filter, final GraphNode newRoot )
    {
        final VirtualGraphTreeNodeModel model = new VirtualGraphTreeNodeModel( filter, AbsGraphModel.getModel(), newRoot );
        final VirtualGraphTreeNode treeRoot = VirtualGraphTreeNode.getGraphNode( newRoot, model, filter );
        jt.setModel( new DefaultTreeModel( GraphTreeNode.getTreeNode( treeRoot ) ) );
    }
}
