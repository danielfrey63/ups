package com.wegmueller.ups.webservice;


import com.wegmueller.ups.webservice.stub.UPSWebService;
import com.wegmueller.ups.webservice.stub.UPSWebServiceServiceLocator;
import org.apache.axis.AxisFault;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Class to call the UPS Server remotly
 *
 *
 */
class UPSServerClient {


    /**
     * Submit a Pruefungsliste and get back a PDF-File
     * @param userName
     * @param passWord
     * @param b                 the pruefungsliste as bytearray
     * @return                  pdf as byte array
     * @throws UPSServerClientException
     *      when userName/password is wrong, server is not available, another exception occurs
     */
    public static byte[] submitPruefungsListe(final String userName, final String passWord, final byte[] b) throws UPSServerClientException {
        try {
            return submitPruefungsListe(new URL(UPSWebServiceServiceLocator.DEFAULT_ADDRESS), userName, passWord, b);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static byte[] submitPruefungsListe(final URL url, final String userName, final String passWord, final byte[] b) throws UPSServerClientException {
        try {
            final UPSWebServiceServiceLocator serviceLocator = new UPSWebServiceServiceLocator();
            final UPSWebService service = serviceLocator.getUPSWebService(url);
            return service.submit(userName, passWord, b);
        } catch (ServiceException e) {
            throw new UPSServerClientException(UPSServerClientException.UNKNOWN_ERROR, e);
        } catch (AxisFault e) {
            if (e.getFaultReason().startsWith(UPSServerClientException.SERVER_NOT_AVAILABLE)) {
                throw new UPSServerClientException(UPSServerClientException.SERVER_NOT_AVAILABLE);
            } else if (e.getFaultString().equalsIgnoreCase(UPSServerClientException.INVALID_CREDENTIALS)) {
                throw new UPSServerClientException(UPSServerClientException.INVALID_CREDENTIALS);
            } else if (e.getFaultString().startsWith(UPSServerClientException.SERVER_ERROR)) {
                throw new UPSServerClientException(UPSServerClientException.SERVER_ERROR);
            } else {
                throw new UPSServerClientException(UPSServerClientException.UNKNOWN_ERROR, e);
            }
        } catch (RemoteException e) {
            throw new UPSServerClientException(UPSServerClientException.UNKNOWN_ERROR, e);
        }

    }

}