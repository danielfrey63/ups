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

    protected static final String TYPE_FACTORY = "xmatrix.input.model.factory.type";

    protected static final String URL = "xmatrix.input.db";

    protected static final String USER = "xmatrix.input.user";

    protected static final String PASSWORD = "xmatrix.input.pw";

    protected static final String DRIVER = "xmatrix.input.driver";

    protected static String MODEL_CLASS = System.getProperty( MODEL );

    protected static String TYPE_FACTORY_CLASS = System.getProperty( TYPE_FACTORY );

    protected static String url = System.getProperty( URL );

    protected static String user = System.getProperty( USER );

    protected static String password = System.getProperty( PASSWORD );

    protected static String driver = System.getProperty( DRIVER );

    private static final String[] CHECK = {url, user, password, driver, MODEL_CLASS};

    private static final String[] CONST = {URL, USER, PASSWORD, DRIVER, MODEL};

    private static final ArrayList<DirtyListener> listeners = new ArrayList<DirtyListener>();

    private static GraphModel model = null;

    private static TypeFactory typeFactory = null;

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

    /**
     * Returns an instance of the configured subclass.
     *
     * @return the graph model
     */
    public static GraphModel getModel()
    {
        if ( model == null )
        {
            LOGGER.trace( "init model by class name " + MODEL_CLASS );
            try
            {
                final Class clazz = Class.forName( MODEL_CLASS );
                model = (GraphModel) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "Could not initiate model: " + MODEL_CLASS;
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
            LOGGER.trace( "init type factory by class name " + TYPE_FACTORY_CLASS );
            try
            {
                final Class clazz = Class.forName( TYPE_FACTORY_CLASS );
                typeFactory = (TypeFactory) clazz.newInstance();
            }
            catch ( Exception ex )
            {
                final String message = "Could not initiate type factory: " + TYPE_FACTORY_CLASS;
                LOGGER.error( message, ex );
                throw new IllegalStateException( message );
            }
        }
        return typeFactory;
    }

    public static GraphNodeList getFiltered( final GraphNodeList list, final Class type )
    {
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
