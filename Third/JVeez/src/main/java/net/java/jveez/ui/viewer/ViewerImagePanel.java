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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import net.java.jveez.ui.viewer.anim.AnimatedImageComponent;
import net.java.jveez.ui.viewer.anim.Animation;
import net.java.jveez.ui.viewer.anim.AnimationExecutor;
import net.java.jveez.ui.viewer.anim.AnimationState;
import net.java.jveez.ui.viewer.anim.ResetAnimation;
import net.java.jveez.ui.viewer.anim.RotationAnimation;
import net.java.jveez.ui.viewer.anim.ZoomAnimation;
import org.apache.log4j.Logger;

public class ViewerImagePanel extends JComponent
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3617292324544132916L;

    private static final Logger LOG = Logger.getLogger( ViewerImagePanel.class );

    private static final int ANIMATION_FPS = 30;

    private static final long DEFAULT_ANIMATION_DURATION = 250;

    private volatile int animationCounter;

    private final AnimatedImageComponent imageComponent = new AnimatedImageComponent();

    private final AnimationExecutor executor = new AnimationExecutor();

    private boolean highQuality = false;

    private final ImageTranslater imageTranslater;

    public ViewerImagePanel()
    {
        super();
        imageTranslater = new ImageTranslater();

        addComponentListener( new ComponentAdapter()
        {
            public void componentResized( final ComponentEvent e )
            {
                identityState();
            }
        } );
    }

    public void setImage( final BufferedImage image )
    {
        if ( image != null )
        {
            imageComponent.setImage( image );
            resetStateIn( 0 );
        }
        else
        {
            imageComponent.setImage( null );
            repaint();
        }
    }

    public void rotateLeft()
    {
        postAnimation( new RotationAnimation( -Math.PI / 2.0, DEFAULT_ANIMATION_DURATION, imageComponent, this ) );
    }

    public void rotateRight()
    {
        postAnimation( new RotationAnimation( Math.PI / 2.0, DEFAULT_ANIMATION_DURATION, imageComponent, this ) );
    }

    public void resetState()
    {
        resetStateIn( DEFAULT_ANIMATION_DURATION );
    }

    public void setAnimationState( final AnimationState state )
    {
        postAnimation( new ResetAnimation( state, 0, imageComponent ) );
    }

    public void identityState()
    {
        final AnimationState state = imageComponent.getAnimationState();
        postAnimation( new ResetAnimation( state, 0, imageComponent ) );
    }

    public void resetStateIn( final long duration )
    {
        final float imageWidth = imageComponent.getImageWidth();
        final float imageHeight = imageComponent.getImageHeight();
        final float canvasWidth = getWidth();
        final float canvasHeight = getHeight();

        // no picture so nothing to do
        if ( imageWidth == 0 )
        {
            return;
        }

        float zoomLevel = Math.min( canvasWidth / imageWidth, canvasHeight / imageHeight );
        if ( zoomLevel > 1.0f )
        {
            zoomLevel = 1.0f;
        }

        final AnimationState state = new AnimationState( imageComponent.getAnimationState() );
        state.sx = zoomLevel;
        state.sy = zoomLevel;
        state.x = ( canvasWidth - zoomLevel * imageWidth ) / 2.0f;
        state.y = ( canvasHeight - zoomLevel * imageHeight ) / 2.0f;
        state.r = 0;
        state.rx = imageWidth / 2.0f;
        state.ry = imageHeight / 2.0f;

        postAnimation( new ResetAnimation( state, duration, imageComponent ) );
    }

    public void zoomIn()
    {
        postAnimation( new ZoomAnimation( 1.2, DEFAULT_ANIMATION_DURATION, imageComponent, this ) );
    }

    public void zoomOut()
    {
        postAnimation( new ZoomAnimation( 0.8, DEFAULT_ANIMATION_DURATION, imageComponent, this ) );
    }

    public void setOffset( final double x, final double y )
    {
        final AnimationState state = imageComponent.getAnimationState();
        state.x = x;
        state.y = y;
        repaint();
    }

    public double getOffsetX()
    {
        return imageComponent.getAnimationState().x;
    }

    public double getOffsetY()
    {
        return imageComponent.getAnimationState().y;
    }

    public AnimationState getAnimationState()
    {
        return imageComponent.getAnimationState();
    }

    public boolean isHighQuality()
    {
        return highQuality;
    }

    public void setHighQuality( final boolean highQuality )
    {
        this.highQuality = highQuality;
        repaint();
    }

    private boolean isAnimating()
    {
        return animationCounter > 0;
    }

    private void postAnimation( final Animation animation )
    {
        // Be careful: Can occur that cuncurrent animations get executed in an unexcpeted order if 0ed and non-0ed are mixed.
        if ( animation.duration() == 0 )
        {
            animation.init();
            animation.perform( 0 );
            repaint();
            animationCounter++;
        }
        else
        {
            executor.post( new Runnable()
            {
                public void run()
                {
                    animation.init();

                    final long sleepTime = 1000L / ANIMATION_FPS;
                    final long startTime = System.currentTimeMillis();
                    long elapsed;
                    do
                    {
                        try
                        {
                            Thread.sleep( sleepTime );
                        }
                        catch ( Exception e )
                        {
                            e.printStackTrace();
                        }

                        elapsed = System.currentTimeMillis() - startTime;
                        animation.perform( elapsed );
                        repaint();
                    }
                    while ( elapsed < animation.duration() );
                    animation.perform( animation.duration() );
                    repaint();

                    animationCounter--;
                }
            } );
        }
    }

    public void paint( final Graphics g )
    {
        final Graphics2D g2d = (Graphics2D) g;

        final int width = getWidth();
        final int height = getHeight();

        g2d.setColor( getBackground() );
        g2d.fillRect( 0, 0, width, height );

        final boolean animating = isAnimating();
        final boolean quality = !animating && highQuality;

        g2d.setRenderingHint( RenderingHints.KEY_RENDERING, quality ? RenderingHints.VALUE_RENDER_QUALITY : RenderingHints.VALUE_RENDER_SPEED );

        imageComponent.render( g2d, width, height, animating );

//        g2d.setColor(Color.RED);
//        g2d.drawLine(width / 2, 0, width / 2, height - 1);
//        g2d.drawLine(0, height / 2, width - 1, height / 2);
    }

    public void setAnimating( final boolean animating )
    {
        if ( animating )
        {
            addMouseListener( imageTranslater );
            addMouseMotionListener( imageTranslater );
        }
        else
        {
            removeMouseListener( imageTranslater );
            removeMouseMotionListener( imageTranslater );
        }
    }

    private class ImageTranslater implements MouseListener, MouseMotionListener
    {
        private boolean active = false;

        private final Point2D.Double initialOffset = new Point2D.Double();

        private final Point dragStart = new Point();

        public void mousePressed( final MouseEvent e )
        {
            if ( !isAnimating() && e.getButton() == MouseEvent.BUTTON1 )
            {
                LOG.debug( "mousePressed()" );
                animationCounter++;
                active = true;
                initialOffset.setLocation( getOffsetX(), getOffsetY() );
                dragStart.setLocation( e.getPoint() );
                setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
            }
        }

        public void mouseReleased( final MouseEvent e )
        {
            if ( active && e.getButton() == MouseEvent.BUTTON1 )
            {
                LOG.debug( "mouseReleased()" );
                animationCounter--;
                active = false;
                setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
                setOffset( initialOffset.x + e.getX() - dragStart.x, initialOffset.y + e.getY() - dragStart.y );
            }
        }

        public void mouseDragged( final MouseEvent e )
        {
            if ( active )
            {
                setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
                assureBounds();
                setOffset( initialOffset.x + e.getX() - dragStart.x, initialOffset.y + e.getY() - dragStart.y );
            }
        }

        // unused

        public void mouseMoved( final MouseEvent e )
        {
        }

        public void mouseClicked( final MouseEvent e )
        {
        }

        public void mouseEntered( final MouseEvent e )
        {
        }

        public void mouseExited( final MouseEvent e )
        {
        }

        // Makes sure the image stays within the bounds of the insets. There is only one exception: The display area is too
        // small to show the entire image. Here the mouse may move the image beyound the inset bounds.
        private void assureBounds()
        {
            final int cw = getWidth();
            final int ch = getHeight();
            final int iw = imageComponent.getImage().getWidth( null );
            final int ih = imageComponent.getImage().getHeight( null );
            if ( cw != 0 && ch != 0 )
            {
                final Insets in = getInsets();
                if ( cw >= iw + in.right + in.left )
                {
//                    dragStart.x = Math.min(Math.max(dragStart.x, in.left), cw - in.right - iw);
                }
                else
                {
//                    dragStart.x = Math.min(Math.max(dragStart.x, cw - in.right - iw), in.left);
                }
                if ( ch >= ih + in.top + in.bottom )
                {
//                    dragStart.y = Math.min(Math.max(dragStart.y, in.top), ch - in.bottom - ih);
                }
                else
                {
//                    dragStart.y = Math.min(Math.max(dragStart.y, ch - in.bottom - ih), in.top);
                }
            }
        }

    }
}
