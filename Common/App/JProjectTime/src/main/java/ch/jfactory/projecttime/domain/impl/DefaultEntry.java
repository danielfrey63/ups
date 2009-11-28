/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.domain.impl;

import ch.jfactory.projecttime.domain.api.IFEntry;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The default implementation of the IFEntry interface.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:56:29 $
 */
public class DefaultEntry extends IFEntry
{
    /**
     * The children IFEntry objects array.
     */
    private IFEntry[] children = new IFEntry[0];

    /**
     * The parent IFEntry object.
     */
    private IFEntry parent;

    /**
     * The type of this entry.
     */
    private Object type;

    /**
     * The start time of this entry.
     */
    private Calendar start;

    /**
     * The end time of this entry.
     */
    private Calendar end;

    /**
     * The name or text of this entry.
     */
    private String name;

    /**
     * Default constructor for sole purpose of serialization.
     */
    public DefaultEntry()
    {
        this( "", "" );
    }

    /**
     * Constructor with name and type.
     *
     * @param name the name for this entry
     * @param type the type of this entry
     */
    public DefaultEntry( final String name, final Object type )
    {
        setName( name );
        setType( type );
    }

    /**
     * Sets the children IFEntry objects.
     *
     * @param children the children
     */
    public void setChildren( final IFEntry[] children )
    {
        this.children = children;
    }

    /**
     * Returns the children IFEntry objects.
     *
     * @return the children
     */
    public IFEntry[] getChildren()
    {
        return children;
    }

    /**
     * Factory method to construct a new child with the given type and name. This implementation makes sure that the
     * given new child has its parent updated.
     *
     * @param name the name for the new child
     * @param type the type of the new child
     * @return the new child entry
     */
    public IFEntry addChild( final String name, final Object type )
    {
        // Create new child
        final IFEntry child = new DefaultEntry( name, type );

        // Make sure to add the child to this children
        children = (IFEntry[]) add( children, child, IFEntry.class );

        // Make sure to set the childs parent to this entry
        if ( child.getParent() != this )
        {
            child.setParent( this );
        }

        return child;
    }

    /**
     * Deletes the child from this childrens list.
     *
     * @param entry the child to delete
     */
    public void deleteChild( final IFEntry entry )
    {
        final List list = new ArrayList( Arrays.asList( children ) );
        list.remove( entry );
        children = (IFEntry[]) list.toArray( new IFEntry[0] );
    }

    /**
     * Retruns the end time of this entry.
     *
     * @return the end time
     */
    public Calendar getEnd()
    {
        return end;
    }

    /**
     * Returns the name of this entry.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the parent of this entry.
     *
     * @return the parent
     */
    public IFEntry getParent()
    {
        return parent;
    }

    /**
     * Returns the start time of this entry.
     *
     * @return the start time
     */
    public Calendar getStart()
    {
        return start;
    }

    /**
     * Returns the type of this entry.
     *
     * @return the type
     */
    public Object getType()
    {
        return type;
    }

    /**
     * Sets the end time of this entry.
     *
     * @param end the end time
     */
    public void setEnd( final Calendar end )
    {
        this.end = end;
    }

    /**
     * Sets the name of this entry.
     *
     * @param name the new name
     */
    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * Sets a new parent of this entry. This entry is removed from the old parent and added to the new one.
     *
     * @param parent the new parent to set
     */
    public void setParent( final IFEntry parent )
    {
        if ( this.parent != parent )
        {
            // Set the parent
            final IFEntry oldParent = this.parent;
            this.parent = parent;

            // Make sure the old parent does not contain this entry any more
            if ( oldParent != null && ArrayUtils.contains( oldParent.getChildren(), this ) )
            {
                oldParent.deleteChild( this );
            }

            // Make sure the parent contains this entry
            if ( parent != null && !ArrayUtils.contains( parent.getChildren(), this ) )
            {
                parent.setChildren( (IFEntry[]) add( parent.getChildren(), this, IFEntry.class ) );
            }
        }
    }

    /**
     * Sets the new start time for this entry.
     *
     * @param start the new start time
     */
    public void setStart( final Calendar start )
    {
        this.start = start;
    }

    /**
     * Sets the new type of this entry.
     *
     * @param type the new type
     */
    public void setType( final Object type )
    {
        this.type = type;
    }

    /**
     * Returns a convenient string representation of this entry containing the name, type, start and end time.
     *
     * @return the string representation
     */
    public String toString()
    {
        return new ToStringBuilder( this, ToStringStyle.SIMPLE_STYLE ).append( name ).append( type ).append( start ).append( end ).toString();
    }

    /**
     * Utility method to add another object to an object array.
     *
     * @param array      the array to add the object to
     * @param newObject  the object to add
     * @param arrayClass the type of array to use
     * @return the complete array //todo Move this method either to a new ArrayUtils class or submit it to Apache
     *         commons-lang.
     */
    private Object[] add( final Object[] array, final Object newObject, final Class arrayClass )
    {
        final List list = new ArrayList( ( array == null ? new ArrayList() : Arrays.asList( array ) ) );
        list.add( newObject );
        return list.toArray( (Object[]) Array.newInstance( arrayClass, list.size() ) );
    }
}
