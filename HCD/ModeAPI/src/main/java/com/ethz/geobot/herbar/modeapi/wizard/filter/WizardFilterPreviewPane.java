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
package com.ethz.geobot.herbar.modeapi.wizard.filter;

import com.ethz.geobot.herbar.gui.tax.TaxTree;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterPreviewPane extends WizardPane
{
    private static final Logger LOG = LoggerFactory.getLogger( WizardFilterPreviewPane.class );

    /** name of the pane */
    public static final String NAME = "filter.preview";

    private final String filterPropertyName;

    private TaxTree preview;

    public WizardFilterPreviewPane( final String filterPropertyName )
    {
        super( NAME, new String[]{filterPropertyName} );
        this.filterPropertyName = filterPropertyName;
    }

    public void activate()
    {
        preview.setHerbarModel( getFilter() );
    }

    public JPanel createDisplayPanel( final String prefix )
    {
        final JPanel text = createTextPanel( prefix );
        final JPanel title = createDefaultTitlePanel( prefix );
        preview = new TaxTree();
        final JScrollPane scrollPane = new JScrollPane( preview );
        scrollPane.setPreferredSize( new Dimension( 10, 10 ) );

        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add( text, gbc );
        gbc.gridy += 1;
        panel.add( title, gbc );
        gbc.weighty = 1.0;
        gbc.gridy += 1;
        panel.add( scrollPane, gbc );

        return panel;
    }

    private FilterModel getFilter()
    {
        return (FilterModel) getProperty( filterPropertyName );
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( filterPropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                LOG.info( "change preview model" );
                final FilterModel fmodel = (FilterModel) event.getNewValue();
                preview.setHerbarModel( fmodel );
            }
        } );
    }
}
