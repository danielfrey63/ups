package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.component.ObjectPopup;
import com.ethz.geobot.herbar.model.Level;
import java.awt.Component;

/**
 * LevelPopup control, used to select the level of a scope. You have to override the itemSelected method to get notified
 * of the level change.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
abstract public class LevelPopUp extends ObjectPopup
{
    /**
     * Construct a LevelPopup with all given levels.
     *
     * @param levels all levels which should be displayed
     */
    public LevelPopUp( final Level[] levels )
    {
        super( levels );
    }

    /**
     * displays the level popup and set the current level settings.
     *
     * @param jb           parent component
     * @param scopeLevels  all levels available for this scope
     * @param currentLevel current selected level
     */
    public void showPopUp( final Component jb, final Level[] scopeLevels, final Level currentLevel )
    {
        super.showPopUp( jb, scopeLevels, currentLevel );
    }
}
