/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.Timer;

/**
 * GlassPane to put onto a JDialog while waiting. A simple use looks like this:
 *
 * <pre>
 * final WaitOverlay waitOverlay = new WaitOverlay(dialog, Color.white);
 * waitOverlay.setTickers(
 *     waitOverlay.new FadeInTicker(0.2f, 500),
 *     waitOverlay.new WaitForThreadTicker(dialog.getRootPane(), thread),
 *     waitOverlay.new FadeOutTicker(300),
 *     waitOverlay.new DisposeDialogTicker(dialog));
 *     waitOverlay.start();
 * </pre>
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class WaitOverlay extends JComponent
{
    private final int width;

    private final int height;

    private Timer timer;

    private boolean running;

    private float alpha = 1f;

    private int state = -1;

    private Ticker ticker;

    private Ticker[] tickers;

    private final Color overlayColor;

    public WaitOverlay( final JDialog dialog, final Color overlayColor )
    {
        this.width = dialog.getWidth();
        this.height = dialog.getHeight();
        dialog.setGlassPane( this );
        this.overlayColor = overlayColor;
    }

    public void start()
    {
        running = true;
        timer = new Timer( 30, new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                advance();
                ticker.tick();
            }
        } );
        timer.setRepeats( true );
        timer.start();
        setVisible( true );
    }

    public void stop()
    {
        if ( !running )
        {
            throw new IllegalStateException( "Not running" );
        }
        running = false;
        timer.stop();
        timer = null;
    }

    /**
     * Alpha (=opacity) of the background.
     *
     * @param alpha the opacity
     */
    public void setAlpha( final float alpha )
    {
        this.alpha = alpha;
    }

    public float getAlpha()
    {
        return alpha;
    }

    private void advance()
    {
        if ( ticker == null || ticker.isDone() )
        {
            state += 1;
            if ( state < tickers.length )
            {
                ticker = tickers[state];
                ticker.start();
            }
        }
    }

    @Override
    protected void paintComponent( final Graphics g )
    {
        super.paintComponent( g );
        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor( overlayColor );
        g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f - alpha ) );
        g2.fillRect( 0, 0, width, height );
        if ( ticker != null )
        {
            ticker.paint( g2 );
        }
    }

    public void setTickers( final Ticker... tickers )
    {
        this.tickers = tickers;
    }

    public abstract class Ticker
    {
        protected long start;

        protected WaitOverlay waitOverlay;

        public abstract void tick();

        public abstract boolean isDone();

        protected Ticker()
        {
            resetTime();
        }

        protected float getPercent( final float time )
        {
            return Math.min( 1f, (float) ( System.currentTimeMillis() - start ) / time );
        }

        protected void resetTime()
        {
            start = System.currentTimeMillis();
        }

        public void start()
        {
            resetTime();
        }

        public void stop()
        {
        }

        public void paint( final Graphics2D g2 )
        {
        }
    }

    public final class FadeInTicker extends Ticker
    {
        private final float dimTime;

        private final float endAlpha;

        public FadeInTicker( final float endAlpha, final float dimTime )
        {
            this.dimTime = dimTime;
            this.endAlpha = endAlpha;
        }

        public void tick()
        {
            final float alpha = 1f + ( endAlpha - 1f ) * getPercent( dimTime );
            setAlpha( alpha );
            WaitOverlay.this.getParent().repaint();
        }

        public boolean isDone()
        {
            return getPercent( dimTime ) >= 1f;
        }
    }

    public final class WaitForThreadTicker extends Ticker
    {
        private static final String IMAGE = "/ch/jfactory/component/wait-circle.png";

        private final BufferedImage waitIcon;

        private final JComponent component;

        private final Thread thread;

        private final float millisPerRotation = 1000f;

        private boolean joined;

        public WaitForThreadTicker( final JComponent component, final Thread thread ) throws IOException
        {
            this.thread = thread;
            this.component = component;
            final BufferedImage image = ImageIO.read( WaitForThreadTicker.class.getResourceAsStream( IMAGE ) );
            final AffineTransform at = new AffineTransform();
            at.scale( 2.0, 2.0 );
            final BufferedImageOp op = new AffineTransformOp( at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
            waitIcon = op.filter( image, null );
        }

        public void tick()
        {
            repaint();
        }

        public void start()
        {
            super.start();
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        thread.join();
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                    joined = true;
                }
            }.start();
        }

        public boolean isDone()
        {
            return joined;
        }

        @Override
        public void paint( final Graphics2D g2 )
        {
            final int imageWidth = waitIcon.getWidth();
            final int imageHeight = waitIcon.getHeight();
            final int componentWidth = component.getWidth();
            final int componentHeight = component.getHeight();
            final float millisPassed = ( (float) ( System.currentTimeMillis() - start ) ) % millisPerRotation;
            final float millisPerSegment = millisPerRotation / 12;
            final int segmentsPassed = (int) ( millisPassed / millisPerSegment );
            g2.rotate( segmentsPassed * Math.PI / 6.0, (double) componentWidth / 2.0, (double) componentHeight / 2.0 );
            g2.drawImage( waitIcon, ( componentWidth - imageWidth ) / 2, ( componentHeight - imageHeight ) / 2, null );
        }
    }

    public final class FadeOutTicker extends Ticker
    {
        private final float dimTime;

        private float startAlpha = -1;

        public FadeOutTicker( final float dimTime )
        {
            this.dimTime = dimTime;
        }

        public void tick()
        {
            if ( startAlpha == -1 )
            {
                startAlpha = getAlpha();
            }
            final float alpha = startAlpha + ( 1f - startAlpha ) * getPercent( dimTime );
            setAlpha( alpha );
            WaitOverlay.this.getParent().repaint();
        }

        public boolean isDone()
        {
            return getPercent( dimTime ) >= 1f;
        }
    }

    public final class DisposeDialogTicker extends Ticker
    {
        private final JDialog dialog;

        public DisposeDialogTicker( final JDialog dialog )
        {
            this.dialog = dialog;
        }

        public void tick()
        {
            dialog.dispose();
        }

        public boolean isDone()
        {
            return true;
        }
    }
}
