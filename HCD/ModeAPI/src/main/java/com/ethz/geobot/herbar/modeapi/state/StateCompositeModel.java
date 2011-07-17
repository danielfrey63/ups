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
package com.ethz.geobot.herbar.modeapi.state;

import java.util.Enumeration;
import java.util.Vector;
import java.util.prefs.Preferences;

/**
 * Base class for state model which are able to contain submodels. To persist the state of this model, you have to override the storeCompositeState and loadCompositeState instead of store/loadState.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
abstract public class StateCompositeModel implements StateModel
{
    final public StateCompositeModel getComposite()
    {
        return this;
    }

    final public void loadState( Preferences node )
    {
        node = loadCompositeState( node );
        final Enumeration<StateModel> components = subStateModels();
        while ( components.hasMoreElements() )
        {
            components.nextElement().loadState( node );
        }
    }

    final public void storeState( Preferences node )
    {
        node = storeCompositeState( node );
        final Enumeration<StateModel> components = subStateModels();
        while ( components.hasMoreElements() )
        {
            components.nextElement().loadState( node );
        }
    }

    final public void add( final StateModel stateModel )
    {
        subStateModelVector.addElement( stateModel );
    }

    final public void remove( final StateModel stateModel )
    {
        subStateModelVector.removeElement( stateModel );
    }

    final public Enumeration<StateModel> subStateModels()
    {
        return subStateModelVector.elements();
    }

    /**
     * override this method to load the state of the model. This method will return a node from which the sub models should be loaded.
     *
     * @param node Preferences node from which the model should load the state
     * @return a node used by the sub models to load there states
     */
    public abstract Preferences loadCompositeState( Preferences node );

    /**
     * override this method to save the state of the model. This method will return a node from which the sub models should be saved.
     *
     * @param node Preferences node from which the model should save the state
     * @return a node used by the sub models to save there states
     */
    public abstract Preferences storeCompositeState( Preferences node );

    private final Vector<StateModel> subStateModelVector = new Vector<StateModel>();
}
