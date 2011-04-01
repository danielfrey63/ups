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

/*
 * LessonMode.java
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;
import com.ethz.geobot.herbar.modeapi.SimpleTaxStateModel;
import com.ethz.geobot.herbar.modeapi.wizard.LessonWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardFilterPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardFocusPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardIntroPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardLevelPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardOrderPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.modeapi.wizard.WizardScopePane;

/**
 * Mode implementation for lesson mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class LessonMode extends AbstractModeAdapter
{
    static
    {
        ModeRegistration.register( new LessonMode() );
    }

    private LessonWizardModel wm = null;

    public LessonMode()
    {
        super( LessonPanel.class );
    }

    public WizardModel getWizardModel()
    {
        if ( wm == null )
        {
            final String title = Strings.getString( "WIZARD.LESSON.TITLE" );
            final WizardPane[] panes = new WizardPane[]{
                    new WizardIntroPane( "intro" ),
                    new WizardFilterPane( LessonWizardModel.MODEL ),
                    new WizardScopePane( LessonWizardModel.MODEL, SimpleTaxStateModel.SCOPE ),
                    new WizardLevelPane( LessonWizardModel.MODEL, SimpleTaxStateModel.SCOPE, SimpleTaxStateModel.LEVEL ),
                    new WizardOrderPane( SimpleTaxStateModel.ORDER ),
                    new WizardFocusPane( LessonWizardModel.TAXON_LIST, SimpleTaxStateModel.FOCUS )
            };
            wm = new LessonWizardModel( this.getHerbarContext(), panes, title );
        }
        return wm;
    }

    public void wizardSettingsInit()
    {
        super.wizardSettingsInit();
        final LessonPanel panel = (LessonPanel) getComponent();
        wm.setModel( panel.getModel() );
        wm.setScope( panel.getScope() );
        wm.setLevel( panel.getLevel() );
        wm.setOrdered( panel.isOrdered() );
        wm.setFocus( panel.getFocus() );
    }

    public void wizardSettingsFinish()
    {
        super.wizardSettingsFinish();
        final LessonPanel panel = (LessonPanel) getComponent();
        panel.setModel( wm.getModel() );
        panel.setScope( wm.getScope() );
        panel.setLevel( wm.getLevel() );
        panel.setOrdered( wm.isOrdered() );
        panel.setFocus( wm.getFocus() );
    }
}
