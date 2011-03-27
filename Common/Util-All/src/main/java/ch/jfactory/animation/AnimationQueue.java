/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.JComponent;

/**
 * Component with queue of animations. Make sure to set the bounds of this component before displaying it.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class AnimationQueue extends JComponent implements StopListener
{
    /** The image to use for the background. */
    private Image backgroundImage;

    /** The border to set within the component. */
    private Insets insets = new Insets( 0, 0, 0, 0 );

    /** An array of paintables to subsequently paint. */
    private Paintable[] paintables = new Paintable[0];

    /** The actual painters index within {@link #paintables}. */
    private transient int currentIndex = 0;

    /** The current Paintable. */
    private Paintable currentPainter;

    /** Create an animation without background image. */
    public AnimationQueue()
    {
        this( null );
    }

    /**
     * Creates an animation.
     *
     * @param backgroundImage the image used for background or null if none
     */
    public AnimationQueue( final Image backgroundImage )
    {
        this.backgroundImage = backgroundImage;

        // This hack makes sure that the animation is started upon first visibility of the component.
        // Todo: Make reusable
        addHierarchyListener( new HierarchyListener()
        {
            public void hierarchyChanged( final HierarchyEvent e )
            {
                if ( getTopLevelAncestor().isVisible() )
                {
                    AnimationQueue.this.removeHierarchyListener( this );
                    setOpaque( false );
                    start();
                }
            }
        } );
    }

    /** Starts the next paintable. */
    public void start()
    {
        if ( currentIndex < paintables.length )
        {
            currentPainter = paintables[currentIndex++];
            currentPainter.start();
            System.out.println( "" );
        }
    }

    /**
     * Adds a pantable to the queue of paintables. Each paintable is painted until it destroyes itself.
     *
     * @param paintable the paintable to add to the queue
     */
    public void addPaintable( final Paintable paintable )
    {
        // Register this queue with the paintable for later invokation of repaint
        paintable.setAnimationQueue( this );

        // Add it to the queue
        final int length = paintables.length;
        final Paintable[] newPaintables = new Paintable[length + 1];
        System.arraycopy( paintables, 0, newPaintables, 0, length );
        newPaintables[length] = paintable;
        paintables = newPaintables;
        paintable.addStopListener( this );
    }

    /**
     * Draws the background image and the paintable.
     *
     * @param g the graphics to paint on
     */
    public void paint( final Graphics g )
    {
        // First background image
        if ( backgroundImage != null )
        {
            g.drawImage( backgroundImage, 0, 0, this );
        }

        // Then foreground paintable
        currentPainter.paint( (Graphics2D) g );
    }

    /** Receives notification of a finished paintable. Start the next one. */
    public void stopPerformed()
    {
        start();
    }

    /**
     * Sets the insets for this component.
     *
     * @param insets the insets to set
     */
    public void setInsets( final Insets insets )
    {
        this.insets = insets;
    }

    /**
     * Returns the insets stored.
     *
     * @return the insets
     */
    public Insets getInsets()
    {
        return insets;
    }
}
