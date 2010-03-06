package ch.jfactory.logging;

import org.apache.log4j.Logger;

/**
 * Implementation for Log4j.
 *
 * @author Daniel Frey 05.03.2010 09:45:21
 */
public class Log4jLogger implements LogInterface
{
    final Logger logger;

    public Log4jLogger( final String channel )
    {
        this.logger = Logger.getLogger( channel );
    }

    public void info( final Object message )
    {
        logger.info( message );
    }

    public void info( final Object message, final Throwable exception )
    {
        logger.info( message, exception );
    }

    public void warn( final Object message )
    {
        logger.warn( message );
    }

    public void warn( final Object message, final Throwable exception )
    {
        logger.warn( message, exception );
    }

    public void debug( final Object message )
    {
        logger.debug( message );
    }

    public void debug( final Object message, final Throwable exception )
    {
        logger.debug( message, exception );
    }

    public void error( final Object message )
    {
        logger.error( message );
    }

    public void error( final Throwable exception )
    {
        logger.error( exception );
    }

    public void error( final Object message, final Throwable exception )
    {
        logger.error( message, exception );
    }

    public void fatal( final Object message )
    {
        logger.fatal( message );
    }

    public void fatal( final Throwable exception )
    {
        logger.fatal( exception );
    }

    public void fatal( final Object message, final Throwable exception )
    {
        logger.fatal( message, exception );
    }

    public boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    public boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }
}
