package ch.jfactory.image;/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class DropShadowImage extends BufferedImage
{
    public static int DEFAULT_DISTANCE = 1;

    public static int DEFAULT_SHADOW_SIZE = 4;

    protected float shadowOpacity = 0.5F;

    protected Color shadowColor = new Color( 0 );

    private int shadowSize = 0;

    public DropShadowImage( final Image image )
    {
        this( image, DEFAULT_SHADOW_SIZE, DEFAULT_DISTANCE, DEFAULT_DISTANCE );
    }

    public DropShadowImage( final Image image, final int shadowSize, final int xDistance, final int yDistance )
    {
        super( image.getWidth( null ), image.getHeight( null ), BufferedImage.TYPE_INT_ARGB );
        this.shadowSize = shadowSize;
        final Graphics2D g = createGraphics();
        g.drawImage( createDropShadow( image ), xDistance, yDistance, null );
        g.drawImage( image, 0, 0, null );
    }

    private BufferedImage prepareImage( final Image image )
    {
        final BufferedImage subject = new BufferedImage( getWidth() + 2 * shadowSize, getHeight() + 2 * shadowSize, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D g2 = subject.createGraphics();
        g2.drawImage( image, shadowSize, shadowSize, null );
        g2.dispose();
        return subject;
    }

    private BufferedImage createDropShadow( final Image image )
    {
        final BufferedImage subject = prepareImage( image );
        final BufferedImage shadow = new BufferedImage( subject.getWidth(), subject.getHeight(), BufferedImage.TYPE_INT_ARGB );
        final BufferedImage shadowMask = createShadowMask( subject );
        getLinearBlurOp( shadowSize ).filter( shadowMask, shadow );
        final BufferedImage result = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB );
        result.getGraphics().drawImage( shadow, -shadowSize, -shadowSize, null );
        return result;
    }

    private BufferedImage createShadowMask( final BufferedImage image )
    {
        final BufferedImage mask = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB );
        final Graphics2D g2d = mask.createGraphics();
        g2d.drawImage( image, 0, 0, null );
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_IN, shadowOpacity ) );
        g2d.setColor( shadowColor );
        g2d.fillRect( 0, 0, image.getWidth(), image.getHeight() );
        g2d.dispose();
        return mask;
    }

    private static ConvolveOp getLinearBlurOp( final int size )
    {
        final float[] data = new float[size * size];
        final float value = 1.0F / (float) ( size * size );
        for ( int i = 0; i < data.length; i++ )
        {
            data[i] = value;
        }
        return new ConvolveOp( new Kernel( size, size, data ) );
    }
}
