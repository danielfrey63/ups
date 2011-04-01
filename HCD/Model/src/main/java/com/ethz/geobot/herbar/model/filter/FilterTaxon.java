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
package com.ethz.geobot.herbar.model.filter;

import ch.jfactory.model.graph.GraphNode;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Taxon class implementing the filter. This class acts as a Proxy for a dependent Taxon. It add the functionality to filter its siblings and levels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class FilterTaxon implements Taxon, Comparable
{
    private static final Logger LOG = LoggerFactory.getLogger( FilterTaxon.class );

    private Taxon dependentTaxon;

    private final FilterModel filterModel;

    private List<Taxon> cachedChildren = null;

    private List<Level> cachedSubLevels = null;

    private Taxon cachedParent = null;

    /**
     * Construct a FilterTaxon
     *
     * @param filterModel    the model with the filter information
     * @param dependentTaxon the taxon on which this FilterTaxon depends
     */
    public FilterTaxon( final FilterModel filterModel, final Taxon dependentTaxon )
    {
        this.filterModel = filterModel;
        this.dependentTaxon = dependentTaxon;
    }

    public int getId()
    {
        return dependentTaxon.getId();
    }

    public Level getLevel()
    {
        return dependentTaxon.getLevel();
    }

    public String getName()
    {
        return dependentTaxon.getName();
    }

    public Taxon getParentTaxon()
    {
        if ( cachedParent == null )
        {
            Taxon parent = dependentTaxon.getParentTaxon();

            while ( parent != null && !filterModel.isIn( parent ) )
            {
                parent = parent.getParentTaxon();
            }
            // TODO: something more intelligent required
            if ( parent != null )
            {
                cachedParent = filterModel.createFilterTaxon( parent );
            }
        }
        return cachedParent;
    }

    public Taxon[] getChildTaxa()
    {
        if ( cachedChildren == null )
        {
            cachedChildren = new ArrayList<Taxon>();
            collectChildren( dependentTaxon, cachedChildren );
        }
        return cachedChildren.toArray( new Taxon[cachedChildren.size()] );
    }

    public Level[] getSubLevels()
    {
        if ( cachedSubLevels == null )
        {
            LOG.trace( "create sub-level list for taxon \"" + this + "\"" );
            cachedSubLevels = new ArrayList<Level>();
            final Level level = getLevel();
            final Level[] levels = dependentTaxon.getSubLevels();
            for ( final Level level1 : levels )
            {
                LOG.trace( "check level: " + level1 );
                final Taxon[] taxList = dependentTaxon.getAllChildTaxa( level1 );
                boolean found = false;
                for ( int t = 0; t < taxList.length && !found; t++ )
                {
                    if ( filterModel.isIn( taxList[t] ) )
                    {
                        found = true;
                    }
                }

                if ( found || level == level1 )
                {
                    LOG.trace( "add level \"" + level1 + "\" as sub-level" );
                    cachedSubLevels.add( level1 );
                }
            }
            // sort levels
            Collections.sort( cachedSubLevels, new LevelComparator() );
        }

        return cachedSubLevels.toArray( new Level[cachedSubLevels.size()] );
    }

    public CommentedPicture[] getCommentedPictures( final PictureTheme theme )
    {
        return dependentTaxon.getCommentedPictures( theme );
    }

    public PictureTheme[] getPictureThemes()
    {
        return dependentTaxon.getPictureThemes();
    }

    public int getRank()
    {
        return dependentTaxon.getRank();
    }

    private void collectChildren( final Taxon tax, final List<Taxon> childList )
    {
        LOG.trace( "collect children for \"" + tax + "\"" );
        final Taxon[] children = tax.getChildTaxa();

        for ( final Taxon child : children )
        {
            if ( filterModel.isIn( child ) )
            {
                // ok, this element is a child
                LOG.trace( "add child to filtered list \"" + child + "\"" );
                childList.add( filterModel.createFilterTaxon( child ) );
            }
            else
            {
                LOG.trace( "collect children for taxon \"" + child + "\"" );
                collectChildren( child, childList );
            }
        }
    }

    public String toDebugString()
    {
        return dependentTaxon.getName() + "[" + dependentTaxon.getId() + "]";
    }

    public GraphNode getAsGraphNode()
    {
        return dependentTaxon.getAsGraphNode();
    }

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

    public Taxon getChildTaxon( final int index )
            throws IndexOutOfBoundsException
    {
        LOG.trace( this.toDebugString() + " getChildTaxon(" + index + ")" );
        return getChildTaxa()[index];
    }

    public int getChildTaxon( final Taxon child )
    {
        for ( int i = 0; i < getChildTaxa().length; i++ )
        {
            if ( getChildTaxon( i ) == child )
            {
                LOG.debug( this.toDebugString() + " getChildTaxon(" + child + ")" + i );
                return i;
            }
        }
        return -1;
    }

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

    public String toString()
    {
        return this.getName();
    }
}
