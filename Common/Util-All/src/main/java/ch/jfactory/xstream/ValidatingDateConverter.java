/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */

package ch.jfactory.xstream;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.core.util.ThreadSafeSimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 01.05.2010 14:56:03
 */
public class ValidatingDateConverter extends AbstractSingleValueConverter
{
    private final ThreadSafeSimpleDateFormat[] formats;

    public ValidatingDateConverter()
    {
        this( "yyyy-MM-dd HH:mm:ss.S z", "yyyy-MM-dd HH:mm:ss.S a",
                "yyyy-MM-dd HH:mm:ssz", "yyyy-MM-dd HH:mm:ss z", // JDK 1.3 needs both versions
                "yyyy-MM-dd HH:mm:ssa" ); // backwards compatibility
    }

    public ValidatingDateConverter( final String... formats )
    {
        this.formats = new ThreadSafeSimpleDateFormat[formats.length];
        for ( int i = 0; i < formats.length; i++ )
        {
            this.formats[i] = new ThreadSafeSimpleDateFormat( formats[i], 1, 20 );
        }
    }

    public boolean canConvert( final Class type )
    {
        return type.equals( Date.class );
    }

    public Object fromString( final String str )
    {
        for ( final ThreadSafeSimpleDateFormat format : formats )
        {
            try
            {
                final Date date = format.parse( str );
                final String result = format.format( date );
                if ( result != null && !result.equals( str ) )
                {
                    continue;
                }
                return date;
            }
            catch ( ParseException e2 )
            {
                // no worries, let's try the next format.
            }
        }
        // no dateFormats left to try
        throw new ConversionException( "Cannot parse date " + str );
    }

    public String toString( final Object obj )
    {
        return formats[0].format( (Date) obj );
    }
}
