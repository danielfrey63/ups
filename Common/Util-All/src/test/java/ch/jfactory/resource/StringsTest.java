/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.resource;

import java.awt.Color;
import java.util.ResourceBundle;
import junit.framework.TestCase;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class StringsTest extends TestCase
{

    protected void setUp() throws Exception
    {
        Strings.setResourceBundle(ResourceBundle.getBundle("ch.jfactory.resource.Strings"));
    }

    public void testColor()
    {
        checkColor("red", Color.red);
        checkColor("RED", Color.red);
        checkColor("nop", null);
        checkColor("orange-red", new Color(178, 154, 17));
    }

    private void checkColor(final String key, final Color color)
    {
        final Color c = Strings.getColor(key);
        assertTrue(c != null && c.equals(color) || color != null && color.equals(c) || c == null && color == null);
    }
}
