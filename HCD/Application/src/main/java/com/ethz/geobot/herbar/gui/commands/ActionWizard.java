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
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.gui.MainFrame;
import com.ethz.geobot.herbar.gui.mode.ModeWizard;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.Wizard;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays the wizard dialog for the actual mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionWizard extends AbstractParametrizedAction
{
    /** logger instance */
    private static final Logger LOG = LoggerFactory.getLogger( ActionWizard.class );

    /**
     * Constructor, need a parent frame to set about box on top.
     *
     * @param parent reference to parent frame
     */
    public ActionWizard( final MainFrame parent )
    {
        super( "MENU.ITEM.WIZARD", parent );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        final MainFrame frame = (MainFrame) parent;
        final Mode mode = frame.getModel().getMode();
        mode.wizardSettingsInit();
        final WizardModel model = ModeWizard.getInstance().getWizardModel( mode );
        final Wizard dlg = new Wizard( model );
        final boolean accepted = dlg.show( AppHerbar.getMainFrame(), 600, 388 );
        if ( accepted )
        {
            try
            {
                mode.wizardSettingsFinish();
            }
            catch ( Throwable ex )
            {
                LOG.error( "exchange of wizard model for mode: " + mode.getProperty( Mode.NAME ) + " failed.", ex );
            }
        }
    }
}
