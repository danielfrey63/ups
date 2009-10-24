package com.ethz.geobot.herbar.model.relevance;

import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * Returns whether the MorValue is weak in the given Taxon list. Not all Taxon objects have to have a MorValue within
 * the same MorAttribute.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class WeakRelevance extends AbsRelevance {

    public WeakRelevance() {
        setName("weak");
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant(Taxon[] taxa, MorValue val) {
        int siblingsWithThisVal = getTaxa(taxa, val).length;
        MorAttribute att = (MorAttribute) val.getParentAttribute();
        int siblingsWithThisAtt = getTaxa(taxa, att).length;
        return siblingsWithThisAtt > 1
                && siblingsWithThisAtt != taxa.length
                && siblingsWithThisAtt != siblingsWithThisVal;
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue, RelevanceMetaData)
     */
    public boolean isRelevant(Taxon[] taxa, MorValue value,
                              RelevanceMetaData md) {
        return md.siblingsWithThisAttribute > 1
                && md.siblingsWithThisAttribute != taxa.length
                && md.siblingsWithThisAttribute != md.siblingsWithThisValue;
    }

}
