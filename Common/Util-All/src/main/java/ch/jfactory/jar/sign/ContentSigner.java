/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */

package ch.jfactory.jar.sign;/*
 * @(#)ContentSigner.java	1.2 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * This class defines a content signing service. Implementations must be instantiable using a zero-argument constructor.
 *
 * @author Vincent Ryan
 * @version 1.2, 12/19/03
 * @since 1.5
 */

public abstract class ContentSigner
{
    /**
     * Generates a PKCS #7 signed data message. This method is used when the signature has already been generated. The signature, the signer's details, and optionally a signature timestamp and the content that was signed, are all packaged into a signed data message.
     *
     * @param parameters     The non-null input parameters.
     * @param omitContent    true if the content should be omitted from the signed data message. Otherwise the content is included.
     * @param applyTimestamp true if the signature should be timestamped. Otherwise timestamping is not performed.
     * @return A PKCS #7 signed data message.
     * @throws NoSuchAlgorithmException The exception is thrown if the signature algorithm is unrecognised.
     * @throws CertificateException     The exception is thrown if an error occurs while processing the signer's certificate or the TSA's certificate.
     * @throws IOException              The exception is thrown if an error occurs while generating the signature timestamp or while generating the signed data message.
     * @throws NullPointerException     The exception is thrown if parameters is null.
     */
    public abstract byte[] generateSignedData(
            ContentSignerParameters parameters, boolean omitContent,
            boolean applyTimestamp )
            throws NoSuchAlgorithmException, CertificateException, IOException;
}
