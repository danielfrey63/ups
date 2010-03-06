/**
 * UPSWebService2ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class UPSWebService2ServiceLocator extends Service implements UPSWebService2Service
{
    public UPSWebService2ServiceLocator()
    {
    }

    public UPSWebService2ServiceLocator( final EngineConfiguration config )
    {
        super( config );
    }

    public UPSWebService2ServiceLocator( final String wsdlLoc, final QName sName ) throws ServiceException
    {
        super( wsdlLoc, sName );
    }

    // Use to get a proxy class for UPSWebService2
    private String UPSWebService2_address = "http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws";

    public String getUPSWebService2Address()
    {
        return UPSWebService2_address;
    }

    // The WSDD service name defaults to the port name.
    private String UPSWebService2WSDDServiceName = "UPSWebService2";

    public String getUPSWebService2WSDDServiceName()
    {
        return UPSWebService2WSDDServiceName;
    }

    public void setUPSWebService2WSDDServiceName( final String name )
    {
        UPSWebService2WSDDServiceName = name;
    }

    public UPSWebService2 getUPSWebService2() throws ServiceException
    {
        final URL endpoint;
        try
        {
            endpoint = new URL( UPSWebService2_address );
        }
        catch ( MalformedURLException e )
        {
            throw new ServiceException( e );
        }
        return getUPSWebService2( endpoint );
    }

    public UPSWebService2 getUPSWebService2( final URL portAddress ) throws ServiceException
    {
        try
        {
            final UPSWebService2SoapBindingStub _stub = new UPSWebService2SoapBindingStub( portAddress, this );
            _stub.setPortName( getUPSWebService2WSDDServiceName() );
            return _stub;
        }
        catch ( AxisFault e )
        {
            return null;
        }
    }

    public void setUPSWebService2EndpointAddress( final String address )
    {
        UPSWebService2_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public Remote getPort( final Class serviceEndpointInterface ) throws ServiceException
    {
        try
        {
            if ( UPSWebService2.class.isAssignableFrom( serviceEndpointInterface ) )
            {
                final UPSWebService2SoapBindingStub _stub = new UPSWebService2SoapBindingStub( new URL( UPSWebService2_address ), this );
                _stub.setPortName( getUPSWebService2WSDDServiceName() );
                return _stub;
            }
        }
        catch ( Throwable t )
        {
            throw new ServiceException( t );
        }
        throw new ServiceException( "There is no stub implementation for the interface:  " + ( serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName() ) );
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public Remote getPort( final QName portName, final Class serviceEndpointInterface ) throws ServiceException
    {
        if ( portName == null )
        {
            return getPort( serviceEndpointInterface );
        }
        final String inputPortName = portName.getLocalPart();
        if ( "UPSWebService2".equals( inputPortName ) )
        {
            return getUPSWebService2();
        }
        else
        {
            final Remote _stub = getPort( serviceEndpointInterface );
            ( (Stub) _stub ).setPortName( portName );
            return _stub;
        }
    }

    public QName getServiceName()
    {
        return new QName( "http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws", "UPSWebService2Service" );
    }

    private HashSet ports = null;

    public Iterator getPorts()
    {
        if ( ports == null )
        {
            ports = new HashSet();
            ports.add( new QName( "http://balti.ethz.ch:8080/upsserver2/UPSWebService2.jws", "UPSWebService2" ) );
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress( final String portName, final String address ) throws ServiceException
    {
        if ( "UPSWebService2".equals( portName ) )
        {
            setUPSWebService2EndpointAddress( address );
        }
        else
        { // Unknown Port Name
            throw new ServiceException( " Cannot set Endpoint Address for Unknown Port" + portName );
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress( final QName portName, final String address ) throws ServiceException
    {
        setEndpointAddress( portName.getLocalPart(), address );
    }

}
