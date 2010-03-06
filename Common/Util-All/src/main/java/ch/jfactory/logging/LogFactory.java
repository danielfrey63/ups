package ch.jfactory.logging;

import java.lang.reflect.Constructor;

/**
 * Interface to abstract logging.
 *
 * @author Daniel Frey 05.03.2010 09:39:39
 */
public class LogFactory
{
    private final static String type = "ch.jfactory.logging.SystemLogger";

    private static final String MESSAGE = "cannot initialize log interface";

    public static LogInterface getLogger( final Class channelClass )
    {
        return getLogger( channelClass.getName() );
    }

    public static LogInterface getLogger( final String channel )
    {
        try
        {
            final Class logClass = Class.forName( type );
            final Constructor<LogInterface> clazz = logClass.getConstructor( String.class );
            return clazz.newInstance( channel );
        }
        catch ( Throwable e )
        {
            System.err.println( MESSAGE );
            e.printStackTrace();
            throw new IllegalStateException( MESSAGE, e );
        }
    }
}
