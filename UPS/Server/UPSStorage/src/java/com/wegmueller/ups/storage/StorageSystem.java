package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.storage.hibernate.HibernateUtil;
import com.wegmueller.ups.storage.beans.PruefungsSession;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import com.wegmueller.ups.storage.beans.OIS2LDAP;
import com.wegmueller.ups.storage.beans.PruefungsListe;
import com.wegmueller.ups.storage.beans.ByteTypeContent;
import com.wegmueller.ups.webservice.UPSWebServiceUtil;

import java.util.Properties;
import java.util.Calendar;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

/**
 * The implementation of the Storage System for the User's Data
 */
public class StorageSystem implements IStorage {
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
    public void storePruefungsListe(String pruefungsSession, String pruefung, String uid, String studentenNummer, byte[] input, Properties userPropertes, byte[] pdf) throws StorageException {
        Session hibernate = getSession();
        try {
            PruefungsListe l = getPruefungslisteByStudentenNummer(pruefungsSession, pruefung, studentenNummer);
            if (l == null) {
                l = new PruefungsListe();
            }
            l.setUserName(uid);
            l.setPruefungsSession(pruefungsSession);
            l.setPruefung(pruefung);
            l.setStudentenNummer(studentenNummer);
            l.setUserProperties(userPropertes);
            l.setPruefungsListe(new ByteTypeContent(input));
            l.setPdf(new ByteTypeContent(pdf));
            hibernate.save(l);
            hibernate.flush();
        } catch (HibernateException e) {
            throw new StorageException(e);
        } catch (StorageException e) {
            throw e;
        }


    }


