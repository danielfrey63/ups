/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.collection.cursor;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Default implementation for NotifiableCursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class DefaultNotifiableCursor implements NotifiableCursor
{
    private Cursor cursor;

    private Vector cursorChangeListenerList;

    /** Construct a empty NotifiableCursor */
    public DefaultNotifiableCursor()
    {
        this.cursor = new DefaultCursor();
    }

    /**
     * Construct a NotifiableCursor depending on an existing cursor
     *
     * @param cursor existing cursor object
     */
    public DefaultNotifiableCursor( final Cursor cursor )
    {
        this.cursor = cursor;
    }

    /**
     * Construct a NotifiableCursor depending on list
     *
     * @param list reference to a List
     */
    public DefaultNotifiableCursor( final List list )
    {
        this.cursor = new DefaultCursor( list );
    }

    /**
     * Construct a NotifiableCursor depending on object array
     *
     * @param array array of objects
     */
    public DefaultNotifiableCursor( final Object[] array )
    {
        this.cursor = new DefaultCursor( array );
    }

    public void setCurrent( final Object obj )
    {
        if ( obj != cursor.getCurrent() )
        {
            cursor.setCurrent( obj );
            fireCursorChange( new CursorChangeEvent( cursor ) );
        }
    }

    /**
     * Initialize the notifiable cursor with a new cursor.
     *
     * @param cursor the new cursor
     */
    public void setCursor( final Cursor cursor )
    {
        this.cursor = cursor;
        fireCursorChange( new CursorChangeEvent( cursor ) );
    }

    /**
     * Initialize the notifiable cursor with a new list.
     *
     * @param list the new list
     */
    public void setCursor( final List list )
    {
        this.cursor = new DefaultCursor( list );
        fireCursorChange( new CursorChangeEvent( cursor ) );
    }

    /**
     * Initialize the notifiable cursor with a new object array.
     *
     * @param objects the new array of objects
     */
    public void setCursor( final Object[] objects )
    {
        this.cursor = new DefaultCursor( objects );
        fireCursorChange( new CursorChangeEvent( cursor ) );
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

    public boolean isEmpty()
    {
        return cursor.isEmpty();
    }

    public Iterator getIterator()
    {
        return cursor.getIterator();
    }

    @Override
    public synchronized void addCursorChangeListener( final CursorChangeListener listener )
    {
        if ( cursorChangeListenerList == null )
        {
            cursorChangeListenerList = new Vector();
        }
        cursorChangeListenerList.add( listener );

        // send initial event
        listener.cursorChange( new CursorChangeEvent( cursor ) );
    }

    @Override
    public synchronized void removeCursorChangeListener( final CursorChangeListener listener )
    {
        if ( cursorChangeListenerList != null )
        {
            cursorChangeListenerList.remove( listener );
        }
    }

    public Object next()
    {
        final Object obj = cursor.next();
        fireCursorChange( new CursorChangeEvent( cursor ) );
        return obj;
    }

    public Object previous()
    {
        final Object obj = cursor.previous();
        fireCursorChange( new CursorChangeEvent( cursor ) );
        return obj;
    }

    public boolean hasNext()
    {
        return cursor.hasNext();
    }

    public boolean hasPrevious()
    {
        return cursor.hasPrevious();
    }

    protected void fireCursorChange( final CursorChangeEvent event )
    {
        final Vector list;
        synchronized ( this )
        {
            if ( cursorChangeListenerList == null )
            {
                return;
            }
            list = (Vector) cursorChangeListenerList.clone();
        }
        for ( final Object aList : list )
        {
            ( (CursorChangeListener) aList ).cursorChange( event );
        }
    }
}
