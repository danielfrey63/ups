/*
 * Herbar CD-ROM version 2
 *
 * TaxonNameInterrogator.java
 *
 * Created on Feb 12, 2003 5:44:06 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.DefaultTextField;
import ch.jfactory.component.ScrollerPanel;
import ch.jfactory.lang.ReferencableBool;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a text field and an enter button to put guesses of the focused species name. The guesses are appended to the
 * end of this panel.
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

    private static final ImageIcon ICON_INDIFFERENT;

    private JPanel innerPanel;

    private ScrollerPanel scroller;

    private final CorrectnessChecker correctnesChecker = new CorrectnessChecker();

    private DefaultTextField edit;

    private Taxon taxon;

    private List answers = new ArrayList();

    private Level level;

    private InterrogatorModel interrogatorModel;

    private ReferencableBool complete;

    private JLabel label;

    /**
     * Asks for a taxon on any level.
     *
     * @param interrogatorModel
     */
    public TaxonNameInterrogator( final InterrogatorModel interrogatorModel )
    {
        this( null, interrogatorModel );
    }

    /**
     * Asks for the taxon on the specified level. If the taxon is not from the specified level, but from a lower level,
     * the parent taxon at the given level is used. If the taxon is from a higher level than the specified one, the
     * entry is disabled.
     *
     * @param level             The taxons level
     * @param interrogatorModel The model implementation to use
     */
    public TaxonNameInterrogator( final Level level, final InterrogatorModel interrogatorModel )
    {
        this.level = level;
        this.interrogatorModel = interrogatorModel;

        edit = createTextField();
        innerPanel = createEditPanel();
        scroller = new ScrollerPanel();

        setLayout( new BorderLayout() );
        add( innerPanel, BorderLayout.WEST );
        add( scroller, BorderLayout.CENTER );
    }

    private JPanel createEditPanel()
    {
        int x = 0;
        final JPanel panel = new JPanel( new GridBagLayout() );
        label = new JLabel();
        panel.add( label, new GridBagConstraints( x++, 0, 1, 1, 0, 1, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, Constants.GAP_WITHIN_GROUP ), 0, 0 ) );
        panel.add( edit, new GridBagConstraints( x++, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, Constants.GAP_WITHIN_GROUP ), 0, 0 ) );

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

    public void setBackground( final Color bg )
    {
        super.setBackground( bg );
        // The super constructor does call setBackground through the UIManager, so a check for null is necessary.
        if ( innerPanel != null )
        {
            innerPanel.setBackground( bg );
        }
        if ( scroller != null )
        {
            scroller.setBackground( bg );
        }
    }

    public void setTaxFocus( final Taxon focus )
    {
        this.taxon = getTaxonOnLevel( focus, level );
        answers = interrogatorModel.getAnswers( focus );
        complete = interrogatorModel.getComplete( focus );
        updateScroller();
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
            final JLabel label = ( (JLabel) answers.get( 0 ) );
            edit.setText( label.getText() );
            edit.selectAll();
        }
        else
        {
            edit.setText( "" );
        }
        final boolean enableByCompletedness = !complete.isTrue();
        edit.setEnabled( enableByCompletedness && enableByLevel || interrogatorModel instanceof MemorizingInterrogatorModel );
    }

    public void addGuess( final String guess )
    {
        final CorrectnessChecker.Correctness correctness = handleCorrectness( guess, taxon );
        answers.add( 0, createLabel( guess, taxon, correctness ) );
        updateDataEntry();
        updateScroller();
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
        final CorrectnessChecker.Correctness correctness = correctnesChecker.getCorrectness( taxon, guess );
        final List answer = correctnesChecker.getCorrectnessText();
        if ( answer != null && !( interrogatorModel instanceof MemorizingInterrogatorModel ) )
        {
            final Object[] buttons = new Object[]{Strings.getString( "BUTTON.OK.TEXT" )};
            JOptionPane.showOptionDialog( this, answer.toArray(), "Hinweis", 0, JOptionPane.INFORMATION_MESSAGE,
                    null, buttons, buttons[0] );
        }
        // some new evaluations take place AFTER the correct taxon has been passed, so keep complete state
        complete.setBool( correctness == CorrectnessChecker.IS_TRUE || complete.isTrue() );
        return correctness;
    }

    /**
     * Moves up the taxonomic tree until the taxon on the given level is found.
     *
     * @param taxon The taxon to start with
     * @param level The level to find the supertaxon on
     * @return The taxon found on the given level
     */
    private Taxon getTaxonOnLevel( Taxon taxon, final Level level )
    {
        if ( level == null )
        {
            return taxon;
        }
        else
        {
            while ( taxon != null && taxon.getLevel() != level )
            {
                taxon = taxon.getParentTaxon();
            }
        }
        return taxon;
    }

    /**
     * Evaluates the given text and compares it to the current taxon. Constructs out of the result an appropriate label
     * to display in the guesses list.
     *
     * @param guess The text to evaluate
     * @param taxon The taxon to compare to
     * @return A label with the given text and an appropriate icon
     */
    private JLabel createLabel( final String guess, final Taxon taxon, final CorrectnessChecker.Correctness correctness )
    {
        JLabel label = null;
        if ( interrogatorModel instanceof MemorizingInterrogatorModel )
        {
            if ( answers.size() == 0 )
            {
                label = new JLabel( guess );
            }
            else
            {
                label = new JLabel( guess, ICON_INDIFFERENT, JLabel.CENTER );
                label.setHorizontalTextPosition( JLabel.LEFT );
            }
        }
        else
        {
            if ( correctness == CorrectnessChecker.IS_TRUE )
            {
                label = new JLabel( guess, ICON_CORRECT, JLabel.CENTER );
            }
            else if ( correctness == CorrectnessChecker.IS_NEARLY_TRUE )
            {
                label = new JLabel( guess, ICON_NEAR, JLabel.CENTER );
            }
            else if ( correctness == CorrectnessChecker.IS_FALSE )
            {
                label = new JLabel( guess, ICON_WRONG, JLabel.CENTER );
            }
            else
            {
                LOG.error( "unknown correctness for guess \"" + guess + "\" and correct \"" + taxon + "\": " + correctness );
            }
        }
        label.setBackground( super.getBackground() );
        label.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
        return label;
    }

    /**
     * Returns whether the entry field and button should be enabled by considering the level of this interrogator and
     * the currenct focus taxon.
     *
     * @param taxon the new focus to set
     */
    private boolean updateForLevel( final Taxon taxon )
    {
        return taxon != null && ( taxon.getLevel() == level || level == null );
    }

    private void updateScroller()
    {
        scroller.removeAll();
        for ( final Object answer : answers )
        {
            final Component component = (Component) answer;
            scroller.add( component );
        }
        scroller.doLayout();
    }

    static
    {
        ICON_CORRECT = ImageLocator.getIcon( "abfrageRichtig.gif" );
        ICON_NEAR = ImageLocator.getIcon( "abfrageFast.png" );
        ICON_WRONG = ImageLocator.getIcon( "abfrageFalsch.gif" );
        ICON_INDIFFERENT = ImageLocator.getIcon( "separator_short.png" );
    }

    public static interface InterrogatorModel
    {
        /**
         * Restore answers from cache or create new ones if the given taxon appears for the first time.
         *
         * @param taxon The new taxon to set.
         */
        public List getAnswers( Taxon taxon );

        /**
         * Returns whether the task has completed or not.
         *
         * @param taxon The taxon the tasks completedness is interrogated
         * @return A referencable boolean containing the flag
         */
        public ReferencableBool getComplete( Taxon taxon );
    }

    /**
     * For learning purpose.
     */
    public static class TransientInterrogatorModel implements InterrogatorModel
    {
        public List getAnswers( final Taxon taxon )
        {
            return new ArrayList();
        }

        public ReferencableBool getComplete( final Taxon taxon )
        {
            return new ReferencableBool( false );
        }
    }

    /**
     * For exam purpose.
     */
    public static class MemorizingInterrogatorModel implements InterrogatorModel
    {
        private Map answersCache = new HashMap();

        private Map completeCache = new HashMap();

        public Map getAnswersMap()
        {
            return answersCache;
        }

        public List getAnswers( final Taxon focus )
        {
            List answers = (List) answersCache.get( focus );
            if ( answers == null )
            {
                answers = new ArrayList();
                answersCache.put( focus, answers );
            }
            return answers;
        }

        public Set getTaxa()
        {
            return answersCache.keySet();
        }

        public ReferencableBool getComplete( final Taxon focus )
        {
            ReferencableBool complete = (ReferencableBool) completeCache.get( focus );
            if ( complete == null )
            {
                complete = new ReferencableBool( false );
                completeCache.put( focus, complete );
            }
            return complete;
        }

        public void reset()
        {
            answersCache = new HashMap();
            completeCache = new HashMap();
        }
    }
}
