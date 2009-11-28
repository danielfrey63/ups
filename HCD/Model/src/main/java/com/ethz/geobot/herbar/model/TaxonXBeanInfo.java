/*
 * Class.java
 *
 * Created on 14. Juni 2002, 14:06
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
public class TaxonXBeanInfo implements JXPathBeanInfo
{
    private final static Logger LOG = Logger.getLogger( TaxonXBeanInfo.class );

    private final Map descriptor = new HashMap();

    public TaxonXBeanInfo()
    {
        try
        {
            PropertyDescriptor des = new PropertyDescriptor( "taxon", com.ethz.geobot.herbar.model.Taxon.class, "getChildTaxa", null );
            descriptor.put( "taxon", des );
            des = new PropertyDescriptor( "id", com.ethz.geobot.herbar.model.Taxon.class, "getId", null );
            descriptor.put( "id", des );
            des = new PropertyDescriptor( "name", com.ethz.geobot.herbar.model.Taxon.class, "getName", null );
            descriptor.put( "name", des );
            des = new PropertyDescriptor( "level", com.ethz.geobot.herbar.model.Taxon.class, "getLevel", null );
            descriptor.put( "level", des );
            des = new PropertyDescriptor( "subLevels", com.ethz.geobot.herbar.model.Taxon.class, "getSubLevels", null );
            descriptor.put( "subLevels", des );
            des = new PropertyDescriptor( "morAttributes", com.ethz.geobot.herbar.model.Taxon.class, "getMorAttributes", null );
            descriptor.put( "morAttributes", des );
            des = new PropertyDescriptor( "morValues", com.ethz.geobot.herbar.model.Taxon.class, "getMorValues", null );
            descriptor.put( "morValues", des );
        }
        catch ( java.beans.IntrospectionException ex )
        {
            ex.printStackTrace();
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
