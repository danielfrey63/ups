/*
 * Herbar CD-ROM version 2
 *
 * ResultPanel.java
 *
 * Created on Feb 13, 2003 2:33:36 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * Collects results and displays them in a tree, together with the state of the overall result.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class ResultPanel extends JPanel {

    private MutableTreeNode root;
    private JTree tree;
    private DetailResultModel resultModel;

    public ResultPanel(DetailResultModel resultModel) {
        this.resultModel = resultModel;
        tree = new JTree();
        tree.setCellRenderer(new ResultPanelTreeCellRenderer());
        tree.setShowsRootHandles(true);
        setLayout(new BorderLayout());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        initResult();
    }

    public void setTaxFocus(Taxon focus) {
        initResult();
    }

    public void addGuess(DefaultMutableTreeNode guess) {
        // Add guess to gueses
        resultModel.addGuess((GraphNode) guess.getUserObject());
        // Add to tree and update tree
        root.insert(guess, root.getChildCount());
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload();
    }

    /**
     * Are all answers given?
     *
     * @return wether all anwers are give
     */
    public boolean isComplete() {
        return resultModel.isComplete();
    }

    /**
     * Set an appropriate text to the root node, indicating the number of answers to search for. The tree model will be
     * reinitialized by this method.
     */
    public void initResult() {
        GraphNodeList answers = resultModel.getAnswers();
        int n = answers == null ? 0 : answers.size();
        root = new DefaultMutableTreeNode(n == 1 ? "Gesucht wird 1 Merkmal" : "Gesucht werden " + n + " Merkmale");
        GraphNodeList guesses = resultModel.getGuesses();
        GraphNodeList guessesTexts = guesses == null ? new GraphNodeList() : guesses;
        for (int i = 0; i < guessesTexts.size(); i++) {
            root.insert(new DefaultMutableTreeNode(guessesTexts.get(i)), root.getChildCount());
        }
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
    }

    class ResultPanelTreeCellRenderer extends DefaultTreeCellRenderer {

        private ImageIcon ICON_CORRECT;
        private ImageIcon ICON_NEAR;
        private ImageIcon ICON_WRONG;
        private ImageIcon ICON_VALUE;
        private ImageIcon ICON_COMPLETE;
        private ImageIcon ICON_INCOMPLETE;

        public ResultPanelTreeCellRenderer() {
            super();
            ICON_CORRECT = ImageLocator.getIcon("abfrageRichtig.gif");
            ICON_NEAR = ImageLocator.getIcon("abfrageFast.gif");
            ICON_WRONG = ImageLocator.getIcon("abfrageFalsch.gif");
            ICON_VALUE = ImageLocator.getIcon("iconValue.gif");
            ICON_COMPLETE = ImageLocator.getIcon("abfrageVollstaendig.gif");
            ICON_INCOMPLETE = ImageLocator.getIcon("abfrageUnvollstaendig.gif");
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (row == 0) {
                if (!(resultModel instanceof MemorizingDetailResultModel)) {
                    if (resultModel.isComplete()) {
                        setIcon(ICON_COMPLETE);
                    }
                    else {
                        setIcon(ICON_INCOMPLETE);
                    }
                }
                else {
                    setIcon(ICON_INCOMPLETE);
                }
                setFont(getFont().deriveFont(Font.PLAIN));
            }
            else if (leaf) {
                TreeModel model = tree.getModel();
                DefaultMutableTreeNode guess = (DefaultMutableTreeNode) model.getChild(model.getRoot(), row - 1);
                int correctness = resultModel.getCorrectness((GraphNode) guess.getUserObject());
                // Display level of correctness except for exam
                if (!(resultModel instanceof MemorizingDetailResultModel)) {
                    if ((correctness & MemorizingDetailResultModel.CORRECT) == MemorizingDetailResultModel.CORRECT) {
                        setIcon(ICON_CORRECT);
                    }
                    else if ((correctness & MemorizingDetailResultModel.NEARBY) == MemorizingDetailResultModel.NEARBY) {
                        setIcon(ICON_NEAR);
                    }
                    else {
                        setIcon(ICON_WRONG);
                    }
                }
                else {
                    setIcon(ICON_VALUE);
                }
            }
            setBackgroundNonSelectionColor(tree.getBackground());
            return this;
        }
    }
}