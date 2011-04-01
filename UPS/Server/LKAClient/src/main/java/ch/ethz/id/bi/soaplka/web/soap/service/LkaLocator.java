/**
 * LkaLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */
package ch.ethz.id.bi.soaplka.web.soap.service;

public class LkaLocator extends org.apache.axis.client.Service implements ch.ethz.id.bi.soaplka.web.soap.service.Lka
{

    public LkaLocator()
    {
    }

    public LkaLocator( org.apache.axis.EngineConfiguration config )
    {
        super( config );
    }

    public LkaLocator( java.lang.String wsdlLoc, javax.xml.namespace.QName sName ) throws javax.xml.rpc.ServiceException
    {
        super( wsdlLoc, sName );
    }

    // Use to get a proxy class for lkaSOAP12port_http

    private java.lang.String lkaSOAP12port_http_address = "http://ois-prd-red3:7080/soapLka-2010-1/services/lka";

    public java.lang.String getlkaSOAP12port_httpAddress()
    {
        return lkaSOAP12port_http_address;
    }

    // The WSDD service name defaults to the port name.

    private java.lang.String lkaSOAP12port_httpWSDDServiceName = "lkaSOAP12port_http";

    public java.lang.String getlkaSOAP12port_httpWSDDServiceName()
    {
        return lkaSOAP12port_httpWSDDServiceName;
    }

    public void setlkaSOAP12port_httpWSDDServiceName( java.lang.String name )
    {
        lkaSOAP12port_httpWSDDServiceName = name;
    }

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP12port_http() throws javax.xml.rpc.ServiceException
    {
        java.net.URL endpoint;
        try
        {
            endpoint = new java.net.URL( lkaSOAP12port_http_address );
        }
        catch ( java.net.MalformedURLException e )
        {
            throw new javax.xml.rpc.ServiceException( e );
        }
        return getlkaSOAP12port_http( endpoint );
    }

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP12port_http( java.net.URL portAddress ) throws javax.xml.rpc.ServiceException
    {
        try
        {
            ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP12BindingStub _stub = new ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP12BindingStub( portAddress, this );
            _stub.setPortName( getlkaSOAP12port_httpWSDDServiceName() );
            return _stub;
        }
        catch ( org.apache.axis.AxisFault e )
        {
            return null;
        }
    }

    public void setlkaSOAP12port_httpEndpointAddress( java.lang.String address )
    {
        lkaSOAP12port_http_address = address;
    }

    // Use to get a proxy class for lkaSOAP11port_http

    private java.lang.String lkaSOAP11port_http_address = "http://ois-prd-red3:7080/soapLka-2010-1/services/lka";

    public java.lang.String getlkaSOAP11port_httpAddress()
    {
        return lkaSOAP11port_http_address;
    }

    // The WSDD service name defaults to the port name.

    private java.lang.String lkaSOAP11port_httpWSDDServiceName = "lkaSOAP11port_http";

    public java.lang.String getlkaSOAP11port_httpWSDDServiceName()
    {
        return lkaSOAP11port_httpWSDDServiceName;
    }

    public void setlkaSOAP11port_httpWSDDServiceName( java.lang.String name )
    {
        lkaSOAP11port_httpWSDDServiceName = name;
    }

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP11port_http() throws javax.xml.rpc.ServiceException
    {
        java.net.URL endpoint;
        try
        {
            endpoint = new java.net.URL( lkaSOAP11port_http_address );
        }
        catch ( java.net.MalformedURLException e )
        {
            throw new javax.xml.rpc.ServiceException( e );
        }
        return getlkaSOAP11port_http( endpoint );
    }

    public ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType getlkaSOAP11port_http( java.net.URL portAddress ) throws javax.xml.rpc.ServiceException
    {
        try
        {
            ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP11BindingStub _stub = new ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP11BindingStub( portAddress, this );
            _stub.setPortName( getlkaSOAP11port_httpWSDDServiceName() );
            return _stub;
        }
        catch ( org.apache.axis.AxisFault e )
        {
            return null;
        }
    }

    public void setlkaSOAP11port_httpEndpointAddress( java.lang.String address )
    {
        lkaSOAP11port_http_address = address;
    }

    /** For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown. This service has multiple ports for a given interface; the proxy implementation returned may be indeterminate. */
    public java.rmi.Remote getPort( Class serviceEndpointInterface ) throws javax.xml.rpc.ServiceException
    {
        try
        {
            if ( ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType.class.isAssignableFrom( serviceEndpointInterface ) )
            {
                ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP12BindingStub _stub = new ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP12BindingStub( new java.net.URL( lkaSOAP12port_http_address ), this );
                _stub.setPortName( getlkaSOAP12port_httpWSDDServiceName() );
                return _stub;
            }
            if ( ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType.class.isAssignableFrom( serviceEndpointInterface ) )
            {
                ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP11BindingStub _stub = new ch.ethz.id.bi.soaplka.web.soap.service.LkaSOAP11BindingStub( new java.net.URL( lkaSOAP11port_http_address ), this );
                _stub.setPortName( getlkaSOAP11port_httpWSDDServiceName() );
                return _stub;
            }
        }
        catch ( java.lang.Throwable t )
        {
            throw new javax.xml.rpc.ServiceException( t );
        }
        throw new javax.xml.rpc.ServiceException( "There is no stub implementation for the interface:  " + ( serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName() ) );
    }

    /** For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown. */
    public java.rmi.Remote getPort( javax.xml.namespace.QName portName, Class serviceEndpointInterface ) throws javax.xml.rpc.ServiceException
    {
        if ( portName == null )
        {
            return getPort( serviceEndpointInterface );
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ( "lkaSOAP12port_http".equals( inputPortName ) )
        {
            return getlkaSOAP12port_http();
        }
        else if ( "lkaSOAP11port_http".equals( inputPortName ) )
        {
            return getlkaSOAP11port_http();
        }
        else
        {
            java.rmi.Remote _stub = getPort( serviceEndpointInterface );
            ( (org.apache.axis.client.Stub) _stub ).setPortName( portName );
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName()
    {
        return new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "lka" );
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts()
    {
        if ( ports == null )
        {
            ports = new java.util.HashSet();
            ports.add( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "lkaSOAP12port_http" ) );
            ports.add( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "lkaSOAP11port_http" ) );
        }
        return ports.iterator();
    }

    /** Set the endpoint address for the specified port name. */
    public void setEndpointAddress( java.lang.String portName, java.lang.String address ) throws javax.xml.rpc.ServiceException
    {

        if ( "lkaSOAP12port_http".equals( portName ) )
        {
            setlkaSOAP12port_httpEndpointAddress( address );
        }
        else if ( "lkaSOAP11port_http".equals( portName ) )
        {
            setlkaSOAP11port_httpEndpointAddress( address );
        }
        else
        { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException( " Cannot set Endpoint Address for Unknown Port" + portName );
        }
    }

    /** Set the endpoint address for the specified port name. */
    public void setEndpointAddress( javax.xml.namespace.QName portName, java.lang.String address ) throws javax.xml.rpc.ServiceException
    {
        setEndpointAddress( portName.getLocalPart(), address );
    }

}
