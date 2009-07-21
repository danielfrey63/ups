package com.wegmueller.ups.ldap.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;

/*
 * A custom socket factory used to override the default socket factory.
 * It prints out debugging information before using default Socket creation
 * methods. This class is required as we don't want to deal with
 * SSL-Certificate of LDAP-Servier's
 */
public class CustomSocketFactory extends SocketFactory {
    private static final Logger log = Logger.getLogger(CustomSocketFactory.class);
    private SSLContext sc;

    public CustomSocketFactory() {
        log.debug("[creating a custom socket factory]");
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            //LdapCtxFactory..setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            log.error("Error getting SSLContext.instance",e);
        }
    }

    public static SocketFactory getDefault() {

        log.debug("[acquiring the default socket factory]");
        return new CustomSocketFactory();
    }

    public Socket createSocket(String host, int port)
            throws IOException, UnknownHostException {
        log.debug("[creating a custom socket (method 1)]");
        return sc.getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(String host, int port, InetAddress localHost,
                               int localPort) throws IOException, UnknownHostException {

        log.debug("[creating a custom socket (method 2)]");
        return  sc.getSocketFactory().createSocket(host, port, localHost, localPort);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {

        log.debug("[creating a custom socket (method 3)]");
        return  sc.getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(InetAddress address, int port,
                               InetAddress localAddress, int localPort) throws IOException {

        log.debug("[creating a custom socket (method 4)]");
        return  sc.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }
}

