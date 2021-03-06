/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */

/*
 * UpdateProcessor.java
 *
 * Created on 26. September 2002, 12:37
 */
package ch.jfactory.update;

import ch.jfactory.resource.Strings;
import java.io.IOException;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class update modules.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class UpdateProcessor
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UpdateProcessor.class );

    /** Utility field used by event firing mechanism. */
    private EventListenerList listenerList = null;

    /** Creates a new instance of UpdateProcessor */
    public UpdateProcessor()
    {
    }

    public void process( final List updates ) throws IOException
    {
        LOGGER.info( "starting update process" );
        final String sep = System.getProperty( "line.separator" );

        final UpdateChangeEvent event = new UpdateChangeEvent( this );
        event.setStepsCount( updates.size() );
        event.setStepDescription( Strings.getString( "WIZARD.UPDATE.PROCESS.DESCRIPTION.START", sep ) );

        fireBeginUpdateChangeEvent( event );

        int step = 0;
        for ( final Iterator it = updates.iterator(); it.hasNext(); step++ )
        {
            final UpdateModule module = (UpdateModule) it.next();
            if ( LOGGER.isDebugEnabled() )
            {
                LOGGER.debug( "update module: " + module.getServerVersionInfo().getName() );
            }
            event.setCurrentStep( step );
            String description = module.getServerVersionInfo().getDescription();
            event.setStepDescription( Strings.getString( "WIZARD.UPDATE.PROCESS.DESCRIPTION.TEXT1", description ) );
            fireProgressUpdateChangeEvent( event );
            module.update();
            description = module.getServerVersionInfo().getDescription();
            event.setStepDescription( " " + Strings.getString( "WIZARD.UPDATE.PROCESS.DESCRIPTION.TEXT2", sep ) );
            fireProgressUpdateChangeEvent( event );
        }

        // send complete status
        event.setCurrentStep( step );
        event.setStepDescription( Strings.getString( "WIZARD.UPDATE.PROCESS.DESCRIPTION.STOP", sep ) );
        fireFinishUpdateChangeEvent( event );

        LOGGER.info( "update process finished" );
    }

    /**
     * Registers UpdateChangeListener to receive events.
     *
     * @param listener The listener to register.
     */
    public synchronized void addUpdateChangeListener( final UpdateChangeListener listener )
    {
        if ( listenerList == null )
        {
            listenerList = new EventListenerList();
        }
        listenerList.add( UpdateChangeListener.class, listener );
    }

    /**
     * Removes UpdateChangeListener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeUpdateChangeListener( final UpdateChangeListener listener )
    {
        listenerList.remove( UpdateChangeListener.class, listener );
    }

    protected synchronized void fireProgressUpdateChangeEvent( final UpdateChangeEvent event )
    {
        final EventListener[] listeners = listenerList.getListeners( UpdateChangeListener.class );
        for ( final EventListener listener1 : listeners )
        {
            final UpdateChangeListener listener = (UpdateChangeListener) listener1;
            listener.progressUpdate( event );
        }
    }

    protected synchronized void fireBeginUpdateChangeEvent( final UpdateChangeEvent event )
    {
        final EventListener[] listeners = listenerList.getListeners( UpdateChangeListener.class );
        for ( final EventListener listener1 : listeners )
        {
            final UpdateChangeListener listener = (UpdateChangeListener) listener1;
            listener.beginUpdate( event );
        }
    }

    protected synchronized void fireFinishUpdateChangeEvent( final UpdateChangeEvent event )
    {
        final EventListener[] listeners = listenerList.getListeners( UpdateChangeListener.class );
        for ( final EventListener listener1 : listeners )
        {
            final UpdateChangeListener listener = (UpdateChangeListener) listener1;
            listener.finishUpdate( event );
        }
    }
}
