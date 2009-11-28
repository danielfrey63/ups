package ch.jfactory.application.view.status;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/06 10:16:23 $
 */
public interface Message
{
    String getText();

    Type getType();

    public enum Type
    {
        FATAL, ERROR, WARN, INFO, VERBOSE, DEBUG
    }
}
