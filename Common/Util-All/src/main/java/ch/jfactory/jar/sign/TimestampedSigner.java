package ch.jfactory.jar.sign;/*
 * @(#)TimestampedSigner.java	1.2 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;
import sun.security.pkcs.SignerInfo;
import sun.security.timestamp.HttpTimestamper;
import sun.security.timestamp.TSRequest;
import sun.security.timestamp.TSResponse;
import sun.security.timestamp.Timestamper;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AccessDescription;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.URIName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertInfo;

/**
 * This class implements a content signing service. It generates a timestamped signature for a given content according
 * to <a href="http://www.ietf.org/rfc/rfc3161.txt">RFC 3161</a>. The signature along with a trusted timestamp and the
 * signer's certificate are all packaged into a standard PKCS #7 Signed Data message.
 *
 * @author Vincent Ryan
 * @version 1.2, 12/19/03
 */

public final class TimestampedSigner extends ContentSigner
{

    /*
     * Random number generator for creating nonce values
     */
    private static final SecureRandom RANDOM;

    static
    {
        SecureRandom tmp = null;
        try
        {
            tmp = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            // should not happen
        }
        RANDOM = tmp;
    }

    /*
     * Object identifier for the subject information access X.509 certificate
     * extension.
     */
    private static final String SUBJECT_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.11";

    /*
     * Object identifier for the timestamping key purpose.
     */
    private static final String KP_TIMESTAMPING_OID = "1.3.6.1.5.5.7.3.8";

    /*
     * Object identifier for the timestamping access descriptors.
     */
    private static final ObjectIdentifier AD_TIMESTAMPING_Id;

    static
    {
        ObjectIdentifier tmp = null;
        try
        {
            tmp = new ObjectIdentifier("1.3.6.1.5.5.7.48.3");
        }
        catch (IOException e)
        {
            // ignore
        }
        AD_TIMESTAMPING_Id = tmp;
    }

    /*
     * Location of the TSA.
     */
    private String tsaUrl = null;

    /*
     * Generates an SHA-1 hash value for the data to be timestamped.
     */
    private MessageDigest messageDigest = null;

    /*
     * Parameters for the timestamping protocol.
     */
    private boolean tsRequestCertificate = true;

    /** Instantiates a content signer that supports timestamped signatures. */
    public TimestampedSigner()
    {
    }

    /**
     * Generates a PKCS #7 signed data message that includes a signature timestamp. This method is used when a signature
     * has already been generated. The signature, a signature timestamp, the signer's certificate chain, and optionally
     * the content that was signed, are packaged into a PKCS #7 signed data message.
     *
     * @param parameters     The non-null input parameters.
     * @param omitContent    true if the content should be omitted from the signed data message. Otherwise the content
     *                       is included.
     * @param applyTimestamp true if the signature should be timestamped. Otherwise timestamping is not performed.
     * @return A PKCS #7 signed data message including a signature timestamp.
     * @throws NoSuchAlgorithmException The exception is thrown if the signature algorithm is unrecognised.
     * @throws CertificateException     The exception is thrown if an error occurs while processing the signer's
     *                                  certificate or the TSA's certificate.
     * @throws IOException              The exception is thrown if an error occurs while generating the signature
     *                                  timestamp or while generating the signed data message.
     * @throws NullPointerException     The exception is thrown if parameters is null.
     */
    public byte[] generateSignedData(final ContentSignerParameters parameters,
                                     final boolean omitContent, final boolean applyTimestamp)
            throws NoSuchAlgorithmException, CertificateException, IOException
    {

        if (parameters == null)
        {
            throw new NullPointerException();
        }

        // Parse the signature algorithm to extract the digest and key
        // algorithms. The expected format is:
        //     "<digest>with<encryption>"
        // or  "<digest>with<encryption>and<mgf>"
        final String signatureAlgorithm = parameters.getSignatureAlgorithm();
        String digestAlgorithm = null;
        String keyAlgorithm = null;
        final int with = signatureAlgorithm.indexOf("with");
        if (with > 0)
        {
            digestAlgorithm = signatureAlgorithm.substring(0, with);
            final int and = signatureAlgorithm.indexOf("and", with + 4);
            if (and > 0)
            {
                keyAlgorithm = signatureAlgorithm.substring(with + 4, and);
            }
            else
            {
                keyAlgorithm = signatureAlgorithm.substring(with + 4);
            }
        }
        final AlgorithmId digestAlgorithmId = AlgorithmId.get(digestAlgorithm);

        // Examine signer's certificate
        final X509Certificate[] signerCertificateChain =
                parameters.getSignerCertificateChain();
        Principal issuerName = signerCertificateChain[0].getIssuerDN();
        if (!(issuerName instanceof X500Name))
        {
            // must extract the original encoded form of DN for subsequent
            // name comparison checks (converting to a String and back to
            // an encoded DN could cause the types of String attribute
            // values to be changed)
            final X509CertInfo tbsCert = new
                    X509CertInfo(signerCertificateChain[0].getTBSCertificate());
            issuerName = (Principal)
                    tbsCert.get(CertificateIssuerName.NAME + "." +
                            CertificateIssuerName.DN_NAME);
        }
        final BigInteger serialNumber = signerCertificateChain[0].getSerialNumber();

        // Include or exclude content
        final byte[] content = parameters.getContent();
        final ContentInfo contentInfo;
        if (omitContent)
        {
            contentInfo = new ContentInfo(ContentInfo.DATA_OID, null);
        }
        else
        {
            contentInfo = new ContentInfo(content);
        }

        // Generate the timestamp token
        final byte[] signature = parameters.getSignature();
        final SignerInfo signerInfo;
        if (applyTimestamp)
        {

            final X509Certificate tsaCertificate = parameters.getTimestampingAuthorityCertificate();
            final URI tsaUri = parameters.getTimestampingAuthority();
            if (tsaUri != null)
            {
                tsaUrl = tsaUri.toString();
            }
            else
            {
                // Examine TSA cert
                final String certUrl = getTimestampingUrl(tsaCertificate);
                if (certUrl == null)
                {
                    throw new CertificateException(
                            "Subject Information Access extension not found");
                }
                tsaUrl = certUrl;
            }

            // Timestamp the signature
            final byte[] tsToken = generateTimestampToken(signature);

            // Insert the timestamp token into the PKCS #7 signer info element
            // (as an unsigned attribute)
            final PKCS9Attributes unsignedAttrs =
                    new PKCS9Attributes(new PKCS9Attribute[]{
                            new PKCS9Attribute(
                                    PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_STR,
                                    tsToken)});
            signerInfo = new SignerInfo((X500Name) issuerName, serialNumber,
                    digestAlgorithmId, null, AlgorithmId.get(keyAlgorithm),
                    signature, unsignedAttrs);
        }
        else
        {
            signerInfo = new SignerInfo((X500Name) issuerName, serialNumber,
                    digestAlgorithmId, AlgorithmId.get(keyAlgorithm), signature);
        }

        final SignerInfo[] signerInfos = {signerInfo};
        final AlgorithmId[] algorithms = {digestAlgorithmId};

        // Create the PKCS #7 signed data message
        final PKCS7 p7 =
                new PKCS7(algorithms, contentInfo, signerCertificateChain,
                        signerInfos);
        final ByteArrayOutputStream p7out = new ByteArrayOutputStream();
        p7.encodeSignedData(p7out);

        return p7out.toByteArray();
    }

