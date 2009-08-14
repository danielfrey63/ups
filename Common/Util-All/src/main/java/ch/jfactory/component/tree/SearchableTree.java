/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * SearchableTree.java
 *
 * Created on 10. Juli 2002, 16:39
 * Created by Daniel Frey
 */
package ch.jfactory.component.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 * Simple JTree that takes a TreeNode and uses a DefaultTreeModel. This tree allows for searches by implementing the
 * #TreeFinder interface.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class SearchableTree extends JTree implements TreeFinder
{

    private static final Logger LOGGER = Logger.getLogger(SearchableTree.class);

    /**
     * Creates a new instance of TaxTreePanel
     *
     * @param root the root node to display
     */
    public SearchableTree(final TreeNode root)
    {
        super(root);
    }

    public SearchableTree(final TreeModel newModel)
    {
        super(newModel);
    }

    /**
     * replace the current TreeModel with a new one.
     *
     * @param root the root TreeNode used for the model
     */
    public void setRootTreeNode(final TreeNode root)
    {
        setModel(new DefaultTreeModel(root));
    }

    // Commented in interface
    public void setSelection(final TreePath tp)
    {
        LOGGER.debug("selecting " + tp);
        setSelectionPath(tp);
        TreeUtils.ensureVisibility(this, tp);
    }
}

// $Log: SearchableTree.java,v $
// Revision 1.3  2006/03/14 21:27:55  daniel_frey
// *** empty log message ***
//
// Revision 1.2  2005/11/17 11:54:58  daniel_frey
// no message
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.7  2003/03/09 14:44:26  daniel_frey
// - Enhancements in key handling
//
