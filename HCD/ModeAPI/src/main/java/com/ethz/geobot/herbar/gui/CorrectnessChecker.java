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

import ch.jfactory.math.EvaluationResult;
import ch.jfactory.math.LevenshteinLevel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the distance of genus and species and prepares an answer.<p> Some mistakes may pass as <code>IS_NEARLY_TRUE</code> if they occur in the follwoing context: <ul> <li>The ending of any name is a commonly mixed up ending</li> <li>Any one addition, deletion or change of a character may occur</li> </ul> So the following guess samples for Cladonia rangiferina should pass: <ul> <li>Cladonia rangiferin<b>um</b></li> <li>Cladoni<b>um</b> rangiferina</li> <li>Cladoni<b>um</b> rangiferin<b>um</b></li> <li>Cl<b>o</b>donia rangiferina</li> <li>Cladonia rang<b>o</b>ferina</li> <li>Cl<b>o</b>doni<b>um</b> rangiferin<b>um</b></li> <li>Cladoni<b>um</b> rang<b>o</b>ferin<b>um</b></li> </ul> Whereas the following shouldn't: <ul> <li>Cl<b>o</b>doni<b>um</b> rang<b>o</b>ferin<b>um</b></li> </ul>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class CorrectnessChecker
{
    public static final Correctness IS_TRUE = new Correctness( 0 );

    public static final Correctness IS_NEARLY_TRUE = new Correctness( 1 );

    public static final Correctness IS_FALSE = new Correctness( 2 );

    private static final LevenshteinLevel mayBeTolerated = LevenshteinLevel.LEVEL_RESTRICTIVE;

    private static final LevenshteinLevel mustBeEqual = LevenshteinLevel.LEVEL_EQUAL;

    private static final String[][] allEndings;

    private List<String> correctnessText;

    public Correctness getCorrectness( final String taxon, String guess, String[] synonyms )
    {
        Correctness ret;
        correctnessText = null;

        // get proposed string and eliminate double spaces
        int iDoubles;
        guess = guess.trim();
        while ( (iDoubles = guess.indexOf( "  " )) >= 0 )
        {
            guess = guess.substring( 0, iDoubles ) + guess.substring( iDoubles + 1, guess.length() );
        }

        // check whether within the given tolerance for species, endings may be wrong
        if ( taxon.contains( " " ) )
        {
            final Correctness[] iG = checkGenus( taxon, guess );
            final Correctness[] iS = checkEpithethon( taxon, guess );
            final List<String> vA = new ArrayList<String>();
            // all wrong if body or ending of genus wrong. then check for synonyms
            if ( iG[0] == IS_FALSE || iS[0] == IS_FALSE )
            {
                ret = checkSynonym( guess, synonyms );
            }
            // all taxon
            else if ( iG[0] == IS_TRUE && iG[1] == IS_TRUE && iS[0] == IS_TRUE && iS[1] == IS_TRUE )
            {
                ret = IS_TRUE;
            }
            // give some hints when near the solution
            else if ( (iG[0] == IS_NEARLY_TRUE && iG[1] != IS_FALSE && iS[0] == IS_TRUE && iS[1] != IS_FALSE) ||
                    (iG[0] == IS_TRUE && iG[1] != IS_FALSE && iS[0] == IS_NEARLY_TRUE && iS[1] != IS_FALSE) ||
                    (iG[0] == IS_TRUE && iG[1] != IS_TRUE && iS[0] == IS_TRUE && iS[1] == IS_TRUE) ||
                    (iG[0] == IS_TRUE && iG[1] == IS_TRUE && iS[0] == IS_TRUE && iS[1] != IS_TRUE) ||
                    (iG[0] == IS_TRUE && iG[1] == IS_NEARLY_TRUE && iS[0] == IS_TRUE && iS[1] == IS_NEARLY_TRUE) )
            {
                if ( iG[0] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Gattung ist beinahe richtig                    " );
                }
                if ( iG[1] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Endung der Gattung ist beinahe richtig         " );
                }
                if ( iS[0] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Das Art-Epithethon is beinahe richtig              " );
                }
                if ( iS[1] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Endung des Art-Epithethons ist beinahe richtig " );
                }
                vA.add( " " );
                vA.add( "Zum Vergleich:" );
                vA.add( "- " + taxon + " (richtige Lösung)" );
                vA.add( "- " + guess + " (Ihr Vorschlag)" );
                correctnessText = new ArrayList<String>();
                correctnessText.add( " " );
                correctnessText.add( "Sie sind nahe dran:                         " );
                correctnessText.addAll( vA );
                correctnessText.add( " " );
                correctnessText.add( "Bitte versuchen Sie es noch einmal...       " );
                correctnessText.add( " " );
                ret = IS_NEARLY_TRUE;
            }
            else
            {
                correctnessText = new ArrayList<String>();
                correctnessText.add( "Der Vorschlag ist nicht korrekt.            " );
                correctnessText.add( " " );
                correctnessText.add( "Bitte versuchen Sie es noch einmal...       " );
                correctnessText.add( " " );
                ret = IS_FALSE;
            }
        }
        else
        { // it's not a species
            final boolean withinTolerance = mayBeTolerated.getEval( taxon.toLowerCase(), guess.toLowerCase() ).isPassed();
            final boolean equal = mustBeEqual.getEval( taxon.toLowerCase(), guess.toLowerCase() ).isPassed();
            final boolean isSynonym = checkSynonym( guess, synonyms ) == IS_NEARLY_TRUE;
            if ( equal )
            {
                ret = IS_TRUE;
            }
            else if ( withinTolerance )
            {
                correctnessText = new ArrayList<String>();
                correctnessText.add( " " );
                correctnessText.add( "Sie sind nahe dran:                         " );
                correctnessText.add( "- Es befinden sich noch kleine Schreibfehler in" );
                correctnessText.add( "  Ihrem Vorschlag" );
                correctnessText.add( " " );
                correctnessText.add( "  " + taxon + " (richtige Lösung)" );
                correctnessText.add( "  " + guess + " (Ihr Vorschlag)" );
                correctnessText.add( " " );
                correctnessText.add( "Bitte versuchen Sie es noch einmal...       " );
                correctnessText.add( " " );
                ret = IS_NEARLY_TRUE;
            }
            else if ( isSynonym )
            {
                correctnessText = new ArrayList<String>();
                correctnessText.add( "Sie verwenden ein Synonym:                  " );
                correctnessText.add( "  " + guess );
                correctnessText.add( " " );
                correctnessText.add( "Wir verwenden jedoch:                       " );
                correctnessText.add( "  " + taxon );
                correctnessText.add( " " );
                ret = IS_NEARLY_TRUE;
            }
            else
            {
                correctnessText = new ArrayList<String>();
                correctnessText.add( "Der Vorschlag ist falsch.                   " );
                correctnessText.add( " " );
                correctnessText.add( "Bitte versuchen Sie es noch einmal...       " );
                correctnessText.add( " " );
                ret = IS_FALSE;
            }
        }
        return ret;
    }

    private Correctness checkSynonym( final String synString, String[] synonyms )
    {
        EvaluationResult eval = EvaluationResult.EVALUATION_FAILED;
        for ( int ix = 0; ix < synonyms.length && !eval.isPassed(); ix++ )
        {
            final String strSyn = synonyms[ix];
            eval = mustBeEqual.getEval( strSyn.toLowerCase(), synString.toLowerCase() );
        }
        if ( eval.isPassed() )
        {
            return IS_NEARLY_TRUE;
        }
        return IS_FALSE;
    }

    private Correctness[] checkGenus( String correct, String guess )
    {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        final int iGuess = guess.indexOf( " " );
        final int iCorrect = correct.indexOf( " " );
        final String geniusGuess = iGuess >= 0 ? guess.substring( 0, iGuess ).trim() : guess;
        final String geniusCorrect = correct.substring( 0, iCorrect ).trim();
        return checkTaxon( geniusCorrect, geniusGuess );
    }

    private Correctness[] checkEpithethon( String correct, String guess )
    {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        final int iCorrect = correct.indexOf( " " );
        final String speciesGuess = guess.contains( " " ) ? guess.substring( guess.indexOf( " " ) + 1, guess.length() ).trim() : guess;
        final String speciesCorrect = correct.substring( iCorrect + 1, correct.length() ).trim();
        return checkTaxon( speciesCorrect, speciesGuess );
    }

    /**
     * This method checks the name taking it as a Taxon.
     * <p/>
     * The first <code>Correctness</code> returned indicates the Levenstein distance of the two strings.
     * <p/>
     * The second <code>Correctness</code> gives a measure for the endings. Certain endings may be tolerated, which is
     * indicated with {@link #IS_NEARLY_TRUE}, others may be definively wrong ({@link #IS_FALSE}) or correct
     * ({@link #IS_TRUE}).
     *
     * @param correct the correct taxon name
     * @param guess   guessfor the taxon name
     * @return an array of length two with correcness for word base and ending
     */
    private Correctness[] checkTaxon( final String correct, final String guess )
    {
        // accept all species epitheta for taxa with "sp."
        if ( correct.equals( "sp." ) || guess.equals( correct ) )
        {
            return new Correctness[]{IS_TRUE, IS_TRUE};
        }
        // or check taxa names
        else
        {
            final Correctness[] res = new Correctness[]{IS_FALSE, IS_FALSE};

            for ( final String[] endings : allEndings )
            {
                for ( final String ending : endings )
                {
                    if ( guess.endsWith( ending ) )
                    {
                        String guessPostfix = "", correctPostfix = "";
                        if ( correct.endsWith( ending ) && guess.length() == correct.length() )
                        {
                            // both endings match, but bodies are different
                            res[1] = IS_TRUE;
                            guessPostfix = ending;
                            correctPostfix = ending;
                        }
                        else
                        {
                            // bodies are different and endings MAY be tolerated. If endings have been determined for
                            // both strings, there bodies are extracted. Bodies then are compared.
                            for ( final String alternateEnding : endings )
                            {
                                if ( guess.endsWith( alternateEnding ) )
                                {
                                    guessPostfix = alternateEnding;
                                }
                                if ( correct.endsWith( alternateEnding ) )
                                {
                                    correctPostfix = alternateEnding;
                                    res[1] = IS_NEARLY_TRUE;
                                }
                            }
                        }
                        final String correctBody = correct.substring( 0, correct.lastIndexOf( correctPostfix ) );
                        final String guessedBody = guess.substring( 0, guess.lastIndexOf( guessPostfix ) );
                        final EvaluationResult strongEval = mustBeEqual.getEval( guessedBody, correctBody );
                        final EvaluationResult tolerantEval = mayBeTolerated.getEval( guessedBody, correctBody );
                        if ( strongEval.isPassed() )
                        {
                            res[0] = IS_TRUE;
                        }
                        else if ( tolerantEval.isPassed() )
                        {
                            res[0] = IS_NEARLY_TRUE;
                        }
                        else
                        {
                            res[0] = IS_FALSE;
                        }
                        return res;
                    }
                }
            }
            return res;
        }
    }

    public List<String> getCorrectnessText()
    {
        return correctnessText;
    }

    static
    {
        // make sure to have the shorter endings at the end...
        allEndings = new String[][] {
                {"tans", "ens"},
                {"er", "ris", "re", "rys", "is"},
                {"um", "us", "a"},
                {"ii", "i"}
        };
    }

    public static class Correctness
    {
        private final int id;

        private Correctness( final int newId )
        {
            id = newId;
        }

        public String toString()
        {
            if ( id == 0 )
            {
                return "RIGHT";
            }
            else if ( id == 1 )
            {
                return "NEAR";
            }
            else if ( id == 2 )
            {
                return "WRONG";
            }
            else
            {
                return "UNKNOWN";
            }
        }
    }

    public static void main( String[] args )
    {
        final Map<String[], Correctness> data = new HashMap<String[], Correctness>();
        data.put( new String[]{"Abies", "Abies"}, IS_TRUE );
        data.put( new String[]{"asdf", "Abies alba"}, IS_FALSE );
        data.put( new String[]{"Abies alba", "Abies alba"}, IS_TRUE );
        data.put( new String[]{"Abies album", "Abies alba"}, IS_NEARLY_TRUE );
        data.put( new String[]{"Einblättriges Lebermoos", "Einblättriges Lebermoos"}, IS_TRUE );
        data.put( new String[]{"Abies alba", "Abies alba i.w.S."}, IS_TRUE );
        data.put( new String[]{"Abies alba", "Abies alba agg."}, IS_TRUE );
        for ( final String[] string : data.keySet() )
        {
            check( data.get( string ), string[0], string[1] );
        }
    }

    static void check( final Correctness expected, final String guess, final String taxon )
    {
        final CorrectnessChecker checker = new CorrectnessChecker();
        final boolean testPassed = expected == checker.getCorrectness( taxon, guess, new String[]{"Abies alba"} );
        if ( !testPassed ) System.out.println( "- checking \"" + taxon + "\" against \"" + guess + "\" expecting " +
                expected + " - test " + (testPassed ? "passed" : ">> FAILED <<") );
    }
}
