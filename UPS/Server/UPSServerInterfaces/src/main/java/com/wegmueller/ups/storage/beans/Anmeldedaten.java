package com.wegmueller.ups.storage.beans;

import com.wegmueller.ups.lka.IAnmeldedaten;
import java.util.Calendar;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  20:51:09 */
public class Anmeldedaten implements IAnmeldedaten {
    private Long id;

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

    private java.lang.String seskez;

    private java.lang.String lkEinheitTypText;

    private Calendar pruefungsdatum;

    private Calendar pruefungsdatumVon;

    private Calendar pruefungsdatumBis;

    private String dozentUserName;

    public String getLkNummer() {
        return lkNummer;
    }

    public void setLkNummer(final String lkNummer) {
        this.lkNummer = lkNummer;
    }

    public int getLkForm() {
        return lkForm;
    }

    public void setLkForm(final int lkForm) {
        this.lkForm = lkForm;
    }

    public String getLkFormText() {
        return lkFormText;
    }

    public void setLkFormText(final String lkFormText) {
        this.lkFormText = lkFormText;
    }

    public String getDozentUserName() {
        return dozentUserName;
    }

    public String getPruefungsmodeText() {
        return pruefungsmodeText;
    }

    public void setPruefungsmodeText(final String pruefungsmodeText) {
        this.pruefungsmodeText = pruefungsmodeText;
    }

    public String getFachrichtung() {
        return fachrichtung;
    }

    public void setFachrichtung(final String fachrichtung) {
        this.fachrichtung = fachrichtung;
    }

    public String getStudentennummer() {
        return studentennummer;
    }

    public void setStudentennummer(final String studentennummer) {
        this.studentennummer = studentennummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    public boolean isRepetent() {
        return repetent;
    }

    public void setRepetent(final boolean repetent) {
        this.repetent = repetent;
    }

    public String getLkEinheitTitel() {
        return lkEinheitTitel;
    }

    public void setLkEinheitTitel(final String lkEinheitTitel) {
        this.lkEinheitTitel = lkEinheitTitel;
    }

    public String getLkEinheitTyp() {
        return lkEinheitTyp;
    }

    public void setLkEinheitTyp(final String lkEinheitTyp) {
        this.lkEinheitTyp = lkEinheitTyp;
    }

    public String getPruefungsraum() {
        return pruefungsraum;
    }

    public void setPruefungsraum(final String pruefungsraum) {
        this.pruefungsraum = pruefungsraum;
    }

    public String getLkEinheitNummerzusatz() {
        return lkEinheitNummerzusatz;
    }

    public void setLkEinheitNummerzusatz(final String lkEinheitNummerzusatz) {
        this.lkEinheitNummerzusatz = lkEinheitNummerzusatz;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(final String nachname) {
        this.nachname = nachname;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(final String studiengang) {
        this.studiengang = studiengang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getSeskez() {
        return seskez;
    }

    public void setSeskez(final String seskez) {
        this.seskez = seskez;
    }

    public String getLkEinheitTypText() {
        return lkEinheitTypText;
    }

    public void setLkEinheitTypText(final String lkEinheitTypText) {
        this.lkEinheitTypText = lkEinheitTypText;
    }

    public Calendar getPruefungsdatum() {
        return pruefungsdatum;
    }

    public void setPruefungsdatum(final Calendar pruefungsdatum) {
        this.pruefungsdatum = pruefungsdatum;
    }

    public Calendar getPruefungsdatumVon() {
        return pruefungsdatumVon;
    }

    public void setPruefungsdatumVon(final Calendar pruefungsdatumVon) {
        this.pruefungsdatumVon = pruefungsdatumVon;
    }

    public Calendar getPruefungsdatumBis() {
        return pruefungsdatumBis;
    }

    public void setPruefungsdatumBis(final Calendar pruefungsdatumBis) {
        this.pruefungsdatumBis = pruefungsdatumBis;
    }

    public void setDozentUserName(final String dozentUserName) {
        this.dozentUserName = dozentUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
