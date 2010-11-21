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

package net.java.jveez.cache.impl;

import java.awt.image.BufferedImage;
import net.java.jveez.cache.ImageLoader;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

public abstract class AbstractImageLoader implements ImageLoader
{
    private static final Logger LOG = Logger.getLogger( AbstractImageLoader.class );

    private final ImageLoader delegateImageLoader;

    protected AbstractImageLoader( final ImageLoader delegateImageLoader )
    {
        this.delegateImageLoader = delegateImageLoader;
    }

    public final boolean isCached( final Picture picture )
    {
        if ( _isCached( picture ) )
        {
            return true;
        }
        else if ( delegateImageLoader != null )
        {
            return delegateImageLoader.isCached( picture );
        }
        return false;
    }

    public final BufferedImage getImage( final Picture picture )
    {
        BufferedImage image = _getImage( picture );
        if ( image == null && delegateImageLoader != null )
        {
            image = delegateImageLoader.getImage( picture );
            if ( image != null )
            {
                _fetchIntoCache( picture, image );
            }
            else
            {
                LOG.warn( "Could not get image for \"" + picture.getFile() + "\"" );
            }
        }
        return image;
    }

    public final void invalidateCache()
    {
        if ( delegateImageLoader != null )
        {
            delegateImageLoader.invalidateCache();
        }
        _invalidateCache();
    }

    public final void close()
    {
        if ( delegateImageLoader != null )
        {
            delegateImageLoader.close();
        }
        _close();
    }

    protected abstract boolean _isCached( Picture picture );

    protected abstract BufferedImage _getImage( Picture picture );

    protected abstract void _fetchIntoCache( Picture picture, BufferedImage image );

    protected abstract void _invalidateCache();

    protected abstract void _close();
}
