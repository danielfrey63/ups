package ch.jfactory.component.wizard;

import ch.jfactory.application.presentation.WindowUtils;
import java.awt.Component;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * This class represent the Wizard Component. It is initialize with a wizard model or create a own DefaultWizardModel
 * with no panes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/03/14 21:27:56 $
 */
public class Wizard
{
    /** logging category for class */
//    private static final Category CAT = Category.getInstance(Wizard.class);

    /** the model of the wizard */
    private WizardModel model;

    /**
     * Construct a wizard with a DefaultWizardModel and initialize it with the given panes.
     *
     * @param panes an array of WizardPane's
     */
    public Wizard(final WizardPane[] panes)
    {
        model = new DefaultWizardModel(new Properties(), panes, "UnspecifiedModel");
    }

    /**
     * Construct a wizard with a given model.
     *
     * @param model the WizardModel
     */
    public Wizard(final WizardModel model)
    {
        this.model = model;
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show(final JFrame parent)
    {
        final String title = model.getDialogTitle();
        final WizardDialogUI dlg = new WizardDialogUI(parent, title, true);
        return doShow(dlg, parent);
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show(final JDialog parent)
    {
        final String title = model.getDialogTitle();
        final WizardDialogUI dlg = new WizardDialogUI(parent, title, true);
        return doShow(dlg, parent);
    }

    private boolean doShow(final WizardDialogUI dlg, final Component parent)
    {
        dlg.setModel(model);
        WindowUtils.centerOnComponent(dlg, parent);
        dlg.setVisible(true);
        return dlg.isAccepted();
    }
}
