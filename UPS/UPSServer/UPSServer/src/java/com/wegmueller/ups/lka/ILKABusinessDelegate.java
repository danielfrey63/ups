package com.wegmueller.ups.lka;



/**
 * Interface für das OIS Syststem
 * Laden der OIS Daten
 */
public interface ILKABusinessDelegate {

    ILKAData reload(String username, String password) throws LKABusinessDelegateException;

}
