/*
 * Herbar CD-ROM version 2
 *
 * CatcherQuestionModel.java
 *
 * Created on 17. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Vector;

/**
 * the question-model delivers the new taxon as basis for the questions.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class CatcherQuestionModel {

    private int rightAnswers = 0;
    private Vector allQuestions = new Vector();
    private Vector selection = new Vector();
    private HerbarModel model;
    private int countQuestions = 0;
    private int amountQuestions;


    /**
     * Method CatcherQuestionModel. Constructor
     *
     * @param model instance of class Herbarmodel
     */
    public CatcherQuestionModel(HerbarModel model) {
        amountQuestions = Integer.parseInt(Strings.getString(Catcher.class, "CATCHER.ANZAHLFRAGEN"));
        this.model = model;
    }

    /**
     * puts the right answers in a hasmap of all right answers of this game
     */
    public void setRightAnswers() {
        rightAnswers = rightAnswers + 1;
    }


    /**
     * puts the right answers in a hasmap of all right answers of this game
     */
    public boolean rightAnswersComplete() {
        return rightAnswers == amountQuestions;
    }

    /**
     * Randomly select an object of the allQuestions - vector, presumed it's not empty
     *
     * @return questionObject which serves as basis for the next question.
     */
    public QuestionDataUnit getQuestion() {
        if (selection.size() != 0) {
            QuestionDataUnit questionDataUnit = null;
            questionDataUnit = ((QuestionDataUnit) selection.elementAt(0));
            selection.remove(0);
            setCountQuestions();
            return questionDataUnit;
        }
        else {
            return null;
        }
    }


    public boolean isFinished() {
        boolean finished = false;
        if (getCountQuestions() == amountQuestions) {
            finished = true;
        }
        else if (getCountQuestions() <= amountQuestions) {
            finished = false;
        }
        return finished;
    }

    private int getCountQuestions() {
        return countQuestions;
    }

    private void setCountQuestions() {
        countQuestions = countQuestions + 1;
    }

    private void initQuestions() {
        Taxon tax = model.getRootTaxon();
        Level lev = model.getLastLevel();
        // fill the array with all taxa on art -level
        Taxon[] taxPool = tax.getAllChildTaxa(lev);
        // for each taxon get a vector with all possible ancestor taxons
        // remove the upper two ancestors (abteilung, klasse) from  the vector
        // create an QuestionDataUnit-object with each taxon/ancestroTax-pair
        // fill those objects in a vector
        for (int i = 0; i < taxPool.length; i++) {
            Vector vectParentTaxa = getParentTaxon(taxPool[ i ]);
            for (int iy = 0; iy < vectParentTaxa.size(); iy++) {
                long idx = allQuestions.size();
                allQuestions.add(new QuestionDataUnit(idx, taxPool[ i ], ((Taxon) vectParentTaxa.elementAt(iy)), false));
            }
        }
    }

    private void renewSelection() {
        if (allQuestions.size() > 0) {
            for (int r = 0; r < amountQuestions; r++) {
                int randomSelect = (int) (Math.random() * (double) allQuestions.size());
                selection.add(allQuestions.elementAt(randomSelect));
            }
        }
    }

    /**
     * fills the vector vectParentTaxa with all parenttaxa above the selected taxon on art-level
     *
     * @param taxon selected taxon upon which the next question is generated
     * @return vector which contains all ancestor taxons of taxon
     */
    public Vector getParentTaxon(Taxon taxon) {
        Vector vectParentTaxa = new Vector();
        getParentTaxon(vectParentTaxa, taxon);
        return vectParentTaxa;
    }

    public void getParentTaxon(Vector vectParentTaxa, Taxon taxon) {
        if (taxon.getLevel().isLower(model.getLevel("Ordnung"))) {
            getParentTaxon(vectParentTaxa, taxon.getParentTaxon());
        }
        vectParentTaxa.add(taxon);
    }

    /**
     * fill a vector with objects of all taxon/ancestorTacon-pairs possible
     */
    public void initializeDataPool() {
        allQuestions.removeAllElements();
        initQuestions();
    }

    public void reset() {
        selection.removeAllElements();
        rightAnswers = 0;
        countQuestions = 0;
        renewSelection();
    }
}
