/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.domain.api;

import java.util.Properties;

/**
 * Helper class to chech for rule fulfillment wich convenient save and load methods.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:09 $
 */
public interface IFEntryHelper
{
    /**
     * Factory method to construct a new entry.
     *
     * @param parent the parent entry to associate with
     * @param name   the name of the new entry
     * @param type   the type of the new entry
     * @return the newly created entry
     */
    IFEntry createEntry( IFEntry parent, String name, Object type );

    /**
     * Helper method to set the parent for a given child. Make sure the old parent does not contain the given child
     * anymore and that the new parent contains the given child.
     *
     * @param parent the parent to set
     * @param child  the child to add
     */
    void setParent( IFEntry parent, IFEntry child );

    /**
     * Saves the given entry array to the persistence layer configures with the given properties.
     *
     * @param entries       the entry array to save
     * @param configuration the configuration for the persistence layer
     */
    void save( IFEntry[] entries, Properties configuration );

    /**
     * Loads an entry array from the persistence layer with the given configuration.
     *
     * @param configuration the configuration for the persistence layer
     * @return the entry array
     */
    IFEntry[] load( Properties configuration );
}
