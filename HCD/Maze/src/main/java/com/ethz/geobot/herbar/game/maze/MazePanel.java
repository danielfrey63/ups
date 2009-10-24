/*
 * Herbar CD-ROM version 2
 *
 * MazePanel.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.Dialogs;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.ScoreListener;
import com.ethz.geobot.herbar.modeapi.ModeActivation;
import com.ethz.geobot.herbar.model.HerbarModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Category;

/**
 * mainpanel containing maze, buttons and panels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class MazePanel extends JPanel implements ScoreListener, ModeActivation {

    private static final Category CAT;
    private QuestionPanel questionPanel;
    private RandomMaze randomMaze;
    private CountScore countScore;
    private JButton start;
    private JButton solve;
    private int score = 0;
    private JLabel scoreLabel;
    private StartPanel startCanvas;
    private JPanel naviPanel;
    private ScoreBar scoreBar;


    /**
     * Constructor
     *
     * @param mode instance of the herbarmodel
     */
    public MazePanel(MazeMode mode) {
        try {
            HerbarModel model = mode.getHerbarContext().getDataModel();
            scoreLabel = new JLabel(Strings.getString(MazePanel.class, "MAZE.SCORELABEL", "" + score));
            start = createStartButton();
            Color wiese = new Color(82, 135, 43);
            startCanvas = new StartPanel(wiese);
            scoreBar = new ScoreBar(wiese);
            countScore = new CountScore();
            countScore.init(0, 0, 10);
            JFrame frame = mode.getHerbarContext().getHerbarGUIManager().getParentFrame();
            questionPanel = new QuestionPanel(frame, model, countScore);

            solve = createSolveButton();
            randomMaze = new RandomMaze(this, questionPanel, wiese, countScore);
            countScore.addScoreListener(this);
            randomMaze.setBackground(wiese);
            JPanel centerPanel = new JPanel();
            JPanel northPanel = new JPanel();
            JPanel titlePanel = new JPanel();
            naviPanel = new JPanel();
            JLabel description = new JLabel(Strings.getString(MazePanel.class, "MAZE.DESCRIPTION"));

            northPanel.setBackground(wiese);
            northPanel.setPreferredSize(new Dimension(0, 20));

            centerPanel.setPreferredSize(new Dimension(750, 620));
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(titlePanel, BorderLayout.NORTH);

            titlePanel.setLayout(new BorderLayout());
            titlePanel.add(description, BorderLayout.NORTH);
            JPanel startSolvePanel = new JPanel(new BorderLayout());
            startSolvePanel.add(start, BorderLayout.CENTER);
            startSolvePanel.add(solve, BorderLayout.EAST);
            titlePanel.add(startSolvePanel, BorderLayout.EAST);
            description.setIcon(ImageLocator.getIcon("titleLaby.gif"));

            centerPanel.add(naviPanel, BorderLayout.SOUTH);
            naviPanel.setLayout(new BorderLayout());
            naviPanel.add(northPanel, BorderLayout.NORTH);
            naviPanel.add(startCanvas, BorderLayout.CENTER);
            naviPanel.add(scoreBar, BorderLayout.WEST);

            this.setLayout(new GridBagLayout());
            this.add(centerPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                    , GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        }
        catch (RuntimeException x) {
            CAT.fatal("MazePanel()", x);
            throw x;
        }
        catch (Error x) {
            CAT.error("MazePanel()", x);
            throw x;
        }
    }

    private JButton createSolveButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSolveActionPerformed();
            }
        };
        JButton solve = ComponentFactory.createButton(MazePanel.class, "MAZE.SOLVE", action);
        solve.setEnabled(false);
        return solve;
    }

    private JButton createStartButton() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonStartActionPerformed();
            }
        };
        JButton start = ComponentFactory.createButton(MazePanel.class, "MAZE.START", action);
        return start;
    }

    /**
     * returns the actual score
     *
     * @return int
     */
    public int getScore() {
        return this.score;
    }


    /**
     * @see com.ethz.geobot.herbar.game.util.ScoreListener#scoreChanged()
     */
    public void scoreChanged() {
        scoreBar.actualizeScoreBar(countScore.getRightScore(), countScore.getWrongScore());
        score = countScore.getTotalScore();
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#deactivate()
     */
    public void deactivate() {
        naviPanel.remove(randomMaze);
        naviPanel.add(startCanvas, BorderLayout.CENTER);
        scoreBar.setShowScoreBar(false);
    }

    public void finished() {
        start.setEnabled(true);
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#activate()
     */
    public void activate() {
        start.setEnabled(true);
        getRootPane().setDefaultButton(start);
        start.requestFocus();
        solve.setEnabled(false);
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#queryDeactivate()
     */
    public boolean queryDeactivate() {
        if (!start.isEnabled()) {
            String paneTitle = Strings.getString(MazePanel.class, "MAZE.TITLE");
            String paneQuest = Strings.getString(MazePanel.class, "MAZE.QUEST");
            return (Dialogs.OK == Dialogs.showQuestionMessageOk(this, paneTitle, paneQuest));
        }
        return true;
    }


    private void buttonStartActionPerformed() {
        start.setEnabled(false);
        solve.setEnabled(true);
        questionPanel.init();
        countScore.init(0, 0, 10);
        scoreBar.init();
        randomMaze.generateTrail();
        score = 0;
        scoreLabel.setText(Strings.getString(MazePanel.class, "MAZE.SCORELABEL", "" + score));
        scoreBar.setShowScoreBar(true);
        naviPanel.remove(startCanvas);
        naviPanel.add(randomMaze, BorderLayout.CENTER);
        randomMaze.setFocusable(true);
        randomMaze.requestFocus();
        this.validate();
    }

    private void buttonSolveActionPerformed() {
        randomMaze.solvePath();
        randomMaze.setFocusable(false);
        solve.setEnabled(false);
        start.setEnabled(true);
    }

    static {
        LogUtils.init();
        Strings.addResourceBundle(MazePanel.class, ResourceBundle.getBundle
                ("com.ethz.geobot.herbar.game.maze.MazePanel"));
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        CAT = Category.getInstance(MazePanel.class);
    }
}
