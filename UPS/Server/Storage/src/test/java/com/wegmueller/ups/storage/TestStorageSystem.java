package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.storage.beans.PruefungsSession;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  21:28:04
 */
public class TestStorageSystem extends TestCase {
    private IStorage system;
    public static final String TEST_DOZENT = "baltisberger";
    public static final String TEST_SESSIONSNAME = "Test";
    private GregorianCalendar testDate; // some day not equal to current
    private Calendar testDate2; //some day later than testdate


    protected void setUp() throws Exception {
        testDate = new GregorianCalendar();
        testDate.add(Calendar.SECOND, -1);

        testDate2 = new GregorianCalendar();
        testDate2.setTime(testDate.getTime());
        testDate2.add(Calendar.DAY_OF_YEAR,1);
        System.setProperty("storage.db.location", "testdatabase");
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        system = new StorageSystem();
    }

    public void testReSave() {
        savePruefungsSession();
        savePruefungsSession();
    }


    public void testSaveAnmdeldung() {
        final TestAnmeldung d = new TestAnmeldung();
        d.setPruefungsdatum(testDate);
        d.setPruefungsdatumBis(testDate);
        d.setPruefungsdatumVon(testDate);
        //d.setDozentUserName("BaltisbergerMathias");
        Throwable e = null;
        try {
            system.storeAnmdeldeDaten(TEST_DOZENT, TestAnmeldung.SESKEZ, new IAnmeldedaten[]{d});
        } catch (StorageException ex) {
            e.printStackTrace();
            e = ex;
        }
        assertNull(e);


        Calendar[] am = new Calendar[0];
        try {
            am = system.getPruefungsZeiten(TestAnmeldung.SESKEZ, TestAnmeldung.LK_NUMMER, TEST_DOZENT);
            assertNotNull(am);
            assertEquals(1, am.length);
        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);

        try {
            final IAnmeldedaten[] ad = system.getAnmeldungen(TestAnmeldung.SESKEZ, TEST_DOZENT, TestAnmeldung.LK_NUMMER, testDate);
            assertNotNull(ad);
            assertEquals(1, ad.length);

            assertEquals(ad[0].getEmail(), TestAnmeldung.EMAIL);
            assertEquals(ad[0].getFachrichtung(), TestAnmeldung.FACHRICHTUNG);
            assertEquals(ad[0].getLkEinheitNummerzusatz(), TestAnmeldung.LK_EINHEIT_NUMMERZUSATZ);
            assertEquals(ad[0].getLkEinheitTitel(), TestAnmeldung.LK_EINHEIT_TITEL);
            assertEquals(ad[0].getLkEinheitTyp(), TestAnmeldung.LK_EINHEIT_TYP);
            assertEquals(ad[0].getLkEinheitTypText(), TestAnmeldung.LK_EINHEIT_TYP_TEXT);
            assertEquals(ad[0].getLkForm(), TestAnmeldung.LK_FORM);
            assertEquals(ad[0].getLkFormText(), TestAnmeldung.LK_FORM_TEXT);
            assertEquals(ad[0].getLkNummer(), TestAnmeldung.LK_NUMMER);
            assertEquals(ad[0].getNachname(), TestAnmeldung.NACHNAME);
            assertSameDay(ad[0].getPruefungsdatum(), testDate);
            assertEquals(ad[0].getPruefungsdatumBis(), testDate);
            assertEquals(ad[0].getPruefungsdatumVon(), testDate);
            assertEquals(ad[0].getPruefungsmodeText(), TestAnmeldung.PRUEFUNGSMODE_TEXT);
            assertEquals(ad[0].getPruefungsraum(), TestAnmeldung.PRUEFUNGSRAUM);
            assertEquals(ad[0].isRepetent(), TestAnmeldung.REPETENT);
            assertEquals(ad[0].getSeskez(), TestAnmeldung.SESKEZ);
            assertEquals(ad[0].getStudentennummer(), TestAnmeldung.STUDENTENNUMMER);
            assertEquals(ad[0].getStudiengang(), TestAnmeldung.STUDIENGANG);
            assertEquals(ad[0].getVorname(), TestAnmeldung.VORNAME);

        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);


        try {
            final IPruefung[] ad = system.getPruefungen(TestAnmeldung.SESKEZ, TEST_DOZENT);
            assertNotNull(ad);
            assertEquals(1, ad.length);
            assertEquals(TestAnmeldung.LK_NUMMER, ad[0].getLKNummer());
        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);
    }

