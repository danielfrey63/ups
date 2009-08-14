package com.wegmueller.ups.lka;

import SoapLKAData.WsAnmeldedaten;
import SoapLKAService.WsSoapLKA;
import SoapLKAService.WsSoapLKAServiceLocator;
import com.wegmueller.ups.lka.data.LKAAnmeldedaten;
import com.wegmueller.ups.lka.data.LKAData;
import com.wegmueller.ups.lka.data.LKAPruefungssession;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

/** Created by: Thomas Wegmueller Date: 08.09.2005,  12:38:05 */
public class LKABusinessDelegate implements ILKABusinessDelegate
{

    private static final Logger log = Logger.getLogger(LKABusinessDelegate.class);

    //private static String SOAP_URL = "http://zo-bi-webtst1.ethz.ch:8080/soaplkanmeldung/services/LKA";
    public static final String SOAP_URL = "https://www.bi.id.ethz.ch/lka/v-1-0/services/v-1-0";

    public ILKAData reload(final String username, final String password) throws LKABusinessDelegateException
    {
        try
        {

            if (log.isDebugEnabled())
            {
                log.debug("LKABusinessDelegate.reload(...) for user " + username);
            }

            final String property = "username=" + username + ";password=" + password + ";simulation=false";
            final WsSoapLKAServiceLocator serviceLocator = new WsSoapLKAServiceLocator();
            final WsSoapLKA s = serviceLocator.getV10(new URL(SOAP_URL));

            final LKAPruefungssession ses = new LKAPruefungssession(s.retrieveAktuellePruefungssession(property));

            final WsAnmeldedaten[] a = s.retrieveAnmeldedaten(property, "*");

            final LKAAnmeldedaten[] amd = new LKAAnmeldedaten[a.length];
            for (int i = 0; i < a.length; i++)
            {
                amd[i] = new LKAAnmeldedaten(a[i]);
            }

            return new LKAData(ses, amd);

        }
        catch (ServiceException e)
        {
            throw new LKABusinessDelegateException(e);
        }
        catch (RemoteException e)
        {
            if (e.getMessage().startsWith("BusinessException-5529"))
            {
                throw new LKABusinessDelegateException(LKABusinessDelegateException.INVALID_CREDENTIALS, e);
            }
            throw new LKABusinessDelegateException(e);
        }
        catch (MalformedURLException e)
        {
            throw new LKABusinessDelegateException(e);
        }
    }
}
