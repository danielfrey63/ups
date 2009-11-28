package com.wegmueller.ups.ldap;

/**
 * Schnittstelle zu LDAP
 */
public interface ILDAPAuth
{
    /**
     * Userdaten abholen
     *
     * @param userName
     * @param password
     * @return
     * @throws LDAPAuthException
     */
    ILDAPUserRecord getUserData( String userName, String password ) throws LDAPAuthException;

}
