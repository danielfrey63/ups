/*
 * Herbar CD-ROM version 2
 *
 * IteratorControlListener.java
 *
 * Created on ??. ??? 2002, ??:??
 * Created by ???
 */
package com.ethz.geobot.herbar.gui.util;

/**
 * Listener interface for the Iterator control.
 *
 * @author $Author: daniel_frey $
 * @version $Version$ $Date: 2007/09/17 11:07:08 $
 */
public interface IteratorControlListener
{
    /**
     * This method is invoked to notify the Observer that the item position has changed.
     *
     * @param event information about the event
     */
    void itemChange( IteratorControlEvent event );
}

// $Log: IteratorControlListener.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//