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
package net.java.jveez.ui.about;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.java.jveez.JVeez;
import net.java.jveez.ui.widgets.TexturePanel;
import net.java.jveez.utils.BuildInfo;
import net.java.jveez.utils.Utils;

public class AboutDialog extends JDialog
{
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3617857486338994993L;

    private JLabel logo;

    private JLabel label;

    private final JButton closeButton = new JButton( "close" );

    public AboutDialog()
    {
        super( JVeez.getMainFrame() );
        setupComponents();
        layoutComponents();
    }

    private void setupComponents()
    {
        // setup dialog
        this.setTitle( "About JVeez ..." );
        this.setModal( true );
        this.setUndecorated( true );

        // setup label & logo
        logo = new JLabel( Utils.loadIcon( "net/java/jveez/icons/jveez-2.png" ), JLabel.CENTER );
        logo.setOpaque( false );
        label = new JLabel( BuildInfo.TITLE, JLabel.CENTER );
        label.setOpaque( false );

        // setup button
        closeButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                setVisible( false );
            }
        } );
    }

    private void layoutComponents()
    {
        final TexturePanel panel = new TexturePanel( Utils.loadImage( "net/java/jveez/icons/steel-1.jpg" ), new BorderLayout() );
        setContentPane( panel );

        JPanel p = new JPanel( new BorderLayout() );
        p.setOpaque( false );
        p.add( logo, BorderLayout.CENTER );
        p.add( label, BorderLayout.SOUTH );
        panel.add( p, BorderLayout.CENTER );

        p = new JPanel();
        p.setOpaque( false );
        p.add( closeButton );

        panel.add( p, BorderLayout.SOUTH );
        setLocationRelativeTo( getOwner() );
        pack();
    }
}
