/*
 * Herbar CD-ROM version 2
 *
 * CatcherMode.java
 *
 * Created on 1. Mai 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.catcher;

import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;

/**
 * Modeclass for game catcher.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created $Date: 2007/09/17 11:05:58 $
 */
public class CatcherMode extends AbstractModeAdapter
{
    /**
     * @see Object#Object()
     */
    public CatcherMode()
    {
        super( Catcher.class );
    }

    static
    {
        ModeRegistration.register( new CatcherMode() );
    }
}
