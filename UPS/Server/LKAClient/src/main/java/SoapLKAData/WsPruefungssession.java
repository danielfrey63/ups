/**
 * WsPruefungssession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAData;

public class WsPruefungssession implements java.io.Serializable
{
    private java.lang.String sessionsname;

    private java.lang.String sessionsende;

    private java.lang.String seskez;

    private java.lang.String planungFreigabe;

    public WsPruefungssession()
    {
    }

    public WsPruefungssession(
            final java.lang.String planungFreigabe,
            final java.lang.String seskez,
            final java.lang.String sessionsende,
            final java.lang.String sessionsname)
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
    public java.lang.String getSessionsname()
    {
        return sessionsname;
    }

    /**
     * Sets the sessionsname value for this WsPruefungssession.
     *
     * @param sessionsname
     */
    public void setSessionsname(final java.lang.String sessionsname)
    {
        this.sessionsname = sessionsname;
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
    public void setSessionsende(final java.lang.String sessionsende)
    {
        this.sessionsende = sessionsende;
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
    public void setSeskez(final java.lang.String seskez)
    {
        this.seskez = seskez;
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
    public void setPlanungFreigabe(final java.lang.String planungFreigabe)
    {
        this.planungFreigabe = planungFreigabe;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(final java.lang.Object obj)
    {
        if (!(obj instanceof WsPruefungssession))
        {
            return false;
        }
        final WsPruefungssession other = (WsPruefungssession) obj;
        if (obj == null)
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        if (__equalsCalc != null)
        {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        final boolean _equals;
        _equals = true &&
                ((this.sessionsname == null && other.getSessionsname() == null) ||
                        (this.sessionsname != null &&
                                this.sessionsname.equals(other.getSessionsname()))) &&
                ((this.sessionsende == null && other.getSessionsende() == null) ||
                        (this.sessionsende != null &&
                                this.sessionsende.equals(other.getSessionsende()))) &&
                ((this.seskez == null && other.getSeskez() == null) ||
                        (this.seskez != null &&
                                this.seskez.equals(other.getSeskez()))) &&
                ((this.planungFreigabe == null && other.getPlanungFreigabe() == null) ||
                        (this.planungFreigabe != null &&
                                this.planungFreigabe.equals(other.getPlanungFreigabe())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode()
    {
        if (__hashCodeCalc)
        {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSessionsname() != null)
        {
            _hashCode += getSessionsname().hashCode();
        }
        if (getSessionsende() != null)
        {
            _hashCode += getSessionsende().hashCode();
        }
        if (getSeskez() != null)
        {
            _hashCode += getSeskez().hashCode();
        }
        if (getPlanungFreigabe() != null)
        {
            _hashCode += getPlanungFreigabe().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(WsPruefungssession.class, true);

    static
    {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:SoapLKAData", "WsPruefungssession"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionsname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sessionsname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionsende");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sessionsende"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seskez");
        elemField.setXmlName(new javax.xml.namespace.QName("", "seskez"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planungFreigabe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "planungFreigabe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /** Return type metadata object */
    public static org.apache.axis.description.TypeDesc getTypeDesc()
    {
        return typeDesc;
    }

    /** Get Custom Serializer */
    public static org.apache.axis.encoding.Serializer getSerializer(
            final java.lang.String mechType,
            final java.lang.Class _javaType,
            final javax.xml.namespace.QName _xmlType)
    {
        return
                new org.apache.axis.encoding.ser.BeanSerializer(
                        _javaType, _xmlType, typeDesc);
    }

    /** Get Custom Deserializer */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            final java.lang.String mechType,
            final java.lang.Class _javaType,
            final javax.xml.namespace.QName _xmlType)
    {
        return
                new org.apache.axis.encoding.ser.BeanDeserializer(
                        _javaType, _xmlType, typeDesc);
    }

}
