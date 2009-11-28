/*
 * AttributeXBeanInfo.java
 *
 * Created on 19. Juni 2002, 10:54
 */
package com.ethz.geobot.herbar.model;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.JXPathBeanInfo;
import org.apache.log4j.Logger;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class AttributeXBeanInfo implements JXPathBeanInfo
{
    private final static Logger LOG = Logger.getLogger( AttributeXBeanInfo.class );

    private final Map descriptor = new HashMap();

    public AttributeXBeanInfo()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "id", com.ethz.geobot.herbar.model.MorAttribute.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", com.ethz.geobot.herbar.model.MorAttribute.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "values", com.ethz.geobot.herbar.model.MorAttribute.class, "getValues", null );
            descriptor.put( "values", des );
        }
        catch ( java.beans.IntrospectionException ex )
        {
            LOG.error( ex );
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
