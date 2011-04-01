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

import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.EditItem;
import ch.jfactory.lang.ToStringComparator;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 * WizardPane to display Focus selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardFocusPane extends WizardPane
{
    public static final String NAME = "lesson.focus";

    /** name of the pane */
    EditItem edit;

    private final String taxListPropertyName;

    private final String focusPropertyName;

    public WizardFocusPane( final String taxListPropertyName, final String focusPropertyName )
    {
        super( NAME, new String[]{taxListPropertyName, focusPropertyName} );
        this.taxListPropertyName = taxListPropertyName;
        this.focusPropertyName = focusPropertyName;
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
                final Taxon[] list = getTaxList();
                Arrays.sort( list, new ToStringComparator<Taxon>() );
                final ListDialog dialog = new ListDialog( (JDialog) getTopLevelAncestor(), "DIALOG.FOCUS", list );
                dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                dialog.setSize( 300, 300 );
                dialog.setLocationRelativeTo( WizardFocusPane.this );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    setFocus( (Taxon) dialog.getSelectedData()[0] );
                }
            }
        };
        edit = createDefaultEdit( prefix, actionListener );
        return createSimpleDisplayPanel( prefix, edit );
    }

    private Taxon getFocus()
    {
        return (Taxon) getProperty( focusPropertyName );
    }

    private void setFocus( final Taxon level )
    {
        setProperty( focusPropertyName, level );
    }

    private Taxon[] getTaxList()
    {
        return (Taxon[]) getProperty( taxListPropertyName );
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( focusPropertyName,
                new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent event )
                    {
                        final Taxon taxon = (Taxon) event.getNewValue();
                        edit.setUserObject( taxon.getName() );
                    }
                } );
    }

    /** This method should be overwritten to set the standard values. */
    public void initDefaultValues()
    {
        // try to set actual scope
        final Taxon focus = getFocus();
        if ( focus != null )
        {
            edit.setUserObject( focus.getName() );
        }
    }
}
