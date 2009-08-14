/*
 * OperatingSystem.java - OS detection
 * Copyright (C) 2002 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ch.jfactory.resource;

import java.awt.Rectangle;
import java.awt.Toolkit;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

/**
 * Operating system detection routines.
 *
 * @author Slava Pestov
 * @version $Id: OperatingSystem.java,v 1.2 2005/08/16 23:31:02 daniel_frey Exp $
 * @since jEdit 4.0pre4
 */
public class OperatingSystem extends SystemUtils
{

    private static final Logger LOGGER = Logger.getLogger(OperatingSystem.class);

    public static Rectangle getScreenBounds()
    {
        final int screenX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        final int screenY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        final int x;
        final int y;
        final int w;
        final int h;

        if (IS_OS_MAC_OSX)
        {
            x = 0;
            y = 22;
            w = screenX;
            h = screenY - y - 4;//shadow size
        }
        else if (IS_OS_WINDOWS)
        {
            x = -4;
            y = -4;
            w = screenX - 2 * x;
            h = screenY - 2 * y;
        }
        else
        {
            x = 0;
            y = 0;
            w = screenX;
            h = screenY;
        }

        return new Rectangle(x, y, w, h);
    }

    public static boolean isWebStartApplication()
    {
        return System.getProperty("javawebstart.version") != null;
    }

    public static boolean isMinimalVersionRunning(final String minimal)
    {
        final Version runningVersion = new Version(System.getProperty("java.version"));
        final Version minimalVersion = new Version(minimal);
        LOGGER.debug("found version \"" + runningVersion + "\", minimal is \"" + minimalVersion + "\"");
        return runningVersion.compareTo(minimalVersion) >= 0;

    }
}
