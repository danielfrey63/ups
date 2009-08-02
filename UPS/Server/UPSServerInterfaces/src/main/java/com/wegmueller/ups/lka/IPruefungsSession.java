package com.wegmueller.ups.lka;

import java.util.Calendar;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  18:05:28 */
public interface IPruefungsSession {

    /**
     * Bezeichnung der Prüfungssession.
     *
     * @return
     */
    String getSessionsname();

    /**
     * Datum., bis zu dem die Daten für diese Session gültig sind. Diese Angabe entspricht dem Ende der Prüfungsession.
     * Im Intervall planungFreigabe .. sessionende sind die Planungsdaten bekannt (Prüfungsdatum, -Raum, -Zeit) und
     * werden von der Operation retrieveAnmeldedaten geliefert.
     *
     * @return
     */
    Calendar getSessionsende();

    /**
     * Sessionkennzeichen, für das die Planung läuft. Beispiele 2005F oder 2005H, wobei zuerst eine vierstellige
     * Jahresangabe vorzunehmen ist und anschliessend die Angabe F -> Frühling oder H -> Herbst folgt.
     *
     * @return
     */
    String getSeskez();

    /**
     * xDatum, ab wann die Daten für die Prüfungsplanung frei gegeben sind. (Format: yyyy-MM-dd.) Im Intervall
     * planungFreigabe .. sessionende sind die Planungsdaten bekannt (Prüfungsdatum, -Raum, -Zeit) und werden von der
     * Operation retrieveAnmeldedaten geliefert.
     *
     * @return
     */
    Calendar getPlanungFreigabe();
}