    private void assertSameDay(final Calendar cal1, final Calendar cal2) {
        final GregorianCalendar c1 = new GregorianCalendar(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DAY_OF_MONTH));
        final GregorianCalendar c2 = new GregorianCalendar(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(c1,c2);
    }


    public void testSaveAnmdeldung2() {
        final String nummer1 ="nummer1";
        final String nummer2 ="nummer2";
        final String nummer3 ="nummer3";


        final TestAnmeldung d = new TestAnmeldung();
        d.setPruefungsdatum(testDate);
        d.setPruefungsdatumBis(testDate);
        d.setPruefungsdatumVon(testDate);
        d.setStudentennummer(nummer1);


        final TestAnmeldung d1 = new TestAnmeldung();
        d1.setPruefungsdatum(testDate2);
        d1.setPruefungsdatumBis(testDate2);
        d1.setPruefungsdatumVon(testDate2);
        d1.setStudentennummer(nummer2);
        //d.setDozentUserName("BaltisbergerMathias");

        final TestAnmeldung d2 = new TestAnmeldung();
        d2.setPruefungsdatum(testDate2);
        d2.setPruefungsdatumBis(testDate2);
        final GregorianCalendar c = new GregorianCalendar();
        c.setTime(testDate2.getTime());
        c.add(Calendar.HOUR, 1);
        d2.setPruefungsdatumVon(c);
        d2.setPruefungsdatumBis(c);
        d2.setStudentennummer(nummer3);

        //d.setDozentUserName("BaltisbergerMathias");
        Throwable e = null;
        try {
            system.storeAnmdeldeDaten(TEST_DOZENT, TestAnmeldung.SESKEZ, new IAnmeldedaten[]{d,d1,d2});
        } catch (StorageException ex) {
            e.printStackTrace();
            e = ex;
        }
        assertNull(e);



        try {
            final Calendar[] am = system.getPruefungsZeiten(TestAnmeldung.SESKEZ, TestAnmeldung.LK_NUMMER, TEST_DOZENT);
            assertNotNull(am);
            assertEquals(2, am.length);

            assertSameDay(am[0],testDate);
            assertSameDay(am[1],testDate2);
        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);

        try {
            IAnmeldedaten[] ad = system.getAnmeldungen(TestAnmeldung.SESKEZ, TEST_DOZENT, TestAnmeldung.LK_NUMMER, testDate);
            assertNotNull(ad);
            assertEquals(1, ad.length);

            assertEquals(nummer1,ad[0].getStudentennummer());

            ad = system.getAnmeldungen(TestAnmeldung.SESKEZ, TEST_DOZENT, TestAnmeldung.LK_NUMMER, testDate2);
            assertNotNull(ad);
            assertEquals(2, ad.length);

            assertEquals(nummer2,ad[0].getStudentennummer());
            assertEquals(nummer3,ad[1].getStudentennummer());

        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);


        try {
            final IPruefung[] ad = system.getPruefungen(TestAnmeldung.SESKEZ, TEST_DOZENT);
            assertNotNull(ad);
            assertEquals(1, ad.length);
            assertEquals(TestAnmeldung.LK_NUMMER, ad[0].getLKNummer());
        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);


        try {
            assertTrue( system.isStored(TestAnmeldung.SESKEZ, TEST_DOZENT) );
            assertFalse( system.isStored("ksahdfkajshdfkjahfds", TEST_DOZENT) );
        } catch (StorageException e1) {
            e1.printStackTrace();
            e = e1;
        }
        assertNull(e);
    }


