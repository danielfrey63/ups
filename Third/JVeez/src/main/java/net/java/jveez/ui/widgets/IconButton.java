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

package net.java.jveez.ui.widgets;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import net.java.jveez.utils.Utils;

public class IconButton extends JButton {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258409538721166389L;

    private static BufferedImage mouseOverIcon = Utils.loadImage("net/java/jveez/icons/mouse-over.png");
    private static BufferedImage mousePressedIcon = Utils.loadImage("net/java/jveez/icons/mouse-pressed.png");
    private BufferedImage icon;
    private boolean mouseOver;
    private boolean mousePressed;

    public IconButton(BufferedImage icon) {
        this.icon = icon;
        setSize(icon.getWidth(), icon.getHeight());
        setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()));
        setVisible(true);
        setBackground(Color.WHITE);
        setOpaque(true);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                repaint();
            }
        });
    }

    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        if (mousePressed) {
            g2d.drawImage(mousePressedIcon, 0, 0, width, height, null);
        }
        else if (mouseOver) {
            g2d.drawImage(mouseOverIcon, 0, 0, width, height, null);
        }
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.drawImage(icon, 0, 0, width, height, null);
    }
}
