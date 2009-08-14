/**
 * V10SoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAService;

public class V10SoapBindingStub extends org.apache.axis.client.Stub implements SoapLKAService.WsSoapLKA
{
    private java.util.Vector cachedSerClasses = new java.util.Vector();

    private java.util.Vector cachedSerQNames = new java.util.Vector();

    private java.util.Vector cachedSerFactories = new java.util.Vector();

    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc[] _operations;

    static
    {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1()
    {
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getInterfaceVersion");
        oper.addParameter(new javax.xml.namespace.QName("", "info"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getInterfaceVersionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("retrieveAktuellePruefungssession");
        oper.addParameter(new javax.xml.namespace.QName("", "info"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:SoapLKAData", "WsPruefungssession"));
        oper.setReturnClass(SoapLKAData.WsPruefungssession.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "retrieveAktuellePruefungssessionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("retrieveAnmeldedaten");
        oper.addParameter(new javax.xml.namespace.QName("", "info"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "lkeinheitNummer"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:SoapLKAService", "ArrayOf_tns1_WsAnmeldedaten"));
        oper.setReturnClass(SoapLKAData.WsAnmeldedaten[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "retrieveAnmeldedatenReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

    }

    public V10SoapBindingStub() throws org.apache.axis.AxisFault
    {
        this(null);
    }

    public V10SoapBindingStub(final java.net.URL endpointURL, final javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
    {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public V10SoapBindingStub(final javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
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
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        final java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        final java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        final java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        final java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        final java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        final java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        final java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        final java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        final java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        final java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("urn:SoapLKAData", "WsAnmeldedaten");
        cachedSerQNames.add(qName);
        cls = SoapLKAData.WsAnmeldedaten.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:SoapLKAData", "WsPruefungssession");
        cachedSerQNames.add(qName);
        cls = SoapLKAData.WsPruefungssession.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("urn:SoapLKAService", "ArrayOf_tns1_WsAnmeldedaten");
        cachedSerQNames.add(qName);
        cls = SoapLKAData.WsAnmeldedaten[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("urn:SoapLKAData", "WsAnmeldedaten");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

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
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this)
            {
                if (firstCall())
                {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i)
                    {
                        final java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        final javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        final java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class)
                        {
                            final java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get(i);
                            final java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory)
                        {
                            final org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            final org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t)
        {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public java.lang.String getInterfaceVersion(final java.lang.String info) throws java.rmi.RemoteException
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
        _call.setOperationName(new javax.xml.namespace.QName("urn:SoapLKAService", "getInterfaceVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try
        {
            final java.lang.Object _resp = _call.invoke(new java.lang.Object[]{info});

            if (_resp instanceof java.rmi.RemoteException)
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments(_call);
                try
                {
                    return (java.lang.String) _resp;
                }
                catch (java.lang.Exception _exception)
                {
                    return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
                }
            }
        }
        catch (org.apache.axis.AxisFault axisFaultException)
        {
            throw axisFaultException;
        }
    }

    public SoapLKAData.WsPruefungssession retrieveAktuellePruefungssession(final java.lang.String info) throws java.rmi.RemoteException
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
        _call.setOperationName(new javax.xml.namespace.QName("urn:SoapLKAService", "retrieveAktuellePruefungssession"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try
        {
            final java.lang.Object _resp = _call.invoke(new java.lang.Object[]{info});

            if (_resp instanceof java.rmi.RemoteException)
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments(_call);
                try
                {
                    return (SoapLKAData.WsPruefungssession) _resp;
                }
                catch (java.lang.Exception _exception)
                {
                    return (SoapLKAData.WsPruefungssession) org.apache.axis.utils.JavaUtils.convert(_resp, SoapLKAData.WsPruefungssession.class);
                }
            }
        }
        catch (org.apache.axis.AxisFault axisFaultException)
        {
            throw axisFaultException;
        }
    }

    public SoapLKAData.WsAnmeldedaten[] retrieveAnmeldedaten(final java.lang.String info, final java.lang.String lkeinheitNummer) throws java.rmi.RemoteException
    {
        if (super.cachedEndpoint == null)
        {
            throw new org.apache.axis.NoEndPointException();
        }
        final org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:SoapLKAService", "retrieveAnmeldedaten"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try
        {
            final java.lang.Object _resp = _call.invoke(new java.lang.Object[]{info, lkeinheitNummer});

            if (_resp instanceof java.rmi.RemoteException)
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments(_call);
                try
                {
                    return (SoapLKAData.WsAnmeldedaten[]) _resp;
                }
                catch (java.lang.Exception _exception)
                {
                    return (SoapLKAData.WsAnmeldedaten[]) org.apache.axis.utils.JavaUtils.convert(_resp, SoapLKAData.WsAnmeldedaten[].class);
                }
            }
        }
        catch (org.apache.axis.AxisFault axisFaultException)
        {
            throw axisFaultException;
        }
    }

}
