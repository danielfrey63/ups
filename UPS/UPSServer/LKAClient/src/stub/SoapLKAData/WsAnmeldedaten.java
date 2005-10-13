/**
 * WsAnmeldedaten.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC3 Feb 28, 2005 (10:15:14 EST) WSDL2Java emitter.
 */

package SoapLKAData;

public class WsAnmeldedaten  implements java.io.Serializable {
    private java.lang.String lkNummer;
    private int lkForm;
    private java.lang.String lkFormText;
    private java.lang.String pruefungsmodeText;
    private java.lang.String fachrichtung;
    private java.lang.String studentennummer;
    private java.lang.String vorname;
    private boolean repetent;
    private java.lang.String lkEinheitTitel;
    private java.lang.String lkEinheitTyp;
    private java.lang.String pruefungsraum;
    private java.lang.String lkEinheitNummerzusatz;
    private java.lang.String nachname;
    private java.lang.String studiengang;
    private java.lang.String email;
    private java.lang.String pruefungszeit;
    private java.lang.String seskez;
    private java.lang.String pruefungsdatum;
    private java.lang.String lkEinheitTypText;

    public WsAnmeldedaten() {
    }

    public WsAnmeldedaten(
           java.lang.String email,
           java.lang.String fachrichtung,
           java.lang.String lkEinheitNummerzusatz,
           java.lang.String lkEinheitTitel,
           java.lang.String lkEinheitTyp,
           java.lang.String lkEinheitTypText,
           int lkForm,
           java.lang.String lkFormText,
           java.lang.String lkNummer,
           java.lang.String nachname,
           java.lang.String pruefungsdatum,
           java.lang.String pruefungsmodeText,
           java.lang.String pruefungsraum,
           java.lang.String pruefungszeit,
           boolean repetent,
           java.lang.String seskez,
           java.lang.String studentennummer,
           java.lang.String studiengang,
           java.lang.String vorname) {
           this.lkNummer = lkNummer;
           this.lkForm = lkForm;
           this.lkFormText = lkFormText;
           this.pruefungsmodeText = pruefungsmodeText;
           this.fachrichtung = fachrichtung;
           this.studentennummer = studentennummer;
           this.vorname = vorname;
           this.repetent = repetent;
           this.lkEinheitTitel = lkEinheitTitel;
           this.lkEinheitTyp = lkEinheitTyp;
           this.pruefungsraum = pruefungsraum;
           this.lkEinheitNummerzusatz = lkEinheitNummerzusatz;
           this.nachname = nachname;
           this.studiengang = studiengang;
           this.email = email;
           this.pruefungszeit = pruefungszeit;
           this.seskez = seskez;
           this.pruefungsdatum = pruefungsdatum;
           this.lkEinheitTypText = lkEinheitTypText;
    }


    /**
     * Gets the lkNummer value for this WsAnmeldedaten.
     * 
     * @return lkNummer
     */
    public java.lang.String getLkNummer() {
        return lkNummer;
    }


    /**
     * Sets the lkNummer value for this WsAnmeldedaten.
     * 
     * @param lkNummer
     */
    public void setLkNummer(java.lang.String lkNummer) {
        this.lkNummer = lkNummer;
    }


    /**
     * Gets the lkForm value for this WsAnmeldedaten.
     * 
     * @return lkForm
     */
    public int getLkForm() {
        return lkForm;
    }


    /**
     * Sets the lkForm value for this WsAnmeldedaten.
     * 
     * @param lkForm
     */
    public void setLkForm(int lkForm) {
        this.lkForm = lkForm;
    }


    /**
     * Gets the lkFormText value for this WsAnmeldedaten.
     * 
     * @return lkFormText
     */
    public java.lang.String getLkFormText() {
        return lkFormText;
    }


    /**
     * Sets the lkFormText value for this WsAnmeldedaten.
     * 
     * @param lkFormText
     */
    public void setLkFormText(java.lang.String lkFormText) {
        this.lkFormText = lkFormText;
    }


    /**
     * Gets the pruefungsmodeText value for this WsAnmeldedaten.
     * 
     * @return pruefungsmodeText
     */
    public java.lang.String getPruefungsmodeText() {
        return pruefungsmodeText;
    }


