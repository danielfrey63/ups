package com.ethz.geobot.herbar.modeapi;

/**
 * This class is used by the Mode to register itself into the application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ModeRegistration {

    private static ModeRegistrationSupport registrationSupport;

    /**
     * This method is used by a Mode to register itself.
     *
     * @param mode reference to itself
     */
    public static void register(Mode mode) {
        registrationSupport.register(mode);
    }

    /**
     * This method is used by the Application to set the reference to the object where the modes should registered. This
     * Method should not used by the Mode.
     *
     * @param registrationSupport reference to registration object
     */
    public static void setRegistrationSupport(ModeRegistrationSupport registrationSupport) {
        ModeRegistration.registrationSupport = registrationSupport;
    }
}
