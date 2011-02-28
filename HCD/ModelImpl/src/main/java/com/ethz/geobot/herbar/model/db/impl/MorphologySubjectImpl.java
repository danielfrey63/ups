package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologySubject;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MorphologySubjectImpl extends GraphNodeImpl implements MorphologySubject
{
    public MorphologyAttribute[] getAttributes()
    {
        final GraphNodeList list = getChildren( MorphologyAttributeImpl.class );
        return (MorphologyAttributeImpl[]) list.getAll();
    }

    public MorphologyAttribute getAttribute( final int index ) throws IndexOutOfBoundsException
    {
        return getAttributes()[index];
    }

    public MorphologySubject[] getSubjects()
    {
        final GraphNodeList list = getChildren( MorphologySubjectImpl.class );
        return (MorphologySubjectImpl[]) list.getAll();
    }

    public MorphologySubject getSubject( final int index ) throws IndexOutOfBoundsException
    {
        return getSubjects()[index];
    }

    public MorphologySubject getParentSubject()
    {
        final GraphNodeList parents = getParents( MorphologySubjectImpl.class );
        return (MorphologySubjectImpl) parents.get( 0 );
    }
}
