package com.wegmueller.ups.storage;

import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.util.GregorianCalendar;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  22:46:20 */
public class TestAnmeldung extends Anmeldedaten
{
    public static final String EMAIL = "setEmail";

    public static final String FACHRICHTUNG = "fachrichtung";

    public static final String LK_EINHEIT_NUMMERZUSATZ = "setLkEinheitNummerzusatz";

    public static final String LK_EINHEIT_TITEL = "setLkEinheitTitel";

    public static final String LK_EINHEIT_TYP = "setLkEinheitTyp";

    public static final String LK_EINHEIT_TYP_TEXT = "setLkEinheitTypText";

    public static final int LK_FORM = 0;

    public static final String LK_FORM_TEXT = "setLkFormText";

    public static final String LK_NUMMER = "LKNUMMER-TEST";

    public static final String NACHNAME = "setNachname";

    public static final GregorianCalendar PRUEFUNGSDATUM_VON = new GregorianCalendar();

    public static final GregorianCalendar PRUEFUNGSDATUM_BIS = new GregorianCalendar();

    public static final GregorianCalendar PRUEFUNGSDATUM = new GregorianCalendar();

    public static final String PRUEFUNGSMODE_TEXT = "setPruefungsmodeText";

    public static final String PRUEFUNGSRAUM = "setPruefungsraum";

    public static final boolean REPETENT = true;

    public static final String SESKEZ = "2005H";

    public static final String STUDENTENNUMMER = "setStudentennummer";

    public static final String STUDIENGANG = "setStudiengang";

    public static final String VORNAME = "setVorname";

    public TestAnmeldung()
    {
        this.setEmail(EMAIL);
        this.setFachrichtung(FACHRICHTUNG);
        this.setLkEinheitNummerzusatz(LK_EINHEIT_NUMMERZUSATZ);
        this.setLkEinheitTitel(LK_EINHEIT_TITEL);
        this.setLkEinheitTyp(LK_EINHEIT_TYP);
        this.setLkEinheitTypText(LK_EINHEIT_TYP_TEXT);
        this.setLkForm(LK_FORM);
        this.setLkFormText(LK_FORM_TEXT);
        this.setLkNummer(LK_NUMMER);
        this.setNachname(NACHNAME);
        this.setPruefungsdatum(PRUEFUNGSDATUM);
        this.setPruefungsdatumBis(PRUEFUNGSDATUM_BIS);
        this.setPruefungsdatumVon(PRUEFUNGSDATUM_VON);
        this.setPruefungsmodeText(PRUEFUNGSMODE_TEXT);
        this.setPruefungsraum(PRUEFUNGSRAUM);
        this.setRepetent(REPETENT);
        this.setSeskez(SESKEZ);
        this.setStudentennummer(STUDENTENNUMMER);
        this.setStudiengang(STUDIENGANG);
        this.setVorname(VORNAME);

    }
}
