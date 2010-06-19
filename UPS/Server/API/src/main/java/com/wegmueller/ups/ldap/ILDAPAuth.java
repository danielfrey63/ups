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
     * @return
     * @throws LDAPAuthException
     */
    ILDAPUserRecord getUserData( String userName ) throws LDAPAuthException;

}
