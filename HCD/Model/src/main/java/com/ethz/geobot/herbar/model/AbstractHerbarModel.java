/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */

/*
 * AbstractHerbarModel.java
 *
 * Created on 13. September 2002, 13:10
 */
package com.ethz.geobot.herbar.model;

import com.ethz.geobot.herbar.model.event.ModelChangeEvent;
import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for implementing HerbarModel, implements the ModelChangeListener behavior.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
abstract public class AbstractHerbarModel implements HerbarModel
{
    /** log category */
    private static final Logger LOG = LoggerFactory.getLogger( AbstractHerbarModel.class );

    /** Utility field holding list of ModelChangeListeners. */
    private ArrayList<ModelChangeListener> modelChangeListenerList = null;

    /** Holds value of property name. */
    private String name;

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
    public synchronized void addModelChangeListener( final ModelChangeListener listener )
    {
        if ( modelChangeListenerList == null )
        {
            modelChangeListenerList = new ArrayList<ModelChangeListener>();
        }
        modelChangeListenerList.add( listener );
    }

    public synchronized void fireModelChangeEvent( final ModelChangeEvent event )
    {
        if ( LOG.isInfoEnabled() )
        {
            LOG.trace( "model >" + name + "< changed: inform all observers" );
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
     * Setter for property name. If you like to support setName you should overwrite this method with a public one and delegate the call to the super class.
     *
     * @param name New value of property name.
     */
    public void setName( final String name )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "rename model form \"" + this.name + "\" to \"" + name + "\"" );
        }
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}
