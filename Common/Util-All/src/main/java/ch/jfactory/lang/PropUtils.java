/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.lang;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Property Utilities.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:09:31 $
 */
public final class PropUtils
{
    /**
     * Filters properties according to the given prefix. The prefix is removed from the resulting property keys. Keys equaling to the prefix given are stored with the given default as key.
     *
     * @param properties the properties to filter
     * @param keyPrefix  the prefix to use for the keys
     * @param defaultKey
     * @return a new Properties object containing the found mappings or null if none could be found or input properties are null.
     */
    public static Properties filterProperties( final Properties properties, final String keyPrefix, final String defaultKey )
    {
        if ( properties == null )
        {
            return null;
        }
        final Enumeration e = properties.keys();
        final Properties p = new Properties();
        while ( e.hasMoreElements() )
        {
            final String key = e.nextElement().toString();
            if ( key.equals( keyPrefix ) )
            {
                p.put( defaultKey, properties.get( key ) );
            }
            else if ( key.startsWith( keyPrefix ) )
            {
                p.put( key.substring( keyPrefix.length() + 1 ), properties.get( key ) );
            }
        }
        return ( p.isEmpty() ? null : p );
    }

    /**
     * Extracts all values of properties where the key starts with the given prefix.
     *
     * @param properties
     * @param prefix
     */
    public static String[] filterProperties( final Properties properties, final String prefix )
    {
        final Enumeration<?> enumeration = properties.propertyNames();
        final TreeMap<String, String> map = new TreeMap<String, String>();
        while ( enumeration.hasMoreElements() )
        {
            final String key = (String) enumeration.nextElement();
            if ( key.startsWith( prefix ) )
            {
                map.put( key, properties.getProperty( key ) );
            }
        }
        final ArrayList<String> list = new ArrayList<String>( map.values() );
        return list.toArray( new String[list.size()] );
    }

    public static void main( final String[] args )
    {
        final Properties p = new Properties();
        p.put( "asdf.03", "03" );
        p.put( "asdf.12", "12" );
        p.put( "asdf.00", "00" );
        p.put( "asdf.11", "11" );
        p.put( "asdf.13", "13" );
        p.put( "asdf.02", "02" );
        p.put( "asdf.10", "10" );
        p.put( "asdf.01", "01" );
        final String[] s = filterProperties( p, "asdf." );
        for ( final String value : s )
        {
            System.out.println( value );
        }
    }
}
