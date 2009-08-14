/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.application.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class AbstractBusinessDelegate implements IFBusinessDelegate
{
    private ArrayList listenerList = new ArrayList();

    /**
     * {@inheritDoc}
     *
     * <p>Make sure to call super when overwriding this medthod.</p>
     */
    public void createNew(final Properties properties) throws SourceVetoedException
    {
        fireSourceStateMayChange(new SourceStateEvent(this, SourceStateEvent.NEW));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Make sure to call super when overwriding this medthod.</p>
     */
    public void save(final Properties properties)
    {
        fireSourceStateChanged(new SourceStateEvent(this, SourceStateEvent.SAVED));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Make sure to call super when overwriding this medthod.</p>
     */
    public void open(final Properties properties) throws SourceVetoedException
    {
        fireSourceStateMayChange(new SourceStateEvent(this, SourceStateEvent.NEW));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Make sure to call super when overwriding this medthod.</p>
     */
    public void cacheToDelete(final Object o)
    {
        fireSourceStateChanged(new SourceStateEvent(this, SourceStateEvent.DIRTY));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Make sure to call super when overwriding this medthod.</p>
     */
    public void cacheToSave(final Object o)
    {
        fireSourceStateChanged(new SourceStateEvent(this, SourceStateEvent.DIRTY));
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @param l the listener to add
     */
    public void addSourceChangeListener(final SourceStateListener l)
    {
        listenerList.add(l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     */
    public void removeSourceChangeListener(final SourceStateListener l)
    {
        listenerList.remove(l);
    }

    protected void fireSourceStateChanged(final SourceStateEvent event)
    {
        final Iterator i = getListener();
        while (i.hasNext())
        {
            ((SourceStateListener) i.next()).sourceStateChanged(event);
        }
    }

    protected void fireSourceStateMayChange(final SourceStateEvent event) throws SourceVetoedException
    {
        final Iterator i = getListener();
        while (i.hasNext())
        {
            ((SourceStateListener) i.next()).sourceStateMayChange(event);
        }
    }

    private Iterator getListener()
    {
        final ArrayList l;

        synchronized (this)
        {
            l = (ArrayList) listenerList.clone();
        }

        return l.iterator();
    }
}
