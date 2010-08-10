package com.wegmueller.ups.ldap;

import com.wegmueller.ups.UPSServerException;

/** Schnittstelle zu LDAP */
public interface ILDAPAuth
{
    /**
     * Userdaten abholen
     *
     * @param userName
     * @return
     * @throws LDAPAuthException
     */
    ILDAPUserRecord getUserData( String userName ) throws UPSServerException;

    void getUserAuthentication( String userName, String password ) throws LDAPAuthException;
}
