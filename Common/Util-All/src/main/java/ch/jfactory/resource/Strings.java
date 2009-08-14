/*
 * Copyright 2002 by x-matrix Switzerland
 *
 * Strings.java
 *
 * Created on ??. ??? 2002, ??:??
 * Created by Thomas Wegmüller
 */
package ch.jfactory.resource;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Class for retrieving locale-sensitive messages. It optionally utilizes the <code>MessageFormat</code> class to
 * produce internationalized messages with parametric replacement. <p/>
 *
 * Missing entries can be written to a file given by the system property <code>herbar.texte.logmissing</code>.
 * Initialize the class with {@link Strings#setResourceBundle(ResourceBundle)} for a simple application or with {@link
 * Strings#addResourceBundle(java.lang.Object, java.util.ResourceBundle)} if you want to handle different bundles.<p/>
 *
 * All calls that are not found produce an entry in a file. The file is taken from the system property <code>
 * jfactory.strings.logmissing</code>.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/22 15:05:10 $
 */
public class Strings
{

    public static final char MISSING_CHAR = '?';

    private static final Logger LOGGER = Logger.getLogger(Strings.class);

    /** Prefix and postfix for missing resource strings */
    private static final String MISSING = "!";

    /** Used for logging missing Texts */
    private static File logWriterFile = null;

    /** Loaded ResourceBundle for default Locale */
    private static ResourceBundle generalBundle = getRessource(null);

    /** Table of ResourceBunlde's */
    private static Map bundles = new HashMap();

    /** Caches Formats, for Messages. Only those are cached */
    private static HashMap formats = new HashMap();

    /**
     * Sets new ResourceBundle
     *
     * @param r the ResourceBundle to be installed
     */
    public static void setResourceBundle(final ResourceBundle r)
    {
        generalBundle = r;
        formats = new HashMap();
    }

    /**
     * Sets a new ResourceBundle and associates it with the given object.
     *
     * @param obj the key used for the bundle
     * @param r   the bundle
     */
    public static void addResourceBundle(final Object obj, final ResourceBundle r)
    {
        bundles.put(obj, r);
    }

    public static char getChar(final char key)
    {
        return getRessourceChar(generalBundle, "" + key);
    }

    public static char getChar(final String key)
    {
        return getRessourceChar(generalBundle, key);
    }

    public static char getChar(final Object obj, final char key)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getRessourceChar(res, "" + key);
    }

    public static char getChar(final Object obj, final String key)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getRessourceChar(res, key);
    }

    /**
     * Gets a text from the global ressource. In the key, empty spaces are converted to '_'-characters
     *
     * @param key the message key
     * @return the message as a i18n string
     */
    public static String getString(final String key)
    {
        return getString(generalBundle, key);
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with one placeholders
     * @param arg       argument for filling into the message
     * @return the message as a i18n string
     */
    public static String getString(final String formatKey, final String arg)
    {
        return getString(generalBundle, formatKey, new Object[]{arg});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with two placeholders
     * @param arg1      argument for filling into the message for first placeholder
     * @param arg2      argument for filling into the message for second placeholder
     * @return the message as a i18n string
     */
    public static String getString(final String formatKey, final String arg1, final String arg2)
    {
        return getString(generalBundle, formatKey,
                new Object[]{arg1, arg2});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with placeholders for any number of placeholders
     * @param args      arguments for filling into the message
     * @return the message as a i18n string
     */
    public static String getString(final String formatKey, final Object[] args)
    {
        return getString(generalBundle, formatKey, args);
    }

    /**
     * Gets a text from the ressource associated with the given object. Empty spaces in the key are converted to
     * '_'-characters. If the given object is null, the global resource is used.
     *
     * @param key the message key
     * @return the message as a i18n string
     */
    public static String getString(final Object obj, final String key)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getString(res, key);
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters. If the given object is null, the global resource is used.
     *
     * @param formatKey the message key with one placeholders
     * @param arg       argument for filling into the message for first placeholder
     * @return the message as a i18n string
     */
    public static String getString(final Object obj, final String formatKey, final String arg)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getString(res, formatKey, new Object[]{arg});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters. If the given object is null, the global resource is used.
     *
     * @param formatKey the message key with one placeholders
     * @param arg1      argument for filling into the message for first placeholder
     * @param arg2      argument for filling into the message for second placeholder
     * @return the message as a i18n string
     */
    public static String getString(final Object obj, final String formatKey, final Object arg1, final Object arg2)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getString(res, formatKey, new Object[]{arg1, arg2});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters. If the given object is null, the global resource is used. Errors not finding strings are
     * logged.
     *
     * @param formatKey the message key with any number of placeholders
     * @param args      arguments for filling into the message
     * @return the message as a i18n string
     */
    public static String getString(final Object obj, final String formatKey, final Object[] args)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getString(res, formatKey, args);
    }

    /**
     * Gets a text from the global ressource. In the key, empty spaces are converted to '_'-characters
     *
     * @param key the message key
     * @return the message as a i18n string
     */
    public static String getSilentString(final String key)
    {
        return getSilentString(generalBundle, key);
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with one placeholders
     * @param arg       argument for filling into the message
     * @return the message as a i18n string
     */
    public static String getSilentString(final String formatKey, final String arg)
    {
        return getSilentString(generalBundle, formatKey, new Object[]{arg});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with two placeholders
     * @param arg1      argument for filling into the message for first placeholder
     * @param arg2      argument for filling into the message for second placeholder
     * @return the message as a i18n string
     */
    public static String getSilentString(final String formatKey, final String arg1, final String arg2)
    {
        return getSilentString(generalBundle, formatKey,
                new Object[]{arg1, arg2});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters.
     *
     * @param formatKey the message key with placeholders for any number of placeholders
     * @param args      arguments for filling into the message
     * @return the message as a i18n string
     */
    public static String getSilentString(final String formatKey, final Object[] args)
    {
        return getSilentString(generalBundle, formatKey, args);
    }

    /**
     * Gets a text from the ressource associated with the given object. Empty spaces in the key are converted to
     * '_'-characters. If the given object is null, the global resource is used.
     *
     * @param key the message key
     * @return the message as a i18n string
     */
    public static String getSilentString(final Object obj, final String key)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getSilentString(res, key);
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters. If the given object is null, the global resource is used.
     *
     * @param formatKey the message key with one placeholders
     * @param arg1      argument for filling into the message for first placeholder
     * @param arg2      argument for filling into the message for second placeholder
     * @return the message as a i18n string
     */
    public static String getSilentString(final Object obj, final String formatKey, final Object arg1, final Object arg2)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getSilentString(res, formatKey, new Object[]{arg1, arg2});
    }

    /**
     * Formats a localized text with the given arguments from the global resource. Empty spaces in the key are converted
     * to '_'-characters. If the given object is null, the global resource is used. An exception is thrown when a string
     * cannot be found.
     *
     * @param formatKey the message key with any number of placeholders
     * @param args      arguments for filling into the message
     * @return the message as a i18n string
     */
    public static String getSilentString(final Object obj, final String formatKey, final Object[] args)
    {
        final ResourceBundle res = getBundleForObject(obj);
        return getSilentString(res, formatKey, args);
    }

    public static Color getColor(final String key)
    {
        final String value = getString(generalBundle, key);
        if (value.startsWith("#"))
        {
            return Color.decode(value);
        }
        else
        {
            final Class clazz = Color.class;
            try
            {
                final Object fieldValue = clazz.getField(value).get(null);
                if (fieldValue instanceof Color)
                {
                    return (Color) fieldValue;
                }
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    private static String getString(final ResourceBundle res, final String key)
    {
        String message = getRessourceString(res, key);
        if (message == null)
        {
            message = getMissing(key);
        }
        return message;
    }

    private static String getSilentString(final ResourceBundle res, final String key)
    {
        String message = getSilentRessourceString(res, key);
        if (message == null)
        {
            message = getMissing(key);
        }
        return message;
    }

    private static String getString(final ResourceBundle res, final String formatKey, final Object[] args)
    {
        MessageFormat format = null;
        synchronized (formats)
        {
            format = (MessageFormat) formats.get(formatKey);
            if (format == null)
            {
                String formatString = getRessourceString(res, formatKey);
                if (formatString == null)
                {
                    formatString = getMissing(formatKey);
                }
                format = new MessageFormat(formatString);
                formats.put(formatKey, format);
            }
        }
        return (format.format(args));
    }

    private static String getSilentString(final ResourceBundle res, final String formatKey, final Object[] args)
    {
        MessageFormat format = null;
        synchronized (formats)
        {
            format = (MessageFormat) formats.get(formatKey);
            if (format == null)
            {
                String formatString = getSilentRessourceString(res, formatKey);
                if (formatString == null)
                {
                    formatString = getMissing(formatKey);
                }
                format = new MessageFormat(formatString);
                formats.put(formatKey, format);
            }
        }
        return (format.format(args));
    }

    /**
     * Determines the format of missing resource <code>String</code>s
     *
     * @param key the key wich is missing
     * @return a String representing the missing key
     */
    private static String getMissing(final String key)
    {
        return MISSING + key + MISSING;
    }

    /**
     * Gets java.util.ResourceBundle for a given locale
     *
     * @param loc locale to get the ResourceBundle for (null = default locale)
     * @return java.util.ResourceBundle
     */
    private static ResourceBundle getRessource(final Locale loc)
    {
        String resource = null;
        try
        {
            resource = System.getProperty("jfactory.strings.resource", "com.ethz.geobot.herbar.gui.Strings");

            if (loc == null)
            {
                return ResourceBundle.getBundle(resource);
            }
            else
            {
                return ResourceBundle.getBundle(resource, loc);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("trying to debug resource loading directly: " + Strings.class.getResource(resource + ".properties"));
            LOGGER.error("could not find ressource: " + resource + (resource == null ? "" : ".properties"), e);
        }
        return null;
    }

    /**
     * Gets a String or Message from the loaded ResourceBundle Empty spaces are converted to '_'-characters
     *
     * @param key the message key
     * @return the i18n-String or message
     */
    private static String getRessourceString(final ResourceBundle res, final String key)
    {
        try
        {
            return res.getString(key.replace(' ', '_'));
        }
        catch (Exception e)
        {
            logMissing(key);
        }
        return null;
    }

    /**
     * Gets a String or Message from the loaded ResourceBundle Empty spaces are converted to '_'-characters
     *
     * @param key the message key
     * @return the i18n-String or message
     */
    private static String getSilentRessourceString(final ResourceBundle res, final String key)
    {
        return res.getString(key.replace(' ', '_'));
    }

    /**
     * Gets a char from the loaded ResourceBundle
     *
     * @param key the char-key
     * @return the i18n-char
     */
    private static char getRessourceChar(final ResourceBundle res, final String key)
    {
        try
        {
            final String str = res.getString(key.replace(' ', '_'));
            if ((str != null) && (str.length() > 0))
            {
                return str.charAt(0);
            }
        }
        catch (Exception e)
        {
            logMissing(key);
        }
        return MISSING_CHAR;
    }

    /**
     * Logs missing text entries to a log file
     *
     * @param key the key for which the resource was not found
     * @return whether the logging was successful
     */
    private static boolean logMissing(final String key)
    {
        LOGGER.error("Could not find resource string for " + key);
        if (getLogWriterFile() != null)
        {
            try
            {
                final PrintWriter logWriterForMissing = new PrintWriter(new FileOutputStream(logWriterFile, true), true);
                logWriterForMissing.println(key.replace(' ', '_') + "=" + key);
                logWriterForMissing.close();
                return true;
            }
            catch (Exception e)
            {
                LOGGER.error("Could not write log entry for missing text ");
            }
        }
        return false;
    }

    /**
     * Tries to initialize the {@link #logWriterFile} field.
     *
     * @return the file or null
     */
    private static File getLogWriterFile()
    {
        if (logWriterFile == null)
        {
            final String fileName = System.getProperty("jfactory.strings.logmissing", null);
            if (fileName != null)
            {
                logWriterFile = new File(fileName);
                try
                {
                    if (logWriterFile.exists())
                    {
                        logWriterFile.delete();
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("Could not delete LogWriterFile " + fileName);
                }
            }
        }
        return logWriterFile;
    }

    private static ResourceBundle getBundleForObject(final Object obj)
    {
        ResourceBundle res = (ResourceBundle) bundles.get(obj);
        if (res == null)
        {
            res = generalBundle;
            if (obj != null)
            {
                LOGGER.warn("No resource bundle for object: " + obj);
            }
        }
        return res;
    }
}
