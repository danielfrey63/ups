/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.image;

import ch.jfactory.resource.CachedImageLocator;
import ch.jfactory.resource.PictureCache;
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

    public Object getSelectedObject()
    {
        return getObjectAt( getSelectedIndex() );
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
}
