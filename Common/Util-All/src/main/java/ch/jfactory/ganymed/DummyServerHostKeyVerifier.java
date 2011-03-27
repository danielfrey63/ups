/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.ganymed;

import ch.ethz.ssh2.ServerHostKeyVerifier;

/**
 * Implementation that returns always true.
 *
 * @author Daniel Frey 27.06.2008 08:48:50
 */
public class DummyServerHostKeyVerifier implements ServerHostKeyVerifier
{
    /** {@inheritDoc} */
    public boolean verifyServerHostKey( final String hostname, final int port, final String serverHostKeyAlgorithm,
                                        final byte[] serverHostKey ) throws Exception
    {
        return true;
    }
}
