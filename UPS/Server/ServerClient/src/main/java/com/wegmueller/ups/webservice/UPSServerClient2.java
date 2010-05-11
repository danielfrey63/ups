package com.wegmueller.ups.webservice;

import com.thoughtworks.xstream.XStream;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.webservice.impl.UPSWebService2;
import com.wegmueller.ups.webservice.impl.UPSWebService2ServiceLocator;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import javax.xml.rpc.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to call the UPS Server remotly
 *
 * Author: Thomas Wegmüller Author: Daniel Frey
 */
public class UPSServerClient2 implements IUPSWebService
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( UPSServerClient2.class );

    private final UPSWebService2 webService;

    private final XStream xstream = new XStream();

    private static final String UPS_WEB_SERVICE = "ch.xmatrix.ups.webservice";

    public UPSServerClient2() throws UPSServerException
    {
        try
        {
            final UPSWebService2ServiceLocator l = new UPSWebService2ServiceLocator();
            final String ws = System.getProperty( UPS_WEB_SERVICE, "http://balti.ethz.ch:8080/upsserver4/UPSWebService2.jws" );
            LOG.info( "using web service at " + ws );
            webService = l.getUPSWebService2( new URL( ws ) );
        }
        catch ( ServiceException e )
        {
            throw new UPSServerException( "cannot instantiate UPSServerClient2", e );
        }
        catch ( MalformedURLException e )
        {
            throw new UPSServerException( "cannot instantiate UPSServerClient2", e );
        }
    }

    public Boolean isClientVersionOk( final String application, final String version ) throws UPSServerException
    {
        return (Boolean) this.invoke( "isClientVersionOk", application, version );
    }

    public IPruefungsSession[] getPruefungsSessionen( final String userName, final String passWord ) throws UPSServerException
    {
        return (IPruefungsSession[]) this.invoke( "getPruefungsSessionen", userName, passWord );
    }

    public byte[] submitPruefungsListe( final String seskz, final String lknumber, final String userName, final String password, final byte[] bytes ) throws UPSServerException
    {
        return (byte[]) this.invoke( "submitPruefungsListe", seskz, lknumber, userName, password, bytes );
    }

    public byte[] getPruefungsListe( final String dozent, final String passwrd, final String seskz, final String lk, final String studiNumber ) throws UPSServerException
    {
        return (byte[]) invoke( "getPruefungsListe", dozent, passwrd, seskz, lk, studiNumber );
    }

    public String addMapping( final String userName, final String password, final String ldapUser, final String oisUser, final String oisPassword ) throws UPSServerException
    {
        return (String) invoke( userName, password, ldapUser, oisUser, oisPassword );
    }

    public IPruefungsSession reloadPruefungsDaten( final String dozent, final String passwrd ) throws UPSServerException
    {
        return (IPruefungsSession) invoke( "reloadPruefungsDaten", dozent, passwrd );
    }

    public Calendar[] getPruefungsDaten( final String dozent, final String passwrd, final String seskz, final String lkNumber ) throws UPSServerException
    {
        return (Calendar[]) invoke( "getPruefungsDaten", dozent, passwrd, seskz, lkNumber );
    }

    public Calendar[] getPruefungsDaten( final String dozent, final String passwrd, final String seskz, final String[] lkNumber ) throws UPSServerException
    {
        return (Calendar[]) invoke( "getPruefungsDaten", dozent, passwrd, seskz, lkNumber );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz, final String lkNumber, final Calendar cal ) throws UPSServerException
    {
        return (IAnmeldedaten[]) invoke( "getAnmeldungen", dozent, passwrd, seskz, lkNumber, cal );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz, final String[] lkNumber, final Calendar cal ) throws UPSServerException
    {
        Object o = lkNumber;
        if ( lkNumber == null )
        {
            o = String[].class;
        }
        return (IAnmeldedaten[]) invoke( "getAnmeldungen", dozent, passwrd, seskz, o, cal );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz ) throws UPSServerException
    {
        return (IAnmeldedaten[]) invoke( "getAnmeldungen", dozent, passwrd, seskz, String[].class, Calendar.class );
    }

    public IPruefung[] getPruefungen( final String dozent, final String passwrd, final String seskz ) throws UPSServerException
    {
        return (IPruefung[]) invoke( "getPruefungen", dozent, passwrd, seskz );
    }

    public String[] getStudisMitPruefunsliste( final String dozent, final String passwrd, final String seskz, final String lkNummer ) throws UPSServerException
    {
        return (String[]) invoke( "getStudisMitPruefunsliste", dozent, passwrd, seskz, lkNummer );
    }

    public Object invoke( final String s, final Object s1, final Object s2, final Object s3, final Object s4, final Object s5 ) throws UPSServerException
    {
        return invoke( s, new Object[]{s1, s2, s3, s4, s5} );
    }

    public Object invoke( final String s, final Object s1, final Object s2, final Object s3, final Object s4 ) throws UPSServerException
    {
        return invoke( s, new Object[]{s1, s2, s3, s4} );
    }

    public Object invoke( final String s, final Object s1, final Object s2, final Object s3 ) throws UPSServerException
    {
        return invoke( s, new Object[]{s1, s2, s3} );
    }

    public Object invoke( final String s, final Object s1, final Object s2 ) throws UPSServerException
    {
        return invoke( s, new Object[]{s1, s2} );
    }

    public Object invoke( final String s, final Object s1 ) throws UPSServerException
    {
        return invoke( s, new Object[]{s1} );
    }

    public Object invoke( final String s, final Object[] s1 ) throws UPSServerException
    {
        try
        {
            final String[] args = new String[s1 == null ? 1 : ( s1.length + 1 )];
            args[0] = s;
            for ( int i = 1; i < args.length; i++ )
            {
                args[i] = xstream.toXML( s1[i - 1] );
            }
            final String r = webService.invoke( args );
            //System.out.println(r);
            final Object result = xstream.fromXML( r );
            if ( result instanceof Throwable )
            {
                final Throwable t = (Throwable) result;
                t.printStackTrace();
                throw new UPSServerException( t );
            }
            return result;
        }
        catch ( RemoteException e )
        {
            throw new UPSServerException( "cannot invoke " + s, e );
        }
        catch ( UPSServerException e )
        {
            throw new UPSServerException( "cannot invoke " + s, e );
        }
    }

    private static boolean isIn( final Object[] array, final Object element )
    {
        if ( array == null || element == null )
        {
            return false;
        }
        for ( final Object o : array )
        {
            if ( o.equals( element ) )
            {
                return true;
            }
        }
        return false;
    }

    public static void main( final String[] args ) throws UPSServerException, ServiceException, RemoteException, MalformedURLException
    {
        final UPSServerClient2 s = new UPSServerClient2();
        final String user = "dfrey";
        final String pass = "leni1234";

        final SimpleDateFormat tf = new SimpleDateFormat( "HH:mm" );
        final SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );

        final IPruefungsSession[] sessionen = s.getPruefungsSessionen( user, pass );
        for ( final IPruefungsSession session : sessionen )
        {
            final String seskz = session.getSeskez();
            System.out.println( "Prüfungssession " + session.getSessionsname() );
            System.out.println( "\tId              : " + seskz );
            System.out.println( "\tPlanungsfreigabe: " + df.format( session.getPlanungFreigabe().getTime() ) );
            System.out.println( "\tSessionsende    : " + df.format( session.getSessionsende().getTime() ) );
            final IPruefung[] pruefungen = s.getPruefungen( user, pass, seskz );
            for ( final IPruefung pruefung : pruefungen )
            {
                System.out.println( "\tPrüfung " + pruefung.getTitle() );
                final String lkNummer = pruefung.getLKNummer();
                System.out.println( "\t\tId: " + lkNummer );
                final Calendar[] daten = s.getPruefungsDaten( user, pass, seskz, lkNummer );
                for ( final Calendar calendar : daten )
                {
                    if ( calendar != null )
                    {
                        System.out.println( "\t\tDatum: " + df.format( calendar.getTime() ) );
                        final IAnmeldedaten[] anmeldungen = s.getAnmeldungen( user, pass, seskz, lkNummer, calendar );
                        Arrays.sort( anmeldungen, new Comparator()
                        {
                            public int compare( final Object o1, final Object o2 )
                            {
                                final IAnmeldedaten a1 = (IAnmeldedaten) o1;
                                final IAnmeldedaten a2 = (IAnmeldedaten) o2;
                                return a1.getPruefungsdatumVon().compareTo( a2.getPruefungsdatumVon() );
                            }
                        } );
                        final String[] pruefunslisten = s.getStudisMitPruefunsliste( user, pass, seskz, lkNummer );
                        for ( final IAnmeldedaten anmeldung : anmeldungen )
                        {
                            final String studentennummer = anmeldung.getStudentennummer();
                            System.out.println( "\t\t\t"
                                    + ( isIn( pruefunslisten, studentennummer ) ? "+" : "-" ) + "\t"
                                    + anmeldung.getVorname() + "\t"
                                    + anmeldung.getNachname() + "\t"
                                    + studentennummer + "\t"
                                    + anmeldung.getStudiengang() + "\t"
                                    + anmeldung.getPruefungsraum() + "\t"
                                    + tf.format( anmeldung.getPruefungsdatumVon().getTime() ) + "\t"
                                    + tf.format( anmeldung.getPruefungsdatumBis().getTime() ) );
                        }
                    }
                }
            }
        }
    }
}
