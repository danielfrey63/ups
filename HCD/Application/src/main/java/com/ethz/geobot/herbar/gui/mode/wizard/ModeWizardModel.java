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
package com.ethz.geobot.herbar.gui.mode.wizard;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.gui.mode.ModeWizard;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.CascadeWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.prefs.Preferences;

/**
 * This WizardModel is used to has two models one is a static model (i.e. Mode Selection) and the dependent one is chosen from the registered mode WizardModel's.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModeWizardModel extends CascadeWizardModel
{
    public final static String SELECTED_MODE = "selectedMode";

    public final static String MODE_LIST = "modeList";

    private Mode selectedMode = null;

    private Collection<?> modeList = Collections.EMPTY_LIST;

    private final Mode currentMode;

    public ModeWizardModel( final Preferences preferences, final WizardPane[] panes, final Mode currentMode )
    {
        super( preferences, panes );
        final Collection modes = new ArrayList<Mode>( ModeManager.getInstance().getModes() );
        this.currentMode = currentMode;
        setModeList( modes );
        initPaneList();
    }

    public String getDialogTitle()
    {
        return Strings.getString( "WIZARD.MODE.TITLE" );
    }

    public void setSelectedMode( final Mode newMode )
    {
        final Mode oldMode = selectedMode;
        selectedMode = newMode;
        if ( selectedMode != null )
        {
            final WizardModel modeWizardModel = ModeWizard.getInstance().getWizardModel( selectedMode );
            this.setCascadeWizardModel( modeWizardModel );
        }
        propertySupport.firePropertyChange( SELECTED_MODE, oldMode, newMode );
    }

    public Mode getSelectedMode()
    {
        return selectedMode;
    }

    public void setModeList( final Collection<?> modeList )
    {
        final Collection<?> oldModeList = this.modeList;
        this.modeList = modeList;
        propertySupport.firePropertyChange( MODE_LIST, oldModeList, modeList );
    }

    public Mode getCurrentMode()
    {
        return currentMode;
    }
}
