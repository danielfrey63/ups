package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorSubject;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutableMorphologySubjectImpl extends MutableGraphNodeImpl implements MorSubject
{
    /**
     * @see MorSubject#getAttributes()
     */
    public MorAttribute[] getAttributes()
    {
        final GraphNodeList list = getChildren( MutableMorphologyAttributeImpl.class );
        return (MutableMorphologyAttributeImpl[]) list.getAll();
    }

    /**
     * @see MorSubject#getAttribute(int)
     */
    public MorAttribute getAttribute( final int index ) throws IndexOutOfBoundsException
    {
        return getAttributes()[index];
    }

    /**
     * @see MorSubject#getSubjects()
     */
    public MorSubject[] getSubjects()
    {
        final GraphNodeList list = getChildren( MutableMorphologySubjectImpl.class );
        return (MutableMorphologySubjectImpl[]) list.getAll();
    }

    /**
     * @see MorSubject#getSubject(int)
     */
    public MorSubject getSubject( final int index ) throws IndexOutOfBoundsException
    {
        return getSubjects()[index];
    }

    /**
     * @see MorSubject#getParentSubject()
     */
    public MorSubject getParentSubject()
    {
        final GraphNodeList parents = getParents( MutableMorphologySubjectImpl.class );
        return (MutableMorphologySubjectImpl) parents.get( 0 );
    }
}
