package com.ethz.geobot.herbar.modeapi;

import com.ethz.geobot.herbar.model.HerbarModel;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * This interface is used by the mode to query and modify the application.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public interface HerbarContext
{
    /**
     * Return the data model of the application.
     *
     * @return reference to the data model
     */
    HerbarModel getDataModel();

    /**
     * Return a data model filtered by a specified filter.
     *
     * @param modelName name of the filter
     * @return reference to the data model
     */
    HerbarModel getModel( String modelName );

    /**
     * Save the the definition of the model.
     *
     * @param model definition of the filter
     */
    void saveModel( HerbarModel model );

    /**
     * remove the specified filter from the persistent storage.
     *
     * @param model name of the filter
     */
    void removeModel( HerbarModel model );

    /**
     * Return a set of all available filters.
     *
     * @return set of String objects
     */
    Set getModelNames();

    Set getModels();

    /**
     * Return a set of all available filters.
     *
     * @return set of String objects
     */
    Set getChangeableModelNames();

    Set getChangeableModels();

    /**
     * Return the GUIManager for the application
     *
     * @return reference to the GUIManager
     */
    HerbarGUIManager getHerbarGUIManager();

    /**
     * Return a property of the application.
     *
     * @param name name of the property
     * @return value of the property
     */
    String getProperty( String name );

    /**
     * Get the root node where the mode should store it persistent data.
     *
     * @return Perferences node to store information
     */
    Preferences getPreferencesNode();

    /**
     * Return a set of all names of available properties.
     *
     * @return set of names
     */
    Set getPropertyNames();

    /**
     * Return current selected model.
     *
     * @return a reference to the model
     */
    HerbarModel getCurrentModel();

    /**
     * Change the current model to the given one
     *
     * @param model reference to the model
     */
    void setCurrentModel( HerbarModel model );
}
