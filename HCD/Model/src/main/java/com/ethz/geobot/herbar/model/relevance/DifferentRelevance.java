package com.ethz.geobot.herbar.model.relevance;

import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * This class implements a differents of the given value within the given Taxon objects. This means that the quality of
 * this MorValue within the given context meets the following criterium: <ul> <li>There are Values other than the given
 * MorValue (within the same MorAttribute) that occur, and all Taxa have at least one MorValue within the MorAttribute
 * of this MorValue. </ul>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class DifferentRelevance extends AbsRelevance
{
    public DifferentRelevance()
    {
        setName( "different" );
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue val )
    {
        final int siblingsWithThisVal = getTaxa( taxa, val ).length;
        final MorAttribute att = val.getParentAttribute();
        final int siblingsWithThisAtt = getTaxa( taxa, att ).length;
        return siblingsWithThisVal > 1
                && siblingsWithThisVal < taxa.length
                && siblingsWithThisAtt == taxa.length;
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue, RelevanceMetaData)
     */
    public boolean isRelevant( final Taxon[] taxa, final MorValue value,
                               final RelevanceMetaData md )
    {
        return md.siblingsWithThisValue > 1
                && md.siblingsWithThisValue < taxa.length
                && md.siblingsWithThisAttribute == taxa.length;
    }

}
