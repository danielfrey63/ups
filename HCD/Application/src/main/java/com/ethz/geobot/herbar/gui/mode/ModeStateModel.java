/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui.mode;

import com.ethz.geobot.herbar.modeapi.Mode;
import com.ethz.geobot.herbar.modeapi.state.StateCompositeModel;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the model for the mainframe. It implements StateCompositeModel, so it is able to store its own state ( selected Mode ). It also fires PropertyChangeEvents if a property has changed.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ModeStateModel extends StateCompositeModel
{
    public static final Logger LOG = LoggerFactory.getLogger( ModeStateModel.class.getName() );

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
        final String modeName = node.get( "mode", "LessonMode" );

        try
        {
            setMode( ModeManager.getInstance().getMode( modeName ) );
        }
        catch ( Exception ex )
        {
            LOG.error( "could not load Lesson Mode", ex );
        }

        return node;
    }

    public Preferences storeCompositeState( final Preferences node )
    {
        if ( mode != null )
        {
            node.put( "mode", (String) mode.getProperty( Mode.NAME ) );
        }

        return node;
    }
}
