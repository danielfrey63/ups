package com.ethz.geobot.herbar.model.filter;

import ch.jfactory.model.graph.GraphNode;
import com.ethz.geobot.herbar.model.AbstractTaxon;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Taxon class implementing the filter. This class acts as a Proxy for a dependent Taxon. It add the functionallity to
 * filter its siblings and levels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class FilterTaxon extends AbstractTaxon
{
    private static final Logger LOG = Logger.getLogger( FilterTaxon.class );

    private Taxon dependentTaxon;

    private final FilterModel filterModel;

    private List cachedChilds = null;

    private List cachedSubLevels = null;

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

    /**
     * set the dependent taxon reference
     *
     * @param dependentTaxon reference to the dependent taxon
     */
    public void setDependentTaxon( final Taxon dependentTaxon )
    {
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
            // TODO: something more intelegent requeired
            if ( parent != null )
            {
                cachedParent = filterModel.createFilterTaxon( parent );
            }
        }
        return cachedParent;
    }

    public Taxon[] getChildTaxa()
    {
        if ( cachedChilds == null )
        {
            cachedChilds = new ArrayList();
            collectChilds( dependentTaxon, cachedChilds );
        }
        return (Taxon[]) cachedChilds.toArray( new Taxon[0] );
    }

    public MorValue[] getMorValues()
    {
        return dependentTaxon.getMorValues();
    }

    public Level[] getSubLevels()
    {
        if ( cachedSubLevels == null )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "create sublevellist for taxon: " + this );
            }
            cachedSubLevels = new ArrayList();
            final Level level = getLevel();
            final Level[] levels = dependentTaxon.getSubLevels();
            for ( final Level level1 : levels )
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "check level: " + level1 );
                }

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
                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "add level " + level1 + " as sublevel" );
                    }
                    cachedSubLevels.add( level1 );
                }
            }
            // sort levels
            Collections.sort( cachedSubLevels );
        }

        return (Level[]) cachedSubLevels.toArray( new Level[0] );
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

    /**
     * get a reference to the dependent taxon object.
     *
     * @return reference to dependent taxon
     */
    public Taxon getDependentTaxon()
    {
        return dependentTaxon;
    }

    private void collectChilds( final Taxon tax, final List childList )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "collect childs for: " + tax );
        }
        final Taxon[] childs = tax.getChildTaxa();

        for ( final Taxon child : childs )
        {
            if ( filterModel.isIn( child ) )
            {
                // ok, this element is a child
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "add child to filtered list: " + child );
                }
                childList.add( filterModel.createFilterTaxon( child ) );
            }
            else
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "collect childs for taxon: " + child );
                }
                collectChilds( child, childList );
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

    public void clearCachedSubLevels()
    {
    }
}
