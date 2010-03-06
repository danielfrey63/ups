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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * questionModel which serves the questionPanel with taxons upon which new questions are generated.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class QuestionModel
{
    private static final Logger LOG = LoggerFactory.getLogger( QuestionModel.class );

    private final Vector taxPool = new Vector();

    private final Vector choosenTaxPool = new Vector();

    private final Vector rightAnswers = new Vector();

    private final Vector wrongAnswers = new Vector();

    private final HerbarModel model;

    private final CountScore countScore;

    /**
     * Constructor
     *
     * @param countScore instance of herbarmodel
     * @param mode       instance of countScore
     */
    public QuestionModel( final CountScore countScore, final HerbarModel mode )
    {
        this.countScore = countScore;
        this.model = mode;
    }

    /**
     * puts the right answers in a vector of all right answers of this game
     *
     * @param taxon selected taxon of the right answerd question
     */
    public void setRightAnswers( final Taxon taxon )
    {
        this.rightAnswers.add( taxon );
        LOG.debug( "right answers: " + taxon.getName() );
    }

    /**
     * puts the wrong answers in a vector of all wrong answers of this game
     *
     * @param taxon selected taxon of the wrong answerd question
     */
    public void setWrongAnswers( final Taxon taxon )
    {
        this.wrongAnswers.add( taxon );
        LOG.debug( "wrong answers: " + taxon.getName() );
    }

    /**
     * returns a taxon for the next question
     *
     * @return Taxon
     */
    public Taxon getTaxon()
    {
        final int rand = (int) ( Math.random() * taxPool.size() );
        final Taxon taxon = (Taxon) taxPool.elementAt( rand );
        taxPool.remove( taxon );
        choosenTaxPool.add( taxon );
        return taxon;
    }

    /**
     * initialization; clears vectors and initializes a new datapool
     */
    public void init()
    {
        taxPool.removeAllElements();
        choosenTaxPool.removeAllElements();
        initializeDataPool();
    }

    /**
     * Prepare a pool of all taxa at the begin of a game.
     */
    public void initializeDataPool()
    {
        final Taxon tax = model.getRootTaxon();
        //     Taxon tax = model.getTaxon( "Liliales" );
        final Level lev = model.getLastLevel();
        final Taxon[] taxArray = tax.getAllChildTaxa( lev );
        for ( final Taxon aTaxArray : taxArray )
        {
            taxPool.add( aTaxArray );
        }
    }
}
