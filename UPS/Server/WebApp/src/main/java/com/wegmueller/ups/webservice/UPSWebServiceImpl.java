package com.wegmueller.ups.webservice;

import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.ldap.LDAPAuthException;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.ILKAData;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.storage.StorageException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The Implementation of the Webservice */
public class UPSWebServiceImpl implements IUPSWebService
{
    private static final Logger LOG = LoggerFactory.getLogger( UPSWebServiceImpl.class );

    private static final boolean INFO = LOG.isInfoEnabled();

    private final IUPSServerService service;

    public UPSWebServiceImpl( final IUPSServerService s )
    {
        this.service = s;
    }

    public Boolean isClientVersionOk( final String application, final String version ) throws UPSServerException, RemoteException
    {
        return Boolean.TRUE;
    }

    public IPruefungsSession[] getPruefungsSessionen( final String userName, final String password ) throws UPSServerException
    {
        checkDataForDozent( userName, password );
        return getPruefungssessionen();
    }

    public byte[] submitPruefungsListe( final String seskz, final String lknumber, final String userName, final String password, final byte[] bytes ) throws UPSServerException
    {
        LOG.debug( userName );
        try
        {
            service.getLDAP().getUserAuthentication( userName, password );
            final ILDAPUserRecord list = service.getLDAP().getUserData( userName );
            LOG.info( list.toString() );
            final byte[] pdf = service.getUST().producePDF( userName, password, list, bytes );
            LOG.info( "produced pdf of length " + pdf.length );
            final Properties attributes = list.getAttributes();
            service.getStorage().storePruefungsListe( seskz, lknumber, userName, list.getStudentenNummer(), bytes, attributes, pdf );
            LOG.info( "storage of data for " + userName + " ok" );
            return pdf;
        }
        catch ( LDAPAuthException e )
        {
            throw new UPSServerException( UPSServerException.SERVER_ERROR, e );
        }
        catch ( UPSServerException e )
        {
            throw e;
        }
        catch ( Throwable e )
        {
            LOG.error( "Unknown error in Webservice", e );
            throw new UPSServerException( UPSServerException.SERVER_ERROR, e );
        }

    }

    public void checkDataForDozent( final String dozent, final String passwrd ) throws UPSServerException
    {
        if ( service.getLDAP().getUserData( dozent ) != null )
        {
            final String[] mapping = getOISCredentials( dozent, passwrd );
            if ( !service.getStorage().isStored( mapping[0] ) )
            {
                reloadOIS( mapping[0], mapping[1] );
            }
        }
    }

    private void checkSessionOfDozenz( final String dozent, final String passwrd, final String seskz ) throws UPSServerException
    {
        if ( service.getLDAP().getUserData( dozent ) != null )
        {
            final String[] mapping = getOISCredentials( dozent, passwrd );
            if ( !service.getStorage().isStored( seskz, mapping[0] ) )
            {
                // there is no data for this session and this user

                reloadOIS( mapping[0], mapping[1] );

                // Test if the data is now present
                if ( !service.getStorage().isStored( seskz, mapping[0] ) )
                {
                    // You tried to retrieve data from a passed Session, but didn't cache the data from ois
                    throw new UPSServerException( UPSServerException.MISSING_DATA + ": exam session for " + mapping[0] +
                            " could not be loaded from OIS." );
                }
            }
        }
    }

    private IPruefungsSession reloadOIS( final String dozent, final String passwrd ) throws UPSServerException
    {
        // reload the data from OIS
        final ILKAData data = service.getLKA().reload( dozent, passwrd );

        // Store the data in storage
        final IPruefungsSession ses = service.getStorage().storePruefungsSession( data.getPruefungsSession() );
        service.getStorage().storeAnmdeldeDaten( dozent, data.getPruefungsSession().getSeskez(), data.getAnmeldedaten() );
        return ses;
    }

    private String[] getOISCredentials( final String dozent, final String password ) throws StorageException
    {
        String[] mapping = service.getStorage().getOISAccount( dozent );
        if ( mapping == null )
        {
            mapping = new String[]{dozent, password};
        }
        return mapping;
    }

