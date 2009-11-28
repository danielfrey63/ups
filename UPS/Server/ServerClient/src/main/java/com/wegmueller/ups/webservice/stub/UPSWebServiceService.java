/**
 * UPSWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

public interface UPSWebServiceService extends javax.xml.rpc.Service
{
    public java.lang.String getUPSWebServiceAddress();

    public com.wegmueller.ups.webservice.stub.UPSWebService getUPSWebService() throws javax.xml.rpc.ServiceException;

    public com.wegmueller.ups.webservice.stub.UPSWebService getUPSWebService( java.net.URL portAddress ) throws javax.xml.rpc.ServiceException;
}
