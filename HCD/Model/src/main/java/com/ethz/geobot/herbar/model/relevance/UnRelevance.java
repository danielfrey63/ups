package com.ethz.geobot.herbar.model.relevance;

import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * Returns always true.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class UnRelevance extends AbsRelevance
{
    public UnRelevance()
    {
        setName( "not relevant" );
    }

    /**
     * @see AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue val )
    {
        return true;
    }

    /**
     * @see AbsRelevance #isRelevant(Taxon[], MorValue, RelevanceMetaData)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue value,
                               final RelevanceMetaData metadata )
    {
        return true;
    }

}
