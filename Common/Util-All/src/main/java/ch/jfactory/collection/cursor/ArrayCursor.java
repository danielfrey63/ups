package ch.jfactory.collection.cursor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for array cursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class ArrayCursor<T> implements Cursor<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ArrayCursor.class );

    private final T[] objects;

    private int currentIndex = 0;

    public ArrayCursor( final T[] array )
    {
        this.objects = array;
    }

    public T next()
    {
        currentIndex = ( currentIndex < getSize() ) ? ++currentIndex : getSize();
        return objects[currentIndex];
    }

    public T previous()
    {
        currentIndex = ( currentIndex == 0 ) ? 0 : --currentIndex;
        return objects[currentIndex];
    }

    public boolean hasNext()
    {
        return ( currentIndex < getSize() - 1 );
    }

    public boolean hasPrevious()
    {
        return ( currentIndex > 0 );
    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public int getSize()
    {
        return objects == null ? 0 : objects.length;
    }

    public T getCurrent()
    {
        if ( objects == null )
        {
            return null;
        }
        if ( currentIndex >= objects.length )
        {
            return null;
        }
        if ( currentIndex < 0 )
        {
            return null;
        }
        return objects[currentIndex];
    }

    public void setCurrent( final T obj )
    {
        for ( int i = 0; i < getSize(); i++ )
        {
            if ( objects[i] == obj )
            {
                currentIndex = i;
                return;
            }
        }
        LOGGER.info( "Error setting Current Object to " + obj );
    }

    /**
     * check if the cursor is empty.
     *
     * @return true if cursor is empty
     */
    public boolean isEmpty()
    {
        return ( getSize() == 0 );
    }

    public Iterator<T> getIterator()
    {
        final List<T> list = Arrays.asList( objects );
        return list.iterator();
    }
}