    /**
     * Examine the certificate for a Subject Information Access extension (<a href="http://www.ietf.org/rfc/rfc3280.txt">RFC
     * 3280</a>). The extension's <tt>accessMethod</tt> field should contain the object identifier defined for
     * timestamping: 1.3.6.1.5.5.7.48.3 and its <tt>accessLocation</tt> field should contain an HTTP URL.
     *
     * @param tsaCertificate An X.509 certificate for the TSA.
     * @return An HTTP URL or null if none was found.
     */
    public static String getTimestampingUrl(final X509Certificate tsaCertificate)
    {

        if (tsaCertificate == null)
        {
            return null;
        }
        // Parse the extensions
        try
        {
            final byte[] extensionValue =
                    tsaCertificate.getExtensionValue(SUBJECT_INFO_ACCESS_OID);
            if (extensionValue == null)
            {
                return null;
            }
            DerInputStream der = new DerInputStream(extensionValue);
            der = new DerInputStream(der.getOctetString());
            final DerValue[] derValue = der.getSequence(5);
            AccessDescription description;
            GeneralName location;
            URIName uri;
            for (final DerValue value : derValue)
            {
                description = new AccessDescription(value);
                if (description.getAccessMethod().equals((Object)AD_TIMESTAMPING_Id))
                {
                    location = description.getAccessLocation();
                    if (location.getType() == GeneralNameInterface.NAME_URI)
                    {
                        uri = (URIName) location.getName();
                        if (uri.getScheme().equalsIgnoreCase("http"))
                        {
                            return uri.getName();
                        }
                    }
                }
            }
        }
        catch (IOException ioe)
        {
            // ignore
        }
        return null;
    }

    /*
     * Returns a timestamp token from a TSA for the given content.
     * Performs a basic check on the token to confirm that it has been signed
     * by a certificate that is permitted to sign timestamps.
     *
     * @param  toBeTimestamped The data to be timestamped.
     * @throws IOException The exception is throw if an error occurs while
     *                     communicating with the TSA.
     * @throws CertificateException The exception is throw if the TSA's
     *                     certificate is not permitted for timestamping.
     */
    private byte[] generateTimestampToken(final byte[] toBeTimestamped) throws CertificateException, IOException
    {
        // Generate hash value for the data to be timestamped
        // SHA-1 is always used.
        if (messageDigest == null)
        {
            try
            {
                messageDigest = MessageDigest.getInstance("SHA-1");
            }
            catch (NoSuchAlgorithmException e)
            {
                // ignore
            }
        }
        final byte[] digest = messageDigest.digest(toBeTimestamped);

        // Generate a timestamp
        final TSRequest tsQuery = new TSRequest(digest, "SHA-1");
        // Generate a nonce
        if (RANDOM != null)
        {
            tsQuery.setNonce(new BigInteger(64, RANDOM));
        }
        tsQuery.requestCertificate(tsRequestCertificate);

        final Timestamper tsa = new HttpTimestamper(tsaUrl); // use supplied TSA
        final TSResponse tsReply = tsa.generateTimestamp(tsQuery);
        final int status = tsReply.getStatusCode();
        // Handle TSP error
        if (status != 0 && status != 1)
        {
            if (tsReply.getFailureCode() == -1)
            {
                throw new IOException("Error generating timestamp: " + tsReply.getStatusCodeAsText());
            }
            else
            {
                throw new IOException("Error generating timestamp: " + tsReply.getStatusCodeAsText() + " " + tsReply.getFailureCodeAsText());
            }
        }
        final PKCS7 tsToken = tsReply.getToken();

        // Examine the TSA's certificate (if present)
        final X509Certificate[] certs = tsToken.getCertificates();
        if (certs != null && certs.length > 0)
        {
            // Use certficate from the TSP reply
            if (!certs[0].getExtendedKeyUsage().contains(KP_TIMESTAMPING_OID))
            {
                throw new CertificateException("Certificate is not valid for timestamping");
            }
        }

        return tsReply.getEncodedToken();
    }
}
