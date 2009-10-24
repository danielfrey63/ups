package com.ethz.geobot.herbar.modeapi;

import ch.jfactory.application.view.status.StatusBar;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.awt.Component;
import javax.swing.JFrame;

/**
 * Used by the Mode to customize herbar GUI.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public interface HerbarGUIManager {

    /**
     * Set the reference to the component that represents the mode.
     *
     * @param component reference to the view component
     */
    void setViewComponent(Component component);

    /**
     * Set WizardModel for the mode. This is used by Herbar application to display the wizard.
     *
     * @param wm reference to a WizardModel
     */
    void setWizardModel(WizardModel wm);

    /**
     * invoke the mode wizard.
     */
    void invokeWizard();

    /**
     * return the parent frame
     *
     * @return reference to the parent frame
     */
    JFrame getParentFrame();

    /**
     * shows an error message
     *
     * @param message the error message
     */
    void showErrorMessage(String message);

    /**
     * Returns the status bar of the main frame.
     */
    StatusBar getStatusBar();
}
