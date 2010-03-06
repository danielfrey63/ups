package com.ethz.geobot.herbar.modeapi.wizard.filter;

import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.EditItem;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.lang.ToStringComparator;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 * wizard pane to edit the filter name
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterBasePane extends WizardPane
{
    /**
     * name of the pane
     */
    public static final String NAME = "filter.base";

    private final String basePropertyName;

    private EditItem base;

    public WizardFilterBasePane( final String basePropertyName )
    {
        super( NAME, new String[]{basePropertyName} );
        this.basePropertyName = basePropertyName;
    }

    public void activate()
    {
        base.setUserObject( getProperty( basePropertyName ) );
        base.setEnabled( getWizardModel().getHerbarContext().getModels().size() > 1 );
    }

    public void passivate()
    {
        setProperty( basePropertyName, base.getUserObject() );
    }

    protected JPanel createDisplayPanel( final String prefix )
    {
        base = createBasePanel( prefix );

        final JPanel baseText = createTextPanel( prefix );

        final JPanel panel = new JPanel( new GridBagLayout() );
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add( baseText, gbc );
        gbc.gridy += 1;
        panel.add( base, gbc );
        gbc.gridy += 1;
        gbc.weighty = 1.0;
        panel.add( new JPanel(), gbc );

        return panel;
    }

    private EditItem createBasePanel( final String prefix )
    {
        final ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isVisible() )
                {
                    return;
                }
                final JDialog parent = (JDialog) WizardFilterBasePane.this.getTopLevelAncestor();
                Object[] listContent = getWizardModel().getHerbarContext().getModels().toArray();
                listContent = ArrayUtils.remove( listContent, base.getUserObject(), new Object[0] );
                final Object currentModel = getProperty( FilterWizardModel.FILTER_MODEL );
                listContent = ArrayUtils.remove( listContent, currentModel, new Object[0] );
                Arrays.sort( listContent, new ToStringComparator() );
                final ListDialog dialog = new ListDialog( parent, "DIALOG.BASELISTS", listContent );
                dialog.setSize( 300, 300 );
                dialog.setLocationRelativeTo( WizardFilterBasePane.this );
                dialog.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                dialog.setVisible( true );
                if ( dialog.isAccepted() )
                {
                    final Object newBase = dialog.getSelectedData()[0];
                    base.setUserObject( newBase );
                }
            }
        };
        return createDefaultEdit( prefix, actionListener );
    }

    public void registerPropertyChangeListener( final WizardModel model )
    {
        model.addPropertyChangeListener( basePropertyName, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent event )
            {
                base.setUserObject( event.getNewValue() );
            }
        } );
    }
}
