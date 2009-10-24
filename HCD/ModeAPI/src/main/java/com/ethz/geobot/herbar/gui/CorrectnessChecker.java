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
import ch.jfactory.math.LevensteinLevel;
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
public class CorrectnessChecker {

    public static final Correctness IS_TRUE = new Correctness(0);
    public static final Correctness IS_NEARLY_TRUE = new Correctness(1);
    public static final Correctness IS_FALSE = new Correctness(2);

    private static final LevensteinLevel mayBeTolerated = LevensteinLevel.LEVEL_RESTRICTIVE;
    private static final LevensteinLevel mustBeEqual = LevensteinLevel.LEVEL_EQUAL;
    private static final Map hsh;

    private List correctnessText;

    public Correctness getCorrectness(Taxon taxon, String guess) {
        String correct = taxon.getName();

        Correctness ret = null;
        correctnessText = null;

        // get proposed string and eliminate double spaces
        int iDoubles = -1;
        guess = guess.trim();
        while ((iDoubles = guess.indexOf("  ")) >= 0) {
            guess = guess.substring(0, iDoubles) + guess.substring(iDoubles + 1, guess.length());
        }

        // check whether within the given tolerance for species, endings may be wrong
        if (correct.indexOf(' ') >= 0) {
            Correctness[] iG = checkGenus(correct, guess);
            Correctness[] iS = checkEpithethon(correct, guess);
            List vA = new ArrayList();
            // all wrong if body or ending of genus wrong. then check for
            // synonymes
            if (iG[ 0 ] == IS_FALSE || iG[ 1 ] == IS_FALSE) {
                return checkSynonyme(taxon, guess);
            }
            // all correct
            else if (iG[ 0 ] == IS_TRUE && iG[ 1 ] == IS_TRUE && iS[ 0 ] == IS_TRUE && iS[ 1 ] == IS_TRUE) {
                ret = IS_TRUE;
            }
            // give some hints when near the solution
            else if ((iG[ 0 ] == IS_NEARLY_TRUE && iG[ 1 ] != IS_FALSE && iS[ 0 ] == IS_TRUE && iS[ 1 ] != IS_FALSE) ||
                    (iG[ 0 ] == IS_TRUE && iG[ 1 ] != IS_FALSE && iS[ 0 ] == IS_NEARLY_TRUE && iS[ 1 ] != IS_FALSE) ||
                    (iG[ 0 ] == IS_TRUE && iG[ 1 ] != IS_TRUE && iS[ 0 ] == IS_TRUE && iS[ 1 ] == IS_TRUE) ||
                    (iG[ 0 ] == IS_TRUE && iG[ 1 ] == IS_TRUE && iS[ 0 ] == IS_TRUE && iS[ 1 ] != IS_TRUE) ||
                    (iG[ 0 ] == IS_TRUE && iG[ 1 ] == IS_NEARLY_TRUE && iS[ 0 ] == IS_TRUE && iS[ 1 ] == IS_NEARLY_TRUE)) {
                if (iG[ 0 ] == IS_NEARLY_TRUE) {
                    vA.add("- Die Gattung ist beinahe richtig");
                }
                if (iG[ 1 ] == IS_NEARLY_TRUE) {
                    vA.add("- Die Endung der Gattung ist beinahe richtig         ");
                }
                if (iS[ 0 ] == IS_NEARLY_TRUE) {
                    vA.add("- Das Art-Epithethon is beinahe richtig                 ");
                }
                if (iS[ 1 ] == IS_NEARLY_TRUE) {
                    vA.add("- Die Endung des Art-Epithethons ist beinahe richtig      ");
                }
                vA.add(" ");
                vA.add("Zum Vergleich:");
                vA.add("- " + correct + " (richtige Lösung)");
                vA.add("- " + guess + " (Ihr Vorschlag)");
                correctnessText = new ArrayList();
                correctnessText.add(" ");
                correctnessText.add("Sie sind nahe dran:                         ");
                correctnessText.addAll(vA);
                correctnessText.add(" ");
                correctnessText.add("Versuchen Sie es noch einmal...");
                correctnessText.add(" ");
                ret = IS_NEARLY_TRUE;
            }
            else {
                ret = IS_FALSE;
            }
        }
        else { // it's not a species
            boolean withinTolerance = mayBeTolerated.getEval(correct.toLowerCase(), guess.toLowerCase()).isPassed();
            boolean equal = mustBeEqual.getEval(correct.toLowerCase(), guess.toLowerCase()).isPassed();
            if (equal) {
                ret = IS_TRUE;
            }
            else if (withinTolerance) {
                correctnessText = new ArrayList();
                correctnessText.add(" ");
                correctnessText.add("Sie sind nahe dran:                         ");
                correctnessText.add("- Es befinden sich noch kleine Schreibfehler in  ");
                correctnessText.add("  Ihrem Vorschlag");
                correctnessText.add(" ");
                correctnessText.add("  " + correct + " (richtige Lösung)");
                correctnessText.add("  " + guess + " (Ihr Vorschlag)");
                correctnessText.add(" ");
                correctnessText.add("Versuchen Sie es noch einmal...");
                correctnessText.add(" ");
                ret = IS_NEARLY_TRUE;
            }
            else {
                ret = checkSynonyme(taxon, guess);
            }
        }
        return ret;
    }

