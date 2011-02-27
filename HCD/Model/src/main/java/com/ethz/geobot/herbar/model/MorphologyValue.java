/*
 * MorValue.java
 *
 * Created on 23.5.2002
 * Created by Dirk Hoffmann
 */
package com.ethz.geobot.herbar.model;

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
