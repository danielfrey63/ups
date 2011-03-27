/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.image;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * A BufferedImage of one of four types of arrow (up, down, left or right) drawn to the size specified on the constructor. <p/> Original code by Andrew J Armstrong.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/04/15 23:03:21 $
 */
public class ArrowImage extends BufferedImage
{
    public ArrowImage( final ArrowDirection nArrowDirection )
    {
        this( 15, 9, nArrowDirection );
    }

    public ArrowImage( final int nWidth, final int nHeight, final ArrowDirection nArrowDirect )
    {
        super( nWidth, nHeight, TYPE_INT_ARGB_PRE );
        // Set the width, height and image type

        // Create a graphics context for this buffered image and set rendering options
        final Map map = new HashMap();
        map.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        map.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        final RenderingHints hints = new RenderingHints( map );
        final Graphics2D g2 = this.createGraphics();
        g2.setRenderingHints( hints );

        final float h = getHeight();
        final float w = getWidth();
        final float w13 = w / 3;
        final float w12 = w / 2;
        final float w23 = w * 2 / 3;
        final float h13 = h / 3;
        final float h12 = h / 2;
        final float h23 = h * 2 / 3;

        final GeneralPath pathArrow = new GeneralPath();
        final Color lightColor = Color.orange;
        final Color darkColor = Color.red;
        if ( nArrowDirect == ArrowDirection.ARROW_UP )
        {
            pathArrow.moveTo( w12, h12 );
            pathArrow.lineTo( w12, 0 );
            pathArrow.lineTo( w, h - 1 );
            pathArrow.lineTo( 0, h - 1 );
            pathArrow.closePath();
            g2.setPaint( new GradientPaint( w13, h13, lightColor, w, h - 1, darkColor ) );
            g2.fill( pathArrow );

            g2.setColor( SystemColor.controlDkShadow );
            g2.draw( new Line2D.Float( 0, h - 1, w, h - 1 ) );
            g2.setColor( darkColor );
            g2.draw( new Line2D.Float( w12, 0, w, h - 1 ) );
            g2.setColor( lightColor );
            g2.draw( new Line2D.Float( 0, h - 1, w12, 0 ) );
        }
        else if ( nArrowDirect == ArrowDirection.ARROW_DOWN )
        {
            pathArrow.moveTo( w12, h12 );
            pathArrow.lineTo( w, 0 );
            pathArrow.lineTo( w12, h - 1 );
            pathArrow.closePath();
            g2.setPaint( new GradientPaint( 0, 0, lightColor, w23, h23, darkColor ) );
            g2.fill( pathArrow );

            g2.setColor( SystemColor.controlDkShadow );
            g2.draw( new Line2D.Float( w, 0, w12, h - 1 ) );
            g2.setColor( darkColor );
            g2.draw( new Line2D.Float( w12, h - 1, 0, 0 ) );
            g2.setColor( lightColor );
            g2.draw( new Line2D.Float( 0, 0, w, 0 ) );
        }
        else if ( nArrowDirect == ArrowDirection.ARROW_LEFT )
        {
            pathArrow.moveTo( w - 1, h13 );
            pathArrow.lineTo( w13, h13 );
            pathArrow.lineTo( w13, 0 );
            pathArrow.lineTo( 0, h12 );
            pathArrow.lineTo( w13, h - 1 );
            pathArrow.lineTo( w13, h23 );
            pathArrow.lineTo( w - 1, h23 );
            pathArrow.closePath();
            g2.setPaint( new GradientPaint( 0, 0, Color.white, 0, h, darkColor ) );
            g2.fill( pathArrow );

            pathArrow.reset();
            pathArrow.moveTo( w13, 0 );
            pathArrow.lineTo( w13, h13 );
            pathArrow.moveTo( w - 1, h13 );
            pathArrow.lineTo( w - 1, h23 );
            pathArrow.lineTo( w13, h23 );
            pathArrow.lineTo( w13, h - 1 );
            g2.setColor( SystemColor.controlDkShadow );
            g2.draw( pathArrow );

            g2.setColor( darkColor );
            g2.draw( new Line2D.Float( 0, h12, w13, h - 1 ) );

            pathArrow.reset();
            pathArrow.moveTo( 0, h12 );
            pathArrow.lineTo( w13, 0 );
            pathArrow.moveTo( w13, h13 );
            pathArrow.lineTo( w - 1, h13 );
            g2.setColor( lightColor );
            g2.draw( pathArrow );
        }
        else
        {
            pathArrow.moveTo( 0, h13 );
            pathArrow.lineTo( w23, h13 );
            pathArrow.lineTo( w23, 0 );
            pathArrow.lineTo( w - 1, h12 );
            pathArrow.lineTo( w23, h - 1 );
            pathArrow.lineTo( w23, h23 );
            pathArrow.lineTo( 0, h23 );
            pathArrow.closePath();
            g2.setPaint( new GradientPaint( 0, 0, Color.white, 0, h, darkColor ) );
            g2.fill( pathArrow );

            pathArrow.reset();
            pathArrow.moveTo( 0, h23 );
            pathArrow.lineTo( w23, h23 );
            pathArrow.moveTo( w23, h - 1 );
            pathArrow.lineTo( w - 1, h12 );
            g2.setColor( SystemColor.controlDkShadow );
            g2.draw( pathArrow );

            g2.setColor( darkColor );
            g2.draw( new Line2D.Float( w - 1, h12, w23, 0 ) );

            pathArrow.reset();
            pathArrow.moveTo( w23, 0 );
            pathArrow.lineTo( w23, h13 );
            pathArrow.lineTo( 0, h13 );
            pathArrow.lineTo( 0, h23 );
            pathArrow.moveTo( w23, h23 );
            pathArrow.lineTo( w23, h - 1 );
            g2.setColor( lightColor );
            g2.draw( pathArrow );
        }
    }

    public static class ArrowDirection
    {
        public static final ArrowDirection ARROW_DOWN = new ArrowDirection();

        public static final ArrowDirection ARROW_LEFT = new ArrowDirection();

        public static final ArrowDirection ARROW_RIGHT = new ArrowDirection();

        public static final ArrowDirection ARROW_UP = new ArrowDirection();

        private ArrowDirection()
        {
        }
    }
}