    /**
     * Sets the pruefungsmodeText value for this WsAnmeldedaten.
     * 
     * @param pruefungsmodeText
     */
    public void setPruefungsmodeText(java.lang.String pruefungsmodeText) {
        this.pruefungsmodeText = pruefungsmodeText;
    }


    /**
     * Gets the fachrichtung value for this WsAnmeldedaten.
     * 
     * @return fachrichtung
     */
    public java.lang.String getFachrichtung() {
        return fachrichtung;
    }


    /**
     * Sets the fachrichtung value for this WsAnmeldedaten.
     * 
     * @param fachrichtung
     */
    public void setFachrichtung(java.lang.String fachrichtung) {
        this.fachrichtung = fachrichtung;
    }


    /**
     * Gets the studentennummer value for this WsAnmeldedaten.
     * 
     * @return studentennummer
     */
    public java.lang.String getStudentennummer() {
        return studentennummer;
    }


    /**
     * Sets the studentennummer value for this WsAnmeldedaten.
     * 
     * @param studentennummer
     */
    public void setStudentennummer(java.lang.String studentennummer) {
        this.studentennummer = studentennummer;
    }


    /**
     * Gets the vorname value for this WsAnmeldedaten.
     * 
     * @return vorname
     */
    public java.lang.String getVorname() {
        return vorname;
    }


    /**
     * Sets the vorname value for this WsAnmeldedaten.
     * 
     * @param vorname
     */
    public void setVorname(java.lang.String vorname) {
        this.vorname = vorname;
    }


    /**
     * Gets the repetent value for this WsAnmeldedaten.
     * 
     * @return repetent
     */
    public boolean isRepetent() {
        return repetent;
    }


    /**
     * Sets the repetent value for this WsAnmeldedaten.
     * 
     * @param repetent
     */
    public void setRepetent(boolean repetent) {
        this.repetent = repetent;
    }


    /**
     * Gets the lkEinheitTitel value for this WsAnmeldedaten.
     * 
     * @return lkEinheitTitel
     */
    public java.lang.String getLkEinheitTitel() {
        return lkEinheitTitel;
    }


    /**
     * Sets the lkEinheitTitel value for this WsAnmeldedaten.
     * 
     * @param lkEinheitTitel
     */
    public void setLkEinheitTitel(java.lang.String lkEinheitTitel) {
        this.lkEinheitTitel = lkEinheitTitel;
    }


    /**
     * Gets the lkEinheitTyp value for this WsAnmeldedaten.
     * 
     * @return lkEinheitTyp
     */
    public java.lang.String getLkEinheitTyp() {
        return lkEinheitTyp;
    }


    /**
     * Sets the lkEinheitTyp value for this WsAnmeldedaten.
     * 
     * @param lkEinheitTyp
     */
    public void setLkEinheitTyp(java.lang.String lkEinheitTyp) {
        this.lkEinheitTyp = lkEinheitTyp;
    }


    /**
     * Gets the pruefungsraum value for this WsAnmeldedaten.
     * 
     * @return pruefungsraum
     */
    public java.lang.String getPruefungsraum() {
        return pruefungsraum;
    }


    /**
     * Sets the pruefungsraum value for this WsAnmeldedaten.
     * 
     * @param pruefungsraum
     */
    public void setPruefungsraum(java.lang.String pruefungsraum) {
        this.pruefungsraum = pruefungsraum;
    }


    /**
     * Gets the lkEinheitNummerzusatz value for this WsAnmeldedaten.
     * 
     * @return lkEinheitNummerzusatz
     */
    public java.lang.String getLkEinheitNummerzusatz() {
        return lkEinheitNummerzusatz;
    }


    /**
     * Sets the lkEinheitNummerzusatz value for this WsAnmeldedaten.
     * 
     * @param lkEinheitNummerzusatz
     */
    public void setLkEinheitNummerzusatz(java.lang.String lkEinheitNummerzusatz) {
        this.lkEinheitNummerzusatz = lkEinheitNummerzusatz;
    }


    /**
     * Gets the nachname value for this WsAnmeldedaten.
     * 
     * @return nachname
     */
    public java.lang.String getNachname() {
        return nachname;
    }


