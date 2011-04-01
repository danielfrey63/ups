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
package net.java.jveez.ui.viewer.anim;

import java.awt.geom.Point2D;
import javax.swing.JComponent;

public class ZoomAnimation implements Animation
{
    private final double zoomFactor;

    private double zoomFactorIncrement;

    private final long duration;

    private final AnimatedComponent targetComponent;

    private final JComponent viewerCanvas;

    private final AnimationState initialState = new AnimationState();

    public ZoomAnimation( final double zoomFactor, final long duration, final AnimatedComponent targetComponent, final JComponent viewerCanvas )
    {
        this.zoomFactor = zoomFactor;
        this.duration = duration;
        this.targetComponent = targetComponent;
        this.viewerCanvas = viewerCanvas;
    }

    public void init()
    {
        initialState.set( targetComponent.getAnimationState() );
        if ( duration > 0 )
        {
            zoomFactorIncrement = ( zoomFactor * initialState.sx - initialState.sx ) / duration;
        }
    }

    public void perform( final long time )
    {
        final float canvasCenterX = viewerCanvas.getWidth() / 2.0f;
        final float canvasCenterY = viewerCanvas.getHeight() / 2.0f;

        final AnimationState state = targetComponent.getAnimationState();

        final Point2D.Double transformedCanvasCenter = state.getImagePointAt( canvasCenterX, canvasCenterY );

        if ( time >= duration )
        {
            state.sx = initialState.sx * zoomFactor;
            state.sy = initialState.sy * zoomFactor;
        }
        else
        {
            state.sx = initialState.sx + ( zoomFactorIncrement * time );
            state.sy = initialState.sy + ( zoomFactorIncrement * time );
        }

        final Point2D.Double newTransformedCanvasCenter = state.getCanvasPointAt( transformedCanvasCenter.x, transformedCanvasCenter.y );

        // keep the point of the image at the center of the screen at the same position
        state.x -= newTransformedCanvasCenter.x - canvasCenterX;
        state.y -= newTransformedCanvasCenter.y - canvasCenterY;
    }

    public long duration()
    {
        return duration;
    }
}
