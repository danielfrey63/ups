package com.wegmueller.ups.lka;

import java.util.Calendar;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  18:05:28 */
public interface IPruefungsSession {

    /**
     * Bezeichnung der Pr�fungssession.
     *
     * @return
     */
    String getSessionsname();

    /**
     * Datum., bis zu dem die Daten f�r diese Session g�ltig sind. Diese Angabe entspricht dem Ende der Pr�fungsession.
     * Im Intervall planungFreigabe .. sessionende sind die Planungsdaten bekannt (Pr�fungsdatum, -Raum, -Zeit) und
     * werden von der Operation retrieveAnmeldedaten geliefert.
     *
     * @return
     */
    Calendar getSessionsende();

    /**
     * Sessionkennzeichen, f�r das die Planung l�uft. Beispiele 2005F oder 2005H, wobei zuerst eine vierstellige
     * Jahresangabe vorzunehmen ist und anschliessend die Angabe F -> Fr�hling oder H -> Herbst folgt.
     *
     * @return
     */
    String getSeskez();

    /**
     * xDatum, ab wann die Daten f�r die Pr�fungsplanung frei gegeben sind. (Format: yyyy-MM-dd.) Im Intervall
     * planungFreigabe .. sessionende sind die Planungsdaten bekannt (Pr�fungsdatum, -Raum, -Zeit) und werden von der
     * Operation retrieveAnmeldedaten geliefert.
     *
     * @return
     */
    Calendar getPlanungFreigabe();
}
