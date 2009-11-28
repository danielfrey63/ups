/*
 * JarUpdateModule.java
 *
 * Created on 24. September 2002, 16:26
 */

package ch.jfactory.update;

import ch.jfactory.file.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
class JarUpdateModule implements UpdateModule
{
    private static final Logger LOGGER = Logger.getLogger( JarUpdateModule.class );

    private VersionInfo localVersion = null;

    private VersionInfo serverVersion = null;

    private File tempLocation = null;

    public JarUpdateModule( final VersionInfo serverVersion, final VersionInfo localVersion )
    {
        this.serverVersion = serverVersion;
        this.localVersion = localVersion;
    }

    /**
     * Creates a new instance of JarUpdateModule
     */
    public JarUpdateModule()
    {
    }

    /**
     * this method return the version information of the local installed version of the fragment.
     *
     * @return version information
     */
    public VersionInfo getLocalVersionInfo()
    {
        return localVersion;
    }

    /**
     * this method return the version information about the update version.
     *
     * @return version information();
     */
    public VersionInfo getServerVersionInfo()
    {
        return serverVersion;
    }

    /**
     * This method should be overwritten to implement the update behavior. Maybe download the jar file and overwrite the
     * local version.
     */
    public void update() throws IOException
    {
        String localLocation = "";
        if ( getLocalVersionInfo() != null )
        {
            localLocation = getLocalVersionInfo().getLocation();
        }
        else
        {
            localLocation = getNewModuleFileName();
        }

        copyFromServer();
        replaceLocalVersion( localLocation );
    }

    private void copyFromServer() throws IOException
    {
        final String serverLocation = getServerVersionInfo().getLocation();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try
        {
            tempLocation = File.createTempFile( "HerbarUpdate", ".jar" );

            if ( LOGGER.isDebugEnabled() )
            {
                LOGGER.debug( "copy jar file from " + serverLocation + " to temporary location " + tempLocation );
            }

            inputStream = new BufferedInputStream( new URL( serverLocation ).openStream() );
            outputStream = new BufferedOutputStream( new FileOutputStream( tempLocation ) );
            final byte[] buffer = new byte[4096];
            int len;
            while ( ( len = inputStream.read( buffer, 0, buffer.length ) ) != -1 )
            {
                outputStream.write( buffer, 0, len );
            }
        }
        finally
        {
            closeStream( inputStream );
            closeStream( outputStream );
        }
    }

    private void replaceLocalVersion( final String localLocation )
    {
        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( "rename temporary file " + tempLocation + " to original location " + localLocation );
        }
        final File localFile = new File( localLocation );
        // renameTo moves platform dependent and only on same volume!
        if ( !tempLocation.renameTo( localFile ) )
        {
            FileUtils.copyFile( tempLocation, localFile );
        }
    }

    static private void closeStream( final InputStream inputStream )
    {
        try
        {
            if ( inputStream != null )
            {
                inputStream.close();
            }
        }
        catch ( IOException ex )
        {
            LOGGER.error( "failed to close InputStream", ex );
        }
    }

    static private void closeStream( final OutputStream outputStream )
    {
        try
        {
            if ( outputStream != null )
            {
                outputStream.close();
            }
        }
        catch ( IOException ex )
        {
            LOGGER.error( "failed to close OutputStream", ex );
        }
    }

    private String getNewModuleFileName()
    {
        return System.getProperty( "user.dir" ) + getServerVersionInfo().getRelativeName();
    }
}
