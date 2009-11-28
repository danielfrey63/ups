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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;

public class AnimatedImageComponent implements AnimationRenderer, AnimatedComponent
{
    private static final Logger LOG = Logger.getLogger( AnimatedImageComponent.class );

    private BufferedImage image;

    private BufferedImage reducedImage;

    private final AnimationState state = new AnimationState();

    private final AnimationRenderer delegateRenderer = new AnimationImageRenderer( this );

    public AnimatedImageComponent()
    {
    }

    public void setImage( final BufferedImage image )
    {
        LOG.debug( "new image size is " + image.getWidth() + "x" + image.getHeight() );

        this.image = image;

//        if (reducedImage != null) {
//            reducedImage.flush();
//            reducedImage = null;
//        }
//
//        if (image != null) {
//            // if image is big then we will use a reduced image to play animations ...
//            reducedImage = image;
//            while (reducedImage.getWidth() > 800 && reducedImage.getHeight() > 600) {
//                // Don't use any other rendering hint as it will be extremely slow!
//                reducedImage = BlurUtils.createScaledInstance(image, 800, 600, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR, false);
//            }
//        }
    }

    public int getImageWidth()
    {
        if ( image != null )
        {
            return image.getWidth();
        }
        else
        {
            return 0;
        }
    }

    public int getImageHeight()
    {
        if ( image != null )
        {
            return image.getHeight();
        }
        else
        {
            return 0;
        }
    }

    public AnimationState getAnimationState()
    {
        return state;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void render( final Graphics2D g2d, final int width, final int height, final boolean animating )
    {
        if ( image == null )
        {
            return;
        }

        LOG.debug( "image size is " + image.getWidth() + "x" + image.getHeight() + ", state is " + state );

        if ( !animating || reducedImage == null )
        {
            delegateRenderer.render( g2d, width, height, animating );
//            g2d.drawImage(image, state.getAffineTransform(), null);
        }
        else
        {
            final AnimationState scaledState = new AnimationState( state );
            scaledState.sx *= 2;
            scaledState.sy *= 2;
            scaledState.rx *= 0.5;
            scaledState.ry *= 0.5;
            g2d.drawImage( reducedImage, scaledState.getAffineTransform(), null );
        }
    }
}
