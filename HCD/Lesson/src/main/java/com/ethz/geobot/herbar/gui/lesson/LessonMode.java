/*
 * LessonMode.java
 *
 * Created on xx.xx.xxxx
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
                    new WizardFocusPane( LessonWizardModel.TAXLIST, SimpleTaxStateModel.FOCUS )
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
