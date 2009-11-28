/*
 * PictureXBeanInfo.java
 *
 * Created on 19. Juni 2002, 11:26
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
public class PictureXBeanInfo implements JXPathBeanInfo
{
    private final static Logger LOG = Logger.getLogger( PictureXBeanInfo.class );

    private final Map descriptor = new HashMap();

    public PictureXBeanInfo()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "id", com.ethz.geobot.herbar.model.Picture.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", com.ethz.geobot.herbar.model.Picture.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "relativURL", com.ethz.geobot.herbar.model.Picture.class, "getRelativURL", null );
            descriptor.put( "relativURL", des );
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
