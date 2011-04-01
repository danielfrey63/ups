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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mainclass of oneoffive-game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class OneOfFiveQuestionModel
{
    private static final Logger LOG = LoggerFactory.getLogger( OneOfFiveQuestionModel.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    /** Amount of taxa displayed to select one which doesnt fit */
    private static final int NUMBER_OF_RIGHT_TAXA = 4;

    private static final int NUMBER_OF_WRONG_TAXA = 1;

    private final HerbarModel model;

    /** Contains all possible combinations of taxa in all valid levels, that are suitable for the game with a fixed number of taxa. */
    private final List allCombis = new ArrayList();

    // vectors which control and evaluate the wrong, false, and total questions

    private final List<QuestionDataUnit> wrongAnswers = new ArrayList<QuestionDataUnit>();

    private final List<QuestionDataUnit> rightAnswers = new ArrayList<QuestionDataUnit>();

    private final List allQuestions = new ArrayList();

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
    public OneOfFiveQuestionModel( final HerbarModel model )
    {
        this.model = model;
        initializeDataModel();
    }

    /**
     * puts the right answers in a hasmap of all right answers of this game
     *
     * @param questionDataUnit contains the taxon/ancestorTaxon - pair
     */
    public void setRightAnswers( final QuestionDataUnit questionDataUnit )
    {
        this.rightAnswers.add( questionDataUnit );
        this.allQuestions.remove( questionDataUnit );
    }

    /**
     * puts the wrong answers in a vector of all wrong answers of this game
     *
     * @param questionDataUnit contains the taxon/ancestorTaxon - pair
     */
    public void setWrongAnswers( final QuestionDataUnit questionDataUnit )
    {
        this.wrongAnswers.add( questionDataUnit );
    }

    /**
     * select randomly one QuestionDataUnit of allCombis
     *
     * @return object containing new taxons and the position of wrong taxon
     */
    public QuestionDataUnit getTaxon()
    {
        if ( allQuestions.size() != 0 )
        {
            final int rand = (int) ( Math.random() * allQuestions.size() );
            return (QuestionDataUnit) allQuestions.get( rand );
        }
        else
        {
            return null;
        }
    }

    /**
     * build the crossproducts between the right and the wrong subparent taxa. then build the crossproduct of the taxa on childLevel under these subparents. finally pack all combinations of right and wrong taxa in form of QuestionDataUnit-objects in allCombis. dependent of the actual game scope the cominatoric deepness is set to get always around 100 questions.
     *
     * @param childLevel child level
     */
    private void getCombinations( final Level childLevel )
    {
        final List<Taxon> wrongParents = wrongChildLevelParentPool.get( childLevel );
        final List<Taxon> rightParents = rightChildLevelParentPool.get( childLevel );
        if ( rightParents != null && wrongParents != null )
        {
            for ( final Taxon wrongParent : wrongParents )
            {
                for ( final Taxon rightParent : rightParents )
                {
                    if ( wrongParent.getLevel() == rightParent.getLevel() && rightParent != wrongParent )
                    {
                        final List<Taxon> wrongSubParents = wrongParentSubparentPool.get( wrongParent );
                        final List<Taxon> rightSubParents = rightParentSubparentPool.get( rightParent );
                        final List<? extends List<Taxon>> wrongSubParVar = Combinatorial.getSubsets( wrongSubParents, NUMBER_OF_WRONG_TAXA );
                        final List<? extends List<Taxon>> rightSubParVar = Combinatorial.getSubsets( rightSubParents, NUMBER_OF_RIGHT_TAXA );
                        final Object[][] cpSubParents = Combinatorial.getCrossProduct( new Object[][]{wrongSubParVar.toArray(), rightSubParVar.toArray()}, 100 );

                        for ( final Object[] wrongAndRightList : cpSubParents )
                        {
                            final Object[][] children = new Object[NUMBER_OF_WRONG_TAXA + NUMBER_OF_RIGHT_TAXA][];
                            int count = 0;
                            for ( final Object aWrongAndRightList : wrongAndRightList )
                            {
                                final List wrongOrRightSubList = (List) aWrongAndRightList;
                                for ( final Object aWrongOrRightSubList : wrongOrRightSubList )
                                {
                                    final Object[] childs = subparentChildrenPool.get( aWrongOrRightSubList );
                                    children[count++] = childs;
                                }
                            }
                            final Object[][] cpChildren = Combinatorial.getCrossProduct( children, 1 );
                            final List newUnit = new ArrayList();
                            for ( final Object[] wrongAndRightTaxa : cpChildren )
                            {
                                for ( final Object aWrongAndRightTaxa : wrongAndRightTaxa )
                                {
                                    final Taxon wrongOrRightTaxon = (Taxon) aWrongAndRightTaxa;
                                    if ( DEBUG )
                                    {
                                        LOG.trace( "tax: " + wrongOrRightTaxon + ", Wparent: " + wrongParent +
                                                ", Rparent: " + rightParent );
                                    }
                                    newUnit.add( wrongOrRightTaxon );
                                }
                            }
                            allCombis.add( newUnit );
                        }
                    }
                }
            }
        }
    }

    public void initializeDataModel()
    {
        rightAnswers.removeAll( rightAnswers );
        Level level = model.getRootLevel();
        // preselect all potential taxa for the next game
        final Taxon root = model.getRootTaxon();
        while ( level != null )
        {
            // call recursive function which builds the two HashMaps, one
            // for all right and one for all wrong parent taxa. The function
            // uses two global hashmaps to store the parents.
            // fill in the hashmaps
            wrongChildLevelParentPool = new HashMap<Level, List<Taxon>>();
            wrongParentSubparentPool = new HashMap<Taxon, List<Taxon>>();
            rightChildLevelParentPool = new HashMap<Level, List<Taxon>>();
            rightParentSubparentPool = new HashMap<Taxon, List<Taxon>>();

            subparentChildrenPool = new HashMap<Taxon, Taxon[]>();
            fillHashMaps( root, level );
            getCombinations( level );

            wrongChildLevelParentPool = null;
            wrongParentSubparentPool = null;
            rightChildLevelParentPool = null;
            rightParentSubparentPool = null;
            subparentChildrenPool = null;

            level = level.getChildLevel();
        }
        LOG.debug( "allCombis: " + allCombis.size() );
    }

    /**
     * Generate all possible questions for the given scope of the actual game
     *
     * @param gameSize number of questions
     */
    public void fillAllQuestions( final int gameSize )
    {
        RandomUtils.randomize( allCombis );
        for ( int i = 0; DEBUG && i < gameSize; i++ )
        {
            LOG.debug( "taxa: " + allCombis.get( i ) );
        }
        allQuestions.removeAll( allQuestions );
        for ( int i = 0; i < gameSize; i++ )
        {
            allQuestions.add( new QuestionDataUnit( (List) allCombis.get( i ), 0 ) );
        }
        if ( DEBUG )
        {
            LOG.debug( "allQuestions: " + allQuestions.size() );
            LOG.debug( "gameSize: " + gameSize );
        }
    }

    /**
     * The datamodel (5 dataPools in HashMaps) is filled in. a parentTax is a valid parent if it has at least one child at the childLevel and at least NUMBER_OF_RIGHT_TAXA subparent taxa.
     *
     * @param parentTax  parent taxon
     * @param childLevel child level
     */
    private void fillHashMaps( final Taxon parentTax, final Level childLevel )
    {
        // It is no good to dig deeper if the level of the current taxon
        // is already lower or equal than the level played on.
        // Note: root level is null.
//        Level parentLevel = parentTax.getLevel();
//        if (childLevel.isLower(parentLevel) || childLevel == parentLevel) {
//            return;
//        }

        // recursive method call with same childLevel and the taxa sub parentTax
        for ( int i = 0; i < parentTax.getChildTaxa().length; i++ )
        {
            fillHashMaps( parentTax.getChildTaxon( i ), childLevel );
        }

        // iterate all subParents of parentTax, getAllChildTaxa on childLevel.
        // if the children taxa array is empty, its subparent is removed,
        // otherwise it is put in the common HasMap subparentChildrenPool
        final List<Taxon> subParents = new ArrayList<Taxon>( Arrays.asList( parentTax.getChildTaxa() ) );
        for ( int i = subParents.size() - 1; i >= 0; i-- )
        {
            final Taxon subParent = subParents.get( i );
            final Taxon[] children = subParent.getAllChildTaxa( childLevel );
            if ( children.length == 0 )
            {
                subParents.remove( i );
            }
            else
            {
                subparentChildrenPool.put( subParent, children );
            }
        }

        // if the number of subParents is at least NUMBER_OF_WRONG_TAXA,
        // NUMBER_OF_RIGHT_TAXA respectively this subparents is put in HashMap
        // wrongParentSubparentPool, rightParentSubparentPool respectively,
        // and the parentTax in the HasMap wrongChildLevelParentPool,
        // rightChildLevelParentPool respectively.
        List<Taxon> vect = null;
        if ( subParents.size() >= NUMBER_OF_WRONG_TAXA )
        {
            if ( DEBUG )
            {
                LOG.debug( "fill wrong: " + parentTax.getName() + " " + subParents + " " + childLevel );
            }
            wrongParentSubparentPool.put( parentTax, subParents );
            vect = wrongChildLevelParentPool.get( childLevel );
            if ( vect == null )
            {
                vect = new ArrayList<Taxon>();
                wrongChildLevelParentPool.put( childLevel, vect );
            }
            vect.add( parentTax );
        }
        if ( subParents.size() >= NUMBER_OF_RIGHT_TAXA )
        {
            if ( DEBUG )
            {
                LOG.debug( "fill right: " + parentTax.getName() + " " + subParents + " " + childLevel );
            }
            rightParentSubparentPool.put( parentTax, subParents );
            vect = rightChildLevelParentPool.get( childLevel );
            if ( vect == null )
            {
                vect = new ArrayList<Taxon>();
                rightChildLevelParentPool.put( childLevel, vect );
            }
            vect.add( parentTax );
        }
    }
}
