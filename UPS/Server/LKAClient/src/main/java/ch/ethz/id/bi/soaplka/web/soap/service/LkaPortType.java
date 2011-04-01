/**
 * LkaPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */
package ch.ethz.id.bi.soaplka.web.soap.service;

public interface LkaPortType extends java.rmi.Remote
{
    public ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession retrieveAktuellePruefungssession( java.lang.String info ) throws java.rmi.RemoteException;

    public ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[] retrieveAnmeldedaten( java.lang.String info, java.lang.String lkeinheitNummer ) throws java.rmi.RemoteException;

    public java.lang.String getInterfaceVersion( java.lang.String info ) throws java.rmi.RemoteException;
}
