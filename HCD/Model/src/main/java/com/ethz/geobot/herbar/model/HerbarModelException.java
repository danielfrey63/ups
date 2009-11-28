/*
 * Herbar CD-ROM Version 2
 *
 * HerbarModelException.java
 *
 * Created on 2. April 2002, 17:32
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;

/**
 * @author Daniel Frey
 */
public class HerbarModelException extends Exception
{
    /**
     * Creates a new instance of <code>HerbarModelException</code> without detail message.
     */
    public HerbarModelException()
    {
    }

    /**
     * Constructs an instance of <code>HerbarModelException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HerbarModelException( final String msg )
    {
        super( msg );
    }
}
