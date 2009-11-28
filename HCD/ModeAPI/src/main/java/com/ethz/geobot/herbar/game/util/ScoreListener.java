/*
 * Herbar CD-ROM version 2
 *
 * ScoreListener.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.util;

import java.util.EventListener;

/**
 * Interface for scorelistener-classes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created $Date: 2007/09/17 11:07:06 $
 */
public interface ScoreListener extends EventListener
{
    public void scoreChanged();
}
