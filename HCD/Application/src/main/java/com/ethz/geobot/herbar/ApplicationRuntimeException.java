/*
 * Herbar CD-ROM version 2
 *
 * ApplicationRuntimException.java
 *
 * Created on 5. April 2002, 13:20
 * Created by Daniel Frey
 */

package com.ethz.geobot.herbar;

/**
 * Exception is thrown for system critical failures during initialization of the application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ApplicationRuntimeException extends java.lang.RuntimeException {

    /**
     * Creates a new instance of ApplicationRuntimException.
     */
    public ApplicationRuntimeException() {
        super("Critical exception occured.");
    }

    /**
     * Creates a new instance of ApplicationRuntimException with a specific message.
     *
     * @param message the exception message
     */
    public ApplicationRuntimeException(String message) {
        super(message);
    }
}

