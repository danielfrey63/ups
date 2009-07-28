/**
 * UPSWebService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

public interface UPSWebService extends java.rmi.Remote {
    public byte[] submit(java.lang.String userName, java.lang.String password, byte[] bytes) throws java.rmi.RemoteException;
    public void throwAxisFault(java.lang.String param) throws java.rmi.RemoteException;
}
