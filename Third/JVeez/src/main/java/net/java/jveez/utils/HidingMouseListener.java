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
package net.java.jveez.utils;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class HidingMouseListener implements MouseMotionListener, FocusListener
{
    private static final Logger LOG = Logger.getLogger( HidingMouseListener.class );

    private final Component target;

    private final Cursor defaultCursor;

    private final Cursor emptyCursor;

    private volatile boolean cursorHidden = false;

    private final Timer timer;

    private long lastMove;

    public HidingMouseListener( final Component target )
    {
        this.target = target;
        this.defaultCursor = target.getCursor();
        this.emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor( new ImageIcon( new byte[0] ).getImage(), new Point( 0, 0 ), "emptyCursor" );

        this.timer = new Timer( "CursorHiding", true );
        timer.schedule( new TimerTask()
        {
            public void run()
            {
                if ( lastMove != 0 && !cursorHidden && System.currentTimeMillis() - lastMove > 2000 )
                {
                    LOG.debug( "hiding mouse" );
                    HidingMouseListener.this.target.setCursor( emptyCursor );
                    cursorHidden = true;
                }
            }
        }, 2000, 500 );

        target.addMouseMotionListener( this );
        target.addFocusListener( this );
    }

    public void focusGained( final FocusEvent e )
    {
        reset();
    }

    public void focusLost( final FocusEvent e )
    {
    }

    public void mouseDragged( final MouseEvent e )
    {
        reset();
    }

    public void mouseMoved( final MouseEvent e )
    {
        reset();
    }

    private void reset()
    {
        lastMove = System.currentTimeMillis();
        if ( cursorHidden )
        {
            target.setCursor( defaultCursor );
            cursorHidden = false;
        }
    }
}
