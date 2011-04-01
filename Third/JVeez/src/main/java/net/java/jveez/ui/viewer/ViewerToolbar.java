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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class ViewerToolbar extends JPanel
{
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3258407348405219889L;

    private final JLabel imageIndexLabel = new JLabel( "", JLabel.HORIZONTAL );

    private final JLabel imageNameLabel = new JLabel( "", JLabel.LEADING );

    private final JComponent controlPanel;

    public ViewerToolbar( final JComponent controlPanel )
    {
        super( new BorderLayout() );
        this.controlPanel = controlPanel;

        setupComponents();
        layoutComponents();
    }

    private void setupComponents()
    {
        setOpaque( true );

        final int indexTextWidth = imageIndexLabel.getFontMetrics( imageIndexLabel.getFont() ).stringWidth( " 000000/00000 " );
        imageIndexLabel.setPreferredSize( new Dimension( indexTextWidth, 0 ) );

        imageIndexLabel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );
        imageNameLabel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );
    }

    private void layoutComponents()
    {
        this.add( imageIndexLabel, BorderLayout.WEST );
        this.add( imageNameLabel, BorderLayout.CENTER );

        final JPanel wrapperPanel = new JPanel( new BorderLayout( 0, 0 ) );
        wrapperPanel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );
        wrapperPanel.add( controlPanel, BorderLayout.CENTER );

        this.add( wrapperPanel, BorderLayout.EAST );
    }

    public void update( final String imageName, final int imageIndex, final int imageCount )
    {
        imageIndexLabel.setText( String.format( " %d/%d ", imageIndex + 1, imageCount ) );
        imageNameLabel.setText( " " + imageName );
    }

    public void clear()
    {
        imageIndexLabel.setText( " " );
        imageNameLabel.setText( " " );
    }
}
