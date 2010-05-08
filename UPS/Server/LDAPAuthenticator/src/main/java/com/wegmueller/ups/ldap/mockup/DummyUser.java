package com.wegmueller.ups.ldap.mockup;

import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.ldap.LDAPUserRecord;
import java.util.Properties;

/** Created by: Thomas Wegmueller Date: 27.09.2005,  00:18:42 */
public class DummyUser extends LDAPUserRecord implements ILDAPUserRecord
{
    private final String pw;

    public DummyUser( final String uid, final String snr, final String pw, final Properties p )
    {
        super( uid );
        this.pw = pw;
        addAttribute( LDAPUserRecord.KEY_MATRICULATION_NUMBER, snr );
    }

    public String getPw()
    {
        return pw;
    }
}
