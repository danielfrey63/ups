/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.7 $ $Date: 2008/01/06 10:16:23 $
 */
public class FileUtils
{
    private static final Logger LOG = LoggerFactory.getLogger( FileUtils.class );

    private static final boolean INFO = LOG.isInfoEnabled();

    private static final boolean DEBUG = LOG.isDebugEnabled();

    /**
     * Copies one file into another one. The method also works across volumes. The destination file exists, the new content will be added to it.
     *
     * @param src  File to copy from
     * @param dest File to copy into
     */
    public static void copyFile( final File src, final File dest )
    {
        BufferedOutputStream myFos = null;
        BufferedInputStream myFis = null;
        try
        {
            // Open a stream to read in bytes from the file FileInputStream myFis = new FileInputStream(src);
            myFis = new BufferedInputStream( new FileInputStream( src ) );

            // Make a destination byte stream to your copy
            if ( !dest.exists() )
            {
                dest.createNewFile();
            }
            myFos = new BufferedOutputStream( new FileOutputStream( dest ) );

            final byte[] buffer = new byte[1000];
            int len;
            while ( ( len = myFis.read( buffer ) ) != -1 )
            {
                myFos.write( buffer, 0, len );
            }

            myFis.close();
            myFos.flush();
            myFos.close();
        }
        catch ( IOException e )
        {
            try
            {
                if ( myFis != null )
                {
                    myFis.close();
                }
            }
            catch ( IOException ex )
            {
                LOG.error( "failed to close InputStream", ex );
            }
            try
            {
                if ( myFos != null )
                {
                    myFos.close();
                }
            }
            catch ( IOException ex )
            {
                LOG.error( "failed to close OutputStream", ex );
            }
        }
    }

    public static void extractFileFromUrl( final URL url, final File destinationDirectory ) throws IOException
    {
        final String path = url.getPath();
        final String pathWithinJar = path.substring( path.indexOf( "!" ) + 1 );
        final File destinationFile = new File( destinationDirectory, pathWithinJar );
        destinationFile.getParentFile().mkdirs();
        final InputStream in = url.openStream();
        final FileOutputStream out = new FileOutputStream( destinationFile );
        IOUtils.copy( in, out );
        IOUtils.closeQuietly( out );
        IOUtils.closeQuietly( in );
    }

    /**
     * Returns a temporary directory in the temp space and creates it.
     *
     * @param prefix  the prefix as in {@link File#createTempFile(String, String)}
     * @param postfix the postfix as in {@link File#createTempFile(String, String)}
     * @return the direcotry file
     * @throws IOException passed through
     */
    public static File createTemporaryDirectory( final String prefix, final String postfix ) throws IOException
    {
        final File tempFile = File.createTempFile( prefix, postfix );
        tempFile.delete();
        tempFile.mkdirs();
        return tempFile;
    }

    /**
     * Extracts the file path information from a given URL. URL encoded paths will be decoded, i.e. a <code>%20</code> will be replaced by a space.
     *
     * @param u URL to extract path from
     * @return path String
     */
    public static String extractPathFromURL( final URL u )
    {
        final String eu = u.toExternalForm();
        return extractPathFromURL( eu );
    }

