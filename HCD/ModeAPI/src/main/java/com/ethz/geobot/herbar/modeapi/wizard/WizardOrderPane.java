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

import ch.jfactory.component.EditItem;
import ch.jfactory.resource.Strings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardOrderPane extends WizardPane
{
    public static final String NAME = "lesson.order";

    private static final String ORDERED = Strings.getString( "WIZARD.LESSON.ORDER.STATE.ORDERED.TEXT" );

    private static final String UNORDERED = Strings.getString( "WIZARD.LESSON.ORDER.STATE.UNORDERED.TEXT" );

    /** name of the pane */
    private final String orderedPropertyName;

    private EditItem edit;

    public WizardOrderPane( final String orderedPropertyName )
    {
        super( NAME, new String[]{orderedPropertyName} );
        this.orderedPropertyName = orderedPropertyName;
    }

    public JPanel createDisplayPanel( final String prefix )
    {
        final ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed( final ActionEvent event )
            {
                if ( !isVisible() )
                {
                    return;
                }
                setOrdered( !getOrdered() );
            }
        };
        edit = createDefaultEdit( prefix, actionListener );
        return createSimpleDisplayPanel( prefix, edit );
    }

    private String getOrderedText( final boolean ordered )
    {
        if ( ordered )
        {
            return ORDERED;
        }
        else
        {
            return UNORDERED;
        }
    }

    private boolean getOrdered()
    {
        return (Boolean) getProperty( orderedPropertyName );
    }

    private void setOrdered( final boolean ordered )
    {
        setProperty( orderedPropertyName, ordered );
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( orderedPropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                final boolean ordered = (Boolean) event.getNewValue();
                edit.setUserObject( getOrderedText( ordered ) );
            }
        } );
    }

    /** This method should be overwritten to set the standard values. */
    public void initDefaultValues()
    {
        // try to set actual scope
        final boolean ordered = getOrdered();
        edit.setUserObject( getOrderedText( ordered ) );
    }
}
