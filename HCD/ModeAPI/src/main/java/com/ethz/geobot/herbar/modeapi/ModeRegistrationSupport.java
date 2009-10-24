package com.ethz.geobot.herbar.modeapi;


/**
 * This interface is implemented by classes how support the registration of a Mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public interface ModeRegistrationSupport {

    /**
     * This method is called to register a Mode.
     *
     * @param mode the mode to register
     */
    void register(Mode mode);
}
