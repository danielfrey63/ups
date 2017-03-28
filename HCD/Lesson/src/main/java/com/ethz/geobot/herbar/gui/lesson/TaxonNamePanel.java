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
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.CorrectnessChecker;
import static com.ethz.geobot.herbar.gui.CorrectnessChecker.IS_FALSE;
import static com.ethz.geobot.herbar.gui.CorrectnessChecker.IS_TRUE;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode.ABFRAGEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.Mode.LERNEN;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.NameText;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The taxon panel is used to query a taxon from the user. Once the query is answered correctly it displays the
 * taxon name. There are two states in this panel: (a) the query subMode, and (b) the display subMode.
 * <p/>
 * In the query subMode the taxon level icon is displayed together with a text field where the user can enter his
 * guess for the taxons name. Once he enters the guess and confirms it with the enter key, the result is evaluated,
 * errors are fed back through a dialog message while the text is kept in the field..
 * <p/>
 * Once the user has entered the correct taxon name the field turns into a label displaying the taxons name.
 * <p/>
 * The user can switch between query and display subMode by clicking on the level icon and switch to guess by clicking
 * on the label.
 */
public class TaxonNamePanel extends JPanel
{
    public static final Logger LOG = LoggerFactory.getLogger( TaxonNamePanel.class );

    /**
     * Allows the user to enter a guess for the taxon.
     */
    private final JTextField taxonField = new JTextField();

    /**
     * Allows to validate guesses.
     */
    private final CorrectnessChecker correctnessChecker = new CorrectnessChecker();

    /**
     * The layout to handle guess and learn statuses.
     */
    private final CardLayout layout = new CardLayout();

    /**
     * THe panel that holds the status panels.
     */
    private final JPanel panel = new JPanel( layout );

    /**
     * Parent component to display dialogs on.
     */
    private final JFrame parent;

    /**
     * The model holding the sub mode states.
     */
    private final TaxStateModel taxStateModel;

    /**
     * The taxon behind this panel
     */
    private final Taxon taxon;
    private final JLabel label;
    private final List<TaxonNamePanel> list;

    /**
     * Displays an icon for the taxon level, a entry field or taxon name and a correctness or question mark.
     *
     * @param taxStateModel the state model
     * @param taxon         the taxon to display and handle
     * @param subMode       the initial sub mode to use
     * @param list
     */
    public TaxonNamePanel( final JFrame parent, final TaxStateModel taxStateModel, final Taxon taxon, final Mode subMode, List<TaxonNamePanel> list )
    {
        setName( taxon.getName() ); // Easier debugging

        this.taxon = taxon;
        this.parent = parent;
        this.taxStateModel = taxStateModel;
        this.list = list;

        label = new JLabel( " " + taxon.getName() + " " );
        panel.add( label, LERNEN.name() );
        panel.add( taxonField, ABFRAGEN.name() );
        panel.setBorder( new EmptyBorder( 1, 0, 0, 0 ) );

        final CellConstraints cc = new CellConstraints();
        setLayout( new FormLayout( "$lcgap, default, $lcgap, fill:10dlu:grow, $ugap", "default" ) );
        final JLabel levelIcon = new JLabel( ImageLocator.getIcon( "icon" + taxon.getLevel().getName() + ".gif" ) );
        add( levelIcon, cc.xy( 2, 1 ) );
        add( panel, cc.xy( 4, 1 ) );

        final MouseAdapter switcher = new MouseAdapter()
        {
            @Override
            public void mouseClicked( MouseEvent e )
            {
                swapSubModes();
            }
        };
        levelIcon.addMouseListener( switcher );
        panel.addMouseListener( switcher );
        label.addMouseListener( switcher );

        taxonField.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                final String guess = taxonField.getText();
                final CorrectnessChecker.Correctness correctness = handleCorrectness( guess, taxon );
                if ( correctness == IS_TRUE )
                {
                    swapSubModes();
                }
                LOG.info( "guess of \"" + guess + "\" for \"" + taxon + "\" is " + correctness );
            }
        } );
        setSubMode( subMode );
    }

    private void swapSubModes()
    {
        final Mode newSubMode = taxStateModel.getSubMode( taxon ).equals( ABFRAGEN ) ? LERNEN : ABFRAGEN;
        taxStateModel.setSubMode( taxon, newSubMode );
        setSubMode( newSubMode );

        for ( TaxonNamePanel panel : list )
        {
            if ( panel != this && taxStateModel.getSubModes().get( panel.taxon ) == ABFRAGEN )
            {
                panel.taxonField.requestFocus();
                LOG.info( "setting focus to field for \"" + taxon + "\"" );
            }
        }
    }

    public void setSubMode( final Mode subMode )
    {
        layout.show( panel, subMode.name() );
        taxonField.setText( "" );
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
        final CorrectnessChecker.Correctness correctness = correctnessChecker.getCorrectness( taxon.getName(), guess, extractSynonymTexts( taxon ) );
        final List<String> answer = correctnessChecker.getCorrectnessText();
        if ( answer != null )
        {
            final Object[] buttons = new Object[]{Strings.getString( "BUTTON.OK.TEXT" )};
            JOptionPane.showOptionDialog( parent, answer.toArray(), "Hinweis", YES_NO_OPTION, INFORMATION_MESSAGE, null, buttons, buttons[0] );
        }
        else if ( correctness == IS_FALSE )
        {
            final Object[] buttons = new Object[]{Strings.getString( "BUTTON.OK.TEXT" )};
            JOptionPane.showOptionDialog( parent, new String[]{"Ihre Eingabe ist nicht zutreffend", "Bitte versuchen Sie es nochmals", ""}, "Nächster Versuch", YES_NO_OPTION, INFORMATION_MESSAGE, null, buttons, buttons[0] );
        }
        // some new evaluations take place AFTER the correct taxon has been passed, so keep complete state
        return correctness;
    }

    private String[] extractSynonymTexts( final Taxon taxon )
    {
        final GraphNodeList syns = taxon.getAsGraphNode().getChildren( NameText.class );
        final String[] synonyms = new String[syns.getAll().length];
        for ( int ix = 0; ix < syns.size(); ix++ )
        {
            synonyms[ix] = syns.get( ix ).toString().toLowerCase();
        }
        return synonyms;
    }

    @Override
    public void requestFocus()
    {
        taxonField.requestFocusInWindow();
    }
}
