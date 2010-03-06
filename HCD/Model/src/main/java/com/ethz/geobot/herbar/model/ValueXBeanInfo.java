/*
 * ValueXBeanInfo.java
 *
 * Created on 19. Juni 2002, 10:59
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
 * @author Dirk
 */
public class ValueXBeanInfo implements JXPathBeanInfo
{
    private final static Logger LOG = LoggerFactory.getLogger( ValueXBeanInfo.class );

    private final Map descriptor = new HashMap();

    public ValueXBeanInfo()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "id", MorValue.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", MorValue.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "text", MorValue.class, "getUserObject", null );
            descriptor.put( "text", des );
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

