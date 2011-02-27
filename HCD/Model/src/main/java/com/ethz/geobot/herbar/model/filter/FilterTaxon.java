package com.ethz.geobot.herbar.model.filter;

import ch.jfactory.model.graph.GraphNode;
import com.ethz.geobot.herbar.model.AbstractTaxon;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.MorphologyValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Taxon class implementing the filter. This class acts as a Proxy for a dependent Taxon. It add the functionality to
 * filter its siblings and levels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class FilterTaxon extends AbstractTaxon
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

    public void setScore( final boolean right )
    {
        dependentTaxon.setScore( right );
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

    public MorphologyValue[] getMorValues()
    {
        return dependentTaxon.getMorValues();
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

    public double getScore()
    {
        return dependentTaxon.getScore();
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
}
