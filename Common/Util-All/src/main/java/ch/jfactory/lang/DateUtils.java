/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.lang;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang.StringUtils;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:59 $
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils
{
    /**
     * <p>Checks if two calendar objects are on the same year.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameYear( final Calendar cal1, final Calendar cal2 )
    {
        if ( cal1 == null || cal2 == null )
        {
            throw new IllegalArgumentException( "The date must not be null" );
        }
        return ( cal1.get( Calendar.ERA ) == cal2.get( Calendar.ERA ) &&
                cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) );
    }

    /**
     * <p>Checks if two calendar objects are on the same month.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameMonth( final Calendar cal1, final Calendar cal2 )
    {
        if ( cal1 == null || cal2 == null )
        {
            throw new IllegalArgumentException( "The date must not be null" );
        }
        return ( cal1.get( Calendar.ERA ) == cal2.get( Calendar.ERA ) &&
                cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) &&
                cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH ) );
    }

    /**
     * Returns a string representing the date difference between the two dates given in millis.Use d for day, H for
     * hour, m for minute and s for seconds to format
     *
     * @param start   start time
     * @param end     end time
     * @param pattern the pattern to format the difference
     * @return the result formatted with the given pattern and rounded.
     */
    public static String dateDifference( final long start, final long end, final String pattern )
    {
        final double diff = end - start;
        return dateDifference( diff, pattern );
    }

    /**
     * Represents the date time difference according to the pattern passed. Use d for day, H for hour, m for minute and
     * s for seconds to format
     *
     * @param difference time difference
     * @param pattern    the pattern to use to format
     * @return a representation of the date time difference
     */
    public static String dateDifference( final double difference, final String pattern )
    {
        double diff = difference;
        final char[] allowedChars = {'d', 'H', 'm', 's'};
        final String allowedString = new String( allowedChars );
        final char[] chars = pattern.toCharArray();
        final char[] foundPatternChars = findPatternChars( pattern, allowedString );
        final double[] factors = {24 * 1000 * 60 * 60, 1000 * 60 * 60, 1000 * 60, 1000};
        final StringBuffer result = new StringBuffer();
        final StringBuffer patternBuffer = new StringBuffer( pattern );
        final int length = foundPatternChars.length;
        for ( int i = 0; i < length; i++ )
        {
            final char patternChar = foundPatternChars[i];
            final int m = StringUtils.countMatches( new String( chars ), "" + patternChar );
            final double factor = factors[allowedString.indexOf( patternChar )];
            final double value = diff / factor;
            diff -= Math.floor( value ) * factor;
            final DecimalFormat f = new DecimalFormat( StringUtils.repeat( "0", m ) );
            result.append( f.format( i + 1 < length ? Math.floor( value ) : value ) );
            patternBuffer.delete( 0, m );
            while ( patternBuffer.length() > 0 && allowedString.indexOf( patternBuffer.substring( 0, 1 ) ) == -1 )
            {
                result.append( patternBuffer.substring( 0, 1 ) );
                patternBuffer.deleteCharAt( 0 );
            }
        }
        return result.toString();
    }

    private static char[] findPatternChars( final String pattern, final String patternChars )
    {
        final StringBuffer patternBuffer = new StringBuffer( pattern );
        final StringBuffer allowedBuffer = new StringBuffer( patternChars );
        for ( int i = patternBuffer.length() - 1; i >= 0; i-- )
        {
            final char currentPatternChar = patternBuffer.charAt( i );
            final int p = allowedBuffer.indexOf( "" + currentPatternChar );
            if ( p == -1 )
            {
                patternBuffer.deleteCharAt( i );
            }
            else
            {
                allowedBuffer.deleteCharAt( p );
            }
        }
        return patternBuffer.toString().toCharArray();
    }

    public static boolean isSameMonth( final Date start, final Date end )
    {
        final Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime( start );
        final Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime( end );
        return isSameMonth( c1, c2 );
    }

    public static boolean isSameYear( final Date start, final Date end )
    {
        final Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime( start );
        final Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime( end );
        return isSameYear( c1, c2 );
    }
}
