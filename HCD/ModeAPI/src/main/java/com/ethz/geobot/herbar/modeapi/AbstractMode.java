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
package com.ethz.geobot.herbar.modeapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a abstract skeleton implementation of the Mode interface. It implements property set/get behaviour and store the context into a instance variable.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
abstract public class AbstractMode implements Mode
{
    private HerbarContext context;

    private final Map props = new HashMap();

    public void activate()
    {
    }

    public void deactivate()
    {
    }

    public boolean queryDeactivate()
    {
        return true;
    }

    final public Object getProperty( final String name )
    {
        return props.get( name );
    }

    final public void setProperty( final String name, final Object value )
    {
        props.put( name, value );
    }

    public void init( final HerbarContext context )
    {
        this.context = context;
        loadState();
    }

    public void destroy()
    {
        saveState();
    }

    public void loadState()
    {
    }

    public void saveState()
    {
    }

    /**
     * Returns the context of the mode.
     *
     * @return reference to the HerbarContext object
     */
    public HerbarContext getHerbarContext()
    {
        return context;
    }

    @Override
    public String toString()
    {
        return getProperty( NAME ) + " [" + getProperty( MODE_GROUP ) + "]";
    }
}
