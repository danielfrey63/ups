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
public class QuestionDataUnit
{
    private List taxItem;

    private int wrongTaxIndex;

    private final Taxon wrongTax;

    /**
     * Constructor
     *
     * @param taxItem   contains the Taxa of this question
     * @param randomAdd defines the position of the wrong Taxon
     */
    public QuestionDataUnit( final List taxItem, final int randomAdd )
    {
        this.taxItem = taxItem;
        wrongTax = (Taxon) taxItem.get( randomAdd );
    }

    /**
     * returns the five taxa for next question
     *
     * @return vector with 5 taxa
     */
    public List getTaxItem()
    {
        return taxItem;
    }

    /**
     * returns the random position of the wrong taxon
     *
     * @return position in vector
     */
    public int getRandomAdd()
    {
        return wrongTaxIndex;
    }

    /** puts the wrong taxon to a new random position within the vector. */
    public void randomize()
    {
        final Object[] taxArray = taxItem.toArray();
        RandomUtils.randomize( taxArray );
        taxItem = new ArrayList( Arrays.asList( taxArray ) );
        for ( int i = 0; i < taxItem.size(); i++ )
        {
            if ( taxItem.get( i ) == wrongTax )
            {
                wrongTaxIndex = i;
            }
        }

    }
}
