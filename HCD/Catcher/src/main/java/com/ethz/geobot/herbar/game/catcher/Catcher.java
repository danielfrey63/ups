/*
 * Herbar CD-ROM version 2
 *
 * Catcher.java
 *
 * Created on 01. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.ScoreListener;
import com.ethz.geobot.herbar.gui.CorrectnessChecker;
import com.ethz.geobot.herbar.modeapi.ModeActivation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Category;

/**
 * @author fre
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

/**
 * mainclass of game catcher.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class Catcher extends JPanel
        implements ScoreListener, KeyListener, ModeActivation {

    private static final Category CAT;
    private Catcher catcher = this;
    private JButton gameButton;
    private FlyingBee bee;
    private FlyingFlower[] ff;
    private Carpet carpet;
    private EnergyBar eb;
    private CountScore countScore;
    private ImageIcon titleHangMan = ImageLocator.getIcon("titleCatcher.gif");
    private QuestionWindow questionWindow;
    private int diff = 10;
    private StartPanel startPanel;
    private JPanel naviPanel = new JPanel();
    private CatcherQuestionModel catcherQuestionModel;
    private int occupied = 0;


    /**
     * Constructor for the Catcher object, lilo
     *
     * @param mode herbarmodel
     */
    public Catcher(CatcherMode mode) {
        try {
            Color green = new Color(20, 70, 20);
            startPanel = new StartPanel(green);
            gameButton = new JButton(Strings.getString(Catcher.class, "CATCHER.START"));
            JPanel centerPanel = new JPanel();
            JPanel titlePanel = new JPanel();
            JLabel description = new JLabel(Strings.getString(Catcher.class, "CATCHER.DESCRIPTION"));
            ff = new FlyingFlower[Carpet.MAX];
            for (int i = 0; i < Carpet.MAX; i++) {
                ff[ i ] = new FlyingFlower();
            }
            FlyingGras[] fg = new FlyingGras[Carpet.MAXGRAS];
            for (int i = 0; i < Carpet.MAXGRAS; i++) {
                fg[ i ] = new FlyingGras();
            }
            FlyingBird[] fb = new FlyingBird[Carpet.MAXBIRD];
            for (int i = 0; i < Carpet.MAXBIRD; i++) {
                fb[ i ] = new FlyingBird();
            }
            bee = new FlyingBee(green);
            FlyingEarth fe = new FlyingEarth();
            countScore = new CountScore();
            countScore.init(100, 0, 0);
            countScore.addScoreListener(this);
            eb = new EnergyBar();
            carpet = new Carpet(this, bee, ff, fg, fb, fe, eb, countScore);
            JFrame frame = mode.getHerbarContext().getHerbarGUIManager().getParentFrame();
            questionWindow = new QuestionWindow(frame, mode.getHerbarContext().getDataModel());
            catcherQuestionModel = new CatcherQuestionModel(mode.getHerbarContext().getDataModel());

            // questionWindow = new QuestionWindow( mode.getConfig().
            // getFilteredModel("myList"), countScore );
            this.addKeyListener(this);
            carpet.addKeyListener(this);
            centerPanel.setPreferredSize(new Dimension(750, 620));
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(titlePanel, BorderLayout.NORTH);

            titlePanel.setLayout(new BorderLayout());
            titlePanel.add(description, BorderLayout.NORTH);
            titlePanel.add(gameButton, BorderLayout.EAST);
            description.setIcon(titleHangMan);

            centerPanel.add(naviPanel, BorderLayout.SOUTH);

            naviPanel.setLayout(new BorderLayout());
            naviPanel.add(startPanel, BorderLayout.CENTER);

            carpet.setSize(750, 470);
            this.setLayout(new GridBagLayout());
            this.add(centerPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            // ActionListener für "start"- Button; Carpet starts falling
            gameButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonStartActionPerformed(e);
                }
            });

        }
        catch (RuntimeException e) {
            e.printStackTrace();
            CAT.fatal("Catcher(...)", e);
            throw e;
        }
        catch (Error e) {
            CAT.error("Catcher(...)", e);
            throw e;
        }
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#queryDeactivate()
     */
    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#queryDeactivate()
     */
    public boolean queryDeactivate() {
        if (!gameButton.getText().equals(Strings.getString(Catcher.class, "CATCHER.START"))) {
            carpet.setFlyingBird(false);
            carpet.setFlyingCarpet(false);
            gameButton.setText(Strings.getString(Catcher.class, "CATCHER.WEITER"));
            String paneTitle = (Strings.getString(Catcher.class, "CATCHER.TITLE"));
            String paneQuest = (Strings.getString(Catcher.class, "CATCHER.QUEST"));
            String[] options =
                    {
                            Strings.getString(Catcher.class, "CATCHER.OPTION.YES"),
                            Strings.getString(Catcher.class, "CATCHER.OPTION.NO")};
            int ret = JOptionPane.showOptionDialog(this, paneQuest, paneTitle, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[ 0 ]);
            return ret == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#deactivate()
     */
    public void deactivate() {
        naviPanel.remove(carpet);
        naviPanel.add(startPanel, BorderLayout.CENTER);
        carpet.stop();
        bee.stopAllSounds();
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#activate()
     */
    public void activate() {
        gameButton.setText(Strings.getString(Catcher.class, "CATCHER.START"));
        gameButton.setEnabled(true);
        getRootPane().setDefaultButton(gameButton);
        gameButton.requestFocus();
        catcherQuestionModel.initializeDataPool();
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        if (occupied == 0) {
            if (!gameButton.getText().equals(Strings.getString(Catcher.class, "CATCHER.WEITER"))) {
                try {
                    if (e.getKeyCode() == 37) {
                        bee.moveLeft(diff);
                    }
                    if (e.getKeyCode() == 39) {
                        bee.moveRight(diff);
                    }
                    if (e.getKeyCode() == 38) {
                        bee.moveUp(diff);
                    }
                    if (e.getKeyCode() == 40) {
                        bee.moveDown(diff);
                    }
                    if (e.getKeyCode() == 32) {
                        for (int i = 0; i < Carpet.MAX; i++) {
                            if (ff[ i ].getYPosFlower() < bee.getYPosBee() + 15
                                    && ff[ i ].getYPosFlower() > bee.getYPosBee() - 15) {
                                if (ff[ i ].getXPosFlower() > bee.getXPosBee() - 15
                                        && ff[ i ].getXPosFlower() < bee.getXPosBee() + 15) {
                                    occupied = 1;
                                    carpet.setFlyingCarpet(false);
                                    carpet.setFlyingBird(false);
                                    QuestionDataUnit questionDataUnit = catcherQuestionModel.getQuestion();
                                    CorrectnessChecker.Correctness correctness = questionWindow.showQuestion(questionDataUnit);
                                    evaluateAnswer(correctness);
                                }
                            }
                        }
                    }
                }
                catch (Exception x) {
                    CAT.fatal("keyPressed()", x);
                }
            }
        }
    }

    private void evaluateAnswer(CorrectnessChecker.Correctness correctness) {
        if (correctness == CorrectnessChecker.IS_TRUE) {
            catcherQuestionModel.setRightAnswers();
            countScore.addRightScore(5);
        }
        if (correctness == CorrectnessChecker.IS_NEARLY_TRUE) {
            countScore.addRightScore(1);
        }
        if (correctness == CorrectnessChecker.IS_FALSE) {
            countScore.addWrongScore(5);
        }
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @see com.ethz.geobot.herbar.game.util.ScoreListener#scoreChanged()
     */
    public void scoreChanged() {
        int count = countScore.getTotalScore();
        eb.setScore(count);
        carpet.setFlyingBird(true);
        carpet.setFlyingCarpet(true);
        occupied = 0;
        try {
            if (count <= 0) {
                carpet.setFlyingCarpet(false);
                gameButton.setText(Strings.getString(Catcher.class, "CATCHER.START"));
                gameButton.setEnabled(true);
                carpet.setRotatingBee(true);
            }
            if (catcherQuestionModel.isFinished() == true) {
                gameButton.setText(Strings.getString(Catcher.class, "CATCHER.START"));
                gameButton.setEnabled(true);
                carpet.setFlyingCarpet(false);
                if (catcherQuestionModel.rightAnswersComplete() == true) {
                    carpet.setWiningBee(true);
                    gameButton.setText(Strings.getString(Catcher.class, "CATCHER.START"));
                }
                if (catcherQuestionModel.rightAnswersComplete() == false) {
                    carpet.setRotatingBee(true);
                    gameButton.setText(Strings.getString(Catcher.class, "CATCHER.START"));
                }
            }
        }
        catch (Exception x) {
            CAT.fatal("scoreChanged()", x);
        }
    }

    protected void buttonStartActionPerformed(ActionEvent e) {
        String name = gameButton.getText();
        if (name.equals(Strings.getString(Catcher.class, "CATCHER.START"))) {
            catcherQuestionModel.reset();
            carpet.setRotatingBee(false);
            carpet.setWiningBee(false);
            carpet.init();
            carpet.setFlyingCarpet(true);
            carpet.setFlyingBird(true);
            bee.init();
            eb.init();
            countScore.init(100, 0, 0);
            gameButton.setText(Strings.getString(Catcher.class, "CATCHER.PAUSE"));
            bee.startSoundLoop();
            catcher.setFocusable(true);
            catcher.requestFocus();
            naviPanel.remove(startPanel);
            naviPanel.add(carpet, BorderLayout.CENTER);
        }
        else if (name.equals(Strings.getString(Catcher.class, "CATCHER.PAUSE"))) {
            carpet.setFlyingCarpet(false);
            carpet.setFlyingBird(false);
            gameButton.setText(Strings.getString(Catcher.class, "CATCHER.WEITER"));
            //  catcher.requestFocus();
        }
        else if (name.equals(Strings.getString(Catcher.class, "CATCHER.WEITER"))) {
            carpet.setFlyingCarpet(true);
            carpet.setFlyingBird(true);
            gameButton.setText(Strings.getString(Catcher.class, "CATCHER.PAUSE"));
            catcher.requestFocus();
        }
    }


    static {
        LogUtils.init();
        Strings.addResourceBundle(Catcher.class, ResourceBundle.getBundle("com.ethz.geobot.herbar.game.catcher.Catcher"));
        CAT = Category.getInstance(Catcher.class);
    }
}
