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

/**
 * Class to call the UPS Server remotly
 *
 * Author: Thomas Wegmüller Author: Daniel Frey
 */
public class UPSServerClient2 implements IUPSWebService {

    private UPSWebService2 webService;

    private XStream x;

    public UPSServerClient2() throws ServiceException, MalformedURLException {
        final UPSWebService2ServiceLocator l = new UPSWebService2ServiceLocator();
//        webService = l.getUPSWebService2(new URL("http://balti.ethz.ch:8080/upsserver4/UPSWebService2.jws"));
        final String ws = System.getProperty("ch.xmatrix.ups.webservice", "http://balti.ethz.ch:8080/upsserver4/UPSWebService2.jws");
//        final String ws = "http://localhost:8080/UPSWebService2.jws";
        webService = l.getUPSWebService2(new URL(ws));
//        webService = l.getUPSWebService2();
        x = new XStream();
    }

    public Boolean isClientVersionOk(final String application, final String version) throws UPSServerException, RemoteException {
        return (Boolean) this.invoke("isClientVersionOk", application, version);
    }

    public IPruefungsSession[] getPruefungsSessionen(final String userName, final String passWord) throws UPSServerException, RemoteException {
        return (IPruefungsSession[]) this.invoke("getPruefungsSessionen", userName, passWord);
    }

    public byte[] submitPruefungsListe(final String seskz, final String lknumber, final String userName, final String password, final byte[] bytes) throws UPSServerException, RemoteException {
        return (byte[]) this.invoke("submitPruefungsListe", seskz, lknumber, userName, password, bytes);
    }

    public byte[] getPruefungsListe(final String dozent, final String passwrd, final String seskz, final String lk, final String studiNumber) throws UPSServerException, RemoteException {
        return (byte[]) invoke("getPruefungsListe", dozent, passwrd, seskz, lk, studiNumber);
    }

    public String addMapping(final String userName, final String password, final String ldapUser, final String oisUser, final String oisPassword) throws UPSServerException, RemoteException {
        return (String) invoke(userName, password, ldapUser, oisUser, oisPassword);
    }

    public IPruefungsSession reloadPruefungsDaten(final String dozent, final String passwrd) throws UPSServerException, RemoteException {
        return (IPruefungsSession) invoke("reloadPruefungsDaten", dozent, passwrd);
    }

    public Calendar[] getPruefungsDaten(final String dozent, final String passwrd, final String seskz, final String lkNumber) throws UPSServerException, RemoteException {
        return (Calendar[]) invoke("getPruefungsDaten", dozent, passwrd, seskz, lkNumber);
    }

    public Calendar[] getPruefungsDaten(final String dozent, final String passwrd, final String seskz, final String[] lkNumber) throws UPSServerException, RemoteException {
        return (Calendar[]) invoke("getPruefungsDaten", dozent, passwrd, seskz, lkNumber);
    }

    public IAnmeldedaten[] getAnmeldungen(final String dozent, final String passwrd, final String seskz, final String lkNumber, final Calendar cal) throws UPSServerException, RemoteException {
        return (IAnmeldedaten[]) invoke("getAnmeldungen", dozent, passwrd, seskz, lkNumber, cal);
    }

    public IAnmeldedaten[] getAnmeldungen(final String dozent, final String passwrd, final String seskz, final String[] lkNumber, final Calendar cal) throws UPSServerException, RemoteException {
        Object o = lkNumber;
        if (lkNumber == null) {
            o = String[].class;
        }
        return (IAnmeldedaten[]) invoke("getAnmeldungen", dozent, passwrd, seskz, o, cal);
    }

    public IAnmeldedaten[] getAnmeldungen(final String dozent, final String passwrd, final String seskz) throws UPSServerException, RemoteException {
        return (IAnmeldedaten[]) invoke("getAnmeldungen", dozent, passwrd, seskz, String[].class, Calendar.class);
    }

    public IPruefung[] getPruefungen(final String dozent, final String passwrd, final String seskz) throws UPSServerException, RemoteException {
        return (IPruefung[]) invoke("getPruefungen", dozent, passwrd, seskz);
    }

    public String[] getStudisMitPruefunsliste(final String dozent, final String passwrd, final String seskz, final String lkNummer) throws RemoteException, UPSServerException {
        return (String[]) invoke("getStudisMitPruefunsliste", dozent, passwrd, seskz, lkNummer);
    }

    public Object invoke(final String s, final Object s1, final Object s2, final Object s3, final Object s4, final Object s5) throws UPSServerException, RemoteException {
        return invoke(s, new Object[]{s1, s2, s3, s4, s5});
    }

    public Object invoke(final String s, final Object s1, final Object s2, final Object s3, final Object s4) throws UPSServerException, RemoteException {
        return invoke(s, new Object[]{s1, s2, s3, s4});
    }

    public Object invoke(final String s, final Object s1, final Object s2, final Object s3) throws UPSServerException, RemoteException {
        return invoke(s, new Object[]{s1, s2, s3});
    }

    public Object invoke(final String s, final Object s1, final Object s2) throws UPSServerException, RemoteException {
        return invoke(s, new Object[]{s1, s2});
    }

    public Object invoke(final String s, final Object s1) throws UPSServerException, RemoteException {
        return invoke(s, new Object[]{s1});
    }

    public Object invoke(final String s, final Object[] s1) throws UPSServerException, RemoteException {
        final String[] args = new String[s1 == null ? 1 : (s1.length + 1)];
        args[0] = s;
        for (int i = 1; i < args.length; i++) {
            args[i] = x.toXML(s1[i - 1]);
        }
        final String r = webService.invoke(args);
        //System.out.println(r);
        final Object result = x.fromXML(r);
        if (result instanceof Throwable) {
            final Throwable t = (Throwable) result;
            t.printStackTrace();
            throw new UPSServerException(t);
        }
        return result;
    }

