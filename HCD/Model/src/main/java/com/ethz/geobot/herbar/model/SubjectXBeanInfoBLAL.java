/*
 * SubjectXBeanInfoBLAL.java
 *
 * Created on 19. Juni 2002, 10:49
 */
package com.ethz.geobot.herbar.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.JXPathBeanInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class SubjectXBeanInfoBLAL implements JXPathBeanInfo
{
    private final static Logger LOG = LoggerFactory.getLogger( SubjectXBeanInfoBLAL.class );

    private final Map descriptor = new HashMap();

    public SubjectXBeanInfoBLAL()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "id", MorSubject.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", MorSubject.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "attributes", MorSubject.class, "getAttributes", null );
            descriptor.put( "attributes", des );
            des = new PropertyDescriptor( "subjects", MorSubject.class, "getSubjects", null );
            descriptor.put( "subjects", des );
        }
        catch ( IntrospectionException ex )
        {
            LOG.error( ex.getMessage(), ex );
        }
    }

    public Class getDynamicPropertyHandlerClass()
    {
        return null;
    }

    public PropertyDescriptor getPropertyDescriptor( final String str )
    {
        return (PropertyDescriptor) descriptor.get( str );
    }

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return (PropertyDescriptor[]) descriptor.values().toArray( new PropertyDescriptor[0] );
    }

    public boolean isAtomic()
    {
        return false;
    }

    public boolean isDynamic()
    {
        return false;
    }
}
