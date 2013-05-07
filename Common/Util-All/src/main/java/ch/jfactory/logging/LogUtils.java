/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils
{
    protected final static Logger LOGGER;

    public static final Calendar STARTTIME;

    public static final String USERHOME;

    private static final String RET = System.getProperty( "line.separator" );

    public static StringBuffer startupLogBuffer = new StringBuffer();

    public static void init()
    {
    }

    /**
     * Opens a stream for the given file. Looks for the file at the following locations: <ol> <li>in any JAR file given in the class path. Relative file names are prepended with a slash.</li> <li>in the start directory</li> <li>in the default directory</li> </ol>
     *
     * @param propFile the file name, may be preceeded by relative or absolute path
     * @return stream to the file
     */
    public static InputStream locateResourceAsStream( final String propFile )
    {
        // first try to load properties from jar (or root directory on linux), then from other file system locations
        final String homeDir = System.getProperty( "user.home" ).replace( '\\', '/' ) + "/.hcd2/";
        InputStream is = LogUtils.class.getResourceAsStream( "/" + propFile );
        new File( homeDir ).mkdir();
        final String[] files = {"/" + propFile, "./" + propFile, homeDir + propFile, propFile};
        int counter = 0;
        while ( is == null && counter < files.length )
        {
            final String file = files[counter++];
            startupLogBuffer.append( "trying to locate resource in " ).append( file ).append( RET );
            try
            {
                is = new FileInputStream( file );
            }
            catch ( FileNotFoundException fnfe )
            {
                startupLogBuffer.append( file ).append( " not found" ).append( RET );
            }
            if ( is != null )
            {
                startupLogBuffer.append( "found " ).append( file ).append( RET );
            }
        }
        return is;
    }

    static
    {
        STARTTIME = new GregorianCalendar();
        USERHOME = System.getProperty( "user.home" ) + "/.hcd2";

        final File userDir = new File( USERHOME );
        if ( !userDir.exists() )
        {
            userDir.mkdir();
        }
        else if ( userDir.isFile() )
        {
            System.err.println( startupLogBuffer );
            throw new IllegalStateException( "There exists a file " + USERHOME
                    + " which makes it impossible to make a directory of the same name" );
        }

        final String logDirName = USERHOME + "/log";
        final File logDir = new File( logDirName );
        if ( !logDir.exists() )
        {
            logDir.mkdir();
        }
        else if ( logDir.isFile() )
        {
            System.err.println( startupLogBuffer );
            throw new IllegalStateException( "There exists a file " + logDirName
                    + " which makes it impossible to make a directory of the same name" );
        }

        // add main properties to existing ones
        final String propFile = System.getProperty( "xmatrix.config", "xmatrix.properties" );
        if ( propFile == null )
        {
            throw new NullPointerException( "Property " + propFile + " does not point to a configuration file." );
        }
        startupLogBuffer.append( "looking for configuration properties " ).append( propFile ).append( RET );
        InputStream is = locateResourceAsStream( propFile );
        if ( is == null )
        {
            startupLogBuffer.append( propFile ).append( " not found, trying with xmatrix.properties" );
            is = locateResourceAsStream( "xmatrix.properties" );
        }
        final Properties props;
        try
        {
            // make sure command line properties overwrite configuration file properties
            props = new Properties();
            props.load( is );
            for ( final Object key : props.keySet() )
            {
                final String value = props.getProperty( (String) key );
                String parsed = value;
                String variable = "\\$\\{.*?\\}";
                final Matcher matcher = Pattern.compile(variable).matcher( value );

                while ( matcher.find() )
                {
                    final String property = value.substring( matcher.start() + 2, matcher.end() - 1 );
                    final String valueToReplace = System.getProperty( property );
                    if (valueToReplace != null) {
                        parsed = parsed.replaceFirst(variable, valueToReplace.replaceAll( "\\\\", "/" ) );
                    }
                }
                System.setProperty( (String) key, parsed );
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            throw new RuntimeException( startupLogBuffer.toString() );
        }

        LOGGER = LoggerFactory.getLogger( LogUtils.class );

        // log system properties
        final StringWriter sw = new StringWriter();
        props.list( new PrintWriter( sw ) );
        LOGGER.info( "System properties are:\n" + sw );
    }
}
