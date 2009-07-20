package ch.jfactory.jar;

import ch.jfactory.jar.sign.JarSigner;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import sun.tools.jar.Main;

/**
 * Utilities to handle JARs.
 *
 * @author Daniel Frey 27.06.2008 09:14:42
 */
public final class JarHelper {

    /** This class logger. */
    private static final Logger LOG = Logger.getLogger(JarHelper.class);

    /** Hide constructor. */
    private JarHelper() {
    }

    /**
     * Builds a jar file.
     *
     * @param jar      the full path and name of the JAR file to build
     * @param dir      the directory to which to change in order to pack the files. They will be packed relatively to
     *                 that directory
     * @param filename the name of the file within the JAR
     * @throws Exception passed through
     */
    public static void build(final String jar, final File dir, final String filename) throws Exception {
        final String[] args = new String[]{"cf", jar, "-C", dir.getAbsolutePath(), filename};
        final ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        final PrintStream out = new PrintStream(outBytes);
        final ByteArrayOutputStream errBytes = new ByteArrayOutputStream();
        final PrintStream err = new PrintStream(errBytes);
        new Main(out, err, "jar").run(args);
        final String errString = errBytes.toString();
        if (errString != null && !"".equals(errString)) {
            LOG.error("problem during building of jar: " + errString);
            throw new Exception(errString);
        }
        final String outString = outBytes.toString();
        if (outString != null && !"".equals(outString)) {
            LOG.info(outString);
        }
    }

    /**
     * Signs the given JAR file.
     *
     * @param file     the JAR file
     * @param keystore the keystore containing the certificate to sign it with
     * @param alias    the keystore alias
     * @param pass     the keystore password
     * @throws Exception passed through
     */
    public static void sign(final String file, final String keystore, final String alias, final String pass) throws Exception {
        new JarSigner().run(new String[]{"-keystore ", keystore, "-storepass ", pass, file, alias});
        LOG.info("jar " + file + " successfully signed");
    }
}
