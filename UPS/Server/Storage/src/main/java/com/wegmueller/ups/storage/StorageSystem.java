package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import com.wegmueller.ups.storage.beans.ByteTypeContent;
import com.wegmueller.ups.storage.beans.OIS2LDAP;
import com.wegmueller.ups.storage.beans.PruefungsListe;
import com.wegmueller.ups.storage.beans.PruefungsSession;
import com.wegmueller.ups.storage.hibernate.HibernateUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * The implementation of the Storage System for the User's Data
 */
public class StorageSystem implements IStorage
{
    private Session session;

    /**
     * Speichert eine Pruefungsliste eines Studenten für eine
     *
     * @param pruefungsSession ID der Pruefungssession
     * @param pruefung         ID der Pruefung
     * @param uid              ID des Users
     * @param studentenNummer  Studentennummer
     * @param input            PruefungsListe (als byte[])
     * @param userPropertes    Ext. Properties des Users (aus LDAP)
     * @param pdf              PDF Report der Bestätigung (als byte[])
     * @throws StorageException
     */
    public void storePruefungsListe( final String pruefungsSession, final String pruefung, final String uid, final String studentenNummer, final byte[] input, final Properties userPropertes, final byte[] pdf ) throws StorageException
    {
        final Session hibernate = getSession();
        try
        {
            PruefungsListe l = getPruefungslisteByStudentenNummer( pruefungsSession, pruefung, studentenNummer );
            if ( l == null )
            {
                l = new PruefungsListe();
            }
            l.setUserName( uid );
            l.setPruefungsSession( pruefungsSession );
            l.setPruefung( pruefung );
            l.setStudentenNummer( studentenNummer );
            l.setUserProperties( userPropertes );
            l.setPruefungsListe( new ByteTypeContent( input ) );
            l.setPdf( new ByteTypeContent( pdf ) );
            hibernate.save( l );
            hibernate.flush();
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }

    }

    /**
     * Gib den File inhalt als byte
     *
     * @param f File
     * @return
     * @throws IOException
     */
    private byte[] getFileBytes( final File f ) throws IOException
    {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final FileInputStream fis = new FileInputStream( f );
        try
        {
            int ch;
            while ( ( ch = fis.read() ) != -1 )
            {
                bos.write( ch );
            }
        }
        finally
        {
            fis.close();
        }
        return bos.toByteArray();
    }

