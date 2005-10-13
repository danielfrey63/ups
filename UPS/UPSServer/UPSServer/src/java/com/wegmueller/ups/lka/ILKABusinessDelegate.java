package com.wegmueller.ups.lka;



/**
 * Interface f�r das OIS Syststem
 * Laden der OIS Daten
 */
public interface ILKABusinessDelegate {

    ILKAData reload(String username, String password) throws LKABusinessDelegateException;

}
