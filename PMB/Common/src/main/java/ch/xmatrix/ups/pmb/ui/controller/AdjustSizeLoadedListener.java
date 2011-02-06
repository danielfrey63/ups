package ch.xmatrix.ups.pmb.ui.controller;

import ch.jfactory.application.view.status.Message;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.SimpleNote;
import ch.xmatrix.ups.pmb.ui.model.PictureStateModel;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import net.java.jveez.ui.viewer.ViewerPanel;
import net.java.jveez.ui.viewer.anim.AnimationState;
import net.java.jveez.vfs.Picture;

/**
 * Adjusts the picture size and position after it is loaded.
 *
 * @author Daniel Frey 27.06.2008 17:09:18
 */
public class AdjustSizeLoadedListener implements ViewerPanel.LoadedListener<Picture>
{
    private final InfoModel infoModel;

    public AdjustSizeLoadedListener( final InfoModel infoModel )
    {
        this.infoModel = infoModel;
    }

    /** {@inheritDoc} */
    public void imageLoaded( final ViewerPanel<Picture> panel )
    {
        final Picture picture = panel.getCurrentPicture();
        if ( picture != null )
        {
            final File file = picture.getFile();
            final PictureStateModel state = parseIntoPictureStateModel( file );
            if ( state != null )
            {
                final BufferedImage image = panel.getImage();
                if ( image != null )
                {
                    final int iw = image.getWidth(); // unzoomed/original/saved  image width
                    final int ih = image.getHeight(); // unzoomed/original/saved image height
                    final Rectangle component = panel.getImagePanel().getBounds();
                    final double cw = component.getWidth(); // component width
                    final double ch = component.getHeight(); // component height
                    final double x1 = state.getX1();
                    final double y1 = state.getY1();
                    final double vw = iw * ( state.getX2() - x1 ); // viewable width
                    final double vh = ih * ( state.getY2() - y1 ); // viewable height
                    final double zoom;
                    final double x, y;
                    if ( vw / vh > cw / ch )
                    {
                        // width is limiting
                        zoom = cw / vw;
                        final double diff = ch - cw * vh / vw;
                        final double rih = ih * zoom; // real image height
                        final double oy1 = y1 * rih; // original absolute y1
                        x = -x1 * iw * zoom;
                        y = adjustFreeSpace( diff / 2 - oy1, rih, ch, oy1 );
                    }
                    else
                    {
                        zoom = ch / vh;
                        final double diff = cw - ch * vw / vh;
                        final double riw = iw * zoom;
                        final double ox1 = x1 * riw;
                        x = adjustFreeSpace( diff / 2 - ox1, riw, cw, ox1 );
                        y = -y1 * ih * zoom;
                    }
                    final AnimationState animationState = new AnimationState();
                    animationState.sx = zoom;
                    animationState.sy = zoom;
                    animationState.x = x;
                    animationState.y = y;
                    infoModel.setNote( new SimpleNote( "Zoom: " + zoom, "PMBController", Message.Type.INFO ) );
                    panel.setAnimationState( animationState );
                }
            }
        }
    }

    /**
     * Parses the string into a PictureStateModel. If parsing ist not successful, returns null.
     *
     * @param file Full file path. Position information is in paranthesis as space separated x1, x2, y1, y2
     * @return PictureStateModel
     */
    public static PictureStateModel parseIntoPictureStateModel( final File file )
    {
        final PictureStateModel model;
        final String name = file.getName();
        final String position = getPositionAndZoom( name );
        final String[] parts = position.split( " " );
        if ( parts.length == 4 )
        {
            model = new PictureStateModel( file.getAbsolutePath() );
            model.setX1( Double.parseDouble( parts[0] ) / 100 );
            model.setX2( Double.parseDouble( parts[1] ) / 100 );
            model.setY1( Double.parseDouble( parts[2] ) / 100 );
            model.setY2( Double.parseDouble( parts[3] ) / 100 );
        }
        else
        {
            model = null;
        }
        return model;
    }

    public static String getPositionAndZoom( final String string )
    {
        final int i1 = string.indexOf( " (" );
        final int i2 = string.indexOf( ")" );
        final String result;
        if ( i1 < i2 )
        {
            result = string.substring( i1 + 2, i2 );
        }
        else
        {
            result = "";
        }
        return result;
    }

    public static double adjustFreeSpace( double position, final double realImageDimension, final double componentDimension,
                                          final double originalPosition )
    {
        if ( realImageDimension < componentDimension )
        {
            position = ( componentDimension - realImageDimension ) / 2;
        }
        else if ( position > 0 && position + realImageDimension > componentDimension )
        {
            if ( realImageDimension < componentDimension )
            {
                position = ( componentDimension - realImageDimension ) / 2 - originalPosition;
            }
            else
            {
                position = 0;
            }
        }
        else if ( position + realImageDimension < componentDimension )
        {
            if ( realImageDimension < componentDimension )
            {
                position = ( componentDimension - realImageDimension ) / 2;
            }
            else
            {
                position = componentDimension - realImageDimension;
            }
        }
        return position;
    }
}
