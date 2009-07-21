package com.wegmueller.ups.ldap;

import java.util.Properties;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  18:06:30
 */
public interface ILDAPUserRecord {
    String KEY_FIRSTNAME = "givenName";
    String KEY_FAMILYNAME = "sn";
    String KEY_USERNAME = "uid";
    String KEY_PASSWORD = "userPassword";

    /**
     * User id des Studis
     * @return
     */
    Object getName();

    /**
     * Leginummer des Studis
     * @return
     */
    String getStudentenNummer();

    /**
     * Attribute aus LDAP
     * @return
     */
    Properties getAttributes();
}
