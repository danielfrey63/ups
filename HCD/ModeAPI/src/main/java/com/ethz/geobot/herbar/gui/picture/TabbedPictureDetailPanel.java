/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.cache.ImageCacheException;
import ch.jfactory.component.Dialogs;
import ch.jfactory.image.PictureDetailPanel;
import ch.jfactory.resource.PictureCache;
import com.ethz.geobot.herbar.model.PictureTheme;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

public class TabbedPictureDetailPanel extends JTabbedPane
{
    /** This class logger. */
    private static final Logger LOG = Logger.getLogger( TabbedPictureDetailPanel.class );

    private final PictureCache cache;

    private final ArrayList<PictureTheme> list = new ArrayList<PictureTheme>();

    public TabbedPictureDetailPanel()
    {
        this.cache = new PictureCache( new PictureCache.CachingExceptionHandler()
        {
            public void handleCachingException( final Throwable e )
            {
                if ( e instanceof ImageCacheException )
                {
                    final String message;
                    if ( e.getCause() instanceof IOException )
                    {
                        message = e.getCause().getMessage();
                    }
                    else
                    {
                        message = e.getMessage();
                    }
                    Dialogs.showErrorMessage( TabbedPictureDetailPanel.this, "Fehler beim Zwischenspeichern von Bildern", message );
                }
            }
        } );
    }

    public PictureDetailPanel getDetail( final int i )
    {
        return (PictureDetailPanel) getComponentAt( i );
    }

    public PictureDetailPanel addTab( final PictureTheme theme )
    {
        LOG.debug( "initializing picture details panel for " + theme + " with cache " + cache );
        final PictureDetailPanel panel = new PictureDetailPanel( cache );
        this.add( panel, theme.getName() );
        list.add( theme );
        return panel;
    }

    private PictureTheme getObjectAt( final int i )
    {
        return list.get( i );
    }

    private int getObjectIndex( final PictureTheme theme )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            if ( theme.equals( getObjectAt( i ) ) )
            {
                return i;
            }
        }
        return -1;
    }

    public void clearAll()
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            getDetail( i ).clear();
        }
    }

    public void setEnabled( final int t, final boolean b )
    {
        final PictureTheme theme = getObjectAt( t );
        final String color = b ? "000000" : "999999";
        this.setTitleAt( t, "<html><body><font color='#" + color + "'>" + theme.getName() + "</font></body></html>" );
    }

    public PictureDetailPanel getThemePanel( final PictureTheme t )
    {
        return getDetail( getObjectIndex( t ) );
    }
}
