package com.wegmueller.ups.ldap.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;

/*
 * A custom socket factory used to override the default socket factory.
 * It prints out debugging information before using default Socket creation
 * methods. This class is required as we don't want to deal with
 * SSL-Certificate of LDAP-Servier's
 */
public class CustomSocketFactory extends SocketFactory
{
    private static final Logger log = Logger.getLogger( CustomSocketFactory.class );

    private SSLContext sc;

    public CustomSocketFactory()
    {
        log.debug( "[creating a custom socket factory]" );
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager()
                {
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }

                    public void checkClientTrusted( final X509Certificate[] certs, final String authType )
                    {
                    }

                    public void checkServerTrusted( final X509Certificate[] certs, final String authType )
                    {
                    }
                }
        };

        // Install the all-trusting trust manager
        try
        {
            sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new SecureRandom() );
            //LdapCtxFactory..setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch ( Exception e )
        {
            log.error( "Error getting SSLContext.instance", e );
        }
    }

    public static SocketFactory getDefault()
    {
        log.debug( "[acquiring the default socket factory]" );
        return new CustomSocketFactory();
    }

    public Socket createSocket( final String host, final int port )
            throws IOException
    {
        log.debug( "[creating a custom socket (method 1)]" );
        return sc.getSocketFactory().createSocket( host, port );
    }

    public Socket createSocket( final String host, final int port, final InetAddress localHost,
                                final int localPort ) throws IOException
    {
        log.debug( "[creating a custom socket (method 2)]" );
        return sc.getSocketFactory().createSocket( host, port, localHost, localPort );
    }

    public Socket createSocket( final InetAddress host, final int port ) throws IOException
    {
        log.debug( "[creating a custom socket (method 3)]" );
        return sc.getSocketFactory().createSocket( host, port );
    }

    public Socket createSocket( final InetAddress address, final int port,
                                final InetAddress localAddress, final int localPort ) throws IOException
    {
        log.debug( "[creating a custom socket (method 4)]" );
        return sc.getSocketFactory().createSocket( address, port, localAddress, localPort );
    }
}

