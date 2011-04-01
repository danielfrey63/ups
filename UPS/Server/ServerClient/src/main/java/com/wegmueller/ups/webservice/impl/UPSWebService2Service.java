/**
 * UPSWebService2Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */
package com.wegmueller.ups.webservice.impl;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface UPSWebService2Service extends Service
{
    public String getUPSWebService2Address();

    public UPSWebService2 getUPSWebService2() throws ServiceException;

    public UPSWebService2 getUPSWebService2( URL portAddress ) throws ServiceException;
}
