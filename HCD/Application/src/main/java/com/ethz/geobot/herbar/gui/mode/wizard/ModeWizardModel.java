package com.ethz.geobot.herbar.gui.mode.wizard;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.wizard.CascadeWizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.prefs.Preferences;

/**
 * This WizardModel is used to has two models one is a static model (i.e. Mode Selection) and the dependent one is
 * chosen from the registered mode WizardModel's.
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
