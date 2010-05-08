/**
 * LkaSOAP11BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ch.ethz.id.bi.soaplka.web.soap.service;

public class LkaSOAP11BindingStub extends org.apache.axis.client.Stub implements ch.ethz.id.bi.soaplka.web.soap.service.LkaPortType
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
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName( "retrieveAktuellePruefungssession" );
        param = new org.apache.axis.description.ParameterDesc( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "info" ), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ), java.lang.String.class, false, false );
        oper.addParameter( param );
        oper.setReturnType( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "WsPruefungssession" ) );
        oper.setReturnClass( ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession.class );
        oper.setReturnQName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "return" ) );
        oper.setStyle( org.apache.axis.constants.Style.WRAPPED );
        oper.setUse( org.apache.axis.constants.Use.LITERAL );
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName( "retrieveAnmeldedaten" );
        param = new org.apache.axis.description.ParameterDesc( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "info" ), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ), java.lang.String.class, false, false );
        oper.addParameter( param );
        param = new org.apache.axis.description.ParameterDesc( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "lkeinheitNummer" ), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ), java.lang.String.class, false, false );
        oper.addParameter( param );
        oper.setReturnType( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "WsAnmeldedaten" ) );
        oper.setReturnClass( ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[].class );
        oper.setReturnQName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "return" ) );
        oper.setStyle( org.apache.axis.constants.Style.WRAPPED );
        oper.setUse( org.apache.axis.constants.Use.LITERAL );
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName( "getInterfaceVersion" );
        param = new org.apache.axis.description.ParameterDesc( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "info" ), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ), java.lang.String.class, false, false );
        oper.addParameter( param );
        oper.setReturnType( new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        oper.setReturnClass( java.lang.String.class );
        oper.setReturnQName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "return" ) );
        oper.setStyle( org.apache.axis.constants.Style.WRAPPED );
        oper.setUse( org.apache.axis.constants.Use.LITERAL );
        _operations[2] = oper;

    }

    public LkaSOAP11BindingStub() throws org.apache.axis.AxisFault
    {
        this( null );
    }

    public LkaSOAP11BindingStub( java.net.URL endpointURL, javax.xml.rpc.Service service ) throws org.apache.axis.AxisFault
    {
        this( service );
        super.cachedEndpoint = endpointURL;
    }

    public LkaSOAP11BindingStub( javax.xml.rpc.Service service ) throws org.apache.axis.AxisFault
    {
        if ( service == null )
        {
            super.service = new org.apache.axis.client.Service();
        }
        else
        {
            super.service = service;
        }
        ( (org.apache.axis.client.Service) super.service ).setTypeMappingVersion( "1.2" );
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "WsAnmeldedaten" );
        cachedSerQNames.add( qName );
        cls = ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten.class;
        cachedSerClasses.add( cls );
        cachedSerFactories.add( beansf );
        cachedDeserFactories.add( beandf );

        qName = new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "WsPruefungssession" );
        cachedSerQNames.add( qName );
        cls = ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession.class;
        cachedSerClasses.add( cls );
        cachedSerFactories.add( beansf );
        cachedDeserFactories.add( beandf );

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException
    {
        try
        {
            org.apache.axis.client.Call _call = super._createCall();
            if ( super.maintainSessionSet )
            {
                _call.setMaintainSession( super.maintainSession );
            }
            if ( super.cachedUsername != null )
            {
                _call.setUsername( super.cachedUsername );
            }
            if ( super.cachedPassword != null )
            {
                _call.setPassword( super.cachedPassword );
            }
            if ( super.cachedEndpoint != null )
            {
                _call.setTargetEndpointAddress( super.cachedEndpoint );
            }
            if ( super.cachedTimeout != null )
            {
                _call.setTimeout( super.cachedTimeout );
            }
            if ( super.cachedPortName != null )
            {
                _call.setPortName( super.cachedPortName );
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while ( keys.hasMoreElements() )
            {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty( key, super.cachedProperties.get( key ) );
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized ( this )
            {
                if ( firstCall() )
                {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle( null );
                    for ( int i = 0; i < cachedSerFactories.size(); ++i )
                    {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get( i );
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get( i );
                        java.lang.Object x = cachedSerFactories.get( i );
                        if ( x instanceof Class )
                        {
                            java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get( i );
                            java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get( i );
                            _call.registerTypeMapping( cls, qName, sf, df, false );
                        }
                        else if ( x instanceof javax.xml.rpc.encoding.SerializerFactory )
                        {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get( i );
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get( i );
                            _call.registerTypeMapping( cls, qName, sf, df, false );
                        }
                    }
                }
            }
            return _call;
        }
        catch ( java.lang.Throwable _t )
        {
            throw new org.apache.axis.AxisFault( "Failure trying to get the Call object", _t );
        }
    }

    public ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession retrieveAktuellePruefungssession( java.lang.String info ) throws java.rmi.RemoteException
    {
        if ( super.cachedEndpoint == null )
        {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation( _operations[0] );
        _call.setUseSOAPAction( true );
        _call.setSOAPActionURI( "urn:retrieveAktuellePruefungssession" );
        _call.setEncodingStyle( null );
        _call.setProperty( org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE );
        _call.setProperty( org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE );
        _call.setSOAPVersion( org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS );
        _call.setOperationName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "retrieveAktuellePruefungssession" ) );

        setRequestHeaders( _call );
        setAttachments( _call );
        try
        {
            java.lang.Object _resp = _call.invoke( new java.lang.Object[]{info} );

            if ( _resp instanceof java.rmi.RemoteException )
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments( _call );
                try
                {
                    return (ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession) _resp;
                }
                catch ( java.lang.Exception _exception )
                {
                    return (ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession) org.apache.axis.utils.JavaUtils.convert( _resp, ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession.class );
                }
            }
        }
        catch ( org.apache.axis.AxisFault axisFaultException )
        {
            throw axisFaultException;
        }
    }

    public ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[] retrieveAnmeldedaten( java.lang.String info, java.lang.String lkeinheitNummer ) throws java.rmi.RemoteException
    {
        if ( super.cachedEndpoint == null )
        {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation( _operations[1] );
        _call.setUseSOAPAction( true );
        _call.setSOAPActionURI( "urn:retrieveAnmeldedaten" );
        _call.setEncodingStyle( null );
        _call.setProperty( org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE );
        _call.setProperty( org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE );
        _call.setSOAPVersion( org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS );
        _call.setOperationName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "retrieveAnmeldedaten" ) );

        setRequestHeaders( _call );
        setAttachments( _call );
        try
        {
            java.lang.Object _resp = _call.invoke( new java.lang.Object[]{info, lkeinheitNummer} );

            if ( _resp instanceof java.rmi.RemoteException )
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments( _call );
                try
                {
                    return (ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[]) _resp;
                }
                catch ( java.lang.Exception _exception )
                {
                    return (ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[]) org.apache.axis.utils.JavaUtils.convert( _resp, ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsAnmeldedaten[].class );
                }
            }
        }
        catch ( org.apache.axis.AxisFault axisFaultException )
        {
            throw axisFaultException;
        }
    }

    public java.lang.String getInterfaceVersion( java.lang.String info ) throws java.rmi.RemoteException
    {
        if ( super.cachedEndpoint == null )
        {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation( _operations[2] );
        _call.setUseSOAPAction( true );
        _call.setSOAPActionURI( "urn:getInterfaceVersion" );
        _call.setEncodingStyle( null );
        _call.setProperty( org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE );
        _call.setProperty( org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE );
        _call.setSOAPVersion( org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS );
        _call.setOperationName( new javax.xml.namespace.QName( "http://service.soap.web.soaplka.bi.id.ethz.ch", "getInterfaceVersion" ) );

        setRequestHeaders( _call );
        setAttachments( _call );
        try
        {
            java.lang.Object _resp = _call.invoke( new java.lang.Object[]{info} );

            if ( _resp instanceof java.rmi.RemoteException )
            {
                throw (java.rmi.RemoteException) _resp;
            }
            else
            {
                extractAttachments( _call );
                try
                {
                    return (java.lang.String) _resp;
                }
                catch ( java.lang.Exception _exception )
                {
                    return (java.lang.String) org.apache.axis.utils.JavaUtils.convert( _resp, java.lang.String.class );
                }
            }
        }
        catch ( org.apache.axis.AxisFault axisFaultException )
        {
            throw axisFaultException;
        }
    }

}
