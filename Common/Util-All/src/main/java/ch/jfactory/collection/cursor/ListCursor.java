package ch.jfactory.collection.cursor;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for list cursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
class ListCursor<T> implements Cursor<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ListCursor.class );

    private final List<T> list;

    private final ListIterator<T> iterator;

    private T currentObject = null;

    public ListCursor( final List<T> list )
    {
        this.list = list;
        this.iterator = list.listIterator();
        this.currentObject = iterator.hasNext() ? iterator.next() : null;
    }

    public T next()
    {
        currentObject = iterator.next();
        return currentObject;
    }

    public T previous()
    {
        iterator.previous();
        currentObject = iterator.previous();
        iterator.next();

        return currentObject;
    }

    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    public boolean hasPrevious()
    {
        return ( iterator.previousIndex() != 0 && iterator.hasPrevious() );
    }

    public int getCurrentIndex()
    {
        return iterator.nextIndex() == 0 ? 0 : iterator.nextIndex() - 1;
    }

    public int getSize()
    {
        return list.size();
    }

    public T getCurrent()
    {
        return currentObject;
    }

    public void setCurrent( final T obj )
    {
        final int idx = list.indexOf( obj );
        if ( idx >= 0 )
        {
            currentObject = obj;
            while ( getCurrentIndex() < idx )
            {
                iterator.next();
            }
            while ( getCurrentIndex() > idx )
            {
                iterator.previous();
            }
        }
        else
        {
            LOGGER.error( "Error setting Current Object to " + obj );
        }
    }

    /**
     * check if the cursor is empty.
     *
     * @return true if cursor is empty
     */
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    public Iterator<T> getIterator()
    {
        return list.iterator();
    }
}
