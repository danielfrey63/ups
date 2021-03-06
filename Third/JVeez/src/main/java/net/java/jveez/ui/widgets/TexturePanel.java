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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class TexturePanel extends JPanel
{
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3258413911131763766L;

    private TexturePaint texturePaint;

    public TexturePanel( final BufferedImage texture, final LayoutManager layout, final boolean isDoubleBuffered )
    {
        super( layout, isDoubleBuffered );
        setupTexture( texture );
    }

    public TexturePanel( final BufferedImage texture, final LayoutManager layout )
    {
        super( layout );
        setupTexture( texture );
    }

    public TexturePanel( final BufferedImage texture, final boolean isDoubleBuffered )
    {
        super( isDoubleBuffered );
        setupTexture( texture );
    }

    public TexturePanel( final BufferedImage texture )
    {
        super();
        setupTexture( texture );
    }

    private void setupTexture( final BufferedImage texture )
    {
        final Rectangle2D.Float rectangle = new Rectangle2D.Float( 0, 0, texture.getWidth(), texture.getHeight() );
        this.texturePaint = new TexturePaint( texture, rectangle );
    }

    protected void paintComponent( final Graphics g )
    {
        final Graphics2D g2d = (Graphics2D) g;

        final Paint oldPaint = g2d.getPaint();
        g2d.setPaint( texturePaint );
        g2d.fillRect( 0, 0, getWidth(), getHeight() );
        g2d.setPaint( oldPaint );
    }
}
