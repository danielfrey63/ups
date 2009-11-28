package ch.xmatrix.ups.pmb.exam.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 05.08.2008 23:26:50
 */
public class RegexTransformer
{
    public static void main( final String[] args ) throws IOException
    {
        final String fileName = "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/debug/Test-list.txt";
        final BufferedReader reader = new BufferedReader( new FileReader( fileName ) );
        final FileWriter out = new FileWriter( "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/debug/Test-list2.txt" );
        String line;
        String lastLine = null;
        while ( ( line = reader.readLine() ) != null )
        {
            if ( !line.equals( lastLine ) )
            {
                out.write( line );
                out.write( "\n" );
                lastLine = line;
            }
        }
        IOUtils.closeQuietly( reader );
        IOUtils.closeQuietly( out );
    }
}
