/**
 * UPSWebService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UPSWebService extends Remote
{
    public byte[] submit( String userName, String password, byte[] bytes ) throws RemoteException;

    public void throwAxisFault( String param ) throws RemoteException;
}
