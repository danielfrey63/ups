package com.ethz.geobot.herbar.model.origin;

import ch.jfactory.model.graph.RoleNull;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class RoleAssignedImpl extends RoleNull implements RoleAssigned
{
    public RoleAssignedImpl()
    {
        this( "NULLRELEVANTROLE" );
    }

    public RoleAssignedImpl( final String name )
    {
        super( name );
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue value )
    {
        return false;
    }
}
