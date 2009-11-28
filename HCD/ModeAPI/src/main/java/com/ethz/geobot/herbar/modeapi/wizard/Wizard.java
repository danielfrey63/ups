package com.ethz.geobot.herbar.modeapi.wizard;

import com.ethz.geobot.herbar.modeapi.HerbarContext;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * This class represent the Wizard Component. It is initialize with a wizard model or create a own DefaultWizardModel
 * with no panes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class Wizard
{
    /** logging category for class */
//    private static final Category LOG = Category.getInstance(Wizard.class);

    /**
     * the model of the wizard
     */
    private final WizardModel model;

    /**
     * Default constructor, create a Wizard with a DefaultWizardModel
     */
    public Wizard()
    {
        model = new DefaultWizardModel( "UnspecifiedModel" );
    }

    /**
     * Construct a wizard with a DefaultWizardModel and initialize it with the given panes.
     *
     * @param panes an array of WizardPane's
     */
    public Wizard( final WizardPane[] panes )
    {
        model = new DefaultWizardModel( (HerbarContext) null, panes, "UnspecifiedModel" );
    }

    /**
     * Construct a wizard with a given model.
     *
     * @param model the WizardModel
     */
    public Wizard( final WizardModel model )
    {
        this.model = model;
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show( final JFrame parent )
    {
        final String title = model.getDialogTitle();
        final WizardDialogUI dlg = new WizardDialogUI( parent, title, true );
        dlg.setModel( model );
        dlg.setLocationRelativeTo( parent );
        dlg.setVisible( true );
        return dlg.isAccepted();
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show( final JFrame parent, final int width, final int height )
    {
        final String title = model.getDialogTitle();
        final WizardDialogUI dlg = new WizardDialogUI( parent, title, true );
        dlg.setModel( model );
        dlg.setSize( width, height );
        dlg.setLocationRelativeTo( parent );
        dlg.setVisible( true );
        return dlg.isAccepted();
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show( final JDialog parent )
    {
        final String title = model.getDialogTitle();
        WizardDialogUI dlg = new WizardDialogUI( parent, title, true );
        dlg = new WizardDialogUI( parent, title, true );
        dlg.setModel( model );
        dlg.setLocationRelativeTo( parent );
        dlg.setVisible( true );
        return dlg.isAccepted();
    }

    /**
     * Display the wizard in the middle of the given parent as a modal dialog box.
     *
     * @param parent the parent frame
     * @return true wizard settings accepted, false wizard canceled
     */
    public boolean show( final JDialog parent, final int width, final int height )
    {
        final String title = model.getDialogTitle();
        WizardDialogUI dlg = new WizardDialogUI( parent, title, true );
        dlg = new WizardDialogUI( parent, title, true );
        dlg.setModel( model );
        dlg.setSize( width, height );
        dlg.setLocationRelativeTo( parent );
        dlg.setVisible( true );
        return dlg.isAccepted();
    }

}
