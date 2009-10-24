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
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public class LevelXBeanInfo implements JXPathBeanInfo {

    private final static Category cat = Category.getInstance(LevelXBeanInfo.class);
    private Map descriptor = new HashMap();

    public LevelXBeanInfo() {
        try {
            PropertyDescriptor des = new PropertyDescriptor("id", com.ethz.geobot.herbar.model.Level.class, "getId", null);
            descriptor.put("id", des);
            des = new PropertyDescriptor("name", com.ethz.geobot.herbar.model.Level.class, "getName", null);
            descriptor.put("name", des);
        }
        catch (java.beans.IntrospectionException ex) {
            cat.error(ex);
        }

    }

    public Class getDynamicPropertyHandlerClass() {
        return null;
    }

    public PropertyDescriptor getPropertyDescriptor(String str) {
        return (PropertyDescriptor) descriptor.get(str);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return (PropertyDescriptor[]) descriptor.values().toArray(new PropertyDescriptor[0]);
    }

    public boolean isAtomic() {
        return false;
    }

    public boolean isDynamic() {
        return false;
    }
}
