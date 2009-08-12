package com.wegmueller.ups.ldap;

import com.wegmueller.ups.ldap.util.CustomSocketFactory;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.log4j.Logger;

/**
 * Module for Authenticating to ETH Zürich's LDAP-Servers
 *
 * Only possible Method is getUserData which has two possible outcomes:
 *
 * 1.) Everything was fine --> a <see>UserData</see>-Object is returned 2.) There was an Error --> a
 * <see>AuthException</see> is thrown
 *
 * NOTE: if the user can authenticate, but there is an error in retrieving the User's Data from LDAP, a UserData-Object
 * with no attributes is returned
 */
public class LDAPAuthenticate implements ILDAPAuth {

    private static final Logger log = Logger.getLogger(LDAPAuthenticate.class);

    public static String[] DEFAULT_HOSTS = {
            "ldaps01.ethz.ch",
            "ldaps02.ethz.ch",
            "ldaps03.ethz.ch"
    };

    public static String[] HOSTS = DEFAULT_HOSTS;

    private static final String BASEDN = "ou=users,ou=nethz,ou=id,ou=auth,o=ethz,c=ch";
    private static final String BINDDN = "cn=upsserver4_proxy,ou=admins,ou=nethz,ou=id,ou=auth,o=ethz,c=ch";
    private static final String PASS = "d178.ftx.3xf.w2e";
    private static final String SECURITY_PROTOCOL = "ssl";
    private static final String SECURITY_AUTHENTICATION = "simple";
    private static final String LDAP_PROCOTCOL = "ldaps://";

    public static final String LDAP_FACTORY_SOCKET_PROPERTY = "java.naming.ldap.factory.socket";
    public static final String UID_SEARCH_STRING = "uid=";
    public static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private static final SearchControls SEARCH_CONTROLS = new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, null, false, false);

    public static void main(final String[] args) throws LDAPAuthException {
        final LDAPAuthenticate d = new LDAPAuthenticate();
        final ILDAPUserRecord rec = d.getUserData("dfrey", "leni1234");
        final Map att = rec.getAttributes();
        for (final Object o : att.keySet()) {
            final String key = (String) o;
            System.out.println(key + "=" + att.get(key));
        }
    }

    /**
     * Return the user's data from one of the specified LDAP-Hosts
     *
     * @throws LDAPAuthException
     */
    public ILDAPUserRecord getUserData(final String userName, final String password) throws LDAPAuthException {
        LDAPAuthException lastException = null;
        Throwable lastThrowable = null;
        if (log.isDebugEnabled()) log.debug("Trying to get userData for " + userName);
        for (final String host : HOSTS) {
            try {
                return getUserDetails(host, userName);
            } catch (LDAPAuthException e) {
                if (log.isDebugEnabled()) log.debug("LDAPAuthenticate.getUserDetails(...) failed for " + host, e);
                if (e.getName().equals(LDAPAuthException.INVALID_CREDENTIALS)) throw e;
                lastException = e;
            } catch (Throwable e) {
                if (log.isDebugEnabled()) log.debug("LDAPAuthenticate.getUserDetails(...) failed for " + host, e);
                lastThrowable = e;
            }
        }
        log.error("No host can answer the question");
        if (lastException != null) throw lastException;
        if (lastThrowable != null) throw new LDAPAuthException(lastThrowable);
        throw new LDAPAuthException("Unknown reason Exception");
    }

    public static LDAPUserRecord getUserDetails(final String host, final String userName) throws LDAPAuthException {
        if (log.isDebugEnabled()) log.debug("trying LDAP host " + host + " for " + userName);
        DirContext dirctx = null;
        try {
            dirctx = getContext(host);
            if (log.isDebugEnabled()) log.debug("context successfully created");
            final LDAPUserRecord list = new LDAPUserRecord(userName);
            try {
                final String filter = UID_SEARCH_STRING + userName;
                if (log.isDebugEnabled()) log.debug("search with base: \"" + BASEDN + "\", filter: \"" + filter + "\", controls: \"" + SEARCH_CONTROLS + "\"");
                final NamingEnumeration<SearchResult> m = dirctx.search(BASEDN, filter, SEARCH_CONTROLS);
                while (m.hasMore()) {
                    final SearchResult res = m.next();
                    if (log.isDebugEnabled()) log.debug("search result: \"" + res + "\"");
                    final NamingEnumeration<? extends Attribute> en = res.getAttributes().getAll();
                    while (en.hasMore()) {
                        final Attribute att = en.next();
                        if (att != null) {
                            for (int i = 0; i < att.size(); i++) {
                                list.addAttribute(att.getID(), att.get(i).toString());
                            }
                        }
                    }
                }
                if (log.isDebugEnabled()) log.debug("search finished");
            } catch (Throwable e) {
                log.error("Authentication was successful, but an error occured retrieving the data of the user: " + userName);
            }
            return list;
        } catch (AuthenticationException e) {
            throw new LDAPAuthException(e);
        } catch (NamingException e) {
            throw new LDAPAuthException(e);
        } finally {
            if (dirctx != null) {
                try {
                    dirctx.close();
                } catch (NamingException e) {
                    log.warn("could not close context");
                }
            }
        }
    }

    private static InitialDirContext getContext(final String host) throws NamingException {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(LDAP_FACTORY_SOCKET_PROPERTY, CustomSocketFactory.class.getName());
        env.put(Context.PROVIDER_URL, LDAP_PROCOTCOL + host);
        env.put(Context.SECURITY_PROTOCOL, SECURITY_PROTOCOL);
        env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, BINDDN);
        env.put(Context.SECURITY_CREDENTIALS, PASS);
        return new InitialDirContext(env);
    }
}