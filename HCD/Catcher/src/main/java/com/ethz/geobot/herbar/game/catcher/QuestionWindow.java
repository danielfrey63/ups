/*
 * Herbar CD-ROM version 2
 *
 * QuestionWindow.java
 *
 * Created on 9. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.Dialogs;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.PictureConverter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.Question;
import com.ethz.geobot.herbar.gui.CorrectnessChecker;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import org.apache.log4j.Category;

/**
 * Window selects and shows the question of this game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class QuestionWindow extends JDialog implements Question {

    private static final Category CAT = Category.getInstance(Catcher.class);
    private ImageIcon plantPict;
    private JLabel pict = new JLabel();
    private JButton go;
    private JTextField answer = new JTextField();
    private Taxon tax;
    private Taxon ancestorTax;
    private PictureTheme[] theme;
    private JLabel frage = new JLabel();
    private Frame parentFrame;
    private HerbarModel model;
    private CorrectnessChecker.Correctness correctness;
    private final CorrectnessChecker correctnessChecker = new CorrectnessChecker();

    /**
     * Method QuestionWindow. Constructor
     *
     * @param model instance of the herbarmodel
     */
    public QuestionWindow(Frame frame, HerbarModel model) {
        super(frame);
        try {
            this.model = model;
            parentFrame = frame;
            init();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * prepares the optionpane to show the correct answer
     *
     * @param unique correct taxon
     * @param text   The new correctOptionPane value
     */
    private void setCorrectOptionPane(String text, Taxon unique) {
        Dialogs.showInfoMessage(this.getRootPane(), "Falsche Antwort", text + " " + unique.getName());
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
     * demand a questionObject of the datapool to generate the next question.
     */
    public CorrectnessChecker.Correctness showQuestion(QuestionDataUnit questionDataUnit) {
        tax = questionDataUnit.getTaxon();
        ancestorTax = questionDataUnit.getParentTaxon();
        // randomly select a pict of all picts of the selected taxon
        Vector pictureNames = new Vector();
        pictureNames.removeAllElements();
        for (int i = 0; i < theme.length; i++) {
            if (theme[ i ].getName().equals("Herbar") | theme[ i ].getName().equals("Portrait")) {
                CommentedPicture[] picts = tax.getCommentedPictures(theme[ i ]);
                if (picts != null) {
                    for (int ia = 0; ia < picts.length; ia++) {
                        pictureNames.add(picts[ ia ].getPicture().getName());
                    }
                }
            }
        }
        int randPict = (int) (Math.random() * pictureNames.size());
        if (pictureNames.size() < 1) {
            answer.setText("kein Bild vorhanden");
            //return showQuestion(questionDataUnit);
        }
        else {
            String name = ((String) pictureNames.elementAt(randPict));
            plantPict = ImageLocator.getPicture(name);
            Dimension plantPictDim = new Dimension(plantPict.getIconWidth(), plantPict.getIconHeight());
            Dimension shouldDim = new Dimension(370, 370);
            Dimension newDim = PictureConverter.getFittingDimension(shouldDim, plantPictDim);
            Image plantImage = plantPict.getImage().getScaledInstance(newDim.width, newDim.height, Image.SCALE_SMOOTH);
            answer.setEditable(true);
            answer.setText("");
            pict.setIcon(new ImageIcon(plantImage));
            this.setSize(500, 550);
            this.repaint();
            getRootPane().setDefaultButton(go);
            frage.setText(Strings.getString(Catcher.class, "CATCHER.FRAGE", ancestorTax.getLevel().getName()));
            CAT.debug("Taxon: " + tax.getName() + ", AncestorTaxon: " + ancestorTax.getName());
        }
        setVisible(true);
        return correctness;
    }

    public void nextQuestion() {
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        }
        super.processWindowEvent(e);
    }

    /**
     * initialisation of jwindow and components, containing the answer validation
     */
    private void init() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        WindowListener[] listeners = this.getWindowListeners();
        for (int i = 0; i < listeners.length; i++) {
            this.removeWindowListener(listeners[ i ]);
        }

        theme = model.getPictureThemes();
        Border borderEmpty = BorderFactory.createEmptyBorder(6, 6, 6, 6);
        Border borderLine = BorderFactory.createLineBorder(new Color(170, 180, 180), 1);
        CompoundBorder cb = new CompoundBorder(borderEmpty, borderLine);
        go = createButtonGo();
        setModal(true);
        setSize(500, 550);
        WindowUtils.centerOnComponent(this, parentFrame);


        answer.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        JPanel frageAntwort = new JPanel(new BorderLayout());
        JPanel answerFlow = new JPanel(new FlowLayout());
        answerFlow.add(answer, null);

        answer.setPreferredSize(new Dimension(200, 20));

        frage.setFont(UIManager.getFont("Menu.font"));
        answer.setHorizontalAlignment(JLabel.CENTER);
        frage.setHorizontalAlignment(JLabel.CENTER);
        pict.setHorizontalAlignment(JLabel.CENTER);

        frageAntwort.add(frage, BorderLayout.NORTH);
        frageAntwort.add(answerFlow, BorderLayout.CENTER);

        JPanel goFlow = new JPanel(new FlowLayout());
        goFlow.add(go, null);
        frageAntwort.setBorder(cb);
        pict.setBorder(cb);
        goFlow.setBorder(cb);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(frageAntwort, BorderLayout.NORTH);
        getContentPane().add(pict, BorderLayout.CENTER);
        getContentPane().add(goFlow, BorderLayout.SOUTH);

        answer.setFocusable(true);
    }

    private JButton createButtonGo() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                correctness = correctnessChecker.getCorrectness(ancestorTax, answer.getText());
                if (correctness == CorrectnessChecker.IS_NEARLY_TRUE) {
                    setCorrectOptionPane(Strings.getString(Catcher.class, "CATCHER.CORRECT.DISTANCE", "" +
                            correctness), ancestorTax);
                }
                else if (correctness == CorrectnessChecker.IS_FALSE) {
                    setCorrectOptionPane(Strings.getString(Catcher.class, "CATCHER.CORRECT.RIGHT"), ancestorTax);
                }
                setVisible(false);
            }
        };
        JButton button = ComponentFactory.createButton(Catcher.class, "CATCHER.GO", action);
        button.setFocusable(true);
        return button;
    }
}
