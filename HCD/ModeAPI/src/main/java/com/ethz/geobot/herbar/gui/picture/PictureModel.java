/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.collection.cursor.ArrayCursor;
import ch.jfactory.collection.cursor.Cursor;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.lang.reflect.Array;
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

    private Cursor<CommentedPicture>[] cursors;

    private int selected;

    public PictureModel( final PictureTheme[] themes )
    {
        this.themes = themes;
    }

    public void setPicture( final String name )
    {
        final Iterator it = getPictureCursor().getIterator();
        while ( it.hasNext() )
        {
            final CommentedPicture pic = (CommentedPicture) it.next();
            final Picture picture = pic.getPicture();
            if ( picture != null )
            {
                if ( picture.getName().equals( name ) )
                {
                    getPictureCursor().setCurrent( pic );
                }
            }
            else
            {
                LOG.error( "picture for " + pic + " is null" );
            }
        }
    }

    public CommentedPicture getPicture()
    {
        return (CommentedPicture) getPictureCursor().getCurrent();
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
            cursors[idx] = new ArrayCursor<CommentedPicture>( getTaxon().getCommentedPictures( themes[idx] ) );
        }
        return cursors[idx];
    }
}