/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.jar.sign;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.zip.ZipFile;

/**
 * TODO: document
 *
 * @author Daniel Frey
 */
class JarSignerParameters implements ContentSignerParameters
{
    private final String[] args;

    private final URI tsa;

    private final X509Certificate tsaCertificate;

    private final byte[] signature;

    private final String signatureAlgorithm;

    private final X509Certificate[] signerCertificateChain;

    private final byte[] content;

    private final ZipFile source;

    /** Create a new object. */
    JarSignerParameters( final String[] args, final URI tsa, final X509Certificate tsaCertificate,
                         final byte[] signature, final String signatureAlgorithm,
                         final X509Certificate[] signerCertificateChain, final byte[] content,
                         final ZipFile source )
    {
        if ( signature == null || signatureAlgorithm == null ||
                signerCertificateChain == null )
        {
            throw new NullPointerException();
        }
        this.args = args;
        this.tsa = tsa;
        this.tsaCertificate = tsaCertificate;
        this.signature = signature;
        this.signatureAlgorithm = signatureAlgorithm;
        this.signerCertificateChain = signerCertificateChain;
        this.content = content;
        this.source = source;
    }

    /**
     * Retrieves the command-line arguments.
     *
     * @return The command-line arguments. May be null.
     */
    public String[] getCommandLine()
    {
        return args;
    }

    /**
     * Retrieves the identifier for a Timestamping Authority (TSA).
     *
     * @return The TSA identifier. May be null.
     */
    public URI getTimestampingAuthority()
    {
        return tsa;
    }

    /**
     * Retrieves the certificate for a Timestamping Authority (TSA).
     *
     * @return The TSA certificate. May be null.
     */
    public X509Certificate getTimestampingAuthorityCertificate()
    {
        return tsaCertificate;
    }

    /**
     * Retrieves the signature.
     *
     * @return The non-null signature bytes.
     */
    public byte[] getSignature()
    {
        return signature;
    }

    /**
     * Retrieves the name of the signature algorithm.
     *
     * @return The non-null string name of the signature algorithm.
     */
    public String getSignatureAlgorithm()
    {
        return signatureAlgorithm;
    }

    /**
     * Retrieves the signer's X.509 certificate chain.
     *
     * @return The non-null array of X.509 public-key certificates.
     */
    public X509Certificate[] getSignerCertificateChain()
    {
        return signerCertificateChain;
    }

    /**
     * Retrieves the content that was signed.
     *
     * @return The content bytes. May be null.
     */
    public byte[] getContent()
    {
        return content;
    }

    /**
     * Retrieves the original source ZIP file before it was signed.
     *
     * @return The original ZIP file. May be null.
     */
    public ZipFile getSource()
    {
        return source;
    }
}
