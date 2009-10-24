/*
 * Herbar CD-ROM version 2
 *
 * QuestionModel.java
 *
 * Created on 17. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import com.ethz.geobot.herbar.game.util.CountScore;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Vector;
import org.apache.log4j.Category;

/**
 * questionModel which serves the questionPanel with taxons upon which new questions are generated.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class QuestionModel {

    private static final Category CAT = Category.getInstance(
            QuestionModel.class);
    private Vector taxPool = new Vector();
    private Vector choosenTaxPool = new Vector();
    private Vector rightAnswers = new Vector();
    private Vector wrongAnswers = new Vector();
    private HerbarModel model;
    private CountScore countScore;


    /**
     * Constructor
     *
     * @param countScore instance of herbarmodel
     * @param mode       instance of countScore
     */
    public QuestionModel(CountScore countScore, HerbarModel mode) {
        this.countScore = countScore;
        this.model = mode;
    }

    /**
     * puts the right answers in a vector of all right answers of this game
     *
     * @param taxon selected taxon of the right answerd question
     */
    public void setRightAnswers(Taxon taxon) {
        this.rightAnswers.add(taxon);
        CAT.debug("right answers: " + taxon.getName());
    }

    /**
     * puts the wrong answers in a vector of all wrong answers of this game
     *
     * @param taxon selected taxon of the wrong answerd question
     */
    public void setWrongAnswers(Taxon taxon) {
        this.wrongAnswers.add(taxon);
        CAT.debug("wrong answers: " + taxon.getName());
    }

    /**
     * returns a taxon for the next question
     *
     * @return Taxon
     */
    public Taxon getTaxon() {
        int rand = (int) (Math.random() * taxPool.size());
        Taxon taxon = (Taxon) taxPool.elementAt(rand);
        taxPool.remove(taxon);
        choosenTaxPool.add(taxon);
        return taxon;
    }

    /**
     * initialization; clears vectors and initializes a new datapool
     */
    public void init() {
        taxPool.removeAllElements();
        choosenTaxPool.removeAllElements();
        initializeDataPool();
    }

    /**
     * Prepare a pool of all taxa at the begin of a game.
     */
    public void initializeDataPool() {
        Taxon tax = model.getRootTaxon();
        //     Taxon tax = model.getTaxon( "Liliales" );
        Level lev = model.getLastLevel();
        Taxon[] taxArray = tax.getAllChildTaxa(lev);
        for (int i = 0; i < taxArray.length; i++) {
            taxPool.add(taxArray[ i ]);
        }
    }
}
