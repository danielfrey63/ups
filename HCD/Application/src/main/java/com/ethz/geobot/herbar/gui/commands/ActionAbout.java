/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.application.presentation.WindowUtils;
import com.ethz.geobot.herbar.gui.about.AboutBox;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**
 * Action class to show the about box.
 *
 * @author $Author: daniel_frey $ $Date: 2007/09/17 11:05:50 $
 * @version $Revision: 1.1 $
 */
public class ActionAbout extends AbstractParametrizedAction
{
    /**
     * Constructor, need a parent frame to set about box on top.
     *
     * @param parent reference to parent frame
     */
    public ActionAbout( final JFrame parent )
    {
        super( "MENU.ITEM.ABOUT", parent );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        final AboutBox dlg = new AboutBox( parent );
        WindowUtils.centerOnComponent( dlg, parent );
        dlg.setVisible( true );
        dlg.toFront();
    }
}
