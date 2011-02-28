/*
 * HerbarModel.java
 *
 * Created on 1.3.2002
 */
package com.ethz.geobot.herbar.model;

import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import com.ethz.geobot.herbar.model.trait.Ecology;
import com.ethz.geobot.herbar.model.trait.Medicine;
import com.ethz.geobot.herbar.model.trait.Morphology;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;

/**
 * abstract representation of the Herbar-data-model.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface HerbarModel
{
    /**
     * Returns the top Level object.
     *
     * @return top Level object
     */
    public Level getRootLevel();

    /**
     * Returns the lowest Level object.
     *
     * @return lowest Level object
     */
    public Level getLastLevel();

    /**
     * return the root taxon
     *
     * @return root-taxon
     */
    public Taxon getRootTaxon();

    /**
     * get the root subject.
     *
     * @return root subject object
     */
    public Morphology getMorphology();

    public Ecology getEcology();

    public Medicine getMedicine();

    /**
     * Return a list of picture themes.
     *
     * @return list of picture themes
     */
    public PictureTheme[] getPictureThemes();

    /**
     * Returns the PictureTheme with the specified name or null if a PictureTheme cannot be found.
     *
     * @param name the name of the PictureTheme searched for
     * @return the PictureTheme object with the given name
     */
    public PictureTheme getPictureTheme( String name );

    /**
     * Returns the Taxon with the specified name or null if a Taxon cannot be found.
     *
     * @param name the name of the Taxon searched for
     * @return the Taxon object with the given name
     */
    public Taxon getTaxon( String name );

    /**
     * Returns all Taxon objects which contain the given Morphology item.
     *
     * @param morphologyValue Morphology item to search for
     * @return Array of Taxon objects
     */
    public Taxon[] getTaxa( MorphologyValue morphologyValue );

    /**
     * Returns the Level with the given name or null if not found.
     *
     * @param name the name of the Level object to be found
     * @return the Level object with the given name, or null if not found
     */
    public Level getLevel( String name );

    /**
     * Returns all Levels from top to bottom.
     *
     * @return all Level objects
     */
    public Level[] getLevels();

    /**
     * return the name of the model
     *
     * @return the name
     */
    public String getName();

    /**
     * register a ModelChangeListener to the model.
     *
     * @param listener the listener object
     */
    public void addModelChangeListener( ModelChangeListener listener );

    /**
     * remove a ModelChangeListener from the model.
     *
     * @param listener the listener object
     */
    public void removeModelChangeListener( ModelChangeListener listener );

    /**
     * Return a Morphology item given by the name.
     *
     * @param name of the item
     */
    public MorphologyValue[] getValues( String name );

    public void setName( String name );

    public void setReadOnly();
}
