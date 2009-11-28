/*
 * MODENOTFOUNDEXCEPTION.JAVA
 *
 * Created on
 */

package com.ethz.geobot.herbar.gui.mode;

/**
 * This exception is thrown when condition occurred
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ModeNotFoundException extends Exception
{
    /**
     * Constructs an Exception without a message.
     */
    public ModeNotFoundException()
    {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     *
     * @param message The message associated with the exception.
     */
    public ModeNotFoundException( final String message )
    {
        super( message );
    }
}
