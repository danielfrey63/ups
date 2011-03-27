/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.resource;

import java.awt.Rectangle;
import java.awt.Toolkit;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operating system detection routines.
 *
 * @author Slava Pestov
 * @version $Id: OperatingSystem.java,v 1.2 2005/08/16 23:31:02 daniel_frey Exp $
 * @since jEdit 4.0pre4
 */
public class OperatingSystem extends SystemUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OperatingSystem.class );

    public static Rectangle getScreenBounds()
    {
        final int screenX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        final int screenY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        final int x;
        final int y;
        final int w;
        final int h;

        if ( IS_OS_MAC_OSX )
        {
            x = 0;
            y = 22;
            w = screenX;
            h = screenY - y - 4;//shadow size
        }
        else if ( IS_OS_WINDOWS )
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

        return new Rectangle( x, y, w, h );
    }

    public static boolean isWebStartApplication()
    {
        return System.getProperty( "javawebstart.version" ) != null;
    }

    public static boolean isMinimalVersionRunning( final String minimal )
    {
        final Version runningVersion = new Version( System.getProperty( "java.version" ) );
        final Version minimalVersion = new Version( minimal );
        LOGGER.debug( "found version \"" + runningVersion + "\", minimal is \"" + minimalVersion + "\"" );
        return runningVersion.compareTo( minimalVersion ) >= 0;

    }
}
