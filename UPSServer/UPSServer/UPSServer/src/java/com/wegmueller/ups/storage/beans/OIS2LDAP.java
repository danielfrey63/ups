package com.wegmueller.ups.storage.beans;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  23:07:05
 */
public class OIS2LDAP {
    private String ldap;
    private String oisusername;
    private String oispassword;

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getOisusername() {
        return oisusername;
    }

    public void setOisusername(String oisusername) {
        this.oisusername = oisusername;
    }

    public String getOispassword() {
        return oispassword;
    }

    public void setOispassword(String oispassword) {
        this.oispassword = oispassword;
    }
}