    public void importLegacyData( final File home ) throws IOException, StorageException
    {
        final File[] f = home.listFiles();
        for ( final File aF : f )
        {
            System.out.println( "importing " + aF.getName() );
            try
            {
                importLegacyData( home, aF.getName() );
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Importiere die Daten welche nach altem Format gespeichert sind
     *
     * @param home
     * @param uid
     * @throws IOException
     * @throws StorageException
     */
    public void importLegacyData( final File home, final String uid ) throws IOException, StorageException
    {
        final File ldapPropertiesFile = new File( home, uid + "/pruefling.properties" );
        final File pdfFile = new File( home, uid + "/pruefungsliste.pdf" );
        final File pruefungsListe = new File( home, uid + "/pruefungsliste.xml" );

        final Properties prop = new Properties();
        final FileInputStream fis = new FileInputStream( ldapPropertiesFile );
        try
        {
            prop.load( fis );
        }
        finally
        {
            fis.close();
        }
        final byte[] pruL = getFileBytes( pruefungsListe );
        final byte[] pdfB = getFileBytes( pdfFile );
        String studiNummer = prop.getProperty( "carLicense", null );
        if ( studiNummer == null )
        {
            throw new IOException( "Studinummer ist null" );
        }
        if ( studiNummer.length() == 8 )
        {
            studiNummer = studiNummer.substring( 0, 2 ) + "-"
                    + studiNummer.substring( 2, 5 ) + "-"
                    + studiNummer.substring( 5 );

        }
        else
        {
            throw new IOException( "studiNummer in wrong format" );
        }

        storePruefungsListe( "2005H", "551-0004-01", uid, studiNummer,
                pruL,
                prop, pdfB );
    }

    /**
     * Gib die Pruefungsliste eines Studenten zurück (oder null)
     *
     * @param pruefungsSession
     * @param pruefung
     * @param studentenNummer
     * @return
     * @throws StorageException
     */
    public byte[] getPruefungsliste( final String pruefungsSession, final String pruefung, final String studentenNummer ) throws StorageException
    {
        final PruefungsListe l = getPruefungslisteByStudentenNummer( pruefungsSession, pruefung, studentenNummer );
        if ( l == null )
        {
            return null;
        }
        return l.getPruefungsListe().getBytes();
    }

    /**
     * Sind daten für diesen Dozenten gespeichert
     *
     * @param userName
     * @return
     * @throws StorageException
     */
    public boolean isStored( final String userName ) throws StorageException
    {
        return isStored( userName, null );
    }

    /**
     * Sind Daten für diesen Dozenten
     *
     * @param session
     * @param userName
     * @return
     * @throws StorageException
     */
    public boolean isStored( final String session, final String userName ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            String sql = "select count(*) from Anmeldedaten ad where " +
                    "ad.dozentUserName=?" +
                    "";
            if ( session != null )
            {
                sql += "and ad.seskez=? ";

            }
            final Query q = hibernate.createQuery( sql );
            q.setString( 0, userName );
            if ( session != null )
            {
                q.setString( 1, session );
            }

            final List list = q.list();
            if ( list.size() == 0 )
            {
                return false;
            }
            final Object obj = list.get( 0 );
            if ( obj instanceof Integer )
            {
                return ( (Integer) obj ).intValue() > 0;
            }
            if ( obj instanceof Long )
            {
                return ( (Long) obj ).longValue() > 0;
            }
            if ( obj instanceof Number )
            {
                return ( (Number) obj ).longValue() > 0;
            }
            throw new StorageException( "unknown format of Number " + obj );
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public IPruefungsSession storePruefungsSession( final IPruefungsSession session ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "from PruefungsSession where seskez=?" );
            q.setString( 0, session.getSeskez() );
            final List list = q.list();
            final PruefungsSession bean;
            if ( list.size() == 0 )
            {
                bean = new PruefungsSession();
            }
            else
            {
                bean = (PruefungsSession) list.get( 0 );
            }
            bean.setPlanungFreigabe( session.getPlanungFreigabe() );
            bean.setSeskez( session.getSeskez() );
            bean.setSessionsende( session.getSessionsende() );
            bean.setSessionsname( session.getSessionsname() );
            bean.setStorageDate( new GregorianCalendar() );
            hibernate.save( bean );
            hibernate.flush();
            return bean;
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public void storeAnmdeldeDaten( final String userName, final String seskez, final IAnmeldedaten[] daten ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Transaction tx = hibernate.beginTransaction();
            try
            {
                final String hqlDelete = "delete Anmeldedaten where dozentUserName = :dozentUserName and seskez = :seskez";
                hibernate.createQuery( hqlDelete )
                        .setString( "dozentUserName", userName )
                        .setString( "seskez", seskez )
                        .executeUpdate();
                for ( final IAnmeldedaten aDaten : daten )
                {
                    final Anmeldedaten d = new Anmeldedaten();
                    d.setDozentUserName( userName );
                    d.setEmail( aDaten.getEmail() );
                    d.setFachrichtung( aDaten.getFachrichtung() );
                    d.setLkEinheitNummerzusatz( aDaten.getLkEinheitNummerzusatz() );
                    d.setLkEinheitTitel( aDaten.getLkEinheitTitel() );
                    d.setLkEinheitTyp( aDaten.getLkEinheitTyp() );
                    d.setLkEinheitTypText( aDaten.getLkEinheitTypText() );
                    d.setLkForm( aDaten.getLkForm() );
                    d.setLkFormText( aDaten.getLkFormText() );
                    d.setLkNummer( aDaten.getLkNummer() );
                    d.setNachname( aDaten.getNachname() );
                    final Calendar pruefungsdatum = aDaten.getPruefungsdatum();
                    if ( pruefungsdatum != null )
                    {
                        d.setPruefungsdatum( convertToDay( pruefungsdatum ) );
                    }
                    d.setPruefungsdatumBis( aDaten.getPruefungsdatumBis() );
                    d.setPruefungsdatumVon( aDaten.getPruefungsdatumVon() );
                    d.setPruefungsmodeText( aDaten.getPruefungsmodeText() );
                    d.setPruefungsraum( aDaten.getPruefungsraum() );
                    d.setRepetent( aDaten.isRepetent() );
                    d.setSeskez( aDaten.getSeskez() );
                    d.setStudentennummer( aDaten.getStudentennummer() );
                    d.setStudiengang( aDaten.getStudiengang() );
                    d.setVorname( aDaten.getVorname() );
                    hibernate.save( d );
                }
                tx.commit();
                hibernate.flush();
            }
            catch ( HibernateException e )
            {
                tx.rollback();
                throw e;
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }

    }

    private Calendar convertToDay( final Calendar c )
    {
        return new GregorianCalendar( c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) );

    }

    public void mapOIS2LDAP( final String ldap, final String ois, final String pass ) throws StorageException
    {
        final Session hibernate = getSession();
        OIS2LDAP bean = getMapping( ldap );
        if ( bean == null )
        {
            bean = new OIS2LDAP();
        }
        bean.setLdap( ldap );
        bean.setOispassword( pass );
        bean.setOisusername( ois );
        hibernate.save( bean );
        hibernate.flush();

    }

    private OIS2LDAP getMapping( final String ldap ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "from OIS2LDAP where ldap=?" );
            q.setString( 0, ldap );

            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            return (OIS2LDAP) list.get( 0 );
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }

    }

    public String[] getOISAccount( final String userName ) throws StorageException
    {
        final OIS2LDAP ldap = getMapping( userName );
        if ( ldap == null )
        {
            return null;
        }
        return new String[]{ldap.getOisusername(), ldap.getOispassword()};
    }

    public IPruefungsSession[] getPruefungssessionen() throws StorageException
    {
        final List list = getList( "from PruefungsSession" );
        if ( list.size() == 0 )
        {
            return null;
        }
        final IPruefungsSession[] ret = new IPruefungsSession[list.size()];
        for ( int i = 0; i < list.size(); i++ )
        {
            ret[i] = (PruefungsSession) list.get( i );
        }
        return ret;

    }

    public IPruefung[] getPruefungen( final String seskez, final String userName ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "select ad.lkNummer, min(ad.lkEinheitTitel) as lk from Anmeldedaten  ad where ad.seskez=? and ad.dozentUserName=? group by ad.lkNummer" );
            q.setString( 0, seskez );
            q.setString( 1, userName );

            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            final IPruefung[] ret = new IPruefung[list.size()];
            for ( int i = 0; i < list.size(); i++ )
            {
                final Object[] rec = (Object[]) list.get( i );

                final String n0 = rec[0] == null ? "" : rec[0].toString();
                final String n1 = rec[1] == null ? "" : rec[1].toString();

                ret[i] = new PruefungImpl( n0, n1 );
            }

            return ret;
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public Calendar[] getPruefungsZeiten( final String seskez, final String userName ) throws StorageException
    {
        return getPruefungsZeiten( seskez, (String[]) null, userName );
    }

    public Calendar[] getPruefungsZeiten( final String seskez, final String lkNummer, final String userName ) throws StorageException
    {
        return getPruefungsZeiten( seskez, new String[]{lkNummer}, userName );
    }

    public Calendar[] getPruefungsZeiten( final String seskez, final String[] lkNummer, final String userName ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            String sql = "select ad.pruefungsdatum from Anmeldedaten  ad where ad.seskez=? " +
                    " and ad.dozentUserName=?";
            if ( ( lkNummer != null ) && ( lkNummer.length > 0 ) )
            {
                sql += " and ad.lkNummer IN (:lkNummer) ";
            }
            sql +=
                    " group by ad.pruefungsdatum order by ad.pruefungsdatum asc";
            final Query q = hibernate.createQuery( sql );
            q.setString( 0, seskez );
            q.setString( 1, userName );
            if ( ( lkNummer != null ) && ( lkNummer.length > 0 ) )
            {
                q.setParameterList( "lkNummer", lkNummer );
            }
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            final Calendar[] ret = new Calendar[list.size()];
            for ( int i = 0; i < list.size(); i++ )
            {
                ret[i] = (Calendar) list.get( i );
            }
            return ret;
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }

    }

