/*
 * Herbar CD-ROM version 2
 *
 * MutableTaxon.java
 *
 * Created on 23. Juni 2002, 23:02
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;


/**
 * Definition of a editable Taxon.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractMutableTaxon extends AbstractTaxon
        implements Rankable {

    /**
     * Sets the children of this AbstractMutableTaxon to the given array. For each child, the setParentTaxon method will
     * be invoced.
     *
     * @param children the children to be set
     */
    public abstract void setChildTaxa(Taxon[] children);

    /**
     * Sets the Level of this AbstarctMutableTaxon to the given object.
     *
     * @param newLevel the new level to set
     */
    public abstract void setLevel(Level newLevel);

    /**
     * Sets the name to the given value.
     *
     * @param newName the new name to set
     */
    public abstract void setName(String newName);

    /**
     * Sets the parent Taxon object to the given value.
     *
     * @param parent the new Taxon object to set
     */
    public abstract void setParentTaxon(Taxon parent);

    /**
     * Sets the rank to the given new rank.
     *
     * @param newRank the new rank to set
     */
    public abstract void setRank(int newRank);
}

// $Log: AbstractMutableTaxon.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.3  2002/07/11 18:52:08  Dani
// Implements Rankable now
//
// Revision 1.2  2002/07/10 14:33:08  Dani
// Reformatted
//
// Revision 1.1  2002/07/05 09:03:18  Dani
// initial
//

