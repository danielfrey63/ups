/*
 * Herbar CD-ROM version 2
 *
 * AbstractMutableValue.java
 *
 * Created on 9. Juli 2002, 14:53
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;

import ch.jfactory.resource.Strings;

/**
 * <class description here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractMutableValue
        implements MorValue, Rankable
{
    /**
     * @see com.ethz.geobot.herbar.model.Rankable#getId()
     */
    public abstract int getId();

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getName()
     */
    public abstract String getName();

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getParentAttribute()
     */
    public abstract MorAttribute getParentAttribute();

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getTaxa()
     */
    public abstract Taxon[] getTaxa();

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getTaxon(int)
     */
    public abstract Taxon getTaxon( int index );

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getText()
     */
    public abstract String[] getTexts();

    /**
     * TODO: Must be removed after interface MorValue has been updated from getUserObject to getTexts.
     *
     * @see com.ethz.geobot.herbar.model.MorValue#getText()
     */
    public String getText()
    {
        final String[] texts = getTexts();
        if ( texts.length == 0 )
        {
            return Strings.getString( "ERROR.NO_MOR_TEXT_DEFINED" );
        }
        return getTexts()[0];
    }

    /**
     * Sets the name of this object.
     *
     * @param name the new name to set
     */
    public abstract void setName( String name );

    /**
     * Set rank of object within its siblings.
     *
     * @param rank the rank to set
     */
    public abstract void setRank( int rank );

    /**
     * Set a new parent {@link com.ethz.geobot.herbar.model.AbstractMutableAttribute}.
     *
     * @param newAtt the new parent to set
     */
    public abstract void setParentAttribute( MorAttribute newAtt );
}

// $Log: AbstractMutableValue.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.10  2004/08/31 22:10:17  daniel_frey
// Examlist loading working
//
// Revision 1.9  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.8  2003/04/24 23:12:51  daniel_frey
// - Integrated complete db
//
// Revision 1.7  2003/04/02 14:49:04  daniel_frey
// - Revised wizards
//
// Revision 1.6  2002/08/27 15:07:39  Dani
// - Converted id from string to integer
//
// Revision 1.5  2002/08/05 19:21:32  Dani
// - Mor dnd working but not saving
//
// Revision 1.4  2002/08/05 13:01:15  Dani
// - Synchronizing without changes
//
// Revision 1.3  2002/08/05 11:47:52  Dani
// - As long as the MorValue interface does not reflect the fact, that one
//   MorValue may have more than one texts, an workaround is introduced
//   by keeping getUserObject and adding a temporary getTexts
// - Added java doc comments
//
// Revision 1.2  2002/07/11 18:52:24  Dani
// Implements Rankable now
//
// Revision 1.1  2002/07/10 14:32:09  Dani
// Initial
//

