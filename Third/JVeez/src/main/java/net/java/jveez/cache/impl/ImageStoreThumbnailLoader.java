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

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import net.java.jveez.cache.ImageStore;
import net.java.jveez.ui.thumbnails.ThumbnailList;
import net.java.jveez.utils.ImageUtils;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

public final class ImageStoreThumbnailLoader extends AbstractImageLoader
{
    private static final Logger LOG = Logger.getLogger( ImageStoreThumbnailLoader.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    public ImageStoreThumbnailLoader()
    {
        super( null );
    }

    public boolean _isCached( final Picture picture )
    {
        return false;
    }

    public BufferedImage _getImage( final Picture picture )
    {
        final int width = ThumbnailList.MAXIMUM_THUMBNAIL_SIZE.width;
        final int height = ThumbnailList.MAXIMUM_THUMBNAIL_SIZE.height;
        final String source;

        // try to load the image from EXIF data
        BufferedImage image = ImageUtils.loadThumbnailFromEXIF( picture );
        if ( image == null || ( image.getWidth() < width && image.getHeight() < height ) )
        {
            source = "image";
            // otherwise load from ImageCache
            image = ImageStore.getInstance().getImage( picture );

            // picture available ?
            if ( image == null )
            {
                return null;
            }
        }
        else
        {
            source = "EXIF thumbnail";
        }

        // check if the picture needs to be rescaled
        if ( image.getWidth() > width || image.getHeight() > height )
        {
            if ( DEBUG )
            {
                LOG.debug( "Downscaling " + source + " for \"" + picture.getFile() + "\"" );
            }
            image = ImageUtils.createScaledImage( image, width, height, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );
        }
        else if ( image.getWidth() < width && image.getHeight() < height )
        {
            if ( DEBUG )
            {
                LOG.debug( "Upscaling " + source + " for \"" + picture.getFile() + "\"" );
            }
            image = ImageUtils.createScaledImage( image, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        }

        return image;

    }

    protected void _fetchIntoCache( final Picture picture, final BufferedImage image )
    {
        // nothing to do here
    }

    public void _invalidateCache()
    {
        // nothing to do here
    }

    public void _close()
    {
        // nothing to do here
    }
}
