/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.domain.impl;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.api.IFEntryHelper;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Default implementation of an entry helper. This entry helper does provide support for validation of entry
 * associations by means of their types.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a> and <a href="wegmueller@wegmueller.com">Thomas
 *         Wegm&uuml;ller</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:09 $
 */
public final class DefaultEntryHelper implements IFEntryHelper
{
    /**
     * The rules associated with this entry helper.
     */
    private final DefaultEntryBuildingRules rules;

    /**
     * Constructs an entry helper. Only used by the factory method.
     *
     * @param rules the rules associated with this helper
     */
    private DefaultEntryHelper( final DefaultEntryBuildingRules rules )
    {
        this.rules = rules;
    }

    /**
     * Factory method to generate an implementation of the entry helper.
     *
     * @param rules the rules associated with the helper
     * @return the newly constructed helper
     */
    public static IFEntryHelper getInstance( final DefaultEntryBuildingRules rules )
    {
        return new DefaultEntryHelper( rules );
    }

    /**
     * Creates a new entry for the given parent and with the given name and type.
     *
     * @param parent the parent for the new entry or null
     * @param name   the name of the new entry
     * @param type   the type of the new entry
     * @return the new entry
     * @throws ch.jfactory.projecttime.domain.impl.DefaultEntryBuildingRules.RuleViolationException
     *
     */
    public IFEntry createEntry( final IFEntry parent, final String name, final Object type )
    {
        if ( rules.validate( parent, type ) )
        {
            final IFEntry entry;
            if ( parent == null )
            {
                entry = new DefaultEntry( name, type );
            }
            else
            {
                entry = parent.addChild( name, type );
            }
            return entry;
        }
        throw new DefaultEntryBuildingRules.RuleViolationException( parent, type );
    }

    /**
     * Sets the parent for the given child.
     *
     * @param parent the new parent to set
     * @param child  the child to set the parent for
     */
    public void setParent( final IFEntry parent, final IFEntry child )
    {
        final Object type = child.getType();
        if ( rules.validate( parent, type ) )
        {
            child.setParent( parent );
        }
        else
        {
            throw new DefaultEntryBuildingRules.RuleViolationException( parent, type );
        }
    }

    /**
     * Loads the entry array from an xml file configured as system property "persistence.file".
     *
     * @param configuration the configuration properties that must contain a value for "persistence.file"
     * @return the entry array loaded
     */
    public IFEntry[] load( final Properties configuration )
    {
        final String fileName = configuration.getProperty( "persistence.file" );
        IFEntry[] entries = null;
        try
        {
            final XMLDecoder dec = new XMLDecoder( new FileInputStream( fileName ) );
            entries = (IFEntry[]) dec.readObject();
            dec.close();
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * Save the entry array to an xml file configured as system property "persistence.file".
     *
     * @param entries       the entry array to store
     * @param configuration the configuration for saving the array. Must contain a value for "persistence.file"
     */
    public void save( final IFEntry[] entries, final Properties configuration )
    {
        final String fileName = configuration.getProperty( "persistence.file" );
        try
        {
            final XMLEncoder enc = new XMLEncoder( new FileOutputStream( fileName ) );
            enc.writeObject( entries );
            enc.close();
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
    }
}
