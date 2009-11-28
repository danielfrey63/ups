/*
 * Level.java
 *
 * Created on
 */
package com.ethz.geobot.herbar.model;

/**
 * Class to represent taxonomic level.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 * @created 2. Mai 2002
 */
public interface Level
{
    /**
     * Returns the name of this Level
     *
     * @return name of this Level
     */
    public String getName();

    /**
     * Returns the id of this Level object, which should be unique in the current applications data model.
     *
     * @return the id of this Level.
     */
    public int getId();

    /**
     * Compares this with another level.
     *
     * @param level object of type Level which should compare
     * @return true if this level is lower than the compared one
     */
    public boolean isLower( Level level );

    /**
     * Compares this with another level.
     *
     * @param level object of type Level which should compare
     * @return true if this level is higher than the compared one
     */
    public boolean isHigher( Level level );

    /**
     * Delivers the next lower Level object or null, if none exists.
     *
     * @return next lower Level object or null.
     */
    public Level getChildLevel();

    /**
     * Delivers the next higher Level object or null, if none exists.
     *
     * @return next higher Level object or null.
     */
    public Level getParentLevel();

    /**
     * Returns all related Taxon objects as an array.
     *
     * @return the related Taxon objects
     */
    public Taxon[] getTaxa();

    /**
     * Returns all levels lower than this level.
     */
    public Level[] getSubLevels();

    /**
     * Returns all levels higher than this level.
     */
    public Level[] getSuperLevels();
}
