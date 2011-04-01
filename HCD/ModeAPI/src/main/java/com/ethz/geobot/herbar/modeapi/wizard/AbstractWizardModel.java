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

/*
 * AbstractWizardModel.java
 *
 * Created on 10. Juli 2002, 14:42
 */
package com.ethz.geobot.herbar.modeapi.wizard;

import com.ethz.geobot.herbar.modeapi.HerbarContext;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import javax.swing.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base functionality for WizardModel. Mainly the beans related parts.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
abstract public class AbstractWizardModel implements WizardModel
{
    /** logger instance */
    private static final Logger LOG = LoggerFactory.getLogger( AbstractWizardModel.class );

    protected PropertyChangeSupport propertySupport;

    /** cached BeanInfo information */
    private BeanInfo cachedBeanInfo = null;

    private Action finishAction = null;

    /** herbar context */
    private HerbarContext context;

    /** Used to identify the wizard model per instance. */
    private final String name;

    private final Preferences preferences;

    /**
     * Create a wizard model for modes.
     *
     * @param context the herbar context used in the mode
     * @param name    the name of the model
     */
    public AbstractWizardModel( final HerbarContext context, final String name )
    {
        this.context = context;
        this.preferences = context.getPreferencesNode();
        this.name = name;
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Create a wizard model for non-mode part of the application.
     *
     * @param preferences the preferences node to store information
     * @param name        the name of the model
     */
    public AbstractWizardModel( final Preferences preferences, final String name )
    {
        this.preferences = preferences;
        this.name = name;
        propertySupport = new PropertyChangeSupport( this );
    }

    public void finishWizard()
    {
        if ( finishAction != null )
        {
            finishAction.actionPerformed( new ActionEvent( this, 1, "CloseWizard" ) );
        }
        else
        {
            LOG.warn( "finish wizard request without registered finish wizard handler" );
        }
    }

    public void registerFinishAction( final Action finishAction )
    {
        this.finishAction = finishAction;
    }

    public void addPropertyChangeListener( final PropertyChangeListener listener )
    {
        propertySupport.addPropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( final String propertyName, final PropertyChangeListener listener )
    {
        propertySupport.addPropertyChangeListener( propertyName, listener );
    }

    public void removePropertyChangeListener( final PropertyChangeListener listener )
    {
        propertySupport.removePropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( final String propertyName, final PropertyChangeListener listener )
    {
        propertySupport.removePropertyChangeListener( propertyName, listener );
    }

    public BeanInfo getBeanInfo() throws IntrospectionException
    {
        if ( cachedBeanInfo == null )
        {
            cachedBeanInfo = Introspector.getBeanInfo( this.getClass() );
        }
        return cachedBeanInfo;
    }

    public HerbarContext getHerbarContext()
    {
        if ( context == null )
        {
            throw new IllegalStateException( "wizard model was created for non-mode components. Use getPreferencesNode." );
        }
        return context;
    }

    public Preferences getPreferencesNode()
    {
        return preferences;
    }

    public String getName()
    {
        return name;
    }
}
