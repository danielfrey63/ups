/*
 * Herbar CD-ROM version 2
 *
 * QuestionDataUnit.java
 *
 * Created on 25. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * represents a question containing a pair of taxon-vector / random position.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class QuestionDataUnit {

    private List taxItem;
    private int wrongTaxIndex;
    private Taxon wrongTax;

    /**
     * Constructor
     *
     * @param taxItem   contains the Taxa of this question
     * @param randomAdd defines the position of the wrong Taxon
     */
    public QuestionDataUnit(List taxItem, int randomAdd) {
        this.taxItem = taxItem;
        wrongTax = (Taxon) taxItem.get(randomAdd);
    }

    /**
     * returns the five taxa for next question
     *
     * @return vector with 5 taxa
     */
    public List getTaxItem() {
        return taxItem;
    }

    /**
     * returns the random position of the wrong taxon
     *
     * @return position in vector
     */
    public int getRandomAdd() {
        return wrongTaxIndex;
    }

    /**
     * puts the wrong taxon to a new random position within the vector.
     */
    public void randomize() {
        Object[] taxArray = taxItem.toArray();
        RandomUtils.randomize(taxArray);
        taxItem = new ArrayList(Arrays.asList(taxArray));
        for (int i = 0; i < taxItem.size(); i++) {
            if ((Taxon) taxItem.get(i) == wrongTax) {
                wrongTaxIndex = i;
            }
        }

    }
}
