/*
 * AttributeXBeanInfo.java
 *
 * Created on 19. Juni 2002, 10:54
 */
package com.ethz.geobot.herbar.model;

import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
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
public class AttributeXBeanInfo implements JXPathBeanInfo
{
    private final static Logger LOG = LoggerFactory.getLogger( AttributeXBeanInfo.class );

    private final Map<String, PropertyDescriptor> descriptor = new HashMap<String, PropertyDescriptor>();

    public AttributeXBeanInfo()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "id", MorphologyAttribute.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", MorphologyAttribute.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "values", MorphologyAttribute.class, "getValues", null );
            descriptor.put( "values", des );
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
        return descriptor.get( str );
    }

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return descriptor.values().toArray( new PropertyDescriptor[descriptor.values().size()] );
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