    public IAnmeldedaten[] getAnmeldungen( final String seskez, final String userName, final String lkNummer, final Calendar c ) throws StorageException
    {
        String[] arr = null;
        if ( lkNummer != null )
        {
            arr = new String[]{lkNummer};
        }
        return getAnmeldungen( seskez, userName, arr, c );
    }

    public IAnmeldedaten[] getAnmeldungen( final String seskez, final String userName ) throws StorageException
    {
        return getAnmeldungen( seskez, userName, (String[]) null, null );
    }

    public IAnmeldedaten[] getAnmeldungen( final String seskez, final String userName, final String lkNummer ) throws StorageException
    {
        String[] arr = null;
        if ( lkNummer != null )
        {
            arr = new String[]{lkNummer};
        }
        return getAnmeldungen( seskez, userName, arr );
    }

    public IAnmeldedaten[] getAnmeldungen( final String seskez, final String userName, final String[] lkNummer ) throws StorageException
    {
        return getAnmeldungen( seskez, userName, lkNummer, null );
    }

    public IAnmeldedaten[] getAnmeldungen( final String seskez, final String userName, final String[] lkNummer, final Calendar c ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            String sql = "from Anmeldedaten  ad where 1=1";
            if ( seskez != null )
            {
                sql += " and ad.seskez=?";
            }
            if ( userName != null )
            {
                sql += " and ad.dozentUserName=?";
            }
            if ( c != null )
            {
                sql += " and ad.pruefungsdatum=?";
            }
            if ( lkNummer != null )
            {
                sql += " and ad.lkNummer in (:lkNumber)";
            }
            sql += " order by ad.pruefungsdatumVon asc";
            final Query q = hibernate.createQuery( sql );
            int idx = 0;
            if ( seskez != null )
            {
                q.setString( idx++, seskez );
            }
            if ( lkNummer != null )
            {
                q.setParameterList( "lkNumber", lkNummer );
            }
            if ( userName != null )
            {
                q.setString( idx++, userName );
            }
            if ( c != null )
            {
                q.setCalendar( idx++, convertToDay( c ) );
            }
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            final IAnmeldedaten[] ret = new IAnmeldedaten[list.size()];
            for ( int i = 0; i < list.size(); i++ )
            {
                ret[i] = (Anmeldedaten) list.get( i );
            }
            return ret;
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public IPruefungsSession getPruefungsSessionByID( final String s ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "from PruefungsSession where seskez=?" );
            q.setString( 0, s );
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            else
            {
                if ( list.size() > 1 )
                {
                    // Should never happen!!!
                    throw new StorageException( "Session is stored more than once: " + s );
                }
                return (IPruefungsSession) list.get( 0 );
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public boolean isAngemeldet( final String dozentUserName, final String seskz, final String lk, final String studiNumber ) throws StorageException
    {
        //Anmeldedaten
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "from Anmeldedaten where lkNummer=? and seskez=? and dozentUserName=? and studentennummer=?" );
            q.setString( 0, lk );
            q.setString( 1, seskz );
            q.setString( 2, dozentUserName );
            q.setString( 3, studiNumber );
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public String getPrufungsTitel( final String lk ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "select min(ad.lkEinheitTitel) from Anmeldedaten ad where ad.lkNummer=? group by ad.lkNummer" );
            q.setString( 0, lk );
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            else
            {
                return (String) list.get( 0 );
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    private PruefungsListe getPruefungslisteByStudentenNummer( final String pruefungsSession, final String pruefung, final String studentenNummer ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( "from PruefungsListe where pruefungsSession=? and studentenNummer=?" );// and pruefung=? ");
            q.setString( 0, pruefungsSession );
            q.setString( 1, studentenNummer );
            //TODO do not check as the lk is not correct!!!! q.setString(2, pruefung);
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            else
            {
                if ( list.size() > 1 )
                {
                    throw new StorageException( "Mehrere pruefungslisten für den user" );
                }
                return (PruefungsListe) list.get( 0 );
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public String[] getStudisMitPruefungslisten( final String pruefungsSession, final String pruefung ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            String sql = "select studentenNummer from PruefungsListe where 1=1 ";
            if ( pruefungsSession != null )
            {
                sql += "and pruefungsSession=? ";
            }
            if ( pruefung != null )
            {
                sql += "and pruefung=? ";
            }
            final Query q = hibernate.createQuery( sql );
            if ( pruefungsSession != null )
            {
                q.setString( 0, pruefungsSession );
            }
            if ( pruefung != null )
            {
                q.setString( 1, pruefung );
            }
            final List list = q.list();
            if ( list.size() == 0 )
            {
                return null;
            }
            else
            {
                final String[] re = new String[list.size()];
                for ( int i = 0; i < list.size(); i++ )
                {
                    re[i] = (String) list.get( i );
                }
                return re;
            }
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public List getList( final String s ) throws StorageException
    {
        try
        {
            final Session hibernate = getSession();
            final Query q = hibernate.createQuery( s );
            return q.list();
        }
        catch ( HibernateException e )
        {
            throw new StorageException( e );
        }
    }

    public void open() throws StorageException
    {
        getSession();
    }

    public void close()
    {
        if ( session != null )
        {
            try
            {
                session.close();
            }
            catch ( Throwable e )
            {
                e.printStackTrace();
            }
            session = null;
        }
    }

    private Session getSession()
    {
        if ( session == null )
        {
            session = HibernateUtil.newSession();
        }
        return session;
    }

    public static void main( final String[] args ) throws StorageException
    {
        //System.setProperty("storage.db.location", "testdatabase");
        final StorageSystem s = new StorageSystem();
//        final byte[] pruefungsliste = s.getPruefungsliste("2007H", "551-0004-01", "123456789");
        final String list = "<root><list><string>Huperzia selago</string></list><uid>1</uid><exam>123</exam></root>";
        s.storePruefungsListe( "test", "test", "test", "test", list.getBytes(), new Properties(), new SimpleDateFormat( "HH:mm:ss SSS" ).format( new Date() ).getBytes() );
//        s.storePruefungsListe("2007H", "551-0004-01", "dfrey", "123456789", list.getBytes(), new Properties(), "Test".getBytes());
        s.close();

        //s.importLegacyData(new File("C:\\daten\\90_Extern\\xmatrix\\repository\\UPS\\UPSServer\\project\\idea\\data"));
        //s.dumpAnmeldungen("2005H", "551-0004-01", "baltisberger");
        //s.dumpAnmeldungen("2005H","baltisberger");

        /*
        IAnmeldedaten[] anm = s.getAnmeldungen("2005H", "baltisberger");
        String[] stu = s.getStudisMitPruefungslisten();
        DumpUtils.dumpTest(anm,stu);
        byte[] b = s.getPruefungsliste("2005H", "79-880", "02-925-139");
        System.out.println(b == null ? "null" : "" + b.length);
        */
        //DumpUtils.dumpCalendars(s.getPruefungsZeiten("2005H", "baltisberger"));

        /*
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("2005H", "baltisberger",(String[])null,new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("2005H", "baltisberger","551-0004-01",new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("2005H", "baltisberger",new String[]{"551-0004-01","80-404"},new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
        //UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("2005H", "baltisberger",(String[])null,new GregorianCalendar(2005,9,6,0,0)));
        */

        //s.getAnmeldungen("2005H","baltis");
        //s.dumpTest();
        //s.dumpStudisMitPruefungslisten();

    }
}
