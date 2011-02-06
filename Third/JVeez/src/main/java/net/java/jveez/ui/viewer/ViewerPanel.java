/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.java.jveez.cache.ImageStore;
import net.java.jveez.ui.thumbnails.ThumbnailList;
import net.java.jveez.ui.viewer.anim.AnimationState;
import net.java.jveez.utils.HidingMouseListener;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.SimplePicture;
import org.apache.log4j.Logger;

public class ViewerPanel<T extends SimplePicture> extends JPanel
{
    private static final long serialVersionUID = 3688782553385809462L;

    private static final Logger LOG = Logger.getLogger( ViewerPanel.class );

    private static final Dimension DEFAULT_DIMENSION = new Dimension( 640, 480 );

    private volatile int currentIndex;

    private final ViewerImagePanel viewerImagePanel = new ViewerImagePanel();

    private final ViewerInfoPanel viewerInfoPanel = new ViewerInfoPanel();

    private final List<LoadedListener> loadedListeners = new ArrayList<LoadedListener>();

    private final JPanel infoPanel;

    private BufferedImage loadingImage = new DefaultImage( new Dimension( 640, 480 ), "Loading..." );

    private BufferedImage noImage = new DefaultImage( new Dimension( 640, 480 ), "No Image" );

    private ThumbnailList<T> thumbnailList;

    private ViewerActions viewerActions;

    private ViewerToolbar viewerToolbar;

    private Future currentImageLoadingJob;

    private Future nextImageLoadingJob;

    private Future previousImageLoadingJob;

    private final ExecutorService executorService = Executors.newFixedThreadPool( 3, Utils.newPriorityThreadFactory( Thread.NORM_PRIORITY ) );

    private BufferedImage image;

    private T currentPicture;

    private boolean toolbarVisible;

    public ViewerPanel()
    {
        this( null );
    }

    public ViewerPanel( final ThumbnailList<T> thumbnailList )
    {
        setThumnailList( thumbnailList );

        // setup toolbar
        viewerActions = new ViewerActions( this, viewerImagePanel );
        viewerToolbar = new ViewerToolbar( viewerActions.getControlButtons() );

        infoPanel = new JPanel( new BorderLayout() );
        infoPanel.setOpaque( false );
        infoPanel.add( Box.createVerticalStrut( 50 ), BorderLayout.NORTH );
        infoPanel.add( Box.createVerticalStrut( 50 ), BorderLayout.SOUTH );
        infoPanel.add( viewerInfoPanel, BorderLayout.EAST );
        viewerInfoPanel.setBlurSource( viewerImagePanel );

        setToolbarVisible( true );

        setLayout( new BorderLayout() );
        add( viewerImagePanel, BorderLayout.CENTER );
        add( viewerToolbar, BorderLayout.SOUTH );
        setBackground( Color.WHITE );
        setSize( DEFAULT_DIMENSION );

        new HidingMouseListener( viewerImagePanel );

    }

    @Override
    public void setOpaque( final boolean isOpaque )
    {
        super.setOpaque( isOpaque );
        if ( viewerImagePanel != null )
        {
            viewerImagePanel.setOpaque( isOpaque );
        }
    }

    @Override
    public void setBackground( final Color bg )
    {
        super.setBackground( bg );
        if ( viewerImagePanel != null )
        {
            viewerImagePanel.setBackground( bg );
        }
    }

    public void setToolbarVisible( final boolean visible )
    {
        this.toolbarVisible = visible;
        if ( toolbarVisible )
        {
            add( viewerToolbar, BorderLayout.SOUTH );
            addKeyListener( viewerActions );
            viewerActions.addMouseWheelListener();
        }
        else
        {
            remove( viewerToolbar );
            removeKeyListener( viewerActions );
            viewerActions.removeMouseWheelListener();
        }
        viewerImagePanel.setAnimating( visible );
        revalidate();
    }

    public AnimationState getAnimationState()
    {
        return viewerImagePanel.getAnimationState();
    }

