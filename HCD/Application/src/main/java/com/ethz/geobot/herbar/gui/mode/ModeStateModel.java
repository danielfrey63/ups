package com.ethz.geobot.herbar.gui.mode;

import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.gui.commands.ActionModeSelection;
import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.state.StateCompositeModel;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;

/**
 * This class is the model for the mainframe. It implements StateCompositeModel, so it is able to store its own state (
 * selected Mode ). It also fires PropertyChangeEvents if a property has changed.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModeStateModel extends StateCompositeModel
{
    private Mode mode;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    private Component viewComponent;

    public void setMode( final Mode mode )
    {
        final Mode oldMode = getMode();
        this.mode = mode;
        propertyChangeSupport.firePropertyChange( "mode", oldMode, mode );
    }

    public void setViewComponent( final Component viewComponent )
    {
        final Component oldViewComponent = getViewComponent();
        this.viewComponent = viewComponent;
        propertyChangeSupport.firePropertyChange( "viewComponent", oldViewComponent, viewComponent );
    }

    public Mode getMode()
    {
        return mode;
    }

    public Component getViewComponent()
    {
        return viewComponent;
    }

    public synchronized void addPropertyChangeListener( final PropertyChangeListener listener )
    {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public synchronized void removePropertyChangeListener( final PropertyChangeListener listener )
    {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

    public Preferences loadCompositeState( final Preferences node )
    {
        final String modeName = node.get( "mode", System.getProperty( "herbar.default.mode.name" ) );

        try
        {
            setMode( ModeManager.getInstance().getMode( modeName ) );
        }
        catch ( Exception ex )
        {
            new ActionModeSelection( AppHerbar.getMainFrame(), node ).actionPerformed( null );
        }

        return node.node( "submodels" );
    }

    public Preferences storeCompositeState( final Preferences node )
    {
        if ( mode != null )
        {
            node.put( "mode", (String) mode.getProperty( Mode.NAME ) );
        }

        return node.node( "submodels" );
    }
}
