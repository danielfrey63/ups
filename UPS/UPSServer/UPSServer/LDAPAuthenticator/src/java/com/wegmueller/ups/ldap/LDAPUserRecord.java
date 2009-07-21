package com.wegmueller.ups.ldap;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ESPWegmueller
 * Date: 29.07.2005
 * Time: 16:00:54
 * To change this template use File | Settings | File Templates.
 */
public class LDAPUserRecord implements ILDAPUserRecord {
    private Properties list = new Properties();
    private String userName;
    public static final String KEY_CARLICENSE = "carLicense";



    public LDAPUserRecord(String userName) {
        this.userName = userName;
    }

    public void addAttribute(String key,String value) {
        if (value!=null) list.put(key,value);
    }

    public Object getName() {
        return userName;
    }

    public String getStudentenNummer() {
        Object obj = list.get(KEY_CARLICENSE);
        if (obj==null) return null;
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.length()==8) {
                return s.substring(0,2)+"-"+s.substring(2,5)+"-"+s.substring(5);

            }

        }
        return null;
    }

    public Properties getAttributes() {
        return list;
    }
}
