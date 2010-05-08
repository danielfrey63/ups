/**
 * WsPruefungssession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package ch.ethz.id.bi.soaplka.web.soap.data.xsd;

public class WsPruefungssession implements java.io.Serializable
{
    private java.lang.String planungFreigabe;

    private java.lang.String seskez;

    private java.lang.String sessionsende;

    private java.lang.String sessionsname;

    public WsPruefungssession()
    {
    }

    public WsPruefungssession(
            java.lang.String planungFreigabe,
            java.lang.String seskez,
            java.lang.String sessionsende,
            java.lang.String sessionsname )
    {
        this.planungFreigabe = planungFreigabe;
        this.seskez = seskez;
        this.sessionsende = sessionsende;
        this.sessionsname = sessionsname;
    }

    /**
     * Gets the planungFreigabe value for this WsPruefungssession.
     *
     * @return planungFreigabe
     */
    public java.lang.String getPlanungFreigabe()
    {
        return planungFreigabe;
    }

    /**
     * Sets the planungFreigabe value for this WsPruefungssession.
     *
     * @param planungFreigabe
     */
    public void setPlanungFreigabe( java.lang.String planungFreigabe )
    {
        this.planungFreigabe = planungFreigabe;
    }

    /**
     * Gets the seskez value for this WsPruefungssession.
     *
     * @return seskez
     */
    public java.lang.String getSeskez()
    {
        return seskez;
    }

    /**
     * Sets the seskez value for this WsPruefungssession.
     *
     * @param seskez
     */
    public void setSeskez( java.lang.String seskez )
    {
        this.seskez = seskez;
    }

    /**
     * Gets the sessionsende value for this WsPruefungssession.
     *
     * @return sessionsende
     */
    public java.lang.String getSessionsende()
    {
        return sessionsende;
    }

    /**
     * Sets the sessionsende value for this WsPruefungssession.
     *
     * @param sessionsende
     */
    public void setSessionsende( java.lang.String sessionsende )
    {
        this.sessionsende = sessionsende;
    }

    /**
     * Gets the sessionsname value for this WsPruefungssession.
     *
     * @return sessionsname
     */
    public java.lang.String getSessionsname()
    {
        return sessionsname;
    }

    /**
     * Sets the sessionsname value for this WsPruefungssession.
     *
     * @param sessionsname
     */
    public void setSessionsname( java.lang.String sessionsname )
    {
        this.sessionsname = sessionsname;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals( java.lang.Object obj )
    {
        if ( !( obj instanceof WsPruefungssession ) )
            return false;
        WsPruefungssession other = (WsPruefungssession) obj;
        if ( obj == null )
            return false;
        if ( this == obj )
            return true;
        if ( __equalsCalc != null )
        {
            return ( __equalsCalc == obj );
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
                ( ( this.planungFreigabe == null && other.getPlanungFreigabe() == null ) ||
                        ( this.planungFreigabe != null &&
                                this.planungFreigabe.equals( other.getPlanungFreigabe() ) ) ) &&
                ( ( this.seskez == null && other.getSeskez() == null ) ||
                        ( this.seskez != null &&
                                this.seskez.equals( other.getSeskez() ) ) ) &&
                ( ( this.sessionsende == null && other.getSessionsende() == null ) ||
                        ( this.sessionsende != null &&
                                this.sessionsende.equals( other.getSessionsende() ) ) ) &&
                ( ( this.sessionsname == null && other.getSessionsname() == null ) ||
                        ( this.sessionsname != null &&
                                this.sessionsname.equals( other.getSessionsname() ) ) );
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode()
    {
        if ( __hashCodeCalc )
        {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if ( getPlanungFreigabe() != null )
        {
            _hashCode += getPlanungFreigabe().hashCode();
        }
        if ( getSeskez() != null )
        {
            _hashCode += getSeskez().hashCode();
        }
        if ( getSessionsende() != null )
        {
            _hashCode += getSessionsende().hashCode();
        }
        if ( getSessionsname() != null )
        {
            _hashCode += getSessionsname().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata

    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc( WsPruefungssession.class, true );

    static
    {
        typeDesc.setXmlType( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "WsPruefungssession" ) );
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName( "planungFreigabe" );
        elemField.setXmlName( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "planungFreigabe" ) );
        elemField.setXmlType( new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setMinOccurs( 0 );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName( "seskez" );
        elemField.setXmlName( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "seskez" ) );
        elemField.setXmlType( new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setMinOccurs( 0 );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName( "sessionsende" );
        elemField.setXmlName( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "sessionsende" ) );
        elemField.setXmlType( new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setMinOccurs( 0 );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName( "sessionsname" );
        elemField.setXmlName( new javax.xml.namespace.QName( "http://data.soap.web.soaplka.bi.id.ethz.ch/xsd", "sessionsname" ) );
        elemField.setXmlType( new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setMinOccurs( 0 );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
    }

    /** Return type metadata object */
    public static org.apache.axis.description.TypeDesc getTypeDesc()
    {
        return typeDesc;
    }

    /** Get Custom Serializer */
    public static org.apache.axis.encoding.Serializer getSerializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType )
    {
        return
                new org.apache.axis.encoding.ser.BeanSerializer(
                        _javaType, _xmlType, typeDesc );
    }

    /** Get Custom Deserializer */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType )
    {
        return
                new org.apache.axis.encoding.ser.BeanDeserializer(
                        _javaType, _xmlType, typeDesc );
    }

}
