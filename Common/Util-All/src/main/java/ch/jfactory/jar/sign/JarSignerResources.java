/*
 * @(#)JarSignerResources.java	1.13 04/04/21
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ch.jfactory.jar.sign;

import java.util.ListResourceBundle;

/**
 * <p> This class represents the <code>ResourceBundle</code> for JarSigner.
 *
 * @version 1.13, 04/21/04
 */
public class JarSignerResources extends ListResourceBundle
{
    private static final Object[][] contents = {
            // shared (from jarsigner)
            {" ", " "},
            {"  ", "  "},
            {"      ", "      "},
            {", ", ", "},

            {"provName not a provider", "{0} not a provider"},
            {"signerClass is not a signing mechanism", "{0} is not a signing mechanism"},
            {"jarsigner error: ", "jarsigner error: "},
            {"Illegal option: ", "Illegal option: "},
            {"-keystore must be NONE if -storetype is PKCS11",
                    "-keystore must be NONE if -storetype is PKCS11"},
            {"-keypass can not be specified if -storetype is PKCS11",
                    "-keypass can not be specified if -storetype is PKCS11"},
            {"If -protected is specified, then -storepass and -keypass must not be specified",
                    "If -protected is specified, then -storepass and -keypass must not be specified"},
            {"Usage: jarsigner [options] jar-file alias",
                    "Usage: jarsigner [options] jar-file alias"},
            {"       jarsigner -verify [options] jar-file",
                    "       jarsigner -verify [options] jar-file"},
            {"[-keystore <url>]           keystore location",
                    "[-keystore <url>]           keystore location"},
            {"[-storepass <password>]     password for keystore integrity",
                    "[-storepass <password>]     password for keystore integrity"},
            {"[-storetype <type>]         keystore type",
                    "[-storetype <type>]         keystore type"},
            {"[-keypass <password>]       password for private key (if different)",
                    "[-keypass <password>]       password for private key (if different)"},
            {"[-sigfile <file>]           name of .SF/.DSA file",
                    "[-sigfile <file>]           name of .SF/.DSA file"},
            {"[-signedjar <file>]         name of signed JAR file",
                    "[-signedjar <file>]         name of signed JAR file"},
            {"[-verify]                   verify a signed JAR file",
                    "[-verify]                   verify a signed JAR file"},
            {"[-verbose]                  verbose output when signing/verifying",
                    "[-verbose]                  verbose output when signing/verifying"},
            {"[-certs]                    display certificates when verbose and verifying",
                    "[-certs]                    display certificates when verbose and verifying"},
            {"[-tsa <url>]                location of the Timestamping Authority",
                    "[-tsa <url>]                location of the Timestamping Authority"},
            {"[-tsacert <alias>]          public key certificate for Timestamping Authority",
                    "[-tsacert <alias>]          public key certificate for Timestamping Authority"},
            {"[-altsigner <class>]        class name of an alternative signing mechanism",
                    "[-altsigner <class>]        class name of an alternative signing mechanism"},
            {"[-altsignerpath <pathlist>] location of an alternative signing mechanism",
                    "[-altsignerpath <pathlist>] location of an alternative signing mechanism"},
            {"[-internalsf]               include the .SF file inside the signature block",
                    "[-internalsf]               include the .SF file inside the signature block"},
            {"[-sectionsonly]             don't compute hash of entire manifest",
                    "[-sectionsonly]             don't compute hash of entire manifest"},
            {"[-protected]                keystore has protected authentication path",
                    "[-protected]                keystore has protected authentication path"},
            {"[-providerName <name>]      provider name",
                    "[-providerName <name>]      provider name"},
            {"[-providerClass <class>     name of cryptographic service provider's",
                    "[-providerClass <class>     name of cryptographic service provider's"},
            {"  [-providerArg <arg>]] ... master class file and constructor argument",
                    "  [-providerArg <arg>]] ... master class file and constructor argument"},
            {"s", "s"},
            {"m", "m"},
            {"k", "k"},
            {"i", "i"},
            {"  s = signature was verified ",
                    "  s = signature was verified "},
            {"  m = entry is listed in manifest",
                    "  m = entry is listed in manifest"},
            {"  k = at least one certificate was found in keystore",
                    "  k = at least one certificate was found in keystore"},
            {"  i = at least one certificate was found in identity scope",
                    "  i = at least one certificate was found in identity scope"},
            {"no manifest.", "no manifest."},
            {"jar is unsigned. (signatures missing or not parsable)",
                    "jar is unsigned. (signatures missing or not parsable)"},
            {"jar verified.", "jar verified."},
            {"jarsigner: ", "jarsigner: "},
            {"signature filename must consist of the following characters: A-Z, 0-9, _ or -",
                    "signature filename must consist of the following characters: A-Z, 0-9, _ or -"},
            {"unable to open jar file: ", "unable to open jar file: "},
            {"unable to create: ", "unable to create: "},
            {"   adding: ", "   adding: "},
            {" updating: ", " updating: "},
            {"  signing: ", "  signing: "},
            {"attempt to rename signedJarFile to jarFile failed",
                    "attempt to rename {0} to {1} failed"},
            {"attempt to rename jarFile to origJar failed",
                    "attempt to rename {0} to {1} failed"},
            {"unable to sign jar: ", "unable to sign jar: "},
            {"Enter Passphrase for keystore: ", "Enter Passphrase for keystore: "},
            {"keystore load: ", "keystore load: "},
            {"certificate exception: ", "certificate exception: "},
            {"unable to instantiate keystore class: ",
                    "unable to instantiate keystore class: "},
            {"Certificate chain not found for: alias.  alias must reference a valid KeyStore key entry containing a private key and corresponding public key certificate chain.",
                    "Certificate chain not found for: {0}.  {1} must reference a valid KeyStore key entry containing a private key and corresponding public key certificate chain."},
            {"found non-X.509 certificate in signer's chain",
                    "found non-X.509 certificate in signer's chain"},
            {"incomplete certificate chain", "incomplete certificate chain"},
            {"Enter key password for alias: ", "Enter key password for {0}: "},
            {"unable to recover key from keystore",
                    "unable to recover key from keystore"},
            {"key associated with alias not a private key",
                    "key associated with {0} not a private key"},
            {"you must enter key password", "you must enter key password"},
            {"unable to read password: ", "unable to read password: "},
            {"certificate is valid from", "certificate is valid from {0} to {1}"},
            {"certificate expired on", "certificate expired on {0}"},
            {"certificate is not valid until",
                    "certificate is not valid until {0}"},
            {"certificate will expire on", "certificate will expire on {0}"},
            {"requesting a signature timestamp",
                    "requesting a signature timestamp"},
            {"TSA location: ", "TSA location: "},
            {"TSA certificate: ", "TSA certificate: "},
            {"no response from the Timestamping Authority. ",
                    "no response from the Timestamping Authority. "},
            {"When connecting from behind a firewall then an HTTP proxy may need to be specified. ",
                    "When connecting from behind a firewall then an HTTP proxy may need to be specified. "},
            {"Supply the following options to jarsigner: ",
                    "Supply the following options to jarsigner: "},
            {"Certificate not found for: alias.  alias must reference a valid KeyStore entry containing an X.509 public key certificate for the Timestamping Authority.",
                    "Certificate not found for: {0}.  {1} must reference a valid KeyStore entry containing an X.509 public key certificate for the Timestamping Authority."},
            {"using an alternative signing mechanism",
                    "using an alternative signing mechanism"},
            {"entry was signed on", "entry was signed on {0}"},
            {"Warning: ", "Warning: "},
            {"This jar contains unsigned entries which have not been integrity-checked. ",
                    "This jar contains unsigned entries which have not been integrity-checked. "},
            {"This jar contains entries whose signer certificate has expired. ",
                    "This jar contains entries whose signer certificate has expired. "},
            {"This jar contains entries whose signer certificate will expire within six months. ",
                    "This jar contains entries whose signer certificate will expire within six months. "},
            {"This jar contains entries whose signer certificate is not yet valid. ",
                    "This jar contains entries whose signer certificate is not yet valid. "},
            {"Re-run with the -verbose option for more details.",
                    "Re-run with the -verbose option for more details."},
            {"Re-run with the -verbose and -certs options for more details.",
                    "Re-run with the -verbose and -certs options for more details."},
            {"The signer certificate has expired.",
                    "The signer certificate has expired."},
            {"The signer certificate will expire within six months.",
                    "The signer certificate will expire within six months."},
            {"The signer certificate is not yet valid.",
                    "The signer certificate is not yet valid."},
    };

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     *
     * <p>
     *
     * @return the contents of this <code>ResourceBundle</code>.
     */
    public Object[][] getContents()
    {
        return contents;
    }
}
