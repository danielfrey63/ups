package com.wegmueller.ups.ust;

import com.wegmueller.ups.ldap.ILDAPUserRecord;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  18:18:03
 */
public interface IUSTBusinessDelegate {

    /**
     * Produziere ein PDF für den Studenten und die Prüfungsliste
     * @param userName      StudiName
     * @param password      StudiPasswort
     * @param list          LDAP Eigenschaften des Studis
     * @param bytes         Eingereichte Prüfungsliste
     * @return
     * @throws USTException
     *
     * @see com.wegmueller.ups.ldap.ILDAPUserRecord#getAttributes()
     */
    byte[] producePDF(String userName, String password, ILDAPUserRecord list, byte[] bytes) throws USTException;
}
