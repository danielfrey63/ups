package com.ethz.geobot.herbar.modeapi.state;

import java.util.prefs.Preferences;

/**
 * Interface for state models. Instead implementing this interface, users should use AbstractStateModel or
 * StateCompositeModel.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public interface StateModel
{
    StateCompositeModel getComposite();

    void loadState( Preferences node );

    void storeState( Preferences node );
}
