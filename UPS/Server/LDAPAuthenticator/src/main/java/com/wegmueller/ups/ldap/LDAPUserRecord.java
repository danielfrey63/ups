package com.wegmueller.ups.ldap;

import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/** Author: Thomas Wegmüller Author: Daniel Frey */
public class LDAPUserRecord implements ILDAPUserRecord
{

    private static final Logger LOG = Logger.getLogger(LDAPUserRecord.class);

    private Properties list = new Properties();

    private String userName;

    public LDAPUserRecord(final String userName)
    {
        this.userName = userName;
    }

    public void addAttribute(final String key, final String value)
    {
        if (value != null)
        {
            list.put(key, value);
        }
    }

    public Object getName()
    {
        return userName;
    }

    public String getStudentenNummer()
    {
        final Object obj = list.get(KEY_CARLICENSE);
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof String)
        {
            final String s = (String) obj;
            if (s.length() <= 8)
            {
                // Student number usually is 8 digist or less, but then the string has be prepended with zeros as prefix.
                final String p = StringUtils.leftPad(s, 8, '0');
                return p.substring(0, 2) + "-" + p.substring(2, 5) + "-" + p.substring(5);
            }
            else
            {
                // my carLicense is entered by hand for test purposes and often hasn't the correct length, so accept it
                // anyway.
                LOG.warn("KEY_CARLICENSE longer than 8 characters, no formatting is applied");
                return s;
            }
        }
        else
        {
            LOG.warn("KEY_CARLICENSE not of type String");
        }
        return null;
    }

    public String getDepartment()
    {
        final Object obj = list.get(KEY_DEPARTMENT);
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof String)
        {
            return (String) obj;
        }
        return null;
    }

    public Properties getAttributes()
    {
        return list;
    }

    public String toString()
    {
        final StringBuffer b = new StringBuffer();
        b.append("userName: ").append(userName).append(", ");
        b.append("KEY_USERNAME: ").append(list.getProperty(KEY_USERNAME)).append(", ");
        b.append("KEY_FIRSTNAME: ").append(list.getProperty(KEY_FIRSTNAME)).append(", ");
        b.append("KEY_FAMILYNAME: ").append(list.getProperty(KEY_FAMILYNAME)).append(", ");
        b.append("KEY_CARLICENSE: ").append(list.getProperty(KEY_CARLICENSE)).append(", ");
        b.append("KEY_DEPARTMENT: ").append(list.getProperty(KEY_DEPARTMENT));
        return b.toString();
    }
}
