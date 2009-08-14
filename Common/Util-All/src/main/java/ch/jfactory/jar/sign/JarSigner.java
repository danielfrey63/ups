package ch.jfactory.jar.sign;/*
 * @(#)JarSigner.java	1.64 04/04/21
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.Identity;
import java.security.IdentityScope;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Timestamp;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;
import sun.security.util.ManifestDigester;
import sun.security.util.Password;
import sun.security.util.SignatureFileVerifier;

/**
 * <p>The jarsigner utility.
 *
 * @author Roland Schemers
 * @author Jan Luehe
 * @version 1.64 04/21/04
 */

public class JarSigner
{

    // for i18n
    private static final ResourceBundle RB = ResourceBundle.getBundle("ch.jfactory.jar.sign.JarSignerResources");

    private static final Collator COLLATOR = Collator.getInstance();

    private static final Logger LOG = Logger.getLogger(JarSigner.class);

    static
    {
        // this is for case insensitive string comparisions
        COLLATOR.setStrength(Collator.PRIMARY);
    }

    private static final String META_INF = "META-INF/";

    // prefix for new signature-related files in META-INF directory
    private static final String SIG_PREFIX = META_INF + "SIG-";

    private static final Class[] PARAM_STRING = {String.class};

    private static final String NONE = "NONE";

    private static final String P11KEYSTORE = "PKCS11";

    private static final long SIX_MONTHS = 180 * 24 * 60 * 60 * 1000L; //milliseconds

    public static void main(final String[] args) throws Exception
    {
        final JarSigner js = new JarSigner();
        js.run(args);
    }

    static final String VERSION = "1.0";

    static final int IN_KEYSTORE = 0x01;

    static final int IN_SCOPE = 0x02;

    // signer's certificate chain (when composing)
    X509Certificate[] certChain;

    /*
     * private key
     */
    PrivateKey privateKey;

    KeyStore store;

    IdentityScope scope;

    String keystore; // key store file

    boolean nullStream = false; // null keystore input stream (NONE)

    boolean token = false; // token-based keystore

    String jarfile;  // jar file to sign

    String alias;    // alias to sign jar with

    char[] storepass; // keystore password

    boolean protectedPath; // protected authentication path

    String storetype; // keystore type

    String providerName; // provider name

    Vector providers = null; // list of providers

    HashMap providerArgs = new HashMap(); // arguments for provider constructors

    char[] keypass; // private key password

    String sigfile; // name of .SF file

    String signedjar; // output filename

    String tsaUrl; // location of the Timestamping Authority

    String tsaAlias; // alias for the Timestamping Authority's certificate

    boolean verify = false; // verify the jar

    boolean verbose = false; // verbose output when signing/verifying

    boolean showcerts = false; // show certs when verifying

    boolean debug = false; // debug

    boolean signManifest = true; // "sign" the whole manifest

    boolean externalSF = true; // leave the .SF out of the PKCS7 block

    // read zip entry raw bytes
    private ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

    private byte[] buffer = new byte[8192];

    private ContentSigner signingMechanism = null;

    private String altSignerClass = null;

    private String altSignerClasspath = null;

    private ZipFile zipFile = null;

    private boolean hasExpiredCert = false;

    private boolean hasExpiringCert = false;

    private boolean notYetValidCert = false;

    public void run(final String[] args) throws Exception
    {
        try
        {
            parseArgs(args);

            // Try to load and install the specified providers
            if (providers != null)
            {
                final ClassLoader cl = ClassLoader.getSystemClassLoader();
                final Enumeration e = providers.elements();
                while (e.hasMoreElements())
                {
                    final String provName = (String) e.nextElement();
                    final Class provClass;
                    if (cl != null)
                    {
                        provClass = cl.loadClass(provName);
                    }
                    else
                    {
                        provClass = Class.forName(provName);
                    }

                    final String provArg = (String) providerArgs.get(provName);
                    final Object obj;
                    if (provArg == null)
                    {
                        obj = provClass.newInstance();
                    }
                    else
                    {
                        final Constructor c = provClass.getConstructor(PARAM_STRING);
                        obj = c.newInstance(provArg);
                    }

                    if (!(obj instanceof Provider))
                    {
                        final MessageFormat form = new MessageFormat(RB.getString("provName not a provider"));
                        final Object[] source = {provName};
                        throw new Exception(form.format(source));
                    }
                    Security.addProvider((Provider) obj);
                }
            }

            hasExpiredCert = false;
            hasExpiringCert = false;
            notYetValidCert = false;

            if (verify)
            {
                loadKeyStore(keystore, false);
                scope = IdentityScope.getSystemScope();
                verifyJar(jarfile);
            }
            else
            {
                loadKeyStore(keystore, true);
                getAliasInfo(alias);

                // load the alternative signing mechanism
                if (altSignerClass != null)
                {
                    signingMechanism = loadSigningMechanism(altSignerClass, altSignerClasspath);
                }
                signJar(jarfile, alias, args);
            }
        }
        catch (Exception e)
        {
            LOG.error(RB.getString("jarsigner error: "), e);
            throw e;
        }
        finally
        {
            // zero-out private key password
            if (keypass != null)
            {
                Arrays.fill(keypass, ' ');
                keypass = null;
            }
            // zero-out keystore password
            if (storepass != null)
            {
                Arrays.fill(storepass, ' ');
                storepass = null;
            }
        }
    }

