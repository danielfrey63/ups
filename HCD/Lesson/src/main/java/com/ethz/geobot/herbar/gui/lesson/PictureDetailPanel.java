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

import static ch.jfactory.application.presentation.Constants.GAP_WITHIN_TOGGLES;
import ch.jfactory.image.CachedImagePictureListCellRenderer;
import ch.jfactory.resource.AsyncPictureLoaderListener;
import ch.jfactory.resource.CachedImage;
import ch.jfactory.resource.CachedImagePicture;
import static ch.jfactory.resource.CachedImagePictureSortingAlgorithm.ByName;
import com.ethz.geobot.herbar.gui.picture.PictureCache;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.jveez.ui.thumbnails.DefaultThumbnailListModel;
import net.java.jveez.ui.thumbnails.ThumbnailList;
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

    private static final Logger LOGGER = LoggerFactory.getLogger( PictureDetailPanel.class );

    private CachedImageComponent imagePanel;

    private final PictureCache cache;

    private ThumbnailList<CachedImagePicture> thumbList;

    private final PropertyChangeSupport propertySupport = new PropertyChangeSupport( this );

    private final Collection<CachedImagePicture> list = new ArrayList<CachedImagePicture>();

    public PictureDetailPanel( final PictureCache cache )
    {
        this.cache = cache;
        initGUI();
    }

    /**
     * sets the image to be displayed
     *
     * @param name name of the image (taken from <see>PictureCache</see>)
     * @see PictureCache
     */
    public void setImagePanel( final String name )
    {
        LOGGER.debug( "setting image to \"" + name + "\"" );
        imagePanel.setImage( name );
    }

    /**
     * is this Component visible to the user
     *
     * @return true if visible, false if not visible
     */
    public boolean isShowing()
    {
        final boolean b = super.isShowing();
        if ( b && (getParent() != null) )
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
        imagePanel.setImage( null );
        thumbList.getThumbnailListModel().clear();
        for ( final CachedImagePicture picture : list )
        {
            picture.detachAll();
        }
        list.clear();
        cache.clearCachingList();
    }

    public void addImage( final String name, final int counter )
    {
        LOGGER.debug( "adding image \"" + name + "\" to picture details panel" );
        final CachedImagePicture picture = new CachedImagePicture( cache.getCachedImage( name ), name );
        picture.attach( new AsyncPictureLoaderListener()
        {
            public void loadFinished( final String name, final Image img, final boolean thumb )
            {
                thumbList.getThumbnailListModel().setPictureAt( counter, picture );
                picture.detach( this );
            }

            public void loadAborted( final String name )
            {
            }

            public void loadStarted( final String name )
            {
            }
        } );
        list.add( picture );
        thumbList.getThumbnailListModel().setPictures( list );
        if ( thumbList.getSelectedIndex() == -1 )
        {
            thumbList.setSelectedIndex( 0 );
        }
    }

    private void initGUI()
    {
        imagePanel = createImageComponent();
        thumbList = createThumbPanel();

        setLayout( new BorderLayout() );
        add( new JScrollPane( thumbList ), BorderLayout.NORTH );
        add( imagePanel, BorderLayout.CENTER );
    }

    private CachedImageComponent createImageComponent()
    {
        final CachedImageComponent image;
        final int gap = GAP_WITHIN_TOGGLES;
        image = new CachedImageComponent( cache );
        image.setBorder( BorderFactory.createEmptyBorder( gap, gap, gap, gap ) );
        return image;
    }

    private ThumbnailList<CachedImagePicture> createThumbPanel()
    {
        final DefaultThumbnailListModel<CachedImagePicture> model = new DefaultThumbnailListModel<CachedImagePicture>();
        final ThumbnailList<CachedImagePicture> thumbPanel = new ThumbnailList<CachedImagePicture>( model, ByName );
        thumbPanel.setThumbnailSize( 96 );
        thumbPanel.setFixedCellHeight( 102 );
        thumbPanel.setFixedCellWidth( 102 );
        thumbPanel.setOpaque( false );
        thumbPanel.setLayoutOrientation( JList.HORIZONTAL_WRAP );
        thumbPanel.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting() )
                {
                    final CachedImage img = imagePanel.getImage();
                    final CachedImagePicture picture = (CachedImagePicture) thumbPanel.getSelectedValue();
                    final String name = picture == null ? null : picture.getName();
                    imagePanel.setImage( name );
                    propertySupport.firePropertyChange( IMAGE, img, name );
                }
            }
        } );
        thumbPanel.setCellRenderer( new CachedImagePictureListCellRenderer() );

        return thumbPanel;
    }

    public void addPropertyChangeListener( final PropertyChangeListener l )
    {
        propertySupport.addPropertyChangeListener( l );
    }
}
