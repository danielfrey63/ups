/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.jar.sign;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipFile;
import sun.misc.BASE64Encoder;
import sun.security.util.ManifestDigester;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertInfo;

/**
 * TODO: document
 *
 * @author Daniel Frey
 */
class SignatureFile
{

    /** SignatureFile */
    Manifest sf;

    /** .SF base name */
    String baseName;

    public SignatureFile(final MessageDigest[] digests,
                         final Manifest mf,
                         final ManifestDigester md,
                         final String baseName,
                         final boolean signManifest)

    {
        this.baseName = baseName;

        final String version = System.getProperty("java.version");
        final String javaVendor = System.getProperty("java.vendor");

        sf = new Manifest();
        final Attributes mattr = sf.getMainAttributes();
        final BASE64Encoder encoder = new BASE64Encoder();

        mattr.putValue(Attributes.Name.SIGNATURE_VERSION.toString(), "1.0");
        mattr.putValue("Created-By", version + " (" + javaVendor + ")");

        if (signManifest)
        {
            // sign the whole manifest
            for (MessageDigest digest : digests)
            {
                mattr.putValue(digest.getAlgorithm() + "-Digest-Manifest",
                        encoder.encode(md.manifestDigest(digest)));
            }
        }

        // create digest of the manifest main attributes
        ManifestDigester.Entry mde =
                md.get(ManifestDigester.MF_MAIN_ATTRS, false);
        if (mde != null)
        {
            for (MessageDigest digest : digests)
            {
                mattr.putValue(digest.getAlgorithm() +
                        "-Digest-" + ManifestDigester.MF_MAIN_ATTRS,
                        encoder.encode(mde.digest(digest)));
            }
        }
        else
        {
            throw new IllegalStateException
                    ("ManifestDigester failed to create " +
                            "Manifest-Main-Attribute entry");
        }

        /* go through the manifest entries and create the digests */

        final Map entries = sf.getEntries();
        for (final Map.Entry<String, Attributes> stringAttributesEntry : mf.getEntries().entrySet())
        {
            final Map.Entry e = (Map.Entry) stringAttributesEntry;
            final String name = (String) e.getKey();
            mde = md.get(name, false);
            if (mde != null)
            {
                final Attributes attr = new Attributes();
                for (MessageDigest digest : digests)
                {
                    attr.putValue(digest.getAlgorithm() + "-Digest",
                            encoder.encode(mde.digest(digest)));
                }
                entries.put(name, attr);
            }
        }
    }

    /**
     * Writes the SignatureFile to the specified OutputStream.
     *
     * @param out the output stream
     * @throws java.io.IOException if an I/O error has occurred
     */

    public void write(final OutputStream out) throws IOException
    {
        sf.write(out);
    }

    /** get .SF file name */
    public String getMetaName()
    {
        return "META-INF/" + baseName + ".SF";
    }

    /** get base file name */
    public String getBaseName()
    {
        return baseName;
    }

    /*
     * Generate a signed data block.
     * If a URL or a certificate (containing a URL) for a Timestamping
     * Authority is supplied then a signature timestamp is generated and
     * inserted into the signed data block.
     *
     * @param tsaUrl The location of the Timestamping Authority. If null
     *               then no timestamp is requested.
     * @param tsaCert The certificate for the Timestamping Authority. If null
     *               then no timestamp is requested.
     * @param signingMechanism The signing mechanism to use.
     * @param args The command-line arguments to jarsigner.
     * @param zipFile The original source Zip file.
     */
    public Block generateBlock(final PrivateKey privateKey,
                               final X509Certificate[] certChain,
                               final boolean externalSF, final String tsaUrl,
                               final X509Certificate tsaCert,
                               final ContentSigner signingMechanism,
                               final String[] args, final ZipFile zipFile)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException,
            SignatureException, CertificateException
    {
        return new Block(this, privateKey, certChain, externalSF, tsaUrl,
                tsaCert, signingMechanism, args, zipFile);
    }

    public static class Block
    {

        private byte[] block;

        private String blockFileName;

        /*
       * Construct a new signature block.
       */
        Block(final SignatureFile sfg, final PrivateKey privateKey,
              final X509Certificate[] certChain, final boolean externalSF, final String tsaUrl,
              final X509Certificate tsaCert, ContentSigner signingMechanism,
              final String[] args, final ZipFile zipFile)
                throws NoSuchAlgorithmException, InvalidKeyException, IOException,
                SignatureException, CertificateException
        {

            Principal issuerName = certChain[0].getIssuerDN();
            if (!(issuerName instanceof X500Name))
            {
                // must extract the original encoded form of DN for subsequent
                // name comparison checks (converting to a String and back to
                // an encoded DN could cause the types of String attribute
                // values to be changed)
                final X509CertInfo tbsCert = new
                        X509CertInfo(certChain[0].getTBSCertificate());
                issuerName = (Principal)
                        tbsCert.get(CertificateIssuerName.NAME + "." +
                                CertificateIssuerName.DN_NAME);
            }
            final BigInteger serial = certChain[0].getSerialNumber();
            final String keyAlgorithm = privateKey.getAlgorithm();

            final String digestAlgorithm;
            if (keyAlgorithm.equalsIgnoreCase("DSA"))
            {
                digestAlgorithm = "SHA1";
            }
            else if (keyAlgorithm.equalsIgnoreCase("RSA"))
            {
                digestAlgorithm = "MD5";
            }
            else
            {
                throw new RuntimeException("private key is not a DSA or "
                        + "RSA key");
            }

            final String signatureAlgorithm = digestAlgorithm + "with" +
                    keyAlgorithm;

            blockFileName = "META-INF/" + sfg.getBaseName() + "." + keyAlgorithm;

            final AlgorithmId digestAlg = AlgorithmId.get(digestAlgorithm);
            final AlgorithmId sigAlg = AlgorithmId.get(signatureAlgorithm);
            final AlgorithmId digEncrAlg = AlgorithmId.get(keyAlgorithm);

            final Signature sig = Signature.getInstance(signatureAlgorithm);
            sig.initSign(privateKey);

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            sfg.write(baos);

            final byte[] content = baos.toByteArray();

            sig.update(content);
            final byte[] signature = sig.sign();

            // Timestamp the signature and generate the signature block file
            if (signingMechanism == null)
            {
                signingMechanism = new TimestampedSigner();
            }
            URI tsaUri = null;
            try
            {
                if (tsaUrl != null)
                {
                    tsaUri = new URI(tsaUrl);
                }
            }
            catch (URISyntaxException e)
            {
                final IOException ioe = new IOException();
                ioe.initCause(e);
                throw ioe;
            }

            // Assemble parameters for the signing mechanism
            final ContentSignerParameters params =
                    new JarSignerParameters(args, tsaUri, tsaCert, signature,
                            signatureAlgorithm, certChain, content, zipFile);

            // Generate the signature block
            block = signingMechanism.generateSignedData(
                    params, externalSF, (tsaUrl != null || tsaCert != null));
        }

        /*
       * get block file name.
       */
        public String getMetaName()
        {
            return blockFileName;
        }

        /**
         * Writes the block file to the specified OutputStream.
         *
         * @param out the output stream
         * @throws java.io.IOException if an I/O error has occurred
         */

        public void write(final OutputStream out) throws IOException
        {
            out.write(block);
        }
    }
}
