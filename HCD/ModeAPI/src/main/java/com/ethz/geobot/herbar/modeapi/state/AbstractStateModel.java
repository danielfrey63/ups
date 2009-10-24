package com.ethz.geobot.herbar.modeapi.state;


/**
 * Base class for models which is not able to contain submodels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public abstract class AbstractStateModel implements StateModel {

    public StateCompositeModel getComposite() {
        return null;
    }
}
