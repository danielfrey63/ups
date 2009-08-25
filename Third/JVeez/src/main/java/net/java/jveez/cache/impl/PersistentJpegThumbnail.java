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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.java.jveez.vfs.Picture;
import org.garret.perst.Storage;

public class PersistentJpegThumbnail extends PersistentThumbnail {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257853160218310455L;

    private byte[] bytes;

    public PersistentJpegThumbnail() {
    }

    public PersistentJpegThumbnail(Storage storage, Picture picture, BufferedImage image) {
        super(storage, picture);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2 * 1024);
        try {
            ImageIO.write(image, "jpeg", bos);
            bos.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save the thumbnail in JPEG !", e);
        }
        this.bytes = bos.toByteArray();
    }

    public BufferedImage getImage() {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load the thumbnail for the byte array", e);
        }
    }
}
