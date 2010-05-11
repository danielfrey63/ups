package com.wegmueller.ups;

import com.wegmueller.ups.ldap.LDAPAuthException;
import java.lang.reflect.InvocationTargetException;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  20:03:15 */
public class UPSServerException extends Throwable
{
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    public static final String INFRASTRUCTURE_EXCEPTION = "INFRASTRUCTURE_EXCEPTION";

    public static final String UNKNOWN_EXCEPTION = "UNKNOWN_EXCEPTION";

    public static final String SERVER_ERROR = "SERVER_ERROR";

    public static final String MISSING_DATA = "MISSING_DATA";

    private final String name;

    private String logMessage;

    private String feedbackMessage;

    private Throwable logThrowable;

    public UPSServerException( final String s, final Throwable e )
    {
        super( e );
        name = s;
        handleException();
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
        handleException();
    }

    public String getName()
    {
        return name;
    }

    private void handleException()
    {
        final Throwable cause1 = getCause();
        if ( cause1 != null )
        {
            final Throwable cause2 = getCause();
            if ( cause1 == cause2 )
            {
                logMessage = cause1.getMessage();
                logThrowable = cause1;
                feedbackMessage = logMessage;
            }
            else if ( cause2 instanceof InvocationTargetException )
            {
                final InvocationTargetException inner2 = (InvocationTargetException) cause2;
                final Throwable cause3 = inner2.getTargetException();
                if ( cause3 instanceof LDAPAuthException )
                {
                    logMessage = "unknown user or password: " + cause2.getMessage();
                    logThrowable = cause3;
                    feedbackMessage = "Name oder Passwort sind unbekannt";
                }
                else if ( cause3 instanceof UPSServerException )
                {
                    final UPSServerException inner3 = (UPSServerException) cause3;
                    if ( MISSING_DATA.equals( inner3.getName() ) )
                    {
                        logMessage = "no data for session and course";
                        logThrowable = inner3;
                        feedbackMessage = "Keine Daten vorhanden";
                    }
                    else
                    {
                        logMessage = cause1.getMessage() + ", caused by " + cause2.getMessage() + ", caused by " + cause3.getMessage();
                        logThrowable = cause3;
                        feedbackMessage = logMessage;
                    }
                }
                else
                {
                    logMessage = cause1.getMessage() + ", caused by " + cause2.getMessage() + ", caused by " + cause3.getMessage();
                    logThrowable = cause3;
                    feedbackMessage = logMessage;
                }
            }
            else
            {
                logMessage = cause1.getMessage() + ", caused by " + cause2.getMessage();
                logThrowable = cause2;
                feedbackMessage = logMessage;
            }
        }
        else
        {
            logMessage = getMessage();
            logThrowable = cause1;
            feedbackMessage = logMessage;
        }
    }

    public String getFeedbackMessage()
    {
        return feedbackMessage;
    }

    public Throwable getLogThrowable()
    {
        return logThrowable;
    }

    public String getLogMessage()
    {
        return logMessage;
    }
}
