/*
 * MorAttribute.java
 *
 * Created on 28.3.2002
 */
package com.ethz.geobot.herbar.model;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface MorphologyAttribute
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
     * Gets the name of this MorphologyAttribute object
     *
     * @return the name of this MorphologyAttribute.
     */
    public String getName();

    /**
     * Get an array of MorphologyValue objects from this MorphologyAttribute.
     *
     * @return an array of MorphologyValue objects.
     */
    public MorphologyValue[] getValues();

    /**
     * Get a MorphologyValue object indicated by index.
     *
     * @param index ???_Description_of_the_Parameter_???
     * @return the MorphologyValue object.
     * @throws IndexOutOfBoundsException ???_Description_of_the_Exception_???
     */
    public MorphologyValue getValue( int index )
            throws IndexOutOfBoundsException;

    /**
     * Returns the MorphologySubject object this belongs to.
     *
     * @return the parent MorphologySubject
     */
    public MorphologySubject getParentSubject();
}
