package com.wegmueller.ups.webservice;

import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

/** Created by: Thomas Wegmueller Date: 27.09.2005,  00:47:13 */
public class Test1 extends TestCase
{
    private IUPSWebService service;

    protected void setUp() throws Exception
    {
        System.setProperty("storage.db.location", "testdatabase");
        //System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        service = new UPSWebServiceImpl(new MockupService());
    }

    public void testStore1()
    {
        Throwable e = null;
        try
        {
            final byte[] pdf = service.submitPruefungsListe("2005H", "551-0004-01", "testuser1", "testuser1", "testuser1pl".getBytes());
            assertNotNull(pdf);
            assertEquals(new String(pdf), "testuser1testuser1");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        e = null;
        try
        {
            final byte[] pdf = service.submitPruefungsListe("2005H", "551-0004-01", "testuser1", "asdfasdf", "testuser1pl".getBytes());
        }
        catch (Throwable ex)
        {
            e = ex;
        }
        assertNotNull(e);

        e = null;
        try
        {
            final byte[] pdf = service.submitPruefungsListe("2005H", "551-0004-01", "asdfasdf", "asdfasdf", "testuser1pl".getBytes());
        }
        catch (Throwable ex)
        {
            e = ex;
        }
        assertNotNull(e);

        e = null;
        try
        {
            service.addMapping("dfrey", "leni1234", "dfrey", "baltisberger", "HmBfDsSt!");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final byte[] pl = service.getPruefungsListe("dfrey", "leni1234", "2005H", "551-0004-01", "01-909-100");
            assertNotNull(pl);
            assertEquals(new String(pl), "testuser1pl");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final byte[] pdf = service.submitPruefungsListe("2005H", "551-0004-01", "testuser1", "testuser1", "newpl".getBytes());
            assertNotNull(pdf);
            assertEquals(new String(pdf), "testuser1testuser1");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final byte[] pl = service.getPruefungsListe("dfrey", "leni1234", "2005H", "551-0004-01", "01-909-100");
            assertNotNull(pl);
            assertEquals(new String(pl), "newpl");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final byte[] pdf = service.submitPruefungsListe("2005H", "551-0004-01", "testuser2", "testuser2", "testuser2pl".getBytes());
            assertNotNull(pdf);
            assertEquals(new String(pdf), "testuser2testuser2");
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final Calendar[] c = service.getPruefungsDaten("dfrey", "leni1234", "2005H", "551-0004-01");
            assertNotNull(c);
            assertTrue(c.length > 0);
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final IPruefung[] c = service.getPruefungen("dfrey", "leni1234", "2005H");
            assertNotNull(c);
            assertTrue(c.length > 0);

            boolean found = false;
            for (int i = 0; i < c.length; i++)
            {
                if (c[i].getLKNummer().equalsIgnoreCase("551-0004-01"))
                {
                    assertEquals(c[i].getTitle(), "Systematische Biologie: Pflanzen I / Systematische Biologie: Pflanzen II");
                    found = true;
                }
            }
            assertTrue(found);
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);

        try
        {
            final Calendar cal = new GregorianCalendar(2005, 9, 11);
            final IAnmeldedaten[] c = service.getAnmeldungen("dfrey", "leni1234", "2005H", "551-0004-01", cal);
            assertNotNull(c);
            assertTrue(c.length > 0);

            boolean found = false;
            for (int i = 0; i < c.length; i++)
            {
                if (c[i].getStudentennummer().equalsIgnoreCase("01-909-100"))
                {
                    found = true;
                }
            }
            assertTrue(found);
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);
    }

    public void testStore2()
    {

    }
}
