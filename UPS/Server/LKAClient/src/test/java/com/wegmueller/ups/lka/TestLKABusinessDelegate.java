package com.wegmueller.ups.lka;

import junit.framework.TestCase;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  23:55:38 */
public class TestLKABusinessDelegate extends TestCase
{
    private LKABusinessDelegate system;

    protected void setUp() throws Exception
    {
        system = new LKABusinessDelegate();
    }

    public void testReload()
    {
        Throwable e = null;
        try
        {
            final ILKAData data = system.reload("aksjf", "lkasdjf");
            assertNull(data);
        }
        catch (LKABusinessDelegateException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        assertTrue(e instanceof LKABusinessDelegateException);

        assertEquals(LKABusinessDelegateException.INVALID_CREDENTIALS, ((LKABusinessDelegateException) e).getName());

        e = null;
        try
        {
            final ILKAData data = system.reload("baltisberger", "HmBfDsSt!");
            assertNotNull(data);
            assertNotNull(data.getPruefungsSession());
            assertNotNull(data.getAnmeldedaten());
            assertTrue(data.getAnmeldedaten().length > 0);
            assertEquals(data.getPruefungsSession().getSeskez(), "2005H");
        }
        catch (LKABusinessDelegateException ex)
        {
            ex.printStackTrace();
            e = ex;
        }
        assertNull(e);
    }

}
