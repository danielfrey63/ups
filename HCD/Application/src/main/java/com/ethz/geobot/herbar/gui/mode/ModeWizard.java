package com.ethz.geobot.herbar.gui.mode;

import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.DefaultWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Category;

/**
 * This class stores the wizard information about the modes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModeWizard {

    /**
     * category object for logging
     */
    private static Category cat = Category.getInstance(ModeWizard.class.getName());
    private static ModeWizard instance = null;

    protected ModeWizard() {
    }

    private Map<Mode, WizardModel> wizardModels = new HashMap<Mode, WizardModel>();

    public void setWizardModel(Mode mode, WizardModel model) {
        if (cat.isInfoEnabled()) {
            cat.info("register wizard model for mode: " + mode.getProperty(Mode.NAME));
        }
        wizardModels.put(mode, model);
    }

    /**
     * check if the given mode has a registered wizard model
     *
     * @param mode the given mode
     * @return true if the Mode has a wizard model
     */
    public boolean hasWizard(Mode mode) {
        return wizardModels.containsKey(mode);
    }

    /**
     * get the wizard model for the given mode.
     *
     * @param mode mode for which the wizard is needed
     * @return instance of the wizard model
     */
    public WizardModel getWizardModel(Mode mode) {
        WizardModel model = wizardModels.get(mode);
        if (model == null) {
            cat.debug("mode " + mode.getProperty(Mode.NAME) + " hasn't register a WizardModel.");
            model = new DefaultWizardModel("ModeWizard");
        }
        return model;
    }

    public static ModeWizard getInstance() {
        if (instance == null) {
            instance = new com.ethz.geobot.herbar.gui.mode.ModeWizard();
        }
        return instance;
    }
}
