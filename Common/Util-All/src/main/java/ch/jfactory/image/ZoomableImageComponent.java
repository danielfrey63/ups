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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class ZoomableImageComponent extends JScrollPane
{
    private final CachedImageComponent ci;

    private Rectangle zoomRect;

    private boolean zoomStarted = false;

    private boolean isZooming = false;

    public static final boolean DYNAMICZOOM = false;

    public ZoomableImageComponent( final PictureCache c, final int size )
    {
        setViewportView( ci = new LocalCachedImageComponent( c, size ) );
        setBorder( BorderFactory.createEmptyBorder() );

        final DragZoom dragZoom = new DragZoom();
        addMouseListener( dragZoom );
        addMouseMotionListener( dragZoom );
    }

    public void setImage( final String s, final boolean b )
    {
        ci.setImage( s, false );
    }

    public String getImage()
    {
        return ci.getImage().getName();
    }

    public void resetZoom()
    {
        ci.setZoomFaktor( 1.0 );
        getViewport().setViewSize( ci.getPreferredSize() );
    }

    public boolean isZoomed()
    {
        return ci.getZoomFaktor() != 1.0;
    }

    public void setZoom( final boolean b )
    {
        setZoom( b ? 2.0 : 1.0 );
    }

    public void setZoom( final double f )
    {
        ci.setZoomFaktor( f );
        getViewport().setViewSize( ci.getPreferredSize() );
    }

    private void setZoom( Rectangle r )
    {
        if ( r != null )
        {
            r = adjustRect( r );
            final Rectangle visi = getVisibleRect();
            visi.width *= ci.getZoomFaktor();
            visi.height *= ci.getZoomFaktor();
            final double oldzoomfaktor = ci.getZoomFaktor();
            double newzoomfaktor = ( (double) visi.width / (double) r.width );
            if ( newzoomfaktor > ( (double) visi.height / (double) r.height ) )
            {
                newzoomfaktor = ( (double) visi.height / (double) r.height );
            }
            ci.setZoomFaktor( newzoomfaktor );
            getViewport().setViewSize( ci.getPreferredSize() );
            final Point p = r.getLocation();
            double x = (double) p.x / oldzoomfaktor;
            double y = (double) p.y / oldzoomfaktor;
            x *= newzoomfaktor;
            y *= newzoomfaktor;
            getViewport().setViewPosition( new Point( (int) x, (int) y ) );
        }
        repaint();
    }

    public Rectangle adjustRect( final Rectangle r )
    {
        final Rectangle r1 = new Rectangle( r );
        if ( r.width < 0 )
        {
            r1.x = r.x + r.width;
            r1.width = -r.width;
        }
        if ( r.height < 0 )
        {
            r1.y = r.y + r.height;
            r1.height = -r.height;
        }
        return r1;
    }

    public void setZoomStarted( final boolean b )
    {
        zoomStarted = b;
        if ( !zoomStarted )
        {
            zoomRect = null;
            zoomStarted = false;
            isZooming = false;

        }
    }

    public boolean isZoomStarted()
    {
        return zoomStarted;
    }

    public void stopZooming()
    {
        if ( isZooming )
        {
            synchronized ( zoomRect )
            {
                isZooming = false;
                zoomRect = null;
            }
            repaint();
        }
    }

    public boolean isZooming()
    {
        return isZooming;
    }

    class DragZoom extends MouseAdapter implements MouseMotionListener
    {
        private Point m_pntLast;

        private Point start;

        /**
         * On a mousePressed the point is stored
         *
         * @param e Description of the Parameter
         */
        public void mousePressed( final MouseEvent e )
        {
            m_pntLast = e.getPoint();
            if ( true )
            {
                return;
            }
            if ( zoomStarted )
            {
                if ( isZooming )
                {
                    setZoom( zoomRect );
                    zoomRect = null;
                }
                isZooming = !isZooming;
                start = e.getPoint();
            }
        }

        /**
         * During the drag, offsets are updated and passed to the component
         *
         * @param e Description of the Parameter
         */
        public void mouseDragged( final MouseEvent e )
        {
            if ( isZooming )
            {
                return;
            }

            m_pntLast.setLocation( m_pntLast.getX() - e.getPoint().getX(),
                    m_pntLast.y - e.getPoint().getY() );
            final JViewport m_jvp = getViewport();

            final Point m_pntOld = m_jvp.getViewPosition();
            double x = m_pntOld.getX();
            double y = m_pntOld.getY();
            if ( m_pntOld.getX() + m_pntLast.getX() >= 0 )
            {
                x = m_pntOld.getX() + m_pntLast.getX();
            }
            if ( m_pntOld.getY() + m_pntLast.getY() >= 0 )
            {
                y = m_pntOld.getY() + m_pntLast.getY();
            }
            Point m_pntNew = new Point();
            m_pntNew.setLocation( x, y );

            m_pntLast = e.getPoint();
            final Point m_pt = m_pntNew;
            m_pntNew = m_pntOld;
            m_jvp.setViewPosition( m_pt );
            m_jvp.revalidate();
        }

        public void mouseMoved( final MouseEvent e )
        {
            if ( true )
            {
                return;
            }
            if ( isZooming )
            {
                zoomRect = new Rectangle( start.x, start.y, e.getPoint().x - start.x, e.getPoint().y - start.y );
                repaint();
            }
        }
    }

    class LocalCachedImageComponent extends CachedImageComponent
    {
        public LocalCachedImageComponent( final PictureCache c, final int size )
        {
            super( c, size );
        }

        public void paintComponent( final Graphics g )
        {
            super.paintComponent( g );
            if ( true )
            {
                return;
            }
            if ( zoomRect != null )
            {
                g.setColor( Color.BLUE );
                final Rectangle r = adjustRect( zoomRect );
                g.drawRect( r.x, r.y, r.width, r.height );
            }
        }
    }
}
