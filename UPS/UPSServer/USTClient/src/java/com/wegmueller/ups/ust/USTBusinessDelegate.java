package com.wegmueller.ups.ust;

import ch.xmatrix.ups.document.pdf.PDFGenerator;
import com.wegmueller.ups.IUPSServerService;
import com.wegmueller.ups.ldap.ILDAPUserRecord;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  19:39:30
 */
public class USTBusinessDelegate implements IUSTBusinessDelegate {

    private static final Logger log = Logger.getLogger(USTBusinessDelegate.class);

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
    public byte[] producePDF(String userName, String password, ILDAPUserRecord list, byte[] data) throws USTException {
        try {
            if (data == null) {
                throw new IllegalArgumentException("data may not be null");
            }
            if (data.length == 0) {
                throw new IllegalArgumentException("data may not be empty");
            }
            if (list == null) {
                throw new IllegalArgumentException("ldap user recored may not be null");
            }
            if (userName == null) {
                throw new IllegalArgumentException("user name may not be null");
            }
            if ("".equals(userName)) {
                throw new IllegalArgumentException("user name may not be empty");
            }
            if (password == null) {
                throw new IllegalArgumentException("password may not be null");
            }
            Map attributes = list.getAttributes();
            final Properties context = new Properties();
            context.put(PDFGenerator.KEY_FIRSTNAME, getLdapAttribute(attributes, ILDAPUserRecord.KEY_FIRSTNAME));
            context.put(PDFGenerator.KEY_FAMILYNAME, getLdapAttribute(attributes, ILDAPUserRecord.KEY_FAMILYNAME));
            context.put(PDFGenerator.KEY_ID, list.getStudentenNummer());
            context.put(PDFGenerator.KEY_USERNAME, userName);
            context.put(PDFGenerator.KEY_PASSWORD, password);
            context.put(PDFGenerator.KEY_SUBJECT, "Bestätigung für das Einreichen der Prüfungsliste");
            context.put(PDFGenerator.KEY_TITLE, "Prüfungslisten-Bestätigung");
            context.put(PDFGenerator.KEY_AUTHOR, "UPS WebService");
            context.put(PDFGenerator.KEY_SPECIES, data);

            final byte[] pdf = PDFGenerator.createPdf(context);
            if (pdf == null || pdf.length == 0) {
                throw new NullPointerException("pdf generated is empty or null");
            }
            return pdf;
        }
        catch (IllegalArgumentException e) {
            log.error("Error during check of ldap data: " + e.getMessage());
            throw new USTException(USTException.MISSING_DATA + ": " + e.getMessage());
        }
        catch (Throwable e) {
            log.error("Error producing pdf for " + userName, e);
            throw new USTException(USTException.SERVER_ERROR + ": PDF: " + e.getMessage());
        }

    }


    private static Object getLdapAttribute(final Map attributes, final String key) {
        final Object value = attributes.get(key);
        if (value == null) {
            throw new IllegalArgumentException("value for attribute \"" + key + "\" may not be null");
        }
        return value;
    }

}
