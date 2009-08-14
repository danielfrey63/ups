/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.converter;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/15 23:03:21 $
 */
public class ISO8601CalendarConverter extends AbstractSingleValueConverter
{

    private DateTimeFormatter[] formatters;

    public ISO8601CalendarConverter()
    {
        this.formatters = createISOFormatters();
    }

    public boolean canConvert(final Class type)
    {
        return type.equals(GregorianCalendar.class);
    }

    public Object fromString(final String str)
    {
        for (final DateTimeFormatter formatter : formatters)
        {
            try
            {
                final DateTime dt = formatter.parseDateTime(str);
                return dt.toCalendar(Locale.getDefault());
            }
            catch (IllegalArgumentException e)
            {
                // try with next formatter
            }
        }
        throw new ConversionException("Cannot parse date " + str);
    }

    public String toString(final Object obj)
    {
        final DateTime dt = new DateTime(obj);
        return dt.toString();
    }

    private DateTimeFormatter[] createISOFormatters()
    {
        final List<DateTimeFormatter> isoFormatters = new ArrayList<DateTimeFormatter>();
        isoFormatters.add(ISODateTimeFormat.dateTime());
        isoFormatters.add(ISODateTimeFormat.dateTimeParser());
        isoFormatters.add(ISODateTimeFormat.dateTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.basicDate());
        isoFormatters.add(ISODateTimeFormat.basicDateTime());
        isoFormatters.add(ISODateTimeFormat.basicDateTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.basicTime());
        isoFormatters.add(ISODateTimeFormat.basicTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.basicTTime());
        isoFormatters.add(ISODateTimeFormat.basicTTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.basicWeekDate());
        isoFormatters.add(ISODateTimeFormat.basicWeekDateTime());
        isoFormatters.add(ISODateTimeFormat.basicWeekDateTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.date());
        isoFormatters.add(ISODateTimeFormat.dateHour());
        isoFormatters.add(ISODateTimeFormat.dateHourMinute());
        isoFormatters.add(ISODateTimeFormat.dateHourMinuteSecond());
        isoFormatters.add(ISODateTimeFormat.dateHourMinuteSecondFraction());
        isoFormatters.add(ISODateTimeFormat.dateHourMinuteSecondMillis());
        isoFormatters.add(ISODateTimeFormat.hour());
        isoFormatters.add(ISODateTimeFormat.hourMinute());
        isoFormatters.add(ISODateTimeFormat.hourMinuteSecond());
        isoFormatters.add(ISODateTimeFormat.hourMinuteSecondFraction());
        isoFormatters.add(ISODateTimeFormat.hourMinuteSecondMillis());
        isoFormatters.add(ISODateTimeFormat.time());
        isoFormatters.add(ISODateTimeFormat.timeNoMillis());
        isoFormatters.add(ISODateTimeFormat.tTime());
        isoFormatters.add(ISODateTimeFormat.tTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.weekDate());
        isoFormatters.add(ISODateTimeFormat.weekDateTime());
        isoFormatters.add(ISODateTimeFormat.weekDateTimeNoMillis());
        isoFormatters.add(ISODateTimeFormat.weekyear());
        isoFormatters.add(ISODateTimeFormat.weekyearWeek());
        isoFormatters.add(ISODateTimeFormat.weekyearWeekDay());
        return (DateTimeFormatter[]) isoFormatters.toArray(new DateTimeFormatter[isoFormatters.size()]);
    }
}