    public void savePruefungsSession() {
        final PruefungsSession s = new PruefungsSession();
        s.setSeskez(TestAnmeldung.SESKEZ);
        s.setSessionsende(testDate);
        s.setSessionsname(TEST_SESSIONSNAME);
        s.setPlanungFreigabe(testDate);
        Throwable e = null;
        try {
            system.storePruefungsSession(s);
        } catch (Throwable ex) {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);
        try {
            final IPruefungsSession[] retSes = system.getPruefungssessionen();
            assertNotNull(retSes);
            assertTrue(retSes.length > 0);
            boolean found = false;
            for (int i = 0; i < retSes.length; i++) {
                if (retSes[i].getSeskez().equals(TestAnmeldung.SESKEZ)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (Throwable ex) {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try {
            final IPruefungsSession ps = system.getPruefungsSessionByID(TestAnmeldung.SESKEZ);


            assertNotNull(ps);
            assertEquals(ps.getSeskez(), TestAnmeldung.SESKEZ);
            assertEquals(ps.getSessionsende(), testDate);
            assertEquals(ps.getSessionsname(), TEST_SESSIONSNAME);
            assertEquals(ps.getPlanungFreigabe(), testDate);
            // test if the storage date is updated
            assertTrue(ps instanceof PruefungsSession);
            // only for 1.5 assertTrue(((PruefungsSession) ps).getStorageDate().compareTo(new GregorianCalendar()) < 0);
            // only for 1.5 assertTrue(((PruefungsSession) ps).getStorageDate().compareTo(testDate) > 0);
        } catch (Throwable ex) {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);
    }


    public void testMapping() {
        Throwable e = null;
        try {
            system.mapOIS2LDAP("dfrey",TEST_DOZENT,"abc");
            String[] ret = system.getOISAccount("dfrey");
            assertNotNull(ret);
            assertEquals(2,ret.length);
            assertEquals(TEST_DOZENT,ret[0]);
            assertEquals("abc",ret[1]);

            system.mapOIS2LDAP("dfrey",TEST_DOZENT,"def");
            ret = system.getOISAccount("dfrey");
            assertNotNull(ret);
            assertEquals(2,ret.length);
            assertEquals(TEST_DOZENT,ret[0]);
            assertEquals("def",ret[1]);

            assertEquals(system.getOISAccount("aksjfdhkasjhfdkjahdsfasfd"),null);
        } catch (StorageException ex) {
            e.printStackTrace();
            e = ex;
        }
        assertNull(e);
    }


    public void testStorePruefungsListe() {
        final byte[] pdf1 = "abc".getBytes();
        final byte[] pdf2 = "def".getBytes();
        final byte[] pl1 = "abc".getBytes();
        final byte[] pl2 = "def".getBytes();

        final Properties p1 = new Properties();
        p1.put("abc","abc");

        final Properties p2= new Properties();
        p2.put("abc","def");


        final Throwable e = null;
        try {
            system.storePruefungsListe(TestAnmeldung.SESKEZ,TestAnmeldung.LK_NUMMER,"dfrey","87-905-717-U",pl1,p1,pdf1);
            assertEquals(pl1,system.getPruefungsliste(TestAnmeldung.SESKEZ,TestAnmeldung.LK_NUMMER,"87-905-717-U"));

            system.storePruefungsListe(TestAnmeldung.SESKEZ,TestAnmeldung.LK_NUMMER,"dfrey","87-905-717-U",pl2,p2,pdf2);
            assertEquals(pl2,system.getPruefungsliste(TestAnmeldung.SESKEZ,TestAnmeldung.LK_NUMMER,"87-905-717-U"));
        } catch (StorageException ex) {
            ex.printStackTrace();
        }
        assertNull(e);


    }
}
