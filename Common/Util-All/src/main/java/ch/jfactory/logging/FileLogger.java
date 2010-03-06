package ch.jfactory.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.abbreviate;
import static org.apache.commons.lang.StringUtils.leftPad;
import static org.apache.commons.lang.StringUtils.rightPad;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 05.03.2010 16:01:47
 */
public class FileLogger implements LogInterface
{
    private final String channel;

    private static final Format FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    public FileLogger( final String channel )
    {
        this.channel = leftPad( abbreviate( channel, 200, 20 ), 22 );
    }

    public void info( final Object message )
    {
        printMessage( message, "INFO" );
    }

    public void info( final Object message, final Throwable exception )
    {
        printMessage( message, "INFO" );
        exception.printStackTrace();
    }

    public void debug( final Object message )
    {
        printMessage( message, "WARN" );
    }

    public void debug( final Object message, final Throwable exception )
    {
        printMessage( message, "WARN" );
        exception.printStackTrace();
    }

    public void warn( final Object message )
    {
        printMessage( message, "WARN" );
    }

    public void warn( final Object message, final Throwable exception )
    {
        printMessage( message, "WARN" );
        exception.printStackTrace();
    }

    public void error( final Object message )
    {
        printMessage( message, "ERROR" );
    }

    public void error( final Throwable exception )
    {
        exception.printStackTrace();
    }

    public void error( final Object message, final Throwable exception )
    {
        printMessage( message, "ERROR" );
        exception.printStackTrace();
    }

    public void fatal( final Object message )
    {
        printMessage( message, "FATAL" );
    }

    public void fatal( final Throwable exception )
    {
        exception.printStackTrace();
    }

    public void fatal( final Object message, final Throwable exception )
    {
        printMessage( message, "FATAL" );
        exception.printStackTrace();
    }

    public boolean isInfoEnabled()
    {
        return true;
    }

    public boolean isDebugEnabled()
    {
        return false;
    }

    private void printMessage( final Object message, final String level )
    {
        try
        {
            final FileWriter file = new FileWriter( "User/danielfrey/debug.txt" );
            file.write( getMessage( message, level ) );
            file.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private String getMessage( final Object message, final String level )
    {
        return FORMAT.format(new Date()) + "  " + channel + "  " + rightPad( "[" + level + "]", 8 ) + message;
    }
}