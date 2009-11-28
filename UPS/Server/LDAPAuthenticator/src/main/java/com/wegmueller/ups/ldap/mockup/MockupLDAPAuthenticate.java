package com.wegmueller.ups.ldap.mockup;

import com.wegmueller.ups.ldap.ILDAPAuth;
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.ldap.LDAPAuthException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by: Thomas Wegmueller Date: 27.09.2005,  00:18:03
 */
public class MockupLDAPAuthenticate implements ILDAPAuth
{
    ArrayList<DummyUser> storage = new ArrayList<DummyUser>();

    public MockupLDAPAuthenticate()
    {
        storage.add( new DummyUser( "testuser1", "03925245", "testuser1", new Properties() ) );
        storage.add( new DummyUser( "testuser2", "04912002", "testuser2", new Properties() ) );
        storage.add( new DummyUser( "testuser3", "04917902", "testuser3", new Properties() ) );
        storage.add( new DummyUser( "testuser4", "03926938", "testuser4", new Properties() ) );
        storage.add( new DummyUser( "testuser5", "03304417", "testuser5", new Properties() ) );
        storage.add( new DummyUser( "testuser6", "04912630", "testuser6", new Properties() ) );
        storage.add( new DummyUser( "testuser7", "04914727", "testuser7", new Properties() ) );
        storage.add( new DummyUser( "testuser8", "04916060", "testuser8", new Properties() ) );
        storage.add( new DummyUser( "testuser9", "04915385", "testuser9", new Properties() ) );
        storage.add( new DummyUser( "testuser10", "04922209", "testuser10", new Properties() ) );
        storage.add( new DummyUser( "testuser11", "04922951", "testuser11", new Properties() ) );
        storage.add( new DummyUser( "testuser12", "03110145", "testuser12", new Properties() ) );
        storage.add( new DummyUser( "testuser13", "03926599", "testuser13", new Properties() ) );
        storage.add( new DummyUser( "testuser14", "03910262", "testuser14", new Properties() ) );
        storage.add( new DummyUser( "testuser15", "04911632", "testuser15", new Properties() ) );
        storage.add( new DummyUser( "testuser16", "04910014", "testuser16", new Properties() ) );
        storage.add( new DummyUser( "testuser17", "04914255", "testuser17", new Properties() ) );
        storage.add( new DummyUser( "testuser18", "04919858", "testuser18", new Properties() ) );
        storage.add( new DummyUser( "testuser19", "04921623", "testuser19", new Properties() ) );
        storage.add( new DummyUser( "testuser20", "03908316", "testuser20", new Properties() ) );
        storage.add( new DummyUser( "testuser21", "04910782", "testuser21", new Properties() ) );
        storage.add( new DummyUser( "testuser22", "04915005", "testuser22", new Properties() ) );
        storage.add( new DummyUser( "testuser23", "04914081", "testuser23", new Properties() ) );
        storage.add( new DummyUser( "testuser24", "04922175", "testuser24", new Properties() ) );
        storage.add( new DummyUser( "testuser25", "04915369", "testuser25", new Properties() ) );
        storage.add( new DummyUser( "testuser26", "04922191", "testuser26", new Properties() ) );
        storage.add( new DummyUser( "testuser27", "04922654", "testuser27", new Properties() ) );
        storage.add( new DummyUser( "testuser28", "01718576", "testuser28", new Properties() ) );
        storage.add( new DummyUser( "testuser29", "04907135", "testuser29", new Properties() ) );
        storage.add( new DummyUser( "testuser30", "04907440", "testuser30", new Properties() ) );
        storage.add( new DummyUser( "testuser31", "04910808", "testuser31", new Properties() ) );
        storage.add( new DummyUser( "testuser32", "04913661", "testuser32", new Properties() ) );
        storage.add( new DummyUser( "testuser33", "04916185", "testuser33", new Properties() ) );
        storage.add( new DummyUser( "testuser34", "04914453", "testuser34", new Properties() ) );
        storage.add( new DummyUser( "testuser35", "03908183", "testuser35", new Properties() ) );
        storage.add( new DummyUser( "testuser36", "03908373", "testuser36", new Properties() ) );
        storage.add( new DummyUser( "testuser37", "03916814", "testuser37", new Properties() ) );
        storage.add( new DummyUser( "testuser38", "04909974", "testuser38", new Properties() ) );
        storage.add( new DummyUser( "testuser39", "04911046", "testuser39", new Properties() ) );
        storage.add( new DummyUser( "testuser40", "04915831", "testuser40", new Properties() ) );
        storage.add( new DummyUser( "testuser41", "04919197", "testuser41", new Properties() ) );
        storage.add( new DummyUser( "testuser42", "04923165", "testuser42", new Properties() ) );
        storage.add( new DummyUser( "testuser43", "04917050", "testuser43", new Properties() ) );
        storage.add( new DummyUser( "testuser44", "03908993", "testuser44", new Properties() ) );
        storage.add( new DummyUser( "testuser45", "03915956", "testuser45", new Properties() ) );
        storage.add( new DummyUser( "testuser46", "04910832", "testuser46", new Properties() ) );
        storage.add( new DummyUser( "testuser47", "04913430", "testuser47", new Properties() ) );
        storage.add( new DummyUser( "testuser48", "04917910", "testuser48", new Properties() ) );
        storage.add( new DummyUser( "testuser49", "04920526", "testuser49", new Properties() ) );
        storage.add( new DummyUser( "testuser50", "04922902", "testuser50", new Properties() ) );
        storage.add( new DummyUser( "testuser51", "04921904", "testuser51", new Properties() ) );
        storage.add( new DummyUser( "testuser52", "04921581", "testuser52", new Properties() ) );
        storage.add( new DummyUser( "testuser53", "04923603", "testuser53", new Properties() ) );
        storage.add( new DummyUser( "testuser54", "03716743", "testuser54", new Properties() ) );
        storage.add( new DummyUser( "testuser55", "03906948", "testuser55", new Properties() ) );
        storage.add( new DummyUser( "testuser56", "03726353", "testuser56", new Properties() ) );
        storage.add( new DummyUser( "testuser57", "03919677", "testuser57", new Properties() ) );
        storage.add( new DummyUser( "testuser58", "04916193", "testuser58", new Properties() ) );
        storage.add( new DummyUser( "testuser59", "04914974", "testuser59", new Properties() ) );
        storage.add( new DummyUser( "testuser60", "04912804", "testuser60", new Properties() ) );
        storage.add( new DummyUser( "testuser61", "04911467", "testuser61", new Properties() ) );
        storage.add( new DummyUser( "testuser62", "04916052", "testuser62", new Properties() ) );
        storage.add( new DummyUser( "testuser63", "04920534", "testuser63", new Properties() ) );
        storage.add( new DummyUser( "testuser64", "04922183", "testuser64", new Properties() ) );
        storage.add( new DummyUser( "testuser65", "03718194", "testuser65", new Properties() ) );
        storage.add( new DummyUser( "testuser66", "03731007", "testuser66", new Properties() ) );
        storage.add( new DummyUser( "testuser67", "03911609", "testuser67", new Properties() ) );
        storage.add( new DummyUser( "testuser68", "04909990", "testuser68", new Properties() ) );
        storage.add( new DummyUser( "testuser69", "04907093", "testuser69", new Properties() ) );
        storage.add( new DummyUser( "testuser70", "04918124", "testuser70", new Properties() ) );
        storage.add( new DummyUser( "testuser71", "04911475", "testuser71", new Properties() ) );
        storage.add( new DummyUser( "testuser72", "04918868", "testuser72", new Properties() ) );
        storage.add( new DummyUser( "testuser73", "02705887", "testuser73", new Properties() ) );
        storage.add( new DummyUser( "testuser74", "04910790", "testuser74", new Properties() ) );
        storage.add( new DummyUser( "testuser75", "04910824", "testuser75", new Properties() ) );
        storage.add( new DummyUser( "testuser76", "04911285", "testuser76", new Properties() ) );
        storage.add( new DummyUser( "testuser77", "04915377", "testuser77", new Properties() ) );
        storage.add( new DummyUser( "testuser78", "04916441", "testuser78", new Properties() ) );
        storage.add( new DummyUser( "testuser79", "04918132", "testuser79", new Properties() ) );
        storage.add( new DummyUser( "testuser80", "04922662", "testuser80", new Properties() ) );
        storage.add( new DummyUser( "testuser81", "02908515", "testuser81", new Properties() ) );
        storage.add( new DummyUser( "testuser82", "03737335", "testuser82", new Properties() ) );
        storage.add( new DummyUser( "testuser83", "02916278", "testuser83", new Properties() ) );
        storage.add( new DummyUser( "testuser84", "03917382", "testuser84", new Properties() ) );
        storage.add( new DummyUser( "testuser85", "04906509", "testuser85", new Properties() ) );
        storage.add( new DummyUser( "testuser86", "04912465", "testuser86", new Properties() ) );
        storage.add( new DummyUser( "testuser87", "04909982", "testuser87", new Properties() ) );
        storage.add( new DummyUser( "testuser88", "04914438", "testuser88", new Properties() ) );
        storage.add( new DummyUser( "testuser89", "04918447", "testuser89", new Properties() ) );
        storage.add( new DummyUser( "testuser90", "01909100", "testuser90", new Properties() ) );
        storage.add( new DummyUser( "testuser91", "04913422", "testuser91", new Properties() ) );
        storage.add( new DummyUser( "testuser92", "04917266", "testuser92", new Properties() ) );
        storage.add( new DummyUser( "testuser93", "04921607", "testuser93", new Properties() ) );
        storage.add( new DummyUser( "testuser94", "04921573", "testuser94", new Properties() ) );
        storage.add( new DummyUser( "testuser95", "04923744", "testuser95", new Properties() ) );
        storage.add( new DummyUser( "testuser96", "94914405", "testuser96", new Properties() ) );
        storage.add( new DummyUser( "testuser97", "02710499", "testuser97", new Properties() ) );
        storage.add( new DummyUser( "testuser98", "03916517", "testuser98", new Properties() ) );
        storage.add( new DummyUser( "testuser99", "03927035", "testuser99", new Properties() ) );
        storage.add( new DummyUser( "testuser100", "04912408", "testuser100", new Properties() ) );
        storage.add( new DummyUser( "testuser101", "04915690", "testuser101", new Properties() ) );
        storage.add( new DummyUser( "testuser102", "04921649", "testuser102", new Properties() ) );
        storage.add( new DummyUser( "testuser103", "04922688", "testuser103", new Properties() ) );
        storage.add( new DummyUser( "baltisberger", null, "baltisberger", new Properties() ) );
        storage.add( new DummyUser( "dfrey", null, "leni12345", new Properties() ) );

    }

    public ILDAPUserRecord getUserData( final String userName, final String password ) throws LDAPAuthException
    {
        for ( final DummyUser o : storage )
        {
            if ( o.getName().equals( userName ) && o.getPw().equals( password ) )
            {
                return o;
            }
        }
        throw new LDAPAuthException( LDAPAuthException.INVALID_CREDENTIALS );
    }

}
