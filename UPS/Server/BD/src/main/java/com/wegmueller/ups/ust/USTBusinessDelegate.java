package com.wegmueller.ups.ust;

import ch.xmatrix.ups.pdf.PDFGenerator;
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Todo: Make sure a common password is used instead of the owners one! Created by: Thomas Wegmueller Date: 26.09.2005,
 * 19:39:30
 */
public class USTBusinessDelegate implements IUSTBusinessDelegate
{

    private static final Logger LOG = Logger.getLogger(USTBusinessDelegate.class);

    /**
     * A Call to PDF-Production
     *
     * @param userName
     * @param password
     * @param list
     * @param data
     * @return
     * @throws USTException
     */
    public byte[] producePDF(final String userName, final String password, final ILDAPUserRecord list, final byte[] data) throws USTException
    {
        try
        {
            if (data == null)
            {
                throw new IllegalArgumentException("data may not be null");
            }
            if (data.length == 0)
            {
                throw new IllegalArgumentException("data may not be empty");
            }
            if (list == null)
            {
                throw new IllegalArgumentException("ldap user recored may not be null");
            }
            if (list.getStudentenNummer() == null)
            {
                throw new IllegalArgumentException("student number may not be null");
            }
            if (userName == null)
            {
                throw new IllegalArgumentException("user name may not be null");
            }
            if ("".equals(userName))
            {
                throw new IllegalArgumentException("user name may not be empty");
            }
            if (password == null)
            {
                throw new IllegalArgumentException("password may not be null");
            }
            final Map attributes = list.getAttributes();
            final Properties context = new Properties();
            context.put(PDFGenerator.KEY_FIRSTNAME, getLdapAttribute(attributes, ILDAPUserRecord.KEY_FIRSTNAME));
            context.put(PDFGenerator.KEY_FAMILYNAME, getLdapAttribute(attributes, ILDAPUserRecord.KEY_FAMILYNAME));
            if (list.getStudentenNummer() != null)
            {
                context.put(PDFGenerator.KEY_ID, list.getStudentenNummer());
            }
            else
            {
                context.put(PDFGenerator.KEY_ID, "<Studenten-Nummer fehlt");
            }
            context.put(PDFGenerator.KEY_USERNAME, userName);
            context.put(PDFGenerator.KEY_PASSWORD, password);
            context.put(PDFGenerator.KEY_SUBJECT, "Bestätigung für das Einreichen der Prüfungsliste");
            context.put(PDFGenerator.KEY_TITLE, "Prüfungslisten-Bestätigung");
            context.put(PDFGenerator.KEY_AUTHOR, "UPS WebService");
            context.put(PDFGenerator.KEY_SPECIES, data);
            final Object attribute = getLdapAttribute(attributes, ILDAPUserRecord.KEY_DEPARTMENT);
            if (attribute != null)
            {
                context.put(PDFGenerator.KEY_DEPARTMENT, attribute);
            }

            final byte[] pdf = PDFGenerator.createPdf(context);
            if (pdf == null || pdf.length == 0)
            {
                throw new NullPointerException("pdf generated is empty or null");
            }
            return pdf;
        }
        catch (IllegalArgumentException e)
        {
            LOG.error("Error during check of ldap data: " + e.getMessage());
            throw new USTException(USTException.MISSING_DATA + ": " + e.getMessage());
        }
        catch (Throwable e)
        {
            LOG.error("Error producing pdf for " + userName, e);
            e.printStackTrace();
            throw new USTException(USTException.SERVER_ERROR + ": PDF: " + e.getMessage());
        }
    }

    private static Object getLdapAttribute(final Map attributes, final String key)
    {
        final Object value = attributes.get(key);
        if (value == null)
        {
            LOG.warn("value for attribute \"" + key + "\" is null");
        }
        return value;
    }
}
