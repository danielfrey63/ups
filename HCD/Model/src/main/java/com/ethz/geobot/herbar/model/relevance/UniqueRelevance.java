package com.ethz.geobot.herbar.model.relevance;

import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * This class implements unique assignability of a MorValue to a Taxon object. Returns whether the MorValue is unique in
 * the given Taxon list which means that one of the following (equivalent) assertions are true: <ul> <li>If we meet this
 * MorValue, it is definitely a specific Taxon.</li> <li>Another Taxon may not have this MorValue.</li> </ul> However,
 * it is not guaranteed that the following statement is true in all the cases: <ul> <li>If we meet another MorValue
 * within the same MorAttribute, it is not this Taxon.</li> </ul> Refer to {@link EquivalentRelevance} if you want to
 * guarantee this statement.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class UniqueRelevance extends AbsRelevance
{
    public UniqueRelevance()
    {
        setName( "unique" );
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue val )
    {
        final int siblingsWithThisVal = getTaxa( taxa, val ).length;
        final MorAttribute att = val.getParentAttribute();
        final int siblingsWithThisAtt = getTaxa( taxa, att ).length;
        return siblingsWithThisVal == 1
                && siblingsWithThisAtt == taxa.length
                && taxa.length > 1;
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue, RelevanceMetaData)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue value,
                               final RelevanceMetaData md )
    {
        return md.siblingsWithThisValue == 1
                && md.siblingsWithThisAttribute == taxa.length
                && taxa.length > 1;
    }

}
