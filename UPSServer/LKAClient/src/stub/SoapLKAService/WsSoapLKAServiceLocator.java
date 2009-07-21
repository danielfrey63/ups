/**
 * WsSoapLKAServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAService;

public class WsSoapLKAServiceLocator extends org.apache.axis.client.Service implements SoapLKAService.WsSoapLKAService {

    public WsSoapLKAServiceLocator() {
    }


    public WsSoapLKAServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsSoapLKAServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for V10
    private java.lang.String V10_address = "http://ois-prd-apa3:8080/soaplkanmeldung/services/v-1-0";

    public java.lang.String getV10Address() {
        return V10_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String V10WSDDServiceName = "v-1-0";

    public java.lang.String getV10WSDDServiceName() {
        return V10WSDDServiceName;
    }

    public void setV10WSDDServiceName(java.lang.String name) {
        V10WSDDServiceName = name;
    }

    public SoapLKAService.WsSoapLKA getV10() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(V10_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getV10(endpoint);
    }

    public SoapLKAService.WsSoapLKA getV10(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SoapLKAService.V10SoapBindingStub _stub = new SoapLKAService.V10SoapBindingStub(portAddress, this);
            _stub.setPortName(getV10WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setV10EndpointAddress(java.lang.String address) {
        V10_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SoapLKAService.WsSoapLKA.class.isAssignableFrom(serviceEndpointInterface)) {
                SoapLKAService.V10SoapBindingStub _stub = new SoapLKAService.V10SoapBindingStub(new java.net.URL(V10_address), this);
                _stub.setPortName(getV10WSDDServiceName());
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
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("v-1-0".equals(inputPortName)) {
            return getV10();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:SoapLKAService", "WsSoapLKAService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:SoapLKAService", "v-1-0"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("V10".equals(portName)) {
            setV10EndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
