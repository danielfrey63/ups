/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.image;

import com.jgoodies.binding.beans.Model;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel to display equally sized {@link SelectableImagePanel} instances.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class ImagesPanel extends JPanel
{
    public static final String PROPERTYNAME_SELECTED = "selected";

    private final SelectionModel model = new SelectionModel();

    private JScrollPane scroller;

    private JPanel panel = new JPanel();

    public ImagesPanel()
    {
        this( 5 );
    }

    public ImagesPanel( final int gap )
    {
        this( gap, 22, 22 );
    }

    private ImagesPanel( final int gap, final int width, final int height )
    {
        try
        {
            initLayout( gap, width, height );
            initListeners();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private void initLayout( final int gap, final int width, final int height ) throws IOException
    {
        panel = new InnerPanel( new FlowLayout( FlowLayout.LEFT, gap, gap ) );
        panel.setBackground( Color.WHITE );
        scroller = new JScrollPane( panel );
        scroller.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        setLayout( new BorderLayout() );
        add( scroller, BorderLayout.CENTER );
        model.setSelected( new SelectableImagePanel( null, width, height ) );
    }

    private void initListeners()
    {
        panel.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent e )
            {
                final Component c = panel.getComponentAt( e.getX(), e.getY() );
                if ( c instanceof SelectableImagePanel )
                {
                    model.setSelected( (SelectableImagePanel) c );
                }
            }
        } );
        model.addPropertyChangeListener( SelectionModel.PROPERTYNAME_SELECTED, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final SelectableImagePanel oldSelectablePanel = (SelectableImagePanel) evt.getOldValue();
                oldSelectablePanel.repaint();
                final SelectableImagePanel newSelectablePanel = (SelectableImagePanel) evt.getNewValue();
                newSelectablePanel.repaint();
            }
        } );
    }

    public Component addImagePanel( final ImagePanel comp )
    {
        return panel.add( comp );
    }

    public void removeAllImagePanels()
    {
        final Component[] components = panel.getComponents();
        for ( final Component component : components )
        {
            if ( component instanceof ImagePanel )
            {
                remove( component );
            }
        }
    }

    public void setScroller( final JScrollPane scroller )
    {
        this.scroller = scroller;
    }

    public SelectionModel getModel()
    {
        return model;
    }

    /**
     * Creates an ImagesPanel from the given icons.
     *
     * @param icons  a collection of icon strings (urls, filepaths)
     * @param width  the width of the icons to display
     * @param height the height of the icons to display
     * @return an instance of ImagesPanel
     */
    public static ImagesPanel createImagesPanel( final Collection<String> icons, final int width, final int height )
    {
        final ImagesPanel panel = new ImagesPanel( 5, width, height );
        for ( final String path : icons )
        {
            Image image;
            try
            {
                image = ImageUtils.createThumbnail( path, width, height );
            }
            catch ( IOException e )
            {
                image = new DefaultImage( new Dimension( width, height ), "Error" );
            }
            panel.addImagePanel( new SelectableImagePanel( image, width, height ) );
        }
        return panel;
    }

    /**
     * Creates an ImagesPanel from the given icons.
     *
     * @param icons  a collection of icon strings (urls, filepaths)
     * @param width  the width of the icons to display
     * @param height the height of the icons to display
     * @param fill   whether to fill the image panel with the image
     * @return an instance of ImagesPanel
     */
    public static ImagesPanel createImagesPanel( final Collection<String> icons, final int width, final int height, final boolean fill )
    {
        final ImagesPanel panel = new ImagesPanel( 5, width, height );
        for ( final String path : icons )
        {
            Image image;
            try
            {
                image = ImageUtils.createThumbnail( path, width, height );
            }
            catch ( IOException e )
            {
                image = new DefaultImage( new Dimension( width, height ), "Error" );
            }
            panel.addImagePanel( new SelectableImagePanel( image, fill ) );
        }
        return panel;
    }

    public static ImagesPanel createImagesPanel( final Image[] images, final int width, final int height, final boolean fill )
    {
        final ImagesPanel panel = new ImagesPanel( 5, width, height );
        for ( final Image image : images )
        {
            panel.addImagePanel( new ImagePanel( image, fill ) );
        }
        return panel;
    }

    public static class SelectionModel extends Model
    {
        public static final String PROPERTYNAME_SELECTED = "selected";

        private SelectableImagePanel selected;

        public SelectionModel()
        {
            selected = new SelectableImagePanel( null );
        }

        public void setSelected( final SelectableImagePanel panelSelectable )
        {
            final SelectableImagePanel old = getSelected();
            old.setSelected( false );
            selected = panelSelectable;
            panelSelectable.setSelected( true );
            firePropertyChange( PROPERTYNAME_SELECTED, old, panelSelectable );
        }

        public SelectableImagePanel getSelected()
        {
            return selected;
        }
    }

    private class InnerPanel extends JPanel
    {
        public InnerPanel( final LayoutManager layout )
        {
            super( layout );
        }

        public Dimension getPreferredSize()
        {
            final FlowLayout layout = (FlowLayout) getLayout();
            final int hGap = layout.getHgap();
            final int vGap = layout.getVgap();
            int w = scroller.getViewport().getWidth();
            final Dimension dim = model.getSelected().getPreferredSize();
            if ( w == 0 )
            {
                w = (int) ( dim.getWidth() * 5 );
            }
            final int horizontalCount = (int) ( (double) ( w ) / ( hGap + dim.getWidth() ) );
            final int verticalCount = (int) Math.ceil( ( (double) getComponentCount() / (double) horizontalCount ) );
            final Dimension dimension = new Dimension( w, (int) ( verticalCount * ( dim.getHeight() + vGap ) ) );
            panel.setPreferredSize( dimension );
            return dimension;
        }
    }
}
