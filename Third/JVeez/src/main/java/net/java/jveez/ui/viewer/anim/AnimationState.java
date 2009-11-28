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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class AnimationState
{
    public double x;

    public double y;

    public double sx;

    public double sy;

    public double r;

    public double rx;

    public double ry;

    public AnimationState()
    {
    }

    public AnimationState( final double x, final double y, final double sx, final double sy, final double r, final double rx, final double ry )
    {
        this();
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.r = r;
        this.rx = rx;
        this.ry = ry;
    }

    public AnimationState( final AnimationState state )
    {
        this();
        set( state );
    }

    public void set( final AnimationState state )
    {
        this.x = state.x;
        this.y = state.y;
        this.sx = state.sx;
        this.sy = state.sy;
        this.r = state.r;
        this.rx = state.rx;
        this.ry = state.ry;
    }

    public AffineTransform getAffineTransform()
    {
        final AffineTransform transform = new AffineTransform();
        transform.translate( x, y );
        transform.scale( sx, sy );
        transform.rotate( r, rx, ry );
        return transform;
    }

    public Point2D.Double getImagePointAt( final double x, final double y )
    {
        try
        {
            return (Point2D.Double) getAffineTransform().inverseTransform( new Point2D.Double( x, y ), new Point2D.Double() );
        }
        catch ( NoninvertibleTransformException e )
        {
            // should never happen since this is an affine transform - true ? :-)
            throw new RuntimeException( e );
        }
    }

    public Point2D.Double getCanvasPointAt( final double x, final double y )
    {
        return (Point2D.Double) getAffineTransform().transform( new Point2D.Double( x, y ), new Point2D.Double() );
    }

    public static void add( final AnimationState state1, final AnimationState state2, final AnimationState target )
    {
        target.x = state1.x + state2.x;
        target.y = state1.y + state2.y;
        target.sx = state1.sx + state2.sx;
        target.sy = state1.sy + state2.sy;
        target.r = state1.r + state2.r;
        target.rx = state1.rx + state2.rx;
        target.ry = state1.ry + state2.ry;
    }

    public static void sub( final AnimationState state1, final AnimationState state2, final AnimationState target )
    {
        target.x = state1.x - state2.x;
        target.y = state1.y - state2.y;
        target.sx = state1.sx - state2.sx;
        target.sy = state1.sy - state2.sy;
        target.r = state1.r - state2.r;
        target.rx = state1.rx - state2.rx;
        target.ry = state1.ry - state2.ry;
    }

    public static void delta( final AnimationState initialState, final AnimationState finalState, final long duration, final AnimationState deltaState )
    {
        deltaState.x = ( finalState.x - initialState.x ) / duration;
        deltaState.y = ( finalState.y - initialState.y ) / duration;
        deltaState.sx = ( finalState.sx - initialState.sx ) / duration;
        deltaState.sy = ( finalState.sy - initialState.sy ) / duration;
        deltaState.r = ( finalState.r - initialState.r ) / duration;
        deltaState.rx = ( finalState.rx - initialState.rx ) / duration;
        deltaState.ry = ( finalState.ry - initialState.ry ) / duration;
    }

    public static void step( final AnimationState initialState, final AnimationState deltaState, final long time, final AnimationState targetState )
    {
        targetState.x = initialState.x + deltaState.x * time;
        targetState.y = initialState.y + deltaState.y * time;
        targetState.sx = initialState.sx + deltaState.sx * time;
        targetState.sy = initialState.sy + deltaState.sy * time;
        targetState.r = initialState.r + deltaState.r * time;
        targetState.rx = initialState.rx + deltaState.rx * time;
        targetState.ry = initialState.ry + deltaState.ry * time;
    }

    public static void mult( final AnimationState state, final double c, final AnimationState target )
    {
        target.x = state.x * c;
        target.y = state.y * c;
        target.sx = state.sx * c;
        target.sy = state.sy * c;
        target.r = state.r * c;
        target.rx = state.rx * c;
        target.ry = state.ry * c;
    }

    public static void div( final AnimationState state, final double f, final AnimationState target )
    {
        final double c = 1.0 / f;
        target.x = state.x * c;
        target.y = state.y * c;
        target.sx = state.sx * c;
        target.sy = state.sy * c;
        target.r = state.r * c;
        target.rx = state.rx * c;
        target.ry = state.ry * c;
    }

    public String toString()
    {
        return "AnimationState@" + Integer.toHexString( hashCode() ) + "[" +
                "x=" + x +
                ", y=" + y +
                ", sx=" + sx +
                ", sy=" + sy +
                ", r=" + r +
                ", rx=" + rx +
                ", ry=" + ry +
                "]";
    }
}
