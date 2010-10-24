package ch.jfactory.model.graph;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public abstract class AbsGraphModel implements GraphModel
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AbsGraphModel.class );

    protected static final String MODEL = "xmatrix.input.model";

    protected static final String TYPE_FACTORY = "xmatrix.input.model.typefactory";

    protected static final String ROLE_FACTORY = "xmatrix.input.model.rolefactory";

    protected static final String URL = "xmatrix.input.db";

    protected static final String USER = "xmatrix.input.user";

    protected static final String PASSWORD = "xmatrix.input.pw";

    protected static final String DRIVER = "xmatrix.input.driver";

    protected static String mod = System.getProperty( MODEL );

    protected static String typeFact = System.getProperty( TYPE_FACTORY );

    protected static String roleFact = System.getProperty( ROLE_FACTORY );

    protected static String url = System.getProperty( URL );

    protected static String user = System.getProperty( USER );

    protected static String password = System.getProperty( PASSWORD );

    protected static String driver = System.getProperty( DRIVER );

    private static final String[] CHECK = {url, user, password, driver, mod};

    private static final String[] CONST = {URL, USER, PASSWORD, DRIVER, MODEL};

    private static final ArrayList listeners = new ArrayList();

    private static GraphModel model = null;

    private static TypeFactory typeFactory = null;

    private static RoleFactory roleFactory = null;

    private static boolean dirty;

    private boolean readOnly;

    static
    {
        for ( int i = 0; i < CHECK.length; i++ )
        {
            if ( CHECK[i] == null )
            {
                throw new IllegalStateException( "System property \"" + CONST[i] + "\" not specified" );
            }
        }
    }

    /** Returns an instance of the configured subclass. */
    public static GraphModel getModel()
    {
        if ( model == null )
        {
            LOGGER.trace( "init model by classname " + mod );
            try
            {
                final Class clazz = Class.forName( mod );
                model = (GraphModel) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "Could not initiate model: " + mod;
                LOGGER.error( message, ex );
                throw new IllegalStateException( message );
            }
        }
        return model;
    }

    public static TypeFactory getTypeFactory()
    {
        if ( typeFactory == null )
        {
            LOGGER.trace( "init type factory by classname " + typeFact );
            try
            {
                final Class clazz = Class.forName( typeFact );
                typeFactory = (TypeFactory) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "Could not initiate type factory: " + typeFact;
                LOGGER.error( message, ex );
                throw new IllegalStateException( message );
            }
        }
        return typeFactory;
    }

    public static RoleFactory getRoleFactory()
    {
        if ( roleFactory == null )
        {
            LOGGER.debug( "Init type factory by classname " + roleFact );
            try
            {
                final Class clazz = Class.forName( roleFact );
                roleFactory = (RoleFactory) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "Could not initiate role factory: " + roleFact;
                LOGGER.error( message, ex );
                throw new IllegalStateException( message );
            }
        }
        return roleFactory;
    }

    public static GraphNodeList getFiltered( final GraphNodeList list, final Class type )
    {
        if ( type.equals( "*" ) )
        {
            return list;
        }
        final GraphNodeList result = new GraphNodeList();
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            if ( node.isType( type ) )
            {
                result.add( node );
            }
        }
        return result;
    }

    /** @see GraphModel#addDirtyListener(DirtyListener) */
    public void addDirtyListener( final DirtyListener listener )
    {
        listeners.add( listener );
    }

    /** @see GraphModel#removeDirtyListener(DirtyListener) */
    public void removeDirtyListener( final DirtyListener listener )
    {
        listeners.remove( listener );
    }

    /** @see GraphModel#setDirty(boolean) */
    public void setDirty( final boolean dirty )
    {
        AbsGraphModel.dirty = dirty;
        fireDirty( dirty );
    }

    /** @see GraphModel#getDirty() */
    public boolean getDirty()
    {
        return dirty;
    }

    private void fireDirty( final boolean dirty )
    {
        for ( final Object listener1 : listeners )
        {
            final DirtyListener listener = (DirtyListener) listener1;
            listener.dirtyChanged( dirty );
        }
    }

    public void setReadOnly()
    {
        this.readOnly = true;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }
}
