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
import org.apache.log4j.Logger;

/**
 * This is a SoftReference to an Image loaded from
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class CachedImage extends ImageReference
{
    private static final Logger LOGGER = Logger.getLogger(CachedImage.class);

    private String pictureURL;

    private AbstractAsynchronPictureLoaderSupport listeners;

    private CachedImageLocator locator;

    public CachedImage(final CachedImageLocator locator, final String pictureURL)
    {
        this.pictureURL = pictureURL;
        this.locator = locator;
    }

    public void setImage(final Image img, final boolean thumb)
    {
        LOGGER.debug("Setting image for " + pictureURL + ", " + thumb);
        if (img == super.getImage(thumb))
        {
            return;
        }
        super.setImage(img, thumb);
        if (listeners == null)
        {
            return;
        }
        if (img == null)
        {
            listeners.informAborted(pictureURL);
        }
        else
        {
            listeners.informFinished(pictureURL, img, thumb);
        }
    }

    public String getName()
    {
        return pictureURL;
    }

    public Dimension getSize()
    {
        Dimension d = super.getSize();
        if (d == null)
        {
            d = PictureLoader.getSize(getPath() + pictureURL);
            setSize(d);
        }
        return d;
    }

    public Image getImage(final boolean thumb)
    {
        Image i = super.getImage(thumb);
        if (i == null)
        {
            if (thumb)
            {
                i = super.getImage(!thumb);
            }
        }
        return i;
    }

    public void attach(final AsynchronPictureLoaderListener list)
    {
        if (listeners == null)
        {
            listeners = new AbstractAsynchronPictureLoaderSupport();
        }
        listeners.attach(list);
    }

    public void detach(final AsynchronPictureLoaderListener list)
    {
        if (listeners == null)
        {
            return;
        }
        listeners.detach(list);
        if (listeners.size() < 0)
        {
            listeners = null;
        }
    }

    protected boolean loaded(final boolean thumb)
    {
        if (super.getImage(thumb) != null)
        {
            return true;
        }
        if (thumb && (super.getImage(false) != null))
        {
            return true;
        }
        return false;
    }

    protected synchronized Image loadImage(final boolean thumb)
    {
        final Image i = PictureLoader.load(getPath() + pictureURL, thumb);
        this.setImage(i, thumb);
        return i;
    }

    private String getPath()
    {
        return locator.getPath();
    }
}
