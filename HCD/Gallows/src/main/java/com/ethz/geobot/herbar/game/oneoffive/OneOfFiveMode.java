/*
 * Herbar CD-ROM version 2
 *
 * OneOfFiveMode.java
 *
 * Created on 11. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;

/**
 * mode-class of game oneoffive. Todo: Remove optical illusion of birds fly. Todo: HangMan moves to the left when texts
 * are to braod. Todo: Signal an appropriate successful end of game. Todo: Ask for "really exit" only if game is
 * running. Explain that the game will be terminated.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class OneOfFiveMode extends AbstractModeAdapter
{
    /**
     * Constructor
     */
    public OneOfFiveMode()
    {
        super( OneOfFive.class );
    }

    static
    {
        ModeRegistration.register( new OneOfFiveMode() );
    }
}
