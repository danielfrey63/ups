/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.modeapi;

import com.ethz.geobot.herbar.gui.picture.PictureCache;
import com.ethz.geobot.herbar.model.HerbarModel;
import java.util.Collection;
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
     * Started in the "scientific" environment.
     */
    String ENV_SCIENTIFIC = "scientific";

    /**
     * Started in the "dendrology" environment.
     */
    String ENV_DENDROLOGY = "dendrology";

    /**
     * Return the data model of the application.
     *
     * @return reference to the data model
     */
    HerbarModel getDataModel();

    /**
     * Returns the default model.
     *
     * @return the default model
     */
    HerbarModel getDefaultModel();

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

    Collection<HerbarModel> getModels();

    /**
     * Return a set of all available filters.
     *
     * @return set of String objects
     */
    Collection getChangeableModelNames();

    Collection getChangeableModels();

    /**
     * Return the GUIManager for the application
     *
     * @return reference to the GUIManager
     */
    HerbarGUIManager getHerbarGUIManager();

    /**
     * Get the root node where the mode should store it persistent data.
     *
     * @return Perferences node to store information
     */
    Preferences getPreferencesNode();

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

    PictureCache getMainCache();

    PictureCache getBackgroundCache();

    void setMainCache( PictureCache mainCache );

    void setBackgroundCache( PictureCache backgroundCache );
}
