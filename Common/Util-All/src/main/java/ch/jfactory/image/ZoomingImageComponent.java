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

import ch.jfactory.resource.CachedImageComponent;
import ch.jfactory.resource.PictureCache;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class ZoomingImageComponent extends JScrollPane
{
    private final CachedImageComponent ci;

    public ZoomingImageComponent( final PictureCache cache )
    {
        ci = new CachedImageComponent( cache );
        setViewportView( ci );
        setBorder( BorderFactory.createEmptyBorder() );

        final DragZoom dragZoom = new DragZoom();
        addMouseListener( dragZoom );
        addMouseMotionListener( dragZoom );
    }

    public void setImage( final String name )
    {
        ci.setImage( name );
    }

    public String getImage()
    {
        return ci == null || ci.getImage() == null ? null : ci.getImage().getName();
    }

    public boolean isZoomed()
    {
        return ci.getZoomFactor() != 1.0;
    }

    public void setZoom( final boolean b )
    {
        setZoom( b ? 2.0 : 1.0 );
    }

    public void setZoom( final double f )
    {
        ci.setZoomFactor( f );
        getViewport().setViewSize( ci.getPreferredSize() );
    }

    class DragZoom extends MouseAdapter implements MouseMotionListener
    {
        private Point lastPoint;

        public void mousePressed( final MouseEvent e )
        {
            lastPoint = e.getPoint();
        }

        /**
         * During the drag, offsets are updated and passed to the component
         *
         * @param e Description of the Parameter
         */
        public void mouseDragged( final MouseEvent e )
        {
            lastPoint.setLocation( lastPoint.getX() - e.getPoint().getX(), lastPoint.y - e.getPoint().getY() );
            final JViewport viewPort = getViewport();

            final Point oldPoint = viewPort.getViewPosition();
            double x = oldPoint.getX();
            double y = oldPoint.getY();
            if ( oldPoint.getX() + lastPoint.getX() >= 0 )
            {
                x = oldPoint.getX() + lastPoint.getX();
            }
            if ( oldPoint.getY() + lastPoint.getY() >= 0 )
            {
                y = oldPoint.getY() + lastPoint.getY();
            }
            final Point newPoint = new Point();
            newPoint.setLocation( x, y );

            lastPoint = e.getPoint();
            viewPort.setViewPosition( newPoint );
            viewPort.revalidate();
        }

        // When switching to JDK 1.6.0_23 this pseudo overwrite was needed to successfully compile the class.
        // - super.mouseMoved doesn't work as the super class seems not to have the method.
        // - without it a MouseMotionListener implementations are missed
        // - removing the MouseMotionListener as it is already implemented by the MouseAdapter fails as the interface
        //   is not recognized anymore
        public void mouseMoved( final MouseEvent e )
        {
        }
    }
}
