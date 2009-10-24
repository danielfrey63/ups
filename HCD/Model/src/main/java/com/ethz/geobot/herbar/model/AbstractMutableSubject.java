/*
 * Herbar CD-ROM version 2
 *
 * AbstractMutableSubject.java
 *
 * Created on 9. Juli 2002, 11:32
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;


/**
 * <class description here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractMutableSubject
        implements MorSubject, Rankable {

    /**
     * Sets the MorAttribute array of this object
     *
     * @param child the new attributes to set
     * @param index the position to insert the new child to
     */
    public abstract void addChild(MorAttribute child, int index);
//    public abstract void setChildAttributes( MorAttribute[] attributes );

    /**
     * Sets the child MorSubject array of this object
     *
     * @param child the new child subjects to set
     * @param index the position to insert the new child to
     */
    public abstract void addChild(MorSubject child, int index);
//    public abstract void setChildSubjects( MorSubject[] subjets );

    public abstract void removeChild(MorSubject child);

    public abstract void removeChild(MorAttribute child);

    /**
     * Sets the name of this object
     *
     * @param name the new name to set
     */
    public abstract void setName(String name);

    /**
     * Sets the parent MorSubject of this object
     *
     * @param parent the new parent MorSubject to set
     */
    public abstract void setParentSubject(MorSubject parent);

    /**
     * Sets the rank of this object
     *
     * @param rank the new rank to set
     */
    public abstract void setRank(int rank);
}

// $Log: AbstractMutableSubject.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.5  2003/04/24 23:12:51  daniel_frey
// - Integrated complete db
//
// Revision 1.4  2002/08/07 13:16:47  Dani
// - Moved most getter to superclass and added addChildren/remove me

//   methods
//
// Revision 1.3  2002/08/05 19:21:32  Dani
// - Mor dnd working but not saving
//
// Revision 1.2  2002/07/11 18:51:45  Dani
// - Implements Rankable now
//
// Revision 1.1  2002/07/10 14:32:09  Dani
// - Initial
//

