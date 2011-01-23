package ch.jfactory.component;

import javax.swing.JMenuItem;

/**
 * Menu item wrapping an object.
 *
 * @author Daniel Frey 23.01.11 12:07
 */
public class ObjectMenuItem<T> extends JMenuItem
{
    private final T object;

    public ObjectMenuItem( final T object )
    {
        super( object.toString() );
        this.object = object;
    }

    public T getObject()
    {
        return object;
    }
}
