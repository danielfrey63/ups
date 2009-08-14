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
    public boolean verifyServerHostKey(final String hostname, final int port, final String serverHostKeyAlgorithm,
                                       final byte[] serverHostKey) throws Exception
    {
        return true;
    }
}