    public byte[] getPruefungsListe( final String dozent, final String passwrd, final String seskz, final String lk, final String studiNumber ) throws UPSServerException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        if ( !service.getStorage().isAngemeldet( mapping[0], seskz, lk, studiNumber ) )
        {
            // You tried to retrieve a Pruefungsliste from a user
            // which might be present, but according to the OIS Data
            // this student is not in one of your Pruefungen, so
            // i'll refuse to give the information
            throw new UPSServerException( UPSServerException.MISSING_DATA );
        }
        return service.getStorage().getPruefungsliste( seskz, lk, studiNumber );
    }

    public String addMapping( final String userName, final String password, final String ldapUser, final String oisUser, final String oisPassword ) throws UPSServerException
    {
        if ( !userName.equals( "dfrey" ) )
        {
            throw new UPSServerException( UPSServerException.INVALID_CREDENTIALS );
        }
        if ( service.getLDAP().getUserData( userName ) != null )
        {
            service.getStorage().mapOIS2LDAP( ldapUser, oisUser, oisPassword );
        }
        else
        {
            // Wrong credentials for this user
            throw new UPSServerException( UPSServerException.INVALID_CREDENTIALS );
        }
        return userName;
    }

    public IPruefungsSession reloadPruefungsDaten( final String dozent, final String passwrd ) throws UPSServerException, RemoteException
    {
        if ( service.getLDAP().getUserData( dozent ) != null )
        {
            final String[] mapping = getOISCredentials( dozent, passwrd );
            return reloadOIS( mapping[0], mapping[1] );
        }
        else
        {
            throw new UPSServerException( UPSServerException.INVALID_CREDENTIALS );
        }
    }

    public IPruefungsSession[] getPruefungssessionen() throws UPSServerException
    {
        return service.getStorage().getPruefungssessionen();
    }

    public Calendar[] getPruefungsDaten( final String dozent, final String passwrd, final String seskz, final String lkNumber ) throws UPSServerException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getPruefungsZeiten( seskz, lkNumber, mapping[0] );
    }

    public Calendar[] getPruefungsDaten( final String dozent, final String passwrd, final String seskz, final String[] lkNumber ) throws UPSServerException, RemoteException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getPruefungsZeiten( seskz, lkNumber, mapping[0] );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz, final String lkNumber, final Calendar cal ) throws UPSServerException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getAnmeldungen( seskz, mapping[0], lkNumber, cal );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz, final String[] lkNumber, final Calendar cal ) throws UPSServerException, RemoteException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getAnmeldungen( seskz, mapping[0], lkNumber, cal );
    }

    public IAnmeldedaten[] getAnmeldungen( final String dozent, final String passwrd, final String seskz ) throws UPSServerException, RemoteException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getAnmeldungen( seskz, mapping[0] );
    }

    public IPruefung[] getPruefungen( final String dozent, final String passwrd, final String seskz ) throws UPSServerException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        final String[] mapping = getOISCredentials( dozent, passwrd );
        return service.getStorage().getPruefungen( seskz, mapping[0] );
    }

    public String[] getStudisMitPruefunsliste( final String dozent, final String passwrd, final String seskz, final String lkNummer ) throws UPSServerException
    {
        checkSessionOfDozenz( dozent, passwrd, seskz );
        return service.getStorage().getStudisMitPruefungslisten( seskz, lkNummer );
    }

    public static void main( final String[] args ) throws UPSServerException, RemoteException
    {
        final UPSWebServiceImpl s = new UPSWebServiceImpl( new UPSServerService() );
//        s.addMapping("dfrey","leni1234","dfrey","baltisberger","HmBfDsSt!");
//        s.reloadPruefungsDaten("dfrey","leni1234");
        final SimpleDateFormat tf = new SimpleDateFormat( "HH:mm" );
        final SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
        final String user = "dfrey";
        final String pass = "leni1234";

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
                    System.out.println( "\t\tDatum: " + df.format( calendar.getTime() ) );
                    final IAnmeldedaten[] anmeldungen = s.getAnmeldungen( user, pass, seskz, lkNummer, calendar );
                    Arrays.sort( anmeldungen, new Comparator<IAnmeldedaten>()
                    {
                        public int compare( final IAnmeldedaten o1, final IAnmeldedaten o2 )
                        {
                            return o1.getPruefungsdatumVon().compareTo( o2.getPruefungsdatumVon() );
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
//        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen(user, pass, "2006H"));
//        UPSWebServiceUtil.dumpPruefungen(s.getPruefungen(user, pass, "2006H"));
//        final Calendar[] daten = s.getPruefungsDaten(user, pass, "2006H", "551-0004-01");

//        final byte[] b = s.submitPruefungsListe("2006H", "551-0004-01", "dfrey", "leni1234", "<list><string>Hallo</string></list>".getBytes());
//        System.out.println(b == null ? "null" : "" + b.length);

        //System.out.println( ws.getPruefungsSessionen("baltisberger","baltisberger")[0].getSessionsname() );
        //UPSWebServiceUtil.dumpAnmeldungen(ws.getAnmeldungen("baltisberger","baltisberger","2005H"));
        /*
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("baltisberger", "baltisberger","2005H", (String[]) null, new GregorianCalendar(2005, 9, 7, 0, 0)));
        System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("baltisberger","baltisberger","2005H", "551-0004-01",new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("baltisberger","baltisberger","2005H",new String[]{"551-0004-01","80-404"},new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
            */
    }

    public void open() throws StorageException
    {
        service.getStorage().open();
    }

    public void close()
    {
        try
        {
            service.getStorage().close();
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
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
}
