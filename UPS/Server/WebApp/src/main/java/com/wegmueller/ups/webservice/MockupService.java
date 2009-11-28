package com.wegmueller.ups.webservice;

import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.ldap.ILDAPAuth;
import com.wegmueller.ups.ldap.mockup.MockupLDAPAuthenticate;
import com.wegmueller.ups.lka.ILKABusinessDelegate;
import com.wegmueller.ups.lka.LKABusinessDelegate;
import com.wegmueller.ups.storage.IStorage;
import com.wegmueller.ups.storage.StorageSystem;
import com.wegmueller.ups.ust.IUSTBusinessDelegate;
import com.wegmueller.ups.ust.mockup.MockupUSTBusinessDelegate;

/**
 * Created by: Thomas Wegmueller Date: 27.09.2005,  00:47:35
 */
public class MockupService implements IUPSServerService
{
    private final IStorage storage = new StorageSystem();

    private final ILDAPAuth ldap = new MockupLDAPAuthenticate();

    private final ILKABusinessDelegate lka = new LKABusinessDelegate();

    private final IUSTBusinessDelegate ust = new MockupUSTBusinessDelegate();

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
