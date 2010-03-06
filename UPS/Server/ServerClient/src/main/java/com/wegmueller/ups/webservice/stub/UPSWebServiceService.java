/**
 * UPSWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface UPSWebServiceService extends Service
{
    public String getUPSWebServiceAddress();

    public UPSWebService getUPSWebService() throws ServiceException;

    public UPSWebService getUPSWebService( URL portAddress ) throws ServiceException;
}
