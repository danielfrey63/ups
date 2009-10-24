package com.ethz.geobot.herbar.modeapi;


/**
 * This interface is used by the AbstractModeAdapter to forward the Mode notification to the given Component. If you
 * implement this interface in the Component (i.e. JPanel) with which the AbstractModeAdapter is initialize, the
 * AbstractModeAdapter forward Mode request like activate, deactivate to the Component.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public interface ModeActivation {

    /**
     * The mode is activated
     */
    void activate();

    /**
     * The mode is deactivated
     */
    void deactivate();

    /**
     * Ask the mode if deactivation is possible.
     *
     * @return true for deactivation possible, false denied deactivation
     */
    boolean queryDeactivate();
}
