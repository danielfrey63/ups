package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Arrays;
import java.util.List;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutableMorphologyValueImpl extends MutableGraphNodeImpl implements MorValue {

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getText()
     */
    public String getText() {
        throw new NoSuchMethodError("getUserObject is depracated");
    }

    public MutableTextImpl[] getTexts() {
        Object[] result = getChildren(MutableTextImpl.class).getAll();
        List list = Arrays.asList(result);
        return (MutableTextImpl[]) list.toArray(new MutableTextImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getTaxa()
     */
    public Taxon[] getTaxa() {
        GraphNodeList parents = getParents(MutableTaxonImpl.class);
        return (MutableTaxonImpl[]) parents.getAll();
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getTaxon(int)
     */
    public Taxon getTaxon(int index) {
        return getTaxa()[ index ];
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorValue#getParentAttribute()
     */
    public MorAttribute getParentAttribute() {
        GraphNodeList atts = getParents(MutableMorphologyAttributeImpl.class);
        return (MutableMorphologyAttributeImpl) atts.get(0);
    }

}
