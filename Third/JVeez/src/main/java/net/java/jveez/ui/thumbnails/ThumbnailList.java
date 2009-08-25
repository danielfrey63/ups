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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import javax.swing.JList;
import net.java.jveez.utils.PictureSortingAlgorithm;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Picture;
import net.java.jveez.vfs.Vfs;
import org.apache.log4j.Logger;

public class ThumbnailList extends JList {

    public static final Dimension MAXIMUM_THUMBNAIL_SIZE = new Dimension(128, 128);

    private static final Logger LOG = Logger.getLogger(ThumbnailList.class);
    private static final long serialVersionUID = 3256725069878212915L;
    private static final Font loadingFont = new Font("Sans Serif", 12, Font.BOLD);

    private ThumbnailListModel model = new DefaultThumbnailListModel();
    private PictureSortingAlgorithm sortingAlgorithm;
    private boolean loading = false;

    /** Whether to show a number on each thumbnail. */
    private boolean showNumber;

    public ThumbnailList() {
        this(PictureSortingAlgorithm.ByName);
    }

    public ThumbnailList(PictureSortingAlgorithm sortingAlgorithm) {
        super();
        this.sortingAlgorithm = sortingAlgorithm;
        setModel(model);
        setCellRenderer(new ThumbnailListCellRenderer());
        setFixedCellHeight(MAXIMUM_THUMBNAIL_SIZE.width + 4);
        setFixedCellWidth(MAXIMUM_THUMBNAIL_SIZE.height + 4);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(0);
    }

    public void setModel(ThumbnailListModel model) {
        super.setModel(model);
        this.model = model;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (loading) {
            int width = getWidth();
            int height = getHeight();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, width, height);
            g2d.setFont(loadingFont);
            g2d.drawString("Loading ...", width / 2, height / 2);
        } else if (showNumber) {
            g2d.setFont(new Font("Sans Serif", 24, Font.BOLD));
            g2d.drawString("1", 30, 30);
        }
    }

    public ThumbnailListModel getThumbnailListModel() {
        return model;
    }

    public void setThumbnailSize(final int size) {
        MAXIMUM_THUMBNAIL_SIZE.setSize(size, size);
        setFixedCellHeight(size);
        setFixedCellWidth(size);
        repaint();
    }

    public Picture getPictureAt(int index) {
        return getThumbnailListModel().getPicture(index);
    }

    public void setCurrentDirectory(final Directory directory) {
        Utils.executeAsyncIfDisptachThread(new Runnable() {
            public void run() {
                model.clear();
                repaint();

                if (directory != null) {
                    try {
                        setLoading(true);

                        Collection<? extends Picture> pictures = Vfs.getInstance().getPictures(directory);
                        model.setPictures(pictures);
                        model.sort(sortingAlgorithm);

                    }
                    finally {
                        setLoading(false);
                    }
                }
            }
        });
    }

    public void setSortingAlgorithm(PictureSortingAlgorithm value) {
        this.sortingAlgorithm = value;

        Utils.executeAsyncIfDisptachThread(new Runnable() {
            public void run() {
                setEnabled(false);

                // retrieve last object selected
                Picture previousSelection = null;
                int index = getSelectionModel().getAnchorSelectionIndex();
                if (index != -1) {
                    previousSelection = model.getPicture(index);
                }

                model.sort(sortingAlgorithm);

                if (previousSelection != null) {
                    index = model.getIndexOf(previousSelection);
                    setSelectedIndex(index);
                    ensureIndexIsVisible(index);
                }
                setEnabled(true);
            }
        });
    }

    public int getIndexBefore(int index) {

        int numberOfPictures = getThumbnailListModel().getSize();

        if (numberOfPictures == 0) {
            return -1;
        }

        int previousIndex = index - 1;
        if (previousIndex < 0) {
            return numberOfPictures - 1;
        }
        else {
            return previousIndex;
        }
    }

    public int getIndexAfter(int index) {

        int numberOfPictures = getThumbnailListModel().getSize();

        if (numberOfPictures == 0) {
            return -1;
        }

        int nextIndex = index + 1;
        if (nextIndex >= numberOfPictures) {
            return 0;
        }
        else {
            return nextIndex;
        }
    }

    public int getNumberOfPictures() {
        return getThumbnailListModel().getSize();
    }

    private void setLoading(boolean loading) {
        if (this.loading != loading) {
            this.loading = loading;
            repaint();
        }
    }

    public void setShowNumber(final boolean showNumber) {
        this.showNumber = showNumber;
    }
}
