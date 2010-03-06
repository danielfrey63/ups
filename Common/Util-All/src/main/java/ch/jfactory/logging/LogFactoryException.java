package ch.jfactory.logging;

/**
 * Exception thrown by the LogFactory.
 *
 * @author Daniel Frey 05.03.2010 09:52:59
 */
public class LogFactoryException extends Exception
{
    public LogFactoryException( final String message, final Throwable cause )
    {
        super( message, cause );
    }
}
