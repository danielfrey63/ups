/*
 * ModelChangeListener.java
 *
 * Created on 13. September 2002, 13:04
 */

package com.ethz.geobot.herbar.model.event;

/**
 * Used for implements ModelChange listener.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface ModelChangeListener extends java.util.EventListener {

    /**
     * this method is invoked if the model has changed.
     *
     * @param event information about the model change
     */
    void modelChanged(ModelChangeEvent event);
}
