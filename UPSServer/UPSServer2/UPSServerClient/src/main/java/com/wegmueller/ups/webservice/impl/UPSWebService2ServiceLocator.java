/**
 * UPSWebService2ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.impl;

public class UPSWebService2ServiceLocator extends org.apache.axis.client.Service implements com.wegmueller.ups.webservice.impl.UPSWebService2Service {

    public UPSWebService2ServiceLocator() {
    }


    public UPSWebService2ServiceLocator(final org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UPSWebService2ServiceLocator(final java.lang.String wsdlLoc, final javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UPSWebService2
    private java.lang.String UPSWebService2_address = "http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws";

    public java.lang.String getUPSWebService2Address() {
        return UPSWebService2_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UPSWebService2WSDDServiceName = "UPSWebService2";

    public java.lang.String getUPSWebService2WSDDServiceName() {
        return UPSWebService2WSDDServiceName;
    }

    public void setUPSWebService2WSDDServiceName(final java.lang.String name) {
        UPSWebService2WSDDServiceName = name;
    }

    public com.wegmueller.ups.webservice.impl.UPSWebService2 getUPSWebService2() throws javax.xml.rpc.ServiceException {
       final java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UPSWebService2_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUPSWebService2(endpoint);
    }

    public com.wegmueller.ups.webservice.impl.UPSWebService2 getUPSWebService2(final java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            final com.wegmueller.ups.webservice.impl.UPSWebService2SoapBindingStub _stub = new com.wegmueller.ups.webservice.impl.UPSWebService2SoapBindingStub(portAddress, this);
            _stub.setPortName(getUPSWebService2WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUPSWebService2EndpointAddress(final java.lang.String address) {
        UPSWebService2_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(final Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.wegmueller.ups.webservice.impl.UPSWebService2.class.isAssignableFrom(serviceEndpointInterface)) {
                final com.wegmueller.ups.webservice.impl.UPSWebService2SoapBindingStub _stub = new com.wegmueller.ups.webservice.impl.UPSWebService2SoapBindingStub(new java.net.URL(UPSWebService2_address), this);
                _stub.setPortName(getUPSWebService2WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(final javax.xml.namespace.QName portName, final Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        final java.lang.String inputPortName = portName.getLocalPart();
        if ("UPSWebService2".equals(inputPortName)) {
            return getUPSWebService2();
        }
        else  {
            final java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws", "UPSWebService2Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws", "UPSWebService2"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(final java.lang.String portName, final java.lang.String address) throws javax.xml.rpc.ServiceException {

if ("UPSWebService2".equals(portName)) {
            setUPSWebService2EndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(final javax.xml.namespace.QName portName, final java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
