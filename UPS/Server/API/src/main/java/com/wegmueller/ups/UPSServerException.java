package com.wegmueller.ups;

/**
 * Created by: Thomas Wegmueller Date: 26.09.2005,  20:03:15
 */
public class UPSServerException extends Throwable
{
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    public static final String INFRASTRUCTURE_EXCEPTION = "INFRASTRUCTURE_EXCEPTION";

    public static final String UNKNOWN_EXCEPTION = "UNKNOWN_EXCEPTION";

    public static final String SERVER_ERROR = "SERVER_ERROR";

    public static final String MISSING_DATA = "MISSING_DATA";

    private final String name;

    public UPSServerException( final String s, final Throwable e )
    {
        super( e );
        name = s;
    }

    public UPSServerException( final String s )
    {
        super( s );
        this.name = s;
    }

    public UPSServerException( final Throwable e )
    {
        super( e );
        name = e.getMessage();
    }

    public String getName()
    {
        return name;
    }
}
