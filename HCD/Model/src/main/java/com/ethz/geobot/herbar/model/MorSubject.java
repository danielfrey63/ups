/*
 * MorSubject.java
 *
 * Created on
 */
package com.ethz.geobot.herbar.model;

import ch.jfactory.model.graph.GraphNode;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface MorSubject
{
    /**
     * Returns the id of this Taxon object, which should be unique in the current applications data model.
     *
     * @return the id of this Taxon.
     */
    public int getId();

    /**
     * Return the rank of the taxon.
     *
     * @return rank as integer
     */
    public int getRank();

    /**
     * Returns the name of this MorSubject object.
     *
     * @return the name of this MorSubject.
     */
    public String getName();

    /**
     * Returns an array of MorAttribute objects belonging to this MorSubject.
     *
     * @return an array of MorAttribute objects.
     */
    public MorAttribute[] getAttributes();

    /**
     * Returns MorAttribute object i belonging to this MorSubject.
     *
     * @param index the index of the MorAttribute to get
     * @return an MorAttribute object.
     * @throws IndexOutOfBoundsException if accessing an MorAttribute object outside the MorAttribute objects
     *                                   available.
     */
    public MorAttribute getAttribute( int index )
            throws IndexOutOfBoundsException;

    /**
     * Returns all MorSubject objects belonging to this MorSubject.
     *
     * @return all MorSubject objects.
     */
    public MorSubject[] getSubjects();

    /**
     * Returns MorSubject object i belonging to this MorSubject.
     *
     * @param index the index of the MorSubject to get
     * @return an MorSubject object.
     * @throws IndexOutOfBoundsException if accessing an MorSubject object outside the MorSubject objects available.
     */
    public MorSubject getSubject( int index )
            throws IndexOutOfBoundsException;

    /**
     * Returns the parent MorSubject object belonging to this MorSubject.
     *
     * @return the parent MorSubject objects.
     */
    public MorSubject getParentSubject();

    public GraphNode getAsGraphNode();
}
