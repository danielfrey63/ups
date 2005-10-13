package com.wegmueller.ups.webservice;

//import com.wegmueller.ups.webservice.dummy.PDFGenerator;

import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.UPSServerException;
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.ILKAData;
import com.wegmueller.ups.lka.IPruefung;
import com.wegmueller.ups.lka.IPruefungsSession;
import com.wegmueller.ups.storage.StorageException;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Properties;
import java.util.GregorianCalendar;
import java.rmi.RemoteException;


/**
 * The Implementation of the Webservice
 */
public class UPSWebServiceImpl implements IUPSWebService {

    private static final Logger log = Logger.getLogger(UPSWebServiceImpl.class);
    private IUPSServerService service;

    public UPSWebServiceImpl(IUPSServerService s) {
        this.service = s;
    }

    public Boolean isClientVersionOk(String application, String version) throws UPSServerException, RemoteException {
        return Boolean.TRUE;
    }

    public IPruefungsSession[] getPruefungsSessionen(String userName, String password) throws UPSServerException {
        checkDataForDozent(userName, password);
        return getPruefungssessionen();
    }


    public byte[] submitPruefungsListe(String seskz, String lknumber, String userName, String password, byte[] bytes) throws UPSServerException {
        log.debug("submit(" + userName + ",****,...)");
        try {
            ILDAPUserRecord list = service.getLDAP().getUserData(userName, password);
            log.debug("getUserData for " + userName + " ok");
            byte[] pdf = service.getUST().producePDF(userName, password, list, bytes);
            log.debug("pdf production for " + userName + " ok");
            Properties attributes = list.getAttributes();
            service.getStorage().storePruefungsListe(seskz, lknumber, userName, list.getStudentenNummer(), bytes, attributes, pdf);
            log.debug("storage of data for " + userName + " ok");
            return pdf;
        }
        catch (UPSServerException e) {
            throw e;
        }
        catch (Throwable e) {
            log.error("Unknown error in Webservice", e);
            throw new UPSServerException(UPSServerException.SERVER_ERROR, e);
        }

    }

    public void checkDataForDozent(String dozent, String passwrd) throws UPSServerException {
        if (service.getLDAP().getUserData(dozent, passwrd) != null) {
            String[] mapping = getOISCredentials(dozent, passwrd);
            if (!service.getStorage().isStored(mapping[0])) {
                reloadOIS(mapping[0], mapping[1]);
            }
        }
    }

    private void checkSessionOfDozenz(String dozent, String passwrd, String seskz) throws UPSServerException {
        if (service.getLDAP().getUserData(dozent, passwrd) != null) {
            String[] mapping = getOISCredentials(dozent, passwrd);
            if (!service.getStorage().isStored(seskz, mapping[0])) {
                // there is no data for this session and this user

                reloadOIS(mapping[0], mapping[1]);

                // Test if the data is now present
                if (!service.getStorage().isStored(seskz, mapping[0])) {
                    // You tried to retrieve data from a passed Session, but didn't cache the data from ois
                    throw new UPSServerException(UPSServerException.MISSING_DATA);
                }

            }
        }
    }

    private IPruefungsSession reloadOIS(String dozent, String passwrd) throws UPSServerException {
        // reload the data from OIS
        ILKAData data = service.getLKA().reload(dozent, passwrd);

        // Store the data in storage
        IPruefungsSession ses = service.getStorage().storePruefungsSession(data.getPruefungsSession());
        service.getStorage().storeAnmdeldeDaten(dozent, data.getPruefungsSession().getSeskez(), data.getAnmeldedaten());
        return ses;

    }

    private String[] getOISCredentials(String dozent, String password) throws StorageException {
        String[] mapping = service.getStorage().getOISAccount(dozent);
        if (mapping == null) {
            mapping = new String[]{dozent, password};
        }
        return mapping;
    }

    public byte[] getPruefungsListe(String dozent, String passwrd, String seskz, String lk, String studiNumber) throws UPSServerException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        if (!service.getStorage().isAngemeldet(mapping[0], seskz, lk, studiNumber)) {
            // You tried to retrieve a Pruefungsliste from a user
            // which might be present, but according to the OIS Data
            // this student is not in one of your Pruefungen, so
            // i'll refuse to give the information
            throw new UPSServerException(UPSServerException.MISSING_DATA);
        }
        return service.getStorage().getPruefungsliste(seskz, lk, studiNumber);
    }

    public String addMapping(String userName, String password, String ldapUser, String oisUser, String oisPassword) throws UPSServerException {
        if (!userName.equals("dfrey")) {
            throw new UPSServerException(UPSServerException.INVALID_CREDENTIALS);
        }
        if (service.getLDAP().getUserData(userName, password) != null) {
            service.getStorage().mapOIS2LDAP(ldapUser, oisUser, oisPassword);
        } else {
            // Wrong credentials for this user
            throw new UPSServerException(UPSServerException.INVALID_CREDENTIALS);
        }
        return userName;

    }

    public IPruefungsSession reloadPruefungsDaten(String dozent, String passwrd) throws UPSServerException, RemoteException {
        if (service.getLDAP().getUserData(dozent, passwrd) != null) {
            String[] mapping = getOISCredentials(dozent, passwrd);
            return reloadOIS(mapping[0], mapping[1]);
        } else {
            throw new UPSServerException(UPSServerException.INVALID_CREDENTIALS);
        }
    }


    public IPruefungsSession[] getPruefungssessionen() throws UPSServerException {
        return service.getStorage().getPruefungssessionen();
    }

    public Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String lkNumber) throws UPSServerException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getPruefungsZeiten(seskz, lkNumber, mapping[0]);
    }

    public Calendar[] getPruefungsDaten(String dozent, String passwrd, String seskz, String[] lkNumber) throws UPSServerException, RemoteException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getPruefungsZeiten(seskz, lkNumber, mapping[0]);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String lkNumber, Calendar cal) throws UPSServerException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getAnmeldungen(seskz, mapping[0], lkNumber, cal);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz, String[] lkNumber, Calendar cal) throws UPSServerException, RemoteException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getAnmeldungen(seskz, mapping[0], lkNumber, cal);
    }

    public IAnmeldedaten[] getAnmeldungen(String dozent, String passwrd, String seskz) throws UPSServerException, RemoteException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getAnmeldungen(seskz, mapping[0]);

    }

    public IPruefung[] getPruefungen(String dozent, String passwrd, String seskz) throws UPSServerException {
        checkSessionOfDozenz(dozent, passwrd, seskz);
        String[] mapping = getOISCredentials(dozent, passwrd);
        return service.getStorage().getPruefungen(seskz, mapping[0]);
    }


    public static void main(String[] args) throws UPSServerException, RemoteException {
        UPSWebServiceImpl s = new UPSWebServiceImpl(new UPSServerService());
        //s.addMapping("dfrey","leni1234","dfrey","baltisberger","HmBfDsSt!");
        //s.reloadPruefungsDaten("dfrey","leni1234");
        byte[] b = s.getPruefungsListe("dfrey", "leni1234", "2005H", "79-880", "02-925-139");
        System.out.println(b == null ? "null" : "" + b.length);

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

    public void open() throws StorageException {
        service.getStorage().open();
    }

    public void close() {
        try {
            service.getStorage().close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
