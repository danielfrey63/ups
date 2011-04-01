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
package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.application.SystemUtil;
import com.ethz.geobot.herbar.gui.MainFrame;
import com.ethz.geobot.herbar.modeapi.Mode;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * @author $author$
 * @version $revision$ $date$
 */
public class ActionQuit extends AbstractParametrizedAction
{
    public ActionQuit( final MainFrame parent )
    {
        super( "MENU.ITEM.QUIT", parent );

        // overwrite default in super class
        putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F4, KeyEvent.ALT_MASK ) );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        final MainFrame mainFrame = ( (MainFrame) parent );
        mainFrame.storeSettings();
        final Mode mode = mainFrame.getModel().getMode();
        if ( mode != null && mode.queryDeactivate() )
        {
            SystemUtil.EXIT.exit( 0 );
        }
    }
}