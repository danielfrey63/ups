/*
 * Herbar CD-ROM version 2
 *
 * LevelComponentWrapper.java
 *
 * Created on 18. Juni 2002, 21:10
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.util;

import com.ethz.geobot.herbar.model.Level;
import javax.swing.JLabel;

/**
 * Main purpose is to make a Level a component. This may be used i.e. for putting Level objects into menues aso.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class LevelComponentWrapper extends JLabel
{
    private Level level;

    /**
     * Creates a new instance of LevelComponentWrapper
     *
     * @param level the Level object to wrap
     */
    public LevelComponentWrapper( final Level level )
    {
        this.setLevel( level );
    }

    /**
     * Sets the wrapped Level object.
     *
     * @param level the Level object to set
     */
    public void setLevel( final Level level )
    {
        this.level = level;
    }

    /**
     * Returns the wrapped Level object.
     *
     * @return the Level object
     */
    public Level getLevel()
    {
        return this.level;
    }

    public String toString()
    {
        return level.toString();
    }
}
