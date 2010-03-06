/**
 * WsPruefungssession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAData;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsPruefungssession implements Serializable
{
    private String sessionsname;

    private String sessionsende;

    private String seskez;

    private String planungFreigabe;

    public WsPruefungssession()
    {
    }

    public WsPruefungssession(
            final String planungFreigabe,
            final String seskez,
            final String sessionsende,
            final String sessionsname )
    {
        this.sessionsname = sessionsname;
        this.sessionsende = sessionsende;
        this.seskez = seskez;
        this.planungFreigabe = planungFreigabe;
    }

    /**
     * Gets the sessionsname value for this WsPruefungssession.
     *
     * @return sessionsname
     */
    public String getSessionsname()
    {
        return sessionsname;
    }

    /**
     * Sets the sessionsname value for this WsPruefungssession.
     *
     * @param sessionsname
     */
    public void setSessionsname( final String sessionsname )
    {
        this.sessionsname = sessionsname;
    }

    /**
     * Gets the sessionsende value for this WsPruefungssession.
     *
     * @return sessionsende
     */
    public String getSessionsende()
    {
        return sessionsende;
    }

    /**
     * Sets the sessionsende value for this WsPruefungssession.
     *
     * @param sessionsende
     */
    public void setSessionsende( final String sessionsende )
    {
        this.sessionsende = sessionsende;
    }

    /**
     * Gets the seskez value for this WsPruefungssession.
     *
     * @return seskez
     */
    public String getSeskez()
    {
        return seskez;
    }

    /**
     * Sets the seskez value for this WsPruefungssession.
     *
     * @param seskez
     */
    public void setSeskez( final String seskez )
    {
        this.seskez = seskez;
    }

    /**
     * Gets the planungFreigabe value for this WsPruefungssession.
     *
     * @return planungFreigabe
     */
    public String getPlanungFreigabe()
    {
        return planungFreigabe;
    }

    /**
     * Sets the planungFreigabe value for this WsPruefungssession.
     *
     * @param planungFreigabe
     */
    public void setPlanungFreigabe( final String planungFreigabe )
    {
        this.planungFreigabe = planungFreigabe;
    }

    private Object __equalsCalc = null;

    public synchronized boolean equals( final Object obj )
    {
        if ( !( obj instanceof WsPruefungssession ) )
        {
            return false;
        }
        final WsPruefungssession other = (WsPruefungssession) obj;
        if ( obj == null )
        {
            return false;
        }
        if ( this == obj )
        {
            return true;
        }
        if ( __equalsCalc != null )
        {
            return ( __equalsCalc == obj );
        }
        __equalsCalc = obj;
        final boolean _equals;
        _equals = true &&
                ( ( this.sessionsname == null && other.getSessionsname() == null ) ||
                        ( this.sessionsname != null &&
                                this.sessionsname.equals( other.getSessionsname() ) ) ) &&
                ( ( this.sessionsende == null && other.getSessionsende() == null ) ||
                        ( this.sessionsende != null &&
                                this.sessionsende.equals( other.getSessionsende() ) ) ) &&
                ( ( this.seskez == null && other.getSeskez() == null ) ||
                        ( this.seskez != null &&
                                this.seskez.equals( other.getSeskez() ) ) ) &&
                ( ( this.planungFreigabe == null && other.getPlanungFreigabe() == null ) ||
                        ( this.planungFreigabe != null &&
                                this.planungFreigabe.equals( other.getPlanungFreigabe() ) ) );
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
        if ( getSessionsname() != null )
        {
            _hashCode += getSessionsname().hashCode();
        }
        if ( getSessionsende() != null )
        {
            _hashCode += getSessionsende().hashCode();
        }
        if ( getSeskez() != null )
        {
            _hashCode += getSeskez().hashCode();
        }
        if ( getPlanungFreigabe() != null )
        {
            _hashCode += getPlanungFreigabe().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static final TypeDesc typeDesc =
            new TypeDesc( WsPruefungssession.class, true );

    static
    {
        typeDesc.setXmlType( new QName( "urn:SoapLKAData", "WsPruefungssession" ) );
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName( "sessionsname" );
        elemField.setXmlName( new QName( "", "sessionsname" ) );
        elemField.setXmlType( new QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new ElementDesc();
        elemField.setFieldName( "sessionsende" );
        elemField.setXmlName( new QName( "", "sessionsende" ) );
        elemField.setXmlType( new QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new ElementDesc();
        elemField.setFieldName( "seskez" );
        elemField.setXmlName( new QName( "", "seskez" ) );
        elemField.setXmlType( new QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
        elemField = new ElementDesc();
        elemField.setFieldName( "planungFreigabe" );
        elemField.setXmlName( new QName( "", "planungFreigabe" ) );
        elemField.setXmlType( new QName( "http://www.w3.org/2001/XMLSchema", "string" ) );
        elemField.setNillable( true );
        typeDesc.addFieldDesc( elemField );
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc()
    {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static Serializer getSerializer(
            final String mechType,
            final Class _javaType,
            final QName _xmlType )
    {
        return
                new BeanSerializer(
                        _javaType, _xmlType, typeDesc );
    }

    /**
     * Get Custom Deserializer
     */
    public static Deserializer getDeserializer(
            final String mechType,
            final Class _javaType,
            final QName _xmlType )
    {
        return
                new BeanDeserializer(
                        _javaType, _xmlType, typeDesc );
    }

}
