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

/** Created by: Thomas Wegmueller Date: 27.09.2005,  00:42:19 */
public class UPSServerImpl implements IUPSServerService
{
    private IStorage storageSystem = new StorageSystem();

    private ILDAPAuth ldap = new LDAPAuthenticate();

    private ILKABusinessDelegate lka = new LKABusinessDelegate();

    private IUSTBusinessDelegate ust = new USTBusinessDelegate();

    public IStorage getStorage()
    {
        return storageSystem;
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