    /**
     * Sets the nachname value for this WsAnmeldedaten.
     * 
     * @param nachname
     */
    public void setNachname(java.lang.String nachname) {
        this.nachname = nachname;
    }


    /**
     * Gets the studiengang value for this WsAnmeldedaten.
     * 
     * @return studiengang
     */
    public java.lang.String getStudiengang() {
        return studiengang;
    }


    /**
     * Sets the studiengang value for this WsAnmeldedaten.
     * 
     * @param studiengang
     */
    public void setStudiengang(java.lang.String studiengang) {
        this.studiengang = studiengang;
    }


    /**
     * Gets the email value for this WsAnmeldedaten.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this WsAnmeldedaten.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the pruefungszeit value for this WsAnmeldedaten.
     * 
     * @return pruefungszeit
     */
    public java.lang.String getPruefungszeit() {
        return pruefungszeit;
    }


    /**
     * Sets the pruefungszeit value for this WsAnmeldedaten.
     * 
     * @param pruefungszeit
     */
    public void setPruefungszeit(java.lang.String pruefungszeit) {
        this.pruefungszeit = pruefungszeit;
    }


    /**
     * Gets the seskez value for this WsAnmeldedaten.
     * 
     * @return seskez
     */
    public java.lang.String getSeskez() {
        return seskez;
    }


    /**
     * Sets the seskez value for this WsAnmeldedaten.
     * 
     * @param seskez
     */
    public void setSeskez(java.lang.String seskez) {
        this.seskez = seskez;
    }


    /**
     * Gets the pruefungsdatum value for this WsAnmeldedaten.
     * 
     * @return pruefungsdatum
     */
    public java.lang.String getPruefungsdatum() {
        return pruefungsdatum;
    }


    /**
     * Sets the pruefungsdatum value for this WsAnmeldedaten.
     * 
     * @param pruefungsdatum
     */
    public void setPruefungsdatum(java.lang.String pruefungsdatum) {
        this.pruefungsdatum = pruefungsdatum;
    }


    /**
     * Gets the lkEinheitTypText value for this WsAnmeldedaten.
     * 
     * @return lkEinheitTypText
     */
    public java.lang.String getLkEinheitTypText() {
        return lkEinheitTypText;
    }


