/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action class to select a different mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionModeSelection extends AbstractParametrizedAction
{
    private final static Logger LOG = LoggerFactory.getLogger( ActionModeSelection.class.getName() );

    private final Preferences preferences;

    /**
     * Constructor, need the mainframe to set mode.
     *
     * @param parent      reference to mainframe
     * @param preferences the settings to use
     */
    public ActionModeSelection( final MainFrame parent, final Preferences preferences )
    {
        super( "MENU.ITEM.MODE_SELECTION", parent );
        this.preferences = preferences;
    }

    public void actionPerformed( final ActionEvent e )
    {
        Mode selectedMode = null;
        final MainFrame frame = (MainFrame) parent;
        try
        {
            final Mode oldMode = frame.getModel().getMode();
            if ( oldMode == null || oldMode.queryDeactivate() )
            {
                final WizardPane[] panes = new WizardPane[]{
                        new WizardModePane( ModeWizardModel.SELECTED_MODE, ModeWizardModel.MODE_LIST )};
                final ModeWizardModel model = new ModeWizardModel( preferences, panes, oldMode );
                final Wizard dlg = new Wizard( model );
                final boolean accepted = dlg.show( AppHerbar.getMainFrame(), 600, 400 );
                if ( accepted )
                {
                    selectedMode = model.getSelectedMode();
                    frame.getModel().setMode( selectedMode );
                }
            }
        }
        catch ( Exception ex )
        {
            LOG.error( "Error during creation of mode " + selectedMode, ex );
            Dialogs.showErrorMessage( parent.getRootPane(), "Error", Strings.getString( "APPLICATION.ERROR.MODE_CREATION.TEXT", "" + selectedMode ) );
        }
    }
}
