package ch.jfactory.collection.cursor;

import java.util.Iterator;
import java.util.Vector;

/**
 * Default implementation for NotifiableCursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class DefaultNotifiableCursor implements NotifiableCursor {
    private Cursor cursor;

    private Vector cursorChangeListenerList;

    /**
     * Construct a empty NotifiableCursor
     */
    public DefaultNotifiableCursor() {
        this.cursor = new DefaultCursor();
    }

    /**
     * Construct a NotifiableCursor depending on an existing cursor
     *
     * @param cursor existing cursor object
     */
    public DefaultNotifiableCursor(final Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Construct a NotifiableCursor depending on list
     *
     * @param list reference to a List
     */
    public DefaultNotifiableCursor(final java.util.List list) {
        this.cursor = new DefaultCursor(list);
    }

    /**
     * Construct a NotifiableCursor depending on object array
     *
     * @param array array of objects
     */
    public DefaultNotifiableCursor(final Object[] array) {
        this.cursor = new DefaultCursor(array);
    }

    public void setCurrent(final Object obj) {
        if (obj != cursor.getCurrent()) {
            cursor.setCurrent(obj);
            fireCursorChange(new CursorChangeEvent(cursor));
        }
    }

    /**
     * Initialize the notifiable cursor with a new cursor.
     *
     * @param cursor the new cursor
     */
    public void setCursor(final Cursor cursor) {
        this.cursor = cursor;
        fireCursorChange(new CursorChangeEvent(cursor));
    }

    /**
     * Initialize the notifiable cursor with a new list.
     *
     * @param list the new list
     */
    public void setCursor(final java.util.List list) {
        this.cursor = new DefaultCursor(list);
        fireCursorChange(new CursorChangeEvent(cursor));
    }

    /**
     * Initialize the notifiable cursor with a new object array.
     *
     * @param objects the new array of objects
     */
    public void setCursor(final Object[] objects) {
        this.cursor = new DefaultCursor(objects);
        fireCursorChange(new CursorChangeEvent(cursor));
    }

    public int getCurrentIndex() {
        return cursor.getCurrentIndex();
    }

    public int getSize() {
        return cursor.getSize();
    }

    public Object getCurrent() {
        return cursor.getCurrent();
    }

    public boolean isEmpty() {
        return cursor.isEmpty();
    }

    public Iterator getIterator() {
        return cursor.getIterator();
    }

    public synchronized void addCursorChangeListener(final CursorChangeListener listener) {
        if (cursorChangeListenerList == null) {
            cursorChangeListenerList = new Vector();
        }
        cursorChangeListenerList.add(listener);

        // send initial event
        listener.cursorChange(new CursorChangeEvent(cursor));
    }

    public synchronized void removeCursorChangeListener(final CursorChangeListener listener) {
        if (cursorChangeListenerList != null) {
            cursorChangeListenerList.remove(listener);
        }
    }

    public Object next() {
        final Object obj = cursor.next();
        fireCursorChange(new CursorChangeEvent(cursor));
        return obj;
    }

    public Object previous() {
        final Object obj = cursor.previous();
        fireCursorChange(new CursorChangeEvent(cursor));
        return obj;
    }

    public boolean hasNext() {
        return cursor.hasNext();
    }

    public boolean hasPrevious() {
        return cursor.hasPrevious();
    }

    protected void fireCursorChange(final CursorChangeEvent event) {
        final Vector list;
        synchronized (this) {
            if (cursorChangeListenerList == null) {
                return;
            }
            list = (Vector) cursorChangeListenerList.clone();
        }
        for (int i = 0; i < list.size(); i++) {
            ((CursorChangeListener) list.get(i)).cursorChange(event);
        }
    }
}

// $Log: DefaultNotifiableCursor.java,v $
// Revision 1.2  2006/03/14 21:27:55  daniel_frey
// *** empty log message ***
//
// Revision 1.1  2005/06/16 06:28:57  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.2  2003/02/27 12:21:57  daniel_frey
// - Removed bug where activate was called twice during initalization of mode
// - Moved some components common to lesson and exam to modeapi
// - Added additional functions to exam
//
// Revision 1.1  2002/09/11 12:39:49  dirk
// add cursor
//
// Revision 1.4  2002/08/02 00:42:21  Dani
// Optimized import statements
//
// Revision 1.3  2002/06/04 11:32:53  dirk
// setCurrent only fire events if current object is not the requested one
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
