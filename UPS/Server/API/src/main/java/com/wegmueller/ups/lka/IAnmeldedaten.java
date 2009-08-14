package com.wegmueller.ups.lka;

import java.util.Calendar;

/**
 * Anmeldedaten aus dem OIS System
 *
 * @author Thomas Wegm�ller
 * @version 0.1 26.09.2005,  18:03:23
 */
public interface IAnmeldedaten
{

    /**
     * Klartext der Angabe, ob es sich um eine schriftliche oder um eine m�ndliche Pr�fung handelt. Falls es sich weder
     * um eine schriftliche noch um eine m�ndliche Pr�fung handelt, so wird "-" ausgegeben.
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
     * Angabe, ob es sich um eine Repetition der Pr�fung handelt.
     *
     * @return
     */
    boolean isRepetent();

    /**
     * Angabe des Raums, in dem die Pr�fung stattfindet, sofern diese Angabe schon bekannt ist. Diese Angabe ist
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur f�r
     * Leistungskontrollen der Form Sessionspr�fung relevant
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
     * Code der Form der Leistungskontrolle. M�gliche Werte (siehe Spalte Code)
     * <pre>
     * Code	Kurztext	Langtext
     * 0	Keine LK	keine Leistungskontrolle
     * 1	Ses.pr�f.	Sessionspr�fung
     * 2	Sem.Endpr.	Semesterendpr�fung
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
     * M�gliche Werte (siehe Spalte Langtext).
     * Code	Kurztext	Langtext
     * 0	Keine LK	keine Leistungskontrolle
     * 1	Ses.pr�f.	Sessionspr�fung
     * 2	Sem.Endpr.	Semesterendpr�fung
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
     * Code f�r den Typ der Leistungskontrolle. M�gliche Werte (siehe Spalte Kurztext).
     * <pre>
     * Code	Kurztext	Langtext
     * 1	S	Semesterkurs
     * 2	J	Sessionspr�fung
     * 3	M	Modifizierte Pr�fung
     * </pre>
     *
     * @return
     */
    String getLkEinheitTyp();

    /**
     * Allenfalls vorhandener Nummernzusatz der Leistungskontrolle (nur im Zusammenhang mit modifizierten Pr�fungen
     * relevant).
     *
     * @return
     */
    String getLkEinheitNummerzusatz();

    /**
     * Typ der Leistungskontrolle im Klartext. M�gliche Werte (siehe Spalte Langtext).
     * <pre>
     * Code	Kurztext	Langtext
     * 1	S	Semesterkurs
     * 2	J	Sessionspr�fung
     * 3	M	Modifizierte Pr�fung
     * </pre>
     *
     * @return
     */
    String getLkEinheitTypText();

    /**
     * Sessionskennzeichen der Anmeldung. Beispiele 2005F oder 2005H, wobei zuerst eine vierstellige Jahresangabe
     * vorzunehmen ist und anschliessend die Angabe F -> Fr�hling oder H . Bemerkung: Das seskez ist bei allen
     * gelieferten Datens�tzen dasselbe. Angabe der Session, in der die Leistungskontrolle angemeldet ist. <p/> Es
     * handelt sich um die aktuelle Planungssession
     *
     * @return
     */
    String getSeskez();

    /**
     * Datum der Pr�fung, sofern dieses bereits bekannt ist. Diese Angabe ist  Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur f�r
     * Leistungskontrollen der Form Sessionspr�fung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatum();

    /**
     * Angabe der Startzeit, sofern diese bereits bekannt sind. Diese Angabe ist Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur f�r
     * Leistungskontrollen der Form Sessionspr�fung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatumVon();

    /**
     * Angabe der Endzeit, sofern diese bereits bekannt sind. Diese Angabe ist Intervall
     * WsPruefungssession.planungFreigabe .. WsPruefungssession.sessionende initialisiert. Diese Angabe ist nur f�r
     * Leistungskontrollen der Form Sessionspr�fung relevant (siehe Feld lkForm).
     *
     * @return
     */
    Calendar getPruefungsdatumBis();

}
