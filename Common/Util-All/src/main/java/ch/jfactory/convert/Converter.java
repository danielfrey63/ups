package ch.jfactory.convert;

import java.io.Reader;

/**
 * Interface hiding the implementation of a specific serializer.
 *
 * @author Daniel Frey 01.01.2010 14:35:29
 */
public interface Converter<T>
{
    T from( Reader reader );

    String from( T object );
}
