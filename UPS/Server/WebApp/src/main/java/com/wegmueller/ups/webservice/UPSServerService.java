package com.wegmueller.ups.webservice;

import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.ldap.ILDAPAuth;
import com.wegmueller.ups.ldap.LDAPAuthenticate;
import com.wegmueller.ups.lka.ILKABusinessDelegate;
import com.wegmueller.ups.lka.LKABusinessDelegate;
import com.wegmueller.ups.storage.IStorage;
import com.wegmueller.ups.storage.StorageSystem;
import com.wegmueller.ups.ust.IUSTBusinessDelegate;
import com.wegmueller.ups.ust.USTBusinessDelegate;

/**
 * Created by: Thomas Wegmueller Date: 26.09.2005,  21:21:28
 */
public class UPSServerService implements IUPSServerService
{
    private final ILDAPAuth ldap = new LDAPAuthenticate();

    private final ILKABusinessDelegate lka = new LKABusinessDelegate();

    private final IUSTBusinessDelegate ust = new USTBusinessDelegate();

    private final IStorage storage = new StorageSystem();

    public IStorage getStorage()
    {
        return storage;
    }

    public ILDAPAuth getLDAP()
    {
        return ldap;
    }

    public ILKABusinessDelegate getLKA()
    {
        return lka;
    }

    public IUSTBusinessDelegate getUST()
    {
        return ust;
    }
}
