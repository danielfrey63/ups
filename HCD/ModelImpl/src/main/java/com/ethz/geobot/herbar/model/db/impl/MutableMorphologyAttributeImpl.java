package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorValue;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutableMorphologyAttributeImpl extends MutableGraphNodeImpl implements MorAttribute
{
    /**
     * @see com.ethz.geobot.herbar.model.MorAttribute#getParentSubject()
     */
    public MorSubject getParentSubject()
    {
        final GraphNodeList parents = getParents( MutableMorphologySubjectImpl.class );
        return (MutableMorphologySubjectImpl) parents.get( 0 );
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorAttribute#getValue(int)
     */
    public MorValue getValue( final int index ) throws IndexOutOfBoundsException
    {
        return getValues()[index];
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorAttribute#getValues()
     */
    public MorValue[] getValues()
    {
        final GraphNodeList list = getChildren( MutableMorphologyValueImpl.class );
        return (MutableMorphologyValueImpl[]) list.getAll();
    }

}
