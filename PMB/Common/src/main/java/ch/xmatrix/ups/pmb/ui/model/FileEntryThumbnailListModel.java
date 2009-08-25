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
package ch.xmatrix.ups.pmb.ui.model;

import ch.xmatrix.ups.pmb.domain.FileEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;
import net.java.jveez.ui.thumbnails.ThumbnailListModel;
import net.java.jveez.utils.SortingAlgorithm;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:18:48 $
 */
public class FileEntryThumbnailListModel extends AbstractListModel implements ThumbnailListModel<FileEntry> {

    private static final Logger LOG = Logger.getLogger(FileEntryThumbnailListModel.class);

    private List<FileEntry> list = new ArrayList<FileEntry>();

    public int getSize() {
        return list.size();
    }

    public Picture getPicture(final int index) {
        return list.get(index).getPicture();
    }

    public Picture getElementAt(final int index) {
        return list.get(index).getPicture();
    }

    public FileEntry getFileEntryAt(final int index) {
        return list.get(index);
    }

    public int getIndexOf(final FileEntry fileEntry) {
        return list.indexOf(fileEntry);
    }

    public int getIndexOf(final Picture picture) {
        for (int i = 0; i < list.size(); i++) {
            final FileEntry fileEntry = list.get(i);
            if (picture == fileEntry.getPicture()) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        LOG.debug("clear()");
        final int previousSize = list.size();
        list.clear();
        if (previousSize > 0) {
            fireIntervalRemoved(this, 0, previousSize - 1);
        }
    }

    public void setPictureAt(final int index, final Picture picture) {
        throw new IllegalArgumentException("use setFileEntryAt(int) instead");
    }

    public void setPictures(final Collection<? extends Picture> pictures) {
        throw new IllegalArgumentException("use setFileEntries(Collciton) instead");
    }

    public void setFileEntries(final Collection<? extends FileEntry> fileEntries) {
        list.clear();
        list.addAll(fileEntries);
        fireContentsChanged(this, 0, list.size() - 1);
    }

    public void setFileEntryAt(final int index, final FileEntry picture) {
        list.set(index, picture);
        fireContentsChanged(this, index, index);
    }

    public void sort(final SortingAlgorithm<FileEntry> algorithm) {
        algorithm.sort(list);
        fireContentsChanged(this, 0, list.size() - 1);
    }

    public synchronized void notifyAsUpdated(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fireContentsChanged(this, index, index);
            }
        });
    }

    public enum FileEntrySortingAlgorithm implements SortingAlgorithm<FileEntry> {

        ByName {
            public void sort(final List<? extends FileEntry> list) {
                Collections.sort(list, new Comparator<FileEntry>() {
                    public int compare(final FileEntry p1, final FileEntry p2) {
                        return p1.getPath().compareTo(p2.getPath());
                    }
                });
            }
        },

        BySize {
            public void sort(final List<? extends FileEntry> list) {
                Collections.sort(list, new Comparator<FileEntry>() {
                    public int compare(final FileEntry p1, final FileEntry p2) {
                        final long l1 = p1.getPicture().getLength();
                        final long l2 = p2.getPicture().getLength();

                        if (l1 < l2) {
                            return -1;
                        }
                        else if (l1 > l2) {
                            return 1;
                        }
                        else {
                            return 0;
                        }
                    }
                });
            }
        },

        ByDate {
            public void sort(final List<? extends FileEntry> list) {
                Collections.sort(list, new Comparator<FileEntry>() {
                    public int compare(final FileEntry p1, final FileEntry p2) {
                        final long l1 = p1.getPicture().getLastModifiedDate();
                        final long l2 = p2.getPicture().getLastModifiedDate();

                        if (l1 < l2) {
                            return -1;
                        }
                        else if (l1 > l2) {
                            return 1;
                        }
                        else {
                            return 0;
                        }
                    }
                });
            }
        };

        public abstract void sort(List<? extends FileEntry> list);

        public static void sort(final File[] files) {
            Arrays.sort(files);
        }
    }
}
