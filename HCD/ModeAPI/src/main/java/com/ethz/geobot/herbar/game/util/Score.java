/*
 * Herbar CD-ROM version 2
 *
 * Score.java
 *
 * Created on 05. Juni 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.util;

/**
 * Interface for Score-Classes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:06 $
 */
public interface Score
{
    public void addWrongScore( int diff );

    public int getWrongScore();

    public void addRightScore( int diff );

    public int getRightScore();

    public int getTotalScore();
}
