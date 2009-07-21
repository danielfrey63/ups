package com.wegmueller.ups.ldap;

import com.wegmueller.ups.ldap.util.CustomSocketFactory;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;

/**
 *
 * Module for Authenticating to ETH Zürich's LDAP-Servers
 *
 * Only possible Method is getUserData which has two possible outcomes:
 *
 * 1.) Everything was fine --> a <see>UserData</see>-Object is returned
 * 2.) There was an Error --> a <see>AuthException</see> is thrown
 *
 * NOTE: if the user can authenticate, but there is an error in retrieving
 * the User's Data from LDAP, a UserData-Object with no attributes is returned
 */
public class LDAPAuthenticate implements ILDAPAuth{

    private static final Logger log = Logger.getLogger(LDAPAuthenticate.class);
    public static String[] DEFAULT_HOSTS = {
        "ldaps01.ethz.ch",
        "ldaps02.ethz.ch",
        "ldaps03.ethz.ch"
    };
    public static String[] HOSTS = DEFAULT_HOSTS;
    private static final String CN = "cn=";
    private static String BASEDN = "ou=users,ou=nethz,ou=id,ou=auth,o=ethz,c=ch";
    private static final String SECURITY_PROTOCOL = "ssl";
    private static final String SECURITY_AUTHENTICATION = "simple";
    private static final String LDAP_PROCOTCOL = "ldaps://";
    public static final String LDAP_FACTORY_SOCKET_PROPERTY = "java.naming.ldap.factory.socket";
    public static final String UID_SEARCH_STRING = "uid=";
    public static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";


    public static void main(String[] args) throws LDAPAuthException {
        LDAPAuthenticate d = new LDAPAuthenticate();
        ILDAPUserRecord rec = d.getUserData("dfrey","leni1234");
        Map att = rec.getAttributes();
        for (Iterator it=att.keySet().iterator();it.hasNext();) {
            String key = (String) it.next();
            System.out.println(key+"="+att.get(key));
        }
    }

    /**
     * Return the user's data from one of the specified LDAP-Hosts
     * @param userName
     * @param password
     * @return
     * @throws LDAPAuthException
     */
    public ILDAPUserRecord getUserData(String userName, String password) throws LDAPAuthException {
        LDAPAuthException lastException = null;
        Throwable lastThrowable = null;
        log.debug("Trying to get userData for "+userName);
        for (int i = 0; i < HOSTS.length; i++) {
            try {
                return getUserData(HOSTS[i], userName, password);
            } catch (LDAPAuthException e) {
                if (log.isDebugEnabled()) log.debug("LDAPAuthenticate.getUserData(...) failed for "+HOSTS[i],e);
                if (e.getName().equals(LDAPAuthException.INVALID_CREDENTIALS)) throw e;
                lastException = e;
            } catch (Throwable e) {
                if (log.isDebugEnabled()) log.debug("LDAPAuthenticate.getUserData(...) failed for "+HOSTS[i],e);
                lastThrowable = e;
            }
        }
        log.error("No host can answer the question");
        if (lastException!=null) throw lastException;
        if (lastThrowable!=null) throw new LDAPAuthException(lastThrowable);
        throw new LDAPAuthException("Unknown reason Exception");
    }

    private static LDAPUserRecord getUserData(String host, String userName, String password) throws LDAPAuthException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(LDAP_FACTORY_SOCKET_PROPERTY, CustomSocketFactory.class.getName());
        env.put(Context.PROVIDER_URL, LDAP_PROCOTCOL + host);
        env.put(Context.SECURITY_PROTOCOL, SECURITY_PROTOCOL);
        env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, buildPrincipal(userName));
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext dirctx = null;
        try {
            dirctx = new InitialDirContext(env);
            if (log.isDebugEnabled()) log.debug("LDAPAuthenticate.getUserData(...) seems to have worked for "+userName);
            LDAPUserRecord list = new LDAPUserRecord(userName);
            try {
                NamingEnumeration m = dirctx.search(BASEDN, UID_SEARCH_STRING + userName, null);
                while (m.hasMore()) {
                    SearchResult res = (SearchResult) m.next();
                    Attributes attr = res.getAttributes();
                    NamingEnumeration en = attr.getAll();
                    while (en.hasMore()) {
                        Attribute att = (Attribute) en.next();
                        if (att==null) {
                            log.warn("Ignoring null Attribute for User"+userName);
                        } else {
                            for (int i=0;i<att.size();i++) {
                                list.addAttribute(att.getID(), att.get(i).toString());

                            }
                        }
                    }
                }
            } catch (Throwable e) {
                log.error("Authentication was successful, but an error occured retrieving the data of the user: "+userName);
            }
            return list;
        } catch (javax.naming.AuthenticationException e) {
            throw new LDAPAuthException(e);
        } catch (NamingException e) {
            throw new LDAPAuthException(e);
        } finally {
            if (dirctx != null) {
                try {
                    dirctx.close();
                } catch (NamingException e) {
                }
            }
        }
    }

    private static Object buildPrincipal(String userName) {
        return CN + userName + "," + BASEDN;
    }



}