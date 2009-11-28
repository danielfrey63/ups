/*
 * Herbar CD-ROM version 2
 *
 * MazeMode.java
 *
 * Created on 4. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.maze;

import com.ethz.geobot.herbar.modeapi.AbstractModeAdapter;
import com.ethz.geobot.herbar.modeapi.ModeRegistration;

/**
 * mode-class of game maze. Todo: Disable OK Button in Right-Answer-Dialog when appropriate. Todo: Set Focus on OK
 * Button in Right-Answer-Dialog. Todo: Maze seems to be sometimes too far to the right, so the frog is not seen. Todo:
 * Ask for "really exit" only if game is running. Explain that the game will be terminated.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:00 $
 */
public class MazeMode extends AbstractModeAdapter
{
    /**
     * Constructor
     */
    public MazeMode()
    {
        super( MazePanel.class );
    }

    static
    {
        ModeRegistration.register( new MazeMode() );
    }
}
