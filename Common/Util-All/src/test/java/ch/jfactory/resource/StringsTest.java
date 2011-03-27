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
        Strings.setResourceBundle( ResourceBundle.getBundle( "ch.jfactory.resource.Strings" ) );
    }

    public void testColor()
    {
        checkColor( "red", Color.red );
        checkColor( "RED", Color.red );
        checkColor( "nop", null );
        checkColor( "orange-red", new Color( 178, 154, 17 ) );
    }

    private void checkColor( final String key, final Color color )
    {
        final Color c = Strings.getColor( key );
        assertTrue( c != null && c.equals( color ) || color != null && color.equals( c ) || c == null && color == null );
    }
}
