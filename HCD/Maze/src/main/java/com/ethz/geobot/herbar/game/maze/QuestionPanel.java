/*
 * Herbar CD-ROM version 2
 *
 * Flower.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.PictureConverter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.Question;
import com.ethz.geobot.herbar.gui.VirtualGraphTreeFactory;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Category;

/**
 * questionpanel modal.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class QuestionPanel extends JDialog implements Question {

    private static final Category CAT = Category.getInstance(QuestionPanel.class);

    private JLabel taxLabel;
    private HerbarModel model;
    private JLabel pict;
    private PictureTheme[] theme;
    private JButton okButton;
    private JPanel okPanel;

    protected Taxon actualTaxon;
    protected JTree tree;
    protected CountScore countScore;
    protected QuestionModel questionModel;
    private VirtualGraphTreeNodeFilter treeFilter;
    private TreeExpander expander;


    /**
     * Constructor
     *
     * @param model     instance of the herbarmodel
     * @param countScor instance of CountScore
     */
    public QuestionPanel(Frame frame, HerbarModel model, CountScore countScor) {
        super(frame);

        this.countScore = countScor;
        this.model = model;

        questionModel = new QuestionModel(countScore, this.model);

        Border borderEmpty = BorderFactory.createEmptyBorder(6, 6, 6, 6);
        Border borderLine = BorderFactory.createLineBorder(new Color(170, 180, 180), 1);
        CompoundBorder cb = new CompoundBorder(borderEmpty, borderLine);

        // top
        JTextPane frage = new JTextPane();
        Font font = UIManager.getFont("Label.font");
        frage.setContentType("text/html");
        frage.setText(Strings.getString(MazePanel.class, "MAZE.FRAGE", font.getName()));
        frage.setBackground(UIManager.getColor("Panel.background"));
        frage.setEditable(false);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontSize(attributes, font.getSize());
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = frage.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), attributes, false);

        taxLabel = new JLabel(" ");
        taxLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel frageAntwort = new JPanel(new BorderLayout());
        frageAntwort.setBorder(cb);
        frageAntwort.add(frage, BorderLayout.CENTER);
        frageAntwort.add(taxLabel, BorderLayout.SOUTH);

        // mid
        pict = new JLabel(" ");
        pict.setHorizontalAlignment(JLabel.CENTER);
        pict.setPreferredSize(new Dimension(370, 370));
        pict.setBorder(cb);

        theme = this.model.getPictureThemes();

        // bottom
        treeFilter = VirtualGraphTreeNodeFilter.getFilter(new Class[]{Taxon.class, MorText.class, MorValue.class, MorAttribute.class, MorSubject.class,
                MorAttribute.class, MorValue.class, MorText.class},
                new int[][]{{0, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 1, 1},
                        {0, 0, 1, 2}, {0, 0, 0, 2}, {1, 0, 0, 2}});
        tree = VirtualGraphTreeFactory.getVirtualTree(model.getRootTaxon().getAsGraphNode(), treeFilter);
        tree.setShowsRootHandles(true);
        expander = new TreeExpander(tree, 2);

        JScrollPane treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(200, 200));

        okButton = createOkButton();
        getRootPane().setDefaultButton(okButton);

        okPanel = new JPanel(new FlowLayout());
        okPanel.add(okButton, null);
        okPanel.setBorder(borderEmpty);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setBorder(cb);
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(treePane, BorderLayout.CENTER);
        lowerPanel.add(okPanel, BorderLayout.SOUTH);

        // build
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(frageAntwort, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(pict), BorderLayout.CENTER);
        contentPane.add(lowerPanel, BorderLayout.SOUTH);

        tree.requestFocus();
        setSize(500, 800);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    TreeNode node = (TreeNode) path.getLastPathComponent();
                    okButton.setEnabled(node.isLeaf());
                }
                else {
                    okButton.setEnabled(false);
                }
            }
        });
    }

    private JButton createOkButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Double check whether all selected values are correct
                // and that there are selected values at all
                TreePath selectionPath = tree.getSelectionPath();
                if (selectionPath != null) {
                    GraphTreeNode lastPathComponent = (GraphTreeNode) selectionPath.getLastPathComponent();
                    GraphNode morText = lastPathComponent.getDependent();
                    GraphNodeList correctTexts = actualTaxon.getAsGraphNode().getChildren(MorText.class);
                    if (correctTexts.contains(morText)) {
                        countScore.addRightScore(1);
                        questionModel.setRightAnswers(actualTaxon);
                    }
                    else {
                        countScore.addWrongScore(1);
                        questionModel.setWrongAnswers(actualTaxon);
                        // collect attributes of the guess text
                        GraphNodeList values = morText.getParents(MorValue.class);
                        GraphNodeList attributes = new GraphNodeList();
                        for (int i = 0; i < values.size(); i++) {
                            attributes.addAll(values.get(i).getParents(MorAttribute.class));
                        }
                        // collect correct text for attributes of the text guess
                        GraphNode attribute = attributes.get(0);
                        GraphNodeList allValues = attribute.getChildren(MorValue.class);
                        GraphNode correctText = null;
                        for (int i = 0; correctText == null && i < allValues.size(); i++) {
                            GraphNodeList allTexts = allValues.get(i).getChildren(MorText.class);
                            for (int j = 0; correctText == null && j < allTexts.size(); j++) {
                                if (correctTexts.contains(allTexts.get(j))) {
                                    correctText = allTexts.get(j);
                                }
                            }
                        }
                        String prompt = (Strings.getString(MazePanel.class, "MAZE.OPTION.CORRECT"));
                        prompt += " " + correctText;
                        String title = Strings.getString(MazePanel.class, "MAZE.OPTION.TITLE");
                        String[] button = new String[]{Strings.getString("BUTTON.OK.TEXT")};
                        int m = JOptionPane.INFORMATION_MESSAGE;
                        JOptionPane.showOptionDialog(QuestionPanel.this, prompt, title, 0, m, null, button, button[ 0 ]);
                    }
                    tree.clearSelection();
                }
                dispose();
            }
        };
        JButton button = ComponentFactory.createButton(MazePanel.class, "MAZE.OK", action);
        button.setFocusable(true);
        button.setEnabled(false);
        return button;
    }

    /**
     * initializes the data for the questionpool
     */
    public void init() {
        questionModel.init();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        WindowListener[] listeners = this.getWindowListeners();
        for (int i = 0; i < listeners.length; i++) {
            this.removeWindowListener(listeners[ i ]);
        }
    }

    /**
     * @see com.ethz.geobot.herbar.game.util.Question#firstQuestion()
     */
    public void firstQuestion() {
    }

    /**
     * @see com.ethz.geobot.herbar.game.util.Question#lastQuestion()
     */
    public void lastQuestion() {
    }

    /**
     * @see com.ethz.geobot.herbar.game.util.Question#nextQuestion()
     */
    public void nextQuestion() {
        actualTaxon = questionModel.getTaxon();
        VirtualGraphTreeFactory.updateModel(tree, treeFilter, actualTaxon.getAsGraphNode());
        ImageIcon plantPict = null;
        taxLabel.setText(actualTaxon.getName());
        List pictureNames = new ArrayList();
        for (int i = 0; i < theme.length; i++) {
            if (theme[ i ].getName().equals("Herbar") | theme[ i ].getName().equals("Portrait")) {
                CommentedPicture[] picts = actualTaxon.getCommentedPictures(theme[ i ]);
                if (picts != null) {
                    for (int ia = 0; ia < picts.length; ia++) {
                        pictureNames.add(picts[ ia ].getPicture().getName());
                    }
                }
            }
        }
        // Workaround as long as not all taxa have traits
        if (((TreeNode) tree.getModel().getRoot()).getChildCount() == 0) {
            okButton.setEnabled(true);
        }
        if (pictureNames.size() > 0) {
            int randPict = (int) (Math.random() * pictureNames.size());
            String name = ((String) pictureNames.get(randPict));
            plantPict = ImageLocator.getPicture(name);
            Dimension plantPictDim = new Dimension(plantPict.getIconWidth(), plantPict.getIconHeight());
            Dimension shouldDim = new Dimension(370, 370);
            Dimension newDim = PictureConverter.getFittingDimension(shouldDim, plantPictDim);
            Image plantImage = plantPict.getImage().getScaledInstance(newDim.width, newDim.height, Image.SCALE_SMOOTH);
            tree.clearSelection();
            pict.removeAll();
            pict.setIcon(new ImageIcon(plantImage));
            pict.setText("");
            repaint();
        }
        // Workaround as long as not all taxa have pictures
        else {
            String text = "Picture not available for " + actualTaxon;
            pict.setIcon(null);
            pict.setText(text);
            CAT.error(text);
        }
        getRootPane().setDefaultButton(okButton);
    }

}