    public static void main(final String[] args) throws UPSServerException, ServiceException, RemoteException, MalformedURLException {

        final UPSServerClient2 s = new UPSServerClient2();
        final String user = "dfrey";
        final String pass = "leni1234";
//        s.reloadPruefungsDaten(user, pass);
//        System.exit(0);

        final SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        s.submitPruefungsListe("2006H", "551-0004-01", "dfrey", "leni1234", ("<list>\n" +
//                "  <string>Lycopodium annotinum</string>\n" +
//                "  <string>Equisetum arvense</string>\n" +
//                "  <string>Asplenium ruta-muraria</string>\n" +
//                "  <string>Asplenium trichomanes</string>\n" +
//                "  <string>Asplenium viride</string>\n" +
//                "</list>").getBytes());
//        final IPruefung[] p = s.getPruefungen(user, pass, "2006H");
//        final IAnmeldedaten[] a = s.getAnmeldungen(user, pass, "2006H");
//        System.exit(0);

        final IAnmeldedaten[] anmeldngen = (IAnmeldedaten[]) s.invoke("getAnmeldungen", user, pass, "2007F", "551-0004-01", Calendar.class);

        final IPruefungsSession[] sessionen = s.getPruefungsSessionen(user, pass);
        for (int i = 0; i < sessionen.length; i++) {
            final IPruefungsSession session = sessionen[i];
            final String seskz = session.getSeskez();
            System.out.println("Prüfungssession " + session.getSessionsname());
            System.out.println("\tId              : " + seskz);
            System.out.println("\tPlanungsfreigabe: " + df.format(session.getPlanungFreigabe().getTime()));
            System.out.println("\tSessionsende    : " + df.format(session.getSessionsende().getTime()));
            final IPruefung[] pruefungen = s.getPruefungen(user, pass, seskz);
            for (int j = 0; j < pruefungen.length; j++) {
                final IPruefung pruefung = pruefungen[j];
                System.out.println("\tPrüfung " + pruefung.getTitle());
                final String lkNummer = pruefung.getLKNummer();
                System.out.println("\t\tId: " + lkNummer);
                final Calendar[] daten = s.getPruefungsDaten(user, pass, seskz, lkNummer);
                for (int k = 0; k < daten.length; k++) {
                    final Calendar calendar = daten[k];
                    if (calendar != null) {
                        System.out.println("\t\tDatum: " + df.format(calendar.getTime()));
                        final IAnmeldedaten[] anmeldungen = s.getAnmeldungen(user, pass, seskz, lkNummer, calendar);
                        Arrays.sort(anmeldungen, new Comparator() {
                            public int compare(final Object o1, final Object o2) {
                                final IAnmeldedaten a1 = (IAnmeldedaten) o1;
                                final IAnmeldedaten a2 = (IAnmeldedaten) o2;
                                return a1.getPruefungsdatumVon().compareTo(a2.getPruefungsdatumVon());
                            }
                        });
                        final String[] pruefunslisten = s.getStudisMitPruefunsliste(user, pass, seskz, lkNummer);
                        for (int l = 0; l < anmeldungen.length; l++) {
                            final IAnmeldedaten anmeldung = anmeldungen[l];
                            final String studentennummer = anmeldung.getStudentennummer();
                            System.out.println("\t\t\t"
                                    + (isIn(pruefunslisten, studentennummer) ? "+" : "-") + "\t"
                                    + anmeldung.getVorname() + "\t"
                                    + anmeldung.getNachname() + "\t"
                                    + studentennummer + "\t"
                                    + anmeldung.getStudiengang() + "\t"
                                    + anmeldung.getPruefungsraum() + "\t"
                                    + tf.format(anmeldung.getPruefungsdatumVon().getTime()) + "\t"
                                    + tf.format(anmeldung.getPruefungsdatumBis().getTime()));
                        }
                    }
                }
            }
        }
        //System.out.println(s.isClientVersionOk("",""));
        //System.out.println( s.getPruefungsSessionen("baltisberger","baltisberger")[0].getSessionsname() );

        /*
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("dfrey","leni1234","2005H"));
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("dfrey", "leni1234","2005H", (String[])null, new GregorianCalendar(2005, 9, 7, 0, 0)));
        //System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("dfrey","leni1234","2005H", "551-0004-01",new GregorianCalendar(2005,9,7,0,0)));
        //System.out.println("");
        UPSWebServiceUtil.dumpAnmeldungen(s.getAnmeldungen("dfrey","leni1234","2005H",new String[]{"551-0004-01","80-404"},new GregorianCalendar(2005,9,7,0,0)));
        System.out.println("");
        */
//        final IPruefungsSession[] sessionen = s.getPruefungsSessionen("dfrey", "leni1234");
//        final Calendar[] daten = s.getPruefungsDaten("dfrey", "leni1234", "2006H", "551-0004-01");
//        final IPruefung[] pruefungen = s.getPruefungen("dfrey", "leni1234", "2006H");
//        System.out.println(sessionen.length);
//        byte[] b = s.getPruefungsListe("dfrey", "leni1234", "2005H", "79-880", "02-925-139");
//        System.out.println(b == null ? "null" : "" + b.length);
    }

    private static boolean isIn(final Object[] array, final Object element) {
        if (array == null || element == null) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            final Object o = array[i];
            if (o.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
