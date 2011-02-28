/*
 * Herbar CD-ROM version 2
 *
 * CorrectnessChecker.java
 *
 * Created on Feb 8, 2003 7:38:59 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.math.EvaluationResult;
import ch.jfactory.math.LevenshteinLevel;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.TaxonSynonym;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Calculates the distance of genus and species and prepares an answer.<p> Some mistakes may pass as
 * <code>IS_NEARLY_TRUE</code> if they occur in the follwoing context: <ul> <li>The ending of any name is a commonly
 * mixed up ending</li> <li>Any one addition, deletion or change of a character may occur</li> </ul> So the following
 * guess samples for Cladonia rangiferina should pass: <ul> <li>Cladonia rangiferin<b>um</b></li> <li>Cladoni<b>um</b>
 * rangiferina</li> <li>Cladoni<b>um</b> rangiferin<b>um</b></li> <li>Cl<b>o</b>donia rangiferina</li> <li>Cladonia
 * rang<b>o</b>ferina</li> <li>Cl<b>o</b>doni<b>um</b> rangiferin<b>um</b></li> <li>Cladoni<b>um</b>
 * rang<b>o</b>ferin<b>um</b></li> </ul> Whereas the following shouldn't: <ul> <li>Cl<b>o</b>doni<b>um</b>
 * rang<b>o</b>ferin<b>um</b></li> </ul>
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

    private static final Map<String, String[]> hsh;

    private List<String> correctnessText;

    public Correctness getCorrectness( final Taxon taxon, String guess )
    {
        final String correct = taxon.getName();

        Correctness ret;
        correctnessText = null;

        // get proposed string and eliminate double spaces
        int iDoubles;
        guess = guess.trim();
        while ( ( iDoubles = guess.indexOf( "  " ) ) >= 0 )
        {
            guess = guess.substring( 0, iDoubles ) + guess.substring( iDoubles + 1, guess.length() );
        }

        // check whether within the given tolerance for species, endings may be wrong
        if ( correct.indexOf( ' ' ) >= 0 )
        {
            final Correctness[] iG = checkGenus( correct, guess );
            final Correctness[] iS = checkEpithethon( correct, guess );
            final List<String> vA = new ArrayList<String>();
            // all wrong if body or ending of genus wrong. then check for synonyms
            if ( iG[0] == IS_FALSE || iG[1] == IS_FALSE )
            {
                ret = checkSynonym( taxon, guess );
            }
            // all correct
            else if ( iG[0] == IS_TRUE && iG[1] == IS_TRUE && iS[0] == IS_TRUE && iS[1] == IS_TRUE )
            {
                ret = IS_TRUE;
            }
            // give some hints when near the solution
            else if ( ( iG[0] == IS_NEARLY_TRUE && iG[1] != IS_FALSE && iS[0] == IS_TRUE && iS[1] != IS_FALSE ) ||
                    ( iG[0] == IS_TRUE && iG[1] != IS_FALSE && iS[0] == IS_NEARLY_TRUE && iS[1] != IS_FALSE ) ||
                    ( iG[0] == IS_TRUE && iG[1] != IS_TRUE && iS[0] == IS_TRUE && iS[1] == IS_TRUE ) ||
                    ( iG[0] == IS_TRUE && iG[1] == IS_TRUE && iS[0] == IS_TRUE && iS[1] != IS_TRUE ) ||
                    ( iG[0] == IS_TRUE && iG[1] == IS_NEARLY_TRUE && iS[0] == IS_TRUE && iS[1] == IS_NEARLY_TRUE ) )
            {
                if ( iG[0] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Gattung ist beinahe richtig" );
                }
                if ( iG[1] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Endung der Gattung ist beinahe richtig         " );
                }
                if ( iS[0] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Das Art-Epithethon is beinahe richtig                 " );
                }
                if ( iS[1] == IS_NEARLY_TRUE )
                {
                    vA.add( "- Die Endung des Art-Epithethons ist beinahe richtig      " );
                }
                vA.add( " " );
                vA.add( "Zum Vergleich:" );
                vA.add( "- " + correct + " (richtige Lösung)" );
                vA.add( "- " + guess + " (Ihr Vorschlag)" );
                correctnessText = new ArrayList<String>();
                correctnessText.add( " " );
                correctnessText.add( "Sie sind nahe dran:                         " );
                correctnessText.addAll( vA );
                correctnessText.add( " " );
                correctnessText.add( "Versuchen Sie es noch einmal..." );
                correctnessText.add( " " );
                ret = IS_NEARLY_TRUE;
            }
            else
            {
                ret = IS_FALSE;
            }
        }
        else
        { // it's not a species
            final boolean withinTolerance = mayBeTolerated.getEval( correct.toLowerCase(), guess.toLowerCase() ).isPassed();
            final boolean equal = mustBeEqual.getEval( correct.toLowerCase(), guess.toLowerCase() ).isPassed();
            if ( equal )
            {
                ret = IS_TRUE;
            }
            else if ( withinTolerance )
            {
                correctnessText = new ArrayList<String>();
                correctnessText.add( " " );
                correctnessText.add( "Sie sind nahe dran:                         " );
                correctnessText.add( "- Es befinden sich noch kleine Schreibfehler in  " );
                correctnessText.add( "  Ihrem Vorschlag" );
                correctnessText.add( " " );
                correctnessText.add( "  " + correct + " (richtige Lösung)" );
                correctnessText.add( "  " + guess + " (Ihr Vorschlag)" );
                correctnessText.add( " " );
                correctnessText.add( "Versuchen Sie es noch einmal..." );
                correctnessText.add( " " );
                ret = IS_NEARLY_TRUE;
            }
            else
            {
                ret = checkSynonym( taxon, guess );
            }
        }
        return ret;
    }

    private Correctness checkSynonym( final Taxon taxon, final String synString )
    {
        final GraphNodeList sil = taxon.getAsGraphNode().getChildren( TaxonSynonym.class );
        EvaluationResult eval = EvaluationResult.EVALUATION_FAILED;
        for ( int ix = 0; ix < sil.size() && !eval.isPassed(); ix++ )
        {
            final String strSyn = sil.get( ix ).toString().toLowerCase();
            eval = mustBeEqual.getEval( strSyn, synString.toLowerCase() );
        }
        if ( eval.isPassed() )
        {
            correctnessText = new ArrayList<String>();
            correctnessText.add( "Sie verwenden ein Synonym:      " );
            correctnessText.add( "  " + synString );
            correctnessText.add( " " );
            correctnessText.add( "Wir verwenden jedoch:  " );
            correctnessText.add( "  " + taxon );
            correctnessText.add( " " );
            return IS_NEARLY_TRUE;
        }
        return IS_FALSE;
    }

    private Correctness[] checkGenus( String correct, String guess )
    {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        final int iReq = correct.indexOf( " " );
        final int iFnd = guess.indexOf( " " );
        String strFound = "";
        if ( iFnd < 0 )
        {
            strFound = guess;
        }
        else
        {
            strFound = guess.substring( 0, iFnd ).trim();
        }
        final String strReq = correct.substring( 0, iReq ).trim();
        return checkTaxon( strReq, strFound );
    }

    private Correctness[] checkEpithethon( String correct, String guess )
    {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        final int iReq = correct.indexOf( " " );
        final int iFnd = guess.indexOf( " " );
        String strFound = "";
        if ( iFnd < 0 )
        {
            strFound = guess;
        }
        else
        {
            strFound = guess.substring( iFnd + 1, guess.length() ).trim();
        }
        final String strReq = correct.substring( iReq + 1, correct.length() ).trim();
        return checkTaxon( strReq, strFound );
    }

    /**
     * This method checks the name taking it as a Taxon.<p> The first <code>Correctness</code> returned indicates the
     * Levenstein distance of the two strings.<p> The second <code>Correctness</code> gives a mesure for the endings.
     * Certain endings may be tolerated, which is indicated with {@link #IS_NEARLY_TRUE}, others may be definively wrong
     * ({@link #IS_FALSE}) or correct ({@link #IS_TRUE}).
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
            return new Correctness[]{CorrectnessChecker.IS_TRUE, CorrectnessChecker.IS_TRUE};
        }
        // or check taxanames
        else
        {
            final Correctness[] res = new Correctness[]{IS_FALSE, IS_FALSE};
            String correctBody = null, guessedBody = null,
                    guessPostfix = "", correctPostfix = "", toleratedEndings[];
            final Iterator<String[]> eVals = hsh.values().iterator();
            for ( final String s : hsh.keySet() )
            {
                toleratedEndings = eVals.next();
                if ( guess.endsWith( s ) )
                {
                    if ( correct.endsWith( s ) && guess.length() == correct.length() )
                    {
                        // both endings match, but bodies are different
                        res[1] = IS_TRUE;
                        guessPostfix = s;
                        correctPostfix = s;
                    }
                    else
                    {
                        // bodies are different and endings MAY be tolerated. If endings have been determined for
                        // both strings, there bodies are extracted. Bodies then are compared.
                        for ( final String alternateEnding : toleratedEndings )
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
                    correctBody = correct.substring( 0, correct.lastIndexOf( correctPostfix ) );
                    guessedBody = guess.substring( 0, guess.lastIndexOf( guessPostfix ) );
                    if ( guessedBody == null )
                    {
                        guessedBody = guess;
                    }
                    if ( correctBody == null )
                    {
                        correctBody = correct;
                    }
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
            return res;
        }
    }

    public List<String> getCorrectnessText()
    {
        return correctnessText;
    }

    static
    {
        hsh = new HashMap<String, String[]>();
        // make sure to have the shorter endings at the end...
        hsh.put( "tans", new String[]{"ens", "tans"} );
        hsh.put( "ens", new String[]{"ens", "tans"} );
        hsh.put( "e", new String[]{"er", "is", "e"} );
        hsh.put( "is", new String[]{"er", "is", "e"} );
        hsh.put( "er", new String[]{"er", "is", "e"} );
        hsh.put( "um", new String[]{"um", "us", "a"} );
        hsh.put( "us", new String[]{"um", "us", "a"} );
        hsh.put( "a", new String[]{"um", "us", "a"} );
        hsh.put( "i", new String[]{"ii", "i"} );
        hsh.put( "ii", new String[]{"ii", "i"} );

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

}