package com.wegmueller.ups.webservice;

/**
 * Exception from a call to a subsystem
 *
 * The Name-Property of this is Exception is passed to the client
 */
public class UPSWebServiceException extends Throwable
{
    private final String name;

    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    public static final String SERVER_ERROR = "SERVER_ERROR";

    public static final String MISSING_DATA = "MISSING_DATA";

    public UPSWebServiceException( final String name )
    {
        super( name );
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
