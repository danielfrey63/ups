/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.model;

import ch.jfactory.convert.Converter;
import ch.jfactory.xstream.XStreamConverter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.swing.AbstractListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataListener;

/**
 * List of models.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class SimpleModelList<T> extends AbstractListModel implements Iterable<T>
{
    private ArrayList<T> models = new ArrayList<T>();

    private Object current;

    protected transient int modCount = 0;

    public SimpleModelList()
    {
        super();
    }

    public SimpleModelList( final ArrayList<T> models )
    {
        this.models = models;
    }

    public int getSize()
    {
        return models.size();
    }

    public T getElementAt( final int index )
    {
        return models.get( index );
    }

    public void add( final T model )
    {
        final int index = models.size();
        models.add( model );
        modCount++;
        fireIntervalAdded( this, index, index );
    }

    public void remove( final T model )
    {
        final int index = models.indexOf( model );
        models.remove( model );
        modCount++;
        fireIntervalRemoved( this, index, index );
    }

    public void clear()
    {
        final int size = models.size();
        models.clear();
        modCount++;
        fireContentsChanged( this, 0, size - 1 );
    }

    public void addAll( final ArrayList<T> list )
    {
        models.addAll( list );
        modCount++;
        fireIntervalAdded( this, 0, list.size() - 1 );
    }

    public void setCurrent( final Object current )
    {
        this.current = current;
    }

    public Object getCurrent()
    {
        return current;
    }

    // I have to initialize the listener list lazyly as loading the data from XML doesn't (and shouldn't) contain
    // listener support data.
    public void addListDataListener( final ListDataListener l )
    {
        if ( listenerList == null )
        {
            listenerList = new EventListenerList();
        }
        super.addListDataListener( l );
    }

    public static Object[] toArray( final SimpleModelList modelList )
    {
        final int size = modelList.getSize();
        final Object[] result = new Object[size];
        for ( int i = 0; i < size; i++ )
        {
            result[i] = modelList.getElementAt( i );
        }
        return result;
    }

    public static Converter getConverter()
    {
        final Map<String, Class> aliases = new HashMap<String, Class>();

        final Map<Class, String> implicitCollections = new HashMap<Class, String>();
        implicitCollections.put( SimpleModelList.class, "models" );

        final Map<Class, String> omits = new HashMap<Class, String>();
        omits.put( AbstractListModel.class, "listenerList" );

        return new XStreamConverter( aliases, implicitCollections, omits );
    }

    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            /**
             * Index of element to be returned by subsequent call to next.
             */
            int cursor = 0;

            /**
             * Index of element returned by most recent call to next or previous.  Reset to -1 if this element is
             * deleted by a call to remove.
             */
            int lastRet = -1;

            /**
             * The modCount value that the iterator believes that the backing List should have.  If this expectation is
             * violated, the iterator has detected concurrent modification.
             */
            int expectedModCount = modCount;

            public boolean hasNext()
            {
                return cursor != getSize();
            }

            public T next()
            {
                checkForComodification();
                try
                {
                    final T next = getElementAt( cursor );
                    lastRet = cursor++;
                    return next;
                }
                catch ( IndexOutOfBoundsException e )
                {
                    checkForComodification();
                    throw new NoSuchElementException();
                }
            }

            public void remove()
            {
                if ( lastRet == -1 )
                {
                    throw new IllegalStateException();
                }
                checkForComodification();

                try
                {
                    SimpleModelList.this.remove( getElementAt( lastRet ) );
                    if ( lastRet < cursor )
                    {
                        cursor--;
                    }
                    lastRet = -1;
                    expectedModCount = modCount;
                }
                catch ( IndexOutOfBoundsException e )
                {
                    throw new ConcurrentModificationException();
                }
            }

            final void checkForComodification()
            {
                if ( modCount != expectedModCount )
                {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
}
