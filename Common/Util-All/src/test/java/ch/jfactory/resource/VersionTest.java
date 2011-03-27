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

import junit.framework.TestCase;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VersionTest extends TestCase
{
    public void testString()
    {
        assertTrue( new Version( "1.1.1" ).compareTo( new Version( "1.1.2" ) ) < 0 );
        assertTrue( new Version( "1.1.1" ).compareTo( new Version( "1.1.1" ) ) == 0 );
        assertTrue( new Version( "1.1.2" ).compareTo( new Version( "1.1.1" ) ) > 0 );
        assertTrue( new Version( "1.1" ).compareTo( new Version( "1.2" ) ) < 0 );
        assertTrue( new Version( "1.1" ).compareTo( new Version( "1.1" ) ) == 0 );
        assertTrue( new Version( "1.2" ).compareTo( new Version( "1.1" ) ) > 0 );
        assertTrue( new Version( "1.1.1.1.1.1.1.1" ).compareTo( new Version( "1.1.1.1.2.1.1.1" ) ) < 0 );
        assertTrue( new Version( "1.4.2_06" ).compareTo( new Version( "1.4.2_05" ) ) > 0 );
        assertTrue( new Version( "1.4.2 build 6" ).compareTo( new Version( "1.4.2 build 5" ) ) > 0 );
    }

    public void testNumber()
    {
        assertTrue( new Version( 1, 1 ).compareTo( new Version( "1.1" ) ) == 0 );
        assertTrue( new Version( 1, 1, 1 ).compareTo( new Version( "1.1.1" ) ) == 0 );
        assertTrue( new Version( 1, 1, 1, 1 ).compareTo( new Version( "1.1.1.1" ) ) == 0 );
    }

    public void testUnequal()
    {
        assertTrue( new Version( 1, 1 ).compareTo( new Version( 1, 1, 1 ) ) < 0 );
        assertTrue( new Version( 1, 1 ).compareTo( new Version( 1, 1, 0 ) ) == 0 );
        assertTrue( new Version( 1, 1 ).compareTo( new Version( 1, 1, -1 ) ) > 0 );

        assertTrue( new Version( 1, 2 ).compareTo( new Version( 1, 1, 1 ) ) > 0 );
        assertTrue( new Version( 1, 2 ).compareTo( new Version( 1, 1, 0 ) ) > 0 );
        assertTrue( new Version( 1, 2 ).compareTo( new Version( 1, 1, -1 ) ) > 0 );
    }

    public void testMissingInfo()
    {
        boolean failed = false;
        try
        {
            new Version( "1" );
        }
        catch ( IllegalArgumentException e )
        {
            failed = true;
        }
        assertTrue( failed );
    }

    public void testWrongType()
    {
        boolean failed = false;
        try
        {
            new Version( "1.1" ).compareTo( new Object() );
        }
        catch ( IllegalArgumentException e )
        {
            failed = true;
        }
        assertTrue( failed );
    }
}
