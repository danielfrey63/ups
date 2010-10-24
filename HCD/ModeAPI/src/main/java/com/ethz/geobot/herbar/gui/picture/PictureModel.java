package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.collection.cursor.DefaultCursor;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class PictureModel
{
    private static final Logger LOG = LoggerFactory.getLogger( PictureModel.class );

    private final PictureTheme[] themes;

    private Taxon taxon;

    private Cursor[] cursors;

    private int selected;

    private boolean zoomed = false;

    public PictureModel( final HerbarModel model )
    {
        this.themes = model.getPictureThemes();
    }

    public boolean isZoomed()
    {
        return zoomed;
    }

    public void setZoomed( final boolean b )
    {
        zoomed = b;
    }

    public void setPicture( final String name )
    {
        final Iterator it = getPictureCursor().getIterator();
        while ( it.hasNext() )
        {
            final CommentedPicture pic = (CommentedPicture) it.next();
            if ( pic.getPicture().getName().equals( name ) )
            {
                getPictureCursor().setCurrent( pic );
            }
        }
    }

    public CommentedPicture getPicture()
    {
        return (CommentedPicture) getPictureCursor().getCurrent();
    }

    public PictureTheme[] getPictureThemes()
    {
        return themes;
    }

    public PictureTheme getPictureTheme()
    {
        return themes[selected];
    }

    public void setSelectedIndex( final int idx )
    {
        selected = idx;
    }

    public int getIndex( final PictureTheme t )
    {
        for ( int i = 0; i < themes.length; i++ )
        {
            if ( themes[i] == t )
            {
                return i;
            }
        }
        return -1;
    }

    public Cursor getPictureCursor()
    {
        return getPictureCursor( getPictureTheme() );
    }

    public Cursor getPictureCursor( final PictureTheme t )
    {
        final int idx = getIndex( t );
        if ( idx >= 0 )
        {
            return ensureCursor( idx );
        }
        return null;
    }

    public Taxon getTaxon()
    {
        return taxon;
    }

    private Cursor ensureCursor( final int idx )
    {
        if ( cursors == null )
        {
            cursors = new Cursor[themes.length];
        }
        if ( cursors[idx] == null )
        {
            cursors[idx] = new DefaultCursor( getTaxon().getCommentedPictures( themes[idx] ) );
        }
        return cursors[idx];
    }

    public void setTaxon( final Taxon taxon )
    {
        LOG.debug( "setting taxon to \"" + taxon + "\"" );
        try
        {
            this.taxon = taxon;
            cursors = null;
        }
        catch ( Exception e )
        {
            LOG.error( "Error in setTaxon ", e );
        }
    }

    public void setPictureTheme( final PictureTheme shownTheme )
    {
        for ( int i = 0; i < themes.length; i++ )
        {
            if ( themes[i] == shownTheme )
            {
                setSelectedIndex( i );
                return;
            }
        }
    }
}