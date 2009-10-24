/*
 * Herbar CD-ROM version 2
 *
 * OneOfFive.java
 *
 * Created on 11. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import ch.jfactory.math.Combinatorial;
import ch.jfactory.math.RandomUtils;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * mainclass of oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class OneOfFiveQuestionModel {

    private static final Category cat = Category.getInstance(OneOfFiveQuestionModel.class);
    private static final boolean DEBUG = cat.isDebugEnabled();

    /**
     * Amount of taxa displayed to select one which doesnt fit
     */
    private static final int NUMBER_OF_RIGHT_TAXA = 4;
    private static final int NUMBER_OF_WRONG_TAXA = 1;
    private HerbarModel model;

    /**
     * Contains all possible combinations of taxa in all valid levels, that are suitable for the game with a fixed
     * number of taxa.
     */
    private List allCombis = new ArrayList();

    // vectors which control and evaluate the wrong, false, and total questions
    private List<QuestionDataUnit> wrongAnswers = new ArrayList<QuestionDataUnit>();
    private List<QuestionDataUnit> rightAnswers = new ArrayList<QuestionDataUnit>();
    private List allQuestions = new ArrayList();

    private Map<Level, List<Taxon>> wrongChildLevelParentPool;
    private Map<Taxon, List<Taxon>> wrongParentSubparentPool;

    private Map<Level, List<Taxon>> rightChildLevelParentPool;
    private Map<Taxon, List<Taxon>> rightParentSubparentPool;

    private Map<Taxon, Taxon[]> subparentChildrenPool;

    /**
     * Constructor for the OneOfFive object
     *
     * @param model Herbar model
     */
    public OneOfFiveQuestionModel(HerbarModel model) {
        this.model = model;
        initializeDataModel();
    }

    /**
     * puts the right answers in a hasmap of all right answers of this game
     *
     * @param questionDataUnit contains the taxon/ancestorTaxon - pair
     */
    public void setRightAnswers(QuestionDataUnit questionDataUnit) {
        this.rightAnswers.add(questionDataUnit);
        this.allQuestions.remove(questionDataUnit);
    }

    /**
     * puts the wrong answers in a vector of all wrong answers of this game
     *
     * @param questionDataUnit contains the taxon/ancestorTaxon - pair
     */
    public void setWrongAnswers(QuestionDataUnit questionDataUnit) {
        this.wrongAnswers.add(questionDataUnit);
    }

    /**
     * select randomly one QuestionDataUnit of allCombis
     *
     * @return object containing new taxons and the position of wrong taxon
     */
    public QuestionDataUnit getTaxon() {
        if (allQuestions.size() != 0) {
            int rand = (int) (Math.random() * allQuestions.size());
            return (QuestionDataUnit) allQuestions.get(rand);
        }
        else {
            return null;
        }
    }

    /**
     * build the crossproducts between the right and the wrong subparent taxa. then build the crossproduct of the taxa
     * on childLevel under these subparents. finally pack all combinations of right and wrong taxa in form of
     * QuestionDataUnit-objects in allCombis. dependent of the actual game scope the cominatoric deepness is set to get
     * always around 100 questions.
     *
     * @param childLevel child level
     */
    private void getCombinations(Level childLevel) {
        List<Taxon> wrongParents = wrongChildLevelParentPool.get(childLevel);
        List<Taxon> rightParents = rightChildLevelParentPool.get(childLevel);
        if (rightParents != null && wrongParents != null) {
            for (int i = 0; i < wrongParents.size(); i++) {
                Taxon wrongParent = wrongParents.get(i);
                for (int j = 0; j < rightParents.size(); j++) {
                    Taxon rightParent = rightParents.get(j);

                    if (wrongParent.getLevel() == rightParent.getLevel() && rightParent != wrongParent) {
                        List<Taxon> wrongSubParents = wrongParentSubparentPool.get(wrongParent);
                        List<Taxon> rightSubParents = rightParentSubparentPool.get(rightParent);
                        List<? extends List<Taxon>> wrongSubParVar = Combinatorial.getSubsets(wrongSubParents, NUMBER_OF_WRONG_TAXA);
                        List<? extends List<Taxon>> rightSubParVar = Combinatorial.getSubsets(rightSubParents, NUMBER_OF_RIGHT_TAXA);
                        Object[][] cpSubParents = Combinatorial.getCrossProduct(new Object[][]{wrongSubParVar.toArray(), rightSubParVar.toArray()}, 100);

                        for (int k = 0; k < cpSubParents.length; k++) {
                            Object[] wrongAndRightList = cpSubParents[ k ];
                            Object[][] children = new Object[NUMBER_OF_WRONG_TAXA + NUMBER_OF_RIGHT_TAXA][];
                            int count = 0;
                            for (int m = 0; m < wrongAndRightList.length; m++) {
                                List wrongOrRightSubList = (List) wrongAndRightList[ m ];
                                for (int n = 0; n < wrongOrRightSubList.size(); n++) {
                                    Object[] childs = subparentChildrenPool.get(wrongOrRightSubList.get(n));
                                    children[ count++ ] = childs;
                                }
                            }
                            Object[][] cpChildren = Combinatorial.getCrossProduct(children, 1);
                            List newUnit = new ArrayList();
                            for (int d = 0; d < cpChildren.length; d++) {
                                Object[] wrongAndRightTaxa = cpChildren[ d ];
                                for (int e = 0; e < wrongAndRightTaxa.length; e++) {
                                    Taxon wrongOrRightTaxon = (Taxon) wrongAndRightTaxa[ e ];
                                    if (DEBUG) {
                                        cat.debug("tax: " + wrongOrRightTaxon + ", Wparent: " + wrongParent +
                                                ", Rparent: " + rightParent);
                                    }
                                    newUnit.add(wrongOrRightTaxon);
                                }
                            }
                            allCombis.add(newUnit);
                        }
                    }
                }
            }
        }
    }

    public void initializeDataModel() {
        rightAnswers.removeAll(rightAnswers);
        Level level = model.getRootLevel();
        // preselect all potential taxa for the next game
        Taxon root = model.getRootTaxon();
        while (level != null) {

            // call recursive function which builds the two HashMaps, one
            // for all right and one for all wrong parent taxa. The function
            // uses two global hashmaps to store the parents.
            // fill in the hashmaps
            wrongChildLevelParentPool = new HashMap<Level, List<Taxon>>();
            wrongParentSubparentPool = new HashMap<Taxon, List<Taxon>>();
            rightChildLevelParentPool = new HashMap<Level, List<Taxon>>();
            rightParentSubparentPool = new HashMap<Taxon, List<Taxon>>();

            subparentChildrenPool = new HashMap<Taxon, Taxon[]>();
            fillHashMaps(root, level);
            getCombinations(level);

            wrongChildLevelParentPool = null;
            wrongParentSubparentPool = null;
            rightChildLevelParentPool = null;
            rightParentSubparentPool = null;
            subparentChildrenPool = null;

            level = level.getChildLevel();
        }
        cat.debug("allCombis: " + allCombis.size());
    }

    /**
     * Generate all possible questions for the given scope of the actual game
     *
     * @param gameSize number of questions
     */
    public void fillAllQuestions(int gameSize) {
        RandomUtils.randomize(allCombis);
        for (int i = 0; DEBUG && i < gameSize; i++) {
            cat.debug("taxa: " + allCombis.get(i));
        }
        allQuestions.removeAll(allQuestions);
        for (int i = 0; i < gameSize; i++) {
            allQuestions.add(new QuestionDataUnit((List) allCombis.get(i), 0));
        }
        if (DEBUG) {
            cat.debug("allQuestions: " + allQuestions.size());
            cat.debug("gameSize: " + gameSize);
        }
    }

    /**
     * The datamodel (5 dataPools in HashMaps) is filled in. a parentTax is a valid parent if it has at least one child
     * at the childLevel and at least NUMBER_OF_RIGHT_TAXA subparent taxa.
     *
     * @param parentTax  parent taxon
     * @param childLevel child level
     */
    private void fillHashMaps(Taxon parentTax, Level childLevel) {

        // It is no good to dig deeper if the level of the current taxon
        // is already lower or equal than the level played on.
        // Note: root level is null.
//        Level parentLevel = parentTax.getLevel();
//        if (childLevel.isLower(parentLevel) || childLevel == parentLevel) {
//            return;
//        }

        // recursive method call with same childLevel and the taxa sub parentTax
        for (int i = 0; i < parentTax.getChildTaxa().length; i++) {
            fillHashMaps(parentTax.getChildTaxon(i), childLevel);
        }

        // iterate all subParents of parentTax, getAllChildTaxa on childLevel.
        // if the children taxa array is empty, its subparent is removed,
        // otherwise it is put in the common HasMap subparentChildrenPool
        List<Taxon> subParents = new ArrayList<Taxon>(Arrays.asList(parentTax.getChildTaxa()));
        for (int i = subParents.size() - 1; i >= 0; i--) {
            Taxon subParent = subParents.get(i);
            Taxon[] children = subParent.getAllChildTaxa(childLevel);
            if (children.length == 0) {
                subParents.remove(i);
            }
            else {
                subparentChildrenPool.put(subParent, children);
            }
        }

        // if the number of subParents is at least NUMBER_OF_WRONG_TAXA,
        // NUMBER_OF_RIGHT_TAXA respectively this subparents is put in HashMap
        // wrongParentSubparentPool, rightParentSubparentPool respectively,
        // and the parentTax in the HasMap wrongChildLevelParentPool,
        // rightChildLevelParentPool respectively.
        List<Taxon> vect = null;
        if (subParents.size() >= NUMBER_OF_WRONG_TAXA) {
            if (DEBUG) {
                cat.debug("fill wrong: " + parentTax.getName() + " " + subParents + " " + childLevel);
            }
            wrongParentSubparentPool.put(parentTax, subParents);
            vect = wrongChildLevelParentPool.get(childLevel);
            if (vect == null) {
                vect = new ArrayList<Taxon>();
                wrongChildLevelParentPool.put(childLevel, vect);
            }
            vect.add(parentTax);
        }
        if (subParents.size() >= NUMBER_OF_RIGHT_TAXA) {
            if (DEBUG) {
                cat.debug("fill right: " + parentTax.getName() + " " + subParents + " " + childLevel);
            }
            rightParentSubparentPool.put(parentTax, subParents);
            vect = rightChildLevelParentPool.get(childLevel);
            if (vect == null) {
                vect = new ArrayList<Taxon>();
                rightChildLevelParentPool.put(childLevel, vect);
            }
            vect.add(parentTax);
        }
    }
}
