package com.ethz.geobot.herbar.gui.mode.wizard;


import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.gui.mode.ModeWizard;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.CascadeWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.prefs.Preferences;
import org.apache.log4j.Category;

/**
 * This WizardModel is used to has two models one is a static model (i.e. Mode Selection) and the dependent one is
 * choosen from the registered mode WizardModel's.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModeWizardModel extends CascadeWizardModel {

    public final static String SELECTEDMODE = "selectedMode";
    public final static String MODELIST = "modeList";

    private final static Category cat = Category.getInstance(ModeWizardModel.class);

    private Mode selectedMode = null;
    private Collection<? extends Object> modeList = Collections.EMPTY_LIST;
    private Mode currentMode;

    public ModeWizardModel(Preferences preferences, WizardPane[] panes, Mode currentMode) {
        super(preferences, panes);
        Collection modes = new ArrayList<Mode>(ModeManager.getInstance().getModes());
        this.currentMode = currentMode;
        setModeList(modes);
    }

    public String getDialogTitle() {
        return Strings.getString("WIZARD.MODE.TITLE");
    }

    public void setSelectedMode(Mode newMode) {
        Mode oldMode = selectedMode;
        selectedMode = newMode;
        if (selectedMode != null) {
            WizardModel modeWizardModel = ModeWizard.getInstance().getWizardModel(selectedMode);
            this.setCascadeWizardModel(modeWizardModel);
        }
        propertySupport.firePropertyChange(SELECTEDMODE, oldMode, newMode);
    }

    public Mode getSelectedMode() {
        return selectedMode;
    }

    public void init(HerbarContext context) {
    }

    public Collection<? extends Object> getModeList() {
        return modeList;
    }

    public void setModeList(Collection<? extends Object> modeList) {
        Collection<? extends Object> oldModeList = this.modeList;
        this.modeList = modeList;
        propertySupport.firePropertyChange(MODELIST, oldModeList, modeList);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }
}
