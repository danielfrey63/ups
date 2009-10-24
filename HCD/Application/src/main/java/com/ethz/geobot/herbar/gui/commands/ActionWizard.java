package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.gui.MainFrame;
import com.ethz.geobot.herbar.gui.mode.ModeWizard;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.Wizard;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.awt.event.ActionEvent;
import org.apache.log4j.Category;

/**
 * Displays the wizard dialog for the actual mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionWizard extends AbstractParametrizedAction {

    /**
     * logger instance
     */
    private static final Category cat = Category.getInstance(ActionWizard.class);

    /**
     * Constructor, need a parent frame to set about box on top.
     *
     * @param parent reference to parent frame
     */
    public ActionWizard(MainFrame parent) {
        super("MENU.ITEM.WIZARD", parent);
    }

    public void actionPerformed(ActionEvent parm1) {
        MainFrame frame = (MainFrame) parent;
        Mode mode = frame.getModel().getMode();
        mode.wizardSettingsInit();
        WizardModel model = ModeWizard.getInstance().getWizardModel(mode);
        Wizard dlg = new Wizard(model);
        boolean accepted = dlg.show(AppHerbar.getMainFrame(), 600, 388);
        if (accepted) {
            try {
                mode.wizardSettingsFinish();
            }
            catch (Throwable ex) {
                cat.fatal("exchange of wizard model for mode: " + mode.getProperty(Mode.NAME) + " failed.", ex);
            }
        }
    }
}
