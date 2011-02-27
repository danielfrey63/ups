package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.MorphologyAttribute;
import com.ethz.geobot.herbar.model.MorphologySubject;
import com.ethz.geobot.herbar.model.MorphologyValue;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MorphologyAttributeImpl extends GraphNodeImpl implements MorphologyAttribute
{
    /**
     * @see com.ethz.geobot.herbar.model.MorphologyAttribute#getParentSubject()
     */
    public MorphologySubject getParentSubject()
    {
        final GraphNodeList parents = getParents( MorphologySubjectImpl.class );
        return (MorphologySubjectImpl) parents.get( 0 );
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorphologyAttribute#getValue(int)
     */
    public MorphologyValue getValue( final int index ) throws IndexOutOfBoundsException
    {
        return getValues()[index];
    }

    /**
     * @see com.ethz.geobot.herbar.model.MorphologyAttribute#getValues()
     */
    public MorphologyValue[] getValues()
    {
        final GraphNodeList list = getChildren( MorphologyValueImpl.class );
        return (MorphologyValueImpl[]) list.getAll();
    }

}
