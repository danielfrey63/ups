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
package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a default implementation for the WizardModel interface. Call {@link #initPaneList()} after you have constructed the object in subclasses.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class DefaultWizardModel extends AbstractWizardModel
{
    /** Category for logging. */
    private static final Logger LOG = LoggerFactory.getLogger( DefaultWizardModel.class );

    /** Index of the current pane. */
    private int currentPane = 0;

    /** Index of the pane to start with. */
    private int startingPane = 0;

    /** contains information about the current states of buttons in a pane */
    Map<String, ButtonStates> paneButtonStates = new HashMap<String, ButtonStates>();

    /** Contains the list of all panes. */
    private List<WizardPane> paneList;

    /** Contains all state listener */
    private Vector<WizardStateListener> wizardStateListenerList;

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
     * Creates a default wizard model for a specific mode.
     *
     * @param context the modes context. May not be null.
     * @param panes   the panes to display in the wizard. May be null.
     * @param name    the name of the wizard model
     */
    public DefaultWizardModel( final HerbarContext context, final WizardPane[] panes, final String name )
    {
        super( context, name );
        paneList = ( panes == null ? new ArrayList<WizardPane>() : Arrays.asList( panes ) );
        init();
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
        paneList = ( panes == null ? new ArrayList<WizardPane>() : Arrays.asList( panes ) );
        init();
        initPaneList();
    }

    public void init()
    {
    }

    private ButtonStates getButtonStates()
    {
        final String paneName = paneList.get( currentPane ).getName();
        ButtonStates states = paneButtonStates.get( paneName );
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
        return paneList.get( currentPane );
    }

    public WizardPane getNextPane()
    {
        currentPane++;
        fireInternalState();
        LOG.debug( "next pane index is " + currentPane );
        return paneList.get( currentPane );
    }

    public WizardPane getPreviousPane()
    {
        currentPane--;
        fireInternalState();
        LOG.debug( "next pane index is " + currentPane );
        return paneList.get( currentPane );
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
        return paneList.toArray( new WizardPane[paneList.size()] );
    }

    public int getCurrentPaneIndex()
    {
        return currentPane;
    }

    public synchronized void addWizardStateListener( final WizardStateListener listener )
    {
        if ( wizardStateListenerList == null )
        {
            wizardStateListenerList = new Vector<WizardStateListener>();
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

    /** This method inform all panes about there model. */
    protected void initPaneList()
    {
        for ( final Object aPaneList : paneList )
        {
            final WizardPane pane = (WizardPane) aPaneList;
            pane.init( this );
        }
    }

    /** this method fires the internal state */
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
        final Preferences preferences = getPreferencesNode();
        preferences.putInt( getName(), startingPane );
        this.startingPane = startingPane;
    }

    public int getStart()
    {
        final Preferences preferences = getPreferencesNode();
        startingPane = preferences.getInt( getName(), 0 );
        return startingPane;
    }

    static class ButtonStates
    {
        /** Enable state indicator of the next button. */
        boolean isNextEnabled = true;

        /** Enable state indicator of the previous button. */
        boolean isPreviousEnabled = true;

        /** Enable state indicator of the finish button. */
        boolean isFinishEnabled = true;

        /** Enable state indicator of the finish button. */
        boolean isCancelEnabled = true;
    }
}
