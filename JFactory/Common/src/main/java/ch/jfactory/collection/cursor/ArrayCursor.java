package ch.jfactory.collection.cursor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Category;

/**
 * Default implementation for array cursor
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class ArrayCursor implements Cursor {
    private static final Category cat = Category.getInstance(ArrayCursor.class);
    private Object[] objects;
    private int currentIndex = 0;

    public ArrayCursor(final Object[] array) {
        this.objects = array;
    }

    public Object next() {
        currentIndex = (currentIndex < getSize()) ? ++currentIndex : getSize();
        return objects[currentIndex];
    }

    public Object previous() {
        currentIndex = (currentIndex == 0) ? 0 : --currentIndex;
        return objects[currentIndex];
    }

    public boolean hasNext() {
        return (currentIndex < getSize() - 1);
    }

    public boolean hasPrevious() {
        return (currentIndex > 0);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getSize() {
        return objects == null ? 0 : objects.length;
    }

    public Object getCurrent() {
        if (objects == null) return null;
        if (currentIndex >= objects.length) return null;
        if (currentIndex < 0) return null;
        return objects[currentIndex];
    }

    public void setCurrent(final Object obj) {
        for (int i = 0; i < getSize(); i++) {
            if (objects[i] == obj) {
                currentIndex = i;
                return;
            }
        }
        cat.info("Error setting Current Object to " + obj);
    }

    /**
     * check if the cursor is empty.
     *
     * @return true if cursor is empty
     */
    public boolean isEmpty() {
        return (getSize() == 0);
    }

    public Iterator getIterator() {
        final List list = Arrays.asList(objects);
        return list.iterator();
    }
}
