package com.wegmueller.ups;

import com.wegmueller.ups.storage.IStorage;
import com.wegmueller.ups.ldap.ILDAPAuth;
import com.wegmueller.ups.lka.ILKABusinessDelegate;
import com.wegmueller.ups.ust.IUSTBusinessDelegate;

/**
 * Server Service interface
 * Date: 26.09.2005,  19:41:45
 */
public interface IUPSServerService {
    /**
     * Storage System
     * @return
     */
    IStorage getStorage();

    /**
     * LDAP System
     * @return
     */
    ILDAPAuth getLDAP();

    /**
     * LKA System
     * @return
     */
    ILKABusinessDelegate getLKA();

    /**
     * UST (PDF-producing)
     * @return
     */
    IUSTBusinessDelegate getUST();

}
