/**
 * Lka.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ch.ethz.id.bi.soaplka.web.soap.service;

public interface Lka extends javax.xml.rpc.Service
{
    public java.lang.String getlkaSOAP12port_httpAddress();

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP12port_http() throws javax.xml.rpc.ServiceException;

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP12port_http( java.net.URL portAddress ) throws javax.xml.rpc.ServiceException;

    public java.lang.String getlkaSOAP11port_httpAddress();

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP11port_http() throws javax.xml.rpc.ServiceException;

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP11port_http( java.net.URL portAddress ) throws javax.xml.rpc.ServiceException;
}
