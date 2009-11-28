/* ====================================================================
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

package ch.jfactory.animation.fading;

import ch.jfactory.animation.Paintable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * An animation that fades into a given color. The animation starts with a totally transparent color, fading it towards
 * the given final color by in-/decrementing the alpha channel by one per cycle.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class FadingPaintable extends Paintable
{
    /**
     * The default deleay to wait between scroll steps.
     */
    private static final int DEFAULT_SCROLL_DELAY = 10;

    /**
     * The actual scroll delay. Defaults to {@link #DEFAULT_SCROLL_DELAY}.
     */
    private final int scrollDelay = DEFAULT_SCROLL_DELAY;

    /**
     * The final color that will be approached stepwise by setting the alpha channel one bit towards the alpha channel
     * of the final color
     */
    private final Color finalColor;

    /**
     * The shape to draw the fading area.
     */
    private Shape shape;

    /**
     * The actual color.
     */
    private transient Color color;

    /**
     * Constructs a new instance. The constructor registers this Paintable with the animation.
     *
     * @param finalColor The color to fade towards
     */
    public FadingPaintable( final Color finalColor )
    {
        this.finalColor = finalColor;
    }

    /**
     * Runs until the final color is reached.
     */
    public void run()
    {
        final Dimension size = getAnimation().getSize();
        shape = new Rectangle( 0, 0, (int) size.getWidth(), (int) size.getHeight() );
        color = new Color( finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), 0 );

        while ( isRun() )
        {
            if ( color.equals( finalColor ) )
            {
                stop();
            }
            else
            {
                color = new Color( color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() + 1 );
                getAnimation().repaint();
            }

            try
            {
                Thread.sleep( scrollDelay );
            }
            catch ( InterruptedException e )
            {
            }
        }
    }

    /**
     * Paints the color.
     *
     * @param g the graphics to paint on
     */
    public void paint( final Graphics2D g )
    {
        g.setColor( color );
        g.fill( shape );
    }
}
