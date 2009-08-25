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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import net.java.jveez.utils.ImageUtils;
import net.java.jveez.vfs.Picture;
import org.garret.perst.Storage;

public class PersistentRawThumbnail extends PersistentThumbnail {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4120849941255368752L;

    public static transient final ColorModel COLOR_MODEL = ColorModel.getRGBdefault();

    private int width;
    private int height;
    private int[] pix;

    public PersistentRawThumbnail() {
    }

    public PersistentRawThumbnail(Storage storage, Picture picture, BufferedImage image) {
        super(storage, picture);

        this.width = image.getWidth();
        this.height = image.getHeight();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, true);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            throw new RuntimeException("Image fetch aborted or errored");
        }

        this.pix = (int[]) pg.getPixels();
    }

    public BufferedImage getImage() {
        Image sourceImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, COLOR_MODEL, pix, 0, width));
        // here, we will copy the (unmanaged) image from ImageIO into a compatible (managed) image to ensure best performance
        return ImageUtils.copyIntoCompatibleImage(sourceImage, width, height);
    }
}
