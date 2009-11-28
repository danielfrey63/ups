package ch.jfactory.collection.cursor;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;

/**
 * Default implementation for list cursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
class ListCursor implements Cursor
{
    private static final Logger LOGGER = Logger.getLogger( ListCursor.class );

    private final List list;

    private final ListIterator iterator;

    private Object currentObject = null;

    public ListCursor( final List list )
    {
        this.list = list;
        this.iterator = list.listIterator();
        this.currentObject = iterator.hasNext() ? iterator.next() : null;
    }

    public Object next()
    {
        currentObject = iterator.next();
        return currentObject;
    }

    public Object previous()
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

    public Object getCurrent()
    {
        return currentObject;
    }

    public void setCurrent( final Object obj )
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

    public Iterator getIterator()
    {
        return list.iterator();
    }
}