    /**
     * Sets the lkEinheitTypText value for this WsAnmeldedaten.
     * 
     * @param lkEinheitTypText
     */
    public void setLkEinheitTypText(java.lang.String lkEinheitTypText) {
        this.lkEinheitTypText = lkEinheitTypText;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsAnmeldedaten)) return false;
        WsAnmeldedaten other = (WsAnmeldedaten) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lkNummer==null && other.getLkNummer()==null) || 
             (this.lkNummer!=null &&
              this.lkNummer.equals(other.getLkNummer()))) &&
            this.lkForm == other.getLkForm() &&
            ((this.lkFormText==null && other.getLkFormText()==null) || 
             (this.lkFormText!=null &&
              this.lkFormText.equals(other.getLkFormText()))) &&
            ((this.pruefungsmodeText==null && other.getPruefungsmodeText()==null) || 
             (this.pruefungsmodeText!=null &&
              this.pruefungsmodeText.equals(other.getPruefungsmodeText()))) &&
            ((this.fachrichtung==null && other.getFachrichtung()==null) || 
             (this.fachrichtung!=null &&
              this.fachrichtung.equals(other.getFachrichtung()))) &&
            ((this.studentennummer==null && other.getStudentennummer()==null) || 
             (this.studentennummer!=null &&
              this.studentennummer.equals(other.getStudentennummer()))) &&
            ((this.vorname==null && other.getVorname()==null) || 
             (this.vorname!=null &&
              this.vorname.equals(other.getVorname()))) &&
            this.repetent == other.isRepetent() &&
            ((this.lkEinheitTitel==null && other.getLkEinheitTitel()==null) || 
             (this.lkEinheitTitel!=null &&
              this.lkEinheitTitel.equals(other.getLkEinheitTitel()))) &&
            ((this.lkEinheitTyp==null && other.getLkEinheitTyp()==null) || 
             (this.lkEinheitTyp!=null &&
              this.lkEinheitTyp.equals(other.getLkEinheitTyp()))) &&
            ((this.pruefungsraum==null && other.getPruefungsraum()==null) || 
             (this.pruefungsraum!=null &&
              this.pruefungsraum.equals(other.getPruefungsraum()))) &&
            ((this.lkEinheitNummerzusatz==null && other.getLkEinheitNummerzusatz()==null) || 
             (this.lkEinheitNummerzusatz!=null &&
              this.lkEinheitNummerzusatz.equals(other.getLkEinheitNummerzusatz()))) &&
            ((this.nachname==null && other.getNachname()==null) || 
             (this.nachname!=null &&
              this.nachname.equals(other.getNachname()))) &&
            ((this.studiengang==null && other.getStudiengang()==null) || 
             (this.studiengang!=null &&
              this.studiengang.equals(other.getStudiengang()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.pruefungszeit==null && other.getPruefungszeit()==null) || 
             (this.pruefungszeit!=null &&
              this.pruefungszeit.equals(other.getPruefungszeit()))) &&
            ((this.seskez==null && other.getSeskez()==null) || 
             (this.seskez!=null &&
              this.seskez.equals(other.getSeskez()))) &&
            ((this.pruefungsdatum==null && other.getPruefungsdatum()==null) || 
             (this.pruefungsdatum!=null &&
              this.pruefungsdatum.equals(other.getPruefungsdatum()))) &&
            ((this.lkEinheitTypText==null && other.getLkEinheitTypText()==null) || 
             (this.lkEinheitTypText!=null &&
              this.lkEinheitTypText.equals(other.getLkEinheitTypText())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLkNummer() != null) {
            _hashCode += getLkNummer().hashCode();
        }
        _hashCode += getLkForm();
        if (getLkFormText() != null) {
            _hashCode += getLkFormText().hashCode();
        }
        if (getPruefungsmodeText() != null) {
            _hashCode += getPruefungsmodeText().hashCode();
        }
        if (getFachrichtung() != null) {
            _hashCode += getFachrichtung().hashCode();
        }
        if (getStudentennummer() != null) {
            _hashCode += getStudentennummer().hashCode();
        }
        if (getVorname() != null) {
            _hashCode += getVorname().hashCode();
        }
        _hashCode += (isRepetent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLkEinheitTitel() != null) {
            _hashCode += getLkEinheitTitel().hashCode();
        }
        if (getLkEinheitTyp() != null) {
            _hashCode += getLkEinheitTyp().hashCode();
        }
        if (getPruefungsraum() != null) {
            _hashCode += getPruefungsraum().hashCode();
        }
        if (getLkEinheitNummerzusatz() != null) {
            _hashCode += getLkEinheitNummerzusatz().hashCode();
        }
        if (getNachname() != null) {
            _hashCode += getNachname().hashCode();
        }
        if (getStudiengang() != null) {
            _hashCode += getStudiengang().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getPruefungszeit() != null) {
            _hashCode += getPruefungszeit().hashCode();
        }
        if (getSeskez() != null) {
            _hashCode += getSeskez().hashCode();
        }
        if (getPruefungsdatum() != null) {
            _hashCode += getPruefungsdatum().hashCode();
        }
        if (getLkEinheitTypText() != null) {
            _hashCode += getLkEinheitTypText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsAnmeldedaten.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:SoapLKAData", "WsAnmeldedaten"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkNummer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkNummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkForm");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkForm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkFormText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkFormText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pruefungsmodeText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pruefungsmodeText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fachrichtung");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fachrichtung"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("studentennummer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "studentennummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vorname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vorname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repetent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "repetent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkEinheitTitel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkEinheitTitel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkEinheitTyp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkEinheitTyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pruefungsraum");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pruefungsraum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkEinheitNummerzusatz");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkEinheitNummerzusatz"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nachname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nachname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("studiengang");
        elemField.setXmlName(new javax.xml.namespace.QName("", "studiengang"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pruefungszeit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pruefungszeit"));
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
        elemField.setFieldName("pruefungsdatum");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pruefungsdatum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lkEinheitTypText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lkEinheitTypText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
