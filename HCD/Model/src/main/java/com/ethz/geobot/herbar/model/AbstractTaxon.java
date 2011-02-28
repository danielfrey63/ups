/*
 * AbstractTaxon.java
 *
 * Created on 20. June 2002, 18:15
 */
package com.ethz.geobot.herbar.model;

import ch.jfactory.lang.ArrayUtils;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation for Taxon. Implements standard behaviour.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractTaxon implements Taxon, Comparable
{
    private final static Logger LOG = LoggerFactory.getLogger( AbstractTaxon.class );

    /** @see Taxon#getAllChildTaxa(Level) */
    public Taxon[] getAllChildTaxa( final Level level )
    {
        final List<Taxon> taxa = new ArrayList<Taxon>();
        Taxon child;
        for ( int i = 0; i < getChildTaxa().length; i++ )
        {
            child = getChildTaxon( i );
            final Level childLevel = child.getLevel();
            if ( childLevel == level )
            {
                taxa.add( child );
            }
            else
            {
                final Taxon[] fromChild = child.getAllChildTaxa( level );
                taxa.addAll( Arrays.asList( fromChild ) );
            }
        }
        return taxa.toArray( new Taxon[taxa.size()] );
    }

    /** @see Taxon#getChildTaxa(Level) */
    public Taxon[] getChildTaxa( final Level level )
    {
        final List<Taxon> taxa = new ArrayList<Taxon>();
        Taxon curr;
        for ( int i = 0; i < getChildTaxa().length; i++ )
        {
            curr = getChildTaxon( i );
            if ( curr.getLevel() == level )
            {
                taxa.add( curr );
            }
        }
        final Taxon[] ret = new Taxon[taxa.size()];
        for ( int i = 0; i < taxa.size(); i++ )
        {
            ret[i] = taxa.get( i );
        }
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( this.toDebugString() + " getChildTaxa(" + level + ") " +
                    Arrays.asList( ret ) );
        }
        return ret;
    }

    /** @see Taxon#getChildTaxon(int) */
    public Taxon getChildTaxon( final int index )
            throws IndexOutOfBoundsException
    {
        LOG.trace( this.toDebugString() + " getChildTaxon(" + index + ")" );
        return getChildTaxa()[index];
    }

    /** @see Taxon#getChildTaxon(Taxon) */
    public int getChildTaxon( final Taxon child )
    {
        int i;
        for ( i = 0; i < getChildTaxa().length; i++ )
        {
            if ( getChildTaxon( i ) == child )
            {
                LOG.debug( this.toDebugString() + " getChildTaxon(" + child + ")" + i );
                return i;
            }
        }
        return -1;
    }

    /** @see Taxon#getMorAttributes() */
    public MorphologyAttribute[] getMorAttributes()
    {
        final MorphologyValue[] values = getMorValues();
        final Set<MorphologyAttribute> hs = new HashSet<MorphologyAttribute>();
        for ( final MorphologyValue val : values )
        {
            hs.add( val.getParentAttribute() );
        }
        return hs.toArray( new MorphologyAttribute[hs.size()] );
    }

    /** @see Taxon#getMorValue(int) */
    public MorphologyValue getMorValue( final int index )
    {
        LOG.debug( this.toDebugString() + " getMorValue(" + index + ") " + this.getMorValues()[index] );
        return getMorValues()[index];
    }

    /**
     * @see Taxon#getSiblings()
     */
    /**
     * Returns the siblings of this Taxon, including this Taxon.
     *
     * @return The siblings value
     */
    public Taxon[] getSiblings()
    {
        final Taxon parent = getParentTaxon();
        if ( parent == null )
        {
            return new Taxon[]{this};
        }
        else
        {
            return parent.getChildTaxa();
        }
    }

    /** @see Taxon#getSubLevels() */
    public Level[] getSubLevels()
    {
        return getSubLevels( this ).toArray( new Level[getSubLevels( this ).size()] );
    }

    /** @see Taxon#isIn(Taxon[]) */
    public boolean isIn( final Taxon[] list )
    {
        return ArrayUtils.contains( list, this );
    }

    /** @see Comparable#compareTo(Object) */
    public int compareTo( final Object obj )
    {
        final Taxon taxon = (Taxon) obj;
        return this.getRank() - taxon.getRank();
    }

    public boolean equals( final Object compare )
    {
        if ( !( compare instanceof Taxon ) )
        {
            throw new IllegalArgumentException( "cannot compare object to taxon" );
        }
        final Taxon taxon = (Taxon) compare;
        return taxon == this || getId() == taxon.getId();
    }

    public int hashCode()
    {
        final String id = "" + getId();
        return id.hashCode();
    }

    public abstract String toDebugString();

    /** @see Object#toString() */
    public String toString()
    {
        return this.getName();
    }

    private Set<Level> getSubLevels( final Taxon tax )
    {
        final Set<Level> levels = new HashSet<Level>();
        levels.add( this.getLevel() );
        final Taxon[] children = tax.getChildTaxa();
        for ( final Taxon aChildren : children )
        {
            levels.addAll( getSubLevels( aChildren ) );
        }
        levels.add( tax.getLevel() );
        return levels;
    }
}
