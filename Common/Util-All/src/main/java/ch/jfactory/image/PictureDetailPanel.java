/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.image;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.resource.CachedImageLocator;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.PictureCache;
import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This control is used to display the Pictures with zooming support
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class PictureDetailPanel extends JPanel
{
    public static final String IMAGE = "IMAGE";

    public static final String ZOOM = "ZOOM";

    private static final int START_ZOOM_KEY = KeyEvent.VK_F1;

    private static final int END_ZOOM_KEY = KeyEvent.VK_ESCAPE;

    private static final Logger LOGGER = LoggerFactory.getLogger( PictureDetailPanel.class );

    private static final int MAXSIZE = 370;

    private ZoomableImageComponent image;

    private final PictureCache cache;

    private ThumbNailPanel thumbPanel;

    private final PropertyChangeSupport propertySupport = new PropertyChangeSupport( this );

    /**
     * Constructs a Panel
     *
     * @param locator the <code>PictureCache</code> Object from which the pictures are taken
     * @see PictureCache
     */
    public PictureDetailPanel( final CachedImageLocator locator )
    {
        this.cache = new PictureCache( locator );
        initGUI();
    }

    public PictureDetailPanel( final PictureCache cache )
    {
        LOGGER.debug( "PictureDetailPanel(" + cache + ")" );
        this.cache = cache;
        initGUI();
    }

    /**
     * sets the image to be displayed
     *
     * @param name name of the image (taken from <see>PictureCache</see>)
     * @see PictureCache
     */
    public void setImage( final String name )
    {
        LOGGER.debug( "setImage(" + name + ")" );
        image.setImage( name, false );
    }

    /**
     * is this Component visible to the user
     *
     * @return true if visible, false if not visible
     */
    public boolean isShowing()
    {
        final boolean b = super.isShowing();
        if ( b && ( getParent() != null ) )
        {
            final Container parent = this.getParent();
            if ( parent instanceof JTabbedPane )
            {
                final JTabbedPane tab = (JTabbedPane) parent;
                return tab.getSelectedComponent() == this;
            }
        }
        return b;
    }

    public void clear()
    {
        image.setImage( null, false );
        thumbPanel.removeAll();
    }

    public void addImage( final String s, final String tooltip )
    {
        LOGGER.debug( "addImage(" + s + ")" );
        thumbPanel.addImage( s, tooltip, false );
    }

    private void initGUI()
    {
        final int gap = Constants.GAP_WITHIN_TOGGLES;

        image = createImageComponent();
        final JButton zoomin = createZoomButton( image );
        thumbPanel = createThumbPanel();

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable( false );
        toolBar.setRollover( true );
        toolBar.setFocusable( false );
        toolBar.setBorder( new EmptyBorder( 0, Constants.GAP_WITHIN_GROUP, 0, Constants.GAP_WITHIN_GROUP ) );
        toolBar.add( zoomin );

        final JPanel controlPanel = new JPanel();
        controlPanel.setLayout( new BorderLayout() );
        controlPanel.add( thumbPanel, BorderLayout.CENTER );
        controlPanel.add( toolBar, BorderLayout.WEST );
        controlPanel.setBorder( BorderFactory.createEmptyBorder( gap, 0, gap, 0 ) );

        //new Drager( sp );

        setLayout( new BorderLayout() );
        add( controlPanel, BorderLayout.NORTH );
        add( image, BorderLayout.CENTER );
    }

    private ZoomableImageComponent createImageComponent()
    {
        final ZoomableImageComponent image;
        final int gap = Constants.GAP_WITHIN_TOGGLES;
        image = new ZoomableImageComponent( cache, MAXSIZE );
        image.setBorder( BorderFactory.createEmptyBorder( gap, gap, gap, gap ) );
        return image;
    }

    private ThumbNailPanel createThumbPanel()
    {
        final ThumbNailPanel thumbPanel;
        thumbPanel = new ThumbNailPanel( cache );
        thumbPanel.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final String img = image.getImage();
                image.setImage( e.getActionCommand(), false );
                propertySupport.firePropertyChange( IMAGE, img, e.getActionCommand() );
            }
        } );
        return thumbPanel;
    }

    private JButton createZoomButton( final ZoomableImageComponent imageComponent )
    {
        final JButton button = ComponentFactory.createButton( "BUTTON.ZOOM.PLUS", null );
        button.setFocusable( false );
        button.setOpaque( false );
        button.setSelectedIcon( ImageLocator.getIcon( Strings.getString( "BUTTON.ZOOM.MINUS.ICON" ) ) );
        button.setPressedIcon( ImageLocator.getIcon( Strings.getString( "BUTTON.ZOOM.MINUS.ICON" ) ) );
        button.setModel( new ToggleButtonModel() );
        if ( ZoomableImageComponent.DYNAMICZOOM )
        {
            KeyStroke stroke = KeyStroke.getKeyStroke( END_ZOOM_KEY, 0 );
            button.registerKeyboardAction( new ActionListener()
            {
                public void actionPerformed( final ActionEvent e )
                {
                    if ( imageComponent.isZooming() )
                    {
                        imageComponent.stopZooming();
                    }
                    else if ( imageComponent.isZoomStarted() )
                    {
                        imageComponent.setZoomStarted( false );
                        button.repaint();
                    }
                    else
                    {
                        imageComponent.resetZoom();
                    }
                }
            }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );
            stroke = KeyStroke.getKeyStroke( START_ZOOM_KEY, 0 );
            button.registerKeyboardAction( new ActionListener()
            {
                public void actionPerformed( final ActionEvent e )
                {
                    if ( !imageComponent.isZooming() )
                    {
                        if ( !imageComponent.isZoomStarted() )
                        {
                            imageComponent.setZoomStarted( true );
                            button.repaint();
                        }
                    }
                }
            }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );
        }
        return button;
    }

    public void addPropertyChangeListener( final String s, final PropertyChangeListener l )
    {
        propertySupport.addPropertyChangeListener( s, l );
    }

    public void addPropertyChangeListener( final PropertyChangeListener l )
    {
        propertySupport.addPropertyChangeListener( l );
    }

    public void setZoomed( final boolean b )
    {
        final boolean old = image.isZoomed();
        if ( b == old )
        {
            return;
        }
        if ( ZoomableImageComponent.DYNAMICZOOM )
        {
            image.setZoomStarted( b );
        }
        else
        {
            image.setZoom( b );
        }
        propertySupport.firePropertyChange( ZOOM, Boolean.valueOf( old ), Boolean.valueOf( b ) );
    }

    private class ToggleButtonModel extends DefaultButtonModel
    {
        public void setSelected( final boolean b )
        {
            if ( isSelected() == b )
            {
                return;
            }
            if ( b )
            {
                stateMask |= SELECTED;
            }
            else
            {
                stateMask &= ~SELECTED;
            }
            setZoomed( b );
        }

        public void setPressed( final boolean b )
        {
            if ( ( isPressed() == b ) || !isEnabled() )
            {
                return;
            }
            if ( !b && isArmed() )
            {
                setSelected( !isSelected() );
            }

            if ( b )
            {
                stateMask |= PRESSED;
            }
            else
            {
                stateMask &= ~PRESSED;
            }

        }

        public boolean isSelected()
        {
            if ( ZoomableImageComponent.DYNAMICZOOM )
            {
                return image.isZoomStarted();
            }
            else
            {
                return image.isZoomed();
            }
        }
    }

    public void clearCachingList()
    {
        cache.clearCachingList();
    }

    public void cacheImage( final String name, final boolean thumb )
    {
        cache.cacheImage( name, thumb, false );
    }
}
