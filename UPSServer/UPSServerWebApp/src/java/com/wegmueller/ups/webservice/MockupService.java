package com.wegmueller.ups.webservice;

import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.ust.IUSTBusinessDelegate;
import com.wegmueller.ups.ust.USTBusinessDelegate;
import com.wegmueller.ups.ust.mockup.MockupUSTBusinessDelegate;
import com.wegmueller.ups.lka.ILKABusinessDelegate;
import com.wegmueller.ups.lka.LKABusinessDelegate;
import com.wegmueller.ups.lka.LKABusinessDelegateException;
import com.wegmueller.ups.ldap.ILDAPAuth;
import com.wegmueller.ups.ldap.LDAPAuthenticate;
import com.wegmueller.ups.ldap.mockup.MockupLDAPAuthenticate;
import com.wegmueller.ups.storage.IStorage;
import com.wegmueller.ups.storage.StorageSystem;

/**
 * Created by: Thomas Wegmueller
 * Date: 27.09.2005,  00:47:35
 */
public class MockupService implements IUPSServerService {
    private IStorage storage = new StorageSystem();
    private ILDAPAuth ldap = new MockupLDAPAuthenticate();
    private ILKABusinessDelegate lka = new LKABusinessDelegate();
    private IUSTBusinessDelegate ust=new MockupUSTBusinessDelegate();

    public IStorage getStorage() {
        return storage;
    }

    public ILDAPAuth getLDAP() {
        return ldap;
    }

    public ILKABusinessDelegate getLKA() {
        return lka;
    }

    public IUSTBusinessDelegate getUST() {
        return ust;
    }

}
