package ch.xmatrix.ups.pmb.exam.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * list resources available from the classpath
 *
 * @author stoughto
 */
public class ResourceList
{
    /**
     * This class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger( ResourceList.class );

    private static final String PATH_SEPARATOR = System.getProperty( "path.separator" );

    private static final String MANIFEST = "META-INF/MANIFEST.MF";

    /**
     * for all elements of java.class.path get a Collection of resources Pattern pattern = Pattern.compile(".*"); gets
     * all resources
     *
     * @param patterns the patterns to match
     * @return the resources in the order they are found
     * @throws IOException passed through
     */
    public static Map<Pattern, String> getResources( final Pattern... patterns ) throws IOException
    {
        LOG.info( "looking for patterns in classpath: " + Arrays.toString( patterns ) );
        final Map<Pattern, String> retval = new HashMap<Pattern, String>();
        final String classPath = getClassPath();
        final String[] classPathElements = classPath.split( PATH_SEPARATOR );
        LOG.debug( "classpath elements are " + Arrays.toString( classPathElements ) );
        for ( final String element : classPathElements )
        {
            final Map<Pattern, String> elements = getResources( element, patterns );
            for ( final Pattern pattern : elements.keySet() )
            {
                final String resource = elements.get( pattern );
                retval.put( pattern, resource );
            }
        }
        return retval;
    }

    private static String getClassPath() throws IOException
    {
        final String standardClassPath = System.getProperty( "java.class.path", "." );
        LOG.info( "start looking for additional classpath entries" );
        final Enumeration<URL> urls = ResourceList.class.getClassLoader().getResources( MANIFEST );
        String classPath = standardClassPath;
        while ( urls.hasMoreElements() )
        {
            final URL url = urls.nextElement();
            LOG.info( "adding to class extended classpath: " + url );
            classPath += PATH_SEPARATOR + getLocalJarFilename( url );
        }
        return classPath;
    }

    private static String getLocalJarFilename( final URL remoteManifestFileName )
    {
        LOG.info( "Trying to create local version of " + remoteManifestFileName );
        // remove trailing
        final String urlStrManifest = remoteManifestFileName.getFile();
        final String urlStrJar = urlStrManifest.substring( 0, urlStrManifest.length() - MANIFEST.length() - 2 );
        LOG.info( "Remote : " + urlStrJar );
        InputStream inputStreamJar = null;
        final File tempJar;
        FileOutputStream fosJar = null;
        try
        {
            final URL urlJar = new URL( urlStrJar );
            inputStreamJar = urlJar.openStream();
            String strippedName = urlStrJar;
            final int dotIndex = strippedName.lastIndexOf( '.' );
            if ( dotIndex >= 0 )
            {
                strippedName = strippedName.substring( 0, dotIndex );
                strippedName = strippedName.replace( "/", File.separator );
                strippedName = strippedName.replace( "\\", File.separator );
                final int slashIndex = strippedName.lastIndexOf( File.separator );
                if ( slashIndex >= 0 )
                {
                    strippedName = strippedName.substring( slashIndex + 1 );
                }
            }
            strippedName += "lib";
            tempJar = File.createTempFile( strippedName, ".jar" );
            LOG.info( "created temporary file '" + tempJar.getAbsolutePath() + "'" );
            tempJar.deleteOnExit();
            fosJar = new FileOutputStream( tempJar );
            final int bytesWritten = IOUtils.copy( inputStreamJar, fosJar );
            LOG.info( "written " + bytesWritten + " bytes to local file" );
            return tempJar.getAbsolutePath();
        }
        catch ( Exception ioe )
        {
            System.out.println( ioe.getMessage() );
            ioe.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly( inputStreamJar );
            IOUtils.closeQuietly( fosJar );
        }
        return null;
    }

    private static Map<Pattern, String> getResources( final String element, final Pattern... patterns )
    {
        final Map<Pattern, String> retval = new HashMap<Pattern, String>();
        final File file = new File( element );
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                LOG.info( "file system directory found: " + file );
                retval.putAll( getResourcesFromDirectory( file, patterns ) );
            }
            else
            {
                LOG.info( "JAR file found: " + file );
                retval.putAll( getResourcesFromJarFile( file, patterns ) );
            }
        }
        else
        {
            LOG.warn( "classpath entry does not exist: " + file );
        }
        return retval;
    }

    private static Map<Pattern, String> getResourcesFromJarFile( final File file, final Pattern... patterns )
    {
        final Map<Pattern, String> retval = new HashMap<Pattern, String>();
        final ZipFile zf;
        try
        {
            zf = new ZipFile( file );
        }
        catch ( ZipException e )
        {
            throw new Error( e );
        }
        catch ( IOException e )
        {
            throw new Error( e );
        }
        final Enumeration e = zf.entries();
        while ( e.hasMoreElements() )
        {
            final ZipEntry ze = (ZipEntry) e.nextElement();
            final String fileName = "/" + ze.getName();
            addResource( patterns, retval, fileName, "jar:file:/" + file.getAbsolutePath().replaceAll( "\\\\", "/" ) + "!" );
        }
        try
        {
            zf.close();
        }
        catch ( IOException e1 )
        {
            throw new Error( e1 );
        }
        return retval;
    }

    private static Map<Pattern, String> getResourcesFromDirectory( final File directory, final Pattern... patterns )
    {
        final Map<Pattern, String> retval = new HashMap<Pattern, String>();
        final File[] fileList = directory.listFiles();
        for ( final File file : fileList )
        {
            if ( file.isDirectory() )
            {
                retval.putAll( getResourcesFromDirectory( file, patterns ) );
            }
            else
            {
                try
                {
                    final String fileName = file.getCanonicalPath();
                    addResource( patterns, retval, fileName, "file:/" );
                }
                catch ( IOException e )
                {
                    throw new Error( e );
                }
            }
        }
        return retval;
    }

    private static void addResource( final Pattern[] patterns, final Map<Pattern, String> retval, final String fileName,
                                     final String prefix )
    {
        for ( final Pattern pattern : patterns )
        {
            if ( pattern.matcher( fileName ).matches() )
            {
                final String resources = retval.get( pattern );
                final String fullFileName = prefix + fileName;
                final String added = ( resources == null ? "" : resources + PATH_SEPARATOR ) + fullFileName;
                LOG.info( "adding resource " + fullFileName + " for pattern " + pattern );
                retval.put( pattern, added );
            }
        }
    }

    /**
     * list the resources that match args[0]
     *
     * @param args the patterns to match, or list all resources if there are no args
     * @throws IOException passed through
     */
    public static void main( final String[] args ) throws IOException
    {
        final Pattern[] patterns;
        if ( args.length < 1 )
        {
            patterns = new Pattern[]{Pattern.compile( ".*" )};
        }
        else
        {
            int counter = 0;
            patterns = new Pattern[args.length];
            for ( final String arg : args )
            {
                patterns[counter++] = Pattern.compile( arg );
            }
        }
        final Map<Pattern, String> map = ResourceList.getResources( patterns );
        for ( final Pattern pattern : map.keySet() )
        {
            final String resources = map.get( pattern );
            System.out.println( pattern + ": " );
            System.out.println( resources.replaceAll( PATH_SEPARATOR, "\n" ) );
        }
    }
}