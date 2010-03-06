/**
 * UPSWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

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

public class UPSWebServiceServiceLocator extends Service implements UPSWebServiceService
{
    public UPSWebServiceServiceLocator()
    {
    }

    public UPSWebServiceServiceLocator( final EngineConfiguration config )
    {
        super( config );
    }

    public UPSWebServiceServiceLocator( final String wsdlLoc, final QName sName ) throws ServiceException
    {
        super( wsdlLoc, sName );
    }

    // Use to get a proxy class for UPSWebService
    public static final String DEFAULT_ADDRESS = "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws";

    private String UPSWebService_address = "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws";

    public String getUPSWebServiceAddress()
    {
        return UPSWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private String UPSWebServiceWSDDServiceName = "UPSWebService";

    public String getUPSWebServiceWSDDServiceName()
    {
        return UPSWebServiceWSDDServiceName;
    }

    public void setUPSWebServiceWSDDServiceName( final String name )
    {
        UPSWebServiceWSDDServiceName = name;
    }

    public UPSWebService getUPSWebService() throws ServiceException
    {
        final URL endpoint;
        try
        {
            endpoint = new URL( UPSWebService_address );
        }
        catch ( MalformedURLException e )
        {
            throw new ServiceException( e );
        }
        return getUPSWebService( endpoint );
    }

    public UPSWebService getUPSWebService( final URL portAddress ) throws ServiceException
    {
        try
        {
            final UPSWebServiceSoapBindingStub _stub = new UPSWebServiceSoapBindingStub( portAddress, this );
            _stub.setPortName( getUPSWebServiceWSDDServiceName() );
            return _stub;
        }
        catch ( AxisFault e )
        {
            return null;
        }
    }

    public void setUPSWebServiceEndpointAddress( final String address )
    {
        UPSWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then
     * ServiceException is thrown.
     */
    public Remote getPort( final Class serviceEndpointInterface ) throws ServiceException
    {
        try
        {
            if ( UPSWebService.class.isAssignableFrom( serviceEndpointInterface ) )
            {
                final UPSWebServiceSoapBindingStub _stub = new UPSWebServiceSoapBindingStub( new URL( UPSWebService_address ), this );
                _stub.setPortName( getUPSWebServiceWSDDServiceName() );
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
        if ( "UPSWebService".equals( inputPortName ) )
        {
            return getUPSWebService();
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
        return new QName( "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws", "UPSWebServiceService" );
    }

    private HashSet ports = null;

    public Iterator getPorts()
    {
        if ( ports == null )
        {
            ports = new HashSet();
            ports.add( new QName( "http://balti.ethz.ch:8080/upsserver/UPSWebService.jws", "UPSWebService" ) );
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress( final String portName, final String address ) throws ServiceException
    {
        if ( "UPSWebService".equals( portName ) )
        {
            setUPSWebServiceEndpointAddress( address );
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
