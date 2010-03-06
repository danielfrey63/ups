/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.awt.Dimension;
import java.awt.Image;
import java.lang.ref.SoftReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageReference
{
    private final static Logger LOGGER = LoggerFactory.getLogger( ImageReference.class );

    private Dimension size;

    private boolean imageWasLoadedOnce = false;

    private boolean thumbNailWasLoadedOnce = false;

    private SoftReference theImageItself;

    private SoftReference theThumbNail;

    public ImageReference()
    {
    }

    public synchronized void setImage( final Image i, final boolean thumb )
    {
        if ( thumb )
        {
            final Image img = ( theImageItself == null ) ? null : (Image) theImageItself.get();
            if ( img == null )
            {
                thumbNailWasLoadedOnce = true;
                theThumbNail = new SoftReference( i );
            }
        }
        else
        {
            imageWasLoadedOnce = true;
            thumbNailWasLoadedOnce = false;
            theThumbNail = null;
            theImageItself = new SoftReference( i );
        }
    }

    protected void setSize( final Dimension d )
    {
        size = d;
    }

    public Dimension getSize()
    {
        return size;
    }

    public Image getImage( final boolean thumb )
    {
        Image img = null;
        if ( thumb )
        {
            img = ( theThumbNail == null ) ? null : (Image) theThumbNail.get();
            if ( thumbNailWasLoadedOnce && ( img == null ) )
            {
                LOGGER.debug( "*** Reference was lost " );
                thumbNailWasLoadedOnce = false;
            }
        }
        else
        {
            img = ( theImageItself == null ) ? null : (Image) theImageItself.get();
            if ( imageWasLoadedOnce && ( img == null ) )
            {
                LOGGER.debug( "*** Reference was lost " );
                imageWasLoadedOnce = false;
            }
        }
        return img;
    }

}
