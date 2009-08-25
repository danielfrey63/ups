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
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.java.jveez.cache.ImageLoader;
import net.java.jveez.config.ConfigurationManager;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;
import org.garret.perst.FieldIndex;
import org.garret.perst.Key;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

public final class PersistentThumbnailLoader extends AbstractImageLoader {

    private static final Logger LOG = Logger.getLogger(PersistentThumbnailLoader.class);
    private static final boolean DEBUG = LOG.isDebugEnabled();

    private final String name;
    private final int pagePoolSize;
    private final BlockingQueue<Runnable> backgroundJobQueue = new ArrayBlockingQueue<Runnable>(20);
    private final ExecutorService backgroundExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            backgroundJobQueue, Utils.newPriorityThreadFactory(Thread.MIN_PRIORITY));

    private Storage storage;
    private FieldIndex<PersistentThumbnail> persistentImageIndex;

    public PersistentThumbnailLoader(ImageLoader delegate, String name, int pagePoolSize) {
        super(delegate);
        this.name = name;
        this.pagePoolSize = pagePoolSize;
        openPersistence();
    }

    private void openPersistence() {
        final File file = getPersistenceFile();
        storage = StorageFactory.getInstance().createStorage();
        storage.setProperty("perst.object.cache.kind", "weak");
        storage.open(file.getAbsolutePath(), pagePoolSize);
        persistentImageIndex = (FieldIndex<PersistentThumbnail>) storage.getRoot();
        if (persistentImageIndex == null) {
            LOG.info(String.format("Creating new persistent cache \"%s\" (poolSize=%d)", name, pagePoolSize));
            persistentImageIndex = storage.createFieldIndex(PersistentThumbnail.class, "path", true);
            storage.setRoot(persistentImageIndex);
        }
        else {
            LOG.info(String.format("Opening existing persistent cache \"%s\" (poolSize=%d)", name, pagePoolSize));
        }
    }

    private File getPersistenceFile() {
        return new File(ConfigurationManager.getInstance().getJveezHome(), name + ".db");
    }

    protected boolean _isCached(Picture picture) {
        return false;
    }

    protected BufferedImage _getImage(Picture picture) {
        persistentImageIndex.sharedLock();
        try {
            PersistentThumbnail persistentImage = persistentImageIndex.get(getkeyForPicture(picture));
            if (persistentImage != null) {
                return persistentImage.getImage();
            }
            if (DEBUG) {
                LOG.debug("Cache missing \"" + picture.getFile() + "\"");
            }
            return null;
        }
        finally {
            persistentImageIndex.unlock();
        }
    }

    private Key getkeyForPicture(Picture picture) {
        return new Key(picture.getAbsolutePath());
    }

    protected void _fetchIntoCache(Picture picture, BufferedImage image) {
        if (DEBUG) {
            LOG.debug("Queuing \"" + picture.getFile() + "\"");
        }
        if (backgroundJobQueue.remainingCapacity() > 0) {
            backgroundExecutor.execute(new ThumbnailFetcherJob(picture, image));
        }
        else {
            LOG.warn("Background job queue full. Cannot process \"" + picture.getFile() + "\"");
        }
    }

    void _doFetchIntoCache(Picture picture, BufferedImage image) {
        if (DEBUG) {
            LOG.debug("Fetch image (" + image.getWidth() + "x" + image.getHeight() + ") into cache \"" + picture.getFile() + "\"");
        }
        Key pictureKey = getkeyForPicture(picture);
        persistentImageIndex.exclusiveLock();
        try {
            PersistentThumbnail persistentThumbnail = persistentImageIndex.get(pictureKey);
            if (persistentThumbnail == null) {
                PersistentThumbnail thumbnail = new PersistentRawThumbnail(storage, picture, image);
                persistentImageIndex.put(thumbnail);
            }
        }
        finally {
            persistentImageIndex.unlock();
        }
    }

    protected void _invalidateCache() {
        persistentImageIndex.clear();
        backgroundJobQueue.clear();
        _close();
        getPersistenceFile().delete();
        openPersistence();
    }

    protected void _close() {
        LOG.info("Closing cache \"" + name + "\"");
        if (storage != null && storage.isOpened()) {
            storage.commit();
            storage.close();
            storage = null;
            persistentImageIndex = null;
        }
    }

    private class ThumbnailFetcherJob implements Runnable {

        private Picture picture;
        private BufferedImage image;

        public ThumbnailFetcherJob(Picture picture, BufferedImage image) {
            if (DEBUG) {
                LOG.debug("Fetcher job for \"" + picture.getFile() + "\" created");
            }
            this.picture = picture;
            this.image = image;
        }

        public void run() {
            _doFetchIntoCache(picture, image);
        }
    }
}
