package com.ethz.geobot.herbar.modeapi;

import java.util.Set;

/**
 * Interface for implementing a mode. <br/> Lifecycle: <br/> 1. herbar initiate the Mode 2. herbar set the context of
 * the mode by calling init method 3. if herbar activate the mode, activate is called 4. if herbar wants to deactivate
 * the mode it will ask queryDeactivate for permisson 5. if permisson is granted deactivate is called
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public interface Mode {

    /**
     * property name of the mode
     */
    final String NAME = "ModeName";

    /**
     * property name of the major version
     */
    final String MAJOR_VERSION = "MajorVersion";

    /**
     * property name for modus group.
     */
    final String MODE_GROUP = "ModeGroup";

    /**
     * property name of the minor version
     */
    final String MINOR_VERSION = "MinorVersion";

    /**
     * property name of the description
     */
    final String DESCRIPTION = "Description";

    /**
     * property name of the release date
     */
    final String RELEASE_DATE = "ReleaseDate";

    /**
     * property name of the author
     */
    final String AUTHOR = "Author";

    /**
     * property name for the icon getProperty(Mode.ICON) return an Icon
     */
    final String ICON = "Icon";

    /**
     * property name for the icon getProperty(Mode.ICON) return an Icon
     */
    final String DISABLED_ICON = "DisabledIcon";

    /**
     * Is called if herbar activate the mode. This normally means the user has selected the mode.
     */
    void activate();

    /**
     * Is called if herbar deactivate the mode. This means a user has choose anothere mode or close the application.
     */
    void deactivate();

    /**
     * Used to ask the mode, if the mode allowed it's deactivation.
     *
     * @return true deactivate allowed, false deactivate denied
     */
    boolean queryDeactivate();

    /**
     * Return a property value.
     *
     * @param name the name of the property
     * @return the value of the property
     */
    Object getProperty(String name);

    /**
     * set a property value
     *
     * @param name  the name of the property
     * @param value the value of the property
     */
    void setProperty(String name, Object value);

    /**
     * Return a set of all names containing in the property list.
     *
     * @return a set of names
     */
    Set getPropertyNames();

    /**
     * Is called by the application to set the mode context.
     *
     * @param context reference to the context object
     */
    void init(HerbarContext context);

    /**
     * Is called before the application release the mode object.
     */
    void destroy();

    /**
     * tells the mode, that the wizard settings are changed
     */
    void wizardSettingsFinish();

    /**
     * Allows for initialization of wizrad setting by the mode.
     */
    void wizardSettingsInit();
}
