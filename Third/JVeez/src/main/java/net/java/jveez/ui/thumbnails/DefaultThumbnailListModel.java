/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.ui.thumbnails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import net.java.jveez.utils.SortingAlgorithm;
import org.apache.log4j.Logger;

public class DefaultThumbnailListModel<T> extends AbstractListModel implements ThumbnailListModel<T>
{
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3258131345149407289L;

    private static final Logger LOG = Logger.getLogger( DefaultThumbnailListModel.class );

    private final List<T> list = new ArrayList<T>();

    public int getSize()
    {
        return list.size();
    }

    public T getPicture( final int index )
    {
        return list.get( index );
    }

    public T getElementAt( final int index )
    {
        return list.get( index );
    }

    public int getIndexOf( final T picture )
    {
        return list.indexOf( picture );
    }

    public void clear()
    {
        LOG.debug( "clear()" );
        final int previousSize = list.size();
        list.clear();
        if ( previousSize > 0 )
        {
            fireIntervalRemoved( this, 0, previousSize - 1 );
        }
    }

    public void setPictures( final Collection<? extends T> pictures )
    {
        list.clear();
        list.addAll( pictures );
        fireContentsChanged( this, 0, list.size() - 1 );
    }

    public void setPictureAt( final int index, final T picture )
    {
        list.set( index, picture );
        fireContentsChanged( this, index, index );
    }

    public void sort( final SortingAlgorithm<T> algorithm )
    {
        algorithm.sort( list );
        fireContentsChanged( this, 0, list.size() - 1 );
    }

    public synchronized void notifyAsUpdated( final int index )
    {
        fireContentsChanged( this, index, index );
    }
}
