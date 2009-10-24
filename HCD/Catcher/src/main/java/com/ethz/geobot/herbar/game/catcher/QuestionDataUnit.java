/*
 * Herbar CD-ROM version 2
 *
 * QuestionDataUnit.java
 *
 * Created on 18. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import com.ethz.geobot.herbar.model.Taxon;

/**
 * represents a question containing a pair of taxon / ancestorTaxon.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class QuestionDataUnit {

    private Taxon taxon;
    private Taxon parentTaxon;
    private boolean lastQuestion;
    private long idx;

    /**
     * Method QuestionDataUnit. Constructor
     *
     * @param taxon        sets the taxon of thies questionUnit
     * @param parentTaxon  sets the parenttaxon of thies questionUnit
     * @param lastQuestion says if there are more questions
     */
    public QuestionDataUnit(long idx, Taxon taxon, Taxon parentTaxon, boolean lastQuestion) {
        this.taxon = taxon;
        this.parentTaxon = parentTaxon;
        this.lastQuestion = lastQuestion;
        this.idx = idx;
    }

    public long getID() {
        return idx;
    }

    /**
     * sets if this Unit ist the last in the allQuestionVetor of the model
     *
     * @param last or not last question
     */
    public void setLastQestion(boolean last) {
        this.lastQuestion = last;
    }

    /**
     * returns the taxon on art-level
     *
     * @return the taxon on art-level
     */
    public Taxon getTaxon() {
        return this.taxon;
    }

    /**
     * returns the ancestorTaxon on art-level
     *
     * @return the ancestorTaxon of one of the ancestor taxons
     */
    public Taxon getParentTaxon() {
        return this.parentTaxon;
    }

    /**
     * returns if this Unit ist the last in the allQuestionVetor of the model
     *
     * @return last or not last question
     */
    public boolean getLastQestion() {
        return this.lastQuestion;
    }
}
