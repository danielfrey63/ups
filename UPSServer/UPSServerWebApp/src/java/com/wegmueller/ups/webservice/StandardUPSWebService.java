package com.wegmueller.ups.webservice;

import com.thoughtworks.xstream.XStream;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.lang.reflect.Method;

import sun.plugin.com.event.ListenerProxy;


/**
 * Created by: Thomas Wegmueller
 * Date: 27.09.2005,  00:40:44
 */
public class StandardUPSWebService {
    private static UPSWebServiceImpl instance;
    private static XStream xstream;


    public static String invoke(String[] args) {
        if (args.length==0) return o2x("hello from ups");
        if (args.length==1)  {
            try {
                return o2x(getUPSWebService().getClass().getMethod(args[0],new Class[]{}).invoke(getUPSWebService()));
            } catch (Throwable e) {
                return o2x(e);
            }
        }
        Object[] a = new Object[args.length-1];
        Class[] c = new Class[args.length-1];
        for (int i=1;i<args.length;i++) {
            Object o = x2o(args[i]);
            if (o instanceof Class) {
                c[i-1] = (Class) o;
                a[i-1] = null;
            } else {
                c[i-1] = o.getClass();
                a[i-1] = o;
            }
            if (Calendar.class.isAssignableFrom(c[i-1] )) {
                c[i-1] = Calendar.class;
            }
        }

        try {
            getUPSWebService().open();
            Method m = getUPSWebService().getClass().getMethod(args[0], c);
            return  o2x(m.invoke(getUPSWebService(),a));
        } catch (Throwable e) {
            e.printStackTrace();
            return o2x(e);
        } finally {
            getUPSWebService().close();
        }
    }


    public static UPSWebServiceImpl getUPSWebService() {
        if (instance == null) {
            instance = new UPSWebServiceImpl(new UPSServerService());
        }
        return instance;
    }

    public static XStream getXStream() {
        if (xstream == null) {
            xstream = new XStream();
        }
        return xstream;
    }


    public static String o2x(Object o) {
        return getXStream().toXML(o);
    }

    private static Object x2o(String arg) {
        return getXStream().fromXML(arg);


    }


    public static void main(String[] args) {
        Calendar c = new GregorianCalendar(2005,9,7,0,0);
        String s=
        StandardUPSWebService.invoke(
                new String[]{"getAnmeldungen",
                        o2x("baltisberger"),
                        o2x("baltisberger"),
                        o2x("2005H"),
                        o2x(String[].class),
                        o2x((Calendar)c
                        )}
        );

        System.out.println(s);


    }
}
