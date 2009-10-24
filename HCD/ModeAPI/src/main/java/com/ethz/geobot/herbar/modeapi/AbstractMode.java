package com.ethz.geobot.herbar.modeapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a abstract skeleton implementation of the Mode interface. It implements property set/get behaviour and store
 * the context into a instance variable.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
abstract public class AbstractMode implements Mode {

    private HerbarContext context;
    private Map props = new HashMap();

    public void activate() {
    }

    public void deactivate() {
    }

    public boolean queryDeactivate() {
        return true;
    }

    final public Object getProperty(String name) {
        return props.get(name);
    }

    final public Set getPropertyNames() {
        return props.keySet();
    }

    final public void setProperty(String name, Object value) {
        props.put(name, value);
    }

    public void init(HerbarContext context) {
        this.context = context;
        loadState();
    }

    public void destroy() {
        saveState();
    }

    public void loadState() {
    }

    public void saveState() {
    }

    /**
     * Returns the context of the mode.
     *
     * @return reference to the HerbarContext object
     */
    public HerbarContext getHerbarContext() {
        return context;
    }


    @Override
    public String toString() {
        return getProperty(NAME) + " [" + getProperty(MODE_GROUP) + "]";
    }
}
