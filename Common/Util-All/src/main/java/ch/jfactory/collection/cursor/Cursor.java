package ch.jfactory.collection.cursor;

import java.util.Iterator;

/**
 * Interface for cursor implementations. Index handling is the same as in Java arrays. Index start with 0 and ends with
 * n-1. Size is n.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public interface Cursor
{
    /**
     * get the next object and move the cursor forward.
     *
     * @return next object
     */
    Object next();

    /**
     * get the previous object and move the cursor backward.
     *
     * @return next object
     */
    Object previous();

    /**
     * Check if a following object exists.
     *
     * @return true is there is a following object
     */
    boolean hasNext();

    /**
     * Check if a previous object exists.
     *
     * @return true is there is a previous object
     */
    boolean hasPrevious();

    /**
     * get index of the actual cursor position.
     *
     * @return current cursor position
     */
    int getCurrentIndex();

    /**
     * return the number of objects handled by the cursor.
     *
     * @return get max size of the list
     */
    int getSize();

    /**
     * return the current object
     *
     * @return the current object or null if cursor is empty
     */
    Object getCurrent();

    /**
     * check if the cursor is empty.
     *
     * @return true if cursor is empty
     */
    boolean isEmpty();

    /**
     * set the current Item
     *
     * @param obj
     */
    void setCurrent(Object obj);

    /**
     * return an iterator for all objects. Iteration over it doesn't change the cursor position.
     *
     * @return an iterator over all objects
     */
    Iterator getIterator();
}
