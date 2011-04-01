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
 * MorSubject.java
 *
 * Created on
 */
package com.ethz.geobot.herbar.model.trait;

import ch.jfactory.model.graph.GraphNode;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface MorphologySubject
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
     * Returns the name of this MorphologySubject object.
     *
     * @return the name of this MorphologySubject.
     */
    public String getName();

    /**
     * Returns an array of MorphologyAttribute objects belonging to this MorphologySubject.
     *
     * @return an array of MorphologyAttribute objects.
     */
    public MorphologyAttribute[] getAttributes();

    /**
     * Returns MorphologyAttribute object i belonging to this MorphologySubject.
     *
     * @param index the index of the MorphologyAttribute to get
     * @return an MorphologyAttribute object.
     * @throws IndexOutOfBoundsException if accessing an MorphologyAttribute object outside the MorphologyAttribute objects available.
     */
    public MorphologyAttribute getAttribute( int index )
            throws IndexOutOfBoundsException;

    /**
     * Returns all MorphologySubject objects belonging to this MorphologySubject.
     *
     * @return all MorphologySubject objects.
     */
    public MorphologySubject[] getSubjects();

    /**
     * Returns MorphologySubject object i belonging to this MorphologySubject.
     *
     * @param index the index of the MorphologySubject to get
     * @return an MorphologySubject object.
     * @throws IndexOutOfBoundsException if accessing an MorphologySubject object outside the MorphologySubject objects available.
     */
    public MorphologySubject getSubject( int index )
            throws IndexOutOfBoundsException;

    /**
     * Returns the parent MorphologySubject object belonging to this MorphologySubject.
     *
     * @return the parent MorphologySubject objects.
     */
    public MorphologySubject getParentSubject();

    public GraphNode getAsGraphNode();
}
