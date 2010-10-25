package com.ethz.geobot.herbar.modeapi.wizard;

import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:11 $
 */
public class WizardIntroPane extends WizardPane
{
    private JCheckBox check;

    public WizardIntroPane( final String name )
    {
        super( name, new String[]{} );
    }

    public void activate()
    {
        check.setSelected( ( getWizardModel().getStart() == 0 ) );
    }

    public void deactivate()
    {
        getWizardModel().setStart( ( check.isSelected() ? 0 : 1 ) );
    }

    protected JPanel createDisplayPanel( final String prefix )
    {
        final JPanel panel = new JPanel( new BorderLayout() );
        final JScrollPane scrollPane = createIntroArea( prefix );

        check = new JCheckBox( Strings.getString( prefix + ".CHECK.TEXT" ) );
        check.setSelected( true );

        panel.add( scrollPane, BorderLayout.CENTER );
        panel.add( check, BorderLayout.SOUTH );

        return panel;
    }
}
