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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public class ZipUtils
{
    private static final Logger LOG = LoggerFactory.getLogger( ZipUtils.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    /**
     * Unzips the given archive into the given directory, reconstructing the directory structure in the zip file. If the archive file name is null, nothing happens. If the destination directory does not exist, it will be created.
     *
     * @param archiveFileName The name of the file to unzip
     * @param destinationDir  The name of the destination directory
     * @throws IOException see {@link ZipFile#ZipFile(String)} for exception cases
     */
    public static void unZip( final String archiveFileName, String destinationDir ) throws IOException
    {
        // take no action if archive file is null
        if ( archiveFileName == null )
        {
            return;
        }
        if ( DEBUG )
        {
            LOG.debug( "trying to unzip " + archiveFileName + " to " + destinationDir );
        }

        // validate path to destination directory
        destinationDir = ( destinationDir.endsWith( "/" ) ? destinationDir : destinationDir + "/" );
        final File destinationDirectory = new File( destinationDir );
        if ( !destinationDirectory.exists() )
        {
            final boolean created = destinationDirectory.mkdirs();
            if ( !created )
            {
                LOG.warn( "cannot create directory " + destinationDirectory );
            }
        }

        // open zip and extract
        final ZipFile zip = new ZipFile( archiveFileName );
        unZip( zip, destinationDir );
    }

    public static void unZip( final ZipFile zip, final String destinationDir )
    {
        final Enumeration entries = zip.entries();
        while ( entries.hasMoreElements() )
        {
            final ZipEntry entry = (ZipEntry) entries.nextElement();
            if ( DEBUG )
            {
                LOG.debug( "extracting " + entry );
            }
            final String destination = destinationDir + entry.getName();
            if ( destination.endsWith( "/" ) )
            {
                final boolean created = new File( destination ).mkdirs();
                if ( !created )
                {
                    LOG.warn( "cannot create directory " + destination );
                }
            }
            else
            {
                try
                {
                    final String parent = new File( destination ).getParent();
                    final boolean created = new File( parent ).mkdirs();
                    if ( !created )
                    {
                        LOG.warn( "cannot create directory " + parent );
                    }
                    final FileOutputStream os = new FileOutputStream( destination );
                    final InputStream is = zip.getInputStream( entry );
                    int chunkLength;
                    final byte[] buffer = new byte[8092];
                    while ( ( chunkLength = is.read( buffer ) ) > 0 )
                    {
                        os.write( buffer, 0, chunkLength );
                    }
                    is.close();
                    os.close();
                }
                catch ( FileNotFoundException e )
                {
                    LOG.error( "Error occured while extracting to " + destination, e );
                }
                catch ( IOException e )
                {
                    LOG.error( "Error occured while opening zip entry " + entry, e );
                }
            }
        }
    }

    /**
     * Zips a given file into a zip file. The entry will be stored in the files name.
     *
     * @param file           the file to add to the zip.
     * @param zip            the zip file to add the file to.
     * @param overwriteZip   if set to <code>true</code> overwrite existing zip file. If set to <code>false</code> the content will be added to the existing zip file.
     * @param overwriteEntry if set to <code>true</code> overwrite existing zip entries. If set to <cdoe>false</code> a ZipException will be thrown if the entry exists.
     * @throws ZipException if the zip entry exists and <code>overwriteEntry</code> is set to false.
     */
    public static void zip( final File file, final File zip, final boolean overwriteZip, final boolean overwriteEntry )
            throws ZipException
    {
        zip( file, zip, overwriteZip, overwriteEntry, file.getName() );
    }

    /**
     * Zips a given file into a zip file.
     *
     * @param file           the file to add to the zip.
     * @param zip            the zip file to add the file to.
     * @param overwriteZip   if set to <code>true</code> overwrite existing zip file. If set to <code>false</code> the content will be added to the existing zip file.
     * @param overwriteEntry if set to <code>true</code> overwrite existing zip entries. If set to <cdoe>false</code> a ZipException will be thrown if the entry exists.
     * @param relativePath   the zip entries relative path (including file name)
     * @throws ZipException if the zip entry exists and <code>overwriteEntry</code> is set to false.
     */
    public static void zip( final File file, final File zip, final boolean overwriteZip,
                            final boolean overwriteEntry, final String relativePath ) throws ZipException
    {
        try
        {
            final StringWriter writer = new StringWriter();
            final FileReader reader = new FileReader( file );
            IOUtils.copy( reader, writer );
            final String input = writer.toString();
            writer.close();
            reader.close();

            zip( input.getBytes(), zip, overwriteZip, overwriteEntry, relativePath );
        }
        catch ( ZipException e )
        {
            throw e;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Zips a given data into a zip file.
     *
     * @param input          the data to add to the zip.
     * @param zip            the zip file to add the data to.
     * @param overwriteZip   if set to <code>true</code> overwrite existing zip file. If set to <code>false</code> the content will be added to the existing zip file.
     * @param overwriteEntry if set to <code>true</code> overwrite existing zip entries. If set to <cdoe>false</code> a ZipException will be thrown if the entry exists.
     * @param relativePath   the zip entries relative path (including file name)
     * @throws ZipException if the zip entry exists and <code>overwriteEntry</code> is set to false.
     */
    public static void zip( final byte[] input, final File zip, final boolean overwriteZip,
                            final boolean overwriteEntry, final String relativePath ) throws ZipException
    {
        try
        {
            final byte[] buf = new byte[1024];
            final ZipOutputStream zipOutpuStream;

            // Copy existing zip contents into new zip file
            if ( !overwriteZip && zip.exists() )
            {
                final File tempZip = File.createTempFile( "tmpZipper", ".zip" );
                FileUtils.copyFile( zip, tempZip );
                final ZipInputStream tempIn = new ZipInputStream( new FileInputStream( tempZip ) );
                ZipEntry tempZipEntry;
                zipOutpuStream = new ZipOutputStream( new FileOutputStream( zip ) );
                while ( ( tempZipEntry = tempIn.getNextEntry() ) != null )
                {
                    int len;
                    if ( !( overwriteEntry && tempZipEntry.getName().equals( relativePath ) ) )
                    {
                        zipOutpuStream.putNextEntry( tempZipEntry );
                        while ( ( len = tempIn.read( buf, 0, 1024 ) ) > 0 )
                        {
                            zipOutpuStream.write( buf, 0, len );
                        }
                    }
                }
                tempIn.close();
                final boolean deleted = tempZip.delete();
                if ( !deleted )
                {
                    LOG.warn( "cannot delete temporary zip file " + tempZip );
                }
            }
            else
            {
                zipOutpuStream = new ZipOutputStream( new FileOutputStream( zip ) );
            }

            // Add new file
            zipOutpuStream.putNextEntry( new ZipEntry( relativePath ) );
            final ByteArrayInputStream byteInput = new ByteArrayInputStream( input );
            IOUtils.copy( byteInput, zipOutpuStream );
            zipOutpuStream.closeEntry();
            zipOutpuStream.close();
            IOUtils.closeQuietly( byteInput );
        }
        catch ( ZipException e )
        {
            throw e;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void zipDirectory( final File directory, final File zip ) throws IOException
    {
        final ZipOutputStream zos = new ZipOutputStream( new FileOutputStream( zip ) );
        zip( directory, directory, zos );
        zos.close();
    }

    private static void zip( final File directory, final File base, final ZipOutputStream zos ) throws IOException
    {
        final File[] files = directory.listFiles();
        final byte[] buffer = new byte[8192];
        int read;
        for ( int i = 0, n = files.length; i < n; i++ )
        {
            if ( files[i].isDirectory() )
            {
                zip( files[i], base, zos );
            }
            else
            {
                final FileInputStream in = new FileInputStream( files[i] );
                final ZipEntry entry = new ZipEntry( files[i].getPath().substring( base.getPath().length() + 1 ) );
                zos.putNextEntry( entry );
                while ( -1 != ( read = in.read( buffer ) ) )
                {
                    zos.write( buffer, 0, read );
                }
                in.close();
            }
        }
    }

    public static void main( final String[] args ) throws IOException
    {
        final File zip = new File( "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/db.jar" );
        unZip( zip.getAbsolutePath(), "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/Temp" );
    }
}
