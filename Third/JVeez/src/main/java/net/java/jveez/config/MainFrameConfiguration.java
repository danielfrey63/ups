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

package net.java.jveez.config;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import org.garret.perst.Storage;

public class MainFrameConfiguration extends ConfigurationItem {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3618419311145203252L;

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean fullscreen;

    public MainFrameConfiguration() {
    }

    public MainFrameConfiguration(Storage storage) {
        super(storage);
    }

    public void setBounds(Rectangle r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
        modify();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        modify();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        modify();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        modify();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        modify();
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        modify();
    }

    public void resetToDefaults() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        this.width = 640;
        this.height = 480;
        this.x = (d.width - width) / 2;
        this.y = (d.height - height) / 2;
        this.fullscreen = false;
        modify();
    }

    public String toString() {
        return "MainFrameConfiguration[" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", fullscreen=" + fullscreen +
                "]";
    }
}
