package com.wegmueller.ups.lka.data;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by: Thomas Wegmueller
 * Date: 20.09.2005,  12:42:04
 */
public class CalendarUtils {

    private static final Logger log = Logger.getLogger(CalendarUtils.class);
    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat TF = new SimpleDateFormat("HH:mm");

    public static final Calendar parsePlanungFreigabe(final String s) {
        if (s==null) return null;
        final GregorianCalendar c = new GregorianCalendar();
        try {
            c.setTime(DF.parse(s));
            return c;
        } catch (ParseException e) {
            log.warn(e);
            return null;
        }
    }

    public static Calendar parsePruefungsdatum(final String pruefungsdatum) {
        return parsePlanungFreigabe(pruefungsdatum);
    }

    public static Calendar parseTime(final Calendar datum, final String vonStr) {
        final Calendar c = parseTime(vonStr);
        return new GregorianCalendar(
                datum.get(Calendar.YEAR),
                datum.get(Calendar.MONTH),
                datum.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),0);

    }

    private static Calendar parseTime(final String vonStr) {
        if (vonStr==null) return new GregorianCalendar(2005,01,01,0,0,0);
        final GregorianCalendar c = new GregorianCalendar();
        try {
            c.setTime(TF.parse(vonStr));
            return c;
        } catch (ParseException e) {
            log.warn(e);
            return new GregorianCalendar(2005,01,01,0,0,0);
        }
    }

    public static String formatDay(final Calendar c) {
        return DF.format(c.getTime());
    }

    public static Calendar parseSessionsEnded(final String sessionsende) {
        return parsePlanungFreigabe(sessionsende);

    }
}
