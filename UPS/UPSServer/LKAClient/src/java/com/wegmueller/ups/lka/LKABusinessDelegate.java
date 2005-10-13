package com.wegmueller.ups.lka;

import SoapLKAData.WsAnmeldedaten;
import SoapLKAService.WsSoapLKA;
import SoapLKAService.WsSoapLKAServiceLocator;
import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.lka.data.LKAAnmeldedaten;
import com.wegmueller.ups.lka.data.LKAPruefungssession;
import com.wegmueller.ups.lka.data.LKAData;
import com.wegmueller.ups.storage.StorageException;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by: Thomas Wegmueller
 * Date: 08.09.2005,  12:38:05
 */
public class LKABusinessDelegate implements ILKABusinessDelegate {


    private static final Logger log = Logger.getLogger(LKABusinessDelegate.class);
    public static final String A1 = "53-335"; //WF F.u.V.Alp+AK Evol, Forstwissenschaften
    public static final String A2 = "551-0004-01"; //Systematische Biologie: Pflanzen I / Systematische Biologie: Pflanzen II,
    public static final String A3 = "61-080"; //Syst. Biol. I+II (P), Eidgenössisches Staatsexamen
    public static final String A4 = "80-404"; //Biosystematik
    public static final String A5 = "81-825"; //Biogeographie

    //private static String SOAP_URL = "http://zo-bi-webtst1.ethz.ch:8080/soaplkanmeldung/services/LKA";
    public static final String SOAP_URL="https://soap.bi.id.ethz.ch/lka/v-1-0/services/v-1-0";

    public ILKAData reload(String username, String password) throws LKABusinessDelegateException {
        try {

            if (log.isDebugEnabled()) {
                log.debug("LKABusinessDelegate.reload(...) for user " + username);
            }

            String property = "username=" + username + ";password=" + password + ";simulation=false";            
            WsSoapLKAServiceLocator serviceLocator = new WsSoapLKAServiceLocator();
            WsSoapLKA s = serviceLocator.getV10(new URL(SOAP_URL));

            LKAPruefungssession ses = new LKAPruefungssession(s.retrieveAktuellePruefungssession(property));



            WsAnmeldedaten[] a = s.retrieveAnmeldedaten(property, "*");

            LKAAnmeldedaten[] amd = new LKAAnmeldedaten[a.length];
            for (int i = 0; i < a.length; i++) {
                amd[i]=new LKAAnmeldedaten(a[i]);
            }

            return new LKAData(ses,amd);

        } catch (ServiceException e) {
            throw new LKABusinessDelegateException(e);
        } catch (RemoteException e) {
            if (e.getMessage().startsWith("BusinessException-5529")) {
                throw new LKABusinessDelegateException(LKABusinessDelegateException.INVALID_CREDENTIALS, e);                
            }
            throw new LKABusinessDelegateException(e);
        } catch (MalformedURLException e) {
            throw new LKABusinessDelegateException(e);
        }

    }


}
