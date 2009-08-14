/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * TreeFinderPanel.java
 *
 * Created on 8. April 2002
 * Created by Daniel Frey
 */
package ch.jfactory.component.tree;

import ch.jfactory.action.ComponentFocusListener;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.DefaultTextField;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

/**
 * This class provides a panel that enables a serach in a tree. To link the panel with the tree, simply pass in a JTree
 * in the constructor. The panel will update the tree to display found items automatically.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2007/09/27 10:41:47 $
 */
public class TreeFinderPanel extends JPanel implements Observer
{

    private JLabel counter;

    private JTextField edit;

    private TreeFinderModel fm;

    private JButton next;

    private JButton prev;

    private JTree tree;

    private JPanel panel;

    private TreeFinder defaultTreeFinder;

    private TreeFinder treeFinder = defaultTreeFinder;

    /**
     * Constructor for the TreeFinderPanel object. This constructor takes two additional arguments for the icons placed
     * on the navigation buttons.
     *
     * @param tree the tree to find objects in
     */
    public TreeFinderPanel(final JTree tree)
    {
        setTreeFinder(new CenteringTreeFinder(tree));
        setTree(tree);
        init();
    }

    public void setTreeFinder(final TreeFinder finder)
    {
        this.treeFinder = finder;
    }

    /**
     * Description of the Method
     *
     * @param obs Description of the Parameter
     * @param obj Description of the Parameter
     */
    public void update(final Observable obs, final Object obj)
    {
        if (!(obs instanceof TreeFinderModel))
        {
            return;
        }
        final TreeFinderModel fm = (TreeFinderModel) obs;

        // Set the node in the tree and make sure it is visilbe.
        treeFinder.setSelection(fm.get());

        // Update button
        prev.setEnabled(fm.hasPrevious());
        next.setEnabled(fm.hasNext());

        // counter label
        counter.setText(fm.getIndex() + 1 + "/" + fm.getCount());
    }

    /** Description of the Method */
    private void init()
    {
        defaultTreeFinder = new CenteringTreeFinder(tree);

        ActionListener action;

        action = new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                fm.find(edit.getText());
                edit.selectAll();
            }
        };
        final JButton findButton = ComponentFactory.createButton("TREEFINDER.FIND", action);

        edit = new DefaultTextField();
        // this removes the (awt compatible) binding of enter key to actions
        // so that the default button is called on enter
        edit.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        // This adapter removes the unwanted replacement of the selected text
        // by a space.
        edit.addKeyListener(new KeyAdapter()
        {
            public void keyTyped(final KeyEvent e)
            {
                if (edit.getSelectedText() != null && edit.getSelectedText().equals(edit.getText()))
                {
                    if (e.getKeyChar() == KeyEvent.VK_SPACE)
                    {
                        e.consume();
                    }
                }
            }
        });
        edit.addFocusListener(new ComponentFocusListener(findButton));

        KeyStroke keyStroke;

        action = new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                fm.next();
            }
        };
        next = ComponentFactory.createButton("TREEFINDER.NEXT", action);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_MASK);
        ComponentFocusListener.registerComponentFocusListener(edit, next, keyStroke);

        action = new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                fm.previous();
            }
        };
        prev = ComponentFactory.createButton("TREEFINDER.PREV", action);
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_MASK);
        ComponentFocusListener.registerComponentFocusListener(edit, prev, keyStroke);

        // Make sure that there is at least place for 3 digits without having to ajust the component
        counter = new JLabel();
        counter.setText("000/000");
        counter.setPreferredSize(counter.getPreferredSize());
        counter.setText("0/0");
        counter.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new FormLayout(
                new ColumnSpec[]{
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));
        final CellConstraints cc = new CellConstraints();
        add(edit, cc.xy(1, 1));
        add(findButton, cc.xy(3, 1));
        add(prev, cc.xy(5, 1));
        add(counter, cc.xy(7, 1));
        add(next, cc.xy(9, 1));

        fm.notifyObservers();
    }

    /**
     * Change the tree of an existing finder.
     *
     * @param tree the new tree
     */
    public void setTree(final JTree tree)
    {
        if (fm != null)
        {
            fm.deleteObserver(this);
        }
        this.tree = tree;
        fm = new TreeFinderModel(this.tree.getModel());
        fm.addObserver(this);
    }

    /** Just scrolls to the found tree node, making sure that it is visible. */
    public class SimpleTreeFinder implements TreeFinder
    {
        private JTree tree;

        public SimpleTreeFinder(final JTree tree)
        {
            this.tree = tree;
        }

        public void setSelection(final TreePath tp)
        {
            tree.setSelectionPath(tp);
            tree.scrollPathToVisible(tp);
        }
    }
}

// $Log: TreeFinderPanel.java,v $
// Revision 1.3  2007/09/27 10:41:47  daniel_frey
// - Removed bug in SortableTableModel
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
// Revision 1.19  2004/03/04 23:39:28  daniel_frey
// - Build with News on Splash
//
// Revision 1.18  2003/05/25 21:38:47  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.17  2003/04/24 10:24:02  daniel_frey
// - Another try of incremental search
//
// Revision 1.16  2003/04/16 16:30:44  daniel_frey
// - Further version of search window
//
// Revision 1.15  2003/04/02 14:49:04  daniel_frey
// - Revised wizards
//
// Revision 1.14  2003/03/16 08:53:01  daniel_frey
// - Exam mode with first shot of results
// - UI rolled partly back
//
// Revision 1.13  2003/03/09 14:44:26  daniel_frey
// - Enhancements in key handling
//
// Revision 1.12  2003/02/27 15:28:39  dirk_hoffmann
// refactor ComponentFocusListener
//
// Revision 1.11  2003/02/09 12:01:38  daniel_frey
// - Externalized ComponentFocusListener
//
// Revision 1.10  2003/02/05 22:03:29  daniel_frey
// - Added method to change tree on the fly (setTree)
//
// Revision 1.9  2003/01/23 10:54:01  daniel_frey
// - Optimized imports
//
// Revision 1.8  2003/01/22 14:46:43  daniel_frey
// - FocusAdapter of edit field now does reassign original default button uppon exit of field.
//
// Revision 1.7  2003/01/22 12:08:39  daniel_frey
// - Corrected/Added javadoc
// - Added setBorder method to surround panel with gap
//
// Revision 1.6  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.5  2002/09/25 14:41:35  daniel_frey
// - Introduced dynamic relevance object model
// - Replaced roles with relevances  by class types for each comination
// - Removed some caching issues
//
// Revision 1.4  2002/09/20 14:01:28  Dani
// - Simplified interface tree-graph node
//
// Revision 1.3  2002/09/17 09:15:34  Dani
// - Entry now working
// - Better encapsulation of layers
//
// Revision 1.2  2002/08/05 12:38:55  Dani
// - Added escape comments for non-externalized strings
//
// Revision 1.1  2002/07/11 12:12:17  Dani
// Moved from com.ethz.geobot.herbar.gui.util to ch.xmatrix.gui.component.tree
//
// Revision 1.4  2002/07/10 15:07:24  Dani
// Adapted import to move of TreeFinder to xmatrix package
//
// Revision 1.3  2002/05/31 20:01:10  Dani
// Refactored tax tree components
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
