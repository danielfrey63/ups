/**
 * UPSWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

public class UPSWebServiceServiceLocator extends org.apache.axis.client.Service implements com.wegmueller.ups.webservice.stub.UPSWebServiceService {

    public UPSWebServiceServiceLocator() {
    }


    public UPSWebServiceServiceLocator(final org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UPSWebServiceServiceLocator(final java.lang.String wsdlLoc, final javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UPSWebService
    public static final java.lang.String DEFAULT_ADDRESS = "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws";
    private java.lang.String UPSWebService_address = "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws";

    public java.lang.String getUPSWebServiceAddress() {
        return UPSWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UPSWebServiceWSDDServiceName = "UPSWebService";

    public java.lang.String getUPSWebServiceWSDDServiceName() {
        return UPSWebServiceWSDDServiceName;
    }

    public void setUPSWebServiceWSDDServiceName(final java.lang.String name) {
        UPSWebServiceWSDDServiceName = name;
    }

    public com.wegmueller.ups.webservice.stub.UPSWebService getUPSWebService() throws javax.xml.rpc.ServiceException {
       final java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UPSWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUPSWebService(endpoint);
    }

    public com.wegmueller.ups.webservice.stub.UPSWebService getUPSWebService(final java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            final com.wegmueller.ups.webservice.stub.UPSWebServiceSoapBindingStub _stub = new com.wegmueller.ups.webservice.stub.UPSWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getUPSWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUPSWebServiceEndpointAddress(final java.lang.String address) {
        UPSWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(final Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.wegmueller.ups.webservice.stub.UPSWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                final com.wegmueller.ups.webservice.stub.UPSWebServiceSoapBindingStub _stub = new com.wegmueller.ups.webservice.stub.UPSWebServiceSoapBindingStub(new java.net.URL(UPSWebService_address), this);
                _stub.setPortName(getUPSWebServiceWSDDServiceName());
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
        if ("UPSWebService".equals(inputPortName)) {
            return getUPSWebService();
        }
        else  {
            final java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://balti.ethz.ch:8080/upsserver/UPSWebService.jws", "UPSWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://balti.ethz.ch:8080/upsserver/UPSWebService.jws", "UPSWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(final java.lang.String portName, final java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("UPSWebService".equals(portName)) {
            setUPSWebServiceEndpointAddress(address);
        }
        else { // Unknown Port Name
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
