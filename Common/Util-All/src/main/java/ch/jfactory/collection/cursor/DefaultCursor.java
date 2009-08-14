package ch.jfactory.collection.cursor;

import java.util.Iterator;
import java.util.List;

/**
 * Default Implementation of the Cursor Interface. The implementation is capable to provide cursor implementation by
 * initialize with an array or a list.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
public class DefaultCursor implements Cursor
{
    private static Cursor emptyCursor = new ListCursor(new java.util.ArrayList());

    private Cursor cursor;

    public DefaultCursor()
    {
        cursor = emptyCursor;
    }

    /**
     * Construct a Cursor depending on a List. If the parameter is null, an empty cursor will be constructed
     *
     * @param list list for the cursor
     */
    public DefaultCursor(final List list)
    {
        if (list == null)
        {
            cursor = emptyCursor;
        }
        else
        {
            cursor = new ListCursor(list);
        }
    }

    /**
     * Construct a Cursor depending on an array. If the parameter is null, an empty cursor will be constructed
     *
     * @param array an array of objects
     */
    public DefaultCursor(final Object[] array)
    {
        if (array == null)
        {
            cursor = emptyCursor;
        }
        else
        {
            cursor = new ArrayCursor(array);
        }
    }

    public Object next()
    {
        return cursor.next();
    }

    public Object previous()
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

    public Object getCurrent()
    {
        return cursor.getCurrent();
    }

    public void setCurrent(final Object obj)
    {
        cursor.setCurrent(obj);
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

    public Iterator getIterator()
    {
        return cursor.getIterator();
    }
}
