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

package net.java.jveez.cache;

import java.awt.image.BufferedImage;
import net.java.jveez.cache.impl.ImageStoreThumbnailLoader;
import net.java.jveez.cache.impl.MemoryImageLoader;
import net.java.jveez.cache.impl.PersistentThumbnailLoader;
import net.java.jveez.vfs.Picture;

public class ThumbnailStore implements ImageLoader {

    private static final int MEMORY_CACHE_CAPACITY = 100;
    private static final String PERSISTENT_LOADER_NAME = "thumbnail-cache";
    private static final int PERSISTENT_LOADER_PAGE_POOL_SIZE = 2 * 1024 * 1024;

    private static final ThumbnailStore instance = new ThumbnailStore();

    private ImageLoader delegateLoader;

    private ThumbnailStore() {
        ImageStoreThumbnailLoader imageStoreThumbnailLoader = new ImageStoreThumbnailLoader();
        PersistentThumbnailLoader persistentThumbnailLoader = new PersistentThumbnailLoader(imageStoreThumbnailLoader, PERSISTENT_LOADER_NAME, PERSISTENT_LOADER_PAGE_POOL_SIZE);
        delegateLoader = new MemoryImageLoader(persistentThumbnailLoader, MEMORY_CACHE_CAPACITY);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                close();
            }
        });
    }

    public boolean isCached(Picture picture) {
        return delegateLoader.isCached(picture);
    }

    public BufferedImage getImage(Picture picture) {
        return delegateLoader.getImage(picture);
    }

    public void invalidateCache() {
        delegateLoader.invalidateCache();
    }

    public void close() {
        if (delegateLoader != null) {
            delegateLoader.close();
            delegateLoader = null;
        }
    }

    public static ThumbnailStore getInstance() {
        return instance;
    }
}
