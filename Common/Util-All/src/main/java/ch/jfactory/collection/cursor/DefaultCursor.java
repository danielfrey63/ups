package ch.jfactory.collection.cursor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default Implementation of the Cursor Interface. The implementation is capable to provide cursor implementation by
 * initialize with an array or a list.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
public class DefaultCursor<T> implements Cursor<T>
{
    private final Cursor<T> emptyCursor = new ListCursor<T>( new ArrayList<T>() );

    private final Cursor<T> cursor;

    public DefaultCursor()
    {
        cursor = emptyCursor;
    }

    /**
     * Construct a Cursor depending on a List. If the parameter is null, an empty cursor will be constructed
     *
     * @param list list for the cursor
     */
    public DefaultCursor( final List<T> list )
    {
        if ( list == null )
        {
            cursor = emptyCursor;
        }
        else
        {
            cursor = new ListCursor<T>( list );
        }
    }

    /**
     * Construct a Cursor depending on an array. If the parameter is null, an empty cursor will be constructed
     *
     * @param array an array of objects
     */
    public DefaultCursor( final T[] array )
    {
        if ( array == null )
        {
            cursor = emptyCursor;
        }
        else
        {
            cursor = new ArrayCursor<T>( array );
        }
    }

    public T next()
    {
        return cursor.next();
    }

    public T previous()
    {
        return cursor.previous();
    }

    public boolean hasNext()
    {
        return cursor.hasNext();
    }

    public boolean hasPrevious()
    {
        return cursor.hasPrevious();
    }

    public int getCurrentIndex()
    {
        return cursor.getCurrentIndex();
    }

    public int getSize()
    {
        return cursor.getSize();
    }

    public T getCurrent()
    {
        return cursor.getCurrent();
    }

    public void setCurrent( final T obj )
    {
        cursor.setCurrent( obj );
    }

    /**
     * check if the cursor is empty.
     *
     * @return true if cursor is empty
     */
    public boolean isEmpty()
    {
        return cursor.isEmpty();
    }

    public Iterator<T> getIterator()
    {
        return cursor.getIterator();
    }
}
