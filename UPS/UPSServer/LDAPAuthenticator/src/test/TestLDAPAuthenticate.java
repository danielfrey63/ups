
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import com.wegmueller.ups.ldap.LDAPAuthException;
import com.wegmueller.ups.ldap.LDAPAuthenticate;
import junit.framework.TestCase;

/**
 * Test Class for LDAPAuthenticate
 */
public class TestLDAPAuthenticate extends TestCase {
    private ILDAPUserRecord record = null;
    private LDAPAuthException error = null;
    private Throwable throwable;
    private LDAPAuthenticate auth;


    protected void setUp() throws Exception {
        auth = new LDAPAuthenticate();
        LDAPAuthenticate.HOSTS = LDAPAuthenticate.DEFAULT_HOSTS;
        record = null;
        error = null;
        throwable = null;
    }


    /**
     * testing with the right infrastructure and a correct user name and password
     */
    public void testOKUseCase() {
        call("dfrey", "leni1234");
        assertNull(throwable);
        assertNull(error);
        assertNotNull(record);
        assertSame("dfrey", record.getName());
        assertTrue(record.getAttributes().size() > 0);
    }

    /**
     * Testing if one of the ldap hosts is missing
     */
    public void testOneMissingHost() {
        LDAPAuthenticate.HOSTS = new String[]{"www.wegmueller.com", "ldaps01.ups.ch"};

        call("dfrey", "leni1234");
        assertNull(throwable);
        assertNull(error);
        assertNotNull(record);
        assertSame("dfrey", record.getName());
        assertTrue(record.getAttributes().size() > 0);
    }

    /**
     * test if none of the hosts can answer
     */
    public void testAllMissing() {
        LDAPAuthenticate.HOSTS = new String[]{"www.wegmueller.com", "www.sternenberg.ch"};

        call("dfrey", "leni1234");
        assertNull(throwable);
        assertNull(record);
        assertNotNull(error);
        assertEquals(error.getName(),LDAPAuthException.INFRASTRUCTURE_EXCEPTION);
    }

    /**
     * test what happens if a user miss-spells username and/or password
     */
    public void testInvalidCredentials() {
        call("dfrey", "sakdfjlasjfdadsf");
        assertNull(throwable);
        assertNull(record);
        assertNotNull(error);
        assertEquals(error.getName(), LDAPAuthException.INVALID_CREDENTIALS);
    }


    /**
     * helper call
     * @param name  username to test
     * @param pw    password to test
     */
    private void call(String name, String pw) {
        try {
            record = auth.getUserData(name, pw);
        } catch (LDAPAuthException e) {
            error = e;
        } catch (Throwable e) {
            throwable = e;
        }
    }
}
