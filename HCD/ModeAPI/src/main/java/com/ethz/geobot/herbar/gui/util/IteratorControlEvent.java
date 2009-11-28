/*
 * Herbar CD-ROM version 2
 *
 * ImageLocator.java
 *
 * Created on ??. ??? 2002, ??:??
 * Created by ???
 */
package com.ethz.geobot.herbar.gui.util;

/**
 * Class containing information about the IteratorControl Event
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class IteratorControlEvent
{
    private final Object currentObject;

    IteratorControlEvent( final Object currentObject )
    {
        this.currentObject = currentObject;
    }

    /**
     * Return the current object selected by the IteratorControlPanel.
     *
     * @return ??? The currentObject value ???
     */
    public Object getCurrentObject()
    {
        return currentObject;
    }
}

// $Log: IteratorControlEvent.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
