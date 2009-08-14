package ch.jfactory.ganymed;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import java.io.IOException;

/**
 * Some convenience methods to handle SSH2.
 *
 * @author Daniel Frey 27.06.2008 08:59:32
 */
public final class Ssh2Helper {

    /** Hide constructor. */
    private Ssh2Helper() {
    }

    /**
     * Uploads the given file to the given server. Locally, a {@link DummyServerHostKeyVerifier} is used for server key
     * verification.
     *
     * @param localFile       the file to upload
     * @param server          the server to upload to
     * @param user            the user to login to the server
     * @param pass            the password to login to the server
     * @param remoteDirectory the directory to upload to
     * @param mode            the mode to apply to the new file
     * @throws IOException passed through
     */
    public static void upload(final String localFile, final String server, final String user, final String pass,
                              final String remoteDirectory, final String mode) throws IOException {
        final Connection conn = new Connection(server);
        conn.connect(new DummyServerHostKeyVerifier());
        conn.authenticateWithPassword(user, pass);
        final SCPClient client = new SCPClient(conn);
        client.put(localFile, remoteDirectory, mode);
    }

    /**
     * Downloads the given file from the given server. Locally, a {@link DummyServerHostKeyVerifier} is used for server
     * key verification.
     *
     * @param remoteFile     the file to download, including absolute path and name
     * @param server         the server to download it from
     * @param user           the user to login to the server
     * @param pass           the password to login to the server
     * @param localDirectory the local directory to download to
     * @throws IOException passed through
     */
    public static void download(final String remoteFile, final String server, final String user, final String pass,
                                final String localDirectory) throws IOException {
        final Connection conn = new Connection(server);
        conn.connect(new DummyServerHostKeyVerifier());
        conn.authenticateWithPassword(user, pass);
        final SCPClient client = new SCPClient(conn);
        client.get(remoteFile, localDirectory);
    }
}