    /**
     * Gib den File inhalt als byte
     *
     * @param f File
     * @return
     * @throws IOException
     */
    private byte[] getFileBytes(File f) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(f);
        try {
            int ch;
            while ((ch = fis.read()) != -1) {
                bos.write(ch);
            }
        } finally {
            fis.close();
        }
        return bos.toByteArray();
    }

    public void importLegacyData(File home) throws IOException, StorageException {
        File[] f = home.listFiles();
        for (int i = 0; i < f.length; i++) {
            System.out.println("importing " + f[i].getName());
            try {
                importLegacyData(home, f[i].getName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (StorageException e) {
                throw e;
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
    public void importLegacyData(File home, String uid) throws IOException, StorageException {
        File ldapPropertiesFile = new File(home, uid + "/pruefling.properties");
        File pdfFile = new File(home, uid + "/pruefungsliste.pdf");
        File pruefungsListe = new File(home, uid + "/pruefungsliste.xml");

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(ldapPropertiesFile);
        try {
            prop.load(fis);
        } finally {
            fis.close();
        }
        byte[] pruL = getFileBytes(pruefungsListe);
        byte[] pdfB = getFileBytes(pdfFile);
        String studiNummer = prop.getProperty("carLicense", null);
        if (studiNummer == null) {
            throw new IOException("Studinummer ist null");
        }
        if (studiNummer.length() == 8) {
            studiNummer = studiNummer.substring(0, 2) + "-"
                          + studiNummer.substring(2, 5) + "-"
                          + studiNummer.substring(5);

        } else {
            throw new IOException("studiNummer in wrong format");
        }


        storePruefungsListe("2005H", "551-0004-01", uid, studiNummer,
                            pruL,
                            prop, pdfB);
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
    public byte[] getPruefungsliste(String pruefungsSession, String pruefung, String studentenNummer) throws StorageException {
        PruefungsListe l = getPruefungslisteByStudentenNummer(pruefungsSession, pruefung, studentenNummer);
        if (l == null) {
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
    public boolean isStored(String userName) throws StorageException {
        return isStored(userName, null);
    }

    /**
     * Sind Daten für diesen Dozenten
     *
     * @param session
     * @param userName
     * @return
     * @throws StorageException
     */
    public boolean isStored(String session, String userName) throws StorageException {
        try {
            Session hibernate = getSession();
            String sql = "select count(*) from Anmeldedaten ad where " +
                         "ad.dozentUserName=?" +
                         "";
            if (session != null) {
                sql += "and ad.seskez=? ";

            }
            Query q = hibernate.createQuery(sql);
            q.setString(0, userName);
            if (session != null) {
                q.setString(1, session);
            }

            List list = q.list();
            if (list.size() == 0) {
                return false;
            }
            Object obj = list.get(0);
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue() > 0;
            }
            if (obj instanceof Long) {
                return ((Long) obj).longValue() > 0;
            }
            if (obj instanceof Number) {
                return ((Number) obj).longValue() > 0;
            }
            throw new StorageException("unknown format of Number " + obj);
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public IPruefungsSession storePruefungsSession(IPruefungsSession session) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("from PruefungsSession where seskez=?");
            q.setString(0, session.getSeskez());
            List list = q.list();
            PruefungsSession bean;
            if (list.size() == 0) {
                bean = new PruefungsSession();
            } else {
                bean = (PruefungsSession) list.get(0);
            }
            bean.setPlanungFreigabe(session.getPlanungFreigabe());
            bean.setSeskez(session.getSeskez());
            bean.setSessionsende(session.getSessionsende());
            bean.setSessionsname(session.getSessionsname());
            bean.setStorageDate(new GregorianCalendar());
            hibernate.save(bean);
            hibernate.flush();
            return bean;
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public void storeAnmdeldeDaten(String userName, String seskez, IAnmeldedaten[] daten) throws StorageException {
        try {
            Session hibernate = getSession();
            Transaction tx = hibernate.beginTransaction();
            try {
                String hqlDelete = "delete Anmeldedaten where dozentUserName = :dozentUserName and seskez = :seskez";
                int deletedEntities = hibernate.createQuery(hqlDelete)
                        .setString("dozentUserName", userName)
                        .setString("seskez", seskez)
                        .executeUpdate();
                for (int i = 0; i < daten.length; i++) {
                    Anmeldedaten d = new Anmeldedaten();
                    d.setDozentUserName(userName);
                    d.setEmail(daten[i].getEmail());
                    d.setFachrichtung(daten[i].getFachrichtung());
                    d.setLkEinheitNummerzusatz(daten[i].getLkEinheitNummerzusatz());
                    d.setLkEinheitTitel(daten[i].getLkEinheitTitel());
                    d.setLkEinheitTyp(daten[i].getLkEinheitTyp());
                    d.setLkEinheitTypText(daten[i].getLkEinheitTypText());
                    d.setLkForm(daten[i].getLkForm());
                    d.setLkFormText(daten[i].getLkFormText());
                    d.setLkNummer(daten[i].getLkNummer());
                    d.setNachname(daten[i].getNachname());
                    d.setPruefungsdatum(convertToDay(daten[i].getPruefungsdatum()));
                    d.setPruefungsdatumBis(daten[i].getPruefungsdatumBis());
                    d.setPruefungsdatumVon(daten[i].getPruefungsdatumVon());
                    d.setPruefungsmodeText(daten[i].getPruefungsmodeText());
                    d.setPruefungsraum(daten[i].getPruefungsraum());
                    d.setRepetent(daten[i].isRepetent());
                    d.setSeskez(daten[i].getSeskez());
                    d.setStudentennummer(daten[i].getStudentennummer());
                    d.setStudiengang(daten[i].getStudiengang());
                    d.setVorname(daten[i].getVorname());
                    hibernate.save(d);
                }
                tx.commit();
                hibernate.flush();
            } catch (HibernateException e) {
                tx.rollback();
                throw e;
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }

    }

    private Calendar convertToDay(Calendar c) {
        return new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

    }

    public void mapOIS2LDAP(String ldap, String ois, String pass) throws StorageException {
        try {
            Session hibernate = getSession();
            OIS2LDAP bean = getMapping(ldap);
            if (bean == null) {
                bean = new OIS2LDAP();
            }
            bean.setLdap(ldap);
            bean.setOispassword(pass);
            bean.setOisusername(ois);
            hibernate.save(bean);
            hibernate.flush();
        } catch (StorageException e) {
            throw e;
        }


    }

    private OIS2LDAP getMapping(String ldap) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("from OIS2LDAP where ldap=?");
            q.setString(0, ldap);

            List list = q.list();
            if (list.size() == 0) {
                return null;
            }
            return (OIS2LDAP) list.get(0);
        } catch (HibernateException e) {
            throw new StorageException(e);
        }

    }

    public String[] getOISAccount(String userName) throws StorageException {
        OIS2LDAP ldap = getMapping(userName);
        if (ldap == null) {
            return null;
        }
        return new String[]{ldap.getOisusername(), ldap.getOispassword()};
    }

    public IPruefungsSession[] getPruefungssessionen() throws StorageException {
        List list = getList("from PruefungsSession");
        if (list.size() == 0) {
            return null;
        }
        IPruefungsSession[] ret = new IPruefungsSession[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = (PruefungsSession) list.get(i);
        }
        return ret;

    }

    public IPruefung[] getPruefungen(String seskez, String userName) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("select ad.lkNummer, min(ad.lkEinheitTitel) as lk from Anmeldedaten  ad where ad.seskez=? and ad.dozentUserName=? group by ad.lkNummer");
            q.setString(0, seskez);
            q.setString(1, userName);

            List list = q.list();
            if (list.size() == 0) {
                return null;
            }
            IPruefung[] ret = new IPruefung[list.size()];
            for (int i = 0; i < list.size(); i++) {
                final Object[] rec = (Object[]) list.get(i);

                ret[i] = new IPruefung() {
                    public String getLKNummer() {
                        return rec[0].toString();
                    }

                    public String getTitle() {
                        return rec[1].toString();
                    }
                };
            }
            return ret;
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public Calendar[] getPruefungsZeiten(String seskez, String userName) throws StorageException {
        return getPruefungsZeiten(seskez, (String[]) null, userName);
    }

    public Calendar[] getPruefungsZeiten(String seskez, String lkNummer, String userName) throws StorageException {
        return getPruefungsZeiten(seskez, new String[]{lkNummer}, userName);
    }


    public Calendar[] getPruefungsZeiten(String seskez, String[] lkNummer, String userName) throws StorageException {
        try {
            Session hibernate = getSession();
            String sql = "select ad.pruefungsdatum from Anmeldedaten  ad where ad.seskez=? " +
                         " and ad.dozentUserName=?";
            if ((lkNummer != null) && (lkNummer.length > 0)) {
                sql +=
                        " and ad.lkNummer IN ? ";
            }
            sql +=
                    " group by ad.pruefungsdatum order by ad.pruefungsdatum asc";
            Query q = hibernate.createQuery(sql);
            q.setString(0, seskez);
            q.setString(1, userName);
            if ((lkNummer != null) && (lkNummer.length > 0)) {
                q.setParameter(2, lkNummer);
            }
            List list = q.list();
            if (list.size() == 0) {
                return null;
            }
            Calendar[] ret = new Calendar[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ret[i] = (Calendar) list.get(i);
            }
            return ret;
        } catch (HibernateException e) {
            throw new StorageException(e);
        }

    }

    public IAnmeldedaten[] getAnmeldungen(String seskez, String userName, String lkNummer, Calendar c) throws StorageException {
        String[] arr = null;
        if (lkNummer != null) {
            arr = new String[]{lkNummer};
        }
        return getAnmeldungen(seskez, userName, arr, c);
    }


    private IAnmeldedaten[] getAnmeldungen() throws StorageException {
        return getAnmeldungen(null);
    }

    private IAnmeldedaten[] getAnmeldungen(String seskez) throws StorageException {
        return getAnmeldungen(seskez, null);
    }

    public IAnmeldedaten[] getAnmeldungen(String seskez, String userName) throws StorageException {
        return getAnmeldungen(seskez, userName, (String[]) null, null);
    }

    public IAnmeldedaten[] getAnmeldungen(String seskez, String userName, String lkNummer) throws StorageException {
        String[] arr = null;
        if (lkNummer != null) {
            arr = new String[]{lkNummer};
        }
        return getAnmeldungen(seskez, userName, arr);
    }

    public IAnmeldedaten[] getAnmeldungen(String seskez, String userName, String[] lkNummer) throws StorageException {
        return getAnmeldungen(seskez, userName, lkNummer, null);
    }


    public IAnmeldedaten[] getAnmeldungen(String seskez, String userName, String[] lkNummer, Calendar c) throws StorageException {
        try {
            Session hibernate = getSession();
            String sql = "from Anmeldedaten  ad where 1=1";
            if (seskez != null) {
                sql += " and ad.seskez=?";
            }
            if (userName != null) {
                sql += " and ad.dozentUserName=?";
            }
            if (c != null) {
                sql += " and ad.pruefungsdatum=?";
            }
            if (lkNummer != null) {
                sql += " and ad.lkNummer in (:lkNumber)";
            }
            sql += " order by ad.pruefungsdatumVon asc";
            Query q = hibernate.createQuery(sql);
            int idx = 0;
            if (seskez != null) {
                q.setString(idx++, seskez);
            }
            if (lkNummer != null) {
                q.setParameterList("lkNumber", lkNummer);
            }
            if (userName != null) {
                q.setString(idx++, userName);
            }
            if (c != null) {
                q.setCalendar(idx++, convertToDay(c));
            }
            List list = q.list();
            if (list.size() == 0) {
                return null;
            }
            IAnmeldedaten[] ret = new IAnmeldedaten[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ret[i] = (Anmeldedaten) list.get(i);
            }
            return ret;
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public IPruefungsSession getPruefungsSessionByID(String s) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("from PruefungsSession where seskez=?");
            q.setString(0, s);
            List list = q.list();
            if (list.size() == 0) {
                return null;
            } else {
                if (list.size() > 1) {
                    // Should never happen!!!
                    throw new StorageException("Session is stored more than once: " + s);
                }
                return (IPruefungsSession) list.get(0);
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public boolean isAngemeldet(String dozentUserName, String seskz, String lk, String studiNumber) throws StorageException {
        //Anmeldedaten
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("from Anmeldedaten where lkNummer=? and seskez=? and dozentUserName=? and studentennummer=?");
            q.setString(0, lk);
            q.setString(1, seskz);
            q.setString(2, dozentUserName);
            q.setString(3, studiNumber);
            List list = q.list();
            if (list.size() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public String getPrufungsTitel(String lk) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("select min(ad.lkEinheitTitel) from Anmeldedaten ad where ad.lkNummer=? group by ad.lkNummer");
            q.setString(0, lk);
            List list = q.list();
            PruefungsSession bean;
            if (list.size() == 0) {
                return null;
            } else {
                return (String) list.get(0);
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    private PruefungsListe getPruefungslisteByStudentenNummer(String pruefungsSession, String pruefung, String studentenNummer) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("from PruefungsListe where pruefungsSession=? and studentenNummer=?");// and pruefung=? ");
            q.setString(0, pruefungsSession);
            q.setString(1, studentenNummer);
            //TODO do not check as the lk is not correct!!!! q.setString(2, pruefung);
            List list = q.list();
            if (list.size() == 0) {
                return null;
            } else {
                if (list.size() > 1) {
                    throw new StorageException("Mehrere pruefungslisten für den user");
                }
                return (PruefungsListe) list.get(0);
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public String[] getStudisMitPruefungslisten(String pruefungsSession, String pruefung) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("select studentenNummer from PruefungsListe where pruefungsSession=? and pruefung=?");
            q.setString(0, pruefungsSession);
            q.setString(1, pruefung);
            List list = q.list();
            if (list.size() == 0) {
                return null;
            } else {
                String[] re = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    re[i] = (String) list.get(i);
                }
                return re;
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    private String[] getStudisMitPruefungslisten() throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery("select studentenNummer from PruefungsListe");
            List list = q.list();
            if (list.size() == 0) {
                return null;
            } else {
                String[] re = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    re[i] = (String) list.get(i);
                }
                return re;
            }
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }

    public List getList(String s) throws StorageException {
        try {
            Session hibernate = getSession();
            Query q = hibernate.createQuery(s);
            return q.list();
        } catch (HibernateException e) {
            throw new StorageException(e);
        }
    }


    public void open() throws StorageException {
        getSession();
    }

    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            session = null;
        }
    }

    private Session getSession() throws StorageException {
        if (session == null) {
            session = HibernateUtil.newSession();
        }
        return session;
    }

    public static void main(String[] args) throws StorageException, IOException {
        //System.setProperty("storage.db.location", "testdatabase");
        StorageSystem s = new StorageSystem();
        //s.importLegacyData(new File("C:\\daten\\90_Extern\\xmatrix\\repository\\UPS\\UPSServer\\project\\idea\\data"));
        //s.dumpAnmeldungen("2005H", "551-0004-01", "baltisberger");
        //s.dumpAnmeldungen("2005H","baltisberger");

        /*
        IAnmeldedaten[] anm = s.getAnmeldungen("2005H", "baltisberger");
        String[] stu = s.getStudisMitPruefungslisten();
        DumpUtils.dumpTest(anm,stu);
        */
        byte[] b = s.getPruefungsliste("2005H", "79-880", "02-925-139");
        System.out.println(b==null?"null":""+b.length);
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
