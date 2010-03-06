package com.ethz.geobot.herbar.modeapi.state;

import java.util.Enumeration;
import java.util.Vector;
import java.util.prefs.Preferences;

/**
 * Base class for state model which are able to contain submodels. To persist the state of this model, you have to
 * override the storeCompositeState and loadCompositeState instead of store/loadState.
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
        final Enumeration components = subStateModels();
        while ( components.hasMoreElements() )
        {
            ( (StateModel) components.nextElement() ).loadState( node );
        }
    }

    final public void storeState( Preferences node )
    {
        node = storeCompositeState( node );
        final Enumeration components = subStateModels();
        while ( components.hasMoreElements() )
        {
            ( (StateModel) components.nextElement() ).loadState( node );
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

    final public Enumeration subStateModels()
    {
        return subStateModelVector.elements();
    }

    /**
     * override this method to load the state of the model. This method will return a node from which the submodels
     * should be loaded.
     *
     * @param node Preferences node from which the model should load the state
     * @return a node used by the submodels to load there states
     */
    public abstract Preferences loadCompositeState( Preferences node );

    /**
     * override this method to save the state of the model. This method will return a node from which the submodels
     * should be saved.
     *
     * @param node Preferences node from which the model should save the state
     * @return a node used by the submodels to save there states
     */
    public abstract Preferences storeCompositeState( Preferences node );

    private final Vector subStateModelVector = new Vector();
}
