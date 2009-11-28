package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 * This class is a default implementation for the WizardModel interface.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class DefaultWizardModel extends AbstractWizardModel
{
    /**
     * Category for logging.
     */
    private static final Logger LOG = Logger.getLogger( DefaultWizardModel.class );

    /**
     * Index of the current pane.
     */
    private int currentPane = 0;

    /**
     * Index of the pane to start with.
     */
    private int startingPane = 0;

    /**
     * contains information about the current states of buttons in a pane
     */
    Map paneButtonStates = new HashMap();

    /**
     * Contains the list of all panes.
     */
    private List paneList;

    /**
     * Contains all state listener
     */
    private Vector wizardStateListenerList;

    /**
     * Creates a default wizard model.
     *
     * @param name the name of the wizard model
     */
    public DefaultWizardModel( final String name )
    {
        this( (Preferences) null, null, name );
    }

    /**
     * Creates a default wizard model for a spceific mode.
     *
     * @param context the modes context. May not be null.
     * @param panes   the panes to display in the wizard. May be null.
     * @param name    the name of the wizard model
     */
    public DefaultWizardModel( final HerbarContext context, final WizardPane[] panes, final String name )
    {
        super( context, name );
        paneList = ( panes == null ? Collections.EMPTY_LIST : Arrays.asList( panes ) );
        init();
        initPaneList();
    }

    /**
     * Creates a default wizard model for non-mode components.
     *
     * @param preferences the preferences node to store and retrieve settings. May not be null.
     * @param panes       the panes to display in the wizard. May be null.
     * @param name        the name of the wizard model
     */
    public DefaultWizardModel( final Preferences preferences, final WizardPane[] panes, final String name )
    {
        super( preferences, name );
        paneList = ( panes == null ? Collections.EMPTY_LIST : Arrays.asList( panes ) );
        init();
        initPaneList();
    }

    public void init()
    {
    }

    private ButtonStates getButtonStates()
    {
        final String paneName = ( (WizardPane) paneList.get( currentPane ) ).getName();
        ButtonStates states = (ButtonStates) paneButtonStates.get( paneName );
        if ( states == null )
        {
            states = new ButtonStates();
            paneButtonStates.put( paneName, states );
        }
        return states;
    }

    public void setNextEnabled( final boolean isNextEnabled )
    {
        getButtonStates().isNextEnabled = isNextEnabled;
        fireInternalState();
    }

    public void setPreviousEnabled( final boolean isPreviousEnabled )
    {
        getButtonStates().isPreviousEnabled = isPreviousEnabled;
        fireInternalState();
    }

    public void setFinishEnabled( final boolean isFinishEnabled )
    {
        getButtonStates().isFinishEnabled = isFinishEnabled;
        fireInternalState();
    }

    public void setCancelEnabled( final boolean isCancelEnabled )
    {
        getButtonStates().isCancelEnabled = isCancelEnabled;
        fireInternalState();
    }

    public WizardPane getPane( final int index )
    {
        currentPane = index;
        fireInternalState();
        return (WizardPane) paneList.get( currentPane );
    }

    public WizardPane getNextPane()
    {
        currentPane++;
        fireInternalState();
        LOG.debug( "next pane index is " + currentPane );
        return (WizardPane) paneList.get( currentPane );
    }

    public WizardPane getPreviousPane()
    {
        currentPane--;
        fireInternalState();
        LOG.debug( "next pane index is " + currentPane );
        return (WizardPane) paneList.get( currentPane );
    }

    public boolean isNextEnabled()
    {
        return getButtonStates().isNextEnabled;
    }

    public boolean isPreviousEnabled()
    {
        return getButtonStates().isPreviousEnabled;
    }

    public boolean isFinishEnabled()
    {
        return getButtonStates().isFinishEnabled;
    }

    public boolean isCancelEnabled()
    {
        return getButtonStates().isCancelEnabled;
    }

    public WizardPane[] getPanes()
    {
        return (WizardPane[]) paneList.toArray( new WizardPane[0] );
    }

    public int getCurrentPaneIndex()
    {
        return currentPane;
    }

    public synchronized void addWizardStateListener( final WizardStateListener listener )
    {
        if ( wizardStateListenerList == null )
        {
            wizardStateListenerList = new Vector();
        }
        wizardStateListenerList.add( listener );
    }

    public synchronized void removeWizardStateListener( final WizardStateListener listener )
    {
        if ( wizardStateListenerList != null )
        {
            wizardStateListenerList.remove( listener );
        }
    }

    public boolean hasNext()
    {
        return currentPane < paneList.size() - 1;
    }

    public String getDialogTitle()
    {
        return Strings.getString( "WIZARD.DEFAULT.TITLE" );
    }

    public boolean hasPrevious()
    {
        return currentPane > 0;
    }

    /**
     * This method fires change information to the registered listener.
     *
     * @param event event information
     */
    protected void fireChange( final WizardStateChangeEvent event )
    {
        final Vector list;
        synchronized ( this )
        {
            if ( wizardStateListenerList == null )
            {
                return;
            }
            list = (Vector) wizardStateListenerList.clone();
        }
        for ( final Object aList : list )
        {
            ( (WizardStateListener) aList ).change( event );
        }
    }

    /**
     * This method inform all panes about there model.
     */
    private void initPaneList()
    {
        for ( final Object aPaneList : paneList )
        {
            final WizardPane pane = (WizardPane) aPaneList;
            pane.init( this );
        }
    }

    /**
     * this method fires the internal state
     */
    final protected void fireInternalState()
    {
        final WizardStateChangeEvent event = new WizardStateChangeEvent( this, hasNext(),
                hasPrevious(), isNextEnabled(), isPreviousEnabled(), isFinishEnabled(), isCancelEnabled() );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "fire internal state change: " + event );
        }

        fireChange( event );
    }

    public void setStart( final int startingPane )
    {
        final Preferences prefs = getPreferencesNode();
        prefs.putInt( getName(), startingPane );
        this.startingPane = startingPane;
    }

    public int getStart()
    {
        final Preferences prefs = getPreferencesNode();
        startingPane = prefs.getInt( getName(), 0 );
        return startingPane;
    }

    static class ButtonStates
    {
        /**
         * Enable state indicator of the next button.
         */
        boolean isNextEnabled = true;

        /**
         * Enable state indicator of the previous button.
         */
        boolean isPreviousEnabled = true;

        /**
         * Enable state indicator of the finish button.
         */
        boolean isFinishEnabled = true;

        /**
         * Enable state indicator of the finish button.
         */
        boolean isCancelEnabled = true;
    }
}
