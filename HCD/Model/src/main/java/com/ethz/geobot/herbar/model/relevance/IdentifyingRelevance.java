package com.ethz.geobot.herbar.model.relevance;

import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * This class implements equivalence between a Taxon and a MorValue object. This means that a value is equivalent to
 * exacly one taxon within the given Taxon list. A value is equivalent if it identifies a Taxon so that -- in the
 * context of the given Taxon objects -- the following two statements are true: <ul> <li>The value is unique to this
 * Taxon, (same as if we find this MorValue, there is definitively exactly one Taxon associated with it; MorValue =>
 * Taxon).</li> <li>There is no other value within this Taxon (same as if we have another MorValue, it is must be
 * another Taxon; B => A).</li> </ul> If multiple Values are given, it is sufficient that one MorValue is equivalent in
 * the above sense to return true.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class IdentifyingRelevance extends AbsRelevance {

    public IdentifyingRelevance() {
        setName("identifying");
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue)
     */
    public boolean isRelevant(Taxon[] taxa, MorValue value) {
        boolean ret = false;
        Taxon[] taxaWithValue = getTaxa(taxa, value);
        int siblingsWithThisVal = taxaWithValue.length;
        MorAttribute att = (MorAttribute) value.getParentAttribute();
        int siblingsWithThisAtt = getTaxa(taxa, att).length;
        if (siblingsWithThisVal == 1) {
            Taxon taxon = taxaWithValue[ 0 ];
            int valuesWithinThisAtt = getValues(taxon, att).length;
            ret = siblingsWithThisVal == 1
                    && siblingsWithThisAtt == taxa.length
                    && taxa.length > 1
                    && valuesWithinThisAtt == 1;
        }
        return ret;
    }

    /**
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance #isRelevant(Taxon[], MorValue, RelevanceMetaData)
     */
    public boolean isRelevant(Taxon[] taxa, MorValue value,
                              RelevanceMetaData md) {
        boolean ret = false;
        if (md.siblingsWithThisValue == 1) {
            Taxon taxon = md.taxaWithThisValue[ 0 ];
            int valuesWithinThisAtt = getValues(taxon, md.parentAttribute).length;
            ret = md.siblingsWithThisValue == 1
                    && md.siblingsWithThisAttribute == taxa.length
                    && taxa.length > 1
                    && valuesWithinThisAtt == 1;
        }
        return ret;
    }

}
