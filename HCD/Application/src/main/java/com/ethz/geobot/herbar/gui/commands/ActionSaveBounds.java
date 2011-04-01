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
import com.ethz.geobot.herbar.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.JFrame;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionSaveBounds extends AbstractParametrizedAction
{
    private final Preferences prefNode;

    public ActionSaveBounds( final JFrame frame, final Preferences prefs )
    {
        super( "MENU.ITEM.SAVE.BOUNDS", frame );
        this.prefNode = prefs;
    }

    public void actionPerformed( final ActionEvent e )
    {
        prefNode.putInt( MainFrame.PREF_X, parent.getLocation().x );
        prefNode.putInt( MainFrame.PREF_Y, parent.getLocation().y );
        prefNode.putInt( MainFrame.PREF_W, parent.getSize().width );
        prefNode.putInt( MainFrame.PREF_H, parent.getSize().height );
    }
}
