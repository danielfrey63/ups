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
public interface MorAttribute {

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
     * Gets the name of this MorAttribute object
     *
     * @return the name of this MorAttribute.
     */
    public String getName();

    /**
     * Get an array of MorValue objects from this MorAttribute.
     *
     * @return an array of MorValue objects.
     */
    public MorValue[] getValues();

    /**
     * Get a MorValue object indicated by index.
     *
     * @param index ???_Description_of_the_Parameter_???
     * @return the MorValue object.
     * @throws IndexOutOfBoundsException ???_Description_of_the_Exception_???
     */
    public MorValue getValue(int index)
            throws IndexOutOfBoundsException;

    /**
     * Returns the MorSubject object this belongs to.
     *
     * @return the parent MorSubject
     */
    public MorSubject getParentSubject();
}
