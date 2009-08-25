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

package net.java.jveez.ui.viewer;

import java.awt.AWTKeyStroke;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import net.java.jveez.utils.Utils;

public abstract class ViewerAction extends AbstractAction {

    private String mouseGesture;
    private BufferedImage image;

    protected ViewerAction() {
        onCreate();
    }

    protected abstract void onCreate();

    protected abstract void onExecute();

    public void actionPerformed(ActionEvent e) {
        onExecute();
    }

    public void setName(String name) {
        putValue(NAME, name);
    }

    public String getName() {
        return (String) getValue(NAME);
    }

    public void setIcon(Icon icon) {
        putValue(Action.SMALL_ICON, icon);
    }

    public void setKeystroke(AWTKeyStroke keyStroke) {
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    public void setDescription(String description) {
        putValue(SHORT_DESCRIPTION, description);
    }

    public String getDescription() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    public void setMouseGesture(String mouseGesture) {
        this.mouseGesture = mouseGesture;
    }

    public boolean hasMouseGesture() {
        return mouseGesture != null;
    }

    public AWTKeyStroke getKeyStroke() {
        return (KeyStroke) getValue(ACCELERATOR_KEY);
    }

    public String getMouseGesture() {
        return mouseGesture;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImageResourceName(String imageResourceName) {
        this.image = Utils.loadImage(imageResourceName);
    }
}
