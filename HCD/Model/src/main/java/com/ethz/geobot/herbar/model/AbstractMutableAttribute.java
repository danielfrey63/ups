/*
 * Herbar CD-ROM version 2
 *
 * AbstractMutableAttribute.java
 *
 * Created on 9. Juli 2002, 13:50
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;


/**
 * <class description here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractMutableAttribute
        implements MorAttribute, Rankable {

    // Interface MorAttribute
    public abstract int getId();

    // Interface MorAttribute
    public abstract String getName();

    // Interface MorAttribute
    public abstract MorSubject getParentSubject();

    // Interface MorAttribute
    public abstract MorValue getValue(int index)
            throws IndexOutOfBoundsException;

    // Interface MorAttribute
    public abstract MorValue[] getValues();

    /**
     * Sets the name of this object.
     *
     * @param name the new name to set
     */
    public abstract void setName(String name);

    /**
     * Sets the rank of this object.
     *
     * @param rank the new rank to set
     */
    public abstract void setRank(int rank);

    /**
     * Sets the Values array of this object.
     *
     * @param values the new values to set
     */
    public abstract void setValues(MorValue[] values);

    /**
     * Sets the parent MorSubject for this object.
     *
     * @param parent the new MorSubject to set as parent
     */
    public abstract void setParentSubject(MorSubject parent);
}

// $Log: AbstractMutableAttribute.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.5  2003/04/24 23:12:51  daniel_frey
// - Integrated complete db
//
// Revision 1.4  2002/08/27 15:07:39  Dani
// - Converted id from string to integer
//
// Revision 1.3  2002/08/05 11:46:08  Dani
// - Inserted setParentSubject
//
// Revision 1.2  2002/07/11 18:51:29  Dani
// Implements Rankable now
//
// Revision 1.1  2002/07/10 14:32:09  Dani
// Initial
//

