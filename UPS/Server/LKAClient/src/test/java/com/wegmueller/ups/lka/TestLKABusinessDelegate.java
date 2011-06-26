package com.wegmueller.ups.lka;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  23:55:38 */
public class TestLKABusinessDelegate extends TestCase
{
    private LKABusinessDelegate system;

    protected void setUp() throws Exception
    {
        system = new LKABusinessDelegate();
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldWork()
    {
        throw new IllegalArgumentException( "hallo" );
    }

    @Ignore
    @Test( expected = LKABusinessDelegateException.class )
    public void shouldThrowExceptionOnWrongCredentials() throws LKABusinessDelegateException
    {
        final ILKAData data = system.reload( "aksjf", "lkasdjf" );
        assertNull( data );
    }

    @Ignore
    @Test
    public void shouldThrowInvalidCredentialsExceptionOnWrongCredentials() throws LKABusinessDelegateException
    {
        try
        {
            final ILKAData data = system.reload( "aksjf", "lkasdjf" );
            assertNull( data );
        }
        catch ( LKABusinessDelegateException e )
        {
            assertEquals( LKABusinessDelegateException.INVALID_CREDENTIALS, e.getName() );
        }
    }

    @Ignore
    @Test
    public void shouldReloadSuccessfully() throws LKABusinessDelegateException
    {
        final ILKAData data = system.reload( "baltisberger", "HmBfDsSt!" );
        assertNotNull( data );
        assertNotNull( data.getPruefungsSession() );
        assertNotNull( data.getAnmeldedaten() );
        assertTrue( data.getAnmeldedaten().length > 0 );
    }

}