    private Correctness checkSynonyme(Taxon taxon, String synString) {
        GraphNodeList sil = taxon.getAsGraphNode().getChildren(TaxonSynonym.class);
        EvaluationResult eval = EvaluationResult.EVAL_FAILED;
        for (int ix = 0; ix < sil.size() && !eval.isPassed(); ix++) {
            String strSyn = sil.get(ix).toString().toLowerCase();
            eval = mustBeEqual.getEval(strSyn, synString.toLowerCase());
        }
        if (eval.isPassed()) {
            correctnessText = new ArrayList();
            correctnessText.add("Sie verwenden ein Synonym:      ");
            correctnessText.add("  " + synString);
            correctnessText.add(" ");
            correctnessText.add("Wir verwenden jedoch:  ");
            correctnessText.add("  " + taxon);
            correctnessText.add(" ");
            return IS_NEARLY_TRUE;
        }
        return IS_FALSE;
    }

    private Correctness[] checkGenus(String correct, String guess) {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        int iReq = correct.indexOf(" ");
        int iFnd = guess.indexOf(" ");
        String strFound = "";
        if (iFnd < 0) {
            strFound = guess;
        }
        else {
            strFound = guess.substring(0, iFnd).trim();
        }
        String strReq = correct.substring(0, iReq).trim();
        return checkTaxon(strReq, strFound);
    }

