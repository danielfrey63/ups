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

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Icon;
import net.java.jveez.cache.ThumbnailStore;
import net.java.jveez.utils.BufferedImageIcon;
import net.java.jveez.utils.DebugUtils;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

public class ThumbnailAsyncLoader
{
    private static final Logger LOG = Logger.getLogger( ThumbnailAsyncLoader.class );

    private static final ThumbnailAsyncLoader instance = new ThumbnailAsyncLoader();

    private static final ExecutorService executor = Executors.newFixedThreadPool( 3, Utils.newPriorityThreadFactory( Thread.NORM_PRIORITY - 1 ) );

    private static final Map<Picture, Boolean> thumbnailsLoading = new ConcurrentHashMap<Picture, Boolean>();

    public Icon getThumbnailFor( final Picture picture, final ThumbnailList thumbnailList, final int index, final int width, final int height )
    {
        DebugUtils.ensureIsDispatchThread();

        if ( ThumbnailStore.getInstance().isCached( picture ) )
        {
            final BufferedImage image = ThumbnailStore.getInstance().getImage( picture );
            return new BufferedImageIcon( image );
        }
        else
        {
            if ( !thumbnailsLoading.containsKey( picture ) )
            {
                thumbnailsLoading.put( picture, Boolean.TRUE );
                scheduleForDownloading( picture, thumbnailList, index, width, height );
            }
            return picture.getIcon();
        }
    }

    private void scheduleForDownloading( final Picture picture, final ThumbnailList thumbnailList, final int index,
                                         final int width, final int height )
    {
        executor.execute( new Runnable()
        {
            public void run()
            {
                try
                {
                    // still need to load ?
                    if ( index > thumbnailList.getLastVisibleIndex() || index < thumbnailList.getFirstVisibleIndex() )
                    {
                        return;
                    }

                    ThumbnailStore.getInstance().getImage( picture );
                    thumbnailList.getThumbnailListModel().notifyAsUpdated( index );
                }
                finally
                {
                    thumbnailsLoading.remove( picture );
                }
            }
        } );
    }

    public static ThumbnailAsyncLoader getInstance()
    {
        return instance;
    }
}
