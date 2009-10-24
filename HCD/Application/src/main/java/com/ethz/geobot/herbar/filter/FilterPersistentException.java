/*
 * FilterPersistentException.java
 *
 * Created on 21. August 2002, 14:59
 */

package com.ethz.geobot.herbar.filter;

/**
 * This exception is thrown on a persistents problem occur on save and load of filters.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class FilterPersistentException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>FilterPersistentException</code> without detail message.
     */
    public FilterPersistentException() {
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FilterPersistentException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param msg   the detail message.
     * @param cause the exception that cause this exception
     */
    public FilterPersistentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an instance of <code>FilterPersistentException</code> with the specified detail message.
     *
     * @param cause the exception that cause this exception
     */
    public FilterPersistentException(Throwable cause) {
        super(cause);
    }
}
