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
 * MorValue.java
 *
 * Created on 23.5.2002
 * Created by Dirk Hoffmann
 */
package com.ethz.geobot.herbar.model.trait;

import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface MorphologyValue
{
    /**
     * Returns the id of this Taxon object, which should be unique in the current applications data model.
     *
     * @return the id of this Taxon.
     */
    public int getId();

    /**
     * Return the rank of the taxon.
     *
     * @return rank as integer
     */
    public int getRank();

    /**
     * Gets the name of this MorphologyValue object
     *
     * @return the name of this MorphologyValue.
     */
    public String getName();

    /**
     * Gets the text of this MorphologyValue object
     *
     * @return the text of this MorphologyValue.
     */
    public String getText();

    /**
     * Gets an array of Taxon object associated with this MorphologyValue.
     *
     * @return an array of Taxon objects.
     */
    public Taxon[] getTaxa();

    /**
     * Gets the Taxon object indicated.
     *
     * @param index index of the Taxon to retrive
     * @return the Taxon object
     * @throws IndexOutOfBoundsException is thrown when the index is not valid.
     */
    public Taxon getTaxon( int index );

    /**
     * Returns the MorphologyAttribute object this belongs to.
     *
     * @return the parent MorphologyAttribute
     */
    public MorphologyAttribute getParentAttribute();
}
