/*
 * Herbar CD-ROM version 2
 *
 * Rankable.java
 *
 * Created on 9. Juli 2002, 15:23
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;


/**
 * Defines rank and id.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface Rankable {

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
     * Set rank of object within its siblings.
     *
     * @param rank the rank to set
     */
    public abstract void setRank(int rank);
}

// $Log: Rankable.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.3  2002/08/27 15:07:39  Dani
// - Converted id from string to integer
//
// Revision 1.2  2002/07/11 18:53:49  Dani
// Removed interface Rankable
//
// Revision 1.1  2002/07/10 14:32:09  Dani
// Initial
//