    private Correctness[] checkEpithethon(String correct, String guess) {
        correct = correct.toLowerCase().trim();
        guess = guess.toLowerCase().trim();
        int iReq = correct.indexOf(" ");
        int iFnd = guess.indexOf(" ");
        String strFound = "";
        if (iFnd < 0) {
            strFound = guess;
        }
        else {
            strFound = guess.substring(iFnd + 1, guess.length()).trim();
        }
        String strReq = correct.substring(iReq + 1, correct.length()).trim();
        return checkTaxon(strReq, strFound);
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
    private Correctness[] checkTaxon(String correct, String guess) {
        // accept all species epitheta for taxa with "sp."
        if (correct.equals("sp.") || guess.equals(correct)) {
            return new Correctness[]{CorrectnessChecker.IS_TRUE, CorrectnessChecker.IS_TRUE};
        }
        // or check taxanames
        else {
            Correctness[] res = new Correctness[]{IS_FALSE, IS_FALSE};
            String correctBody = null, guessedBody = null, strKey,
                    guessPostfix = "", correctPostfix = "", toleratedEndings[];
            Iterator eVals = hsh.values().iterator();
            Iterator eKeys = hsh.keySet().iterator();
            while (eKeys.hasNext()) {
                strKey = (String) eKeys.next();
                toleratedEndings = (String[]) eVals.next();
                if (guess.endsWith(strKey)) {
                    if (correct.endsWith(strKey) && guess.length() == correct.length()) {
                        // both endings match, but bodies are different
                        res[ 1 ] = IS_TRUE;
                        guessPostfix = strKey;
                        correctPostfix = strKey;
                    }
                    else {
                        // bodies are different and endings MAY be tolerated. If endings have been determined for
                        // both strings, there bodies are extracted. Bodies then are compared.
                        for (int iX = 0; iX < toleratedEndings.length; iX++) {
                            String alternateEnding = toleratedEndings[ iX ];
                            if (guess.endsWith(alternateEnding)) {
                                guessPostfix = alternateEnding;
                            }
                            if (correct.endsWith(alternateEnding)) {
                                correctPostfix = alternateEnding;
                                res[ 1 ] = IS_NEARLY_TRUE;
                            }
                        }
                    }
                    correctBody = correct.substring(0, correct.lastIndexOf(correctPostfix));
                    guessedBody = guess.substring(0, guess.lastIndexOf(guessPostfix));
                    if (guessedBody == null) {
                        guessedBody = guess;
                    }
                    if (correctBody == null) {
                        correctBody = correct;
                    }
                    EvaluationResult strongEval = mustBeEqual.getEval(guessedBody, correctBody);
                    EvaluationResult tolerantEval = mayBeTolerated.getEval(guessedBody, correctBody);
                    if (strongEval.isPassed()) {
                        res[ 0 ] = IS_TRUE;
                    }
                    else if (tolerantEval.isPassed()) {
                        res[ 0 ] = IS_NEARLY_TRUE;
                    }
                    else {
                        res[ 0 ] = IS_FALSE;
                    }
                    return res;
                }
            }
            return res;
        }
    }

    public List getCorrectnessText() {
        return correctnessText;
    }

//    /**
//     * Tests each associated element of the current taxon. All morphological
//     * ecological and use associations are compared to the given item. This
//     * method calls <a href="#getCorrectness(com.ethz.geobot.herbarCD.model.TaxItem,
//     * com.ethz.geobot.herbarCD.model.ExtendedItem)"><code>getCorrectness()</code></a>
//     * with the current taxon (focus).
//     * @param  p_ei item searched for
//     * @return type of correctness
//     * @see    #getCorrectness(String, String)
//     * getCorrectness(TaxItem, ExtendedItem)
//     */
//    public Correctness getCorrectness(ExtendedItem p_ei) {
//        /* Syslog.info(this, "getCorrectness(" + p_ei + ")"); */
//        return getCorrectness(m_ms.getTaxFocus(), p_ei);
//    }
//
//    /**
//     * Tests each associated element of the given taxon. All morphological
//     * ecological and use associations are compared to the given item.
//     * @param  p_ei item searched for
//     * @param  p_tiFocus base to derive all associations to compare
//     * @return type of correctness
//     * @see    #getCorrectness(com.ethz.geobot.herbarCD.model.ExtendedItem)
//     * getCorrectness(ExtendedItem)
//     */
//    public Correctness getCorrectness(TaxItem p_tiFocus, ExtendedItem p_ei) {
//        /* Syslog.info(this, "getCorrectness(" + p_tiFocus + "," + p_ei + ")"); */
//        int i = 0;
//        Correctness cCorrect = IS_FALSE;
//        MorItemList mil = p_tiFocus.getMor();
//        for (i = 0; cCorrect != IS_TRUE && i < mil.count(); i++) {
//            if (mil.getMor(i) == p_ei) {
//                cCorrect = IS_TRUE;
//                break;
//            }
//            if (mil.getMor(i).getParent() == p_ei.getParent()) {
//                cCorrect = IS_NEARLY_TRUE;
//            }
//        }
//        AttItemList ail = p_tiFocus.getOek();
//        for (i = 0; cCorrect != IS_TRUE && i < ail.count(); i++) {
//            if (ail.getAtt(i) == p_ei) {
//                cCorrect = IS_TRUE;
//                break;
//            }
//            if (ail.getAtt(i).getParent() == p_ei.getParent()) {
//                cCorrect = IS_NEARLY_TRUE;
//            }
//        }
//        ail = p_tiFocus.getVerw();
//        for (i = 0; cCorrect != IS_TRUE && i < ail.count(); i++) {
//            if (ail.getAtt(i) == p_ei) {
//                cCorrect = IS_TRUE;
//                break;
//            }
//            if (ail.getAtt(i).getParent() == p_ei.getParent()) {
//                cCorrect = IS_NEARLY_TRUE;
//            }
//        }
//        return cCorrect;
//    }

    static {
        hsh = new HashMap();
        // make sure to have the shorter endings at the end...
        hsh.put("tans", new String[]{"ens", "tans"});
        hsh.put("ens", new String[]{"ens", "tans"});
        hsh.put("e", new String[]{"er", "is", "e"});
        hsh.put("is", new String[]{"er", "is", "e"});
        hsh.put("er", new String[]{"er", "is", "e"});
        hsh.put("um", new String[]{"um", "us", "a"});
        hsh.put("us", new String[]{"um", "us", "a"});
        hsh.put("a", new String[]{"um", "us", "a"});
        hsh.put("i", new String[]{"ii", "i"});
        hsh.put("ii", new String[]{"ii", "i"});

    }

    public static class Correctness {

        private int id;

        private Correctness(int newId) {
            id = newId;
        }

        public String toString() {
            if (id == 0) {
                return "RIGHT";
            }
            else if (id == 1) {
                return "NEAR";
            }
            else if (id == 2) {
                return "WRONG";
            }
            else {
                return "UNKNOWN";
            }
        }
    }

}