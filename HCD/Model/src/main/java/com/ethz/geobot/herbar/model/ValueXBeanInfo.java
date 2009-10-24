/*
 * ValueXBeanInfo.java
 *
 * Created on 19. Juni 2002, 10:59
 */

package com.ethz.geobot.herbar.model;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.JXPathBeanInfo;
import org.apache.log4j.Category;

/**
 * @author Dirk
 */
public class ValueXBeanInfo implements JXPathBeanInfo {

    private final static Category cat = Category.getInstance(ValueXBeanInfo.class);
    private Map descriptor = new HashMap();

    public ValueXBeanInfo() {
        try {
            PropertyDescriptor des = new PropertyDescriptor("id", com.ethz.geobot.herbar.model.MorValue.class, "getId", null);
            descriptor.put("id", des);
            des = new PropertyDescriptor("name", com.ethz.geobot.herbar.model.MorValue.class, "getName", null);
            descriptor.put("name", des);
            des = new PropertyDescriptor("text", com.ethz.geobot.herbar.model.MorValue.class, "getUserObject", null);
            descriptor.put("text", des);
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

