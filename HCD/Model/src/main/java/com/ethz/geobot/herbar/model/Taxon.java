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
package com.ethz.geobot.herbar.model;

import ch.jfactory.model.graph.GraphNode;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface Taxon
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
     * Gets the taxonomic level of the Taxon object
     *
     * @return the taxonomic level
     */
    public Level getLevel();

    /**
     * Gets the name of the Taxon object
     *
     * @return the taxonomic name
     */
    public String getName();

    /**
     * Gets the parent Taxon.
     *
     * @return the parent taxon or null if it is the root taxon.
     */
    public Taxon getParentTaxon();

    /**
     * Gets an array of Taxon containing all direct children of this Taxon.
     *
     * @return the cildren of this Taxon.
     */
    public Taxon[] getChildTaxa();

    /**
     * Gets the child Taxon at index.
     *
     * @param index index of Taxon to retrieve.
     * @return the Taxon retrieved.
     * @throws IndexOutOfBoundsException thrown during an access of an invalid Taxon.
     */
    public Taxon getChildTaxon( int index ) throws IndexOutOfBoundsException;

    /**
     * Gets the position of the Taxon indicated within the array of Taxons.
     *
     * @param child child to locate.
     * @return the position within the array of child Taxon objects.
     */
    public int getChildTaxon( Taxon child );

    /**
     * Returns all taxa on the specified level, which are direct children to this taxon.
     *
     * @param level the level to collect all Taxon objects from.
     * @return an array of Taxon objects.
     */
    public Taxon[] getChildTaxa( Level level );

    /**
     * Returns all taxa on the specified level, including all children from this taxon.
     *
     * @param level the level to collect all Taxon objects from.
     * @return an array of Taxon objects.
     */
    public Taxon[] getAllChildTaxa( Level level );

    /**
     * Returns all Level objects from all Taxon objects belonging to this Taxon, including this Taxon objects level.
     *
     * @return an array of Level objects.
     */
    public Level[] getSubLevels();

    /**
     * Returns an array of Picture objects for a given theme or null if the theme is not part of this Taxon object.
     *
     * @param theme the theme for which the pictures are requested.
     * @return an array of Picture objects.
     */
    public CommentedPicture[] getCommentedPictures( PictureTheme theme );

    /**
     * Returns the list of available PictureTheme objects for this Taxon.
     *
     * @return an array of PictureTheme objects
     */
    public PictureTheme[] getPictureThemes();

    /**
     * Returns this Taxon objects as a GraphNode.
     *
     * @return graph node
     */
    public GraphNode getAsGraphNode();
}
