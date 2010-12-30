/*
 * Herbar CD-ROM Version 2
 *
 * MutableTaxonImpl.java
 */

package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.GraphNodeRankComparator;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.MutableTaxon;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureText;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.relevance.AbsRelevance;
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
public class MutableTaxonImpl extends MutableGraphNodeImpl implements MutableTaxon
{
    private static final Logger LOG = LoggerFactory.getLogger( MutableTaxonImpl.class );

    private static final Comparator<GraphNode> RANK_COMPARATOR = new GraphNodeRankComparator();

    public static GraphNodeList getNodes( final Taxon[] list )
    {
        final GraphNodeList result = new GraphNodeList();
        for ( final Taxon aList : list )
        {
            result.add( (MutableTaxonImpl) aList );
        }
        return result;
    }

    public AbsRelevance getRelevance( final MorValue value )
    {
        return AbsRelevance.getRelevance( getSiblings(), value );
    }

    public void setChildTaxa( final Taxon[] children )
    {
        final GraphNodeList newChildren = new GraphNodeList();
        for ( final Taxon aChildren : children )
        {
            newChildren.add( (MutableTaxonImpl) aChildren );
        }
        setChildren( newChildren, MutableTaxonImpl.class );
    }

    public void setLevel( final Level newLevel )
    {
        // Replace all level type children by the new one
        setChildren( new GraphNodeList( (MutableTaxonLevelImpl) newLevel ), MutableTaxonLevelImpl.class );
    }

    public void setParentTaxon( final Taxon parent )
    {
        setParents( new GraphNodeList( (MutableTaxonImpl) parent ), MutableTaxonImpl.class );
    }

    public Level getLevel()
    {
        final GraphNodeList list = getChildren( MutableTaxonLevelImpl.class );
        return (MutableTaxonLevelImpl) list.get( 0 );
    }

    public Taxon getParentTaxon()
    {
        return (Taxon) super.getParents( MutableTaxonImpl.class ).get( 0 );
    }

    public Taxon[] getChildTaxa()
    {
        final GraphNodeList childTaxa = getChildren( MutableTaxonImpl.class );
        return (Taxon[]) childTaxa.getAll( new MutableTaxonImpl[0] );
    }

    public Taxon getChildTaxon( final int index ) throws IndexOutOfBoundsException
    {
        return getChildTaxa()[index];
    }

    public MorValue[] getMorValues()
    {
        final GraphNodeList texts = getChildren( MorText.class );
        final GraphNodeList values = new GraphNodeList();
        for ( int i = 0; i < texts.size(); i++ )
        {
            values.addAll( texts.get( i ).getParents( MorValue.class ) );
        }
        return (MorValue[]) values.getAll( new MutableMorphologyValueImpl[0] );
    }

    public MorValue getMorValue( final int index )
    {
        return getMorValues()[index];
    }

    public MorAttribute[] getMorAttributes()
    {
        final MutableMorphologyValueImpl[] values = (MutableMorphologyValueImpl[]) getMorValues();
        final GraphNodeList attributes = new GraphNodeList();
        for ( final MutableMorphologyValueImpl value : values )
        {
            if ( !attributes.contains( value ) )
            {
                attributes.add( (MutableMorphologyAttributeImpl) value.getParentAttribute() );
            }
        }
        return (MorAttribute[]) attributes.getAll( new MutableMorphologyAttributeImpl[0] );
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
        final GraphNodeList all = getChildren( MutableTaxonImpl.class );
        final GraphNodeList children = all.getChildren( MutableTaxonLevelImpl.class );
        final GraphNodeList result = new GraphNodeList();
        for ( int i = 0; i < all.size(); i++ )
        {
            if ( children.get( i ) == level )
            {
                result.add( all.get( i ) );
            }
        }
        final List<GraphNode> list = Arrays.asList( result.getAll() );
        return list.toArray( new MutableTaxonImpl[list.size()] );
    }

    public Taxon[] getAllChildTaxa( final Level level )
    {
        final GraphNodeList result = new GraphNodeList();
        final GraphNodeList children = getChildren( MutableTaxonImpl.class );
        result.addAll( getNodes( getChildTaxa( level ) ) );
        for ( int i = 0; i < children.size(); i++ )
        {
            final MutableTaxonImpl child = (MutableTaxonImpl) children.get( i );
            final Taxon[] subChildren = child.getAllChildTaxa( level );
            result.addAll( getNodes( subChildren ) );
        }
        return (Taxon[]) result.getAll( new MutableTaxonImpl[0] );
    }

    public boolean isIn( final Taxon[] list )
    {
        return ArrayUtils.contains( list, this );
    }

    public Level[] getSubLevels()
    {
        final GraphNodeList children = getChildren( MutableTaxonImpl.class );
        final Set<GraphNode> result = new HashSet<GraphNode>();

        // add this level
        final GraphNodeList levels = getChildren( MutableTaxonLevelImpl.class );
        result.addAll( Arrays.asList( levels.getAll() ) );

        // add all direct children levels first
        {
            final GraphNodeList subLevels = children.getChildren( MutableTaxonLevelImpl.class );
            result.addAll( Arrays.asList( subLevels.getAll() ) );
        }

        // add subsequently descendants
        for ( int i = 0; i < children.size(); i++ )
        {
            final MutableTaxonImpl tax = (MutableTaxonImpl) children.get( i );
            result.addAll( Arrays.asList( (MutableTaxonLevelImpl[]) tax.getSubLevels() ) );
        }
        final List<GraphNode> list = new ArrayList<GraphNode>( result );
        Collections.sort( list, RANK_COMPARATOR );
        return list.toArray( new MutableTaxonLevelImpl[list.size()] );
    }

    public CommentedPicture[] getCommentedPictures( final PictureTheme theme )
    {
        final List<MutableCommentedPictureImpl> result = new ArrayList<MutableCommentedPictureImpl>();
        final GraphNodeList texts = getChildren( PictureText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            final GraphNode pic = text.getParents( Picture.class ).get( 0 );
            if ( text.getChildren( PictureTheme.class ).get( 0 ) == theme )
            {
                result.add( new MutableCommentedPictureImpl( this, (Picture) pic, text.getName() ) );
            }
        }
        LOG.debug( "pictures for \"" + this + "\" and theme \"" + theme + "\" are " + result );
        return result.toArray( new MutableCommentedPictureImpl[result.size()] );
    }

    public PictureTheme[] getPictureThemes()
    {
        final List<GraphNode> result = new ArrayList<GraphNode>();
        final GraphNodeList texts = getChildren( PictureText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            result.add( text.getChildren( PictureTheme.class ).get( 0 ) );
        }
        return result.toArray( new MutablePictureThemeImpl[result.size()] );
    }

    public Taxon[] getSiblings()
    {
        final GraphNode parent = getParents( MutableTaxonImpl.class ).get( 0 );
        if ( parent == null )
        {
            return new Taxon[0];
        }
        else
        {
            final GraphNodeList children = parent.getChildren( MutableTaxonImpl.class );
            return (MutableTaxonImpl[]) children.getAll( new MutableTaxonImpl[0] );
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