    public void setAnimationState( final AnimationState state )
    {
        viewerImagePanel.setAnimationState( state );
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public JComponent getImagePanel()
    {
        return viewerImagePanel;
    }

    public boolean isToolbarVisible()
    {
        return toolbarVisible;
    }

    public void setThumnailList( final ThumbnailList thumbnailList )
    {
        this.thumbnailList = thumbnailList;
    }

    public void toogleDisplayPictureInformation()
    {
        LOG.debug( "toogleDisplayPictureInformation()" );

        final JFrame frame = (JFrame) getTopLevelAncestor();
        frame.setGlassPane( infoPanel );
        frame.getGlassPane().setVisible( !frame.getGlassPane().isVisible() );
    }

    public void closeViewer()
    {
        setVisible( false );
    }

    public void gotoFirst()
    {
        LOG.debug( "gotoFirst()" );
        thumbnailClicked( 0 );
    }

    public void gotoPrevious()
    {
        LOG.debug( "gotoPrevious()" );
        final int nextIndex = thumbnailList.getIndexBefore( currentIndex );
        thumbnailClicked( nextIndex );
    }

    public void gotoNext()
    {
        LOG.debug( "gotoNext()" );
        final int nextIndex = thumbnailList.getIndexAfter( currentIndex );
        thumbnailClicked( nextIndex );
    }

    public void gotoLast()
    {
        LOG.debug( "gotoLast()" );
        final int nextIndex = thumbnailList.getNumberOfPictures() - 1;
        thumbnailClicked( nextIndex );
    }

    public T getCurrentPicture()
    {
        return currentPicture;
    }

    public void setCurrentPicture( final T picture )
    {
        setCurrentPicture( picture, null, null, null );
    }

    public void setLoadingText( final String text )
    {
        loadingImage = new DefaultImage( new Dimension( 640, 480 ), text );
    }

    public void setNoImageText( final String text )
    {
        noImage = new DefaultImage( new Dimension( 640, 480 ), text );
    }

    public void setCurrentPicture( final T picture, final T previousPicture, final T nextPicture,
                                   final AnimationState state )
    {
        // cancel any pending loading job
        if ( currentImageLoadingJob != null )
        {
            currentImageLoadingJob.cancel( true );
        }
        if ( nextImageLoadingJob != null )
        {
            nextImageLoadingJob.cancel( true );
        }
        if ( previousImageLoadingJob != null )
        {
            previousImageLoadingJob.cancel( true );
        }

        currentPicture = picture;

        if ( picture != null )
        {
            if ( !ImageStore.getInstance().isCached( picture ) )
            {
                viewerToolbar.update( picture.getName(), currentIndex, thumbnailList == null ? 1 : thumbnailList.getNumberOfPictures() );
                viewerImagePanel.setImage( loadingImage );
                viewerImagePanel.resetStateIn( 0 );
                repaint();
                viewerInfoPanel.updateContent( null, null );

                final Runnable future = new Runnable()
                {
                    public void run()
                    {
                        if ( !Thread.interrupted() )
                        {
                            // (eventually) load the image
                            image = ImageStore.getInstance().getImage( picture );
                            if ( !Thread.interrupted() && image != null )
                            {
                                viewerImagePanel.setImage( image );
                                if ( state != null )
                                {
                                    viewerImagePanel.setAnimationState( state );
                                }
                                else
                                {
                                    viewerImagePanel.resetStateIn( 0 );
                                }
                                SwingUtilities.invokeLater( new Runnable()
                                {
                                    public void run()
                                    {
                                        viewerInfoPanel.updateContent( picture, image );
                                    }
                                } );
                                LOG.info( "fireing image loaded event from asynchronous picture loader thread" );
                                firePictureLoaded();
                                prefetchNextPicture( nextPicture );
                                prefetchPreviousPicture( previousPicture );
                            }
                            else
                            {
                                if ( state != null )
                                {
                                    viewerImagePanel.setAnimationState( state );
                                }
                                else
                                {
                                    viewerImagePanel.resetStateIn( 0 );
                                }
                            }
                        }
                    }
                };
                currentImageLoadingJob = executorService.submit( future );
            }
            else
            {
                image = ImageStore.getInstance().getImage( picture );
                viewerImagePanel.setImage( image );
                if ( state != null )
                {
                    viewerImagePanel.setAnimationState( state );
                }
                else
                {
                    viewerImagePanel.resetStateIn( 0 );
                }
                viewerInfoPanel.updateContent( picture, image );
                viewerToolbar.update( picture.getName(), currentIndex, thumbnailList == null ? 1 : thumbnailList.getNumberOfPictures() );
                LOG.info( "fireing image loaded event from synchronous cache picture loading" );
                firePictureLoaded();
                prefetchNextPicture( nextPicture );
                prefetchPreviousPicture( previousPicture );
            }
        }
        else
        {
            viewerToolbar.clear();
            viewerImagePanel.setImage( noImage );
            viewerInfoPanel.updateContent( null, noImage );
        }
    }

    private void firePictureLoaded()
    {
        for ( final LoadedListener listener : loadedListeners )
        {
            listener.imageLoaded( this );
        }
    }

    public void thumbnailClicked( final int imageIndex )
    {
        JFrame f = (JFrame) getTopLevelAncestor();
        if ( f == null )
        {
            f = new JFrame();
            f.add( this );
            f.setSize( 500, 500 );
            f.setLocationRelativeTo( null );
        }
        else
        {
            f.setVisible( true );
        }
        currentIndex = imageIndex;

        if ( imageIndex == -1 )
        {
            setCurrentPicture( null );
            return;
        }

        final T picture = thumbnailList.getPictureAt( imageIndex );
        T nextPicture = null;
        T previousPicture = null;

        // put in cache the image before the selected one (if needed)
        final int previousIndex = thumbnailList.getIndexBefore( imageIndex );
        if ( previousIndex != -1 && previousIndex != imageIndex )
        {
            previousPicture = thumbnailList.getPictureAt( previousIndex );
        }

        // put in cache the image after the selected one (if needed)
        final int nextIndex = thumbnailList.getIndexAfter( imageIndex );
        if ( nextIndex != -1 && nextIndex != imageIndex )
        {
            nextPicture = thumbnailList.getPictureAt( nextIndex );
        }

        setCurrentPicture( picture, previousPicture, nextPicture, null );
    }

    public void addLoadedListener( final LoadedListener listener )
    {
        loadedListeners.add( listener );
    }

    public void removeLoadedListener( final LoadedListener listener )
    {
        loadedListeners.remove( listener );
    }

    private void prefetchNextPicture( final T nextPicture )
    {
        if ( nextPicture != null )
        {
            nextImageLoadingJob = executorService.submit( new Runnable()
            {
                public void run()
                {
                    if ( Thread.interrupted() )
                    {
                        return;
                    }

                    ImageStore.getInstance().getImage( nextPicture );
                }
            } );
        }
    }

    private void prefetchPreviousPicture( final T nextPicture )
    {
        if ( nextPicture != null )
        {
            previousImageLoadingJob = executorService.submit( new Runnable()
            {
                public void run()
                {
                    if ( Thread.interrupted() )
                    {
                        return;
                    }

                    ImageStore.getInstance().getImage( nextPicture );
                }
            } );
        }
    }

    static class DefaultImage extends BufferedImage
    {
        public DefaultImage( final Dimension dim, final String string )
        {
            super( dim.width, dim.height, TYPE_INT_ARGB_PRE );
            final Graphics2D g = this.createGraphics();
            g.setFont( new Font( "Arial", Font.BOLD, 20 ) );
            final FontMetrics metrics = g.getFontMetrics();
            final Rectangle2D bounds = metrics.getStringBounds( string, g );
            final double f = Math.min( getWidth() / bounds.getWidth() / 2, getHeight() / bounds.getHeight() / 2 );
            g.setFont( new Font( "Arial", Font.BOLD, (int) ( 12 * f ) ) );
            final FontMetrics metrics2 = g.getFontMetrics();
            final Rectangle2D bounds2 = metrics2.getStringBounds( string, g );
            final int x = (int) ( ( getWidth() - bounds2.getWidth() ) / 2 );
            final int y = (int) ( ( getHeight() - bounds2.getHeight() ) / 2 + bounds2.getHeight() );
            g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setColor( Color.lightGray );
            g.drawString( string, x, y );
            g.dispose();
        }
    }

    public interface LoadedListener<T extends SimplePicture>
    {
        void imageLoaded( ViewerPanel<T> panel );
    }
}
