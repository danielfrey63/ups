package com.wegmueller.ups.webservice;


import com.thoughtworks.xstream.XStream;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.webservice.impl.UPSWebService2;
import com.wegmueller.ups.webservice.impl.UPSWebService2ServiceLocator;

import javax.xml.rpc.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Class to call the UPS Server remotly
 *
 *
 */
public class UPSServerClient2 implements IUPSWebService {
    private UPSWebService2 webService;


    private XStream x;
    public UPSServerClient2() throws ServiceException, MalformedURLException {
        UPSWebService2ServiceLocator
                l = new UPSWebService2ServiceLocator();
        //webService = l.getUPSWebService2(new URL("http://geobot1.ethz.ch:8080/upsserver2/UPSWebService2.jws"));
        webService = l.getUPSWebService2();
        x = new XStream();
    }

    public Boolean isClientVersionOk(String application, String version) throws UPSServerException, RemoteException {
        return (Boolean) this.invoke("isClientVersionOk",application,version);
    }

    public IPruefungsSession[] getPruefungsSessionen(String userName, String passWord) throws UPSServerException, RemoteException {
        return (IPruefungsSession[]) this.invoke("getPruefungsSessionen",userName,passWord);
    }

    public byte[] submitPruefungsListe(String seskz, String lknumber, String userName, String password, byte[] bytes) throws UPSServerException, RemoteException {
        return (byte[]) this.invoke("submitPruefungsListe",seskz,lknumber,userName,password,bytes);
    }



    public byte[] getPruefungsListe(String dozent, String passwrd, String seskz, String lk, String studiNumber) throws UPSServerException, RemoteException {
        return (byte[]) invoke("getPruefungsListe",dozent,passwrd,seskz,lk,studiNumber);
    }

    public String addMapping(String userName, String password, String ldapUser, String oisUser, String oisPassword) throws UPSServerException, RemoteException {
        return (String) invoke(userName,password,ldapUser,oisUser,oisPassword);

    }

    public IPruefungsSession reloadPruefungsDaten(String dozent, String passwrd) throws UPSServerException, RemoteException {
        return (IPruefungsSession) invoke("reloadPruefungsDaten",dozent,passwrd);
    }

    public Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String lkNumber) throws UPSServerException, RemoteException {
        return (Calendar[]) invoke("getPruefungsDaten",dozent,passwrd,seskz,lkNumber);
    }

    public Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String[] lkNumber) throws UPSServerException, RemoteException {
        return (Calendar[]) invoke("getPruefungsDaten",dozent,passwrd,seskz,lkNumber);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String lkNumber, Calendar cal) throws UPSServerException, RemoteException {
        Object arr = String[].class;
        if (lkNumber!=null) arr = new String[]{lkNumber};

        return (IAnmeldedaten[]) invoke("getAnmeldungen",dozent,passwrd,seskz,lkNumber,cal);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String[] lkNumber, Calendar cal) throws UPSServerException, RemoteException {
        Object o = lkNumber;
        if (lkNumber==null) {
            o = String[].class;

        }
        return (IAnmeldedaten[]) invoke("getAnmeldungen",dozent,passwrd,seskz,o,cal);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz) throws UPSServerException, RemoteException {
        return (IAnmeldedaten[]) invoke("getAnmeldungen",dozent,passwrd,seskz,String[].class,Calendar.class);
    }

    public IPruefung[] getPruefungen(String dozent, String passwrd, String seskz) throws UPSServerException, RemoteException {
        return (IPruefung[]) invoke("getPruefungen",dozent,passwrd,seskz);
    }

    public Object invoke(String s, Object s1, Object s2, Object s3, Object s4, Object s5) throws UPSServerException, RemoteException {
         return invoke(s,new Object[]{s1,s2,s3,s4,s5});
     }
    public Object invoke(String s, Object s1, Object s2, Object s3, Object s4) throws UPSServerException, RemoteException {
         return invoke(s,new Object[]{s1,s2,s3,s4});
     }
    public Object invoke(String s, Object s1, Object s2, Object s3) throws UPSServerException, RemoteException {
         return invoke(s,new Object[]{s1,s2,s3});
     }
      public Object invoke(String s, Object s1, Object s2) throws UPSServerException, RemoteException {
        return invoke(s,new Object[]{s1,s2});
    }
    public Object invoke(String s, Object s1) throws UPSServerException, RemoteException {
        return invoke(s,new Object[]{s1});
    }
    public Object invoke(String s, Object[] s1) throws UPSServerException, RemoteException {
        String[] args = new String[s1==null?1:(s1.length+1)];
        args[0] = s;
        for (int i=1;i<args.length;i++) {
            args[i] = x.toXML(s1[i-1]);
        }
        String r =  webService.invoke(args);
        //System.out.println(r);
        if (r.startsWith("<java.lang.reflect.InvocationTargetException>") ) {
            InvocationTargetException t =  (InvocationTargetException) x.fromXML(r);
            t.printStackTrace();
            throw (UPSServerException)t.getCause();
        }
        return x.fromXML( r );
    }


    public static void main(String[] args) throws UPSServerException, ServiceException, RemoteException, MalformedURLException {

        UPSServerClient2 s = new UPSServerClient2();
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
        byte[] b = s.getPruefungsListe("dfrey","leni1234","2005H","79-880","02-925-139");
        System.out.println(b==null?"null":""+b.length);

    }




}