package ch.xmatrix.ups.pmb.exam;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 30.06.2008 23:01:43
 */
public class ConfigurationException extends Exception
{
    public ConfigurationException()
    {
    }

    public ConfigurationException( final String message )
    {
        super( message );
    }

    public ConfigurationException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public ConfigurationException( final Throwable cause )
    {
        super( cause );
    }
}
