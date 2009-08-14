package com.wegmueller.ups.webservice;

import com.thoughtworks.xstream.XStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** Created by: Thomas Wegmueller Date: 27.09.2005,  00:40:44 */
public class StandardUPSWebService
{
    private static UPSWebServiceImpl instance;

    private static XStream xstream;

    public static String invoke(final String[] args)
    {
        if (args.length == 0)
        {
            return o2x("hello from ups");
        }
        if (args.length == 1)
        {
            try
            {
                final Class aClass = getUPSWebService().getClass();
                final Method method = aClass.getMethod(args[0], new Class[]{});
                final Object o = method.invoke(getUPSWebService());
                return o2x(o);
            }
            catch (Throwable e)
            {
                return o2x(e);
            }
        }
        final Object[] a = new Object[args.length - 1];
        final Class[] c = new Class[args.length - 1];
        for (int i = 1; i < args.length; i++)
        {
            final Object o = x2o(args[i]);
            if (o instanceof Class)
            {
                c[i - 1] = (Class) o;
                a[i - 1] = null;
            }
            else
            {
                c[i - 1] = o.getClass();
                a[i - 1] = o;
            }
            if (Calendar.class.isAssignableFrom(c[i - 1]))
            {
                c[i - 1] = Calendar.class;
            }
        }

        try
        {
            getUPSWebService().open();
            final Method m = getUPSWebService().getClass().getMethod(args[0], c);
            return o2x(m.invoke(getUPSWebService(), a));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return o2x(e);
        }
        finally
        {
            getUPSWebService().close();
        }
    }

    public static UPSWebServiceImpl getUPSWebService()
    {
        if (instance == null)
        {
            instance = new UPSWebServiceImpl(new UPSServerService());
        }
        return instance;
    }

    public static XStream getXStream()
    {
        if (xstream == null)
        {
            xstream = new XStream();
        }
        return xstream;
    }

    public static String o2x(final Object o)
    {
        return getXStream().toXML(o);
    }

    private static Object x2o(final String arg)
    {
        return getXStream().fromXML(arg);

    }

    public static void main(final String[] args)
    {
        final Calendar c = new GregorianCalendar(2005, 9, 7, 0, 0);
        final String s =
                StandardUPSWebService.invoke(
                        new String[]{"getAnmeldungen",
                                o2x("baltisberger"),
                                o2x("baltisberger"),
                                o2x("2005H"),
                                o2x(String[].class),
                                o2x((Calendar) c
                                )}
                );

        System.out.println(s);

    }
}
