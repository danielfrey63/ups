/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.image.PictureDetailPanel;
import ch.jfactory.resource.CachedImageLocator;
import ch.jfactory.resource.PictureCache;
import com.ethz.geobot.herbar.model.PictureTheme;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

public class TabbedPictureDetailPanel extends JTabbedPane
{
    private final PictureCache cache;

    private final ArrayList<Object> list = new ArrayList<Object>();

    public TabbedPictureDetailPanel( final CachedImageLocator locator )
    {
        this.cache = new PictureCache( locator );
    }

    public void clearCachingList()
    {
        cache.clearCachingList();
    }

    public PictureDetailPanel getDetail( final int i )
    {
        return (PictureDetailPanel) getComponentAt( i );
    }

    public PictureDetailPanel addTab( final Object o, final String s )
    {
        final PictureDetailPanel panel = new PictureDetailPanel( cache );
        this.add( panel, s );
        list.add( o );
        return panel;
    }

    public Object getObjectAt( final int i )
    {
        return list.get( i );
    }

    public int getObjectIndex( final Object o )
    {
        for ( int i = 0; i < list.size(); i++ )
        {
            if ( o.equals( getObjectAt( i ) ) )
            {
                return i;
            }
        }
        return -1;
    }

    public void clearAll()
    {
        cache.clearCachingList();
        for ( int i = 0; i < list.size(); i++ )
        {
            getDetail( i ).clear();
        }
    }

    public void setEnabled( final int t, final boolean b )
    {
        final PictureTheme theme = (PictureTheme) getObjectAt( t );
        final String color = b ? "000000" : "999999";
        this.setTitleAt( t, "<html><body><font color='#" + color + "'>" + theme.getName() + "</font></body></html>" );
    }

    public PictureDetailPanel getThemePanel( final PictureTheme t )
    {
        return getDetail( getObjectIndex( t ) );
    }
}
