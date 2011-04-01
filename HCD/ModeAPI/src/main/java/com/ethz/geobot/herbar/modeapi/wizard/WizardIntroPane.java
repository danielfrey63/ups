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

    public JPanel createDisplayPanel( final String prefix )
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
