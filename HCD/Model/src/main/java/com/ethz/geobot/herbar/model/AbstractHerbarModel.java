/*
 * AbstractHerbarModel.java
 *
 * Created on 13. September 2002, 13:10
 */

package com.ethz.geobot.herbar.model;

import com.ethz.geobot.herbar.model.event.ModelChangeEvent;
import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * Abstract class for implementing HerbarModel, implements the ModelChangeListener behavior.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
abstract public class AbstractHerbarModel implements HerbarModel
{
    /**
     * log category
     */
    private static final Logger LOG = Logger.getLogger( AbstractHerbarModel.class );

    /**
     * Utility field holding list of ModelChangeListeners.
     */
    private java.util.ArrayList modelChangeListenerList = null;

    /**
     * Holds value of property name.
     */
    private String name;

    protected boolean readOnly;

    public AbstractHerbarModel( final String name )
    {
        if ( LOG.isInfoEnabled() )
        {
            LOG.info( "create HerbarModel with name " + name );
        }
        this.name = name;
    }

    /**
     * Registers ModelChangeListener to receive events.
     *
     * @param listener The listener to register.
     */
    public synchronized void addModelChangeListener( final com.ethz.geobot.herbar.model.event.ModelChangeListener listener )
    {
        if ( modelChangeListenerList == null )
        {
            modelChangeListenerList = new java.util.ArrayList();
        }
        modelChangeListenerList.add( listener );
    }

    /**
     * Removes ModelChangeListener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeModelChangeListener( final com.ethz.geobot.herbar.model.event.ModelChangeListener listener )
    {
        if ( modelChangeListenerList != null )
        {
            modelChangeListenerList.remove( listener );
        }
    }

    public synchronized void fireModelChangeEvent( final ModelChangeEvent event )
    {
        if ( LOG.isInfoEnabled() )
        {
            LOG.info( "model >" + name + "< changed: inform all observers" );
        }
        if ( modelChangeListenerList != null )
        {
            for ( final Object aModelChangeListenerList : modelChangeListenerList )
            {
                final ModelChangeListener listener = (ModelChangeListener) aModelChangeListenerList;
                listener.modelChanged( event );
            }
        }
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name. If you like to support setName you should overwrite this method with a public one and
     * delegate the call to the super class.
     *
     * @param name New value of property name.
     */
    public void setName( final String name )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "rename model form >" + this.name + "< to >" + name + "<" );
        }
        this.name = name;
    }

    public void setReadOnly()
    {
        this.readOnly = true;
    }

    public String toString()
    {
        return name;
    }
}
