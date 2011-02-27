/*
 * Herbar CD-ROM Version 2
 *
 * MutableTaxonImpl.java
 */

package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MorphologyAttribute;
import com.ethz.geobot.herbar.model.MorphologyText;
import com.ethz.geobot.herbar.model.MorphologyValue;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureText;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class TaxonImpl extends GraphNodeImpl implements Taxon
{
    private static final Logger LOG = LoggerFactory.getLogger( TaxonImpl.class );

    public Level getLevel()
    {
        final GraphNodeList list = getChildren( LevelImpl.class );
        return (LevelImpl) list.get( 0 );
    }

    public Taxon getParentTaxon()
    {
        return (Taxon) super.getParents( TaxonImpl.class ).get( 0 );
    }

    public Taxon[] getChildTaxa()
    {
        final GraphNodeList childTaxa = getChildren( TaxonImpl.class );
        return (Taxon[]) childTaxa.getAll( new TaxonImpl[0] );
    }

    public Taxon getChildTaxon( final int index ) throws IndexOutOfBoundsException
    {
        return getChildTaxa()[index];
    }

    public MorphologyValue[] getMorValues()
    {
        final GraphNodeList texts = getChildren( MorphologyText.class );
        final GraphNodeList values = new GraphNodeList();
        for ( int i = 0; i < texts.size(); i++ )
        {
            values.addAll( texts.get( i ).getParents( MorphologyValue.class ) );
        }
        return (MorphologyValue[]) values.getAll( new MorphologyValueImpl[0] );
    }

    public MorphologyValue getMorValue( final int index )
    {
        return getMorValues()[index];
    }

    public MorphologyAttribute[] getMorAttributes()
    {
        final MorphologyValueImpl[] values = (MorphologyValueImpl[]) getMorValues();
        final GraphNodeList attributes = new GraphNodeList();
        for ( final MorphologyValueImpl value : values )
        {
            if ( !attributes.contains( value ) )
            {
                attributes.add( (MorphologyAttributeImpl) value.getParentAttribute() );
            }
        }
        return (MorphologyAttribute[]) attributes.getAll( new MorphologyAttributeImpl[0] );
    }

    public int getChildTaxon( final Taxon child )
    {
        final Taxon[] taxa = getChildTaxa();
        for ( int i = 0; i < taxa.length; i++ )
        {
            if ( child == taxa[i] )
            {
                return i;
            }
        }
        return -1;
    }

    public Taxon[] getChildTaxa( final Level level )
    {
        final GraphNodeList all = getChildren( TaxonImpl.class );
        final GraphNodeList children = all.getChildren( LevelImpl.class );
        final List<TaxonImpl> result = new ArrayList<TaxonImpl>();
        for ( int i = 0; i < all.size(); i++ )
        {
            if ( children.get( i ) == level )
            {
                result.add( (TaxonImpl) all.get( i ) );
            }
        }
        return result.toArray( new TaxonImpl[result.size()] );
    }

    public Taxon[] getAllChildTaxa( final Level level )
    {
        final GraphNodeList children = getChildren( TaxonImpl.class );
        final List<Taxon> result = new ArrayList<Taxon>();
        result.addAll( Arrays.asList( getChildTaxa( level ) ) );
        for ( int i = 0; i < children.size(); i++ )
        {
            final TaxonImpl child = (TaxonImpl) children.get( i );
            final Taxon[] subChildren = child.getAllChildTaxa( level );
            result.addAll( Arrays.asList( subChildren ) );
        }
        return result.toArray( new Taxon[result.size()] );
    }

    public boolean isIn( final Taxon[] list )
    {
        return ArrayUtils.contains( list, this );
    }

    public Level[] getSubLevels()
    {
        final GraphNodeList children = getChildren( TaxonImpl.class );
        final Set<LevelImpl> result = new HashSet<LevelImpl>();

        // add this level
        final GraphNodeList levels = getChildren( LevelImpl.class );
        result.addAll( Arrays.asList( (LevelImpl[]) levels.getAll( new LevelImpl[levels.size()] ) ) );

        // add all direct children levels first
        final GraphNodeList subLevels = children.getChildren( LevelImpl.class );
        result.addAll( Arrays.asList( (LevelImpl[]) subLevels.getAll( new LevelImpl[subLevels.size()] ) ) );

        // add subsequently descendants
        for ( int i = 0; i < children.size(); i++ )
        {
            final TaxonImpl tax = (TaxonImpl) children.get( i );
            result.addAll( Arrays.asList( (LevelImpl[]) tax.getSubLevels() ) );
        }
        final List<LevelImpl> list = new ArrayList<LevelImpl>( result );
        Collections.sort( list, new Comparator<LevelImpl>()
        {
            public int compare( final LevelImpl o1, final LevelImpl o2 )
            {
                return new Integer( o1.getRank() ).compareTo( o2.getRank() );
            }
        } );
        return list.toArray( new LevelImpl[list.size()] );
    }

    public CommentedPicture[] getCommentedPictures( final PictureTheme theme )
    {
        final List<CommentedPictureImpl> result = new ArrayList<CommentedPictureImpl>();
        final GraphNodeList texts = getChildren( PictureText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            final GraphNode pic = text.getParents( Picture.class ).get( 0 );
            if ( text.getChildren( PictureTheme.class ).get( 0 ) == theme )
            {
                result.add( new CommentedPictureImpl( this, (Picture) pic, text.getName() ) );
            }
        }
        LOG.debug( "pictures for \"" + this + "\" and theme \"" + theme + "\" are " + result );
        return result.toArray( new CommentedPictureImpl[result.size()] );
    }

    public PictureTheme[] getPictureThemes()
    {
        final List<PictureTheme> result = new ArrayList<PictureTheme>();
        final GraphNodeList texts = getChildren( PictureText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            result.add( (PictureTheme) text.getChildren( PictureTheme.class ).get( 0 ) );
        }
        return result.toArray( new PictureTheme[result.size()] );
    }

    public Taxon[] getSiblings()
    {
        final GraphNode parent = getParents( TaxonImpl.class ).get( 0 );
        if ( parent == null )
        {
            return new Taxon[0];
        }
        else
        {
            final GraphNodeList children = parent.getChildren( TaxonImpl.class );
            return (TaxonImpl[]) children.getAll( new TaxonImpl[0] );
        }
    }

    public double getScore()
    {
        throw new NoSuchMethodError( "getScore not implemented yet" );
    }

    public void setScore( final boolean right )
    {
        throw new NoSuchMethodError( "setScore not implemented yet" );
    }
}
