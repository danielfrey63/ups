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
package com.ethz.geobot.herbar.gui;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.DefaultTextField;
import ch.jfactory.component.ScrollPanel;
import ch.jfactory.lang.BooleanReference;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

/**
 * Displays a text field and an enter button to put guesses of the focused species name. The guesses are appended to
 * the end of this panel.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxonNameInterrogator extends JPanel
{
    private static final Logger LOG = LoggerFactory.getLogger( TaxonNameInterrogator.class );

    private static final ImageIcon ICON_CORRECT;

    private static final ImageIcon ICON_NEAR;

    private static final ImageIcon ICON_WRONG;

    private ScrollPanel scroll;

    private final CorrectnessChecker correctnessChecker = new CorrectnessChecker();

    private DefaultTextField edit;

    private Taxon taxon;

    private List<JLabel> answers = new ArrayList<JLabel>();

    private Level level;

    private InterrogatorModel interrogatorModel;

    private BooleanReference complete;

    private JLabel label;

    /**
     * Asks for a taxon on any level.
     *
     * @param interrogatorModel the model
     */
    public TaxonNameInterrogator( final InterrogatorModel interrogatorModel )
    {
        this( null, interrogatorModel );
    }

    /**
     * Asks for the taxon on the specified level. If the taxon is not from the specified level,
     * but from a lower level, the parent taxon at the given level is used. If the taxon is from a higher level than
     * the specified one, the entry is disabled.
     *
     * @param level             The level of the taxon
     * @param interrogatorModel The model implementation to use
     */
    public TaxonNameInterrogator( final Level level, final InterrogatorModel interrogatorModel )
    {
        this.level = level;
        this.interrogatorModel = interrogatorModel;

        edit = createTextField();
        final JPanel innerPanel = createEditPanel();
        scroll = new ScrollPanel();

        setLayout( new BorderLayout() );
        add( innerPanel, BorderLayout.WEST );
        add( scroll, BorderLayout.CENTER );
    }

    private JPanel createEditPanel()
    {
        final JPanel panel = new JPanel( new GridBagLayout() );
        label = new JLabel();
        panel.add( label, new GridBagConstraints( 0, 0, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, Constants.GAP_WITHIN_GROUP ), 0, 0 ) );
        panel.add( edit, new GridBagConstraints( 1, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, Constants.GAP_WITHIN_GROUP ), 0, 0 ) );

        final Dimension preferredSize = panel.getPreferredSize();
        panel.setPreferredSize( preferredSize );

        return panel;
    }

    private DefaultTextField createTextField()
    {
        final DefaultTextField edit = new DefaultTextField( 30 );
        edit.setPreferredSize( edit.getPreferredSize() );
        final ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final DefaultTextField field = (DefaultTextField) e.getSource();
                addGuess( field.getText() );
            }
        };
        edit.addActionListener( actionListener );
        return edit;
    }

    public void setTaxFocus( final Taxon focus )
    {
        LOG.debug( "setting taxon focus to " + focus );
        this.taxon = focus;
        answers = interrogatorModel.getAnswers( focus );
        complete = interrogatorModel.getComplete( focus );
        updateScroll();
        updateDataEntry();
        final Level focusLevel = focus.getLevel();
        final String newLevel = ( focusLevel == null ? "No Level" : ( level == null ? focusLevel.toString() : level.toString() ) );
        label.setText( Strings.getString( "FIELD.TAXON.LABEL", newLevel ) );
        invalidate();
    }

    private void updateDataEntry()
    {
        final boolean enableByLevel = updateForLevel( taxon );
        if ( answers.size() > 0 )
        {
            final JLabel label = ( answers.get( 0 ) );
            edit.setText( label.getText() );
            edit.selectAll();
        }
        else
        {
            edit.setText( "" );
        }
        final boolean enableByCompleteness = !complete.isTrue();
        edit.setEnabled( enableByCompleteness && enableByLevel );
    }

    public void addGuess( final String guess )
    {
        final CorrectnessChecker.Correctness correctness = handleCorrectness( guess, taxon );
        answers.add( 0, createLabel( guess, taxon, correctness ) );
        updateDataEntry();
        updateScroll();
    }

    /**
     * Sets edit field and enter button to appropriate enable states and displays a dialog with hints if available.
     *
     * @param guess The text to evaluate
     * @param taxon The taxon to compare to
     * @return The correctness found for that pair of parameters
     */
    private CorrectnessChecker.Correctness handleCorrectness( final String guess, final Taxon taxon )
    {
        final CorrectnessChecker.Correctness correctness = correctnessChecker.getCorrectness( taxon, guess );
        final List<String> answer = correctnessChecker.getCorrectnessText();
        if ( answer != null )
        {
            final Object[] buttons = new Object[]{Strings.getString( "BUTTON.OK.TEXT" )};
            JOptionPane.showOptionDialog( this, answer.toArray(), "Hinweis", YES_NO_OPTION, INFORMATION_MESSAGE, null, buttons, buttons[0] );
        }
        // some new evaluations take place AFTER the correct taxon has been passed, so keep complete state
        complete.setBool( correctness == CorrectnessChecker.IS_TRUE || complete.isTrue() );
        return correctness;
    }

    /**
     * Evaluates the given text and compares it to the current taxon. Constructs out of the result an appropriate label
     * to display in the guesses list.
     *
     * @param guess       The text to evaluate
     * @param taxon       The taxon to compare to
     * @param correctness the correctness used
     * @return A label with the given text and an appropriate icon
     */
    private JLabel createLabel( final String guess, final Taxon taxon, final CorrectnessChecker.Correctness correctness )
    {
        JLabel label;
        if ( correctness == CorrectnessChecker.IS_TRUE )
        {
            label = new JLabel( guess, ICON_CORRECT, JLabel.CENTER );
        }
        else if ( correctness == CorrectnessChecker.IS_NEARLY_TRUE )
        {
            label = new JLabel( guess, ICON_NEAR, JLabel.CENTER );
        }
        else
        {
            label = new JLabel( guess, ICON_WRONG, JLabel.CENTER );
        }
        label.setBackground( super.getBackground() );
        label.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
        return label;
    }

    /**
     * Returns whether the entry field and button should be enabled by considering the level of this interrogator and
     * the current focus taxon.
     *
     * @param taxon the new focus to set
     * @return enabled state
     */
    private boolean updateForLevel( final Taxon taxon )
    {
        return taxon != null && ( taxon.getLevel() == level || level == null );
    }

    private void updateScroll()
    {
        scroll.removeAll();
        for ( final Object answer : answers )
        {
            final Component component = (Component) answer;
            scroll.add( component );
        }
        scroll.doLayout();
    }

    static
    {
        ICON_CORRECT = ImageLocator.getIcon( "abfrageRichtig.gif" );
        ICON_NEAR = ImageLocator.getIcon( "abfrageFast.png" );
        ICON_WRONG = ImageLocator.getIcon( "abfrageFalsch.gif" );
    }

    public static interface InterrogatorModel
    {
        /**
         * Restore answers from cache or create new ones if the given taxon appears for the first time.
         *
         * @param taxon The new taxon to set.
         * @return the list of answers
         */
        public List<JLabel> getAnswers( Taxon taxon );

        /**
         * Returns whether the task has completed or not.
         *
         * @param taxon The taxon the tasks completeness is interrogated
         * @return A reference to boolean containing the flag
         */
        public BooleanReference getComplete( Taxon taxon );
    }

    /**
     * For learning purpose.
     */
    public static class TransientInterrogatorModel implements InterrogatorModel
    {
        public List<JLabel> getAnswers( final Taxon taxon )
        {
            return new ArrayList<JLabel>();
        }

        public BooleanReference getComplete( final Taxon taxon )
        {
            return new BooleanReference( false );
        }
    }
}
