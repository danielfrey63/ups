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
import com.ethz.geobot.herbar.gui.tax.LevelPopUp;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * WizardPane to display Level selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardLevelPane extends WizardPane
{
    /** name of the pane */
    public final static String NAME = "lesson.level";

    private final String levelPropertyName;

    private final String scopePropertyName;

    private final String modelPropertyName;

    private EditItem edit;

    public WizardLevelPane( final String modelPropertyName, final String scopePropertyName, final String levelPropertyName )
    {
        super( NAME, new String[]{modelPropertyName, scopePropertyName, levelPropertyName} );
        this.modelPropertyName = modelPropertyName;
        this.scopePropertyName = scopePropertyName;
        this.levelPropertyName = levelPropertyName;
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
                final Level[] allLevels = getAllLevels();
                final LevelPopUp popUp = new LevelPopUp( allLevels )
                {
                    public void itemSelected( final Object obj )
                    {
                        setLevel( (Level) obj );
                    }
                };
                popUp.showPopUp( edit, getSubLevels(), getLevel() );
            }
        };
        edit = createDefaultEdit( prefix, actionListener );
        return createSimpleDisplayPanel( prefix, edit );
    }

    private Level getLevel()
    {
        return (Level) getProperty( levelPropertyName );
    }

    private void setLevel( final Level level )
    {
        setProperty( levelPropertyName, level );
    }

    private Level[] getAllLevels()
    {
        return ( (HerbarModel) getProperty( modelPropertyName ) ).getLevels();
    }

    private Level[] getSubLevels()
    {
        return ( (Taxon) getProperty( scopePropertyName ) ).getSubLevels();
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( levelPropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                final Level level = (Level) event.getNewValue();
                edit.setUserObject( level.getName() );
            }
        } );
    }

    /** This method should be overwritten to set the standard values. */
    public void initDefaultValues()
    {
        // try to set actual scope
        final Level level = getLevel();
        if ( level != null )
        {
            edit.setUserObject( level.getName() );
        }
    }
}