    /*
     * Parse command line arguments.
     */
    void parseArgs(final String[] args)
    {
        /* parse flags */
        int n = 0;

        for (n = 0; (n < args.length) && args[n].startsWith("-"); n++)
        {

            String flags = args[n];

            if (COLLATOR.compare(flags, "-keystore") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                keystore = args[n];
            }
            else if (COLLATOR.compare(flags, "-storepass") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                storepass = args[n].toCharArray();
            }
            else if (COLLATOR.compare(flags, "-storetype") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                storetype = args[n];
            }
            else if (COLLATOR.compare(flags, "-providerName") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                providerName = args[n];
            }
            else if ((COLLATOR.compare(flags, "-provider") == 0) ||
                    (COLLATOR.compare(flags, "-providerClass") == 0))
            {
                if (++n == args.length)
                {
                    usage();
                }
                if (providers == null)
                {
                    providers = new Vector(3);
                }
                providers.add(args[n]);

                if (args.length > (n + 1))
                {
                    flags = args[n + 1];
                    if (COLLATOR.compare(flags, "-providerArg") == 0)
                    {
                        if (args.length == (n + 2))
                        {
                            usage();
                        }
                        providerArgs.put(args[n], args[n + 2]);
                        n += 2;
                    }
                }
            }
            else if (COLLATOR.compare(flags, "-protected") == 0)
            {
                protectedPath = true;
            }
            else if (COLLATOR.compare(flags, "-debug") == 0)
            {
                debug = true;
            }
            else if (COLLATOR.compare(flags, "-keypass") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                keypass = args[n].toCharArray();
            }
            else if (COLLATOR.compare(flags, "-sigfile") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                sigfile = args[n];
            }
            else if (COLLATOR.compare(flags, "-signedjar") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                signedjar = args[n];
            }
            else if (COLLATOR.compare(flags, "-tsa") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                tsaUrl = args[n];
            }
            else if (COLLATOR.compare(flags, "-tsacert") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                tsaAlias = args[n];
            }
            else if (COLLATOR.compare(flags, "-altsigner") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                altSignerClass = args[n];
            }
            else if (COLLATOR.compare(flags, "-altsignerpath") == 0)
            {
                if (++n == args.length)
                {
                    usage();
                }
                altSignerClasspath = args[n];
            }
            else if (COLLATOR.compare(flags, "-sectionsonly") == 0)
            {
                signManifest = false;
            }
            else if (COLLATOR.compare(flags, "-internalsf") == 0)
            {
                externalSF = false;
            }
            else if (COLLATOR.compare(flags, "-verify") == 0)
            {
                verify = true;
            }
            else if (COLLATOR.compare(flags, "-verbose") == 0)
            {
                verbose = true;
            }
            else if (COLLATOR.compare(flags, "-certs") == 0)
            {
                showcerts = true;
            }
            else if (COLLATOR.compare(flags, "-h") == 0 ||
                    COLLATOR.compare(flags, "-help") == 0)
            {
                usage();
            }
            else
            {
                System.err.println(RB.getString("Illegal option: ") + flags);
                usage();
            }
        }

        if (n == args.length)
        {
            usage();
        }
        jarfile = args[n++];

        if (!verify)
        {
            if (n == args.length)
            {
                usage();
            }
            alias = args[n++];
        }

        if (NONE.equals(keystore))
        {
            nullStream = true;
        }
        if (storetype == null)
        {
            storetype = KeyStore.getDefaultType();
        }
        if (P11KEYSTORE.equalsIgnoreCase(storetype))
        {
            token = true;
        }

        if (token && !nullStream)
        {
            System.err.println(RB.getString("-keystore must be NONE if -storetype is " + P11KEYSTORE));
            System.err.println();
            usage();
        }

        if (token && keypass != null)
        {
            System.err.println(RB.getString("-keypass can not be specified if -storetype is " + P11KEYSTORE));
            System.err.println();
            usage();
        }

        if (protectedPath)
        {
            if (storepass != null || keypass != null)
            {
                System.err.println(RB.getString("If -protected is specified, then -storepass and -keypass must not be specified"));
                System.err.println();
                usage();
            }
        }
    }

    void usage()
    {
        System.out.println(RB.getString("Usage: jarsigner [options] jar-file alias"));
        System.out.println(RB.getString("       jarsigner -verify [options] jar-file"));
        System.out.println();
        System.out.println(RB.getString("[-keystore <url>]           keystore location"));
        System.out.println();
        System.out.println(RB.getString("[-storepass <password>]     password for keystore integrity"));
        System.out.println();
        System.out.println(RB.getString("[-storetype <type>]         keystore type"));
        System.out.println();
        System.out.println(RB.getString("[-keypass <password>]       password for private key (if different)"));
        System.out.println();
        System.out.println(RB.getString("[-sigfile <file>]           name of .SF/.DSA file"));
        System.out.println();
        System.out.println(RB.getString("[-signedjar <file>]         name of signed JAR file"));
        System.out.println();
        System.out.println(RB.getString("[-verify]                   verify a signed JAR file"));
        System.out.println();
        System.out.println(RB.getString("[-verbose]                  verbose output when signing/verifying"));
        System.out.println();
        System.out.println(RB.getString("[-certs]                    display certificates when verbose and verifying"));
        System.out.println();
        System.out.println(RB.getString("[-tsa <url>]                location of the Timestamping Authority"));
        System.out.println();
        System.out.println(RB.getString("[-tsacert <alias>]          public key certificate for Timestamping Authority"));
        System.out.println();
        System.out.println(RB.getString("[-altsigner <class>]        class name of an alternative signing mechanism"));
        System.out.println();
        System.out.println(RB.getString("[-altsignerpath <pathlist>] location of an alternative signing mechanism"));
        System.out.println();
        System.out.println(RB.getString("[-internalsf]               include the .SF file inside the signature block"));
        System.out.println();
        System.out.println(RB.getString("[-sectionsonly]             don't compute hash of entire manifest"));
        System.out.println();
        System.out.println(RB.getString("[-protected]                keystore has protected authentication path"));
        System.out.println();
        System.out.println(RB.getString("[-providerName <name>]      provider name"));
        System.out.println();
        System.out.println(RB.getString("[-providerClass <class>     name of cryptographic service provider's"));
        System.out.println(RB.getString("  [-providerArg <arg>]] ... master class file and constructor argument"));
        System.out.println();
    }

