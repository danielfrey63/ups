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
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.game.util.Question;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mainclass of oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class OneOfFiveQuestionPanel extends JPanel implements Question
{
    private static final Logger LOG = LoggerFactory.getLogger( OneOfFiveQuestionPanel.class );

    private static final boolean INFO = LOG.isInfoEnabled();

    private static final int NUMBER_OF_TAXA = 4;

    private final JButton[] taxButton = new JButton[NUMBER_OF_TAXA + 1];

    private final HerbarModel model;

    private QuestionDataUnit unit;

    /**
     * Contains the five taxa for the next question
     */
    private List taxItem = new ArrayList();

    /**
     * Position of the wrong Taxon within the vector taxItem
     */
    private int randomAdd;

    private final OneOfFiveQuestionModel questionModel;

    private final CountScore countScore;

    /**
     * Constructor
     *
     * @param model  instance of the herbarmodel
     * @param counti instance of CountScore
     */
    public OneOfFiveQuestionPanel( final HerbarModel model, final CountScore counti )
    {
        this.model = model;
        this.countScore = counti;
        questionModel = new OneOfFiveQuestionModel( model );
        //  int totalQuestions = questionModel.initializeDataModel();

        // ActionListener für Taxon-Buttons: richtige oder falsche Antwort.
        final ActionListener actionListenerTaxa =
                new ActionListener()
                {
                    public void actionPerformed( final ActionEvent e )
                    {
                        final JButton button = (JButton) ( e.getSource() );
                        LOG.debug( "Source is " + button.getName() + ", wrong is " + randomAdd );
                        if ( ( (JButton) ( e.getSource() ) ).getName().equals( "" + randomAdd ) )
                        {
                            countScore.addRightScore( 1 );
                            questionModel.setRightAnswers( unit );
                        }
                        else
                        {
                            showCorrectAnswer();
                            countScore.addWrongScore( 1 );
                            questionModel.setWrongAnswers( unit );
                        }
                    }
                };

        for ( int i = 0; i < ( taxButton.length ); i++ )
        {
            this.add( taxButton[i] = ComponentFactory.createButton( OneOfFive.class, "ONEOFFIVE.BUT." + i, actionListenerTaxa ) );
            taxButton[i].setEnabled( false );
            taxButton[i].setFocusPainted( true );
        }
    }

    /**
     * @see Question#firstQuestion()
     */
    public void firstQuestion()
    {
    }

    /**
     * @see Question#lastQuestion()
     */
    public void lastQuestion()
    {
    }

    /**
     * initializes the data to generate all questions out of it.
     */
    public void init()
    {
        questionModel.initializeDataModel();
    }

    /**
     * cleans the text in all buttons and disables them
     */
    public void reset()
    {
        for ( final JButton aTaxButton : taxButton )
        {
            aTaxButton.setText( " " );
            aTaxButton.setEnabled( false );
        }
    }

    /**
     * Restarts the game within the given scope.
     */
    public void restart()
    {
        questionModel.fillAllQuestions( countScore.getMaxScore() );
    }

    /**
     * get the QuestionDataUnit for the next question.
     */
    public void nextQuestion()
    {
        unit = questionModel.getTaxon();
        unit.randomize();
        taxItem = unit.getTaxItem();
        randomAdd = unit.getRandomAdd();
        for ( int i = 0; i < ( taxButton.length ); i++ )
        {
            taxButton[i].setText( ( (Taxon) ( taxItem.get( i ) ) ).getName() );
            if ( INFO )
            {
                LOG.info( "Taxon: (" + i + ")" + ( (Taxon) taxItem.get( i ) ).getName() +
                        ", Level: " + ( (Taxon) taxItem.get( i ) ).getLevel().getName() +
                        ", Parenttaxon: " + ( (Taxon) taxItem.get( i ) ).getParentTaxon().getName() +
                        ", Parentlevel: " + ( (Taxon) taxItem.get( i ) ).getParentTaxon().getLevel().getName() );
            }
            taxButton[i].setName( new Integer( i ).toString() );
            taxButton[i].setEnabled( true );
        }
    }

    private void showCorrectAnswer()
    {
        final JOptionPane jop = new JOptionPane();
        final ImageIcon imageWrong = ImageLocator.getIcon( "bird_0.gif" );
        final String paneTitle = Strings.getString( OneOfFive.class, "ONEOFFIVE.PANE.TITLE" );
        final String lineBreak = System.getProperty( "line.separator" ) + System.getProperty( "line.separator" );
        final String paneQuest = Strings.getString( OneOfFive.class,
                "ONEOFFIVE.AUSGABE.WRONG", lineBreak, taxButton[randomAdd].getText() );
        JOptionPane.showMessageDialog( this.getParent(), paneQuest, paneTitle, JOptionPane.ERROR_MESSAGE, imageWrong );
    }
}
