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
import org.apache.log4j.Logger;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterPreviewPane extends WizardPane
{
    private static final Logger LOG = Logger.getLogger( WizardFilterPreviewPane.class );

    /**
     * name of the pane
     */
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

    protected JPanel createDisplayPanel( final String prefix )
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
