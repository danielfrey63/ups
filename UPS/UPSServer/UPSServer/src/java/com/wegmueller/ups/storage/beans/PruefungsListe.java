package com.wegmueller.ups.storage.beans;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  23:12:10
 */
public class PruefungsListe {

   private ByteTypeContent pdf;
   private ByteTypeContent properties;
   private ByteTypeContent pruefungsListe;
   private Long id;
   private String pruefung;
   private String pruefungsSession;
   private String studentenNummer;
   private String userName;
    private Properties p;

    public ByteTypeContent getPdf() {
        return pdf;
    }

    public void setPdf(ByteTypeContent pdf) {
        this.pdf = pdf;
    }

    public ByteTypeContent getProperties() {
        return properties;
    }

    public void setProperties(ByteTypeContent properties) {
        this.properties = properties;
    }


    public Properties getUserProperties() {
        if (p==null) {
            p = new Properties();
            try {
                p.load(new ByteArrayInputStream(properties.getBytes()));
            } catch (IOException e) {
                return null;
            }
        }
        return p;
    }

    public void setUserProperties(Properties p) {
        this.p = p;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            p.store(bos,"");
            properties = new ByteTypeContent(bos.toByteArray());
        } catch (IOException e) {

        }
    }
    public ByteTypeContent getPruefungsListe() {
        return pruefungsListe;
    }

    public void setPruefungsListe(ByteTypeContent pruefungsListe) {
        this.pruefungsListe = pruefungsListe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPruefung() {
        return pruefung;
    }

    public void setPruefung(String pruefung) {
        this.pruefung = pruefung;
    }

    public String getPruefungsSession() {
        return pruefungsSession;
    }

    public void setPruefungsSession(String pruefungsSession) {
        this.pruefungsSession = pruefungsSession;
    }

    public String getStudentenNummer() {
        return studentenNummer;
    }

    public void setStudentenNummer(String studentenNummer) {
        this.studentenNummer = studentenNummer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
