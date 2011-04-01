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
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * WizardPane to display Scope selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardScopePane extends WizardPane
{
    /** name of the pane */
    public final static String NAME = "lesson.scope";

    /** display field for the selected scope */
    private final String modelPropertyName;

    private final String scopePropertyName;

    private EditItem edit;

    public WizardScopePane( final String modelPropertyName, final String scopePropertyName )
    {
        super( NAME, new String[]{modelPropertyName, scopePropertyName} );
        this.modelPropertyName = modelPropertyName;
        this.scopePropertyName = scopePropertyName;
    }

    public JPanel createDisplayPanel( final String prefix )
    {
        final ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed( final ActionEvent ev )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final HerbarModel herbarModel = (HerbarModel) getProperty( modelPropertyName );
                final Taxon rootTaxon = herbarModel.getRootTaxon();
                final TaxTreeDialog dlg = new TaxTreeDialog( (JDialog) getTopLevelAncestor(), rootTaxon );
                dlg.setSelectedTaxon( (Taxon) getProperty( scopePropertyName ) );
                dlg.setSize( 400, 500 );
                dlg.setLocationRelativeTo( WizardScopePane.this );
                dlg.setVisible( true );
                if ( dlg.isAccepted() )
                {
                    final Taxon selectedTaxon = dlg.getSelectedTaxon();
                    if ( selectedTaxon != null )
                    {
                        setProperty( scopePropertyName, selectedTaxon );
                    }
                }
            }
        };
        edit = createDefaultEdit( prefix, actionListener );
        return createSimpleDisplayPanel( prefix, edit );
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( scopePropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                edit.setUserObject( event.getNewValue() );
            }
        } );
    }

    /** This method should be overwritten to set the standard values. */
    public void initDefaultValues()
    {
        // try to set actual scope
        final Object scope = getProperty( scopePropertyName );
        if ( scope != null )
        {
            edit.setUserObject( scope );
        }
    }
}
