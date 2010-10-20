/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
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

    public ZoomingImageComponent( final PictureCache c, final int size )
    {
        ci = new CachedImageComponent( c, size );
        setViewportView( ci );
        setBorder( BorderFactory.createEmptyBorder() );

        final DragZoom dragZoom = new DragZoom();
        addMouseListener( dragZoom );
        addMouseMotionListener( dragZoom );
    }

    public void setImage( final String name, final boolean isThumb )
    {
        ci.setImage( name, isThumb );
    }

    public String getImage()
    {
        return ci.getImage().getName();
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
    }
}
