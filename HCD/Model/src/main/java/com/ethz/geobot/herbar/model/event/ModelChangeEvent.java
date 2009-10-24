/*
 * ModelChangeEvent.java
 *
 * Created on 13. September 2002, 13:07
 */

package com.ethz.geobot.herbar.model.event;

/**
 * Event object for ModelChangeListener.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class ModelChangeEvent extends java.util.EventObject {

    /**
     * Creates a new instance of ModelChangeEvent
     */
    public ModelChangeEvent(Object source) {
        super(source);
    }

}
