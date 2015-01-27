/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.resource.AsyncPictureLoaderListener;
import ch.jfactory.resource.CachedImage;
import ch.jfactory.resource.PictureConverter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedImageComponent extends JComponent implements AsyncPictureLoaderListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CachedImageComponent.class );

    private static final Dimension minimumSize = new Dimension( 50, 50 );

    private final PictureCache cache;

    private CachedImage cachedImage;

    private double zoomFactor = 1.0;

    /** Size of the picture with a zooming factor of 1. */
    private int fixedSize;

    private Dimension size;

    public static Border BORDER = BorderFactory.createEmptyBorder( 1, 1, 1, 1 );

    public CachedImageComponent( final PictureCache cache )
    {
        this.cache = cache;
        this.setBorder( BORDER );
        this.fixedSize = -1;
        this.size = null;
    }

    public double getZoomFactor()
    {
        return zoomFactor;
    }

    public synchronized void setImage( final String name )
    {
        LOGGER.debug( "setting image" + " \"" + name + "\"" );
        boolean reValidate = false;
//        if ( cachedImage != null )
//        {
//            cachedImage.detachAll();
//            reValidate = true;
//        }
        cachedImage = name == null ? null : cache.getCachedImage( name );
        loadImage();
        size = null;
        if ( cachedImage != null )
        {
            cachedImage.attach( this );
            reValidate = true;
        }
        if ( reValidate )
        {
            revalidate();
        }
        repaint();
    }

    public synchronized void setZoomFactor( final double d )
    {
        size = null;
        zoomFactor = d;
        revalidate();
    }

    public CachedImage getImage()
    {
        return cachedImage;
    }

    private void loadImage()
    {
        if ( cachedImage != null )
        {
            LOGGER.debug( "loading image " + cachedImage.getName() );
            cache.cacheImage( cachedImage.getName(), false, true );
        }
    }

    public void paintComponent( final Graphics g )
    {
        super.paintComponent( g );
        final Insets i = getInsets();
        final int w = i.right + i.left;
        final int h = i.top + i.bottom;
        Image im = ( cachedImage == null ) ? null : cachedImage.getImage( false );
        if ( im != null )
        {
            final Dimension p = getSize();
            final double wi = p.width - w / 2;
            final double he = p.height - h / 2;
            final int iw = im.getWidth( null );
            final int ih = im.getHeight( null );
            final double f = (double) PictureConverter.getFittingFactor( (int) (wi), (int) (he), iw, ih );
            // Use time saving smooth scaling for smaller images
            if ( f < 0.2 )
            {
                im = im.getScaledInstance( (int) ( iw * f ), (int) ( ih * f ), Image.SCALE_SMOOTH );
                g.drawImage( im, 0, 0, null );
            }
            else if ( f != 1.0 )
            {
                ( (Graphics2D) g ).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
                final AffineTransform trans = new AffineTransform();
                trans.scale( f, f );
                trans.translate( w / 2, h / 2 );
                ( (Graphics2D) g ).drawImage( im, trans, null );
            }
        }
    }

    public synchronized void loadFinished( final String name, final Image img, final boolean thumb )
    {
        LOGGER.debug( "loading of image \"" + name + "\" finished" );
        cachedImage.detach( this );
        size = null;
        repaint();
    }

    public synchronized void loadAborted( final String name )
    {
        LOGGER.debug( "loading of image \"" + name + "\" aborted" );
        repaint();
    }

    public void loadStarted( final String name )
    {
        LOGGER.debug( "loading of image \"" + name + "\" started" );
    }
}
