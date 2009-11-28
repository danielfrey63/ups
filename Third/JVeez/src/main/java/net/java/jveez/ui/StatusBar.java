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

package net.java.jveez.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import net.java.jveez.utils.Utils;

public class StatusBar extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257288019714192697L;

    private final ActivityNotifier activityNotifier = new ActivityNotifier();

    private final JLabel memoryLabel = new JLabel( "-", JLabel.CENTER );

    private final JLabel messageLabel = new JLabel( "", JLabel.LEADING );

    private final JButton gcButton = new JButton( "GC" );

    private final Timer memoryTimer = new Timer( "MemoryTimer", true );

    public StatusBar()
    {
        super( new BorderLayout() );

        final JPanel messagePanel = new JPanel( new BorderLayout() );
        messagePanel.add( messageLabel, BorderLayout.CENTER );
        messagePanel.add( activityNotifier, BorderLayout.EAST );

        memoryLabel.setPreferredSize( new Dimension( 100, 1 ) );
        final JPanel memoryPanel = new JPanel( new BorderLayout() );
        memoryPanel.add( memoryLabel, BorderLayout.WEST );
        memoryPanel.add( gcButton, BorderLayout.EAST );
        memoryPanel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );

        messageLabel.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED ) );

        memoryTimer.schedule( new TimerTask()
        {
            public void run()
            {
                updateMemoryLabel();
            }
        }, 2000, 1000 );

        gcButton.addActionListener( new ActionListener()
        {
            private long lastInvocation;

            public void actionPerformed( final ActionEvent e )
            {
                final long now = System.currentTimeMillis();
                // if the GC button has been already been pressed less than 2 seconds ago, perform a hard GC
                Utils.freeMemory( now - lastInvocation < 2000 );
                lastInvocation = now;
            }
        } );

        this.add( messagePanel, BorderLayout.CENTER );
        this.add( memoryPanel, BorderLayout.EAST );
    }

    public void showMessage( final String message )
    {
        messageLabel.setText( message );
    }

    public void showActivity( final boolean activity )
    {
        activityNotifier.setActive( activity );
    }

    private void updateMemoryLabel()
    {
        final Runtime runtime = Runtime.getRuntime();
        final long totalMemory = runtime.totalMemory();
        final long usedMemory = totalMemory - Runtime.getRuntime().freeMemory();
        final String label = String.format( "%dk / %dk", usedMemory / 1024, totalMemory / 1024 );
        memoryLabel.setText( label );
    }
}
