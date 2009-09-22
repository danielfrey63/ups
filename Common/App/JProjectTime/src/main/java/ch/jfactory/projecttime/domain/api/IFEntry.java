/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.domain.api;

import com.jgoodies.binding.beans.Model;
import java.util.Calendar;

/**
 * Generic interface for entry objects allowing for a composite pattern.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:56:29 $
 */
public abstract class IFEntry extends Model {

    public static final String PROPERTYNAME_TYPE = "type";
    public static final String PROPERTYNAME_NAME = "name";
    public static final String PROPERTYNAME_PARENT = "parent";
    public static final String PROPERTYNAME_START = "start";
    public static final String PROPERTYNAME_END = "end";
    public static final String PROPERTYNAME_CHILDREN = "children";

    /**
     * Sets a new type.
     *
     * @param type the new type
     */
    public abstract void setType(Object type);

    /**
     * Returns the type.
     *
     * @return the type
     */
    public abstract Object getType();

    /**
     * Sets a new name.
     *
     * @param name the name
     */
    public abstract void setName(String name);

    /**
     * Returns the name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Sets a new parent.
     *
     * @param parent the new parent
     */
    public abstract void setParent(IFEntry parent);

    /**
     * Returns the parent.
     *
     * @return the parent
     */
    public abstract IFEntry getParent();

    /**
     * Sets a new start.
     *
     * @param start the new start
     */
    public abstract void setStart(Calendar start);

    /**
     * Returns the start time.
     *
     * @return the start time
     */
    public abstract Calendar getStart();

    /**
     * Sets a new end time.
     *
     * @param end the end time
     */
    public abstract void setEnd(Calendar end);

    /**
     * Returns the end time.
     *
     * @return the end time
     */
    public abstract Calendar getEnd();

    /**
     * Sets the new children array.
     *
     * @param children the new children array
     */
    public abstract void setChildren(IFEntry[] children);

    /**
     * Returns the children array.
     *
     * @return the children array
     */
    public abstract IFEntry[] getChildren();

    /**
     * Deletes a child from this children array.
     *
     * @param entry the child to delete
     */
    public abstract void deleteChild(IFEntry entry);

    /**
     * Factory method to add a new child to this entry.
     *
     * @param name the name of the new child to create
     * @param type the type of the new child to create
     * @return the newly created child entry
     */
    public abstract IFEntry addChild(String name, Object type);
}