    /**
     * Extracts the file path information from a given URL String. Strings with URL encoded paths will be decoded, i.e. a <code>%20</code>  will be replaced by a space.
     *
     * @param s URL to extract path from
     * @return path String
     */
    public static String extractPathFromURL( String s )
    {
        if ( s.startsWith( "jar:file:" ) )
        {
            s = s.substring( 9 );
            final int n = s.indexOf( "!" );
            if ( n > -1 )
            {
                s = s.substring( 0, n );
            }
        }
        try
        {
            s = URLDecoder.decode( s, "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            LOG.error( e.getMessage(), e );
        }
        return s;
    }

    /**
     * Scans the system property java.class.path for files.
     *
     * @param filter to filter files
     * @return a collection of strings of absolute pathes
     */
    public static Collection getFilesFromClasspath( final StringFilter filter ) throws IOException
    {
        final ArrayList files = new ArrayList();
        final String classpath = System.getProperty( "java.class.path" );
        final String separator = System.getProperty( "path.separator" );
        final String[] paths = classpath.split( separator );
        for ( final String path : paths )
        {
            if ( path.endsWith( ".jar" ) || path.endsWith( ".zip" ) )
            {
                if ( new File( path ).exists() )
                {
                    files.addAll( getFilesFromJar( path, filter ) );
                }
                else
                {
                    LOG.warn( "file not found: " + path );
                }
            }
            else
            {
                files.addAll( getFilesFromDirectory( path, filter ) );
            }
        }
        return files;
    }

    /**
     * Scans a jar for files.
     *
     * @param jar    the jar to scan
     * @param filter the filter to apply
     * @return a collection of strings of absolute pathes
     * @throws IOException
     */
    public static Collection getFilesFromJar( final String jar, final StringFilter filter ) throws IOException
    {
        final ArrayList files = new ArrayList();
        final JarFile file = new JarFile( jar );
        final Enumeration e = file.entries();
        while ( e.hasMoreElements() )
        {
            final JarEntry entry = (JarEntry) e.nextElement();
            final String name = entry.getName();
            final String url = "jar:file:/" + jar.replace( "\\", "/" ) + "!/" + name;
            if ( filter.accept( url ) )
            {
                files.add( url );
            }
        }
        return files;
    }

    /**
     * Scans a directory for files.
     *
     * @param path   the directory to scan
     * @param filter filter to apply
     * @return a collection of string of realtive pathes
     */
    public static Collection getFilesFromDirectory( final String path, final StringFilter filter )
    {
        final ArrayList files = new ArrayList();
        final File pathFile = new File( path );
        if ( pathFile.exists() )
        {
            files.addAll( getFileFromDirectory( pathFile, pathFile, filter ) );
        }
        return files;
    }

    /**
     * Scans recursively for files.
     *
     * @param directory the directory to scan
     * @param filter    the filter to apply
     * @return a collection of strings of relative pathes
     */
    private static Collection getFileFromDirectory( final File rootPath, final File directory, final StringFilter filter )
    {
        final ArrayList files = new ArrayList();
        final File[] paths = directory.listFiles();
        if ( paths != null )
        {
            for ( final File file : paths )
            {
                if ( filter.accept( file.getAbsolutePath() ) )
                {
                    if ( file.isFile() )
                    {
                        final String path = file.getAbsolutePath().replace( '\\', '/' );
                        if ( file.exists() )
                        {
                            files.add( path.substring( rootPath.getAbsolutePath().length() ) );
                        }
                        else
                        {
                            LOG.warn( "file not found: " + path );
                        }
                    }
                    else
                    {
                        files.addAll( getFileFromDirectory( rootPath, file, filter ) );
                    }
                }
            }
        }
        return files;
    }

    /**
     * Saves the given content to the system by locating it throu the classloder. There are two supported ways. In both ways the location of the destination is derived from the resource given, loaded by the classloader:
     *
     * <ul>
     *
     * <li>The given resource points to a file in the file system.</li>
     *
     * <li>The given resoruce points to a jar in the file system.</li>
     *
     * </ul>
     *
     * @param content  the content to be stored
     * @param resource the resource for which the url is derived from the classloader
     */
    public static void saveResource( final String content, final String resource )
    {
        final URL url = FileUtils.class.getResource( resource );
        if ( INFO )
        {
            LOG.info( "saving content to " + resource + " at " + url );
        }
        if ( DEBUG )
        {
            LOG.debug( "content is\n" + content );
        }
        try
        {
            final String protocol = url.getProtocol();
            final URI uri = new URI( url.toExternalForm() );
            if ( protocol.equals( "jar" ) )
            {
                final String[] parts = url.getFile().split( "!/" );
                ZipUtils.zip( content.getBytes(), new File( parts[0].substring( "file:/".length() ) ), false, true, parts[1] );
            }
            else if ( protocol.equals( "file" ) )
            {
                final FileWriter file = new FileWriter( new File( uri ) );
                file.write( content );
                file.close();
            }
            else
            {
                throw new IllegalStateException( "protocol " + protocol + " not handled" );
            }
        }
        catch ( IOException e )
        {
            LOG.error( "error while trying to access " + url, e );
        }
        catch ( URISyntaxException e )
        {
            LOG.error( "problem with url " + url, e );
        }
    }

    public static void writePropertyToXML( final String filePath, final String property, final String value ) throws IOException
    {
        final Properties props = new Properties();
        props.put( property, value );
        final FileOutputStream outputStream = new FileOutputStream( new File( filePath ) );
        props.storeToXML( outputStream, "" );
        outputStream.close();
    }

    public static Object readPropertyFromXML( final String filePath, final String property ) throws IOException
    {
        File file = new File( filePath );
        if ( file.exists() )
        {
            final Properties props = new Properties();
            final FileInputStream inputStream = new FileInputStream( filePath );
            props.loadFromXML( inputStream );
            inputStream.close();
            return props.get( property );
        }
        return null;
    }

    public static interface StringFilter
    {
        boolean accept( String filter );
    }
}
