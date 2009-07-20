package ch.jfactory.model.graph;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public abstract class AbsGraphModel implements GraphModel {

    private static final Category cat = Category.getInstance(AbsGraphModel.class);

    protected static final String MODEL = "xmatrix.input.model";
    protected static final String TYPE_FACTORY = "xmatrix.input.model.typefactory";
    protected static final String ROLE_FACTORY = "xmatrix.input.model.rolefactory";
    protected static final String URL = "xmatrix.input.db";
    protected static final String USER = "xmatrix.input.user";
    protected static final String PASSWORD = "xmatrix.input.pw";
    protected static final String DRIVER = "xmatrix.input.driver";

    protected static String mod = System.getProperty(MODEL);
    protected static String typeFact = System.getProperty(TYPE_FACTORY);
    protected static String roleFact = System.getProperty(ROLE_FACTORY);
    protected static String url = System.getProperty(URL);
    protected static String user = System.getProperty(USER);
    protected static String password = System.getProperty(PASSWORD);
    protected static String driver = System.getProperty(DRIVER);

    private static final String[] CHECK = {url, user, password, driver, mod};
    private static final String[] CONST = {URL, USER, PASSWORD, DRIVER, MODEL};

    private static ArrayList listeners = new ArrayList();
    private static GraphModel model = null;
    private static TypeFactory typeFactory = null;
    private static RoleFactory roleFactory = null;
    private static boolean dirty;
    private boolean readOnly;

    static {
        for (int i = 0; i < CHECK.length; i++) {
            if (CHECK[i] == null) {
                throw new IllegalStateException("System property \"" + CONST[i] + "\" not specified");
            }
        }
    }

    /**
     * Returns an instance of the configured subclass.
     */
    public static GraphModel getModel() {
        if (model == null) {
            cat.debug("Init model by classname " + mod);
            try {
                final Class clazz = Class.forName(mod);
                model = (GraphModel) clazz.newInstance();
            } catch (Exception ex) {
                final String message = "Could not initiate model: " + mod;
                cat.fatal(message, ex);
                throw new IllegalStateException(message);
            }
        }
        return model;
    }

    public static TypeFactory getTypeFactory() {
        if (typeFactory == null) {
            cat.debug("Init type factory by classname " + typeFact);
            try {
                final Class clazz = Class.forName(typeFact);
                typeFactory = (TypeFactory) clazz.newInstance();
            } catch (Exception ex) {
                final String message = "Could not initiate type factory: " + typeFact;
                cat.fatal(message, ex);
                throw new IllegalStateException(message);
            }
        }
        return typeFactory;
    }

    public static RoleFactory getRoleFactory() {
        if (roleFactory == null) {
            cat.debug("Init type factory by classname " + roleFact);
            try {
                final Class clazz = Class.forName(roleFact);
                roleFactory = (RoleFactory) clazz.newInstance();
            } catch (Exception ex) {
                final String message = "Could not initiate role factory: " + roleFact;
                cat.fatal(message, ex);
                throw new IllegalStateException(message);
            }
        }
        return roleFactory;
    }

    public static GraphNodeList getFiltered(final GraphNodeList list, final Class type) {
        if (type.equals("*")) {
            return list;
        }
        final GraphNodeList result = new GraphNodeList();
        for (int i = 0; i < list.size(); i++) {
            final GraphNode node = (GraphNode) list.get(i);
            if (node.isType(type)) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#addDirtyListener(DirtyListener)
     */
    public void addDirtyListener(final DirtyListener listener) {
        listeners.add(listener);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#removeDirtyListener(DirtyListener)
     */
    public void removeDirtyListener(final DirtyListener listener) {
        listeners.remove(listener);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#setDirty(boolean)
     */
    public void setDirty(final boolean dirty) {
        AbsGraphModel.dirty = dirty;
        fireDirty(dirty);
    }

    /**
     * @see ch.jfactory.model.graph.GraphModel#getDirty()
     */
    public boolean getDirty() {
        return dirty;
    }

    private void fireDirty(final boolean dirty) {
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            final DirtyListener listener = (DirtyListener) iter.next();
            listener.dirtyChanged(dirty);
        }
    }

    public void setReadOnly() {
        this.readOnly = true;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
