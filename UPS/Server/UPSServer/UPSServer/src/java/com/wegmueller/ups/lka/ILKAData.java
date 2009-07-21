package com.wegmueller.ups.lka;

/**
 * OIS Daten
 *
 */
public interface ILKAData {
    /**
     * Anmeldungen für alle Prüfungen
     * @return
     */
    IAnmeldedaten[] getAnmeldedaten();

    /**
     * Aktuelle Prüfungssession
     * @return
     */
    IPruefungsSession getPruefungsSession();
}
