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

import com.ethz.geobot.herbar.model.Taxon;

/**
 * represents a question containing a pair of taxon / ancestorTaxon.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class QuestionDataUnit
{
    private final Taxon taxon;

    private final Taxon parentTaxon;

    private boolean lastQuestion;

    private final long idx;

    /**
     * Method QuestionDataUnit. Constructor
     *
     * @param taxon        sets the taxon of thies questionUnit
     * @param parentTaxon  sets the parenttaxon of thies questionUnit
     * @param lastQuestion says if there are more questions
     */
    public QuestionDataUnit( final long idx, final Taxon taxon, final Taxon parentTaxon, final boolean lastQuestion )
    {
        this.taxon = taxon;
        this.parentTaxon = parentTaxon;
        this.lastQuestion = lastQuestion;
        this.idx = idx;
    }

    public long getID()
    {
        return idx;
    }

    /**
     * sets if this Unit ist the last in the allQuestionVetor of the model
     *
     * @param last or not last question
     */
    public void setLastQestion( final boolean last )
    {
        this.lastQuestion = last;
    }

    /**
     * returns the taxon on art-level
     *
     * @return the taxon on art-level
     */
    public Taxon getTaxon()
    {
        return this.taxon;
    }

    /**
     * returns the ancestorTaxon on art-level
     *
     * @return the ancestorTaxon of one of the ancestor taxons
     */
    public Taxon getParentTaxon()
    {
        return this.parentTaxon;
    }

    /**
     * returns if this Unit ist the last in the allQuestionVetor of the model
     *
     * @return last or not last question
     */
    public boolean getLastQestion()
    {
        return this.lastQuestion;
    }
}
