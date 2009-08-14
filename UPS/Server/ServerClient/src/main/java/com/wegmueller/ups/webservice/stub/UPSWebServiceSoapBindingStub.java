/**
 * UPSWebServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package com.wegmueller.ups.webservice.stub;

public class UPSWebServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.wegmueller.ups.webservice.stub.UPSWebService
{
    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static
    {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1()
    {
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("submit");
        oper.addParameter(new javax.xml.namespace.QName("", "userName"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "password"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "bytes"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "submitReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("throwAxisFault");
        oper.addParameter(new javax.xml.namespace.QName("", "param"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

    }

    public UPSWebServiceSoapBindingStub() throws org.apache.axis.AxisFault
    {
        this(null);
    }

    public UPSWebServiceSoapBindingStub(final java.net.URL endpointURL, final javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
    {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public UPSWebServiceSoapBindingStub(final javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
    {
        if (service == null)
        {
            super.service = new org.apache.axis.client.Service();
        }
        else
        {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException
    {
        try
        {
            final org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet)
            {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null)
            {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null)
            {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null)
            {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null)
            {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null)
            {
                _call.setPortName(super.cachedPortName);
            }
            final java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements())
            {
                final java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            return _call;
        }
        catch (java.lang.Throwable _t)
        {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public byte[] submit(final java.lang.String userName, final java.lang.String password, final byte[] bytes) throws java.rmi.RemoteException
    {
        if (super.cachedEndpoint == null)
        {
            throw new org.apache.axis.NoEndPointException();
        }
        final org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://DefaultNamespace", "submit"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try
        {
            final java.lang.Object _resp = _call.invoke(new java.lang.Object[]{userName, password, bytes});

            if (_resp instanceof java.rmi.RemoteException)
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments(_call);
                try
                {
                    return (byte[]) _resp;
                }
                catch (java.lang.Exception _exception)
                {
                    return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
                }
            }
        }
        catch (org.apache.axis.AxisFault axisFaultException)
        {
            throw axisFaultException;
        }
    }

    public void throwAxisFault(final java.lang.String param) throws java.rmi.RemoteException
    {
        if (super.cachedEndpoint == null)
        {
            throw new org.apache.axis.NoEndPointException();
        }
        final org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://DefaultNamespace", "throwAxisFault"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try
        {
            final java.lang.Object _resp = _call.invoke(new java.lang.Object[]{param});

            if (_resp instanceof java.rmi.RemoteException)
            {
                throw (java.rmi.RemoteException) _resp;
            }
            extractAttachments(_call);
        }
        catch (org.apache.axis.AxisFault axisFaultException)
        {
            throw axisFaultException;
        }
    }

}
