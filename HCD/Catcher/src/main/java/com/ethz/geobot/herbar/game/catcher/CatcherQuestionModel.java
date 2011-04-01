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
public class CatcherQuestionModel
{
    private int rightAnswers = 0;

    private final Vector allQuestions = new Vector();

    private final Vector selection = new Vector();

    private final HerbarModel model;

    private int countQuestions = 0;

    private final int amountQuestions;

    /**
     * Method CatcherQuestionModel. Constructor
     *
     * @param model instance of class Herbarmodel
     */
    public CatcherQuestionModel( final HerbarModel model )
    {
        amountQuestions = Integer.parseInt( Strings.getString( Catcher.class, "CATCHER.ANZAHLFRAGEN" ) );
        this.model = model;
    }

    /** puts the right answers in a hasmap of all right answers of this game */
    public void setRightAnswers()
    {
        rightAnswers = rightAnswers + 1;
    }

    /** puts the right answers in a hasmap of all right answers of this game */
    public boolean rightAnswersComplete()
    {
        return rightAnswers == amountQuestions;
    }

    /**
     * Randomly select an object of the allQuestions - vector, presumed it's not empty
     *
     * @return questionObject which serves as basis for the next question.
     */
    public QuestionDataUnit getQuestion()
    {
        if ( selection.size() != 0 )
        {
            QuestionDataUnit questionDataUnit = null;
            questionDataUnit = ( (QuestionDataUnit) selection.elementAt( 0 ) );
            selection.remove( 0 );
            setCountQuestions();
            return questionDataUnit;
        }
        else
        {
            return null;
        }
    }

    public boolean isFinished()
    {
        boolean finished = false;
        if ( getCountQuestions() == amountQuestions )
        {
            finished = true;
        }
        else if ( getCountQuestions() <= amountQuestions )
        {
            finished = false;
        }
        return finished;
    }

    private int getCountQuestions()
    {
        return countQuestions;
    }

    private void setCountQuestions()
    {
        countQuestions = countQuestions + 1;
    }

    private void initQuestions()
    {
        final Taxon tax = model.getRootTaxon();
        final Level lev = model.getLastLevel();
        // fill the array with all taxa on art -level
        final Taxon[] taxPool = tax.getAllChildTaxa( lev );
        // for each taxon get a vector with all possible ancestor taxons
        // remove the upper two ancestors (abteilung, klasse) from  the vector
        // create an QuestionDataUnit-object with each taxon/ancestroTax-pair
        // fill those objects in a vector
        for ( final Taxon aTaxPool : taxPool )
        {
            final Vector vectParentTaxa = getParentTaxon( aTaxPool );
            for ( int iy = 0; iy < vectParentTaxa.size(); iy++ )
            {
                final long idx = allQuestions.size();
                allQuestions.add( new QuestionDataUnit( idx, aTaxPool, ( (Taxon) vectParentTaxa.elementAt( iy ) ), false ) );
            }
        }
    }

    private void renewSelection()
    {
        if ( allQuestions.size() > 0 )
        {
            for ( int r = 0; r < amountQuestions; r++ )
            {
                final int randomSelect = (int) ( Math.random() * (double) allQuestions.size() );
                selection.add( allQuestions.elementAt( randomSelect ) );
            }
        }
    }

    /**
     * fills the vector vectParentTaxa with all parenttaxa above the selected taxon on art-level
     *
     * @param taxon selected taxon upon which the next question is generated
     * @return vector which contains all ancestor taxons of taxon
     */
    public Vector getParentTaxon( final Taxon taxon )
    {
        final Vector vectParentTaxa = new Vector();
        getParentTaxon( vectParentTaxa, taxon );
        return vectParentTaxa;
    }

    public void getParentTaxon( final Vector vectParentTaxa, final Taxon taxon )
    {
        if ( taxon.getLevel().isLower( model.getLevel( "Ordnung" ) ) )
        {
            getParentTaxon( vectParentTaxa, taxon.getParentTaxon() );
        }
        vectParentTaxa.add( taxon );
    }

    /** fill a vector with objects of all taxon/ancestorTacon-pairs possible */
    public void initializeDataPool()
    {
        allQuestions.removeAllElements();
        initQuestions();
    }

    public void reset()
    {
        selection.removeAllElements();
        rightAnswers = 0;
        countQuestions = 0;
        renewSelection();
    }
}
