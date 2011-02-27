package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.MorphologyAttribute;
import com.ethz.geobot.herbar.model.MorphologyValue;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MorphologyValueImpl extends GraphNodeImpl implements MorphologyValue
{
    /**
     * @see com.ethz.geobot.herbar.model.MorphologyValue#getText()
     */
    public String getText()
    {
        throw new NoSuchMethodError( "getUserObject is deprecated" );
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorphologyValue#getTaxa()
     */
    public Taxon[] getTaxa()
    {
        final GraphNodeList parents = getParents( TaxonImpl.class );
        return (TaxonImpl[]) parents.getAll();
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorphologyValue#getTaxon(int)
     */
    public Taxon getTaxon( final int index )
    {
        return getTaxa()[index];
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorphologyValue#getParentAttribute()
     */
    public MorphologyAttribute getParentAttribute()
    {
        final GraphNodeList list = getParents( MorphologyAttributeImpl.class );
        return (MorphologyAttributeImpl) list.get( 0 );
    }

}
