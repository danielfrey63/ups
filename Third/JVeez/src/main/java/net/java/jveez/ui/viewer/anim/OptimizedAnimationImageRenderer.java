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
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import net.java.jveez.utils.ImageUtils;
import org.apache.log4j.Logger;

public class OptimizedAnimationImageRenderer implements AnimationRenderer
{
    private static final Logger LOG = Logger.getLogger( OptimizedAnimationImageRenderer.class );

    private final AnimatedImageComponent imageComponent;

    // image creation
    private VolatileImage imagePortion;

    public OptimizedAnimationImageRenderer( final AnimatedImageComponent imageComponent )
    {
        this.imageComponent = imageComponent;
    }

    public void render( final Graphics2D g2d, final int width, final int height, final boolean animating )
    {
        final BufferedImage image = imageComponent.getImage();
        final AnimationState state = imageComponent.getAnimationState();

        try
        {
            final Rectangle2D r = new Rectangle2D.Double( 0, 0, width, height );
            final Shape s = state.getAffineTransform().createInverse().createTransformedShape( r );
            Rectangle2D bounds = s.getBounds2D();
            bounds = bounds.createIntersection( new Rectangle2D.Double( 0, 0, image.getWidth(), image.getHeight() ) );

            final int bx = (int) Math.floor( bounds.getX() );
            final int by = (int) Math.floor( bounds.getY() );
            final int bw = (int) Math.ceil( bounds.getWidth() );
            final int bh = (int) Math.ceil( bounds.getHeight() );

            final AnimationState st = new AnimationState( state );
            st.x = 0;
            st.y = 0;
            st.rx = bw / 2;
            st.ry = bh / 2;

            imagePortion = ImageUtils.createVolatileImage( bw, bh );

            do
            {
                final int returnCode = imagePortion.validate( ImageUtils.getGraphicsConfiguration() );
                if ( returnCode == VolatileImage.IMAGE_RESTORED )
                {
                    // Contents need to be restored
                    copyImagePortion( image, bx, by, bw, bh );
                }
                else if ( returnCode == VolatileImage.IMAGE_INCOMPATIBLE )
                {
                    // old vImg doesn't work with new GraphicsConfig; re-create it
                    imagePortion = ImageUtils.createVolatileImage( bw, bh );
                    copyImagePortion( image, bx, by, bw, bh );
                }

                g2d.drawImage( imagePortion, st.getAffineTransform(), null );
            }
            while ( imagePortion.contentsLost() );

            imagePortion.flush();
            imagePortion = null;

        }
        catch ( NoninvertibleTransformException e )
        {
            LOG.warn( "oops" );
        }

    }

    private void copyImagePortion( final BufferedImage source, final int bx, final int by, final int bw, final int bh )
    {
        final Graphics2D graphics = imagePortion.createGraphics();
        graphics.drawImage( source,
                0,
                0,
                bw,
                bh,
                bx,
                by,
                bx + bw,
                by + bh,
                null );
        graphics.dispose();
    }

}
