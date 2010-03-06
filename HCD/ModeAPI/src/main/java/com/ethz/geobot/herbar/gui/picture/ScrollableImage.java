/*
 * Herbar CD-ROM version 2
 *
 * ScrollableImage.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.resource.CachedImageLocator;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Picture;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This control is used to display a scrollable Image
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class ScrollableImage extends JLabel implements Scrollable
{
    /**
     * category for logging
     */
    private final static Logger LOG = LoggerFactory.getLogger( ScrollableImage.class );

    /**
     * currently shown image
     */
    private ImageIcon image = null;

    /**
     * zoomed instance of currently shown image
     */
    private ImageIcon zoomedImage = null;

    /**
     * zoomed state information
     */
    private boolean isZoomed = false;

    /**
     * reference to currently shown picture
     */
    private Picture currentPicture;

    /**
     * Instance of image locator
     */
    private final CachedImageLocator locator = ImageLocator.pictLocator;

    /**
     * Listener to asynchron nofication of picture loading private AsynchronPictureLoaderListener finishVisitor = new
     * AsynchronPictureLoaderListener() { public void loadFinished( String name, Image img, boolean bool ) { setImage(
     * new ImageIcon( img ), "" ); } <p/> public void loadAborted( String name ) { setImage( null, "" ); } <p/> public
     * void loadStarted( String name ) { setImage( null, Strings.getString( "PICTURE.LOAD" ) ); } };
     */
    public ScrollableImage()
    {
        super();
        //locator.attach( finishVisitor );
    }

    /**
     * used by client to set current picture.
     *
     * @param p reference to picture instancewhich should be shown
     */
    public void setPicture( final Picture p )
    {
        LOG.debug( "setPicture called with picture: " + p );
        this.currentPicture = p;

        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                locator.abort();
                if ( currentPicture != null )
                {
                    locator.loadImage( currentPicture.getName() );
                }
                else
                {
                    setImage( null, "" );
                }
            }
        } );
    }

    /**
     * set zoom state
     *
     * @param zoom true zoom enabled; false zoom disabled
     */
    public void setZoom( final boolean zoom )
    {
        LOG.debug( " setting new Zoom " + zoom );

        isZoomed = zoom;
        showCurrentIcon();
    }

    private void setImage( final ImageIcon i, final String text )
    {
        LOG.debug( "set image (" + i + ") and statustext: " + text );
        if ( this.image != i )
        {
            if ( image != i )
            {
                this.image = i;
                this.zoomedImage = null;
            }

            showCurrentIcon();
            setText( text );
        }
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement( final Rectangle visibleRect,
                                           final int orientation,
                                           final int direction )
    {
        return 50;
    }

    public int getScrollableBlockIncrement( final Rectangle visibleRect,
                                            final int orientation,
                                            final int direction )
    {
        return 50;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    private void showCurrentIcon()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                ImageIcon showImage = image;
                if ( isZoomed )
                {
                    if ( zoomedImage == null && image != null )
                    {
                        LOG.debug( " scaling the image " );
                        final Image img = image.getImage();
                        final int width = img.getWidth( null ) * 2;
                        final int height = img.getHeight( null ) * 2;
                        final Image scaled = img.getScaledInstance( width, height, Image.SCALE_FAST );
                        showImage = zoomedImage = new ImageIcon( scaled );
                    }
                    showImage = zoomedImage;
                }
                setIcon( showImage );
            }
        } );
    }
}
