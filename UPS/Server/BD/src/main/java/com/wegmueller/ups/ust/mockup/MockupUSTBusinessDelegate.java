package com.wegmueller.ups.ust.mockup;

import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.ust.IUSTBusinessDelegate;
import com.wegmueller.ups.ust.USTException;

/**
 * Created by: Thomas Wegmueller
 * Date: 27.09.2005,  00:36:16
 */
public class MockupUSTBusinessDelegate implements IUSTBusinessDelegate {
    public byte[] producePDF(final String userName, final String password, final ILDAPUserRecord list, final byte[] bytes) throws USTException {
        return (userName+password).getBytes();
    }
}
