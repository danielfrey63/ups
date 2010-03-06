package com.ethz.geobot.herbar.model.filter;

import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a definition detail.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class FilterDefinitionDetail implements Cloneable
{
    private final static Logger LOG = LoggerFactory.getLogger( FilterDefinitionDetail.class );

    private Taxon scope;

    private Level[] levels;

    private FilterModel model;

    FilterDefinitionDetail( final FilterModel model, final Taxon scope, final Level level )
    {
        this( model, scope, new Level[]{level} );
    }

    FilterDefinitionDetail( final FilterModel model, final Taxon scope, final Level[] levels )
    {
        this.model = model;
        this.scope = scope;
        this.levels = levels;
    }

    public Taxon getScope()
    {
        return scope;
    }

    public void setScope( final Taxon scope )
    {
        this.scope = scope;
        model.notifyModelChange();
    }

    public Level[] getLevels()
    {
        return levels;
    }

    public void setLevels( final Level[] levels )
    {
        this.levels = levels;
        model.notifyModelChange();
    }

    public boolean isIn( final Taxon taxon )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "check taxon: " + taxon );
            LOG.debug( "isChild of scope " + scope + ": " + isChild( taxon ) );
            LOG.debug( "levelcheck " + Arrays.asList( levels ) + ": " + taxon.getLevel() );
        }

        // is scope root ?
        if ( isRootTaxon( taxon ) )
        {
            return true;
        }

        if ( scope.equals( taxon ) || isChild( taxon ) )
        {
            for ( final Level level : levels )
            {
                if ( taxon.getLevel().equals( level ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChild( final Taxon tax )
    {
        Taxon parent = tax.getParentTaxon();
        while ( parent != null )
        {
            if ( scope.equals( parent ) )
            {
                return true;
            }
            parent = parent.getParentTaxon();
        }
        return false;
    }

    private boolean isRootTaxon( final Taxon tax )
    {
        if ( tax.getParentTaxon() == null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException ex )
        {
            LOG.error( "clone isn't supported by cloneable class !?!?!", ex );
            throw new RuntimeException( ex );
        }
    }

    public String toString()
    {
        return scope + " " + Arrays.asList( levels );
    }
}
