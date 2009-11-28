package ch.jfactory.collection.cursor;

/**
 * Class holding information about the CursorChangeEvent.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
public class CursorChangeEvent
{
    public CursorChangeEvent( final Cursor cursor )
    {
        this.cursor = cursor;
    }

    /**
     * return a reference to the cursor object.
     *
     * @return reference to cursor object
     */
    public Cursor getCursor()
    {
        return cursor;
    }

    /**
     * return reference to current object.
     *
     * @return the current object
     */
    public Object getCurrentObject()
    {
        return cursor.getCurrent();
    }

    private final Cursor cursor;
}
