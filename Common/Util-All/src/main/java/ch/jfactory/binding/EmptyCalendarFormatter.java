/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.binding;

import com.jgoodies.binding.BindingUtils;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.DateFormatter;

/**
 * In addition to its superclass DateFormatter, this class converts to/from the empty string. Therefore it holds an <em>empty value</em> that is the counterpart of the empty string. The Method <code>#valueToString</code> converts the empty value to the empty string. And <code>#stringToValue</code> converts blank strings to the empty value. In all other cases the conversion is delegated to its superclass.<p>
 *
 * Often the empty value is <code>null</code>. As an alternative you can map the empty string to a given date, for example epoch (January 1, 1970).
 *
 * <strong>Examples:</strong><pre>
 * new EmptyDateFormatter();
 * new EmptyDateFormatter(new Date(0));
 * </pre>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * @see Format
 */
public class EmptyCalendarFormatter extends DateFormatter
{
    /**
     * Holds the date that is converted to an empty string and that is the result of converting blank strings to a value.
     *
     * @see #stringToValue(String)
     * @see #valueToString(Object)
     */
    private final Date emptyValue;

    // Instance Creation ****************************************************

    /** Constructs an EmptyDateFormatter that converts <code>null</code> to the empty string and vice versa. */
    public EmptyCalendarFormatter()
    {
        this( (Date) null );
    }

    /**
     * Constructs an EmptyDateFormatter configured with the specified Format; maps <code>null</code> to the empty string and vice versa.
     *
     * @param format Format used to dictate legal values
     */
    public EmptyCalendarFormatter( final DateFormat format )
    {
        this( format, null );
    }

    /**
     * Constructs an EmptyDateFormatter that converts the given <code>emptyValue</code> to the empty string and vice versa.
     *
     * @param emptyValue the representation of the empty string
     */
    public EmptyCalendarFormatter( final Date emptyValue )
    {
        this.emptyValue = emptyValue == null
                ? null
                : new Date( emptyValue.getTime() );
    }

    /**
     * Constructs an EmptyDateFormatter configured with the specified Format; maps <code>null</code> to the given <code>emptyValue</code> and vice versa.
     *
     * @param format     Format used to dictate legal values
     * @param emptyValue the representation of the empty string
     */
    public EmptyCalendarFormatter( final DateFormat format, final Date emptyValue )
    {
        super( format );
        this.emptyValue = emptyValue == null
                ? null
                : new Date( emptyValue.getTime() );
    }

    // Overriding Superclass Behavior *****************************************

    /**
     * Returns the <code>Object</code> representation of the <code>String</code> <code>text</code>.<p>
     *
     * Unlike its superclass, this class converts blank strings to the empty value.
     *
     * @param text <code>String</code> to convert
     * @return <code>Object</code> representation of text
     * @throws ParseException if there is an error in the conversion
     */
    public Object stringToValue( final String text ) throws ParseException
    {
        return BindingUtils.isBlank( text )
                ? emptyValue
                : super.stringToValue( text );
    }

    /**
     * Returns a String representation of the Object <code>value</code>. This invokes <code>format</code> on the current <code>Format</code>.<p>
     *
     * Unlike its superclass, this class converts the empty value to the empty string.
     *
     * @param value the value to convert
     * @return a String representation of value
     * @throws ParseException if there is an error in the conversion
     */
    public String valueToString( final Object value ) throws ParseException
    {
        return BindingUtils.equals( value, emptyValue )
                ? ""
                : super.valueToString( value );
    }

}