    void verifyJar(final String jarName) throws Exception
    {
        boolean anySigned = false;
        boolean hasUnsignedEntry = false;
        try
        {

            final JarFile jf = new JarFile(jarName, true);

            final Vector entriesVec = new Vector();
            final byte[] buffer = new byte[8192];

            final Enumeration entries = jf.entries();
            while (entries.hasMoreElements())
            {
                final JarEntry je = (JarEntry) entries.nextElement();
                entriesVec.addElement(je);
                final InputStream is = jf.getInputStream(je);
                while ((is.read(buffer, 0, buffer.length)) != -1)
                {
                    // we just read. this will throw a SecurityException
                    // if  a signature/digest check fails.
                }
                is.close();
            }

            final Manifest man = jf.getManifest();
            // Don't use "jf" after it's been closed!
            jf.close();

            if (man != null)
            {
                if (verbose)
                {
                    System.out.println();
                }
                final Enumeration e = entriesVec.elements();

                final long now = System.currentTimeMillis();

                while (e.hasMoreElements())
                {
                    final JarEntry je = (JarEntry) e.nextElement();
                    final String name = je.getName();
                    final CodeSigner[] signers = je.getCodeSigners();
                    final boolean isSigned = (signers != null);
                    anySigned |= isSigned;
                    hasUnsignedEntry |= !je.isDirectory() && !isSigned
                            && !signatureRelated(name);

                    if (verbose)
                    {
                        final int inStoreOrScope = inKeyStore(signers);
                        final boolean inStore = (inStoreOrScope & IN_KEYSTORE) != 0;
                        final boolean inScope = (inStoreOrScope & IN_SCOPE) != 0;
                        final boolean inManifest =
                                ((man.getAttributes(name) != null) ||
                                        (man.getAttributes("./" + name) != null) ||
                                        (man.getAttributes("/" + name) != null));
                        System.out.print(
                                (isSigned ? RB.getString("s") : RB.getString(" ")) +
                                        (inManifest ? RB.getString("m") : RB.getString(" ")) +
                                        (inStore ? RB.getString("k") : RB.getString(" ")) +
                                        (inScope ? RB.getString("i") : RB.getString(" ")) +
                                        RB.getString("  "));
                        final StringBuffer sb = new StringBuffer();
                        final String s = Long.toString(je.getSize());
                        for (int i = 6 - s.length(); i > 0; --i)
                        {
                            sb.append(' ');
                        }
                        sb.append(s).append(' ').append(new Date(je.getTime()).toString());
                        sb.append(' ').append(je.getName());
                        System.out.println(sb.toString());

                        if (signers != null && showcerts)
                        {
                            final String tab = RB.getString("      ");
                            for (CodeSigner signer : signers)
                            {
                                System.out.println();
                                final List<Certificate> certs = (List<Certificate>) signer.getSignerCertPath().getCertificates();
                                // display the signature timestamp, if present
                                final Timestamp timestamp = signer.getTimestamp();
                                if (timestamp != null)
                                {
                                    System.out.println(printTimestamp(tab, timestamp));
                                }
                                // display the certificate(s)
                                for (final Certificate c : certs)
                                {
                                    System.out.println(printCert(tab, c, true, now));
                                }
                            }
                            System.out.println();
                        }

                    }
                    if (isSigned && !showcerts)
                    {
                        for (CodeSigner signer : signers)
                        {
                            final Certificate cert = signer.getSignerCertPath().getCertificates().get(0);
                            if (cert instanceof X509Certificate)
                            {
                                final long notAfter = ((X509Certificate) cert).getNotAfter().getTime();

                                if (notAfter < now)
                                {
                                    hasExpiredCert = true;
                                }
                                else if (notAfter < now + SIX_MONTHS)
                                {
                                    hasExpiringCert = true;
                                }
                            }
                        }
                    }
                }
            }
            if (verbose)
            {
                System.out.println();
                System.out.println(RB.getString("  s = signature was verified "));
                System.out.println(RB.getString("  m = entry is listed in manifest"));
                System.out.println(RB.getString("  k = at least one certificate was found in keystore"));
                System.out.println(RB.getString("  i = at least one certificate was found in identity scope"));
                System.out.println();
            }

            if (man == null)
            {
                System.out.println(RB.getString("no manifest."));
            }

            if (!anySigned)
            {
                System.out.println(RB.getString("jar is unsigned. (signatures missing or not parsable)"));
            }
            else
            {
                System.out.println(RB.getString("jar verified."));
                if (hasUnsignedEntry || hasExpiredCert || hasExpiringCert || notYetValidCert)
                {

                    System.out.println();
                    System.out.print(RB.getString("Warning: "));
                    if (hasUnsignedEntry)
                    {
                        System.out.print(RB.getString("This jar contains unsigned entries which have not been integrity-checked. "));
                        if (!verbose)
                        {
                            System.out.println(RB.getString("Re-run with the -verbose option for more details."));
                        }
                        else
                        {
                            System.out.println();
                        }
                    }
                    if (hasExpiredCert)
                    {
                        System.out.print(RB.getString("This jar contains entries whose signer certificate has expired. "));
                        if (!(verbose && showcerts))
                        {
                            System.out.println(RB.getString("Re-run with the -verbose and -certs options for more details."));
                        }
                        else
                        {
                            System.out.println();
                        }
                    }
                    if (hasExpiringCert)
                    {
                        System.out.print(RB.getString("This jar contains entries whose signer certificate will expire within six months. "));
                        if (!(verbose && showcerts))
                        {
                            System.out.println(RB.getString("Re-run with the -verbose and -certs options for more details."));
                        }
                        else
                        {
                            System.out.println();
                        }
                    }
                    if (notYetValidCert)
                    {
                        System.out.print(RB.getString("This jar contains entries whose signer certificate is not yet valid. "));
                        if (!(verbose && showcerts))
                        {
                            System.out.println(RB.getString("Re-run with the -verbose and -certs options for more details."));
                        }
                        else
                        {
                            System.out.println();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(RB.getString("jarsigner: "), e);
        }
    }

    /*
     * Display some details about a certificate:
     *
     * <cert-type> [", " <subject-DN>] [" (" <keystore-entry-alias> ")"]
     */
    String printCert(final Certificate c)
    {
        return printCert("", c, false, 0);
    }

    private static MessageFormat validityTimeForm = null;

    private static MessageFormat notYetTimeForm = null;

    private static MessageFormat expiredTimeForm = null;

    private static MessageFormat expiringTimeForm = null;

    /*
     * Display some details about a certificate:
     *
     * [<tab>] <cert-type> [", " <subject-DN>] [" (" <keystore-entry-alias> ")"]
     * [<validity-period> | <expiry-warning>]
     */
    String printCert(final String tab, final Certificate c, final boolean checkValidityPeriod, long now)
    {

        final StringBuilder certStr = new StringBuilder();
        final String space = RB.getString(" ");
        X509Certificate x509Cert = null;

        if (c instanceof X509Certificate)
        {
            x509Cert = (X509Certificate) c;
            certStr.append(tab).append(x509Cert.getType()).append(RB.getString(", ")).append(x509Cert.getSubjectDN().getName());
        }
        else
        {
            certStr.append(tab).append(c.getType());
        }

        final String alias = (String) storeHash.get(c);
        if (alias != null)
        {
            certStr.append(space).append(alias);
        }

        if (checkValidityPeriod && x509Cert != null)
        {

            certStr.append("\n").append(tab).append("[");
            final Date notAfter = x509Cert.getNotAfter();
            try
            {
                x509Cert.checkValidity();
                // test if cert will expire within six months
                if (now == 0)
                {
                    now = System.currentTimeMillis();
                }
                if (notAfter.getTime() < now + SIX_MONTHS)
                {
                    hasExpiringCert = true;

                    if (expiringTimeForm == null)
                    {
                        expiringTimeForm = new MessageFormat(RB.getString("certificate will expire on"));
                    }
                    final Object[] source = {notAfter};
                    certStr.append(expiringTimeForm.format(source));

                }
                else
                {
                    if (validityTimeForm == null)
                    {
                        validityTimeForm = new MessageFormat(RB.getString("certificate is valid from"));
                    }
                    final Object[] source = {x509Cert.getNotBefore(), notAfter};
                    certStr.append(validityTimeForm.format(source));
                }
            }
            catch (CertificateExpiredException cee)
            {
                hasExpiredCert = true;

                if (expiredTimeForm == null)
                {
                    expiredTimeForm = new MessageFormat(
                            RB.getString("certificate expired on"));
                }
                final Object[] source = {notAfter};
                certStr.append(expiredTimeForm.format(source));

            }
            catch (CertificateNotYetValidException cnyve)
            {
                notYetValidCert = true;

                if (notYetTimeForm == null)
                {
                    notYetTimeForm = new MessageFormat(
                            RB.getString("certificate is not valid until"));
                }
                final Object[] source = {x509Cert.getNotBefore()};
                certStr.append(notYetTimeForm.format(source));
            }
            certStr.append("]");
        }
        return certStr.toString();
    }

    private static MessageFormat signTimeForm = null;

    private String printTimestamp(final String tab, final Timestamp timestamp)
    {

        if (signTimeForm == null)
        {
            signTimeForm =
                    new MessageFormat(RB.getString("entry was signed on"));
        }
        final Object[] source = {timestamp.getTimestamp()};

        return new StringBuilder().append(tab).append("[")
                .append(signTimeForm.format(source)).append("]").toString();
    }

    Hashtable storeHash = new Hashtable();

    int inKeyStore(final CodeSigner[] signers)
    {
        int result = 0;

        if (signers == null)
        {
            return 0;
        }

        boolean found = false;

        for (CodeSigner signer : signers)
        {
            found = false;
            final List<Certificate> certs = (List<Certificate>) signer.getSignerCertPath().getCertificates();

            for (final Certificate c : certs)
            {
                String alias = (String) storeHash.get(c);

                if (alias != null)
                {
                    if (alias.startsWith("("))
                    {
                        result |= IN_KEYSTORE;
                    }
                    else if (alias.startsWith("["))
                    {
                        result |= IN_SCOPE;
                    }
                }
                else
                {
                    if (store != null)
                    {
                        try
                        {
                            alias = store.getCertificateAlias(c);
                        }
                        catch (KeyStoreException kse)
                        {
                            // never happens, because keystore has been loaded
                        }
                        if (alias != null)
                        {
                            storeHash.put(c, "(" + alias + ")");
                            found = true;
                            result |= IN_KEYSTORE;
                        }
                    }
                    if (!found && (scope != null))
                    {
                        final Identity id = scope.getIdentity(c.getPublicKey());
                        if (id != null)
                        {
                            result |= IN_SCOPE;
                            storeHash.put(c, "[" + id.getName() + "]");
                        }
                    }
                }
            }
        }
        return result;
    }

    void signJar(final String jarName, final String alias, final String[] args) throws Exception
    {
        boolean aliasUsed = false;
        X509Certificate tsaCert = null;

        if (sigfile == null)
        {
            sigfile = alias;
            aliasUsed = true;
        }

        if (sigfile.length() > 8)
        {
            sigfile = sigfile.substring(0, 8).toUpperCase();
        }
        else
        {
            sigfile = sigfile.toUpperCase();
        }

        final StringBuffer tmpSigFile = new StringBuffer(sigfile.length());
        for (int j = 0; j < sigfile.length(); j++)
        {
            char c = sigfile.charAt(j);
            if (!
                    ((c >= 'A' && c <= 'Z') ||
                            (c >= '0' && c <= '9') ||
                            (c == '-') ||
                            (c == '_')))
            {
                if (aliasUsed)
                {
                    // convert illegal characters from the alias to be _'s
                    c = '_';
                }
                else
                {
                    throw new
                            RuntimeException(RB.getString
                            ("signature filename must consist of the following characters: A-Z, 0-9, _ or -"));
                }
            }
            tmpSigFile.append(c);
        }

        sigfile = tmpSigFile.toString();

        final String tmpJarName;
        if (signedjar == null)
        {
            tmpJarName = jarName + ".sig";
        }
        else
        {
            tmpJarName = signedjar;
        }

        final File jarFile = new File(jarName);
        final File signedJarFile = new File(tmpJarName);

        // Open the jar (zip) file
        try
        {
            zipFile = new ZipFile(jarName);
        }
        catch (IOException ioe)
        {
            LOG.error(RB.getString("jarsigner: ") + (RB.getString("unable to open jar file: ") + jarName), ioe);
        }

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(signedJarFile);
        }
        catch (IOException ioe)
        {
            LOG.error(RB.getString("jarsigner: ") + (RB.getString("unable to create: ") + tmpJarName), ioe);
        }

        final PrintStream ps = new PrintStream(fos);
        final ZipOutputStream zos = new ZipOutputStream(ps);

        /* First guess at what they might be - we don't xclude RSA ones. */
        String sfFilename = (META_INF + sigfile + ".SF").toUpperCase();
        String bkFilename = (META_INF + sigfile + ".DSA").toUpperCase();

        final Manifest manifest = new Manifest();
        final Map mfEntries = manifest.getEntries();

        boolean mfModified = false;
        boolean mfCreated = false;
        byte[] mfRawBytes = null;

        try
        {
            // For now, hard-code the message digest algorithm to SHA-1
            final MessageDigest[] digests = {MessageDigest.getInstance("SHA1")};

            // Check if manifest exists
            ZipEntry mfFile;
            if ((mfFile = getManifestFile(zipFile)) != null)
            {
                // Manifest exists. Read its raw bytes.
                mfRawBytes = getBytes(zipFile, mfFile);
                manifest.read(new ByteArrayInputStream(mfRawBytes));
            }
            else
            {
                // Create new manifest
                final Attributes mattr = manifest.getMainAttributes();
                mattr.putValue(Attributes.Name.MANIFEST_VERSION.toString(),
                        "1.0");
                final String javaVendor = System.getProperty("java.vendor");
                final String jdkVersion = System.getProperty("java.version");
                mattr.putValue("Created-By", jdkVersion + " (" + javaVendor
                        + ")");
                mfFile = new ZipEntry(JarFile.MANIFEST_NAME);
                mfCreated = true;
            }

            /*
            * For each entry in jar
            * (except for signature-related META-INF entries),
            * do the following:
            *
            * - if entry is not contained in manifest, add it to manifest;
            * - if entry is contained in manifest, calculate its hash and
            *   compare it with the one in the manifest; if they are
            *   different, replace the hash in the manifest with the newly
            *   generated one. (This may invalidate existing signatures!)
            */
            final BASE64Encoder encoder = new BASE64Encoder();
            final Vector mfFiles = new Vector();

            for (Enumeration enum_ = zipFile.entries(); enum_.hasMoreElements();)
            {
                final ZipEntry ze = (ZipEntry) enum_.nextElement();

                if (ze.getName().startsWith(META_INF))
                {
                    // Store META-INF files in vector, so they can be written
                    // out first
                    mfFiles.addElement(ze);

                    if (signatureRelated(ze.getName()))
                    {
                        // ignore signature-related and manifest files
                        continue;
                    }
                }

                if (manifest.getAttributes(ze.getName()) != null)
                {
                    // jar entry is contained in manifest, check and
                    // possibly update its digest attributes
                    if (updateDigests(ze, zipFile, digests, encoder, manifest))
                    {
                        mfModified = true;
                    }
                }
                else if (!ze.isDirectory())
                {
                    // Add entry to manifest
                    final Attributes attrs = getDigestAttributes(ze, zipFile, digests, encoder);
                    mfEntries.put(ze.getName(), attrs);
                    mfModified = true;
                }
            }

            // Recalculate the manifest raw bytes if necessary
            if (mfModified)
            {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                manifest.write(baos);
                mfRawBytes = baos.toByteArray();
            }

            // Write out the manifest
            if (mfModified)
            {
                // manifest file has new length
                mfFile = new ZipEntry(JarFile.MANIFEST_NAME);
            }
            if (verbose)
            {
                if (mfCreated)
                {
                    System.out.println(RB.getString("   adding: ") + mfFile.getName());
                }
                else if (mfModified)
                {
                    System.out.println(RB.getString(" updating: ") + mfFile.getName());
                }
            }
            zos.putNextEntry(mfFile);
            zos.write(mfRawBytes);

            // Calculate SignatureFile (".SF") and SignatureBlockFile
            final ManifestDigester manDig = new ManifestDigester(mfRawBytes);
            final SignatureFile sf = new SignatureFile(digests, manifest, manDig, sigfile, signManifest);

            if (tsaAlias != null)
            {
                tsaCert = getTsaCert(tsaAlias);
            }

            SignatureFile.Block block = null;

            try
            {
                block = sf.generateBlock(privateKey, certChain, externalSF, tsaUrl, tsaCert, signingMechanism, args, zipFile);
            }
            catch (SocketTimeoutException e)
            {
                // Provide a helpful message when TSA is beyond a firewall
                LOG.error(RB.getString("jarsigner: ") + (RB.getString("unable to sign jar: ") +
                        RB.getString("no response from the Timestamping Authority. ") +
                        RB.getString("When connecting from behind a firewall then an HTTP proxy may need to be specified. ") +
                        RB.getString("Supply the following options to jarsigner: ") +
                        "\n  -J-Dhttp.proxyHost=<hostname> " +
                        "\n  -J-Dhttp.proxyPort=<portnumber> "), e);
            }

            sfFilename = sf.getMetaName();
            bkFilename = block.getMetaName();

            final ZipEntry sfFile = new ZipEntry(sfFilename);
            final ZipEntry bkFile = new ZipEntry(bkFilename);

            final long time = System.currentTimeMillis();
            sfFile.setTime(time);
            bkFile.setTime(time);

            // signature file
            zos.putNextEntry(sfFile);
            sf.write(zos);
            if (verbose)
            {
                if (zipFile.getEntry(sfFilename) != null)
                {
                    System.out.println(RB.getString(" updating: ") + sfFilename);
                }
                else
                {
                    System.out.println(RB.getString("   adding: ") + sfFilename);
                }
            }

            if (verbose)
            {
                if (tsaUrl != null || tsaCert != null)
                {
                    System.out.println(RB.getString("requesting a signature timestamp"));
                }
                if (tsaUrl != null)
                {
                    System.out.println(RB.getString("TSA location: ") + tsaUrl);
                }
                if (tsaCert != null)
                {
                    final String certUrl = TimestampedSigner.getTimestampingUrl(tsaCert);
                    if (certUrl != null)
                    {
                        System.out.println(RB.getString("TSA location: ") + certUrl);
                    }
                    System.out.println(RB.getString("TSA certificate: ") + printCert(tsaCert));
                }
                if (signingMechanism != null)
                {
                    System.out.println(RB.getString("using an alternative signing mechanism"));
                }
            }

            // signature block file
            zos.putNextEntry(bkFile);
            block.write(zos);
            if (verbose)
            {
                if (zipFile.getEntry(bkFilename) != null)
                {
                    System.out.println(RB.getString(" updating: ") + bkFilename);
                }
                else
                {
                    System.out.println(RB.getString("   adding: ") + bkFilename);
                }
            }

            // Write out all other META-INF files that we stored in the
            // vector
            for (int i = 0; i < mfFiles.size(); i++)
            {
                final ZipEntry ze = (ZipEntry) mfFiles.elementAt(i);
                if (!ze.getName().equalsIgnoreCase(JarFile.MANIFEST_NAME)
                        && !ze.getName().equalsIgnoreCase(sfFilename)
                        && !ze.getName().equalsIgnoreCase(bkFilename))
                {
                    writeEntry(zipFile, zos, ze);
                }
            }

            // Write out all other files
            for (Enumeration enum_ = zipFile.entries(); enum_.hasMoreElements();)
            {
                final ZipEntry ze = (ZipEntry) enum_.nextElement();

                if (!ze.getName().startsWith(META_INF))
                {
                    if (verbose)
                    {
                        if (manifest.getAttributes(ze.getName()) != null)
                        {
                            System.out.println(RB.getString("  signing: ") + ze.getName());
                        }
                        else
                        {
                            System.out.println(RB.getString("   adding: ") + ze.getName());
                        }
                    }
                    writeEntry(zipFile, zos, ze);
                }
            }

            zipFile.close();
            zos.close();

            if (signedjar == null)
            {
                // attempt an atomic rename. If that fails,
                // rename the original jar file, then the signed
                // one, then delete the original.
                if (!signedJarFile.renameTo(jarFile))
                {
                    final File origJar = new File(jarName + ".orig");

                    if (jarFile.renameTo(origJar))
                    {
                        if (signedJarFile.renameTo(jarFile))
                        {
                            origJar.delete();
                        }
                        else
                        {
                            final MessageFormat form = new MessageFormat(RB.getString("attempt to rename signedJarFile to jarFile failed"));
                            final Object[] source = {signedJarFile, jarFile};
                            LOG.error(RB.getString("jarsigner: ") + form.format(source));
                        }
                    }
                    else
                    {
                        final MessageFormat form = new MessageFormat(RB.getString("attempt to rename jarFile to origJar failed"));
                        final Object[] source = {jarFile, origJar};
                        LOG.error(RB.getString("jarsigner: ") + form.format(source));
                    }
                }
            }

            if (hasExpiredCert || hasExpiringCert || notYetValidCert)
            {
                System.out.println();

                System.out.print(RB.getString("Warning: "));
                if (hasExpiredCert)
                {
                    System.out.println(RB.getString("The signer certificate has expired."));
                }
                else if (hasExpiringCert)
                {
                    System.out.println(RB.getString("The signer certificate will expire within six months."));
                }
                else if (notYetValidCert)
                {
                    System.out.println(RB.getString("The signer certificate is not yet valid."));
                }
            }

        }
        catch (IOException ioe)
        {
            LOG.error(RB.getString("jarsigner: ") + (RB.getString("unable to sign jar: ") + ioe), ioe);
        }
    }

    /**
     * signature-related files include: . META-INF/MANIFEST.MF . META-INF/SIG-* . META-INF/*.SF . META-INF/*.DSA .
     * META-INF/*.RSA
     */
    private boolean signatureRelated(final String name)
    {
        final String ucName = name.toUpperCase();
        if (ucName.equals(JarFile.MANIFEST_NAME) ||
                ucName.equals(META_INF) ||
                (ucName.startsWith(SIG_PREFIX) &&
                        ucName.indexOf("/") == ucName.lastIndexOf("/")))
        {
            return true;
        }

        if (ucName.startsWith(META_INF) &&
                SignatureFileVerifier.isBlockOrSF(ucName))
        {
            // .SF/.DSA/.RSA files in META-INF subdirs
            // are not considered signature-related
            return (ucName.indexOf("/") == ucName.lastIndexOf("/"));
        }

        return false;
    }

    private void writeEntry(final ZipFile zf, final ZipOutputStream os, final ZipEntry ze)
            throws IOException
    {
        final byte[] data = getBytes(zf, ze);
        final ZipEntry ze2 = new ZipEntry(ze.getName());
        ze2.setMethod(ze.getMethod());
        ze2.setTime(ze.getTime());
        ze2.setComment(ze.getComment());
        ze2.setExtra(ze.getExtra());
        if (ze.getMethod() == ZipEntry.STORED)
        {
            ze2.setSize(ze.getSize());
            ze2.setCrc(ze.getCrc());
        }
        os.putNextEntry(ze2);
        if (data.length > 0)
        {
            os.write(data);
        }
    }

    void loadKeyStore(String keyStoreName, final boolean prompt) throws Exception
    {

        if (!nullStream && keyStoreName == null)
        {
            keyStoreName = System.getProperty("user.home") + File.separator + ".keystore";
        }

        if (providerName == null)
        {
            store = KeyStore.getInstance(storetype);
        }
        else
        {
            store = KeyStore.getInstance(storetype, providerName);
        }

        // Get pass phrase
        // XXX need to disable echo; on UNIX, call getpass(char *prompt)Z
        // and on NT call ??
        if (token && storepass == null && !protectedPath)
        {
            storepass = getPass(RB.getString("Enter Passphrase for keystore: "));
        }
        else if (!token && storepass == null && prompt)
        {
            storepass = getPass(RB.getString("Enter Passphrase for keystore: "));
        }

        if (nullStream)
        {
            store.load(null, storepass);
        }
        else
        {
            keyStoreName = keyStoreName.replace(File.separatorChar, '/');
            URL url = null;
            try
            {
                url = new URL(keyStoreName);
            }
            catch (java.net.MalformedURLException e)
            {
                // try as file
                final File kfile = new File(keyStoreName);
                url = new URL("file:" + kfile.getCanonicalPath());
            }
            final InputStream is = url.openStream();
            store.load(is, storepass);
            is.close();
        }
    }

    X509Certificate getTsaCert(final String alias)
    {

        java.security.cert.Certificate cs = null;

        try
        {
            cs = store.getCertificate(alias);
        }
        catch (KeyStoreException kse)
        {
            // this never happens, because keystore has been loaded
        }
        if (cs == null || (!(cs instanceof X509Certificate)))
        {
            final MessageFormat form = new MessageFormat(RB.getString
                    ("Certificate not found for: alias.  alias must reference a valid KeyStore entry containing an X.509 public key certificate for the Timestamping Authority."));
            final Object[] source = {alias, alias};
            LOG.error(RB.getString("jarsigner: ") + form.format(source));
        }
        return (X509Certificate) cs;
    }

    void getAliasInfo(final String alias)
    {

        Key key = null;

        try
        {

            java.security.cert.Certificate[] cs = null;

            try
            {
                cs = store.getCertificateChain(alias);
            }
            catch (KeyStoreException kse)
            {
                // this never happens, because keystore has been loaded
            }
            if (cs == null)
            {
                final MessageFormat form = new MessageFormat(RB.getString
                        ("Certificate chain not found for: alias.  alias must reference a valid KeyStore key entry containing a private key and corresponding public key certificate chain."));
                final Object[] source = {alias, alias};
                LOG.error(RB.getString("jarsigner: ") + form.format(source));
            }

            certChain = new X509Certificate[cs.length];
            for (int i = 0; i < cs.length; i++)
            {
                if (!(cs[i] instanceof X509Certificate))
                {
                    LOG.error(RB.getString("jarsigner: ") + RB.getString
                            ("found non-X.509 certificate in signer's chain"));
                }
                certChain[i] = (X509Certificate) cs[i];
            }

            // order the cert chain if necessary (put user cert first,
            // root-cert last in the chain)
            final X509Certificate userCert
                    = (X509Certificate) store.getCertificate(alias);

            // check validity of signer certificate
            try
            {
                userCert.checkValidity();

                if (userCert.getNotAfter().getTime() <
                        System.currentTimeMillis() + SIX_MONTHS)
                {

                    hasExpiringCert = true;
                }
            }
            catch (CertificateExpiredException cee)
            {
                hasExpiredCert = true;

            }
            catch (CertificateNotYetValidException cnyve)
            {
                notYetValidCert = true;
            }

            if (!userCert.equals(certChain[0]))
            {
                // need to order ...
                final X509Certificate[] certChainTmp
                        = new X509Certificate[certChain.length];
                certChainTmp[0] = userCert;
                Principal issuer = userCert.getIssuerDN();
                for (int i = 1; i < certChain.length; i++)
                {
                    int j;
                    // look for the cert whose subject corresponds to the
                    // given issuer
                    for (j = 0; j < certChainTmp.length; j++)
                    {
                        if (certChainTmp[j] == null)
                        {
                            continue;
                        }
                        final Principal subject = certChainTmp[j].getSubjectDN();
                        if (issuer.equals(subject))
                        {
                            certChain[i] = certChainTmp[j];
                            issuer = certChainTmp[j].getIssuerDN();
                            certChainTmp[j] = null;
                            break;
                        }
                    }
                    if (j == certChainTmp.length)
                    {
                        LOG.error(RB.getString("jarsigner: ") + RB.getString("incomplete certificate chain"));
                    }

                }
                certChain = certChainTmp; // ordered
            }

            try
            {
                if (!token && keypass == null)
                {
                    key = store.getKey(alias, storepass);
                }
                else
                {
                    key = store.getKey(alias, keypass);
                }
            }
            catch (UnrecoverableKeyException e)
            {
                if (token)
                {
                    throw e;
                }
                else if (keypass == null)
                {
                    // Did not work out, so prompt user for key password
                    final MessageFormat form = new MessageFormat(RB.getString
                            ("Enter key password for alias: "));
                    final Object[] source = {alias};
                    keypass = getPass(form.format(source));
                    key = store.getKey(alias, keypass);
                }
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error(RB.getString("jarsigner: ") + e.getMessage());
        }
        catch (UnrecoverableKeyException e)
        {
            LOG.error(RB.getString("jarsigner: ") + RB.getString("unable to recover key from keystore"));
        }
        catch (KeyStoreException kse)
        {
            // this never happens, because keystore has been loaded
        }

        if (!(key instanceof PrivateKey))
        {
            final MessageFormat form = new MessageFormat(RB.getString
                    ("key associated with alias not a private key"));
            final Object[] source = {alias};
            LOG.error(RB.getString("jarsigner: ") + form.format(source));
        }
        else
        {
            privateKey = (PrivateKey) key;
        }
    }

    char[] getPass(final String prompt)
    {
        System.err.print(prompt);
        System.err.flush();
        try
        {
            final char[] pass = Password.readPassword(System.in);

            if (pass == null)
            {
                LOG.error(RB.getString("jarsigner: ") + RB.getString("you must enter key password"));
            }
            else
            {
                return pass;
            }
        }
        catch (IOException ioe)
        {
            LOG.error(RB.getString("jarsigner: ") + (RB.getString("unable to read password: ") + ioe.getMessage()));
        }
        // this shouldn't happen
        return null;
    }

    /*
     * Reads all the bytes for a given zip entry.
     */
    private synchronized byte[] getBytes(final ZipFile zf,
                                         final ZipEntry ze) throws IOException
    {
        int n;

        final InputStream is = zf.getInputStream(ze);
        baos.reset();
        long left = ze.getSize();

        while ((left > 0) && (n = is.read(buffer, 0, buffer.length)) != -1)
        {
            baos.write(buffer, 0, n);
            left -= n;
        }

        is.close();

        return baos.toByteArray();
    }

    /*
     * Returns manifest entry from given jar file, or null if given jar file
     * does not have a manifest entry.
     */
    private ZipEntry getManifestFile(final ZipFile zf)
    {
        ZipEntry ze = zf.getEntry(JarFile.MANIFEST_NAME);
        if (ze == null)
        {
            // Check all entries for matching name
            final Enumeration enum_ = zf.entries();
            while (enum_.hasMoreElements() && ze == null)
            {
                ze = (ZipEntry) enum_.nextElement();
                if (!JarFile.MANIFEST_NAME.equalsIgnoreCase
                        (ze.getName()))
                {
                    ze = null;
                }
            }
        }
        return ze;
    }

    /*
     * Computes the digests of a zip entry, and returns them as an array
     * of base64-encoded strings.
     */
    private synchronized String[] getDigests(final ZipEntry ze, final ZipFile zf,
                                             final MessageDigest[] digests,
                                             final BASE64Encoder encoder)
            throws IOException
    {

        int n, i;
        final InputStream is = zf.getInputStream(ze);
        long left = ze.getSize();
        while ((left > 0)
                && (n = is.read(buffer, 0, buffer.length)) != -1)
        {
            for (i = 0; i < digests.length; i++)
            {
                digests[i].update(buffer, 0, n);
            }
            left -= n;
        }
        is.close();

        // complete the digests
        final String[] base64Digests = new String[digests.length];
        for (i = 0; i < digests.length; i++)
        {
            base64Digests[i] = encoder.encode(digests[i].digest());
        }
        return base64Digests;
    }

    /*
     * Computes the digests of a zip entry, and returns them as a list of
     * attributes
     */
    private Attributes getDigestAttributes(final ZipEntry ze, final ZipFile zf,
                                           final MessageDigest[] digests,
                                           final BASE64Encoder encoder)
            throws IOException
    {

        final String[] base64Digests = getDigests(ze, zf, digests, encoder);
        final Attributes attrs = new Attributes();

        for (int i = 0; i < digests.length; i++)
        {
            attrs.putValue(digests[i].getAlgorithm() + "-Digest",
                    base64Digests[i]);
        }
        return attrs;
    }

    /*
     * Updates the digest attributes of a manifest entry, by adding or
     * replacing digest values.
     * A digest value is added if the manifest entry does not contain a digest
     * for that particular algorithm.
     * A digest value is replaced if it is obsolete.
     *
     * Returns true if the manifest entry has been changed, and false
     * otherwise.
     */
    private boolean updateDigests(final ZipEntry ze, final ZipFile zf,
                                  final MessageDigest[] digests,
                                  final BASE64Encoder encoder,
                                  final Manifest mf) throws IOException
    {
        boolean update = false;

        final Attributes attrs = mf.getAttributes(ze.getName());
        final String[] base64Digests = getDigests(ze, zf, digests, encoder);

        for (int i = 0; i < digests.length; i++)
        {
            final String name = digests[i].getAlgorithm() + "-Digest";
            String mfDigest = attrs.getValue(name);
            if (mfDigest == null
                    && digests[i].getAlgorithm().equalsIgnoreCase("SHA"))
            {
                // treat "SHA" and "SHA1" the same
                mfDigest = attrs.getValue("SHA-Digest");
            }
            if (mfDigest == null)
            {
                // compute digest and add it to list of attributes
                attrs.putValue(name, base64Digests[i]);
                update = true;
            }
            else
            {
                // compare digests, and replace the one in the manifest
                // if they are different
                if (!mfDigest.equalsIgnoreCase(base64Digests[i]))
                {
                    attrs.putValue(name, base64Digests[i]);
                    update = true;
                }
            }
        }
        return update;
    }

    /*
     * Try to load the specified signing mechanism.
     * The URL class loader is used.
     */
    private ContentSigner loadSigningMechanism(final String signerClassName,
                                               final String signerClassPath) throws Exception
    {

        // construct class loader
        String cpString = null;   // make sure env.class.path defaults to dot

        // do prepends to get correct ordering
        cpString = appendPath(System.getProperty("env.class.path"), cpString);
        cpString = appendPath(System.getProperty("java.class.path"), cpString);
        cpString = appendPath(signerClassPath, cpString);
        final URL[] urls = pathToURLs(cpString);
        final ClassLoader appClassLoader = new URLClassLoader(urls);

        // attempt to find signer
        final Class signerClass = appClassLoader.loadClass(signerClassName);

        // Check that it implements ContentSigner
        final Object signer = signerClass.newInstance();
        if (!(signer instanceof ContentSigner))
        {
            final MessageFormat form = new MessageFormat(
                    RB.getString("signerClass is not a signing mechanism"));
            final Object[] source = {signerClass.getName()};
            throw new IllegalArgumentException(form.format(source));
        }
        return (ContentSigner) signer;
    }

    private static String appendPath(final String path1, final String path2)
    {
        if (path1 == null || path1.length() == 0)
        {
            return path2 == null ? "." : path2;
        }
        else if (path2 == null || path2.length() == 0)
        {
            return path1;
        }
        else
        {
            return path1 + File.pathSeparator + path2;
        }
    }

    /**
     * Utility method for converting a search path string to an array of directory and JAR file URLs.
     *
     * @param path the search path string
     * @return the resulting array of directory and JAR file URLs
     */
    private static URL[] pathToURLs(final String path)
    {
        final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
        URL[] urls = new URL[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens())
        {
            final URL url = fileToURL(new File(st.nextToken()));
            if (url != null)
            {
                urls[count++] = url;
            }
        }
        if (urls.length != count)
        {
            final URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }

    /**
     * Returns the directory or JAR file URL corresponding to the specified local file name.
     *
     * @param file the File object
     * @return the resulting directory or JAR file URL, or null if unknown
     */
    private static URL fileToURL(final File file)
    {
        String name;
        try
        {
            name = file.getCanonicalPath();
        }
        catch (IOException e)
        {
            name = file.getAbsolutePath();
        }
        name = name.replace(File.separatorChar, '/');
        if (!name.startsWith("/"))
        {
            name = "/" + name;
        }
        // If the file does not exist, then assume that it's a directory
        if (!file.isFile())
        {
            name = name + "/";
        }
        try
        {
            return new URL("file", "", name);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("file");
        }
    }
}

/*
* This object encapsulates the parameters used to perform content signing.
*/

