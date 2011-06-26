package com.wegmueller.ups.lka;

import ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten;
import ch.ethz.id.bi.soaplka.web.soap.service.LkaLocator;
import ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType;
import com.wegmueller.ups.lka.data.LKAAnmeldedaten;
import com.wegmueller.ups.lka.data.LKAData;
import com.wegmueller.ups.lka.data.LKAPruefungssession;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

/** Created by: Thomas Wegmueller Date: 08.09.2005,  12:38:05 */
public class LKABusinessDelegate implements ILKABusinessDelegate
{
    private static final Logger LOG = Logger.getLogger( LKABusinessDelegate.class );

    public static final String SOAP_URL_PROPERTY = "ch.xmatrix.ups.server.lka.soap.url";

    public static final String SOAP_URL = System.getProperty( SOAP_URL_PROPERTY, "https://www.bi.id.ethz.ch/soapLka-2010-1/services/lka" );

    public LKABusinessDelegate()
    {
        final String axisSecureFactory = "axis.socketSecureFactory";
        System.setProperty( axisSecureFactory, "org.apache.axis.components.net.SunFakeTrustSocketFactory" );
        LOG.info( "using axis socket factory " + System.getProperty( axisSecureFactory ) );
        LOG.info( "using SOAP URL " + SOAP_URL );
    }

    public ILKAData reload( final String username, final String password ) throws LKABusinessDelegateException
    {
        try
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "reload OIS data for user " + username );
            }

            final String property = "username=" + username + ";password=" + password + ";simulation=false";
            final LkaLocator serviceLocator = new LkaLocator();
            final LkaPortType s = serviceLocator.getlkaSOAP11port_http( new URL( SOAP_URL ) );
            final LKAPruefungssession ses = new LKAPruefungssession( s.retrieveAktuellePruefungssession( property ) );
            final WsAnmeldedaten[] a = s.retrieveAnmeldedaten( property, "*" );
            final LKAAnmeldedaten[] amd = new LKAAnmeldedaten[a.length];
            for ( int i = 0; i < a.length; i++ )
            {
                amd[i] = new LKAAnmeldedaten( a[i] );
            }

            return new LKAData( ses, amd );
        }
        catch ( ServiceException e )
        {
            throw new LKABusinessDelegateException( e );
        }
        catch ( RemoteException e )
        {
            if ( e.getMessage().startsWith( "BusinessException-MSG_5529" ) )
            {
                throw new LKABusinessDelegateException( LKABusinessDelegateException.INVALID_CREDENTIALS, e );
            }
            throw new LKABusinessDelegateException( e );
        }
        catch ( MalformedURLException e )
        {
            throw new LKABusinessDelegateException( e );
        }
    }
}
