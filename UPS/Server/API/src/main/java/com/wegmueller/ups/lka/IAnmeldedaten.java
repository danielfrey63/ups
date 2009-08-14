package com.wegmueller.ups.lka;

import java.util.Calendar;

/**
 * Anmeldedaten aus dem OIS System
 *
 * @author Thomas Wegmüller
 * @version 0.1 26.09.2005,  18:03:23
 */
public interface IAnmeldedaten
{

    /**
     * Klartext der Angabe, ob es sich um eine schriftliche oder um eine mündliche Prüfung handelt. Falls es sich weder
     * um eine schriftliche noch um eine mündliche Prüfung handelt, so wird "-" ausgegeben.
     *
     * @return
     */
    String getPruefungsmodeText();

    /**
     * Bezeichnung der Fachrichtung, unter dem die Anmeldung erfolgt ist.
     *
     * @return
     */
    String getFachrichtung();

    /**
     * Immatrikulationsnummer
     *
     * @return
     */
    String getStudentennummer();

    /**
     * Vorname der Studentin respektive des Studenten.
     *
     * @return
     */
    String getVorname();

    /**
     * Angabe, ob es sich um eine Repetition der Prüfung handelt.
     *
     * @return
     */
    boolean isRepetent();

    /**
     * Angabe des Raums, in dem die Prüfung stattfindet, sofern diese Angabe schon bekannt ist. Diese Angabe ist
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur für
     * Leistungskontrollen der Form Sessionsprüfung relevant
     *
     * @return
     * @see IAnmeldedaten#getLkForm()
     */
    String getPruefungsraum();

    /**
     * Nachname der Studentin respektive des Studenten
     *
     * @return
     */
    String getNachname();

    /**
     * Bezeichnung des Studiengangs, unter dem die Anmeldung erfolgt ist.
     *
     * @return
     */
    String getStudiengang();

    /**
     * Bevorzugte E-Mail.
     *
     * @return
     */
    String getEmail();

    /**
     * Nummer der Leistungskontrolle. Achtung: Nicht mit der Nummer der Lerneinheit zu verwechseln.
     *
     * @return
     */
    String getLkNummer();

    /**
     * Code der Form der Leistungskontrolle. Mögliche Werte (siehe Spalte Code)
     * <pre>
     * Code	Kurztext	Langtext
     * 0	Keine LK	keine Leistungskontrolle
     * 1	Ses.prüf.	Sessionsprüfung
     * 2	Sem.Endpr.	Semesterendprüfung
     * 3	benot. SL	benotete Semesterleistung
     * 4	unben. SL	unbenotete Semesterleistung
     * </pre>
     *
     * @return
     */
    int getLkForm();

    /**
     * Form der Leistungskontrolle im Klartext.
     * <pre>
     * Mögliche Werte (siehe Spalte Langtext).
     * Code	Kurztext	Langtext
     * 0	Keine LK	keine Leistungskontrolle
     * 1	Ses.prüf.	Sessionsprüfung
     * 2	Sem.Endpr.	Semesterendprüfung
     * 3	benot. SL	benotete Semesterleistung
     * 4	unben. SL	unbenotete Semesterleistung
     * </pre>
     *
     * @return
     */
    String getLkFormText();

    /**
     * Bezeichnung der LK-Einheit.
     *
     * @return
     */
    String getLkEinheitTitel();

    /**
     * Code für den Typ der Leistungskontrolle. Mögliche Werte (siehe Spalte Kurztext).
     * <pre>
     * Code	Kurztext	Langtext
     * 1	S	Semesterkurs
     * 2	J	Sessionsprüfung
     * 3	M	Modifizierte Prüfung
     * </pre>
     *
     * @return
     */
    String getLkEinheitTyp();

    /**
     * Allenfalls vorhandener Nummernzusatz der Leistungskontrolle (nur im Zusammenhang mit modifizierten Prüfungen
     * relevant).
     *
     * @return
     */
    String getLkEinheitNummerzusatz();

    /**
     * Typ der Leistungskontrolle im Klartext. Mögliche Werte (siehe Spalte Langtext).
     * <pre>
     * Code	Kurztext	Langtext
     * 1	S	Semesterkurs
     * 2	J	Sessionsprüfung
     * 3	M	Modifizierte Prüfung
     * </pre>
     *
     * @return
     */
    String getLkEinheitTypText();

    /**
     * Sessionskennzeichen der Anmeldung. Beispiele 2005F oder 2005H, wobei zuerst eine vierstellige Jahresangabe
     * vorzunehmen ist und anschliessend die Angabe F -> Frühling oder H . Bemerkung: Das seskez ist bei allen
     * gelieferten Datensätzen dasselbe. Angabe der Session, in der die Leistungskontrolle angemeldet ist. <p/> Es
     * handelt sich um die aktuelle Planungssession
     *
     * @return
     */
    String getSeskez();

    /**
     * Datum der Prüfung, sofern dieses bereits bekannt ist. Diese Angabe ist  Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur für
     * Leistungskontrollen der Form Sessionsprüfung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatum();

    /**
     * Angabe der Startzeit, sofern diese bereits bekannt sind. Diese Angabe ist Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur für
     * Leistungskontrollen der Form Sessionsprüfung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatumVon();

    /**
     * Angabe der Endzeit, sofern diese bereits bekannt sind. Diese Angabe ist Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur für
     * Leistungskontrollen der Form Sessionsprüfung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatumBis();

}
