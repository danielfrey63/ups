package com.wegmueller.ups.ldap;

import com.wegmueller.ups.UPSServerException;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;

/**
 * AuthException for Exception within the process of LDAPAuthentication
 *
 * The process has 4 possible failure outcomes, these are accessible to a client by asking the
 * <see>getType()</see>-value: 1.) Username and password are invalid --> INVALID_CREDENTIALS 2.) No LDAP host is
 * accessible --> INFRASTRUCTURE_EXCEPTION 3.) There was a unknown condition in the Authenticate-code -->
 * PROGRAMMING_EXCEPTION 4.) There was a unknown (Throwable) Exception --> UNKNOWN_EXCEPTION
 */
public class LDAPAuthException extends UPSServerException
{
    public LDAPAuthException( final AuthenticationException e )
    {
        super( INVALID_CREDENTIALS, e );
    }

    public LDAPAuthException( final NamingException e )
    {
        super( INFRASTRUCTURE_EXCEPTION, e );
    }

    public LDAPAuthException( final String e )
    {
        super( e );
    }

    public LDAPAuthException( final Throwable lastThrowable )
    {
        super( UNKNOWN_EXCEPTION, lastThrowable );
    }

}
