package com.ethz.geobot.herbar.modeapi.wizard.filter;

import ch.jfactory.component.DynamicDocument;
import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.component.TextEditItem;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * wizard pane to edit the filter name
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterNamePane extends WizardPane
{
    /**
     * name of the pane
     */
    public static final String NAME = "filter.name";

    private final String namePropertyName;

    private TextEditItem name;

    private JLabel warn;

    public WizardFilterNamePane( final String namePropertyName )
    {
        super( NAME, new String[]{namePropertyName} );
        this.namePropertyName = namePropertyName;
    }

    public void activate()
    {
        name.setText( getProperty( namePropertyName ) );
        final FilterWizardModel wizardModel = (FilterWizardModel) getWizardModel();
        final boolean valid = wizardModel.isValidName( name.getText() );
        getWizardModel().setFinishEnabled( valid );
        getWizardModel().setNextEnabled( valid );
        getWizardModel().setPreviousEnabled( valid );
    }

    public void passivate()
    {
        setProperty( namePropertyName, name.getText() );
    }

    protected JPanel createDisplayPanel( final String prefix )
    {
        name = createNamePanel( prefix );
        warn = createWarnLabel( prefix );

        final JPanel nameText = createTextPanel( prefix );

        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add( nameText, gbc );
        gbc.gridy += 1;
        panel.add( name, gbc );
        gbc.gridy += 1;
        panel.add( warn, gbc );
        gbc.gridy += 1;
        gbc.weighty = 1.0;
        panel.add( new JPanel(), gbc );

        return panel;
    }

    private JLabel createWarnLabel( final String prefix )
    {
        final JLabel warn;
        warn = new JLabel( Strings.getString( prefix + ".WARN.TEXT" ) );
        warn.setFont( warn.getFont().deriveFont( Font.BOLD ) );
        warn.setForeground( Color.orange );
        warn.setVisible( false );
        return warn;
    }

    private TextEditItem createNamePanel( final String prefix )
    {
        final TextEditItem name;
        final DocumentListener namesDocumentListener = new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                final Document d = e.getDocument();
                String newName = null;
                try
                {
                    newName = d.getText( 0, d.getLength() );
                }
                catch ( Exception x )
                {
                }
                final FilterWizardModel wizardModel = (FilterWizardModel) getWizardModel();
                final boolean valid = wizardModel.isValidName( newName );
                wizardModel.setFinishEnabled( valid );
                wizardModel.setNextEnabled( valid );
                wizardModel.setPreviousEnabled( valid );
                warn.setVisible( !valid );
            }
        };
        final DynamicDocument document = new DynamicDocument( Strings.getString( "WIZARD.FILTER.NAME.VALIDINPUT" ) );
        document.addDocumentListener( namesDocumentListener );
        name = createDefaultTextEdit( prefix, null, null );
        name.getTextField().setDocument( document );
        return name;
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( namePropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                name.setText( event.getNewValue() );
            }
        } );
    }
}
