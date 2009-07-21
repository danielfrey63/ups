package com.wegmueller.ups.lka;

/**
 * OIS Daten
 *
 */
public interface ILKAData {
    /**
     * Anmeldungen f�r alle Pr�fungen
     * @return
     */
    IAnmeldedaten[] getAnmeldedaten();

    /**
     * Aktuelle Pr�fungssession
     * @return
     */
    IPruefungsSession getPruefungsSession();
}
