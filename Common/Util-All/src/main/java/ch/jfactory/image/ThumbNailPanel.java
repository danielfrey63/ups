/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.image;

import ch.jfactory.component.ScrollerPanel;
import ch.jfactory.resource.CachedImage;
import ch.jfactory.resource.CachedImageComponent;
import ch.jfactory.resource.PictureCache;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbNailPanel extends ScrollerPanel
{
    private final static int THUMB_HEIGHT = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger( ThumbNailPanel.class );

    private final JPanel panel;

    private final PictureCache cache;

    private final ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

    public ThumbNailPanel( final PictureCache cache )
    {
        panel = this;
        this.cache = cache;
    }

    public Dimension getPreferredSize()
    {
        int w = panel.getComponentCount() * THUMB_HEIGHT;
        int h = THUMB_HEIGHT;
        Insets i = getInsets();
        w += i.right + i.left;
        h += i.top + i.top;
        i = CachedImageComponent.BORDER.getBorderInsets( null );
        w += i.right + i.left;
        h += i.top + i.bottom;
        return new Dimension( w, h );
    }

    public Dimension getMinimumSize()
    {
        int w = THUMB_HEIGHT + btnNext.getPreferredSize().width + btnPrev.getPreferredSize().width;
        int h = THUMB_HEIGHT;
        Insets i = getInsets();
        w += i.right + i.left;
        h += i.top + i.top;
        i = CachedImageComponent.BORDER.getBorderInsets( null );
        w += i.right + i.left;
        h += i.top + i.bottom;
        return new Dimension( w, h );
    }

    public void addImage( final String c, final String toolTip, final boolean reValidate )
    {
        for ( int i = 0; i < panel.getComponentCount(); i++ )
        {
            if ( !panel.getComponent( i ).isVisible() )
            {
                final CachedImageComponent ci = (CachedImageComponent) panel.getComponent( i );
                ci.setImage( c, true );
                ci.setVisible( true );
                ci.setToolTipText( toolTip );
                if ( reValidate )
                {
                    revalidate();
                }
                return;
            }
        }
        final CachedImageComponent ci = new CachedImageComponent( cache, THUMB_HEIGHT );
        ci.addMouseListener( new MouseAdapter()
        {
            public void mouseReleased( final MouseEvent e )
            {
                fireActionEvent( (CachedImageComponent) e.getSource() );
                e.consume();
            }
        } );
        panel.add( ci );
        ci.setImage( c, true );
        ci.setToolTipText( toolTip );
        if ( reValidate )
        {
            revalidate();
        }
    }

    public void addActionListener( final ActionListener l )
    {
        listeners.add( l );
    }

    public void fireActionEvent( final CachedImageComponent ci )
    {
        final ActionEvent e = new ActionEvent( this, 0, ci.getImage().getName() );
        if ( listeners != null && listeners.size() > 0 )
        {
            LOGGER.info( "fireActionEvent " + ci.getImage().getName() );
        }
        for ( final ActionListener listener : listeners )
        {
            listener.actionPerformed( e );
        }
    }

    public void select( final CachedImage ci )
    {
        for ( int i = 0; i < panel.getComponentCount(); i++ )
        {
            final CachedImageComponent cic = (CachedImageComponent) panel.getComponent( i );
            if ( cic.getImage() == ci )
            {
                cic.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );
            }
            else
            {
                cic.setBorder( BorderFactory.createEmptyBorder() );
            }
        }
        revalidate();
    }

    public void removeAll()
    {
        for ( int i = 0; i < panel.getComponentCount(); i++ )
        {
            final Component c = panel.getComponent( i );
            if ( c instanceof CachedImageComponent )
            {
                final CachedImageComponent ci = (CachedImageComponent) c;
                ci.setImage( null, true );
                ci.setVisible( false );
            }
        }
        revalidate();
    }
}
