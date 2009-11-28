/**
 * WsSoapLKA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAService;

public interface WsSoapLKA extends java.rmi.Remote
{
    public java.lang.String getInterfaceVersion( java.lang.String info ) throws java.rmi.RemoteException;

    public SoapLKAData.WsPruefungssession retrieveAktuellePruefungssession( java.lang.String info ) throws java.rmi.RemoteException;

    public SoapLKAData.WsAnmeldedaten[] retrieveAnmeldedaten( java.lang.String info, java.lang.String lkeinheitNummer ) throws java.rmi.RemoteException;
}
