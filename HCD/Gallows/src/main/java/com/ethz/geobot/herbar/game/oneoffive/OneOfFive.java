/*
 * Herbar CD-ROM version 2
 *
 * OneOfFive.java
 *
 * Created on 11. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.component.ComponentFactory;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.ScoreListener;
import com.ethz.geobot.herbar.modeapi.ModeActivation;
import com.ethz.geobot.herbar.model.HerbarModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.log4j.Category;

/**
 * mainclass of oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class OneOfFive extends JPanel implements ModeActivation, ScoreListener {

    private static final Category CAT;

    private Galgen galgi = new Galgen();
    private JButton buttonMischen;
    private HerbarModel model;
    private OneOfFiveQuestionPanel questionPanel;
    private CountScore countScore;
    private StartPanel startPanel = new StartPanel();
    private JPanel naviPanel = new JPanel();
    private CountDown countDown;
    private int gameSize;

    /**
     * Constructor for the OneOfFive object, lilo
     *
     * @param mode Herbar model
     */
    public OneOfFive(OneOfFiveMode mode) {
        try {
            this.model = mode.getHerbarContext().getDataModel();
            countScore = new CountScore();
            countScore.init(0, 0, gameSize);
            countScore.addScoreListener(this);
            buttonMischen = createButtonStart();
            questionPanel = new OneOfFiveQuestionPanel(model, countScore);
            countDown = new CountDown();
            //   countDown = new CountDown( countScore.getMaxScore() );
            JPanel titlePanel = new JPanel();
            JLabel title = new JLabel();
            ImageIcon titleHangMan = ImageLocator.getIcon("titleHangMan.gif");
            title.setText(Strings.getString(OneOfFive.class, "ONEOFFIVE.TITLE"));
            title.setHorizontalAlignment(SwingConstants.LEFT);
            title.setIcon(titleHangMan);
            titlePanel.setLayout(new BorderLayout());
            titlePanel.add(title, BorderLayout.NORTH);

            gameSize = Integer.parseInt(Strings.getString(OneOfFive.class, "ONEOFFIVE.ANZAHLFRAGEN"));

            //   titlePanel.add( countDown, BorderLayout.SOUTH );
            titlePanel.add(buttonMischen, BorderLayout.EAST);

            naviPanel.setLayout(new BorderLayout());
            galgi.setSize(750, 470);
            naviPanel.add(countDown, BorderLayout.NORTH);
            naviPanel.add(startPanel, BorderLayout.CENTER);

            JPanel centerPanel = new JPanel();
            centerPanel.setPreferredSize(new Dimension(750, 620));
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(titlePanel, BorderLayout.NORTH);
            centerPanel.add(questionPanel, BorderLayout.CENTER);
            centerPanel.add(naviPanel, BorderLayout.SOUTH);

            this.setLayout(new GridBagLayout());
            this.add(centerPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            CAT.fatal("OneOfFive(...)", e);
            throw e;
        }
        catch (Error e) {
            CAT.error("OneOfFive(...)", e);
            throw e;
        }
    }

    private JButton createButtonStart() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mischenActionPerformed();
            }
        };
        JButton start = ComponentFactory.createButton(OneOfFive.class, "ONEOFFIVE.START", action);
        return start;
    }

    /**
     * @see com.ethz.geobot.herbar.game.util.ScoreListener#scoreChanged()
     */
    public void scoreChanged() {
        int rights = countScore.getRightScore();
        int wrongs = countScore.getWrongScore();
        int totals = countScore.getMaxScore();
        galgi.setGalgenBauStep(wrongs);
        countDown.actualizeScoreBar(rights, totals);
        if (rights == totals || wrongs == Galgen.TOTAL_STEPS) {
            questionPanel.reset();
            buttonMischen.setEnabled(true);
        }
        if (wrongs < Galgen.TOTAL_STEPS && rights < totals && !(wrongs == 0 && rights == 0)) {
            questionPanel.nextQuestion();
        }
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#deactivate()
     */
    public void deactivate() {
        galgi.stopThread();
        naviPanel.remove(galgi);
        naviPanel.add(startPanel, BorderLayout.CENTER);
        countDown.setShowScoreBar(false);
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#activate()
     */
    public void activate() {
        buttonMischen.setEnabled(true);
        buttonMischen.requestFocus();
        getRootPane().setDefaultButton(buttonMischen);
        countScore.init(0, 0, gameSize);
        questionPanel.reset();
        galgi.initThread();
    }

    /**
     * @see com.ethz.geobot.herbar.modeapi.ModeActivation#queryDeactivate()
     */
    public boolean queryDeactivate() {
        if (!buttonMischen.isEnabled()) {
            String paneTitle = (Strings.getString(OneOfFive.class, "ONEOFFIVE.OPTION.TITLE"));
            String paneQuest = (Strings.getString(OneOfFive.class, "ONEOFFIVE.QUEST"));
            String[] options = {Strings.getString(OneOfFive.class, "ONEOFFIVE.OPTION.YES"),
                    Strings.getString(OneOfFive.class, "ONEOFFIVE.OPTION.NO")};
            int ret = JOptionPane.showOptionDialog(this, paneQuest, paneTitle, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[ 0 ]);
            return ret == JOptionPane.YES_OPTION;
        }
        return true;
    }

    private void mischenActionPerformed() {
        countScore.init(0, 0, gameSize);
        questionPanel.restart();
        questionPanel.nextQuestion();
        buttonMischen.setEnabled(false);
        naviPanel.remove(startPanel);
        naviPanel.add(galgi, BorderLayout.CENTER);
        countDown.setShowScoreBar(true);
    }

    static {
        LogUtils.init();
        Strings.addResourceBundle(OneOfFive.class,
                ResourceBundle.getBundle("com.ethz.geobot.herbar.game.oneoffive.OneOfFive"));
        CAT = Category.getInstance(OneOfFive.class);
    }
}
