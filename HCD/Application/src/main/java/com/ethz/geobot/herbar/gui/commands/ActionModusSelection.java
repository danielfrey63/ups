package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.component.Dialogs;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.gui.MainFrame;
import com.ethz.geobot.herbar.gui.mode.wizard.ModeWizardModel;
import com.ethz.geobot.herbar.gui.mode.wizard.WizardModePane;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.Wizard;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import org.apache.log4j.Category;

/**
 * Action class to select a different mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionModusSelection extends AbstractParametrizedAction {

    private final static Category cat = Category.getInstance(ActionModusSelection.class.getName());
    private Preferences preferences;

    /**
     * Constructor, need the mainframe to set mode.
     *
     * @param parent      reference to mainframe
     * @param preferences the settings to use
     */
    public ActionModusSelection(MainFrame parent, Preferences preferences) {
        super("MENU.ITEM.MODUS_SELECTION", parent);
        this.preferences = preferences;
    }

    public void actionPerformed(ActionEvent e) {
        Mode selectedMode = null;
        MainFrame frame = (MainFrame) parent;
        try {
            Mode oldMode = frame.getModel().getMode();
            if (oldMode == null || oldMode.queryDeactivate()) {
                WizardPane[] panes = new WizardPane[]{
                        new WizardModePane(ModeWizardModel.SELECTEDMODE, ModeWizardModel.MODELIST)};
                ModeWizardModel model = new ModeWizardModel(preferences, panes, oldMode);
                Wizard dlg = new Wizard(model);
                boolean accepted = dlg.show(AppHerbar.getMainFrame(), 600, 400);
                if (accepted) {
                    selectedMode = model.getSelectedMode();
                    frame.getModel().setMode(selectedMode);
                }
            }
        }
        catch (Exception ex) {
            cat.error("Error during creation of mode " + selectedMode, ex);
            Dialogs.showErrorMessage(parent.getRootPane(), "Fehler", Strings.getString("APPLICATION.ERROR.MODUSCREATION.TEXT", "" + selectedMode));
        }
    }
}
