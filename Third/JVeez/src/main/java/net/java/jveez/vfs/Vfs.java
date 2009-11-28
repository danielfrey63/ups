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

package net.java.jveez.vfs;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import net.java.jveez.vfs.impl.VfsImpl;
import org.apache.log4j.Logger;

public abstract class Vfs
{
    private static final Logger LOG = Logger.getLogger( Vfs.class );

    private final CopyOnWriteArrayList<VfsEventListener> listeners = new CopyOnWriteArrayList<VfsEventListener>();

    private volatile int allowEventDispatchCounter;

    private static final Vfs INSTANCE = new VfsImpl();

    public static Vfs getInstance()
    {
        return INSTANCE;
    }

    public abstract void synchronize();

    public abstract boolean isCached( Directory diectory );

    public abstract Collection<? extends Directory> getRootDirectories();

    public abstract boolean hasSubDirectories( Directory directory );

    public abstract boolean hasPictures( Directory directory );

    public abstract Collection<? extends Directory> getSubDirectories( Directory directory );

    public abstract Collection<? extends Picture> getPictures( Directory directory );

    public void addVfsListener( final VfsEventListener listener )
    {
        listeners.add( listener );
    }

    protected void setEventDispatchEnabled( final boolean eventDispatchEnabled )
    {
        LOG.debug( "setEventDispatchEnabled(" + eventDispatchEnabled + ")" );
        if ( eventDispatchEnabled )
        {
            allowEventDispatchCounter--;
        }
        else
        {
            allowEventDispatchCounter++;
        }
    }

    protected void dispatchEvent( final VfsEvent event )
    {
        if ( allowEventDispatchCounter != 0 )
        {
            return;
        }

        LOG.debug( "dispatchEvent(" + event + ")" );

        for ( final VfsEventListener listener : listeners )
        {
            try
            {
                listener.vfsEventDispatched( event );
            }
            catch ( Throwable t )
            {
                LOG.error( "Throwable caught during event dispatching", t );
            }
        }
    }
}